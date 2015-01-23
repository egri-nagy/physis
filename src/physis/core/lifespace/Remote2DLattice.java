/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Remote2DLattice.java,v 1.2 2000/07/28 19:00:18 sirna Exp $
 * $Revision: 1.2 $
 * $Date: 2000/07/28 19:00:18 $
 */
package physis.core.lifespace;

import physis.core.DigitalOrganism;

import java.rmi.*;

/**
 *  Remote interface for distributed two-dimensional lattice.
 */
public interface Remote2DLattice extends Remote{
    DigitalOrganism[] getNorth() throws RemoteException;
    DigitalOrganism[] getEast() throws RemoteException;
    DigitalOrganism[] getSouth() throws RemoteException;
    DigitalOrganism[] getWest() throws RemoteException;
}
