/*
 * QueueVector.java
 *
 * Created in Mart 2002 
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

import java.util.*;

import Approco.control.*;

public class QueueVector implements Queue { 

  protected Vector elemQueue;
  
  private int capacity; // Maximal Queue Size
  private long get_timeout;
  private int min_batch;
  
  private boolean consumerRun;
  private int consNumb;
    
  // Constructor  
  public QueueVector(int number) {
      // Get parameters values from the Params 
      capacity = Params.capacity;
      get_timeout = Params.getTimeout; // Must be > 0 to avoid wait(0);
      min_batch = Params.minBatch;
  
      if(capacity <= 0)
         capacity = 1; // Max Queue Size MUST be  > 1
      
      elemQueue = new Vector(capacity);     
      
      consumerRun = false;
      consNumb = number;        
  }    
  
  
  private boolean isEmpty(){
     return elemQueue.isEmpty();
  }
  
  private boolean isFull() {
     int tmpSize = getSize(); 
     return (capacity <= tmpSize);
  }
  
  public int getSize() {
      return elemQueue.size();
  }    
         
  public synchronized void setMinBatch(int choicedBatch) {
      min_batch = choicedBatch;
  }    
  
  private synchronized void waitForBatchWithTimeout()
                throws java.lang.InterruptedException
  {
      long start = System.currentTimeMillis();
      long endTime = start + get_timeout;
      long msRemaining = get_timeout;
      long now;       
   
      while ( (getSize() < min_batch) && ( msRemaining > 0 ) ) {
           try {
             wait(msRemaining);
           } catch ( java.lang.InterruptedException ex) {
            Print.show(
               "waitForBatchWithTimeout: , ChannelType " + consNumb + ex.toString());     
           }     
           now = System.currentTimeMillis();
           msRemaining = endTime - now;
      }      
  }    
  
  public void finalize() {
          Print.show("Queue is deleted, ChannelType " + consNumb);      
  }
  
  private synchronized void waitWhileEmptyAndConsumerRun() 
               throws java.lang.InterruptedException
  {
      // Used in the function get => wait if queue is empty
      // Function add send notifyAll and wait is freeing
      try {
        while (isEmpty() && isConsumerRun() ) {
            wait();
        }    
      } catch (java.lang.InterruptedException efExc) {
            Print.show(
            "waitWhileEmptyAndConsumerRun: ChannelType " + consNumb + efExc.toString());     
      }    
      
      // Don't notify, becuse it is invokated only in waitUntilBatchCollected
  }
    
  private boolean isConsumerRun() {
      return consumerRun;
  }    

  public synchronized void setConsumerRunFlag() {
     consumerRun = true;   
     
     notifyAll();
     
  }    

  public synchronized void setConsumerStopFlag() {
     consumerRun = false;   
     
     notifyAll();          
  }    
  
  // return length of new batch 
  // Timeout is set in milliseconds
  private synchronized boolean waitUntilBatchCollected() 
			throws InterruptedException {
        
        int tmpSize;
        
        waitWhileEmptyAndConsumerRun();
     
        if( isConsumerRun() == false ) {
          return false;
        }    
        else {                           
          waitForBatchWithTimeout();
          return true;
        }
        
        // Don't notify, becuse it is invokated only in getStructuredEventBatch
  }
     
  public synchronized EventM[] getStructuredEventBatch() throws InterruptedException {
        
        if (waitUntilBatchCollected() == false )
            return null;
        
        if( getSize() == 0 ) {
           Print.show("Size of Queue " + (consNumb+1) + " is 0");
           return null;
        }    
                
        int len = getSize();
        
        if (min_batch == Params.minBatch/2 && len > 1) {
            len = len/2;
        }    
        if (min_batch == Params.minBatch/4 && len > 3) {
            len = len/4;
        }
        
        EventM[] list = new EventM[len]; // use the current size

	for ( int i = 0; i < len; i++ ) {
		// list[i] = (EventM)elemQueue.remove(0);  // get();
		list[i] = (EventM)elemQueue.elementAt(i);  // get();
	}
        
        for ( int i = 0; i < len; i++ ) {
		elemQueue.removeElementAt(0);
	}

        notifyAll();

	// if FIFO was empty, a zero-length array is returned
	return list;
  }
  
  public synchronized void putWaitWhileFullWithTimeout(EventM elem) 
               throws java.lang.InterruptedException
  {           
      if( isConsumerRun() == false ) {
          return;
      }    
         
      if (isFull()){ 
         Print.show( " !!! Discarded by timeout, Queue " + (consNumb+1) + " is full");
         clearQueue();
         return;
      }
      
      // elemQueue.add(elem);
      elemQueue.addElement(elem);
            
      // notify all waiting threads about change
      notifyAll();      
  }   
  
  // Removes all of the elements from Queue
  public synchronized void clearQueue() {
      Print.show( "Remove " + getSize() + " elements from Queue " + (consNumb+1));
      //elemQueue.clear();    
      elemQueue.removeAllElements();    
            
      notifyAll();
  }
}
