import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;

class ProcessComparator implements Comparator<Process> {
    @Override
    public int compare(Process p1, Process p2) {
        // Compare burst times
        int remainingTimeComparison = Integer.compare(p1.getRemainingTime(), p2.getRemainingTime());

        // If burst times are different, return the comparison result
        if (remainingTimeComparison != 0) {
            // Reverse the comparison by negating the burst times
            return remainingTimeComparison;
        } else {
            // If burst times are equal, compare arrival times
            int arrivalTimeComparison = Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());

            // Reverse the comparison to prioritize higher arrival times
            return -arrivalTimeComparison;
        }
    }
}

class Scheduler extends Thread{
    private PriorityQueue<Process> mProcessQueue;
    private ArrayList<Boolean> flagArray;
    private ArrayList<Boolean> startedArray;
    private ArrayList<Process> mProcessList;
    private int time = 0;
//    private final int quantum;

    public Scheduler()
    {
        // ProcessComparator() sets the ordering of the PriorityQueue
        mProcessQueue = new PriorityQueue<>(new ProcessComparator());
        mProcessList = new ArrayList<>();
        flagArray = new ArrayList<>();
        startedArray = new ArrayList<>();
    }

    public void addProcess(Process p)
    {
        // Add a process to the queue
        mProcessQueue.add(p);
        mProcessList.add(p);
        flagArray.add(false);
        startedArray.add(false);
        p.setFlagArrayReference(flagArray);
    }

    public void run() {
        while (!mProcessQueue.isEmpty()) {
            Process currentProcess = mProcessQueue.poll(); // Retrieve and remove the head of the queue
            currentProcess.setCurrentTime(time);
            if (!startedArray.get(currentProcess.getIds() - 1)) {
                System.out.println("Time " + time + ", Process " + currentProcess.getIds() + " has started.");
                currentProcess.start(); // Start process if it hasn't started
                startedArray.set(currentProcess.getIds() - 1, true);
            }
            setFlag(currentProcess, true); // Signal the process to run
            try {
                currentProcess.join(); // Wait for the process to complete its quantum
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (currentProcess.getRemainingTime() > 0) {
                // If the process still has remaining time, add it back to the queue
                mProcessQueue.add(currentProcess);
            }
            time = currentProcess.getCurrentTime();
        }
        System.out.println("----------------------------");
        System.out.println("Waiting Times: ");
        for(int i = 0; i < mProcessList.size(); i++){
            System.out.println("Process " + mProcessList.get(i).getIds() + ": " + mProcessList.get(i).getWaitingTime());
        }


    }

    public synchronized void setFlag(Process currentProcess, boolean value) {
//        System.out.println("db scheduler: pre setFlag() pr."+ currentProcess.getIds() +" : " + flagArray);
        flagArray.set(currentProcess.getIds()-1, value);
//        System.out.println("db scheduler: post setFlag() pr."+ currentProcess.getIds() +" : " + flagArray);
//        System.out.println("db scheduler: pre notifyAll()");
        notifyAll(); // Notify all waiting threads that flag has changed
//        System.out.println("db scheduler: post notifyAll()");
    }
}
