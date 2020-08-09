package main;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MonitoringThread extends Thread {

    private long refreshInterval;
    private boolean stopped;

    private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    OperatingSystemMXBean mont;

    Queue cpuLoad;
    Queue ramLoad;
    Queue thdLoad;

    public MonitoringThread(long refreshInterval) {
        this.refreshInterval = refreshInterval;


        setName("MonitoringThread");
        long[] threadid = new long[1];
        threadid[0] = this.getId();
        cpuLoad = new Queue(12);
        ramLoad = new Queue(12);
        thdLoad = new Queue(12);
        mont = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public void run() {
        while(!stopped) {

            cpuLoad.enqueue(mont.getCpuLoad());
            ramLoad.enqueue(mont.getTotalPhysicalMemorySize()-mont.getFreeMemorySize());
            thdLoad.enqueue(ServerMain.threadCount);

            try {
                Thread.sleep(refreshInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void shutdown() {
        this.stopped = true;
    }

}