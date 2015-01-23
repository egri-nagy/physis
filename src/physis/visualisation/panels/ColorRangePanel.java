/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: ColorRangePanel.java,v 1.2 2003/01/10 12:54:39 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2003/01/10 12:54:39 $
 */

package physis.visualisation.panels;

import physis.visualisation.util.ColorRange;
import javax.swing.JPanel;
import java.awt.Graphics;

/**
 * A simple panel which shows a colorrange.
 */
public class ColorRangePanel extends JPanel{
    ColorRange cr;
    
    public ColorRangePanel(ColorRange cr_){
       	cr = cr_;
	setToolTipText("Range of colors indicating some ranking.");
    }
    
    
    public void setColorRange(ColorRange cr_){
	cr = cr_;
    }
    
    public void paint(Graphics g){
        double s =  getSize().getWidth() / (double)cr.getSize();
	for (int j = 0; j < cr.getSize(); j++){
	    g.setColor(cr.getColor(j));
	    g.fillRect((int)Math.round(j*s),0, (int)Math.round((j+1)*s),getSize().height);
	    
	}
    }    
}
