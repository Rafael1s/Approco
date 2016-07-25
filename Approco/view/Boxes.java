/*
 * PCBoxes.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.view;

import java.awt.*;
import java.util.*;

import Approco.control.*;
/**
 *
 */
public class Boxes {

   private int x;
   private int y;
   private int width;
   private int height;
   private int delta;
   private int boxes;
   private int shift;
   private int[] x_cur;
   //private int x_prev;
   private Color bg;
   private Color fg;
   private Color border;

   private static Random rand = new Random(10);
   // private Image image;
   
    /** Creates new PCBox */
    public Boxes(int x0,int y0,int width0,int height0,Color bg0,Color fg0,Color border0)
    {
         x = x0; 
         y = y0; 
         width = width0; 
         height = height0; 
         delta = 7; // width/10;
         boxes = 3;
         shift = 5*delta;
         x_cur = new int[boxes];
         
         for (int i = 0; i < boxes; i++) {
           x_cur[i] = x + width - delta + i*shift;
         }
         
         
         bg = bg0;
         fg = fg0;
         border = border0;
         // image = image0;
    }

     public void drawMovedBox(Graphics g, boolean animate) {
         
         if ((rand.nextInt() % 4) == 0 )
             return;
                           
           g.setColor(bg);
           // g.setColor(new Color(0xd0, 0xd0, 0xff));
           g.fillRect(x,y, width, height); 
           g.setColor(border);
           g.drawRect(x,y,width,height);
           
           // drawColoredBox(g, fg, x_cur[0], y+1, delta, height-1);  
           //drawColoredBox(g, new Color(0xd0, 0xd0, 0xff), x_cur[0], y+1, delta, height-1); 
           drawColoredBoxBorder(
              g,  x_cur[0], y+1, delta, height-1, Params.streamedBoxCol, Color.blue);               

           
           if (animate) {
             // x_prev = x_cur;
             if (x_cur[0] > x) { //  + delta)  {
                x_cur[0] = x_cur[0] - delta; 
             }
             else {
                x_cur[0] = x + width - delta;    
             }
           }  

        }
     
        
       public void drawStreamedBoxes(Graphics g, boolean animate) {
         
         if ((rand.nextInt() % 4) == 0 )
             return;
                           
           g.setColor(bg);
           g.fillRect(x,y, width, height); 
           g.setColor(border);
           g.drawRect(x,y,width,height);
           
           for (int i = 0; i < boxes; i++) {
              if ( x_cur[i] < x + width) {
                drawColoredBoxBorder(g,  x_cur[i] + 1, y+1, delta, height-1, Color.white, Color.blue);               
              }           
              if (animate) {
                if (x_cur[i] > x) {
                  x_cur[i] = x_cur[i] - delta; 
                }
                else {
                 x_cur[i] = x + width - delta;    
                }
              }  
           }

        }
        
       /* 

       private void drawCircledBox(Graphics g,              
           int x, int y, int delta, int height,Color bg, Color border) {
         
               
               drawColoredBoxBorder(g,  x_cur, y0, delta, height, Color.white, Color.blue);
       } */
        
       private void drawColoredBox(
          Graphics g, Color color, int x0, int y0, int width0, int height0) {
              
          g.setColor(color);
          g.fillRect(x0,y0, width0, height0);       
       }
       
       public static void drawColoredBoxBorder(Graphics g,              
             int x0, int y0, int width0, int height0,
             Color bg, Color border) {
                                  
          g.setColor(bg);
          g.fillRect(x0,y0, width0, height0);       
          g.setColor(border);
          g.drawRect(x0,y0, width0, height0);       
       }
}

