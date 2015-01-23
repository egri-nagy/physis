/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: LifeSpaceViewer.java,v 1.3 2003/01/10 13:42:31 sirna Exp $
 * $Revision: 1.3 $
 * $Date: 2003/01/10 13:42:31 $
 */

package physis.visualisation.panels.lifespace;

import physis.visualisation.panels.SpectrumIndicatorPanel;

import java.awt.event.ActionListener;

/**
 * Interface for viewers which display lifespaces.
 */
public interface LifeSpaceViewer extends ActionListener{
    public static final String MERIT_COMM = "MERIT";
    public static final String FITNESS_COMM = "FITNESS";
    public static final String AGE_COMM = "AGE";
    public static final String MAX_COMM = "MAX";
    public static final String MAXER_COMM = "MAXER";
    public static final String FROMZERO_COMM = "FROMZERO";

    //color assignment methods
    public static final int BY_MAX = 1;
    public static final int BY_MAX_EVER_REACHED = 2;

    //viewmodes
    public static final int MERIT = 0;
    public static final int FITNESS = 1;
    public static final int AGE = 2;

    void setSpectrumIndicator(SpectrumIndicatorPanel sp);
}
