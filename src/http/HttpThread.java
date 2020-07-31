package http;

import logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HttpThread extends Thread {



    Socket socket;
    private final int clientNo;
    Logger logger;

    public HttpThread(Socket socket, int clientNo) {
        this.socket = socket;
        this.clientNo = clientNo;
        logger = new Logger(this);
    }

    public void run() {
        var encoding = StandardCharsets.UTF_8;
        try ( // Use the socket to read the client's request
              var reader = new BufferedReader(new InputStreamReader(
                      socket.getInputStream()));
              // Writing to the output stream and then closing it
              // sends data to the client
              var writer = socket.getOutputStream();
        ) {
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

        } catch (Exception e) {
            System.err.println("Exception while creating response");
            e.printStackTrace();
        } finally{
            logger.log("Client -" + clientNo + " exit!! ");
        }

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
