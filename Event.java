

/**
 * @author Harry Tran
 * @version final
 * This is the model class for events
 * This class is my own reused asset from assignment 2 (console calendar)
 */
public class Event {

	private String title;
	private String date;
	private String startTime;
	private String endTime;
	
	/**
	 * ctor for Event class
	 * @param title is the title of the event
	 * @param date is the date of the event in: month day, year
	 * @param startTime is the staring time of the event
	 * @param endTime is the ending time of the event
	 */
	public Event(String title, String date, String startTime, String endTime) {
		this.title = title;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * getter for the title
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * getter for the date
	 * @return date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * getter for the starting time
	 * @return startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	
	/**
	 * getter for the ending time
	 * @return endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	
	/**
	 * changes startTime from a string to an int
	 * @return timeT which is the starting time as an integer
	 */
	public int parseStartT() {
		String format = startTime.replace(":", "");
		int timeT = Integer.parseInt(format);
		return timeT;
	}
	
	/**
	 * changes endTime from a string to an int
	 * @return timeT which is the ending time as an integer
	 */
	public int parseEndT() {
		if(endTime.equals(""))
			return 0;
		String format = endTime.replace(":", "");
		int timeT = Integer.parseInt(format);
		return timeT;
	}
	
	/**
	 * gets the year from the date and returns it as an int
	 * @return the year as an integer
	 */
	public int parseYear() {
		int i = date.indexOf(",");
		return Integer.parseInt(date.substring(i + 2));
	}
	
	/**
	 * gets the day from the date and returns it as an int
	 * @return the day as an integer
	 */
	public int parseDay() {
		return Integer.parseInt(date.substring(date.indexOf(",") - 2, date.indexOf(",")));
	}
	
	/**
	 * gets the month from the date and returns it as an int
	 * int returned by this is the [number of the month on the calendar - 1]
	 * this is to match with the numbering of the months in the calendar library
	 * @return an int value based on the month in date
	 */
	public int parseMonth() {
		if(date.substring(0, 3).toLowerCase().equals("jan"))
			return 0;
		else if(date.substring(0, 3).toLowerCase().equals("feb"))
			return 1;
		else if(date.substring(0, 3).toLowerCase().equals("mar"))
			return 2;
		else if(date.substring(0, 3).toLowerCase().equals("apr"))
			return 3;
		else if(date.substring(0, 3).toLowerCase().equals("may"))
			return 4;
		else if(date.substring(0, 3).toLowerCase().equals("jun"))
			return 5;
		else if(date.substring(0, 3).toLowerCase().equals("jul"))
			return 6;
		else if(date.substring(0, 3).toLowerCase().equals("aug"))
			return 7;
		else if(date.substring(0, 3).toLowerCase().equals("sep"))
			return 8;
		else if(date.substring(0, 3).toLowerCase().equals("oct"))
			return 9;
		else if(date.substring(0, 3).toLowerCase().equals("nov"))
			return 10;
		else
			return 11;
	}
	
	/**
	 * toString to print out the title, startime, and endtime of the Event
	 * this method is used in [D]ay view option to display the events
	 * @return title: startTime - endTime
	 */
	public String toString() {
		if(!endTime.equals(""))
			return title + ": "  + startTime + " - " + endTime;
		else
			return title + ": " + startTime;
	}
	
	/**
	 * alternative toString to print out the month, title, startTime and endTime of the Event
	 * this method is used in the [E]vent list option to display the events
	 * @return month: title @ startTime - endTime
	 */
	public String toList() {
		if(!endTime.equals(""))
			return date.substring(0, date.indexOf(",")) + ": " + title + " @ "  + startTime + " - " + endTime;
		else
			return  date.substring(0, date.indexOf(",")) + ": " + title + " @ "  + startTime;
	}
}
