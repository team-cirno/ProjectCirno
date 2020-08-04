package http;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;

public class HttpConstructor {

    public static String AccessControl = "Access-Control-Allow-Origin: *\r\n" +
            "Access-Control-Allow-Methods: GET, OPTIONS\r\n" +
            "Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization\r\n" +
            "Access-Control-Allow-Credentials: false\r\n" +
            "Access-Control-Max-Age: 3600\r\n";

    public static Http getDefault(){

        byte [] body = "The server says hi...\r\n".getBytes();
        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                AccessControl +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n",
                        "text/plain") +
                // An empty line marks the end of the response's header
                "\r\n";


        return new Http(head, body);
    }

    public static Http getJpg(){


        FileInputStream fis = null;
        BufferedInputStream bis = null;

        File myFile = new File("./test_image.jpg");
        byte [] body  = new byte [(int)myFile.length()];
        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(body,0,body.length);

        } catch (IOException e) {
            e.printStackTrace();
        }


        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                AccessControl +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n",
                        "image/jpeg") +
                // An empty line marks the end of the response's header
                "\r\n";


        return new Http(head, body);
    }
    public static Http getIcon(){


        FileInputStream fis = null;
        BufferedInputStream bis = null;

        File myFile = new File("./favicon.ico");
        byte [] body  = new byte [(int)myFile.length()];
        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(body,0,body.length);

        } catch (IOException e) {
            e.printStackTrace();
        }


        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                AccessControl +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n",
                        "image/x-icon") +
                // An empty line marks the end of the response's header
                "\r\n";


        return new Http(head, body);
    }

    public static Http getMP4(){


        FileInputStream fis = null;
        BufferedInputStream bis = null;

        File myFile = new File("./test_video.mp4");
        byte [] body  = new byte [(int)myFile.length()];
        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(body,0,body.length);

        } catch (IOException e) {
            e.printStackTrace();
        }


        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                AccessControl +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n", "video/mpeg4") +
                "Content-Disposition: inline; name=\"test_video\"; " +
                format("filename=\"test_video.mp4\"\r\n") +
                // An empty line marks the end of the response's header
                "\r\n";
        return new Http(head, body);
    }
    public static Http getFile(String filePath, String context){

        FileInputStream fis = null;
        BufferedInputStream bis = null;

        Path path = new File("./www"+filePath).toPath();
        String mimeType = null;
        try {
            mimeType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File myFile = new File("./www"+filePath);
        byte [] body  = new byte [(int)myFile.length()];
        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(body,0,body.length);

        } catch (IOException e) {
            e.printStackTrace();
        }


        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                AccessControl +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n", mimeType) +
                // An empty line marks the end of the response's header
                "\r\n";
        return new Http(head, body);
    }

    public static Http getAuth(){
        //Need generate random access code
        byte [] body = "RandomAuth\r\n".getBytes();
        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                AccessControl +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n",
                        "text/plain") +
                // An empty line marks the end of the response's header
                "\r\n";

        return new Http(head,body);
    }
    public static Http getStop(){
        //Need generate random access code
        byte [] body = "Server Sutting Down\r\n".getBytes();
        int contentLength = body.length;

        String head = "HTTP/1.1 200 OK\r\n" +
                AccessControl +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n",
                        "text/plain") +
                // An empty line marks the end of the response's header
                "\r\n";

        return new Http(head,body);
    }

//    public static Http unauthorized(){
//
//        return new Http(head,body);
//    }
}
