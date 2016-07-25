/*
 * PCChain4Boxes.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.view;

import java.awt.*;
import Approco.model.*;
import Approco.control.*;
/*
 *
 *
 */
public class Chain4Boxes{

    private Boxes leftQBox;
    private Boxes rightQBox;
    
    private int wConsCommon;
    private int margin = 20;
    private int xConsol;
    private int xCons, xLeftQ, xRightQ, xQ;
    private int yCons, yLeftQ, yRightQ, yQ;
    private int wCons, wLeftQ, wRightQ, wQ;
    private int hCons, hLeftQ, hRightQ, hQ;
    private int currConsNumb;
    
    private Color bg;
    private Color fg;
    private Color border;

    private String consName;
    private Font smallFont = Params.timesItalic10; 
    private String strTotal;
    private long total;
    
    /** Creates new Chain4Boxes */
    public Chain4Boxes(Graphics g,int numb0,int xConsol0,int yCons0,int wConsCommon0,int hCons0,Color bg0,Color fg0,Color border0) {
    
       wConsCommon = wConsCommon0; 
       xConsol = xConsol0;
       currConsNumb = numb0;
       consName = new String("Consumer " + Integer.toString(currConsNumb));
       // wConsCommon = wCons + 2*(Margin = 20) + (wProd = 20, out of this class) + 
       //        + (wLeftQ = wCons + 40) + (wRightQ = wCons + 40) + (wQ = wCons + 60) + 
       //        = 4*wCons + 200
       // wCons = (wConsCommon - 200)/4 = wConsCommon/4 - 50
       bg = bg0;
       fg = fg0;
       border = border0;

       xCons = xConsol + margin;
       yCons = yCons0;
       wCons = wConsCommon/4 - 40; // wConsCommon = 400, wCons = 60;
       hCons = hCons0;
       
       xLeftQ = xCons + wCons;
       yLeftQ = yCons + hCons/4;
       wLeftQ = wCons + 30; // 90, was80;
       hLeftQ = hCons/2;
       
       xQ = xLeftQ + wLeftQ;
       yQ = yCons;
       hQ = hCons;
       wQ = wCons + 40; // 100, was 100;
       
       xRightQ = xQ + wQ;
       yRightQ = yLeftQ;
       wRightQ = wLeftQ;
       hRightQ = hLeftQ;

       //ConsimerBox 
       Boxes.drawColoredBoxBorder(g, xCons , yCons, wCons, hCons, bg, border);
        displayConsumerName(g, consName, xCons, yCons, wCons, hCons);
       //LeftFromQueueBox
       leftQBox = new Boxes(xLeftQ, yRightQ, wRightQ, hRightQ, bg, fg, border); 
       //QueueBox     
       Boxes.drawColoredBoxBorder(g, xQ, yQ, wQ, hQ, bg, border);
       //RightFromQueueBox
       rightQBox = new Boxes(xRightQ, yRightQ, wRightQ, hRightQ, bg, fg, border);         

    }    
    
    public int getRightSideOfChain() {
        return xRightQ + wRightQ;
    }    
    
    private void displayTotalUsedUp(
       Graphics g , int x, int y, int width, int height) {
         
         int xt, yt; 
         
         Consumer cons = MultiQueue.getInstance().getConsumer(currConsNumb-1);
         if ( cons == null)
             return;
         
         /*
         if (cons.getConsumerRunFlag() == false)             
             return;
         */
         
         total = MultiQueue.getInstance().getConsumer(currConsNumb-1).getTotalUsedUp();  
        
         strTotal = new String(String.valueOf(total));
         g.setColor(Color.black);
         g.setFont(smallFont);
         FontMetrics fm = g.getFontMetrics();
         int w = fm.stringWidth(strTotal);
         xt = x + (width - w)/2;         
         yt = y + height/2;
         g.drawString(strTotal, xt, yt);  
    }    
    
    public void animateBoxes(Graphics g) {
        //ConsimerBox 
        Boxes.drawColoredBoxBorder(g, xCons , yCons, wCons, hCons, bg, border);
        displayConsumerName(g, consName, xCons, yCons, wCons, hCons);
        displayTotalUsedUp(g, xCons, yCons, wCons, hCons);
        
        Consumer cons = MultiQueue.getInstance().getConsumer(currConsNumb-1);     
        Producer prod = MultiQueue.getInstance().getProducer();
        QueueVector queue = MultiQueue.getInstance().getQueue(currConsNumb-1);
        
        if ( (cons != null) && cons.getConsumerRunFlag() && queue.getSize() > 0) {   
           leftQBox.drawStreamedBoxes(g, true);
        } 
        else {
           leftQBox.drawStreamedBoxes(g, false);
        }           
        
        if ( (prod != null) && prod.getProducerRunFlag()) {   
           //rightQBox.drawStreamedBoxes(g, true);
           rightQBox.drawMovedBox(g, true);
        } 
        else {
           //rightQBox.drawStreamedBoxes(g, false);
           rightQBox.drawMovedBox(g, false);
        } 
        
        // rightQBox.drawMovedBox(g, true);
        //QueueBox
        // PCBoxes.drawColoredBoxBorder(g, xQ, yQ, wQ, hQ, bg, border);
        displayQueueBox(g, xQ, yQ, wQ, hQ, bg, border);
        if ( (cons != null) && cons.getConsumerRunFlag() && queue.getSize() > 0) { 
              displayQueueSize(g, xQ, yQ + hQ + 10, queue.getSize());
        }        
        if ( (cons != null) && queue.getSize() == 0 ) {
              displayQueueSize(g, xQ, yQ + hQ + 10, 0 );
        }    
    }
    
    private void displayQueueSize(Graphics g, int x, int y, int queueSize) {

        int xt, yt;  
        
        g.setColor(Color.black);
        g.setFont(smallFont);
        FontMetrics fm = g.getFontMetrics();
               
        String queueInfo = 
           new String("Queue " + String.valueOf(currConsNumb) + 
                      ":    " + String.valueOf(queueSize) );         
        
        int w = fm.stringWidth(queueInfo);
        int h = fm.getHeight();
        
        xt = x + (wQ - w)/2;
        yt = y;

        g.setColor(Params.consolCol);
        g.fillRect(xt-10, yt-8, wQ + 20, h + 4);
        g.setColor(Color.black);
        g.drawString(queueInfo, xt, yt); 
    }    
    
    private void displayConsumerName(
       Graphics g , String title, int x, int y, int width, int height) {
           
         int xt, yt;  
                  
         g.setColor(Color.black);
         g.setFont(smallFont);
         FontMetrics fm = g.getFontMetrics();
         int w = fm.stringWidth(title);
         xt = x + (width - w)/2;         
         yt = y + height/5;
         g.drawString(title, xt, yt);                 
       }    

    private void displayQueueBox(
      Graphics g, int xQ, int yQ, int wQ, int hQ, Color bg, Color border) {
          
        Boxes.drawColoredBoxBorder(g, xQ, yQ, wQ, hQ, bg, border);
        
        QueueVector queue = MultiQueue.getInstance().getQueue(currConsNumb-1); 
        if (queue == null)
            return;
        
        int lenQ = queue.getSize();
        
        for ( int i = 0; i < lenQ; i++ ) {
           if ( 4*i + 4 < wQ)               
              Boxes.drawColoredBoxBorder(g, xQ + 4*i, yQ, 4, hQ, Color.white, Color.blue); 
        }    
           
    }       
           
}   

