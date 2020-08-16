package main;

import http.HttpHandler;
import logger.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ServerMain {

    public static Logger logger;
    static HttpHandler httpHandler;
    static MonitoringThread monitoringThread;

    public static String version;
    public static String serverName;
    long port;
    String address;

    public static int threadCount;


    public ServerMain(){

        logger = new Logger(this);
        logger.log("Creating Server Main");

        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader("./server.json"));
            JSONObject jb = (JSONObject)obj;
            port  = (long) jb.get("port");
            address = jb.get("address").toString();
            Logger.debugMode = (long) jb.get("debugMode");
            version = jb.get("Version").toString();
            serverName = jb.get("ServerName").toString();

        }catch(Exception e){
            e.printStackTrace();
        }

        httpHandler = new HttpHandler(address, port);
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
        String welcomMsg =  "==========================================\r\n"+
                            String.format("Server: %s\r\n", serverName)+
                            String.format("Version: %s\r\n", version)+
                            "==========================================";
        System.out.println(welcomMsg);
        while(true){
            cmd = sc.nextLine();
            if(cmd.equals("stop"))
                return;
            else if(cmd.equals("hello"))
                System.out.println("hi");
            else if(cmd.equals("ls"))
                System.out.println(String.format("Running %d threads",ServerMain.setthreadCount(0)));
            else if(cmd.equals("help"))
                System.out.println("HAHA no help");
            else{
                System.out.println("Commend Not Found help for help");
            }
        }

    }

}
