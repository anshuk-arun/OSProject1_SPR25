public class ProcessThread extends Thread {
    int pid, burstTime;
    public ProcessThread(int inPid, int inBurstTime){
        this.pid = inPid;
        this.burstTime = inBurstTime;
    }
    
    public void run(){
        // Print to console when a certain process starts and finishes
        System.out.println("Process " + pid + " started.");
        try {
            Thread.sleep(burstTime * 1000);
        } catch (InterruptedException e){}
        System.out.println("Process " + pid + " finished.");
    }
}
