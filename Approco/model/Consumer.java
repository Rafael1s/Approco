/*
 * Consumer.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

import Approco.control.*;

public final class Consumer extends AbstractConsumer {
        
    EventM [] eventsToForward;      
    
    /** Creates new ConsumerForwarder */
    public Consumer(int number) 
    {           
        initTotalUsedUp();
        consNumb = number;
        
        // Thread Name
        setName(new String("Consumer " + String.valueOf(consNumb+1)) );
        // runFlag = stop
        setConsumerStop(); 
    }
     
    
        
        
    public void run() {
         
        int k = 0; // How many poll loops

        // Print.show( "Consumer run is entered ... ");  
        
        QueueVector queue = MultiQueue.getInstance().getQueue(consNumb); 
        
        if (queue == null) {
           Print.show( "run: Queue is null for channel " + consNumb);  
           return;            
        }    
        

        Print.show("Consumer Run  " + (consNumb+1) + " is entered");
        
        while (true) { // k < 100 ) 
                        
            try {    
                
                QueueTM.getInstance().put(this.getName());

                eventsToForward = queue.getStructuredEventBatch();                                
                
                if (eventsToForward == null) {
                   // consumerRunFlag should be False
                   Thread.sleep(Params.getTimeout);
                   k++;
                   continue;   
                }
                
            } 
            catch (InterruptedException e) {
                Print.show(
                      " Exception, getBatch " + e + " in Consumer Run, consNumb" + consNumb);
                setConsumerStop();
                // Here we already must know the corresponding Producer
                return;
            }                             
                
             // totalUsedUp = totalUsedUp + eventsToForward.length;
            setTotalUsedUp(eventsToForward.length);
            k++; 
          } // end of while
        
     }// end of run
        
   
  public boolean do_push(EventM [] events)
  {   
    int n = 0;
 
    // only draw "total_sent"        

    return true;
  }  


}
