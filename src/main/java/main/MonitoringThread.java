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

    private Map<Long, ThreadTime> threadTimeMap = new HashMap<Long, ThreadTime>();
    private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    OperatingSystemMXBean mont;

    Queue cpuLoad;
    Queue ramLoad;
    Queue netLoad;

    public MonitoringThread(long refreshInterval) {
        this.refreshInterval = refreshInterval;

        setName("MonitoringThread");
        long[] threadid = new long[1];
        threadid[0] = this.getId();
        this.mapNewThreads(threadid);
        cpuLoad = new Queue(12);
        ramLoad = new Queue(12);
        netLoad = new Queue(12);
        mont = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
    }

    @Override
    public void run() {
        while(!stopped) {

            cpuLoad.enqueue(mont.getCpuLoad());
            ramLoad.enqueue(mont.getTotalPhysicalMemorySize()-mont.getFreeMemorySize());
            netLoad.enqueue(0);

            try {
                Thread.sleep(refreshInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void mapNewThreads(long[] allThreadIds) {
        for (long id : allThreadIds) {
            synchronized (threadTimeMap) {
                if(!threadTimeMap.containsKey(id))
                    threadTimeMap.put(id, new ThreadTime(id));
            }
        }
    }

    private void removeDeadThreads(Set<Long> mappedIds, long[] allThreadIds) {
        outer: for (long id1 : mappedIds) {
            for (long id2 : allThreadIds) {
                if(id1 == id2)
                    continue outer;
            }
            synchronized (threadTimeMap) {
                threadTimeMap.remove(id1);
            }
        }
    }

    public void shutdown() {
        this.stopped = true;
    }

    public double getTotalUsage() {
        Collection<ThreadTime> values;
        synchronized (threadTimeMap) {
            values = new HashSet<ThreadTime>(threadTimeMap.values());
        }

        double usage = 0D;
        for (ThreadTime threadTime : values) {
            synchronized (threadTime) {
                usage += (threadTime.getCurrent() - threadTime.getLast()) / (refreshInterval * 10000);
            }
        }
        return usage;
    }

    static class ThreadTime {

        private long id;
        private long last;
        private long current;

        public ThreadTime(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public long getLast() {
            return last;
        }

        public void setLast(long last) {
            this.last = last;
        }

        public long getCurrent() {
            return current;
        }

        public void setCurrent(long current) {
            this.current = current;
        }
    }
}