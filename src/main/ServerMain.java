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
        httpHandler = new HttpHandler("192.168.0.110", 2048);

    }

    public void init(){
        //load history and database
        httpHandler.start();
        logger.log("HttpHandler running...");

        try {
            TimeUnit.SECONDS.sleep(120);
            logger.log("Still waiting");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        httpHandler.stop();

        Logger.exportLog();
    }

}
