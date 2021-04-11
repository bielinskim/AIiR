/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webproxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.logging.Level;

import javaxt.io.Image;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpProxyServerBootstrap;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LittleProxy {

    Logger logger = LoggerFactory.getLogger(LittleProxy.class);

    public static void main(String[] args) {
        LittleProxy littleProxy = new LittleProxy();
        littleProxy.startProxyServer();
    }

    private void startProxyServer() {
        HttpProxyServer server
                = DefaultHttpProxyServer.bootstrap()
                .withPort(1080)
                .withFiltersSource(new HttpFiltersSourceAdapter() {
                                       public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
                                           return new HttpFiltersAdapter(originalRequest) {
                                               @Override
                                               public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                                                   if (httpObject instanceof FullHttpRequest) {
                                                       FullHttpRequest request = (FullHttpRequest) httpObject;
                                                       String modified = request.headers().get(HttpHeaders.Names.IF_MODIFIED_SINCE);
                                                       if (StringUtils.isNoneBlank(modified)) {
                                                           logger.info("Not modified");
                                                           HttpResponse newResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_MODIFIED);
                                                           return newResponse;
                                                       }
                                                   }
                                                   return null;
                                               }

                                               @Override
                                               public HttpObject serverToProxyResponse(HttpObject httpObject) {
                                                   // logger.info("serverToProxyResponse: "+httpObject);

                                                   return httpObject;
                                               }

                                               @Override
                                               public HttpObject proxyToClientResponse(HttpObject httpObject) {
                                                   //logger.info("proxyToClientResponse: " + httpObject);
                                                   if (httpObject instanceof FullHttpResponse) {
                                                       //logger.info("FullHttpResponse: ");
                                                       FullHttpResponse response = (FullHttpResponse) httpObject;
                                                       if (response.getStatus().code() != 200) {
                                                           return httpObject;
                                                       }
                                                       ByteBuf buffer = response.content();

                                                       try {
                                                           String fileName = URLEncoder.encode(this.originalRequest.getUri(), "UTF-8");
                                                           fileName = fileName.replaceAll("\\*", "%2A");
                                                           if (fileName.length() > 255)
                                                               fileName = fileName.substring(0, 255);

                                                           File proxyDir = new File("proxy/");
                                                           proxyDir.mkdirs();
                                                           File proxyFile = new File("proxy/" + fileName);
                                                           proxyFile.createNewFile();
                                                           logger.info("Path: " + proxyFile.getAbsolutePath());
                                                           FileOutputStream fo = new FileOutputStream(proxyFile, false);

                                                           buffer.getBytes(0, fo, buffer.readableBytes());
                                                           //String original = URLDecoder.decode(fileName, "UTF-8");
                                                           fo.close();
                                                       } catch (Exception ex) {
                                                           logger.error("Error during writing proxied file", ex);
                                                       }

                                                       String contentType = response.headers().get(HttpHeaders.Names.CONTENT_TYPE);
                                                       logger.info("contentType: " + contentType);
                                                       if (contentType.contains("text/html")) {
                                                           //buffer.g

                                                           String html = String.valueOf(buffer.toString(Charset.forName("UTF-8")));
                                                           Document doc = Jsoup.parse(html);

                                                           Element title = doc.select("title").first();
                                                           title.prepend("Proxied: ");

                                                           Element body = doc.select("body").first();
                                                           body.append("<p>Strona zmodyfikowana przez Mateusza Bielińskiego</p>");

                                                           buffer.clear();
                                                           byte[] docBytes = doc.html().getBytes(Charset.forName("UTF-8"));
                                                           buffer.ensureWritable(docBytes.length);
                                                           buffer.writeBytes(docBytes);
                                                           //logger.info();
                                                           HttpResponse newResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
                                                           HttpHeaders.setContentLength(newResponse, buffer.readableBytes());
                                                           HttpHeaders.setHeader(newResponse, HttpHeaders.Names.CONTENT_TYPE, "text/html");
                                                           return newResponse;
                                                       }

                                                       if (contentType.contains("image/jpeg")) {
                                                           byte[] imgBytes = new byte[buffer.readableBytes()];
                                                           buffer.getBytes(0, imgBytes);

                                                           Image img = new Image(imgBytes);
                                                           img.rotate(180);
                                                           imgBytes = img.getByteArray();
                                                           buffer.clear();
                                                           buffer.ensureWritable(imgBytes.length);
                                                           buffer.writeBytes(imgBytes);
                                                           HttpResponse newResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
                                                           HttpHeaders.setContentLength(newResponse, buffer.readableBytes());
                                                           HttpHeaders.setHeader(newResponse, HttpHeaders.Names.CONTENT_TYPE, "image/jpeg");
                                                           return newResponse;
                                                       }

                                                   }
                                                   return httpObject;
                                               }

                                           };
                                       }

                                       @Override
                                       public int getMaximumRequestBufferSizeInBytes() {
                                           return 10 * 1024 * 1024;
                                       }

                                       @Override
                                       public int getMaximumResponseBufferSizeInBytes() {
                                           return 10 * 1024 * 1024;
                                       }

                                   }
                ).start();
    }
}
