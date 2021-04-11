/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webproxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;

/**
 *
 * @author Marcin
 */
public class AdvancedProxyThread extends ProxyThread {

    public AdvancedProxyThread(Socket s) {
        super(s);
    }

    public AdvancedProxyThread(Socket s, String proxy, int port) {
        super(s, proxy, port);
    }
    
    public void run()
	{
		try
		{
			long startTime = System.currentTimeMillis();
			
			// client streams (make sure you're using streams that use
			// byte arrays, so things like GIF and JPEG files and file
			// downloads will transfer properly)
			BufferedInputStream clientIn = new BufferedInputStream(pSocket.getInputStream());
			BufferedOutputStream clientOut = new BufferedOutputStream(pSocket.getOutputStream());
			
			// the socket to the remote server
			Socket server = null;
			
			// other variables
			byte[] request = null;
			byte[] response = null;
			int requestLength = 0;
			int responseLength = 0;
			int pos = -1;
			StringBuffer host = new StringBuffer("");
			String hostName = "";
			int hostPort = 80;
			
			// request - żądanie z przeglądarki do serwera proxy
                        // get the header info (the web browser won't disconnect after
			// it's sent a request, so make sure the waitForDisconnect
			// parameter is false)
			request = getHTTPData(clientIn, host, false);
			requestLength = Array.getLength(request);
			
			// separate the host name from the host port, if necessary
			// (like if it's "servername:8000")
			hostName = host.toString();
			pos = hostName.indexOf(":");
			if (pos > 0)
			{
				try { hostPort = Integer.parseInt(hostName.substring(pos + 1)); 
					}  catch (Exception e)  { }
				hostName = hostName.substring(0, pos);
			}
			
			// send request to the Host
			try
			{
                            server = new Socket(hostName, hostPort);
				
			}  catch (Exception e)  {
				// tell the client there was an error
				String errMsg = "HTTP/1.0 500\nContent Type: text/plain\n\n" + 
								"Error connecting to the server:\n" + e + "\n";
				clientOut.write(errMsg.getBytes(), 0, errMsg.length());
			}
			
			if (server != null)
			{
				server.setSoTimeout(socketTimeout);
				BufferedInputStream serverIn = new BufferedInputStream(server.getInputStream());
				BufferedOutputStream serverOut = new BufferedOutputStream(server.getOutputStream());
				
				// send the request out
				serverOut.write(request, 0, requestLength);
				serverOut.flush();
				
				// and get the response; if we're not at a debug level that
				// requires us to return the data in the response, just stream
				// it back to the client to save ourselves from having to
				// create and destroy an unnecessary byte array. Also, we
				// should set the waitForDisconnect parameter to 'true',
				// because some servers (like Google) don't always set the
				// Content-Length header field, so we have to listen until
				// they decide to disconnect (or the connection times out).
				if (debugLevel > 1)
				{
					response = getHTTPData(serverIn, true);
					responseLength = Array.getLength(response);
				}  else  {
					responseLength = streamHTTPData(serverIn, clientOut, true);
				}
				
				serverIn.close();
				serverOut.close();
			}
			
			// send the response back to the client, if we haven't already
			if (debugLevel > 1)
				clientOut.write(response, 0, responseLength);
			
			// if the user wants debug info, send them debug info; however,
			// keep in mind that because we're using threads, the output won't
			// necessarily be synchronous
			if (debugLevel > 0)
			{
				long endTime = System.currentTimeMillis();
				debugOut.println("Request from " + pSocket.getInetAddress().getHostAddress() + 
									" on Port " + pSocket.getLocalPort() + 
									" to host " + hostName + ":" + hostPort + 
									"\n  (" + requestLength + " bytes sent, " + 
									responseLength + " bytes returned, " + 
									Long.toString(endTime - startTime) + " ms elapsed)");
				debugOut.flush();
			}
			if (debugLevel > 1)
			{
				debugOut.println("REQUEST:\n" + (new String(request)));
				debugOut.println("RESPONSE:\n" + (new String(response)));
				debugOut.flush();
			}
			
			// close all the client streams so we can listen again
			clientOut.close();
			clientIn.close();
			pSocket.close();
		}  catch (Exception e)  {
			if (debugLevel > 0)
				debugOut.println("Error in ProxyThread: " + e);
			//e.printStackTrace();
		}

	}
}
