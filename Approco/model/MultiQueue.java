/*
 * MultiQueue.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

import java.io.IOException;
import java.awt.*;
import Approco.control.*;

public final class MultiQueue { 
       
    private static int numbOfConsumers = Params.numbOfConsumers;
    private Consumer[] consM;
    private QueueVector[] queueM; 
    private Producer prod;
    
    // private Choice modeGetChoice; 
    
    /** Creates new MultiQueue: Singleton */
    private MultiQueue() { 
       consM = new Consumer[numbOfConsumers];
       queueM = new QueueVector[numbOfConsumers];
       
       Listeners.getInstance();
       /*
       prod = new Producer();              
       prod.setProducerRun();
       */
       Print.show(" MultiQueue is init ...");
       //prod.start();              
    }    
    
    private static MultiQueue instance = new MultiQueue();
    public static MultiQueue getInstance() {
        if(instance == null) {
            instance = new MultiQueue();
        }    
        return instance;
    }    
    
    public void  setMultiQueueNull() {
        instance = null;
    }    
                
    public QueueVector getQueue(int consNumb) {
        return queueM[consNumb];
    }    

    public Consumer getConsumer(int consNumb) {
        return consM[consNumb];
    }  
    
    public Producer getProducer() { return prod; }

    /**
     *  Launch thread for given Consumer
     */    
    public synchronized void startConsumer(int consNumb )
    {       
        if (consM[consNumb] == null ) {
           queueM[consNumb] = new QueueVector(consNumb);
           consM[consNumb] = new Consumer(consNumb);
           Print.show( "New Consumer = " + (consNumb+1) + " is created");        
           queueM[consNumb].setConsumerRunFlag();        
           consM[consNumb].setConsumerRun();
           consM[consNumb].start();                                  
        }
        else {
           queueM[consNumb].setConsumerRunFlag();        
           consM[consNumb].setConsumerRun();
           Print.show("Consumer " + (consNumb+1) + " is restarted");  
          // consM[consNumb].start();
        }    
    }    
    
    public synchronized void startProducer() {
        if (prod == null) {
           prod = new Producer();              
           prod.setProducerRun();
           Print.show("Producer created and started");
           prod.start();              
        }     
        else {
           prod.setProducerRun();
           Print.show("Producer restarted");
        }    
    }    
    
    public synchronized void stopProducer() {
        if (prod != null) {
           prod.setProducerStop();
           prod = null;
        }    
    }    
    
    /**
     * Stop thread for All Channels
     */
    public synchronized void stopConsumer(int consNumb) {

            QueueVector queue = queueM[consNumb];
            Consumer consumer = consM[consNumb];
                     
            if (consumer != null ) {
                 consumer.setConsumerStop();
                 // consumer = null;
            }               
            else {
                Print.show("Consumer " +(consNumb+1) + " is not initialized");  
                return;
            }    
            
            if ( queue != null ) {
                queue.setConsumerStopFlag();
                queue.clearQueue();
                // queue = null;
            }   
            else {
               Print.show( 
                          " Queue " + (consNumb+1) + " is not initialized");                     
               return;                 
            }    
    }  
    
   
    public synchronized boolean put(EventM event)
                               throws java.lang.InterruptedException { 
                                   
        long id = event.getCommonId();
        
        if (id == -1) {
            Print.show(
                    "NotificationId " + id + " is discarded, NotifId isn't valid "); 
            return false;            
        }    
                        
        QueueVector queue;   
        
        for ( int i = 0; i < numbOfConsumers; i++) {

            queue = queueM[i];
                    
            if (queue != null ) {                   
                   try {  // Match checking in the putWaitWhileFullWithTimeout
                       switch (i) {
                           case 0: 
                            if (id%5 == 0) // 20% Filter
                               queue.putWaitWhileFullWithTimeout(event);                     
                           break;
                           
                           case 1:
                             if (id%3 == 0) // 33% Filter
                               queue.putWaitWhileFullWithTimeout(event);      
                           break;  
                           
                           case 2:  
                             if (id%2 == 0) // 50% Filter
                               queue.putWaitWhileFullWithTimeout(event);      
                           break;
                           
                           case 3:  // Every event matched
                           default:    
                               queue.putWaitWhileFullWithTimeout(event);        
                           break;                                   
                       }
                       

                   } catch (java.lang.InterruptedException ex) {
                       Print.show(  
                       "put: InterruptedException" + ex.toString()); 
                   }        
            }  
            else {
               Print.show("put: Queue is null, Queue Number = " + i);                    
            }    
        } // end of while of Queues    
        
        return true;
    }
                            
    public synchronized void terminate(){
       
        Consumer consumer;
        QueueVector queue;
        
        
        for ( int i = 0; i < numbOfConsumers; i++) {
            
            consumer = consM[i];
            queue = queueM[i];
            
             if (consumer != null ) {                        
                 if ( queue != null ) {                   
                   queue.setConsumerStopFlag(); 
                   queue.clearQueue();          
                   queue = null;
                   // q.finalize();
                 }
                 else {
                   Print.show(" Queue # " + i + " isn't initilaised");  
                 }
                 
                 consumer.setConsumerStop();                 
                
             }
             else {
                  Print.show("Consumer #" + i + " isn't initilised"); 
             }                                       
        }        
        
        if ( prod != null ) {
             prod.setProducerStop();
             prod = null;
        }                   
    }    
       
}
