class Process extends Thread {
    private final int mId;
    private final int mArrivalTime;
    private int mBurstTime;
    private int mRemainingTime;
    private int mWaitingTime;

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
            int quantum = (int) Math.ceil(this.mRemainingTime * 0.1);
            Thread.sleep(quantum);
            this.mRemainingTime -= quantum;

            // TODO: Notify the scheduler if the process is finished or needs more time
            Scheduler.flag = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
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

