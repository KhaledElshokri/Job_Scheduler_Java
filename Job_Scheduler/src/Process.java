import java.util.ArrayList;

class Process extends Thread {
    private final int mId;
    private final int mArrivalTime;
    private int mBurstTime;
    private int mRemainingTime;
    private int mWaitingTime;
    private ArrayList<Boolean> flagArrayReference;

    public Process(int iId, int iArrivalTime, int iBurstTime)
    {
        this.mId = iId;
        this.mArrivalTime = iArrivalTime;
        this.mBurstTime = iBurstTime;
        this.mRemainingTime = iBurstTime;
        this.mWaitingTime = 0;
    }

    //chooses how long to run
    public void run()
    {
        //sleep to simulate computation time
        try {
                Thread.sleep(10);
            while(mRemainingTime != 0) {
//                System.out.println("db process: pre waitForFlagChange() (Process " + this.mId + " ).");
//                System.out.println("db process: pre waitForFlagChange() : " + flagArrayReference);
                waitForFlagChange();
//                System.out.println("db process: post waitForFlagChange() : " + flagArrayReference);
//                System.out.println("db process: post waitForFlagChange() (Process " + this.mId + " ).");
                System.out.println("Process " + this.mId + " has resumed. ");
                int quantum = (int) Math.ceil(this.mRemainingTime * 0.1);
                Thread.sleep(quantum);
                this.mRemainingTime -= quantum;
//                System.out.println("db process: remaining time: "+ this.mRemainingTime);
                System.out.println("Process " + this.mId + " has paused. ");
                if(this.mRemainingTime == 0){
//                    System.out.println("db process: break, mRemainingTime==0");
                    break;
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    public synchronized void waitForFlagChange() {
        while (!flagArrayReference.get(this.mId-1)) {
            try {
//                System.out.println("db process: go inside of wait");
                wait(); // Wait until flag changes
//                System.out.println("db process: breaks out of wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        flagArrayReference.set(this.mId-1, false);
        // Flag has changed, do something
    }

    public void setFlagArrayReference(ArrayList<Boolean> flagArray) {
        this.flagArrayReference = flagArray;
    }

    public int getIds()
    {
        return mId;
    }

    public int getArrivalTime()
    {
        return mArrivalTime;
    }

    public int getBurstTime()
    {
        return mBurstTime;
    }

    public int getRemainingTime()
    {
        return mRemainingTime;
    }

    public int getWaitingTime()
    {
        return mWaitingTime;
    }

    public void setWaitingTime(int iWaitingTime)
    {
        this.mWaitingTime = iWaitingTime;
    }

    public int getFinishTime()
    {
        return mArrivalTime + mBurstTime + mWaitingTime;
    }



}

