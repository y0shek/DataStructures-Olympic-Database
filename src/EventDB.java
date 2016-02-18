import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

/**
 * The EventDBMain class is instantiable and provides a data structure
 * made of ArrayLists, including iterators,
 * in which to house data about events and athletes.
 * 
 * @author Joshua Kellerman, CS 367
 */

public class EventDB {

	private List<Event> events = new ArrayList<Event>(); //data structure
	private Iterator<Event> eventItr; //eventItr obj
	private Iterator<String> rosterItr; //rosterItr obj

	/**
	 * Constructs an EventDB with the given lists of athletes and events.
	 * 
	 * @param List<String> athletes, List<Event> events
	 */

	EventDB(List<String> athletes, List<Event> events){ //constructor for EventDB
		this.events = events;
	} //end EventDB constructor

	/**
	 * Adds an event to database of type t
	 * 
	 * @param String the event type
	 */

	void addEvent(String t){
		eventItr = events.iterator();
		while (eventItr.hasNext()){ //check for duplicates
			if (eventItr.next().getType().compareTo(t) == 0){
				return;
			} else {
				events.add(new Event(t)); //if not, add event
			}
		}
	} //end addEvent

	/**
	 * Adds an event to database of type t
	 * 
	 * @param String n athlete name, String t event type
	 */

	void addAthlete (String n, String t){
		eventItr = events.iterator();
		Event temp = null; //event dummy
		boolean missing = true;
		while(eventItr.hasNext()){
			temp = eventItr.next();
			if(t.compareTo(temp.getType()) == 0){ //test for duplicates of event
				missing = false;
				break;
			}
		}
		if(missing){ //throw null pointer as illegal argument
			throw new IllegalArgumentException("Event not in Database");
		}
		rosterItr = temp.getRoster().iterator(); //iterate through roster
		while (rosterItr.hasNext()){ 
			if(rosterItr.next().equals(n)){
				return; //quit operation if athlete already present
			}
		}
		temp.getRoster().add(n); //otherwise, add athlete
	} //end addAthlete

	/**
	 * removes an event of type t from database
	 * 
	 * @param String t event type
	 * @return boolean event removed
	 */

	boolean removeEvent (String t){
		int counter = 0;
		eventItr = events.iterator();
		while (eventItr.hasNext()){
			Event tempEvent = eventItr.next();
			if (!tempEvent.getType().equals(t)){ //test for matches
				counter ++;
			} else {
				events.remove(counter); //if found remove, and return true
				return true;
			}
		}
		return false; //else return false
	} //end removeEvent

	/**
	 * returns boolean value representing whether event is in database (true)
	 * or not (false)
	 * 
	 * @param String t event type
	 * @return boolean event present
	 */

	boolean containsEvent (String t){
		eventItr = events.iterator();
		while (eventItr.hasNext()){
			if(t.equals(eventItr.next().getType())){ //test for matches
				return true; //if found
			}
		}
		return false; //if not
	} //end containsEvent

	/**
	 * returns boolean value representing whether athlete is in database (true)
	 * or not (false)
	 * 
	 * @param String n athlete name
	 * @return boolean athlete present
	 */

	boolean containsAthlete (String n){
		eventItr = events.iterator();
		while (eventItr.hasNext()){
			rosterItr = eventItr.next().getRoster().iterator();
			while(rosterItr.hasNext()){
				if(n.equals(rosterItr.next())){ //test for matches
					return true; //if found
				}
			}		
		}
		return false; //if not
	} //end containsAthlete

	/**
	 * returns boolean value representing whether athlete is registered with a
	 * certain event
	 * 
	 * @param String n athlete name, String t event type
	 * @return boolean athlete present in event
	 */

	boolean isRegistered (String n, String t){
		eventItr = events.iterator();
		while (eventItr.hasNext()){
			Event temp = eventItr.next();
			if(temp.getType().equals(t)){ // check for type match
				rosterItr = temp.getRoster().iterator();
				while(rosterItr.hasNext()){
					if(rosterItr.next().equals(n)){ //check for name match
						return true;
					} // end second if
				} //end second while
			} // end first if
		} // end first while
		return false;
	} // end isRegistered

	/**
	 * returns Roster String array of specified event t
	 * 
	 * @param String t event type
	 * @return List<String> Roster of event type t
	 */

	List<String> getRoster(String t){
		eventItr = events.iterator(); 
		while(eventItr.hasNext()){ //iterate through whole event list
			Event temp = eventItr.next(); //dummy to not advance iterator
			if(temp.getType().equals(t)){ //check for match
				return temp.getRoster(); //return the roster
			}
		}
		return null;
	} //end getRoster

	/**
	 * returns String array of events that athlete n is present in
	 * 
	 * @param String n athlete name
	 * @return List<String> of events athlete n is present in
	 */

	List<String> getEvents(String n){
		eventItr = events.iterator();
		List<String> returnEvent = new ArrayList<String>();
		while(eventItr.hasNext()){
			Event temp = eventItr.next();
			rosterItr = temp.getRoster().iterator();
			while (rosterItr.hasNext()){
				if (rosterItr.next().equals(n)){
					returnEvent.add(temp.getType());
				}
			}
		}
		if(returnEvent.size() != 0){
			return returnEvent;
		} else {
			return null;
		}
	}

	/**
	 * returns iterator for event list

	 * @return Iterator<Event> iterator
	 */

	Iterator<Event> iterator(){
		return events.iterator(); //returns iterator
	} //end iterator

	/**
	 * returns size of event list

	 * @return int event list size
	 */

	int size(){
		return events.size(); //returns size of list
	} //end size

	boolean removeAthlete(String n){
		boolean removed = false; //remove test starts false
		eventItr = events.iterator(); 
		while(eventItr.hasNext()){ //iterate through events
			Iterator<String> athleteItr = eventItr.next(). //create athlete list itr
					getRoster().iterator();
			while(athleteItr.hasNext()){ //iterate through athletes
				if(athleteItr.next().equals(n)){ //compare
					athleteItr.remove(); //remove
					removed = true; //report
				} 
			}
		}
		return removed; //return result
	} //end removeAthlete()
} // end class
