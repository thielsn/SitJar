/*
* Copyright 2013 Simon Thiel
*
* This file is part of SitJar.
*
* SitJar is free software: you can redistribute it and/or modify
* it under the terms of the GNU LESSER GENERAL PUBLIC LICENSE as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* SitJar is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with SitJar. If not, see <http://www.gnu.org/licenses/lgpl.txt>.
*/

/**
 * @author Simon Thiel <simon.thiel at gmx.de>
 */
package sit.web;

/*
 * With some inspiration from
 * http://java.sun.com/developer/technicalArticles/Networking/Webserver/
 */
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer implements HttpConstants, Runnable {

    /**
     * Singleton instance
     */
    private final static WebServer instance = new WebServer();

    /**
     * private constructor to enforce singelton
     */
    private WebServer() {
    }

    /* print to the log file */
    protected void log(String s) {
        Logger.getLogger(getClass().getName()).log(Level.FINE, s);
    }
    /* Where worker threads stand idle */
    private Vector<WebWorker> threads = new Vector();

    /* the web server's virtual root */
    private File root = new File(".");

    /* timeout on client connections */
    private int timeout = 5000;

    /* max # worker threads */
    private int workers = 5;
    private int port = 8080;
    private boolean permitDirectoryListing = true;
    private boolean stopping = false;

    private void init() throws Exception {

        /* start worker threads */
        for (int i = 0; i < workers; ++i) {
            WebWorker w = new WebWorker();
            (new Thread(w, "worker #" + i)).start();
            threads.addElement(w);
        }
    }

    public void run() {
        ServerSocket ss = null;
        try {
            log("init webserver");
            init();
            ss = new ServerSocket(getPort());
            Logger.getLogger(WebServer.class.getName()).log(Level.INFO, "Server is listening at port: " + port);
            Logger.getLogger(WebServer.class.getName()).log(Level.INFO, "Root is set to: " + root.getAbsolutePath());


        } catch (IOException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            stopping = true;
        } catch (Exception ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            stopping = true;
        }
        while (!stopping) {
            try {
                Socket s = ss.accept(); //wait for incoming connection

                WebWorker w = null;
                synchronized (this) {
                    if (threads.isEmpty()) {
                        //all threads are busy - create new one
                        WebWorker ws = new WebWorker();
                        ws.setSocket(s);
                        (new Thread(ws, "additional worker")).start();
                        Logger.getLogger(WebServer.class.getName()).log(Level.FINE, "started new WebWorker");
                    } else {
                        //use thread from thread pool
                        w = threads.elementAt(0);
                        threads.removeElementAt(0);
                        w.setSocket(s);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
                stopping = true;
            }
        }
        Logger.getLogger(WebServer.class.getName()).log(Level.INFO, "Server stopped.");
    }

    public synchronized void setRoot(File root) {
        this.root = root;
    }

    /**
     * should only be called from WebWorker Threads
     * to add them to the thread pool
     * @param worker
     */
    synchronized void addThreadToPool(WebWorker worker) {
        if (threads.size() < workers) {
            threads.add(worker);
        } else {
            worker.stop();
        }
    }

    public synchronized int getTimeOut() {
        return timeout;
    }

    public synchronized File getRoot() {
        return root;
    }

    public synchronized static WebServer getInstance() {
        return instance;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the permitDirectoryListing
     */
    public boolean isPermitDirectoryListing() {
        return permitDirectoryListing;
    }

    /**
     * @param permitDirectoryListing the permitDirectoryListing to set
     */
    public synchronized void setPermitDirectoryListing(boolean permitDirectoryListing) {
        this.permitDirectoryListing = permitDirectoryListing;
    }
}
