package main;

import com.sun.management.OperatingSystemMXBean;

import javax.management.ObjectName;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import static java.lang.System.out;


public class test {
    public static void main(String args[]) throws SocketException {
        out.println(getNetworkUsageKb());
    }

    public static long[] getNetworkUsageKb() {
        BufferedReader reader;
        String line;
        String[] values;
        long[] totalBytes = new long[2];//rx,tx
        try {
            reader = new BufferedReader(new FileReader("/proc/net/dev"));

            while ((line = reader.readLine()) != null) {
                if (line.contains("eth") || line.contains("wlan")){
                    values = line.trim().split("\\s+");
                    totalBytes[0] +=Long.parseLong(values[1]);//rx
                    totalBytes[1] +=Long.parseLong(values[9]);//tx
                }
            }
            reader.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //transfer to kb
        totalBytes[0] =  totalBytes[0] / 1024;
        totalBytes[1] =  totalBytes[1] / 1024;

        return totalBytes;
    }
}
