/*
 * PCApplet.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco;


import java.awt.*;
import java.applet.*;

import Approco.control.*;
import Approco.view.*;
import Approco.model.*;

import java.awt.event.*;

public class ApprocoApplet extends Applet {
  
    private static Label urlLabel = null;
    private static Print prn = null;
    
    //private Frame window;
    private static ListenerURLLabel listenerURLLabel = null;    
 
    
    public Label getURLLabel() {
        return urlLabel;
    }    
    
    public void init() {
                
        setLayout(new BorderLayout());        

        // true - means Applet, false for Frame
        ApprocoPanel.getInstance().initPCPanel(true);       
        
        add("North", ApprocoPanel.getInstance());             
        
        Panel refPanel = new Panel();                
        //Label text = new Label("Details see on the  ");              
        urlLabel = new Label("http://cities.lk.net");
        //urlLabel = new Label("http://www.yahoo.com");

        urlLabel.setForeground(Color.blue); 
        //refPanel.add(text);
        refPanel.add(urlLabel);
        add("South", refPanel);
                
        resize(650,470);         
        
  
        listenerURLLabel = new ListenerURLLabel(this);
        
        
        
        //Create but don't show window.
        // window = new Frame("Window Event Window");
        // window.addWindowListener(this);
        
        showStatus("Applet init completed");
    }

    public void start() {
        prn = new Print(this, Print.SHOWSTATUS);
        // Print pr = new Print(this, 0);
        showStatus(" Applet started ...");
        MultiQueue.getInstance();
        ApprocoPanel.getInstance().startCanvases();                
              
    }      
    
    public void stop() {       
        Print.setPrintlnOut();
        ApprocoPanel.getInstance().stopCanvases();
    }                   
}
