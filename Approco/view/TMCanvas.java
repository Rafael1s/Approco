/*
 * TMCanvas.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.view;

import java.awt.*;
import java.util.*;

import Approco.control.*;
import Approco.model.*;


public class TMCanvas extends Canvas implements Runnable {

    private Thread thTMCanvas = null;
  
    private Image offscreen;
    private Dimension offscreensize;
    private Graphics offgraphics;

    private Vector threadNames;

    // private static int filtFlag = 0;
    String title = "Thread Monitor";
    int xOffset, xOrig;
    int yOffset, yOffsetName;
    
    private volatile boolean runTMCanvas;
    // 3 functions below: removed synchronized, runFlag changed to volatile
    public synchronized void setTMCanvasStop() { 
        runTMCanvas = false;
    }
    public synchronized void setTMCanvasRun() { runTMCanvas = true; }
    public synchronized boolean getTMCanvasFlag() { return runTMCanvas; }  
    
    public void setTMThreadAndTMCanvasNull(){
        thTMCanvas = null;
        instance = null;
    }    
    
    private static TMCanvas instance = new TMCanvas();
    public static TMCanvas getInstance() {
       
        int i = 0;
        while (instance == null) {
           Print.show(" TMCanvas = null, i = " + i); 
           instance = new TMCanvas(); 
           try {                     
                    Thread.sleep(500);  
               } catch ( java.lang.InterruptedException ie) {}
               if ( i > 5 ) {
                       Print.show(" i > 5: TMCanvas = null, i = " + i);
                       System.exit(1);
               }
        }    
         
        return instance;
    }  
    
    private TMCanvas() {         
         yOffset = 15; 
         yOffsetName = 50;
         xOffset = xOrig = 5;     
         setSize(Params.wTm + 1, Params.hTm + 1);	
       
         threadNames = new Vector(10);
         
    }

    public void update(Graphics g) {
        paint(g);
    }
    public void paint(Graphics g) {
       if (getTMCanvasFlag() &&  (instance != null)) {
         Dimension d = this.getSize();
         if ((offscreen == null) || (d.width != offscreensize.width)
	                            || (d.height != offscreensize.height)) {
	        offscreen = createImage(d.width, d.height);
	        offscreensize = d;
	        offgraphics = offscreen.getGraphics();         
	        offgraphics.setFont(Params.timesItalic12);
         }
       
         displayConsoleBG();
         displayTitle();
         displayCurrentThread();
       
         g.drawImage(offscreen, 0, 0, null);
       }  
    }    
    
    private void displayConsoleBG() {
       offgraphics.setColor(Params.consolCol); 
       offgraphics.fillRect(0, 0, Params.wTm, Params.hTm);    
       offgraphics.setColor(Color.red); 
       offgraphics.drawRect(0, 0, Params.wTm, Params.hTm);   
    }       
    
    
    private void displayTitle() {         
         int xt;
         offgraphics.setColor(Color.black);         
         FontMetrics fm = offgraphics.getFontMetrics();
         int w = fm.stringWidth(title);
         xt = (Params.wTm - w)/2;        
         offgraphics.drawString (title, xt, yOffset);                    
    }    
   
    public void run() {
          while(getTMCanvasFlag()) {
          try {  
            Thread.sleep(Params.tmPeriod);  
          } catch ( java.lang.InterruptedException ie) {}
          repaint();
        }

    }
       
    private void displayCurrentThread() {
        
        int xt, h, i, w, len;
        String currName;
        
        String name =  (String)QueueTM.getInstance().get();  
        
        if (threadNames.contains(name) == false ) {
            threadNames.addElement(name);
        }    
                
        //Iterator it = threadNames.iterator();
        
        i = 0;
        
        FontMetrics fm = offgraphics.getFontMetrics();
        // w = fm.stringWidth(name);
        // xt = (Params.wTm - w)/2;  
        xt = 10;
        h = fm.getHeight();
        offgraphics.setColor(Color.black);                    
        
        for ( len = 0; len < threadNames.size(); len++) {
           currName = (String)threadNames.elementAt(len);
           if (filterName(currName)) {
             i++; 
             if ( currName.compareTo(name) == 0 ) {
                offgraphics.setColor(Params.sliceCol); 
                offgraphics.fillRect(1, yOffsetName + h*(i-2)+4, Params.wTm-1, h);
                offgraphics.setColor(Color.blue);                    
                offgraphics.drawString (currName, xt, yOffsetName + h*(i-1));                          
             }
             else {
                offgraphics.setColor(Color.black);                                   
                offgraphics.drawString (currName, xt, yOffsetName + h*(i-1));                          
             }    
          }              
        }   
    }  
    
    private boolean filterName(String name) {               
        
        if (name.compareTo("Consumer 1") == 0 && 
           MultiQueue.getInstance().getConsumer(0).getConsumerRunFlag() == false )
            return false;
        if (name.compareTo("Consumer 2") == 0 && 
           MultiQueue.getInstance().getConsumer(1).getConsumerRunFlag() == false )
            return false;
        if (name.compareTo("Consumer 3") == 0 && 
           MultiQueue.getInstance().getConsumer(2).getConsumerRunFlag() == false )
            return false;
        if (name.compareTo("Consumer 4") == 0 && 
           MultiQueue.getInstance().getConsumer(3).getConsumerRunFlag() == false )
            return false;
        if (name.compareTo("Producer") == 0 && 
           MultiQueue.getInstance().getProducer().getProducerRunFlag() == false )
            return false;
        
        return true;
    }    
    
    public void start()
	{
		// Check to see if thread is active
		if (thTMCanvas == null)
		{
			// Pass our thread an instance of java.lang.Runnable (us)
			thTMCanvas = new Thread(this, "Monitor");                       
			thTMCanvas.start();
		}
	}

    /*        
	// Clear our reference to the thread, for automated garbage collection
	thTMCanvas = null;
    */

}
