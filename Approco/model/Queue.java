/*
 * Queue.java
 *
 * Created on 12 מרץ 2002, 21:44
 */

package Approco.model;

/*
 * Queue.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

public interface Queue {

  public void putWaitWhileFullWithTimeout(EventM elem) 
                    throws java.lang.InterruptedException; 
  public EventM[] getStructuredEventBatch() throws InterruptedException;
  
  public void clearQueue();
}
