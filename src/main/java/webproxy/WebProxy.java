/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webproxy;

/**
 *
 * @author Marcin
 */
public class WebProxy {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port = 1080;
        String fwdProxyServer = "";
        int fwdProxyPort = 0;

        // create and start the jProxy thread, using a 20 second timeout
        // value to keep the threads from piling up too much
        System.err.println("  **  Starting jProxy on port " + port + ". Press CTRL-C to end.  **\n");
        AdvancedProxy jp = new AdvancedProxy(port, fwdProxyServer, fwdProxyPort, 20);
        jp.setDebug(2, System.out);		// or set the debug level to 2 for tons of output
        jp.start();

        // run forever; if you were calling this class from another
        // program and you wanted to stop the jProxy thread at some
        // point, you could write a loop that waits for a certain
        // condition and then calls jProxy.closeSocket() to kill
        // the running jProxy thread
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
        }

        // if we ever had a condition that stopped the loop above,
        // we'd want to do this to kill the running thread
        //jp.closeSocket();
        //return;
    }

}
