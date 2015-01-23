/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: SpectrumIndicatorPanel.java,v 1.2 2003/01/10 13:01:26 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2003/01/10 13:01:26 $
 */

package physis.visualisation.panels;

import physis.visualisation.util.ColorRange;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.BorderLayout;


/**
 * This panel displays a spectrum and the corresponding minimum and maximum values.
 */
public class SpectrumIndicatorPanel extends JPanel{
    private ColorRangePanel spectrum;
    private JTextField ltxt;
    private JTextField htxt;
    private JLabel title;
    
    public SpectrumIndicatorPanel(ColorRange cr_){
       	setLayout(new BorderLayout());
	spectrum = new ColorRangePanel(cr_);
	ltxt = new JTextField(5);
	ltxt.setEditable(false);
	ltxt.setToolTipText("Minimum value");
	htxt = new JTextField(5);
	htxt.setEditable(false);
	htxt.setToolTipText("Maximum value");
	title = new JLabel();
	this.add(ltxt,BorderLayout.WEST);
	this.add(title,BorderLayout.NORTH);
	this.add(spectrum, BorderLayout.CENTER);
	this.add(htxt, BorderLayout.EAST);
    }
    
    
    public void setSpectrum(ColorRange cr_){ spectrum.setColorRange(cr_); }
    public void setLow(double low_){ltxt.setText(low_ + "");}
    public void setHigh(double high_){htxt.setText(high_ + "");}
    public void setTitle(String title_){title.setText(title_);}
}
