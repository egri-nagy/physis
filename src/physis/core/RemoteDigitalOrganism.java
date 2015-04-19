/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: RemoteDigitalOrganism.java,v 1.9 2003/03/05 11:30:18 sirna Exp $
 * $Revision: 1.9 $
 * $Date: 2003/03/05 11:30:18 $
 */
package physis.core;

import physis.core.virtualmachine.GeneticCodeTape;
import physis.core.virtualmachine.PhysisVirtualMachine;
import physis.core.genotype.Genome;
import physis.core.task.PerformedTasksRegister;
import physis.log.Log;

import java.net.Socket;
import java.util.Hashtable;
import java.io.*;

/**
 * This class represents a remote digital organism. It accesses a remote
 * DigitalOrganism object through a socket via the Digital Organism Transfer Protocol (DOTP).
 * If the socket breaks down it uses a local reference.
 * Each methods defiened in the DigitalOrganism interface simply
 * delegates the operations to a remote proxy.
 * Actually it communicates with a DigitalOrganismProxy.
 */
public class RemoteDigitalOrganism implements DigitalOrganism{
    /** The socket used for the DOTP commmunication with a remote Digital Org proxy. */
    protected Socket socket;
    protected BufferedReader in;
    protected PrintWriter out;
    
    /** The index of the remote digital organism. */
    protected int index;
    /** If the connection breaks down this local reference shoud be used. */
    protected DigitalOrganism localRef;
    
    public RemoteDigitalOrganism(int index_, Socket socket_, BufferedReader in_, PrintWriter out_, DigitalOrganism local_ref){
        socket = socket_;
        in = in_;
        out = out_;
        index = index_;
        localRef = local_ref;
    }
    
    public void vivify(DigitalOrganism neighbour_,GeneticCodeTape codetape,int inherited_length){
        try{
            //neighbour is discarded
            StringBuffer request = new StringBuffer(DigitalOrganismProxy.VIVIFY);
            request.append("\n");
            request.append(index+ "\n+\n");
            request.append(codetape.getSize()+"\n");
            for (int i = 0; i < codetape.getSize(); i++){
                request.append(codetape.read(i)+ "\n");
            }
            request.append("+\n");
            request.append(inherited_length + "\n");
            request.append(".\n");
            //sending the request
            out.write(request.toString());
            out.flush();
            //the response should be only a terminator
            in.readLine();
        } catch (IOException ioe){
            localRef.vivify(neighbour_, codetape, inherited_length);
            Log.status("Using local reference!!!");
        }
    }
    
    /**
     * Kills the organism.
     */
    public void kill(){}
    
    public void increaseAge(){}
    public int getAge(){ return localRef.getAge(); }
    
    public boolean isAlive(){ return localRef.isAlive(); }
    
    public boolean isFertile(){ return localRef.isFertile(); }
    public void setFertile(boolean fertility){}
    
    
    public PhysisVirtualMachine getVM(){ return localRef.getVM(); }
    public Genome getGenome(){return localRef.getGenome(); }
    
    public Metabolism getMetabolism(){return localRef.getMetabolism();}
    
    public PerformedTasksRegister getPerformedTasks(){return localRef.getPerformedTasks();}

    public double getBonusMultiplier(){
	return localRef.getBonusMultiplier();
    }
    
    
    public void update(){}
    public void update(int cpu_cycles){}
    
    public void recalculateFitness(){}
    
    public double getFitness(){
        double fitness = 0;
        try{
            //the request
            out.write(DigitalOrganismProxy.FITNESS + "\n" + index + "\n" + ".\n");
            out.flush();
            //reading the response
            fitness = Double.parseDouble(in.readLine());
            in.readLine();
        } catch (IOException ioe){
            fitness = localRef.getFitness();
            Log.status("Using local reference!!!");
        }
        return fitness;
        
    }
    
    public void recalculateBonus(double bonus_multiplier){}
    
    public int getEffectiveLength(){return localRef.getEffectiveLength();}
    
    public void recalculateEffectiveLength(){}
    
    public int getMerit(){
        int merit = 0;
        
        try{
            //the request
            out.write(DigitalOrganismProxy.MERIT + "\n" + index + "\n" + ".\n");
            out.flush();
            //reading the response
            merit = Integer.parseInt(in.readLine());
            //readding close
            in.readLine();
        } catch (IOException ioe){
            merit = localRef.getMerit();
            physis.log.Log.status("Using local reference!!!");
        }
        return merit;
    }
    
    public void setNeighbour(DigitalOrganism digorg){}
    public DigitalOrganism getNeighbour(){return localRef.getNeighbour();}
    
    public void setPositionInfo(Object o){}
    public Object getPositinInfo(){return localRef.getPositinInfo(); }
    
}

