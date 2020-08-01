package http;

import logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HttpThread extends Thread {


    private OutputStream writer;
    private BufferedReader reader;
    Socket socket;
    private final int clientNo;
    boolean closed;
    Logger logger;

    public HttpThread(Socket socket, int clientNo) {
        this.socket = socket;
        this.clientNo = clientNo;
        logger = new Logger(this);
        this.closed = false;

        try {
            reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            writer = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        var encoding = StandardCharsets.UTF_8;
        logger.log("Client #"+clientNo+" Start");
        try{
            ArrayList<String> header;
            header = getHeaderLines(reader);
            logger.log("Client #" +clientNo+" | From: " + socket.getInetAddress().toString() + " | " + header.toString());
            //getHeaderLines(reader).forEach(System.out::println);
            //Auth
            //Do Stuff
            //get response
            Http res = HttpResponse.getResponse(encoding, header);
            writer.write(res.getHead().getBytes(), 0, res.getHead().getBytes().length);
            writer.flush();
            writer.write(res.getPayload(), 0, res.getPayload().length);
            writer.flush();

            // We're done with the connection → Close the socket
            writer.close();
            reader.close();
            socket.close();
            this.closed = true;

        } catch (Exception e) {

        } finally{
            logger.log("Client -" + clientNo + " exit!! ");
        }

    }
    public void close(){
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isClosed() {
        return closed;
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
}
