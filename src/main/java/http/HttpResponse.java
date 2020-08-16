package http;

import main.ServerMain;
import object.user.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HttpResponse {

    private static object.user.User curUser = null;

    public static Http getResponse(Charset encoding, HashMap<String, String> header) {
        String requestUrl = header.get("url");
//        String authorization = header.get(?);

//        if (requestUrl.equals("/")) {
//            return HttpConstructor.getFile("/index.html");
//        } else if (requestUrl.equals("/auth")) {
//            return HttpConstructor.getAuth();
//        } else if (requestUrl.equals("/file")) {
//            return HttpConstructor.getMP4();
//        } else if (requestUrl.equals("/image")) {
//            return HttpConstructor.getJpg();
//        } else if (requestUrl.equals("/favicon.ico")) {
//            return HttpConstructor.getIcon();
//        } else if (requestUrl.equals("/profile")) {
//            return HttpConstructor.postUser(curUser);
//        } else if (requestUrl.equals("/stop")) {
//            ServerMain.stop();
//            return HttpConstructor.getStop();
//        } else {
//            return HttpConstructor.getFile(requestUrl);
//        }
        switch (requestUrl){
            case "/":
                return HttpConstructor.getFile("/index.html");
            case "/auth":
                return HttpConstructor.getAuth();
            case "/image":
                return HttpConstructor.getJpg();
            case "/favicon.ico":
                return HttpConstructor.getIcon();
            case "/profile":
                return HttpConstructor.getUser(curUser);
            case "/stop":
                ServerMain.stop();
                return HttpConstructor.getStop();
            default:
                if(header.get("Range") == null || true){
                    return HttpConstructor.getFile(header.get("url"));
                }else{
                    //request with content range
                    int start;
                    int end;
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(header.get("Range"));
                    if(m.find()){
                        start = Integer.parseInt(m.group());
                    } else{
                        return HttpConstructor.get416();
                    }
                    if(m.find()) {
                        end = Integer.parseInt(m.group());
                    }else{
                        end = -1;
                    }
                    return HttpConstructor.getFilePartial(header.get("url"),start,end);
                }
        }
    }

    public static Http poseResponse(HashMap<String, String> request) {
        if (request.get("url").equals("/createUser")) {
            try {
                String bodyString = request.get("body");
                JSONParser parser = new JSONParser();
                JSONObject body = (JSONObject) parser.parse(bodyString);
                curUser = new User(body.get("firstname").toString(),body.get("lastName").toString(),body.get("nickName").toString(), (String) body.get("userName"), body.get("eMail").toString(), UUID.fromString(body.get("uuid").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return HttpConstructor.getDefault();
        }
        return HttpConstructor.getDefault();
    }
}