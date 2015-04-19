/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Lattice2DViewer.java,v 1.8 2003/01/10 13:40:30 sirna Exp $
 * $Revision: 1.8 $
 * $Date: 2003/01/10 13:40:30 $
 */

package physis.visualisation.panels.lifespace;

import physis.core.iterator.DigitalOrganismIterator;
import physis.core.PHYSIS;
import physis.core.Population;
import physis.core.Configuration;
import physis.core.DigitalOrganism;
import physis.visualisation.Viewer;
import physis.visualisation.util.ColorRange;
import physis.visualisation.panels.SpectrumIndicatorPanel;
import physis.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *  A panel which displays the whole lattice. @see physis.core.lifespace.Lattice2DLifeSpace
 */
public class Lattice2DViewer extends JPanel implements Viewer, LifeSpaceViewer{
    //default view_mode
    public static final int DEFAULT_VIEW_MODE = FITNESS;
    int view_mode = DEFAULT_VIEW_MODE;
    
    //defaulet color assignment mode
    public static final int DEFAULT_COLOR_ASSIGNMENT_MODE = BY_MAX_EVER_REACHED;
    int color_assignmnet_mode = DEFAULT_COLOR_ASSIGNMENT_MODE;
    
    //cell size stuff
    int cell_size = DEFAULT_CELL_SIZE;
    private static final int DEFAULT_CELL_SIZE = 5;
    
    //colorrange_size
    public static final int DEFAULT_COLOR_RANGE_SIZE = 128;
    int colorrange_size = DEFAULT_COLOR_RANGE_SIZE;
    
    private  boolean fromzero_flag = true;
    private DigitalOrganismIterator digorgit;
    private Color[][] colors;
    private ColorRange colorrange;
    //reference to the population
    private Population population;
    private SpectrumIndicatorPanel spindpanel;
    
    /** Properties as in the Lattice2DLifeSpace */
    private static final String X_SIZE="2Dlattice_xsize";
    private static final String Y_SIZE="2Dlattice_ysize";
    
    /**
     * The constructor gets the population from the Physis instance and lifespace parameters
     * from the Configuration.
     */
    public Lattice2DViewer(){
        super();
        population = PHYSIS.getInstance().getPopulation();
        digorgit = population.getAllOrganism();
        int xsize = Integer.parseInt(Configuration.getProperty(X_SIZE));
        int ysize = Integer.parseInt(Configuration.getProperty(Y_SIZE));
        colors = new Color[xsize][ysize];
        colorrange = new ColorRange(colorrange_size);
        setPreferredSize(new java.awt.Dimension(xsize * cell_size,ysize * cell_size));
    }
    
    public void setSpectrumIndicator(SpectrumIndicatorPanel sp){
        spindpanel = sp;
	spindpanel.setSpectrum(colorrange);
    }
    
    /**
     * Refreshes the viewer's internal data about the population. The meaning of the information
     * is determined by the @see view_mode variable (fitness, merit, age).
     */
    public void refresh(){
        switch(view_mode){
	    case MERIT : refreshMerit(); break;
	    case FITNESS : refreshFitness(); break;
	    case AGE : refreshAge(); break;
	    default : Log.error("No valid view mode in " + this.getClass().getName() + "!"); break;
        }
        repaint();
    }
    
    private void refreshFitness(){
	double min_fitness = 0;
	if (!fromzero_flag) {min_fitness = population.getMinFitness();}
	double max_fitness = 0;
	if (color_assignmnet_mode == BY_MAX){
	    max_fitness = population.getMaxFitness();
	}
	else if (color_assignmnet_mode == BY_MAX_EVER_REACHED){
	    max_fitness = population.getMaxFitnessEverReached();
	}
	else{
	    Log.error("No valid color assignment mode in " + this.getClass().getName() + "!");
	}
	refreshSpectrumIndicator("FITNESS",min_fitness, max_fitness);
	double step  = (max_fitness - min_fitness) / (colorrange.getSize() - 1);
	
	digorgit.reset();
	DigitalOrganism dorg = null;
	for (int i = 0; i < colors.length; i ++){
	    for (int j = 0; j < colors[0].length; j++){
		dorg = digorgit.next();
		if (dorg.isFertile()){
		    colors[i][j] = colorrange.getColor((int) Math.round((dorg.getFitness() - min_fitness) / step));
		}
		else if (dorg.isAlive()){
		    colors[i][j] = colorrange.getNewBornColor();
		}
		else{
		    colors[i][j] = colorrange.getDeadColor();
		}
	    }
	}
        
    }
    
