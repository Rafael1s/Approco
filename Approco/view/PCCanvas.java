/*
 * PCCanvas.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.view;

import java.awt.*;
import java.awt.event.*;

import Approco.control.*;
import Approco.model.*;

public class PCCanvas extends Canvas implements Runnable { 

    private Thread thPCCanvas = null;
  
    private Image offscreen;
    // private Dimension offscreensize;
    private Graphics offgraphics;
    Chain4Boxes chB1, chB2, chB3, chB4;
    
    // private final static Color consolCol = new Color(0x77, 0xff, 0xcc);
    
    private String title = "Producer Consumer Console";
    private int xOffset, xOrig;
    private int yOffset, yOffsetTitle;
    private Font prodFont;

    private int numbSelected = 0;
    
    private boolean redrawConsole;
    
    private volatile boolean runPCCanvas;
    // 3 functions below: removed synchronized, runFlag changed to volatile
    public void setPCCanvasStop() { 
        runPCCanvas = false;
    }
    public void setPCCanvasRun() { runPCCanvas = true; }
    public boolean getPCCanvasFlag() { return runPCCanvas; }  
    
    public void setPCThreadAndPCCanvasNull(){
        thPCCanvas = null;
        instance = null;
    }    
    
    private static  PCCanvas instance = new PCCanvas();
    public static PCCanvas getInstance() {
        
        int i = 0;
        while (instance == null) {
           Print.show(" PCCanvas = null, i = " + i); 
           instance = new PCCanvas(); 
           try {                     
                    Thread.sleep(500);  
               } catch ( java.lang.InterruptedException ie) {}
               if ( i > 5 ) {
                       Print.show(" i > 5: PCCanvas = null, i = " + i);
                       System.exit(1);
               }
        }    
         
        return instance;
    }  
    
    
    private PCCanvas() {        
         yOffset = 30;       
         yOffsetTitle = 15;
         xOffset = xOrig = 200;       
         prodFont = Params.timesItalic12;
         
         redrawConsole = true;
         // It's very important
         setSize(Params.wPc + 1, Params.hPc + 1);         
    }
    
    private synchronized Image getPCImage() {
        if (offscreen == null)  { 
                // addNotify();
                int i = 0;
                while ( offscreen == null ) {
	           offscreen = createImage(Params.wPc + 1, Params.hPc + 1);
                   i++;
                   Print.show(" offgraphics = null, i = " + i);
                   try {  
                     Thread.sleep(500);  
                   } catch ( java.lang.InterruptedException ie) {}
                   if ( i > 5 ) {
                       Print.show(" i > 5: offgraphics = null, i = " + i);
                       System.exit(1);
                   }    
                }   
	        offgraphics = offscreen.getGraphics();         
	        offgraphics.setFont(prodFont);
                if ( offgraphics == null ) {
                    Print.show(" offgraphics = null");
                    System.exit(1);
                }    
                Print.show(" Image Created ");

                initChains(offgraphics);
        }
        
        return offscreen;
    }    

    public void update(Graphics g) {
        paint(g);
    }
    public void paint(Graphics g) {       

       offscreen = getPCImage();
       
       if (redrawConsole) {
         displayConsoleBG();
         Print.show(" Display Producer Consumer Console ");
         redrawConsole = false;
       }
       
       displayTitle();
       animateChains(offgraphics);
       
       g.drawImage(offscreen, 0, 0, null);
    }    
    
    private void displayConsoleBG() {
       offgraphics.setColor(Params.consolCol); 
       offgraphics.fillRect(0, 0, Params.wPc, Params.hPc);    
       offgraphics.setColor(Color.red); 
       offgraphics.drawRect(0, 0, Params.wPc, Params.hPc);   
    }        
    
    private void displayTitle() {         
        offgraphics.setColor (Params.consolCol);
        offgraphics.fillRect(1, 1, Params.wPc-1, 30);
        
        xOffset--; 

        if (xOffset < 10)
		xOffset = xOrig;             
        offgraphics.setColor (Color.black);
        offgraphics.setFont(prodFont);
        offgraphics.drawString (title, xOffset, yOffsetTitle);
    }                     
    
    public void run() {
            
        int i = 0;
        
        offscreen = getPCImage();
        Graphics g = offscreen.getGraphics();
        
        Print.show(" PC Canvas is running "); 

        while(getPCCanvasFlag()) {
          try {  
            if (i%5 == 0)  
               QueueTM.getInstance().put(thPCCanvas.getName());
 
            Thread.sleep(Params.pcPeriod);  
          } catch ( java.lang.InterruptedException ie) {}
           
          repaint();            
        }
    }
    
    public void start() {
		// Check to see if thread is active
		if (thPCCanvas == null)
		{
			// Pass our thread an instance of java.lang.Runnable (us)
			thPCCanvas = new Thread(this, "PC Console");       
                        Print.show(" PCCanvas is started "); 
			thPCCanvas.start();
		}
    }

    private void drawProducerBox(Graphics g, Chain4Boxes chB) {
        int x = chB.getRightSideOfChain();
        
        Boxes.drawColoredBoxBorder(
                    g, x, yOffset, 20, 280, Params.producerCol, Color.red);     
                    //g, x, yOffset, 20, 280, Params.consumerCol2, Color.red); 
        
        displayTitleProducer(g, x, yOffset, 20, 280);
    }    
    
    private void initChains(Graphics g) {
                                       
             switch (ApprocoPanel.getInstance().getConsumerChoice().getSelectedIndex()) {
               case 0: 
                chB1 = new Chain4Boxes(
                   g, (int)1, 0, yOffset + 120, Params.wPc, 40,
                     Params.consumerCol1, Color.blue, Color.red);                
                break;
                
               case 1:  
                chB1 = new Chain4Boxes(  
                  g, (int)1, 0, yOffset, Params.wPc, 40,
                     Params.consumerCol1, Color.blue, Color.red);
                chB2 = new Chain4Boxes(
                  g, (int)2, 0, yOffset + 240, Params.wPc, 40,
                      Params.consumerCol2, Color.blue, Color.red);
              
                break;
                
              case 2:  
                chB1 = new Chain4Boxes(  
                  g, (int)1, 0, yOffset, Params.wPc, 40,
                     Params.consumerCol1, Color.blue, Color.red);
                chB2 = new Chain4Boxes(
                  g, (int)2, 0, yOffset + 120, Params.wPc, 40,
                      Params.consumerCol2, Color.blue, Color.red);
                chB3 = new Chain4Boxes(
                  g, (int)3, 0, yOffset + 240, Params.wPc, 40,
                      Params.consumerCol3, Color.blue, Color.red);
                break;
                
              case 3:  
                chB1 = new Chain4Boxes(  
                  g, (int)1, 0, yOffset, Params.wPc, 40,
                     Params.consumerCol1, Color.blue, Color.red);
                chB2 = new Chain4Boxes(
                  g, (int)2, 0, yOffset + 80, Params.wPc, 40,
                      Params.consumerCol2, Color.blue, Color.red);
                chB3 = new Chain4Boxes(
                  g, (int)3, 0, yOffset + 160, Params.wPc, 40,
                      Params.consumerCol3, Color.blue, Color.red);
                chB4 = new Chain4Boxes(
                  g, (int)4, 0, yOffset + 240, Params.wPc, 40,
                      Params.consumerCol4, Color.blue, Color.red);
                break;

             }    
             

             drawProducerBox(g, chB1);

        }    

    
        private void animateChains(Graphics g) {
                           
               switch (ApprocoPanel.getInstance().getConsumerChoice().getSelectedIndex()) {
                           case 0: 
                            if (numbSelected != 0) {   
                                initChains(g);
                                numbSelected = 0;
                                redrawConsole = true;
                            }    
                            chB1.animateBoxes(g);             
                            break;
                           
                           case 1:
                            if (numbSelected != 1) {   
                                initChains(g);
                                numbSelected = 1;
                                redrawConsole = true;
                            }       
                            chB1.animateBoxes(g);
                            chB2.animateBoxes(g);
                            break;
                           
                           case 2:
                             if (numbSelected != 2) {   
                                initChains(g);
                                numbSelected = 2;
                                redrawConsole = true;
                             }                                      
                             chB1.animateBoxes(g);
                             chB2.animateBoxes(g);
                             chB3.animateBoxes(g);
                            break;  
                            
                            case 3:
                             if (numbSelected != 3) {   
                                initChains(g);
                                numbSelected = 3;
                                redrawConsole = true;
                             }                                      
                             chB1.animateBoxes(g);
                             chB2.animateBoxes(g);
                             chB3.animateBoxes(g);
                             chB4.animateBoxes(g);
                            break;  
               }      
                         
               drawProducerBox(g, chB1);
    
        }

        private void displayTitleProducer(Graphics g, int x, int y, int width, int height) {
         int xt, yt;  
         String str = new String("Producer");
         String strTmp = new String();
         g.setColor(Color.blue);
         g.setFont(prodFont);
         FontMetrics fm = g.getFontMetrics();
         int w = fm.stringWidth("P");
         xt = x + (width - w)/2;         
         yt = y + height/8 - 5;
         for ( int i = 0; i < 8; i++ ) {
              strTmp = String.valueOf(str.charAt(i));
              g.drawString(strTmp, xt, yt);                 
              yt = yt + height/8;             
         }                 
      }    

        
       /**
        	// Clear our reference to the thread, for automated garbage collection
	        thPCCanvas = null;
	}
         */

}

