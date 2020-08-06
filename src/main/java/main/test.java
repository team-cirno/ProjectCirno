package main;

import com.sun.management.OperatingSystemMXBean;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class test {
    public static void main(String[] args) {
        OperatingSystemMXBean mbean = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        double load;
        for(int i=0; i<10; i++) {
            load = mbean.getProcessCpuLoad();
            if((load<0.0 || load>1.0) && load != -1.0) {
                throw new RuntimeException("getProcessCpuLoad() returns " + load
                        +   " which is not in the [0.0,1.0] interval");
            }
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("Ps CPU load   ");
            System.out.println(load);
            System.out.print(" CPU load     ");
            System.out.println(mbean.getCpuLoad());
            System.out.print(" ram load     ");
            System.out.println(mbean.getTotalPhysicalMemorySize()/1024/1024/1024);
            System.out.print("free ram load ");
            System.out.println(mbean.getFreePhysicalMemorySize()/1024/1024);
        }
    }
}
