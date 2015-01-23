/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: PhysisGUI.java,v 1.8 2003/05/16 13:39:50 sirna Exp $
 * $Revision: 1.8 $
 * $Date: 2003/05/16 13:39:50 $
 */

package physis.visualisation;

import physis.core.*;
import physis.visualisation.panels.*;
import physis.visualisation.panels.lifespace.*;
import physis.visualisation.util.ColorRange;

import javax.swing.*;
import java.awt.*;


/**
 * The main window and an alternative entry point to the system.
 */
public class PhysisGUI extends JFrame{
    
    private static PHYSIS physis;
    private LifeSpaceViewer lsviewer;
    
    public PhysisGUI() {
        super();
        setLocation(0,0);
        initComponents ();
	createMenu();
        pack ();
    }
    
    
    private void initComponents () {
        setTitle (Configuration.getVersion());
        lsviewer = new Lattice2DViewer();
	SpectrumIndicatorPanel spip = new SpectrumIndicatorPanel(null);
        JScrollPane scrp = new JScrollPane((Component)lsviewer);
	lsviewer.setSpectrumIndicator(spip);
        getContentPane().add((JComponent) scrp,BorderLayout.CENTER);
	getContentPane().add((JComponent)spip, BorderLayout.SOUTH);
	
	setSize(((JComponent)lsviewer).getPreferredSize());
        
	//setting the viewer
	Viewer[] vrs = new Viewer[1];
        vrs[0] = (Viewer) lsviewer;
        physis.setViewers(vrs);
        
	//window closing
	addWindowListener (new java.awt.event.WindowAdapter () {
		public void windowClosing (java.awt.event.WindowEvent evt) {
		    System.exit(0);
		}
	    });
    }
    
    private void createMenu(){
	JMenuBar menubar = new JMenuBar();
	setJMenuBar(menubar);
	
	JMenu mode_menu = new JMenu("Mode");
	ButtonGroup mode_grp = new ButtonGroup();
	menubar.add(mode_menu);
	JMenuItem fitness_mi = new JRadioButtonMenuItem("Fitness",true);
	fitness_mi.setActionCommand(LifeSpaceViewer.FITNESS_COMM);
	fitness_mi.addActionListener(lsviewer);
	mode_menu.add(fitness_mi);
	mode_grp.add(fitness_mi);
	JMenuItem merit_mi = new JRadioButtonMenuItem("Merit");
	merit_mi.setActionCommand(LifeSpaceViewer.MERIT_COMM);
	merit_mi.addActionListener(lsviewer);
	mode_menu.add(merit_mi);
	mode_grp.add(merit_mi);
	JMenuItem age_mi = new JRadioButtonMenuItem("Age");
	age_mi.setActionCommand(LifeSpaceViewer.AGE_COMM);
	age_mi.addActionListener(lsviewer);
	mode_menu.add(age_mi);
	mode_grp.add(age_mi);
	
	JMenu boundary_menu = new JMenu("Boundary");
	menubar.add(boundary_menu);
	ButtonGroup boundary_grp = new ButtonGroup();
        JRadioButtonMenuItem max_mi = new JRadioButtonMenuItem("Maximum");
	max_mi.setActionCommand(LifeSpaceViewer.MAX_COMM);
	max_mi.addActionListener(lsviewer);
	boundary_grp.add(max_mi);
	boundary_menu.add(max_mi);
        JRadioButtonMenuItem maxer_mi = new JRadioButtonMenuItem("Maximum ever reached",true);
	maxer_mi.setActionCommand(LifeSpaceViewer.MAXER_COMM);
	maxer_mi.addActionListener(lsviewer);
	boundary_grp.add(maxer_mi);
	boundary_menu.add(maxer_mi);
	JSeparator sep = new JSeparator();
	boundary_menu.add(sep);
        JRadioButtonMenuItem fromzero_mi = new JRadioButtonMenuItem("From Zero",true);
	fromzero_mi.setActionCommand(LifeSpaceViewer.FROMZERO_COMM);
	fromzero_mi.addActionListener(lsviewer);
	boundary_menu.add(fromzero_mi);
	
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main (String args[]) {
        PHYSIS.setConfigFileName(args[0]);
        physis = PHYSIS.getInstance();
        
        
        
        new PhysisGUI().show();
        physis.start();
    }
        
}
