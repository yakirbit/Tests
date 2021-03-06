package bgu.atd.a1;

import java.util.LinkedList;
import java.util.List;

/**
 * an abstract class that represents private states of an actor
 * it holds actions that the actor has executed so far 
 * IMPORTANT: You can not add any field to this class.
 */
public abstract class PrivateState {
	
	// holds the actions' name what were executed
	private List<String> history;

	public PrivateState() {
		this.history = new LinkedList<String>();
	}

	public List<String> getLogger(){
		return history;
	}
	
	/**
	 * add an action to the records
	 *  
	 * @param actionName
	 */
	public void addRecord(String actionName){
		history.add(actionName);
	}
	
	
}
