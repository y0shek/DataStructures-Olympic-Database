import java.util.*;
/**
 * The Event class is used to represent a single event that keeps track of its
 * type and roster (a list of athletes).
 * 
 * @author Sarah Gilliland, CS 367 TA, copyright 2012
 */
public class Event {
    private String type;         // the event type
    private List<String> roster;  // the list of athletes in the event
    
    /**
     * Constructs a event with the given type and an empty roster.
     * 
     * @param type the type of this event
     */
    public Event(String type) {
        this.type = type;
        roster = new ArrayList<String>();
    }
    
    /**
     * Returns the type of this event.
     * 
     * @return the type of this event
     */
    public String getType() {
        return type;
    }
    
    /**
     * Returns the roster of this event (i.e., the list of athletes in this event).
     * 
     * @return the roster for this event
     */
    public List<String> getRoster() {
        return roster;
    }
}
