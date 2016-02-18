import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The EventDBMain class provides the user interface and IO interface
 * to interact with an EventDB object from a shell terminal.
 * 
 * @author Joshua Kellerman, CS 367
 */


public class EventDBMain { //start class
	static EventDB database; //universal database
	public static void main(String[] args) { //start main

		//check if user inputs 1 argument

		if (args.length != 1){
			System.out.println("Usage: java EventDBMain FileName\nQuitting...");
			return;
		}
		
		Scanner fileIn;
		
		//error checking for file
		
		try{
			fileIn = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Cannot access input file!");
			return;
		} finally {
		}	

		//parsing to data database

		String currLine; //the current line being read
		String[] results; //the string array each word will be put into
		List<String> athletes = new ArrayList<String>(); //athlete list to copy in
		List<Event> events = new ArrayList<Event>(); // events list to copy in
		boolean eventExists; 
		Event tempEvent1 = null; //dummy event
		boolean athleteFound;

		//create the EventDB object
		database = new EventDB(athletes, events);

		//read in line by line

		while(fileIn.hasNextLine()){
			currLine = fileIn.nextLine(); 
			results = currLine.split(",");
			for (int i = 1; i < results.length; i++){
				eventExists = false;			// reset alreadyExists boolean
				if(events.size()>0){			//make sure events has elements 
					//before check
					for(int j = 0; j < events.size(); j++){
						if(convertCaps(results[i]).equals
								(events.get(j).getType())){ //compare list name to all others
							eventExists = true; //if found
							tempEvent1 = events.get(j); //put event in temp
							break;
						}
					}
				} if (eventExists){	//check for athlete in raster
					athleteFound = false;
					for(int k = 0; k < tempEvent1.getRoster().size(); k ++){
						if(tempEvent1.getRoster().contains(convertCaps(results[0]))){
							athleteFound = true;
							break;
						}
					}

					if(!athleteFound){ //add athlete
						database.addAthlete(convertCaps(results[0]), 
								tempEvent1.getType()); //add athlete to event
					}

				} else if (!eventExists){
					Event tempEvent = 
							new Event(convertCaps(results[i])); //create new event
					events.add(tempEvent); //add new event to roster
					database.addAthlete(convertCaps(results[0]), 
							tempEvent.getType()); //add athlete to roster
				}
			}
		}


		Scanner stdin = new Scanner(System.in);  // for reading console input
		boolean done = false;
		while (!done) {
			System.out.print("Enter option ( cdprswx ): ");
			String input = stdin.nextLine();

			// only do something if the user enters at least one character
			if (input.length() > 0) {
				char choice = input.charAt(0);  // strip off option character
				String remainder = "";  // used to hold the remainder of input
				if (input.length() > 1) { // if there is an argument
					// trim off any leading or trailing spaces
					remainder = input.substring(1).trim(); 

					switch (choice) { // the commands that have arguments

					case 'c':

						//clear a given event from database

						String userTypeC = convertCaps(remainder);
						if(database.containsEvent(userTypeC)){
							database.removeEvent(userTypeC);
							System.out.println("Event Removed.");
						} else {
							System.out.println("Event not Found!");
						} //end if else
						break; //end case c

					case 'p':

						//print list of events for a given athlete

						String userTypeP = convertCaps(remainder);
						if(returnAthleteList().contains(userTypeP)){
							for(int i = 0; i < database.getEvents(userTypeP).size()-1; i ++){
								System.out.print(database.getEvents(userTypeP).get(i) +", ");
							} //end for
							System.out.println(database.getEvents(userTypeP).get
									(database.getEvents(userTypeP).size()-1));

						} else {
							System.out.println("Athlete not found.");
						} //end if

						break; //end case p

					case 'r':

						// print roster list
						String userTypeR = convertCaps(remainder);
						Iterator<Event> eventItr = database.iterator();
						Event tempEvent = null;
						boolean found = false;

						while(eventItr.hasNext()){
							tempEvent = eventItr.next();
							if(tempEvent.getType().equals(userTypeR)){
								found = true;
								break;
							} //end if
						} //end while

						//check if event is found
						if(!found){
							System.out.println("Event not found.");
							break;
						} else if (found) { 
							for(int i = 0; i < tempEvent.getRoster().size()-1; i ++){
								System.out.print(tempEvent.getRoster().get(i) + ", ");
							}	
							System.out.println(tempEvent.getRoster().
									get(tempEvent.getRoster().size()-1));
						} //end if
						break; // end case r

					case 's':
						// The following code reads in a comma-separated sequence 
						// of strings.  If there are exactly two strings in the 
						// sequence, the strings are assigned to name1 and name2.
						// Otherwise, an error message is printed.
						String[] tokens = remainder.split("[,]+");
						if (tokens.length != 2) {
							System.out.println("need to provide exactly two names");
						}
						else {
							String name1 = tokens[0].trim();
							String name2 = tokens[1].trim();

							//create temp objects
							List<String> typeList = new ArrayList<String>();
							eventItr = database.iterator();
							tempEvent = null;

							//check to see if both names are in any event rosters
							while(eventItr.hasNext()){
								tempEvent = eventItr.next();
								if(tempEvent.getRoster().contains(convertCaps(name1)) && 
										tempEvent.getRoster().contains(convertCaps(name2))){
									typeList.add(tempEvent.getType()); //add to arraylist
								} //end if

							} //end while
							if (typeList.size() == 0){
								System.out.println("none"); //no matches exist
							} else {
								for(int i = 0; i < typeList.size()-1; i++){
									System.out.print(typeList.get(i)+ ", "); //print list	
								} //end for and print last element
								System.out.println(typeList.get(typeList.size()-1)); 
							} //end if

						} //end if
						break; // end case s

					case 'w': //removes athlete from roster
						String userTypeW = convertCaps(remainder);
						if(returnAthleteList().contains(userTypeW)){ //check contains
							database.removeAthlete(userTypeW); //if true remove
							System.out.println(userTypeW + " withdrawn from all events");

						} else {
							System.out.println("athlete not found");
						}
						break; //end case w

					default: // ignore any unknown commands
						System.out.println("Incorrect command.");
						break;

					} // end switch
				} // end if
				else { //if there is no argument
					switch (choice) { // the commands without arguments

					case 'd': 
						//print out events and athletes totals
						System.out.println("Events: "+ database.size() 
								+ ", Athletes: " + returnAthleteList().size());

						//print out athletes/event
						System.out.println("# of athletes/event: most " + 
								Math.round(athletesPerEvent(1)) + ", least " + 
								Math.round(athletesPerEvent(-1)) + ", average " + 
								Math.round((athletesPerEvent(0))));

						//print out events/athlete
						System.out.println("# of events/athlete: most " + 
								Math.round(eventsPerAthlete(1)) + ", least " + 
								Math.round(eventsPerAthlete(-1)) + ", average " + 
								Math.round(eventsPerAthlete(0))); 

						//print out Most Popular
						System.out.print("Most Popular: "); 
						for(int i = 0; i < popularEvent().size()-1; i ++){
							System.out.print(popularEvent().get(i) +", ");
						}
						System.out.print(popularEvent().get(popularEvent().size()-1));
						System.out.println(" [" + mostPopReturn() + "]");

						//print out Least Popular
						System.out.print("Least Popular: "); 
						for(int i = 0; i < leastPopularEvent().size()-1; i ++){
							System.out.print(leastPopularEvent().get(i) +", ");
						}
						System.out.print(leastPopularEvent().get(leastPopularEvent().size()-1));
						System.out.println(" [" + leastPopReturn() + "]");

						break; //end case d

					case 'x': //exits program
						done = true;
						System.out.println("exit");
						break; //end case x

					default:  // a command with no argument
						System.out.println("Incorrect command.");
						break;
					} // end switch
				} // end else  
			} // end if
		} // end while
	} // end main

