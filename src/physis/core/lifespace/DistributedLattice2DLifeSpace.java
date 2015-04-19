/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DistributedLattice2DLifeSpace.java,v 1.14 2001/07/06 08:44:26 sirna Exp $
 * $Revision: 1.14 $
 * $Date: 2001/07/06 08:44:26 $
 */
package physis.core.lifespace;

import physis.core.virtualmachine.*;
import physis.core.*;
import physis.core.iterator.*;
import physis.core.random.Randomness;

import physis.log.Log;

import java.net.Socket;
import java.util.*;
import java.io.*;

/**
 * dependency: Array2DDigitalOrganismIterator, ArrayDigitalOrganismIterator
 */

public class DistributedLattice2DLifeSpace implements CellLifeSpace, Remote2DLattice{
    private static final String X_SIZE="2Dlattice_xsize";
    private static final String Y_SIZE="2Dlattice_ysize";
    //remote adresses
    private static final String EAST = "DistributedLattice2DLifeSpace_east";
    private static final String WEST = "DistributedLattice2DLifeSpace_west";
    private static final String NORTH = "DistributedLattice2DLifeSpace_north";
    private static final String SOUTH = "DistributedLattice2DLifeSpace_south";
    //local portnumbers
    private static final String LOCALEAST ="DistributedLattice2DLifeSpace_local_east";
    private static final String LOCALWEST = "DistributedLattice2DLifeSpace_local_west";
    private static final String LOCALNORTH = "DistributedLattice2DLifeSpace_local_north";
    private static final String LOCALSOUTH = "DistributedLattice2DLifeSpace_local_south";
    
    private DigitalOrganism[] east, south, west, north;
    private DigitalOrganismProxy east_server, south_server, west_server, north_server;
    private int x_size;
    private int y_size;
    private DigitalOrganism[][] lifespace;
    private DigitalOrganism[] neighbours = new DigitalOrganism[8];
    private ArrayDigitalOrganismIterator neighbour_iterator = new ArrayDigitalOrganismIterator(neighbours);
    private Randomness rnd = PHYSIS.environment.getRandomness();
    
    /**
     *  Creates a lifespace. The dimensional parameters are got from Configuration.
     */
    public DistributedLattice2DLifeSpace(){
        this(Integer.parseInt(Configuration.getProperty(X_SIZE)),
             Integer.parseInt(Configuration.getProperty(Y_SIZE)));
    }
    
    
    /**
     * Creates a lifespace with different with dimension x * y.
     * Setting up the local servers and conects to the remote servers.
     */
    public DistributedLattice2DLifeSpace(int x_size_, int y_size_) {
        try{
            x_size = x_size_;
            y_size = y_size_;
            
            //filling up the lifespace with organisms
            lifespace = new DigitalOrganism[x_size][];
            for (int i = 0; i < lifespace.length; i++){
                lifespace[i] = new DigitalOrganism[y_size];
                for (int j = 0; j < lifespace[i].length; j++){
                    lifespace[i][j] = new DigitalOrganismImpl();
                    lifespace[i][j].setPositionInfo(new PositionInfo2DLattice(i,j));
                }
            }
            
            //setting up servers
            if (Configuration.doesExistProperty(LOCALNORTH)){
                north_server = new DigitalOrganismProxy(Integer.parseInt(Configuration.getProperty(LOCALNORTH)),getNorth());
            }
            if (Configuration.doesExistProperty(LOCALSOUTH)){
                south_server = new DigitalOrganismProxy(Integer.parseInt(Configuration.getProperty(LOCALSOUTH)),getSouth());
            }
            if (Configuration.doesExistProperty(LOCALEAST)){
                east_server = new DigitalOrganismProxy(Integer.parseInt(Configuration.getProperty(LOCALEAST)),getEast());
            }
            if (Configuration.doesExistProperty(LOCALWEST)){
                west_server = new DigitalOrganismProxy(Integer.parseInt(Configuration.getProperty(LOCALWEST)),getWest());
            }
            Log.status("Servers are ok");
            
            //CLIENTS
            Socket  socket = null;
            BufferedReader in;
            PrintWriter out;
            String host_port;
            //north
            if (Configuration.doesExistProperty(NORTH)){
                host_port = Configuration.getProperty(NORTH);
                socket = createSocket(host_port);
                in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), false);
                north = new RemoteDigitalOrganism[x_size];
                for (int i = 0 ; i < x_size; i++){
                    north[i] = new RemoteDigitalOrganism(i, socket, in, out, lifespace[i][0]);
                }
                Log.status("North is bound to remote: " + socket.getInetAddress() + ":" + socket.getPort());
            }
            else{
                north = getSouth();
                Log.status("North is bound to south locally.");
            }
            
