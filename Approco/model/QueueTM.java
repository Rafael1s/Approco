/*
 * QueueTM.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

import java.util.*;

public class QueueTM {
    
    protected Vector threadQueue;
  
    private int capacity = 30;

    private static final QueueTM instance = new QueueTM();
    
    public static QueueTM getInstance() {
        return instance;
    }    
    
    /** Creates new QueueTM */
    private QueueTM() {
        threadQueue = new Vector(capacity);    
    }

    private int getSize() {
      return threadQueue.size();
    }   
    
    public synchronized void put(String name) {
        if (getSize() > 0) {
          String last = (String)threadQueue.lastElement();
        
          if (last.compareTo(name) == 0 ) {           
             return;
          }    
          else {
             while (getSize() >= capacity ) {
                threadQueue.removeElementAt(0);                        
             }                   
          }    
        }   
         
        threadQueue.addElement(name);
        
    }    
    
    public synchronized String get() {
        String name;
        
        if (getSize() > 0) {
           name = (String)threadQueue.elementAt(0);
           threadQueue.removeElementAt(0);  
           return name;
        }
        else {
           return "Thread Monitor"; 
        }    
        
    }    
}
