package http;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class HttpResponse {

    public static Http getResponse(Charset encoding, ArrayList<String> header) {
        String requestUrl = header.get(0);
//        String authorization = header.get(?);

        if(requestUrl.equals("GET / HTTP/1.1")){
//        if(!IsAuth()){
//            return HttpConstructor.unauthorized();
//         }
            return HttpConstructor.getAuth();
        }else if(requestUrl.equals("GET /file HTTP/1.1")){
            return HttpConstructor.getMP4();
        }else if(requestUrl.equals("GET /image HTTP/1.1")){
            return HttpConstructor.getJpg();
        }else{
            return HttpConstructor.getDefault();
        }
    }

    private static boolean IsAuth(String userId, String authorization){
        //check Auth
        return true;
    }
}
