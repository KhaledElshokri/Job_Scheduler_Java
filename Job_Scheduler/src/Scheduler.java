import java.sql.Array;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Iterator;

class ProcessComparator implements Comparator<Process> {
    @Override
    public int compare(Process p1, Process p2) {
        // Compare burst times
        int burstTimeComparison = Integer.compare(p1.getBurstTime(), p2.getBurstTime());

        // If burst times are different, return the comparison result
        if (burstTimeComparison != 0) {
            // Reverse the comparison by negating the burst times
            return burstTimeComparison;
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

    public Scheduler()
    {
        // ProcessComparator() sets the ordering of the PriorityQueue
        mProcessQueue = new PriorityQueue<>(new ProcessComparator());
        flagArray = new ArrayList<>();
        startedArray = new ArrayList<>();
    }

    public void addProcess(Process p)
    {
        // Add a process to the queue
        mProcessQueue.add(p);
        flagArray.add(false);
        startedArray.add(false);
        p.setFlagArrayReference(flagArray);
    }

    public void run() {
        // Main scheduling loop
        while (!mProcessQueue.isEmpty()) {
            Process currentProcess = mProcessQueue.poll();

            if(startedArray.get(currentProcess.getIds()-1) == false){
                System.out.println("Process " + currentProcess.getIds() + " has started.");
                currentProcess.start();
                startedArray.set(currentProcess.getIds()-1, true);
            }

            try {
                Thread.sleep(3000);
                setFlag(currentProcess, true);
                System.out.println("db scheduler: pre join()");
                currentProcess.join(); // Wait for the process to finish execution
                System.out.println("db scheduler: post join()");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            // Update waiting times and handle process completion
            int currentTime = Math.max(currentProcess.getArrivalTime(), currentProcess.getFinishTime());
            int waitingTime = currentTime - currentProcess.getArrivalTime() - currentProcess.getBurstTime();
            currentProcess.setWaitingTime(waitingTime);


            if (currentProcess.getRemainingTime() == 0) {
                System.out.println("Process " + currentProcess.getIds() + " completed execution.");
            } else mProcessQueue.add(currentProcess);
        }
    }

    public synchronized void setFlag(Process currentProcess, boolean value) {
        System.out.println("db scheduler: pre setFlag()");
        flagArray.set(currentProcess.getIds()-1, value);
        System.out.println("db scheduler: post setFlag()");
        System.out.println("db scheduler: pre notifyAll()");
        notifyAll(); // Notify all waiting threads that flag has changed
        System.out.println("db scheduler: post notifyAll()");
    }
}
