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
    private WebServer(){
    }

    /* print to the log file */
    protected void log(String s) {
        Logger.getLogger(getClass().getName()).log(Level.FINE, s);
    }


    /* Where worker threads stand idle */
    private Vector threads = new Vector();

    /* the web server's virtual root */
    private File root = new File(".");

    /* timeout on client connections */
    private int timeout = 5000;

    /* max # worker threads */
    private int workers = 5;
    private int port = 8080;
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
            ss = new ServerSocket(port);
            
        } catch (IOException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            stopping = true;
        } catch (Exception ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            stopping = true;
        }
        log("start webserver at:"+root.getAbsolutePath());
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
                    } else {
                        //use thread from thread pool
                        w = (WebWorker) threads.elementAt(0);
                        threads.removeElementAt(0);
                        w.setSocket(s);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
                stopping = true;
            }
        }
    }

    public synchronized void setRoot(File root){
        this.root = root;
    }

    /**
     * should only be called from WebWorker Threads
     * to add them to the thread pool
     * @param worker
     */
    synchronized void addThreadToPool(WebWorker worker) {
        if (threads.size()<workers){
            threads.add(worker);
        }else{
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
}





