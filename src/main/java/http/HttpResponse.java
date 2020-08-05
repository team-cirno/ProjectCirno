package http;

import main.ServerMain;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpResponse {

    public static Http getResponse(Charset encoding, HashMap<String,String> header) {
        String requestUrl = header.get("url");
//        String authorization = header.get(?);

        if(requestUrl.equals("/")){
            return HttpConstructor.getFile("/index.html",header.get("Accept"));
        }else if(requestUrl.equals("/auth")){
            return HttpConstructor.getAuth();
        }else if(requestUrl.equals("/file")){
            return HttpConstructor.getMP4();
        }else if(requestUrl.equals("/image")){
            return HttpConstructor.getJpg();
        }else if(requestUrl.equals("/favicon.ico")){
            return HttpConstructor.getIcon();
        }else if(requestUrl.equals("/profile")){
            return HttpConstructor.getIcon();
        }else if(requestUrl.equals("/stop")){
            ServerMain.stop();
            return HttpConstructor.getStop();
        }else{
            return HttpConstructor.getFile(requestUrl,header.get("Accept"));
        }
    }

    private static boolean IsAuth(String userId, String authorization){
        //check Auth
        return true;
    }
}