	/**
	 * Capitalizes the first character of every word in a String of words separated
	 * by white spaces.
	 * 
	 * @param String s
	 * @return String words
	 */

	public static String convertCaps(String s){
		if (s.length() <= 0){ //check to make sure string has characters
			return s; //return if not
		}
		String finalString = "";
		String[] words = s.split(" "); //split words into string array

		for(int i = 0; i<words.length; i++){ //go through array
			char first = words[i].charAt(0);
			first = Character.toUpperCase(first); //first character to upper case
			String end = words[i].substring(1).toLowerCase(); //rest of characters to lower case
			finalString += first + end + " "; //concatonate all words
		}
		return finalString.trim(); //trim off space at end
	} //end convertCaps

	/**
	 * The returnEventList returns the current list of events in the EventDB obj.
	 * returns: List<Event>
	 * @return List<Event> returns copy of Event List from database
	 */

	static List<Event> returnEventList(){
		Iterator<Event> eventItr =	database.iterator(); //go through each element
		List<Event> returnList = new ArrayList<Event>(); //add to new arraylist
		while(eventItr.hasNext()){ 
			returnList.add((Event)eventItr.next()); //copy
		}
		return returnList; //return

	} //end returnEventList

	static List<String> returnAthleteList(){
		boolean exists = false;
		List<String> returnList = new ArrayList<String>();
		if (database.size()>0){

			// this triple for loop is the same complexity as an iterator, 
			//iterator, contains loop but is homogeneous.

			for(int i = 0; i < database.size(); i++){ 
				//go through eventlist copy
				for(int k = 0; k < returnEventList().get(i).getRoster().size(); k++){ 
					//go through each roster
					for(int o = 0; o < returnList.size(); o++){
						exists = false; 
						if(returnEventList().get(i).getRoster().get(k).equals(returnList.get(o))){
							//compares each name in returnList to each name in every roster.
							exists = true;
							break; //do not add if already exists in returnList
						} //end first if
					} //end third for				
					if(!exists){ //if it doesn't exist, add
						returnList.add(returnEventList().get(i).getRoster().get(k));
					} //end second if
				} // end second for
			} // end first for
		}	// end first if
		return returnList;
	} //end returnAthleteList

