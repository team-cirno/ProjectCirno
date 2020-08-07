package main;

import http.HttpHandler;
import logger.Logger;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ServerMain {

    public static Logger logger;
    static HttpHandler httpHandler;
    static MonitoringThread monitoringThread;

    public static int threadCount;


    public ServerMain(){

        logger = new Logger(this);
        logger.log("Creating Server Main");
        httpHandler = new HttpHandler("10.0.0.40", 2048);
        threadCount = 0;
        monitoringThread = new MonitoringThread(5*1000);


    }

    public static synchronized int setthreadCount(int unit){
        threadCount += unit;
        return threadCount;
    };

    public void init(){
        //load history and database
        ServerMain.setthreadCount(1);
        ServerMain.setthreadCount(1);
        httpHandler.start();
        monitoringThread.start();
        cmd();
        stop();

    }

    public static void stop(){
        httpHandler.shutdown();
        monitoringThread.shutdown();
        Logger.exportLog();
    }

    public void cmd(){
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream.
        String cmd;
        while(true){
            cmd = sc.nextLine();
            if(cmd.equals("stop"))
                return;
        }

    }

}
