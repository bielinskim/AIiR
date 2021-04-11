/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webproxy;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Marcin
 */
public class AdvancedProxy extends jProxy {
    
    public AdvancedProxy(int port) {
        super(port);
    }
    public AdvancedProxy(int port, String proxyServer, int proxyPort) {
        super(port, proxyServer, proxyPort);
    }
    public AdvancedProxy(int port, String proxyServer, int proxyPort, int timeout) {
        super(port, proxyServer, proxyPort, timeout);
    }
    
    @Override
    public void run()
	{
		try {
			// create a server socket, and loop forever listening for
			// client connections
			server = new ServerSocket(thisPort);
			if (debugLevel > 0)
				debugOut.println("Started AdvancedProxy on port " + thisPort);
			
			while (true)
			{
				Socket client = server.accept();
				AdvancedProxyThread t = new AdvancedProxyThread(client, fwdServer, fwdPort);
				t.setDebug(debugLevel, debugOut);
				t.setTimeout(ptTimeout);
				t.start();
			}
		}  catch (Exception e)  {
			if (debugLevel > 0)
				debugOut.println("AdvancedProxy Thread error: " + e);
		}
		
		closeSocket();
	}
    
}
