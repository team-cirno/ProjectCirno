package http;

import java.io.*;
import java.nio.charset.Charset;

public class HttpResponse {

    private String getResponse(Charset encoding, String requestUrl) {
//        ---------------------
        String contentType = "text/plain";
//        var body = "The server says hi 👋\r\n";
//        var contentLength = ((String) body).getBytes(encoding).length;
        Object body;
        int contentLength;

        if(requestUrl.equals("GET /auth HTTP/1.1")){
            body = "random auth";
            contentLength = ((String) body).getBytes(encoding).length;
        }else if(requestUrl.equals("GET /file HTTP/1.1")){
            File testFile = new File("./test_video.mp4");
            byte[] byteArray = new byte[(int) testFile.length()];
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(testFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedInputStream bis = new BufferedInputStream(fis);
            try {
                bis.read(byteArray,0,byteArray.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            body = byteArray;
            contentLength = byteArray.length;
            contentType = "video/mpeg4";
        }else if(requestUrl.equals("GET /image HTTP/1.1")){
            File testImage = new File("./test_image.jpg");
            byte[] byteArray = new byte[(int) testImage.length()];
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(testImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedInputStream bis = new BufferedInputStream(fis);
            try {
                bis.read(byteArray,0,byteArray.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            body = byteArray;
            contentLength = byteArray.length;
            contentType = "image/jpeg";
        }else{
            body = "The server says hi 👋\r\n";
            contentLength = ((String) body).getBytes(encoding).length;
        }

//        ---------------------

        return "HTTP/1.1 200 OK\r\n" +
                String.format("Access-Control-Allow-Origin: *\r\n") +
                String.format("Access-Control-Allow-Methods: GET, OPTIONS\r\n") +
                String.format("Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization\r\n") +
                String.format("Access-Control-Allow-Credentials: false\r\n") +
                String.format("Access-Control-Max-Age: 3600\r\n") +
                String.format("Content-Length: %d\r\n", contentLength) +
                String.format("Content-Type: %s; charset=%s\r\n",
                        contentType,encoding.displayName()) +
                // An empty line marks the end of the response's header
                "\r\n" +
                body;
    }
}
