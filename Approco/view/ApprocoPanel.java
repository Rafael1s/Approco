/*
 * PCPanel.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.view;

import java.awt.*;
import java.awt.event.*;
import Approco.model.*;
import Approco.control.*;
// import javax.swing.*;

public class ApprocoPanel extends Panel {

    // private static TMCanvas tmCanvas = null;
    // private static PCCanvas pcCanvas = null;
    // boolean af (applet/frame), true = applet, false = frame
    
    // 0,1,2,3 - for Consumers, One for producer = 4
    private static int numbOfButtons = Params.numbOfConsumers; // 4
    private static Choice consChoice = null;
    private static Choice modeGetChoice = null; 
    private static Label labProducerPeriod = null;
    private static Scrollbar pcSlider = null;       
    private static ConsumerButton[] buttonCons = new ConsumerButton[numbOfButtons]; // 4
    private static ConsumerButton buttonProd = null;
    
    public static final int NUMB_OF_PRODUCER_BUTTON = 100;

    private ApprocoPanel() {
        for (int i = 0; i < numbOfButtons; i++) {
            buttonCons[i] = null;
        }    
           
        // buttonCons[0] = new PCButton("Start Consumer 1", 0);
        buttonCons[0] = new ConsumerButton("Stop Consumer 1", 0);
        
        
        buttonCons[1] = new ConsumerButton("Start Consumer 2", 1);
        //buttonCons[1].setVisible(false);
        //buttonCons[1].setEnabled(false);
        
        buttonCons[2] = new ConsumerButton("Start Consumer 3", 2);
        //buttonCons[2].setVisible(false);
        //buttonCons[2].setEnabled(false);

        buttonCons[3] = new ConsumerButton("Start Consumer 4", 3);
        //buttonCons[3].setVisible(false);
        //buttonCons[3].setEnabled(false);

        // buttonProd = new PCButton("Start Producer", NUMB_OF_PRODUCER_BUTTON);
        buttonProd = new ConsumerButton("Stop Producer", NUMB_OF_PRODUCER_BUTTON);
        
        modeGetChoice = new Choice();
        consChoice = new Choice();        
        TMCanvas.getInstance();        
        PCCanvas.getInstance(); 
        
        labProducerPeriod = 
                new Label("Production period: " + String.valueOf(Params.putTimeout) );   
        int val = Params.putTimeout/10;
        // minimum = 200/10 = 20, maximum = 2000/10 = 200
        // 0 - visiable, current val = putTimeout/10
        pcSlider = new Scrollbar(Scrollbar.HORIZONTAL, val, 0, 20, 200);

    }
    
    private static ApprocoPanel instance = new ApprocoPanel();
    public static ApprocoPanel getInstance() {
        
        int i = 0;
        while (instance == null) {
           Print.show(" ApprocoPanel = null, i = " + i); 
           instance = new ApprocoPanel(); 
           try {                     
                    Thread.sleep(500);  
               } catch ( java.lang.InterruptedException ie) {}
               if ( i > 5 ) {
                       Print.show(" i > 5: ApprocoPanel = null, i = " + i);
                       System.exit(1);
               }
        } 
        
        return instance;
    }    
    
    /** Creates new ExPanel */
    public void initPCPanel(boolean af) {
        Font font = Params.helveticaPlain10;                        
        
        this.setLayout(new BorderLayout()); // GridLayout(2,0, 20, 20));
        
        Panel top = new Panel();
        top.setLayout(new FlowLayout()); //GridLayout(0,2, 0, 0));
        top.add(TMCanvas.getInstance()); 
        top.add(PCCanvas.getInstance()); 
        this.add("North", top);
        
        
        Panel bot = new Panel();
        // bot.setBackground(Params.consolCol);
       
        bot.setLayout(new GridLayout(0,3, 30, 10)); 
   
        // Row 1
        consChoice.addItem("One Consumer");
        consChoice.addItem("Two Consumers");
        consChoice.addItem("Three Consumers");
        consChoice.addItem("Four Consumers");
        consChoice.setFont(font);
        bot.add(consChoice);  //1        
        
        modeGetChoice.addItem("Get 100% of Queue");
        modeGetChoice.addItem("Get 50% of Queue");
        modeGetChoice.addItem("Get 25% of Queue");
        modeGetChoice.setFont(font);
        bot.add(modeGetChoice); // 2
        // MultiQueue.getInstance().setModeGetChoice(modeGetChoice);
        
        // bot.add(new PCButton("Stop Producer")); //3      
                
        bot.add(buttonProd); //3          
                
        // Row 2           
        bot.add(buttonCons[0]); //4
        bot.add(buttonCons[1]); //4
        bot.add(buttonCons[2]); //4
        bot.add(buttonCons[3]); //4
        
        labProducerPeriod.setFont(font);
        bot.add(pcSlider);
        bot.add(labProducerPeriod);
        this.add(bot);
                         
        /*
        if ( af == false ) {
           startCanvases();
        }  
         */
        
        buttonCons[0].changeLabelToStart();
        buttonCons[1].changeLabelToStart();
        buttonCons[2].changeLabelToStart();
        buttonCons[3].changeLabelToStart();
        buttonProd.changeLabelToStart();
        
        consChoice.select(3); // Four Consumers are selected
        
        if ( af == false ) {
           startCanvases();
        }  
    }
    
    // if af == true (applet) this method should be invoked
    // from the start() of Applet
    public void startCanvases() {
        // MultiQueue.getInstance();
        TMCanvas.getInstance().start();
        TMCanvas.getInstance().setTMCanvasRun();
        PCCanvas.getInstance().start();         
        PCCanvas.getInstance().setPCCanvasRun();   
    }          
    
    public void stopCanvases() {
        PCCanvas.getInstance().setPCCanvasStop();
        PCCanvas.getInstance().setPCThreadAndPCCanvasNull();
        TMCanvas.getInstance().setTMCanvasStop();   
        TMCanvas.getInstance().setTMThreadAndTMCanvasNull();
        
        MultiQueue.getInstance().terminate();
        MultiQueue.getInstance().setMultiQueueNull();        
        this.instance = null;
    }    
    
    public Choice getModeQueueChoice() {
        return modeGetChoice;
    }   
    
    public Choice getConsumerChoice() {
        return consChoice;
    }    
    
    public Scrollbar getPCSlider() {
        return pcSlider;
    }    
    
    public Label getProducerPeriodLabel() {
        return labProducerPeriod;
    }    
    
    public static ConsumerButton[] getButtons() {
        return buttonCons;
    }    
}
    