    private void refreshMerit(){
	int min_merit = 0;
	if (!fromzero_flag){min_merit = population.getMinMerit();}
	int max_merit = 0;
	if (color_assignmnet_mode == BY_MAX){
	    max_merit = population.getMaxMerit();
	}
	else if (color_assignmnet_mode == BY_MAX_EVER_REACHED){
	    max_merit = population.getMaxMeritEverReached();
	}
	else{
	    Log.error("No valid color assignment mode in " + this.getClass().getName() + "!");
	}
	refreshSpectrumIndicator("MERIT",min_merit, max_merit);
	double step  = (double) (max_merit - min_merit) / (colorrange.getSize() - 1);
	digorgit.reset();
	DigitalOrganism dorg = null;
	for (int i = 0; i < colors.length; i ++){
	    for (int j = 0; j < colors[0].length; j++){
		dorg = digorgit.next();
		if (dorg.isFertile()){
		    colors[i][j] = colorrange.getColor((int) Math.floor( (dorg.getMerit() - min_merit) / step));
		}
		else if (dorg.isAlive()){
		    colors[i][j] = colorrange.getNewBornColor();
		}
		else{
		    colors[i][j] = colorrange.getDeadColor();
		}
	    }
	}
    }
    
    private void refreshAge(){
	
	int max_age = 0;
	
	if (color_assignmnet_mode == BY_MAX){
	    max_age = population.getMaxAge();
	}
	else if (color_assignmnet_mode == BY_MAX_EVER_REACHED){
	    max_age = population.getMaxAgeEverReached();
	}
	else{
	    Log.error("No valid color assignment mode in " + this.getClass().getName() + "!");
	}
	refreshSpectrumIndicator("AGE",0, max_age);
	
	double step  = (max_age / (double)(colorrange.getSize() - 1));
	digorgit.reset();
	DigitalOrganism dorg = null;
	for (int i = 0; i < colors.length; i ++){
	    for (int j = 0; j < colors[0].length; j++){
		dorg = digorgit.next();
		if (dorg.isFertile()){
		    colors[i][j] = colorrange.getColor((int) Math.round( dorg.getAge() / step));
		}
		else if (dorg.isAlive()){
		    colors[i][j] = colorrange.getNewBornColor();
		}
		else{
		    colors[i][j] = colorrange.getDeadColor();
		}
	    }
	}
    }
    
    private void refreshSpectrumIndicator(String title,double low, double high){
	spindpanel.setTitle(title);
        spindpanel.setLow(low);
	spindpanel.setHigh(high);
	//this repaint call caused funny affects (spindpanel appeared in lifespcae area)
	//spindpanel.repaint(); 
    }
    
    public void paint(Graphics g){
        for (int i = 0; i < colors.length; i++){
	    for (int j = 0; j < colors[i].length; j ++){
		g.setColor(colors[i][j]);
		g.fillRect(i * cell_size, j * cell_size, cell_size, cell_size);
	    }
        }
    }
    
    public void setViewMode(int mode){ view_mode = mode; }
    public int getViewMode() { return view_mode; }
    public void setColorAssignmentMode(int mode) { color_assignmnet_mode = mode ; }
    public int getColorAssignmentMode() { return color_assignmnet_mode; }
    public void setCellSize(int new_size) { cell_size = new_size; }
    public int getCellSize() { return cell_size; }
    public void setColorRangeSize(int new_size){ colorrange = new ColorRange(new_size); }
    public int getColorRangeSize() { return colorrange.getSize(); }
    public void flipFromZeroFlag(){fromzero_flag = !fromzero_flag;}
    
    public void actionPerformed(ActionEvent ae){
	String action = ae.getActionCommand();
	if (MERIT_COMM.equals(action)){
	    setViewMode(MERIT);
	}
	else if(FITNESS_COMM.equals(action)){
	    setViewMode(FITNESS);
	}
	else if(AGE_COMM.equals(action)){
	    setViewMode(AGE);
	}
	else if(MAX_COMM.equals(action)){
	    setColorAssignmentMode(BY_MAX);
	}
	else if(MAXER_COMM.equals(action)){
	    setColorAssignmentMode(BY_MAX_EVER_REACHED);
	}
	else if(FROMZERO_COMM.equals(action)){
	    flipFromZeroFlag();
	}
    }
    
}







