/*
 * Event.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.model;

/**
 *
 * 
 */
public class EventM {

    private static long idCommon = 0;
    String message;
    
    public void initCommonId() {
        idCommon = 0;
    }    
    
    /** Creates new Event */
    public EventM() {
        message = new String("Message" + String.valueOf(idCommon));     
        idCommon++;
    }
    
    public long getCommonId() {
        return idCommon;
    }
    
}
