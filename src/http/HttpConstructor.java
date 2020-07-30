package http;

import java.io.*;

public class HttpConstructor {

    public static HttpResponse getDefault(){

        byte [] body = "The server says hi...\r\n".getBytes();
        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                String.format("Access-Control-Allow-Origin: *\r\n") +
                String.format("Access-Control-Allow-Methods: GET, OPTIONS\r\n") +
                String.format("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization\r\n") +
                String.format("Access-Control-Allow-Credentials: false\r\n") +
                String.format("Access-Control-Max-Age: 3600\r\n") +
                String.format("Content-Length: %d\r\n", contentLength) +
                String.format("Content-Type: %s\r\n",
                        "text/plain") +
                // An empty line marks the end of the response's header
                "\r\n";


        return new HttpResponse(head, body);
    }

    public static HttpResponse getImage(){


        FileInputStream fis = null;
        BufferedInputStream bis = null;

        File myFile = new File("./test_image.jpg");
        byte [] body  = new byte [(int)myFile.length()];
        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(body,0,body.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                String.format("Access-Control-Allow-Origin: *\r\n") +
                String.format("Access-Control-Allow-Methods: GET, OPTIONS\r\n") +
                String.format("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization\r\n") +
                String.format("Access-Control-Allow-Credentials: false\r\n") +
                String.format("Access-Control-Max-Age: 3600\r\n") +
                String.format("Content-Length: %d\r\n", contentLength) +
                String.format("Content-Type: %s\r\n",
                        "image/jpeg") +
                // An empty line marks the end of the response's header
                "\r\n";


        return new HttpResponse(head, body);
    }
}
