/*
 * Listeners.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.control;

import java.awt.event.*;
import java.awt.*;

import Approco.view.*;
import Approco.model.*;
// import Approco.*;

import java.applet.*;
import java.net.*;
import java.util.*;

/**
 *  All Listeners are here except ListenerURLLabel for Applet
 */
public class Listeners {

    private static ListenerModeQueueChoice listenterMGC = null;
    private static ListenerConsumerChoice listenerConsumerChoice = null;
    private static ListenerProducerPeriodSlider listenerProducerPeriodSlider = null;
    
    private static final Listeners instance = new Listeners();    
    public static Listeners getInstance() {
        return instance;
    }   
    
    /** Creates new Listeners */
    private Listeners() {
        listenterMGC = new ListenerModeQueueChoice();
        // listenerURLLabel = new ListenerURLLabel();
        listenerConsumerChoice = new ListenerConsumerChoice();
        listenerProducerPeriodSlider = new ListenerProducerPeriodSlider();
    }    
}

class ListenerConsumerChoice implements ItemListener {
    Choice consumerChoice;
    ConsumerButton[] buttonCons;
    
    Font boldFont = Params.helveticaBold10; 
    
    // Observer
    ListenerConsumerChoice() {
        consumerChoice = ApprocoPanel.getInstance().getConsumerChoice();
         // Observer (this) attached to Observable (control Choice)
        consumerChoice.addItemListener(this);
        buttonCons = ApprocoPanel.getInstance().getButtons();            
    }    
        
    public void itemStateChanged(java.awt.event.ItemEvent ie) {
         if (ie.getStateChange() == ItemEvent.SELECTED) {
            PCCanvas.getInstance().repaint();
       
            Print.show(" Chosen " + (consumerChoice.getSelectedIndex()+1) + " consumers" );
            switch (consumerChoice.getSelectedIndex()) {
                case 0:
                    buttonCons[1].setVisible(false);
                    buttonCons[1].setEnabled(false);
                    buttonCons[2].setVisible(false);
                    buttonCons[2].setEnabled(false);
                    buttonCons[3].setVisible(false);
                    buttonCons[3].setEnabled(false);
                    
                    setHiddenButtonToStop(buttonCons[1]);
                    setHiddenButtonToStop(buttonCons[2]);
                    setHiddenButtonToStop(buttonCons[3]);
                    break;    
                case 1:
                    buttonCons[1].setVisible(true);
                    buttonCons[1].setEnabled(true);
                    buttonCons[2].setVisible(false);
                    buttonCons[2].setEnabled(false);
                    buttonCons[3].setVisible(false);
                    buttonCons[3].setEnabled(false);

                    setHiddenButtonToStop(buttonCons[2]);
                    setHiddenButtonToStop(buttonCons[3]);
                    break;    
                case 2:
                    buttonCons[1].setVisible(true);
                    buttonCons[1].setEnabled(true);
                    buttonCons[2].setVisible(true);
                    buttonCons[2].setEnabled(true);
                    buttonCons[3].setVisible(false);
                    buttonCons[3].setEnabled(false);           

                    setHiddenButtonToStop(buttonCons[3]);
                    break;
                case 3:
                    buttonCons[1].setVisible(true);
                    buttonCons[1].setEnabled(true);
                    buttonCons[2].setVisible(true);
                    buttonCons[2].setEnabled(true);
                    buttonCons[3].setVisible(true);
                    buttonCons[3].setEnabled(true);                    
                    break;                        
            }    
        }  
    }
    
    private void setHiddenButtonToStop(ConsumerButton button) {                    
        if (button.isCurrentNameEqualStop()) {
             button.changeLabelToStop();
             return;
        }     
    }        
}    

class ListenerModeQueueChoice implements ItemListener {
    Choice modeGetChoice;
    
    public static final int GET_100_QUEUE = 0;
    public static final int GET_050_QUEUE = 1;
    public static final int GET_025_QUEUE = 2;
    
    // Observer
    ListenerModeQueueChoice() {
        modeGetChoice = ApprocoPanel.getInstance().getModeQueueChoice();
        
        // Observer (this) attached to Observable (control Choice)
        modeGetChoice.addItemListener(this);
    }    
    
    public void itemStateChanged(java.awt.event.ItemEvent ie) {
        if (ie.getStateChange() == ItemEvent.SELECTED) {
             switch (modeGetChoice.getSelectedIndex()) {
                  case GET_100_QUEUE: 
                     Print.show("Get From Queue 100%");
                     setMinBatchForAllQueues(Params.minBatch); 
                  return;
                  
                  case GET_050_QUEUE:
                     Print.show("Get From Queue 50%");
                     setMinBatchForAllQueues(Params.minBatch/2);                   
                  return;
                  
                  case GET_025_QUEUE:
                     Print.show("Get From Queue 25% ");
                     setMinBatchForAllQueues(Params.minBatch/4);                   
                  return;
             }    
        }    
    }
    
    private void setMinBatchForAllQueues(int batch) {
        for (int i = 0; i < Params.numbOfConsumers; i++ ) {
            if (MultiQueue.getInstance().getQueue(i) != null) {
                MultiQueue.getInstance().getQueue(i).setMinBatch(batch);
            }   
        }    
    }    
    
}    

class ListenerProducerPeriodSlider implements AdjustmentListener {
    Scrollbar pcSlider;
    Label labProducerPeriod;
    
    //Observer
    ListenerProducerPeriodSlider() {
        pcSlider = ApprocoPanel.getInstance().getPCSlider();        
        // Observer (this) attached to Observable (control Scrollbar)
        pcSlider.addAdjustmentListener(this);
        
        labProducerPeriod = ApprocoPanel.getInstance().getProducerPeriodLabel();
    }
    
    public void adjustmentValueChanged(AdjustmentEvent e) {
       int putT; 
       int val = e.getValue();
       if (val%10 == 0 ) {
         putT = val*10;  
         labProducerPeriod.setText("Production period: " + String.valueOf(putT) + " ms");
         MultiQueue.getInstance().getProducer().setPutTimeout(putT);
       }
    }    
    
}    

