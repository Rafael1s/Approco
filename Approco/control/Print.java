/*
 * Print.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.control;

import Approco.*;

public class Print  {

    // Java Console (for applet) or Output Window (for applicaton)
    public static final int OUTPRINTLN = 0; 
    // Status Bar ( for applet only)
    public static final int SHOWSTATUS = 1;
    
    private static ApprocoApplet ap;
    private static int outType = 0;
    
    /** Creates new Print */
    public Print(ApprocoApplet apInp, int outTypeInp) {
        ap = apInp;
        outType = outTypeInp;
    }
        
    public synchronized static void show(String str) {
        // System.out.println(" ap = " + ap);
        switch (outType) {
            case SHOWSTATUS:    
                ap.showStatus(str);
                return;
            case OUTPRINTLN:
            default:    
                System.out.println(str);
                return;
        }     
    }    
    
    public synchronized static void setPrintlnOut() {
        outType = OUTPRINTLN;
    }    
}
