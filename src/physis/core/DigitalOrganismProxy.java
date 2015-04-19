/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: DigitalOrganismProxy.java,v 1.12 2000/10/30 17:09:00 sirna Exp $
 * $Revision: 1.12 $
 * $Date: 2000/10/30 17:09:00 $
 */
package physis.core;

import physis.core.virtualmachine.GeneticCodeTapeFactory;
import physis.core.virtualmachine.Instruction;
import physis.core.virtualmachine.InstructionSet;
import physis.log.Log;

import java.io.*;
import java.net.*;

/**
 * This class functions as a server. It provides access to an array of DigitalOrganisms. It runs
 * as a separate thread.
 */
public class DigitalOrganismProxy extends Thread{
    private static InstructionSet instruction_set = InstructionSet.getInstance();

    protected DigitalOrganism[] orgs;
    protected ServerSocket server;
    protected Socket client;
    protected BufferedReader in;
    protected PrintWriter out;
    
    public  static String FITNESS = "fitness";
    public  static String MERIT = "merit";
    public  static String VIVIFY = "vivify";
    
    
    public DigitalOrganismProxy(int port_number, DigitalOrganism[] local_orgs){
        try{
            server = new ServerSocket(port_number);
            orgs = local_orgs;
            this.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void run(){
        try{
            //waiting for the lifespace
            while (PHYSIS.environment.getLifeSpace() == null){
                sleep(500);
            }

            client = server.accept();
            Log.status("DigitalOrganismProxy on port " + server.getLocalPort() + " connected to client on " + client.getInetAddress() + ":" + client.getPort());
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), false);
            
            String command = null;
            int index = 0;
            while(true){
                if (in.ready()){
                    command = in.readLine();
                    //reading the index of the organism
                    index = Integer.parseInt(in.readLine());
                    if (FITNESS.equals(command)){
                        fitness(index);
                    }
                    else if (MERIT.equals(command)){
                        merit(index);
                    }
                    else if (VIVIFY.equals(command)){
                        vivify(index);
                    }
                    
                }
                else{
                    yield();
                }
                
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Writes to the socket the organism's fitness.
     * @param index The index of the organism.
     */
    protected void fitness(int index) throws IOException{
        //creating the response
        StringBuilder response = new StringBuilder();
        response.append(orgs[index].getFitness());
        response.append("\n.\n");
        //sending the response
        out.write(response.toString());
        out.flush();
        //reading the .
        in.readLine();
    }
    
    /**
     * Writes to the socket the organism's fitness.
     * @param index The index of the organism.
     */
    protected void merit(int index) throws IOException{
        //creating the response
        StringBuilder response = new StringBuilder();
        response.append(orgs[index].getMerit());
        response.append("\n.\n");
        //sending the response
        out.write(response.toString());
        out.flush();
        //reading the .
        in.readLine();
    }
    
    protected void vivify(int index) throws IOException{
        //reading the separator
        in.readLine();
        //reading the size of the codetape
        int codetape_size = Integer.parseInt(in.readLine());
        //creating the codetape
        short[] code = new short[codetape_size];
        for (int i = 0; i < codetape_size; i++){
            code[i] = Short.parseShort(in.readLine());
        }
        //reading separator
        in.readLine();
        int inherited_length = Integer.parseInt(in.readLine());
        DigitalOrganism org = PHYSIS.environment.getNeighbourRandomly(orgs[index]);
        orgs[index].vivify(org, GeneticCodeTapeFactory.getGeneticCodeTape(code, new byte[code.length]), inherited_length);
        out.write(".\n");
        out.flush();
        //reading terminator
        in.readLine();
    }
}




