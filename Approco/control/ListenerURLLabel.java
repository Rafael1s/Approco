/*
 * ListenerURLLabel.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.control;

import Approco.*;

import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.applet.*;

public class ListenerURLLabel implements MouseListener {
    
    Label urlLabel;
    ApprocoApplet ap;
    
    // ListenerURLLabel() {
    public ListenerURLLabel(ApprocoApplet apTmp) {
       // urlLabel = PCApplet.getInstance().getURLLabel();
       ap = apTmp;
       urlLabel = ap.getURLLabel();
        
       // Observer (this) attached to Observable (control Label)
       urlLabel.addMouseListener(this);
    }   
    
    public void mouseClicked(java.awt.event.MouseEvent e ) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e ) {
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e ) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e ) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e ) {
       Print.show(" mousePressed is happened "); 
       URL url = null;
       AppletContext ac = null;
        
       ac = ap.getAppletContext();
       ap.showStatus(" ApprocoApplet = " + ap);        

           
        try {
          url = new URL(urlLabel.getText());
        } catch (java.net.MalformedURLException me) {
          Print.show(" MalformedURLException = " + me.toString());
          return;
        }    

        ap.stop();
        ap.showStatus("Approco stopped");
        // ap.destroy();
        // ap.showStatus("Approco stopped and destroyed");
        ac.showDocument(url); 
    }    
}    
