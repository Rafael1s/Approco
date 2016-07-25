/*
 * PCFrame.java
 *
 * Created in Mart 2002
 * 
 * Rafael Stekolshchik
 */

package Approco.view;

import Approco.control.*;
import java.awt.*;

public class ApprocoFrame extends Frame {

    /** Creates new ExFrame */
    public ApprocoFrame() {
        super("Frame Example");
        setLayout(new FlowLayout());

        // false - means frame, true for applet
        ApprocoPanel.getInstance().initPCPanel(false);            
        add(ApprocoPanel.getInstance());
        // add(ep);


        Print.show("Frame init ...");
        setSize(650,470);
        
        show();
    }
}

