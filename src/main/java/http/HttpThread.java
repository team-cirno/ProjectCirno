package http;

import logger.Logger;
import main.ServerMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
                HashMap<String,String> request = parseRequest(reader);

                logger.log("Client #" + clientNo + " | From: " + socket.getInetAddress().toString() + " | " + request.get("url") + " " + request.toString());
                Http res;

                if(request.get("method").equals("GET")){
                    res = HttpResponse.getResponse(encoding, request);
                }
                else{
                    res = HttpResponse.poseResponse(request);
                }
                writer.write(res.getHead().getBytes(), 0, res.getHead().getBytes().length);
                writer.flush();
                writer.write(res.getPayload(), 0, res.getPayload().length);
                writer.flush();
                if(request.get("Connection") == null || request.get("Connection").equals("close"))
                    this.closed = true;
                //break;
            }
            // We're done with the connection → Close the socket


        }  catch (NoSuchElementException | SocketTimeoutException ignored){

        } finally {
            writer.close();
            reader.close();
            socket.close();
            this.closed = true;
        }

        }  catch (Exception e) {
            if(Logger.debugMode == 1)
                e.printStackTrace();
        } finally{
            ServerMain.setthreadCount(-1);
            logger.log("Client -" + clientNo + " exit!! ");
        }

    }

    private HashMap<String, String> parseRequest(BufferedReader reader) {
        HashMap<String,String> request = new HashMap<>();

        try {
            String line = null;
            line = reader.readLine();
            // An empty line marks the end of the request's header
            while (!line.isEmpty()) {
                logger.log("=>"+line);
                if(!line.contains(":")){
                    String[] parts = line.split(" ");
                    request.put("method",parts[0]);
                    if(parts[1].indexOf('?')>=0){
                        parts[1] = parts[1].substring(0,parts[1].indexOf('?'));
                        if(parts[1].indexOf('?')>=0)
                            request.put("keyWord", parts[1].substring(parts[1].indexOf('?')));
                    }
                    request.put("url",parts[1]);
                    request.put("httpVersion",parts[2]);
                }else {
                    String[] pair = line.split(": ");
                    request.put(pair[0], pair[1]);
                }
                line = reader.readLine();

            }
            if(request.get("method").equals("GET"))
                return request;
            line = reader.readLine();
            String body = "";
            while (!line.isEmpty()) {
                body += line.trim().replace(" ","");
                line = reader.readLine();
            }
            request.put("body",body);

        } catch (IOException e) {
            if(Logger.debugMode == 1)
                e.printStackTrace();        }

        return request;
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

}
