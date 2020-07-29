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
        httpHandler = new HttpHandler("10.0.0.33", 2048);

    }

    public int init(){
        //load history and database
        httpHandler.start();
        logger.log("HttpHandler running...");

        try {
            TimeUnit.SECONDS.sleep(10);
            logger.log("Still waiting");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        httpHandler.stop();
        while(true){
            try {
                TimeUnit.SECONDS.sleep(1);
                logger.log("Still waiting");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
