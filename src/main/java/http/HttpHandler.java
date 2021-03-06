package http;

import logger.Logger;
import main.ServerMain;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpHandler extends Thread{

    public static Logger logger;


    public InetSocketAddress Address;
    public ServerSocket serverSocket;

    private Thread t;
    private final String threadName = "HttpHandler";
    HttpThread[] threadPool;

    String address;
    long port;
    static boolean isLive = false;

    int counter;

    public static synchronized boolean isLive(){
        return isLive;
    }

    public static synchronized void setIsLive(boolean isLive){
        HttpHandler.isLive = isLive;
    }

    public HttpHandler(String address, long port){
        this.address = address;
        this.port = port;
        counter = 0;
        logger = new Logger(this);
        logger.log("Creating HTTP hander "+
                String.format("Address: %s | ", address)+
                String.format("Port: %d", port));
        Address = new InetSocketAddress(address, Math.toIntExact(port));
        try{
            serverSocket = getServerSocket(Address);
            serverSocket.setSoTimeout(2000);
        } catch (Exception e) {
            System.err.println("Could not create socket at " + address);
            e.printStackTrace();
        }
        threadPool = new HttpThread[20];
        for(int i=0; i<threadPool.length;i++){
            threadPool[i]=null;
        }

        logger.log("Creating ThreadPool with cap of 8");
    }


    @Override
    public void run() {
        setIsLive(true);
        logger.log("HttpHandler run...");
        startMultiThreaded(Address);
    }

    public void shutdown() {
        logger.log("Shutting Down HttpHandler...");
        setIsLive(false);


        logger.log("Shutting Down HttpHandler... Done.");

    }


    public ServerSocket getServerSocket(InetSocketAddress address) throws Exception {

        JSONParser parser = new JSONParser();
        JSONObject jb = null;
        try{
            Object obj = parser.parse(new FileReader("./server.json"));
            jb = (JSONObject)obj;
        }catch(Exception e){
            e.printStackTrace();
        }


        if((long)jb.get("https") == 0){
            logger.log("Get HTTP ServerSocket");
            logger.log("HTTP success");
            return new ServerSocket(Math.toIntExact((long) jb.get("port")));
        }

        logger.log("Get HTTPs ServerSocket");
        // Backlog is the maximum number of pending connections on the socket,
        // 0 means that an implementation-specific default is used
        int backlog = 0;
        logger.log("Get keystore at location => /home/neko/neko_server/ssl/nekohub.me.keystore");
        //var keyStorePath = Path.of("/home/neko/neko_server/ssl/nekohub.me.keystore");
        var keyStorePath = Path.of(jb.get("keyStorePath").toString());
        char[] keyStorePassword = jb.get("keyStorePassword").toString().toCharArray();

        // Bind the socket to the given port and address
        var serverSocket = getSslContext(keyStorePath, keyStorePassword)
                .getServerSocketFactory()
                .createServerSocket(address.getPort(), backlog, address.getAddress());

        // We don't need the password anymore → Overwrite it
        Arrays.fill(keyStorePassword, '0');
        logger.log("HTTPs success");
        return serverSocket;


    }

    private SSLContext getSslContext(Path keyStorePath, char[] keyStorePass)
            throws Exception {

        var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(keyStorePath.toFile()), keyStorePass);

        var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyStorePass);

        var sslContext = SSLContext.getInstance("TLS");
        // Null means using default implementations for TrustManager and SecureRandom
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        return sslContext;
    }

    public void startMultiThreaded(InetSocketAddress address) {
        logger.log("Starting multi-threaded server at " + address);

        // A cached thread pool with a limited number of threads

        var encoding = StandardCharsets.UTF_8;
        logger.log("Listening at address:" +
                String.format("Address: %s | ", address)+
                String.format("Port: %d", port));
        // This infinite loop is not CPU-intensive since method "accept" blocks
        // until a client has made a connection to the socket

        while (true) {
            try {
                if(!isLive()){
                    logger.log("Find server closed");
                    if (serverSocket!=null&& !serverSocket.isClosed()) {
                        try {
                            serverSocket.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
                var socket = serverSocket.accept();
                int alloc = -1;
                for (int i = 0 ; i<threadPool.length;i++)
                {
                    if(threadPool[i] == null || threadPool[i].isClosed()){
                        HttpThread var = new HttpThread(socket, i);
                        ServerMain.setthreadCount(1);
                        var.start();
                        threadPool[i]=var;
                        counter +=1;
                        alloc = i;
                        logger.log("Alloc in thread #"+i);
                        break;
                    };
                }
                if(alloc == -1){
                    logger.log("Faild to Alloc too busy");
                    socket.close();
                }

            } catch (SocketTimeoutException ignored) {

            } catch (SocketException e) {

            } catch (IOException e) {
                System.err.println("Exception while handling connection");
                e.printStackTrace();
            }
        }


        if (serverSocket!=null&& !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0 ; i<threadPool.length;i++)
        {
            if(threadPool[i] != null && !threadPool[i].isClosed()){
                threadPool[i].close();
                threadPool[i].interrupt();
                threadPool[i].stop();
            };
        }
    }
}