package http;

import logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HttpThread extends Thread {


    private OutputStream writer;
    private BufferedReader reader;
    Socket socket;
    private final int clientNo;
    boolean closed;
    Logger logger;

    public HttpThread(Socket socket, int clientNo) {
        this.socket = socket;
        try {
            this.socket.setSoTimeout(10*1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
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
        try{try {
            while (!this.closed) {
                HashMap<String, String> header;
                String head = "";
                String line = "";
                while((line=reader.readLine())!=null && line.length()!=0){
                    head += line+"\r\n";
                };
                header = getHeaderLines(head);
                logger.log("Client #" + clientNo + " | From: " + socket.getInetAddress().toString() + " | " + header.get("url") + header.toString());
                //getHeaderLines(reader).forEach(System.out::println);
                //Auth
                //Do Stuff
                //get response
                Http res = HttpResponse.getResponse(encoding, header);
                writer.write(res.getHead().getBytes(), 0, res.getHead().getBytes().length);
                writer.flush();
                writer.write(res.getPayload(), 0, res.getPayload().length);
                writer.flush();
            }
            // We're done with the connection → Close the socket


        }  catch (NoSuchElementException e){

        }  catch (SocketTimeoutException e) {

        } finally {
            writer.close();
            reader.close();
            socket.close();
            this.closed = true;
        }

        }  catch (Exception e) {
            e.printStackTrace();

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

//    private ArrayList<String> getHeaderLines(BufferedReader reader) throws IOException {
//        var lines = new ArrayList<String>();
//        var line = reader.readLine();
//        // An empty line marks the end of the request's header
//        while (!line.isEmpty()) {
//            lines.add(line);
//            line = reader.readLine();
//        }
//        return lines;
//    }

    private HashMap<String, String> getHeaderLines(String head) throws IOException {

        HashMap<String,String> header = new HashMap<>();
        for (String line : head.split("\r\n")){
            if (!line.contains(":")){
                String[] parts = line.split(" ");
                header.put("method",parts[0]);
                header.put("url",parts[1]);
                header.put("httpVersion",parts[2]);
            }else {
                String[] pair = line.split(": ");
                header.put(pair[0], pair[1]);
            }
        }
        return header;
    }
}
