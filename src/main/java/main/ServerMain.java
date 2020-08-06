package main;

import http.HttpHandler;
import logger.Logger;

import java.util.concurrent.TimeUnit;

public class ServerMain {

    public static Logger logger;
    static HttpHandler httpHandler;


    public ServerMain(){
        MonitoringThread monitoringThread = new MonitoringThread(5*1000);
        logger = new Logger(this);
        logger.log("Creating Server Main");
        httpHandler = new HttpHandler("10.0.0.16", 2048);

    }

    public void init(){
        //load history and database
        httpHandler.run();

        Logger.exportLog();
    }

    public static void stop(){
        httpHandler.stop();
    }

}
