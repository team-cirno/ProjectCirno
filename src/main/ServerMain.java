package main;

import http.HttpHandler;
import logger.Logger;

import java.util.concurrent.TimeUnit;

public class ServerMain {

    public static Logger logger;
    HttpHandler httpHandler;


    public ServerMain(){
        logger = new Logger(this);
        logger.log("Creating Server Main");
        httpHandler = new HttpHandler("10.0.0.16", 2048);

    }

    public int init(){
        //load history and database
        httpHandler.run();
        while(true){
            try {
                TimeUnit.SECONDS.sleep(1);
                logger.log("Still runing");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
