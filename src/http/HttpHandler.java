package http;

import logger.Logger;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpHandler implements Runnable{

    public static Logger logger;

    public InetSocketAddress Address;
    public ServerSocket serverSocket;
    ExecutorService threadPool;

    private Thread t;
    private final String threadName = "HttpHandler";

    String address;
    int port;
    boolean isLive = false;

    public HttpHandler(String address, int port){
        this.address = address;
        this.port = port;
        logger = new Logger(this);
        logger.log("Creating HTTP hander "+
                String.format("Address: %s | ", address)+
                String.format("Port: %d", port));
        Address = new InetSocketAddress(address,port);
        try{
            serverSocket = getServerSocket(Address);
            serverSocket.setSoTimeout(2000);
        } catch (Exception e) {
            System.err.println("Could not create socket at " + address);
            e.printStackTrace();
        }
        logger.log("Creating ThreadPool with cap of 8");
        threadPool = newCachedThreadPool(8);
    }


    @Override
    public void run() {
        isLive = true;
        logger.log("HttpHandler run...");
        startMultiThreaded(Address);
    }

    public void stop() {
        logger.log("Shutting Down HttpHandler...");
        isLive = false;

        if (serverSocket!=null&& !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.log("Shutting Down HttpHandler... Done.");

    }


    public ServerSocket getServerSocket(InetSocketAddress address)
            throws Exception {

        int https = 0;

        if(https == 0){
            logger.log("Get HTTP ServerSocket");
            logger.log("HTTP success");
            return new ServerSocket(2048);
        }
        logger.log("Get HTTPs ServerSocket");
        // Backlog is the maximum number of pending connections on the socket,
        // 0 means that an implementation-specific default is used
        int backlog = 0;
        logger.log("Get keystore at location => /home/neko/neko_server/ssl/nekohub.me.keystore");
        var keyStorePath = Path.of("/home/neko/neko_server/ssl/nekohub.me.keystore");
        char[] keyStorePassword = "Nekohub!".toCharArray();

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

    private ArrayList<String> getHeaderLines(BufferedReader reader) throws IOException {
        var lines = new ArrayList<String>();
        var line = reader.readLine();
        // An empty line marks the end of the request's header
        while (!line.isEmpty()) {
            lines.add(line);
            line = reader.readLine();
        }
        return lines;
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
                if(!isLive){
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

                // Create a response to the request on a separate thread to
                // handle multiple requests simultaneously
                threadPool.submit(() -> {

                    try ( // Use the socket to read the client's request
                          var reader = new BufferedReader(new InputStreamReader(
                                  socket.getInputStream()));
                          // Writing to the output stream and then closing it
                          // sends data to the client
                          var writer = socket.getOutputStream();
                    ) {
                        ArrayList<String> header;
                        header = getHeaderLines(reader);
                        logger.log("From: "+socket.getInetAddress().toString()+" | "+header.toString());
                        //getHeaderLines(reader).forEach(System.out::println);
                        //Auth
                        //Do Stuff
                        //get response
                        Http res = HttpResponse.getResponse(encoding,header);
                        writer.write(res.getHead().getBytes(),0,res.getHead().getBytes().length);
                        writer.flush();
                        writer.write(res.getPayload(),0,res.getPayload().length);
                        writer.flush();

                        // We're done with the connection → Close the socket
                        writer.close();
                        reader.close();
                        socket.close();

                    } catch (Exception e) {
                        System.err.println("Exception while creating response");
                        e.printStackTrace();
                    }
                });
            } catch (SocketTimeoutException | SocketException ignored) {

            } catch (IOException e) {
                System.err.println("Exception while handling connection");
                e.printStackTrace();
            }
        }

    }

    private ExecutorService newCachedThreadPool(int maximumNumberOfThreads) {
        return new ThreadPoolExecutor(0, maximumNumberOfThreads,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }



}