package http;

import com.mongodb.util.JSON;
import main.ServerMain;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;

import org.json.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HttpResponse {

    private static object.user.User curUser = null;

    public static Http getResponse(Charset encoding, HashMap<String, String> header) {
        String requestUrl = header.get("url");
//        String authorization = header.get(?);

        if (requestUrl.equals("/")) {
            return HttpConstructor.getFile("/index.html");
        } else if (requestUrl.equals("/auth")) {
            return HttpConstructor.getAuth();
        } else if (requestUrl.equals("/file")) {
            return HttpConstructor.getMP4();
        } else if (requestUrl.equals("/image")) {
            return HttpConstructor.getJpg();
        } else if (requestUrl.equals("/favicon.ico")) {
            return HttpConstructor.getIcon();
        } else if (requestUrl.equals("/profile")) {
            return HttpConstructor.getIcon();
        } else if (requestUrl.equals("/stop")) {
            ServerMain.stop();
            return HttpConstructor.getStop();
        } else {
            return HttpConstructor.getFile(requestUrl);
        }
    }

    public static Http poseResponse(HashMap<String, String> request) {
        if (request.get("url").equals("/createUser")) {
            try {
                String bodyString = request.get("body");
                JSONParser parser = new JSONParser();
                JSONObject body = (JSONObject) parser.parse(bodyString);
                curUser = new object.user.User((String) body.get("firstname"), (String) body.get("lastName"), (String) body.get("nickName"), (String) body.get("userName"), (String) body.get("eMail"), UUID.fromString((String) body.get("uuid")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return HttpConstructor.getDefault();
        } else if (request.get("url").equals("/profile")) {
            return HttpConstructor.poseUser(curUser);
        }
        return HttpConstructor.getDefault();
    }
}