/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Viewer.java,v 1.4 2003/01/10 12:43:42 sirna Exp $
 * $Revision: 1.4 $
 * $Date: 2003/01/10 12:43:42 $
 */

package physis.visualisation;

/**
 * All viewer system should have a class that implements this interface.
 */
public interface Viewer {
    /**
     * This method refreshes the viewer's state according to the changes in the population.
     * It's called by the main thread of  Physis execution after each update cycle.
     */
    void refresh();
}
