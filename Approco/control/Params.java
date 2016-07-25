/*
 * Params.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.control;

import java.awt.*;

public class Params {

    /** Creates new Params */
    public Params() {
    }
    
        // NumbOfConsumers
        public final static int numbOfConsumers = 4;
        
        // Thread Monitor Consile
        public final static int xTm = 0;
        public final static int yTm = 0;
        public final static int wTm = 100;
        public final static int hTm = 340;//254; 
        
        // Producer Consumer Console
        public final static int xPc = 0;
        public final static int yPc = 0;
        public final static int wPc = 400; 
        public final static int hPc = 340;// 254;
        
        // Colors
        public final static Color consolCol = new Color(0x77, 0xff, 0xcc);
        public final static Color consumerCol1 = new Color(0x75, 0xfd, 0xee);
        public final static Color consumerCol2 = new Color(0x55, 0xdd, 0xcc);
        //public final static Color consumerCol3 = new Color(0x35, 0xbd, 0xee);        
        public final static Color consumerCol3 = new Color(0x70, 0xdb, 0xda);        
        public final static Color consumerCol4 = new Color(0x75, 0xbc, 0xda);        
        // public final static Color producerCol = new Color(0x70, 0xdb, 0xda);        
        public final static Color producerCol = new Color(0xc0, 0xe0, 0xff);        
        public final static Color sliceCol = new Color(0xd0, 0xff, 0xd0);
        // streamedBoxCol = producerCol
        public final static Color streamedBoxCol =  new Color(0xc0, 0xe0, 0xff);
        
        // Fonts
        public final static Font timesItalic12 = new Font("Times",Font.ITALIC,12);
        public final static Font timesItalic10 = new Font("Times",Font.ITALIC,10);
        public final static Font timesBold10 = new Font("Times",Font.BOLD,10);
        public final static Font timesPlain10 = new Font("Times",Font.PLAIN,10);
        // public final static Font helveticaBold10 = new Font("Helvetica",Font.BOLD,10);
        public final static Font helveticaBold10 = new Font("Helvetica",Font.ITALIC,10);
        public final static Font helveticaPlain10 = new Font("Helvetica",Font.PLAIN,10);
        public final static Font arialBold10 = new Font("Arial",Font.BOLD,10);
        public final static Font arialPlain10 = new Font("Arial",Font.PLAIN,10);
        
        
        // Delays
        public final static int tmPeriod = 400;
        public final static int pcPeriod = 200;
        
        // Model Params
        public final static int capacity = 100;
        public final static int getTimeout = 7000; // Must be > 0 to avoid wait(0);
        public final static int minBatch = 20;
        // public final static int maxBatch = 20;
        public final static int putTimeout = 300;
}
