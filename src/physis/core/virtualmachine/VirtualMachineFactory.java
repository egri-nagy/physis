/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: VirtualMachineFactory.java,v 1.5 2000/10/24 14:34:57 sirna Exp $
 * $Revision: 1.5 $
 * $Date: 2000/10/24 14:34:57 $
 */
package physis.core.virtualmachine;

import physis.core.Configuration;

import physis.log.Log;

/**
 * As a Factory this will make the digital-organism-instatiating easy.
 * <br>
 * CRC
 * <br>
 * 1. ensuring easy instantiating - Configuration, virtualmachine
 * @stereotype factory
 */
public class VirtualMachineFactory {
    
    /**
     * Creates a VM with initial state. The classname for the VM comes from the configuration file.
     */
    public static VirtualMachine getVM(){
	VirtualMachine vm = null;
	//instantiating the VirtualMachine class
	try{
	    
	    Class vm_class = Class.forName(Configuration.getVMClassName());
	    vm = (VirtualMachine)vm_class.newInstance();
	} catch (ClassNotFoundException cnfe) {
	    Log.error("VirtualMachine class not found: " + Configuration.getVMClassName());
	}
	catch (IllegalAccessException iae) {
	    Log.error("Class or zero-argument constructor not available: " + Configuration.getVMClassName());
	}
	catch (InstantiationException cnfe) {
	    Log.error("Cannot instantiate: " + Configuration.getVMClassName());
	}
	
	
	return vm;
    }
    
    /**
     * Creates a VM for Physis with initial state. The classname for the VM comes from the configuration file.
     */
    public static PhysisVirtualMachine getPhysisVM(){
	PhysisVirtualMachine vm = null;
	//instantiating the VirtualMachine class
	try{
	    
	    Class vm_class = Class.forName(Configuration.getVMClassName());
	    vm = (PhysisVirtualMachine)vm_class.newInstance();
	} catch (ClassNotFoundException cnfe) {
	    Log.error("PhysisVirtualMachine class not found: " + Configuration.getVMClassName());
	}
	catch (IllegalAccessException iae) {
	    Log.error("Class or zero-argument constructor not available: " + Configuration.getVMClassName());
	}
	catch (InstantiationException cnfe) {
	    Log.error("Cannot instantiate: " + Configuration.getVMClassName());
	}
	
	
	return vm;
    }

    /**
     * Creates a VM for Physis with initial state. The classname for the VM comes as parameters.
     */
    public static PhysisVirtualMachine getPhysisVM(String classname){
	PhysisVirtualMachine vm = null;
	//instantiating the VirtualMachine class
	try{
	    
	    Class vm_class = Class.forName(classname);
	    vm = (PhysisVirtualMachine)vm_class.newInstance();
	} catch (ClassNotFoundException cnfe) {
	    Log.error("PhysisVirtualMachine class not found: " + classname);
	}
	catch (IllegalAccessException iae) {
	    Log.error("Class or zero-argument constructor not available: " + classname);
	}
	catch (InstantiationException cnfe) {
	    Log.error("Cannot instantiate: " + classname);
	}
	
	
	return vm;
    }
    
}
