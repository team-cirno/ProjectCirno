package http;


import database.DataBaseHandler;
import logger.Logger;
import object.user.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.String.format;

public class HttpConstructor {

    public static Logger logger = new Logger(new HttpConstructor());

    public static String AccessControl =
            "Access-Control-Allow-Origin: *\r\n" +
            "Access-Control-Allow-Methods: GET, OPTIONS\r\n" +
            "Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, X-Auth-Token, X-Csrf-Token, WWW-Authenticate, Authorization\r\n" +
            "Access-Control-Allow-Credentials: false\r\n" +
            "Access-Control-Max-Age: 3600\r\n";

    public static String getFormat(){
        JSONParser parser = new JSONParser();
        JSONObject jb = null;
        try{
            Object obj = parser.parse(new FileReader("./server.json"));
            jb = (JSONObject)obj;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "HTTP/1.1 200 OK\r\n" +
                format("Date: %s\r\n", getServerTime()) +
                format("Server: %s\r\n", jb.get("serverName")) +
                AccessControl +
                "Accept-Ranges: bytes\r\n";
    }

    public static String getPartialFormat(){
        JSONParser parser = new JSONParser();
        JSONObject jb = null;
        try{
            Object obj = parser.parse(new FileReader("./server.json"));
            jb = (JSONObject)obj;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "HTTP/1.1 206 Partial Content\r\n" +
                format("Date: %s\r\n", getServerTime()) +
                format("Server: %s\r\n", jb.get("serverName")) +
                AccessControl+
                "Accept-Ranges: bytes\r\n";
    }

    static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }

    public static Http getDefault(){

        byte [] body = "The server says hi...\r\n".getBytes();
        int contentLength = body.length;

        String head = getFormat() +
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

        String head = getFormat() +
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

        String head = getFormat() +
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

        String head = getFormat() +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n", "video/mpeg4") +
                "Content-Disposition: inline; name=\"test_video\"; " +
                format("filename=\"test_video.mp4\"\r\n") +
                // An empty line marks the end of the response's header
                "\r\n";
        return new Http(head, body);
    }

    public static Http getFilePartial(String filePath, int start, int end){
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
        int fileLength = (int)myFile.length();
        byte [] body  = new byte [fileLength];
        try {
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(body,0,body.length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        int interval = 100;
        if (start>fileLength || end>fileLength || start>=end){
            return get416();
        }else if(end == -1 || end-start>interval){
            end = start + interval;
        }

        int contentLength = end-start;
        String head = getPartialFormat() +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n", mimeType) +
                format("Content-Range: bytes %d-%d\r\n",start,end)+
                // An empty line marks the end of the response's header
                "\r\n";
        return new Http(head, Arrays.copyOfRange(body, start, end));
    }

    public  static Http get416(){
        String head = "HTTP/1.1 416 Range Not Satisfiable";
        byte[] body = null;
        return new Http(head,body);
    }

    public static Http getFile(String filePath){

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

        String head = getFormat() +
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

        String head = getFormat() +
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

        String head = getFormat() +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n",
                        "text/plain") +
                // An empty line marks the end of the response's header
                "\r\n";

        return new Http(head,body);
    }

    public static Http getUser(String user){

        User tmp = DataBaseHandler.getUser(user);

        logger.log("State build http request");
        logger.log(tmp.getFirstName());
        logger.log(tmp.getLastName());
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("firstName",tmp.getFirstName());
        bodyJson.put("lastName",tmp.getLastName());

        byte[] body = bodyJson.toString().getBytes();
        int contentLength = body.length;

        String head = getFormat() +
                format("Content-Length: %d\r\n", contentLength) +
                format("Content-Type: %s\r\n",
                        "application/json") +
                // An empty line marks the end of the response's header
                "\r\n";
        logger.log("Finish build http request");
        return new Http(head,body);
    }


    public static Http getAuthRequest(){
        logger.log("State build http request");

        JSONParser parser = new JSONParser();
        JSONObject jb = null;
        try{
            Object obj = parser.parse(new FileReader("./server.json"));
            jb = (JSONObject)obj;
        }catch(Exception e){
            e.printStackTrace();
        }
        String tmp = "HTTP/1.1 401 Unauthorized\r\n" +
                format("Date: %s\r\n", getServerTime()) +
                format("Server: %s\r\n", jb.get("serverName")) +
                AccessControl;
        String head = tmp +
                "WWW-Authenticate: Basic realm=\"Access to the staging site\", charset=\"UTF-8\"\r\n"+
                // An empty line marks the end of the response's header
                "\r\n";
        logger.log("Finish build http request");
        return new Http(head,null);
    }

//    public static Http unauthorized(){
//
//        return new Http(head,body);
//    }
}
