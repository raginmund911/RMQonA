/**
 * 
 */
package src.com.ransomer.rmqon;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * @author ransomer
 *This is my 'controller' code...
 */
public class SDNEventQueue {
	private ArrayList <SDNEvent> eventArray = new ArrayList<SDNEvent>();
	
	//Initializer to read a text file into an array of SDNEvent objects
	public SDNEventQueue (String queue_name) {
		String line;
		BufferedReader br;
	
	//you may need a try/catch statement here...
	
	}
	
	//Method to retrieve events
	public ArrayList<SDNEvent> getSDNEvents() {
		return eventArray;
	}
	
	
}
