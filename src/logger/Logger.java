package logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Logger {
    private static String curClass = null;
    private static ArrayList<String> logSet = new ArrayList();

    public Logger(Object obj){
        this.curClass=obj.getClass().getSimpleName();
    }

    public static void log(String logEntry){
        String time = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println(time+"\t"+curClass+"\t"+logEntry);
        exportLog(time+"\t"+curClass+"\t"+logEntry);
    }

    public static void exportLog(String log){
        try{
            BufferedWriter logWriter = new BufferedWriter(new FileWriter("./logger.txt",true));
            logWriter.write(log);
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
