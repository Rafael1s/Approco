/*
 * PCButton.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import Approco.model.*;
import Approco.control.*;


public class ConsumerButton extends Button implements ActionListener {
    Font plainFont = Params.helveticaPlain10; 
    Font boldFont = Params.helveticaBold10;     

    private String name;
    private String start;
    private String stop;
    private int numbOfButton;  

    public ConsumerButton(String str,int numb) {
        super(str);
        numbOfButton = numb;
        name = str;
        addActionListener(this);
        setFont(plainFont);
        
        if (numbOfButton != ApprocoPanel.NUMB_OF_PRODUCER_BUTTON) {
           start = new String("Start Consumer " + String.valueOf(numb+1));
           stop  = new String("Stop Consumer " + String.valueOf(numb+1));
        }
        else {
           start = new String("Start Producer");
           stop  = new String("Stop Producer");            
        }   
        
        if(isCurrentNameEqualStop()){
            setFont(boldFont);
        }    
        
        if (isCurrentNameEqualStart()) {
            setFont(plainFont);
        }    
    }
    
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        String name = ae.getActionCommand();
          
        if (isCurrentNameEqualStop()) {
               changeLabelToStop();
               return;
        }       
       
        if (isCurrentNameEqualStart()) {
               changeLabelToStart();
               return;
        }                               
   }
   
   
   public void changeLabelToStart() {                    
        setFont(boldFont);
        setLabel(stop);
        name = stop;
        if (numbOfButton != ApprocoPanel.NUMB_OF_PRODUCER_BUTTON) {
           MultiQueue.getInstance().startConsumer(numbOfButton);
        }
        else {
           MultiQueue.getInstance().startProducer(); 
        }    
        return;
   }    
   
   public void changeLabelToStop() {
        setFont(plainFont);
        setLabel(start);
        name = start;
        if (numbOfButton != ApprocoPanel.NUMB_OF_PRODUCER_BUTTON) {
           MultiQueue.getInstance().stopConsumer(numbOfButton);
        }
        else {
           MultiQueue.getInstance().getProducer().setProducerStop();
        }    
        return;                      
   }    
   
   
   public boolean isCurrentNameEqualStop() {   
       if (name.compareTo(stop) == 0 )
           return true;
       else
           return false;
   }         
   
   private boolean isCurrentNameEqualStart() {   
       if (name.compareTo(start) == 0 )
           return true;
       else
           return false;
   }         
   
   /*
   public String getStopName() {   
       return stop;
   }    

   public String getCurrentName() {   
       return stop;
   }    
   */
}
 
