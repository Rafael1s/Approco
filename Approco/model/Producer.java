/*
 * Producer.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

import Approco.control.*;


public final class Producer extends AbstractProducer {
    
    private long total_sent;
    EventM event;     
    
    private volatile int putTimeout;
    
    public void setPutTimeout(int putT) {
        putTimeout = putT;
    }
  
    
    /** Creates new ConsumerForwarder */
    public Producer() 
    {           
        total_sent = 0;      

        putTimeout = Params.putTimeout;
        // Thread Name
        setName("Producer");
        // runFlag = stop
        setProducerStop(); 
    }       
                      
    public void run() {
         
        int k = 0; // How many poll loops

        Print.show( "Producer run is entered ... ");  
        
        QueueVector queue;         
    
        while (true) { // && getProducerRunFlag() ) { 
                  
                waitForStart();                
            
                try {                
                    event = new EventM();
                    
                    if (event == null) {
                      continue;   
                    }               
                    
                    MultiQueue.getInstance().put(event);
                    
                    QueueTM.getInstance().put(this.getName());
                    
                    Thread.sleep(putTimeout);
                                      
                } 
                catch (InterruptedException e) {
                    Print.show(
                       " Exception, getBatch " + e + " in Producer Run");
                    setProducerStop();
                    // Here we already must know the corresponding Producer
                    return;
               } 
                 
               
             k++; 
          } // end of while        
   }// end of run
     
}
