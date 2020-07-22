package Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Logger {
    private static String curClass = null;
    private static ArrayList<String> logSet = new ArrayList<>();
    private static  ArrayList<String > serverStat = new ArrayList<>();
    private static int requestCount = 0;

    public Logger(Object obj){
        this.curClass=obj.getClass().getSimpleName();
    }

    public static void log(String logEntry){
        String time = new Timestamp(System.currentTimeMillis()).toString();
        logSet.add(time+"\t"+curClass+"\t"+logEntry);
    }

    public static void serverStat(String userIP, String userIdentifier, String userId, String requestLine, String statusCode, String returnSize){
        //enter "-" for missing data
        String time = new Timestamp(System.currentTimeMillis()).toString();
        serverStat.add(userIP+' '+userIdentifier+' '+userId+' '+time+' '+requestLine+' '+statusCode+' '+returnSize);
    }

    public static void exportServerStat(ArrayList<String> serverStat){
        try{
            BufferedWriter logWriter = new BufferedWriter(new FileWriter("./serverStat.txt",true));
            for(String s : serverStat){
                logWriter.write(s);
                logWriter.newLine();
            }
            logWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void exportLog(ArrayList<String> logSet){
        //export info to the log file when server stopped
        try{
            BufferedWriter logWriter = new BufferedWriter(new FileWriter("./logger.txt",true));
            for(String s : logSet){
                logWriter.write(s);
                logWriter.newLine();
            }
            logWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void clearLog(){
        try{
            BufferedWriter logWriter = new BufferedWriter(new FileWriter("./logger.txt"));
            logWriter.write("");
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
