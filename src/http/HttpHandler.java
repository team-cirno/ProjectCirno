package http;

import logger.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.Charset;
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

    static String address;
    static int port;

    public HttpHandler(String address, int port){
        this.address = address;
        this.port = port;
        logger = new Logger(this);
        logger.log("Creating HTTP hander "+
                String.format("Address: %s | ", address)+
                String.format("Port: %d", port));
        Address = new InetSocketAddress(address,port);

    }


    @Override
    public void run() {
        startMultiThreaded(Address);
    }

    private static ServerSocket getServerSocket(InetSocketAddress address)
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

    private static SSLContext getSslContext(Path keyStorePath, char[] keyStorePass)
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

    private static String getResponse(Charset encoding) {
        var body = "The server says hi 👋\r\n";
        var contentLength = body.getBytes(encoding).length;

        return "HTTP/1.1 200 OK\r\n" +
                String.format("Access-Control-Allow-Origin: *\r\n") +
                String.format("Access-Control-Allow-Methods: GET, OPTIONS\r\n") +
                String.format("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization\r\n") +
                String.format("Access-Control-Allow-Credentials: false\r\n") +
                String.format("Access-Control-Max-Age: 3600\r\n") +
                String.format("Content-Length: %d\r\n", contentLength) +
                String.format("Content-Type: text/plain; charset=%s\r\n",
                        encoding.displayName()) +
                // An empty line marks the end of the response's header
                "\r\n" +
                body;
    }

    private static ArrayList<String> getHeaderLines(BufferedReader reader)
            throws IOException {
        var lines = new ArrayList<String>();
        var line = reader.readLine();
        // An empty line marks the end of the request's header
        while (!line.isEmpty()) {
            lines.add(line);
            line = reader.readLine();
        }
        return lines;
    }

    public static void startMultiThreaded(InetSocketAddress address) {
        logger.log("Starting multi-threaded server at " + address);

        try (var serverSocket = getServerSocket(address)) {


            // A cached thread pool with a limited number of threads
            var threadPool = newCachedThreadPool(8);

            var encoding = StandardCharsets.UTF_8;
            logger.log("Listening at address:" +
                    String.format("Address: %s | ", address)+
                    String.format("Port: %d", port));
            // This infinite loop is not CPU-intensive since method "accept" blocks
            // until a client has made a connection to the socket
            while (true) {
                try {
                    var socket = serverSocket.accept();
                    // Create a response to the request on a separate thread to
                    // handle multiple requests simultaneously
                    threadPool.submit(() -> {

                        try ( // Use the socket to read the client's request
                              var reader = new BufferedReader(new InputStreamReader(
                                      socket.getInputStream(), encoding.name()));
                              // Writing to the output stream and then closing it
                              // sends data to the client
                              var writer = new BufferedWriter(new OutputStreamWriter(
                                      socket.getOutputStream(), encoding.name()))
                        ) {
                            logger.log("From: "+socket.getInetAddress().toString()+" | "+getHeaderLines(reader).toString());
                            //getHeaderLines(reader).forEach(System.out::println);
                            //Auth
                            //Do Stuff
                            //get response
                            writer.write(getResponse(encoding));
                            writer.flush();
                            // We're done with the connection → Close the socket
                            socket.close();

                        } catch (Exception e) {
                            System.err.println("Exception while creating response");
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    System.err.println("Exception while handling connection");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Could not create socket at " + address);
            e.printStackTrace();
        }
    }

    private static ExecutorService newCachedThreadPool(int maximumNumberOfThreads) {
        return new ThreadPoolExecutor(0, maximumNumberOfThreads,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }

}
