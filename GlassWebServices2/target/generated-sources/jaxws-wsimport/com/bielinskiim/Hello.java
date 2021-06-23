
package com.bielinskiim;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "hello", targetNamespace = "http://bielinskiim.com/", wsdlLocation = "http://localhost:8080/GlassWebServices2/hello?wsdl")
public class Hello
    extends javax.xml.ws.Service
{

    private final static URL HELLO_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.bielinskiim.Hello.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.bielinskiim.Hello.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/GlassWebServices2/hello?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/GlassWebServices2/hello?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        HELLO_WSDL_LOCATION = url;
    }

    public Hello(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Hello() {
        super(HELLO_WSDL_LOCATION, new QName("http://bielinskiim.com/", "hello"));
    }

    /**
     * 
     * @return
     *     returns Service
     */
    @WebEndpoint(name = "servicePort")
    public com.bielinskiim.Service getServicePort() {
        return super.getPort(new QName("http://bielinskiim.com/", "servicePort"), Service.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Service
     */
    @WebEndpoint(name = "servicePort")
    public com.bielinskiim.Service getServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://bielinskiim.com/", "servicePort"), Service.class, features);
    }

}