            //east
            if (Configuration.doesExistProperty(EAST)){
                host_port = Configuration.getProperty(EAST);
                socket = createSocket(host_port);
                in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), false);
                east = new RemoteDigitalOrganism[y_size];
                for (int i = 0 ; i < y_size; i++){
                    east[i] = new RemoteDigitalOrganism(i, socket, in, out, lifespace[x_size - 1][i]);
                }
                Log.status("East is bound to remote: " + socket.getInetAddress() + ":" + socket.getPort());
            }
            else{
                east = getWest();
                Log.status("East is bound to west locally.");
            }
            
            //south
            if (Configuration.doesExistProperty(SOUTH)){
                host_port = Configuration.getProperty(SOUTH);
                socket = createSocket(host_port);
                in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), false);
                south = new RemoteDigitalOrganism[x_size];
                for (int i = 0 ; i < x_size; i++){
                    south[i] = new RemoteDigitalOrganism(i, socket, in, out, lifespace[i][y_size-1]);
                }
                Log.status("South is bound to remote: " + socket.getInetAddress() + ":" + socket.getPort());
            }
            else{
                south = getNorth();
                Log.status("South is bound to north locally.");
            }
            
            
            //west
            if (Configuration.doesExistProperty(WEST)){
                host_port = Configuration.getProperty(WEST);
                socket = createSocket(host_port);
                in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), false);
                west = new RemoteDigitalOrganism[y_size];
                for (int i = 0 ; i < y_size; i++){
                    west[i] = new RemoteDigitalOrganism(i, socket, in, out, lifespace[0][i]);
                }
                Log.status("South is bound to remote: " + socket.getInetAddress() + ":" + socket.getPort());
            }
            else{
                west = getEast();
                Log.status("West is bound to east locally.");
            }
            
        } catch(Exception e){
            Log.error(e);
        }
    }
    
    /**
     * Injects the genome into the soup. The target cell is random.
     */
    public void injectGenome(GeneticCodeTape ct){
        try{
            int x_position = rnd.nextInt(lifespace.length);
            int y_position = rnd.nextInt(lifespace[0].length);
            
            lifespace[x_position][y_position].vivify((getNeighbours(lifespace[x_position][y_position])).randomly(),ct, Configuration.getAverageTimeSlice());
        } catch(Exception e){
            Log.error(e);
        }
    }
    
    /**
     * This method provides all the organisms of the lifespace. This feature is needed  when making the update.
     */
    public DigitalOrganismIterator getAllOrganisms(){
        return new Array2DDigitalOrganismIterator(lifespace);
    }
    /**
     * This method gives the neighbours of an organism. This is needed when an organism interact with its neighbourhood. Here the
     * topology is 8-neighbourhood on a toroidal two dimensional lattice.
     */
    public DigitalOrganismIterator getNeighbours(DigitalOrganism digorg){
        try{
            PositionInfo2DLattice pos = (PositionInfo2DLattice)digorg.getPositinInfo();
            
            if (pos.x == 0){
                if (pos.y == 0){
                    neighbours[0] = lifespace[x_size - 1][y_size - 1];
                    neighbours[1] = north[0];
                    neighbours[2] = north[1];
                    neighbours[3] = lifespace[pos.x + 1][pos.y];
                    neighbours[4] = lifespace[pos.x+1] [pos.y+1];
                    neighbours[5] = lifespace[pos.x] [pos.y+1];
                    neighbours[6] = west[1];
                    neighbours[7] = west[0];
                    
                }
                else if (pos.y == (y_size -1)){
                    neighbours[0] = west[west.length -2];
                    neighbours[1] = lifespace[pos.x] [pos.y-1];
                    neighbours[2] = lifespace[pos.x+1] [pos.y-1];
                    neighbours[3] = lifespace[pos.x+1] [pos.y];
                    neighbours[4] = south[1];
                    neighbours[5] = south[0];
                    neighbours[6] = lifespace[x_size - 1][0];
                    neighbours[7] = west[west.length - 1];
                }
                else{
                    neighbours[0] = west[pos.y -1];
                    neighbours[1] = lifespace[pos.x] [pos.y-1];
                    neighbours[2] = lifespace[pos.x+1] [pos.y-1];
                    neighbours[3] = lifespace[pos.x+1] [pos.y];
                    neighbours[4] = lifespace[pos.x+1] [pos.y+1];
                    neighbours[5] = lifespace[pos.x] [pos.y+1];
                    neighbours[6] = west[pos.y + 1];
                    neighbours[7] = west[pos.y];
                }
                
            }
            else if(pos.y == 0){
                if ( pos.x == (x_size - 1)){
                    neighbours[0] = north[north.length -2];
                    neighbours[1] = north[north.length - 1];
                    neighbours[2] = lifespace[0][y_size - 1];
                    neighbours[3] = east[0];
                    neighbours[4] = east[1];
                    neighbours[5] = lifespace[pos.x] [pos.y+1];
                    neighbours[6] = lifespace[pos.x-1] [pos.y+1];
                    neighbours[7] = lifespace[pos.x-1] [pos.y];
                }
                else{
                    neighbours[0] = north[pos.x - 1];
                    neighbours[1] = north[pos.x];
                    neighbours[2] = north[pos.y + 1];
                    neighbours[3] = lifespace[pos.x+1] [pos.y];
                    neighbours[4] = lifespace[pos.x+1] [pos.y+1];
                    neighbours[5] = lifespace[pos.x] [pos.y+1];
                    neighbours[6] = lifespace[pos.x-1] [pos.y+1];
                    neighbours[7] = lifespace[pos.x-1] [pos.y];
                }
            }
            else if (pos.x == (x_size - 1)) {
                if (pos.y == (y_size -  1)){
                    neighbours[0] = lifespace[pos.x-1] [pos.y-1];
                    neighbours[1] = lifespace[pos.x] [pos.y-1];
                    neighbours[2] = east[east.length-2];
                    neighbours[3] = east[east.length-1];
                    neighbours[4] = lifespace[0][0];
                    neighbours[5] = south[south.length-1];
                    neighbours[6] = south[south.length-2];
                    neighbours[7] = lifespace[pos.x-1] [pos.y];
                }
                else{
                    neighbours[0] = lifespace[pos.x-1] [pos.y-1];
                    neighbours[1] = lifespace[pos.x] [pos.y-1];
                    neighbours[2] = east[pos.y-1];
                    neighbours[3] = east[pos.y];
                    neighbours[4] = east[pos.y+1];
                    neighbours[5] = lifespace[pos.x] [pos.y+1];
                    neighbours[6] = lifespace[pos.x-1] [pos.y+1];
                    neighbours[7] = lifespace[pos.x-1] [pos.y];
                }
            }
            else if(pos.y == (y_size - 1)){
                neighbours[0] = lifespace[pos.x-1] [pos.y-1];
                neighbours[1] = lifespace[pos.x] [pos.y-1];
                neighbours[2] = lifespace[pos.x+1] [pos.y-1];
                neighbours[3] = lifespace[pos.x+1] [pos.y];
                neighbours[4] = south[pos.x -1];
                neighbours[5] = south[pos.x];
                neighbours[6] = south[pos.x+1];
                neighbours[7] = lifespace[pos.x-1] [pos.y];
                
            }
                //and this is the normal
            else{
                neighbours[0] = lifespace[pos.x-1] [pos.y-1];
                neighbours[1] = lifespace[pos.x] [pos.y-1];
                neighbours[2] = lifespace[pos.x+1] [pos.y-1];
                neighbours[3] = lifespace[pos.x+1] [pos.y];
                neighbours[4] = lifespace[pos.x+1] [pos.y+1];
                neighbours[5] = lifespace[pos.x] [pos.y+1];
                neighbours[6] = lifespace[pos.x-1] [pos.y+1];
                neighbours[7] = lifespace[pos.x-1] [pos.y];
            }
            /* instead of creating all the time the iterator
             * we can keep only one instance and change its inner fields
             * it's really weird  from the view of OO design
             * but it's really it's a significant performance gain - that's life
             * It may not cause concurrency problems, as if we run it as one thread.
             */
            neighbour_iterator.reset();
        } catch(Exception e){
            Log.error(e);
        }
        return neighbour_iterator;
    }
    /**
     * Gives an organism randomly from the entire 'soup'. It's needed for point mutations for example.
     */
    public DigitalOrganism getOrganismRandomly(){
        return lifespace[rnd.nextInt(x_size)][rnd.nextInt(y_size)];
    }
    /**
     * Gives the capacity of the lifespace.
     */
    
    public int getSize(){
        return x_size * y_size;
    }
    
    public DigitalOrganismIterator getRegion(double percent){
        return null;
    }
    
    public DigitalOrganismIterator getSomeOrganisms(double precent){
	return null;
    }
    
    
    public DigitalOrganism[] getWest(){
        return lifespace[0];
    }
    
    public DigitalOrganism[] getEast(){
        return lifespace[x_size - 1];
    }
    
    public DigitalOrganism[] getNorth(){
        DigitalOrganism[] orgs = new DigitalOrganism[x_size];
        for (int i = 0; i < x_size; i++){
            orgs[i] = lifespace[i][0];
        }
        return orgs;
    }
    public DigitalOrganism[] getSouth(){
        DigitalOrganism[] orgs = new DigitalOrganism[x_size];
        for (int i = 0; i < x_size; i++){
            orgs[i] = lifespace[i][y_size - 1];
        }
        return orgs;
    }
    
    
    protected Socket createSocket(String host_port){
        Socket socket = null;
        boolean is_ok = false;
        while (!is_ok){
            try{
                socket = new Socket(host_port.substring(0,host_port.indexOf(":")), Integer.parseInt(host_port.substring(host_port.indexOf(":") + 1)));
                is_ok = true;
            } catch (Exception e){
                Log.status("Waiting for: " + host_port);
                try {Thread.currentThread().sleep(10000);} catch (InterruptedException ire) {}
            }
        }
        Log.status("Endpoint created: " + socket.getInetAddress() + ":" + socket.getPort());
        return socket;
    }
    
    
}

