/*
 * AbstractProducer.java
 *
 * Created in Mart  2002
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

import Approco.control.*;

public class AbstractProducer extends Thread { 

    private volatile boolean runFlag;
    //protected int numberOfConsumer;

    // 3 functions below: removed synchronized, runFlag changed to volatile
    public synchronized void setProducerStop() { 
         runFlag = false;
         Print.show("Producer in state stop");
    }
    public synchronized void setProducerRun() {
         runFlag = true;
         Print.show("Producer in state run");
         
         notifyAll();
    }
    
    public synchronized boolean getProducerRunFlag() { return runFlag; }  
    
    protected synchronized void waitForStart() {        
        try {
            while( getProducerRunFlag() == false ) {
                wait();
            }    
            
        } catch (java.lang.InterruptedException ie) {    
            Print.show(" waitForStart: interrupt " + ie.toString());
        }    
    }    
    
        /** Creates new AbstractConsumer */
    public AbstractProducer() {
    }    



}
