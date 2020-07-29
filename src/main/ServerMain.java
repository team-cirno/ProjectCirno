package main;

import http.HttpHandler;

public class ServerMain {

    HttpHandler httpHandler;


    public ServerMain(){
        httpHandler = new HttpHandler("10.0.0.16", 2048);


    }

    public int init(){
        //load history and database
        httpHandler.run();

        return 0;

    }

}
