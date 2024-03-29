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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Process currentProcess = mProcessQueue.poll();
            System.out.println("db scheduler: Poll from process queue");

            if(startedArray.get(currentProcess.getIds()-1) == false){
                System.out.println("Process " + currentProcess.getIds() + " has started.");
                currentProcess.start();
                startedArray.set(currentProcess.getIds()-1, true);
            }


            setFlag(currentProcess, true);
            System.out.println("db scheduler: post setFlag()");


            // Update waiting times and handle process completion
            int currentTime = Math.max(currentProcess.getArrivalTime(), currentProcess.getFinishTime());
            int waitingTime = currentTime - currentProcess.getArrivalTime() - currentProcess.getBurstTime();
            currentProcess.setWaitingTime(waitingTime);


            if (currentProcess.getRemainingTime() == 0) {
                System.out.println("Process " + currentProcess.getIds() + " completed execution.");
                try{
                    currentProcess.join();
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            } else {
                try{
                    currentProcess.join();
                }catch(InterruptedException e){};
                mProcessQueue.add(currentProcess);
            }

        }
    }

    public synchronized void setFlag(Process currentProcess, boolean value) {
//        System.out.println("db scheduler: pre setFlag()");
        flagArray.set(currentProcess.getIds()-1, value);
//        System.out.println("db scheduler: post setFlag()");
//        System.out.println("db scheduler: pre notifyAll()");
        notifyAll(); // Notify all waiting threads that flag has changed
//        System.out.println("db scheduler: post notifyAll()");
    }
}
