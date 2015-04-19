/**
 * Physis - platform for digital evolution
 * License: GNU GPL
 *
 * $Id: Log.java,v 1.8 2001/05/28 09:03:20 sirna Exp $
 * $Revision: 1.8 $
 * $Date: 2001/05/28 09:03:20 $
 */

package physis.log;

import java.util.*;
import java.text.*;
/**
 *
 * This is a general tool which makes the logging easier.
 * <br>
 * @author  sirna
 */
public class Log extends Object {
    
    public static void status(String log_message){
        System.out.println(log_message);
    }
    
    public static void debug(String log_message){
        System.out.println("DEBUG " + log_message);
    }
    
    public static void error(String log_message){
        System.out.println("ERROR " + log_message);
    }
    
    public static void error(Exception e){
        error(e.getMessage());
        e.printStackTrace();
    }
    
    public static String getFullTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MMMM.dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }
    
    
    public static String getTimeStamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd-HH.mm");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }
    
    public static String getJVMInfo(){
        Properties sysprops = System.getProperties();
        StringBuilder sb = new StringBuilder();
        sb.append("JAVA VERSION: " + sysprops.getProperty("java.version") + "\n");
        sb.append("JAVA CLASSPATH: " + sysprops.getProperty("java.class.path") + "\n");
        sb.append("running on\n");
        sb.append("OS: " + sysprops.getProperty("os.name") + " " + sysprops.getProperty("os.arch") + "\n");
        return sb.toString();
    }
    
    public static void printMemoryInfo(){
        Runtime rt = Runtime.getRuntime();
        status("Total memory: " + byte2Mbyte(rt.totalMemory(),2) + "M Free memory: " + byte2Mbyte(rt.freeMemory(),2)
                   + "M ## " + (rt.freeMemory()*100/rt.totalMemory()) + "% free ");
        
    }
    
    private static double byte2Mbyte(long bytes, int figures){
        return ((long) ((bytes / (double) (1024*1024)) * Math.pow(10, figures))) / Math.pow(10, figures);
    }
    
    //Just for testing
    public static void main(String[] args){
        Log.status(Log.getFullTimeStamp() + " Just testing...");
	Log.status(Log.getTimeStamp());
        Log.printMemoryInfo();
        Log.status(Log.getJVMInfo());
    }
}