	/**
	 * The athletesPerEvent returns either the lowest, highest, or average
	 * athletes per event, depending on choice (1 = highest, -1 = lowest, 0 = avg)
	 * 
	 * @param int choice 1 = highest, -1 = lowest, 0 = average
	 * @return double answer
	 */

	static double athletesPerEvent(int choice){
		double returnThis = 0.0; //double to return
		double rosterAvgTemp = 0.0;

		if(database.size() > 0){ //check to make sure we have events

			if(choice == -1){ // for the least value
				returnThis = leastPopReturn();

			} else if(choice == 1){ // for the greatest value
				returnThis = mostPopReturn();

			} else if (choice == 0){ //for the average
				for(int i = 0; i < database.size(); i++){
					rosterAvgTemp += returnEventList().get(i).getRoster().size();
				}
				rosterAvgTemp /= returnEventList().size();
				returnThis = rosterAvgTemp;

			} //end else-if branch
			return returnThis; 
		}
		else {
			return 0.0;
		}
	} //end athletesPerEvent

	/**
	 * The eventsPerAthlete returns either the lowest, highest, or average
	 * events per athlete, depending on choice (1 = highest, -1 = lowest, 0 = avg)
	 * 
	 * @param int choice 1 = highest, -1 = lowest, 0 = average
	 * @return double answer
	 */

	static double eventsPerAthlete(int choice){

		int most = 0; //set variables
		int least = 0;
		int counter = 0;
		Iterator<String> athleteItr = returnAthleteList().iterator();
		if(database.size() > 0){ //check to make sure we have events

			//find average choice 0
			if(choice == 0){
				while(athleteItr.hasNext()){
					counter +=  database.getEvents(athleteItr.next()).size();
				}
				return counter/returnAthleteList().size();

				//find least choice -1
			} else if (choice == -1){  //find least
				least = database.getEvents(returnAthleteList().get(0)).size();
				int leastTemp = 0;
				while(athleteItr.hasNext()){
					String athleteTemp = athleteItr.next();
					leastTemp = database.getEvents(athleteTemp).size();
					if(least > leastTemp)
						least = leastTemp;
				} return least * 1.0;

				//find most choice 1
			} else if (choice == 1){
				most = 0;
				int mostTemp = 0;
				while(athleteItr.hasNext()){
					String athleteTemp = athleteItr.next();
					mostTemp = database.getEvents(athleteTemp).size();
					if(most < mostTemp)
						most = mostTemp; //comparison
				} return most; //return most
			}			
		} return 0.0; //otherwise return 0
	}

	/**
	 * The popularEvent method finds a list of the most popular events and
	 * returns them as an arraylist.
	 * 
	 * @return List<String> list of most popular events
	 */

	static List<String> popularEvent(){
		List<String> tempList = new ArrayList<String>(); //list to return

		for(int i = 0; i < database.size(); i++){
			if(mostPopReturn() == returnEventList().get(i).getRoster().size()){
				tempList.add(returnEventList().get(i).getType()); //count and compare
			}//end if
		}//end for
		return tempList;	
	}// end popularEvent

	/**
	 * The mostPopReturn method returns the integer of the amount of people
	 * in the most popular event.
	 * 
	 * @return int number of people in raster of most popular event with the
	 * same number of people on roster
	 */

	static int mostPopReturn(){
		int mostPop = 0;
		for(int i = 0; i < database.size(); i++){
			if(mostPop < returnEventList().get(i).getRoster().size()){
				mostPop = returnEventList().get(i).getRoster().size();
			} //count and compare
		}
		return mostPop;
	} //end mostPopReturn

	/**
	 * The leastPopularEvent method returns a list of the least popular events types.
	 * 
	 * @return List<String> list of the types of least popular events.
	 */

	static List<String> leastPopularEvent(){
		List<String> tempList = new ArrayList<String>();

		for(int i = 0; i < database.size(); i++){
			if(leastPopReturn() == returnEventList().get(i).getRoster().size()){
				tempList.add(returnEventList().get(i).getType());
			} //count and compare
		}
		return tempList;	
	} //end leastPopularEvent

	/**
	 * The leastPopReturn method returns and integer of number of least
	 * popular events.
	 * 
	 * @return int the number of least popular events with same number of
	 * people on roster
	 */

	static int leastPopReturn(){
		if(database.size() > 0){
			int leastPop = returnEventList().get(0).getRoster().size();
			for(int i = 0; i < database.size(); i++){
				if(leastPop > returnEventList().get(i).getRoster().size()){
					leastPop = returnEventList().get(i).getRoster().size();
				}
			}
			return leastPop;
		} return 0;
	} //end leastPopReturn
} // end class
