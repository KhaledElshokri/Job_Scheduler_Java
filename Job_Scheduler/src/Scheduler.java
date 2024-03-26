import java.util.PriorityQueue;
import java.util.Comparator;

class ProcessComparator implements Comparator<Process> {
    @Override
    public int compare(Process p1, Process p2) {
        // First, compare the arrival time of the two processes
        if (p1.getArrivalTime() != p2.getArrivalTime()) {
            // -1 when p1 > p2
            // 0 when p1 == p2
            // 1 when p1 > p2
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        } else {
            // If the arrival time is the same, compare their IDs
            return Integer.compare((int) p1.getIds(), (int) p2.getIds());
        }
    }
}

class Scheduler extends Thread{
    private PriorityQueue<Process> mProcessQueue;
    static public boolean flag = false;

    public Scheduler()
    {
        // ProcessComparator() sets the ordering of the PriorityQueue
        mProcessQueue = new PriorityQueue<>(new ProcessComparator());
    }

    public void addProcess(Process p)
    {
        // Add a process to the queue
        mProcessQueue.add(p);
    }

    public void printContents()
    {
        while(!mProcessQueue.isEmpty()){
            System.out.println(mProcessQueue.poll().getBurstTime());
        }
    }

    public void schedule()
    {
        // Main scheduling loop
        while (!mProcessQueue.isEmpty())
        {
            Process currentProcess = mProcessQueue.poll();
            currentProcess.start(); // Start executing the current process
//            currentProcess.notify();

            try {
                while(!flag);
                flag = false;
                currentProcess.wait();
//                currentProcess.join(); // Wait for the process to finish execution
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            // Update waiting times and handle process completion
            int currentTime = Math.max(currentProcess.getArrivalTime(), currentProcess.getFinishTime());
            int waitingTime = currentTime - currentProcess.getArrivalTime() - currentProcess.getBurstTime();
            currentProcess.setWaitingTime(waitingTime);

            System.out.println("Process " + currentProcess.getIds() + " completed execution.");
        }
    }
}
