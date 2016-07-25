/*
 * AbstractConsumer.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

/**
 * Only method run() should be implemented
 */

public class AbstractConsumer extends Thread { 

    private volatile boolean runFlag;
   
    protected int consNumb; // Number of Channel, Consumer, Queue and other
    
    // 3 functions below: removed synchronized, runFlag changed to volatile
    public synchronized void setConsumerStop() { 
        // totalUsedUp = 0;
        runFlag = false;
    }
    public synchronized void setConsumerRun() { runFlag = true; }
    public boolean getConsumerRunFlag() { return runFlag; }  
    
    private volatile long totalUsedUp = 0;
    public synchronized long getTotalUsedUp() { return totalUsedUp; }
    protected synchronized void initTotalUsedUp() { totalUsedUp = 0; }
    protected synchronized void setTotalUsedUp(long usedUp) {
        totalUsedUp = totalUsedUp + usedUp;
    }        
    
    public int getNumber() {
        return consNumb;
    }    
        /** Creates new AbstractConsumer */
    public AbstractConsumer() {
    }    


}
