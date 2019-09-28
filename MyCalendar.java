

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;

enum MONTHS {
	January, Febuary, March, April, May, June, July, August, September, October, November, December;
}
enum DAYS {
	Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday ;
}

/**
 * @author Harry Tran
 * @studentID 011075987
 * @version final
 * This is the controller class for the GuiCalendar (view) and Event (model) classes
 * My own reused asset from the console calendar assignment (assignment 2)
 * Has been modified to fit a gui calendar instead of a console calendar
 */
public class MyCalendar {
	
	private ArrayList<Event> events;
	private Calendar calendar;
	private static MONTHS[] months = MONTHS.values();
	private static DAYS[] days = DAYS.values();
	
	/**
	 * ctor for MyCalendar class
	 * creates a calendar from gregorian calendar
	 * creates an ArrayList of Events
	 */
	public MyCalendar() {
		calendar = new GregorianCalendar();
		events = new ArrayList<Event>();
	}
	
	/**
	 * getter for the calendar
	 * @return calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}
	
	/**
	 * getter for the ArrayList of events
	 * @return events
	 */
	public ArrayList<Event> getEvents() {
		return events;
	}
	
	/**
	 * @return the Month and Year
	 */
	public String fetchMonth() {
		return getMonth() + " " + calendar.get(Calendar.YEAR);
	}
	
	/**
	 * @return Su Mo Tu We Th Fr Sa
	 */
	public String fetchDayLine() {
		return "Su Mo Tu We Th Fr Sa";
	}
	
	/**
	 * @return a string that contains all of the days of the month
	 */
	public String fetchCalendarBody() {
		return calendarDays();
	}
	
	/**
	 * Displays the events that occur on this day
	 * @return a list of events
	 */
	public String displayEvents() {
		ArrayList<Event> eventsThisDay = eventsThisDay(eventsThisMonth(events));
		String eventList = "";
		for (Event e: eventsThisDay) {
			eventList += e.toString() + "\n";
		}
		return eventList;
	}
	
	/**
	 * rolls the calendar a month back
	 */
	public void prevMonth() {
		if(calendar.get(Calendar.MONTH) == 0)
			calendar.roll(Calendar.YEAR, false);
		calendar.roll(Calendar.MONTH, false);
	}
	
	/**
	 * rolls the calendar a month forward
	 */
	public void nextMonth() {
		if(calendar.get(Calendar.MONTH) == 11)
			calendar.roll(Calendar.YEAR, true);
		calendar.roll(Calendar.MONTH, true);
	}
	
	/**
	 * rolls the calendar a day back
	 */
	public void prevDay() {
		if(calendar.get(Calendar.MONTH) == 0 && calendar.get(Calendar.DAY_OF_MONTH) == 1)
			calendar.roll(Calendar.YEAR, false);
		if(calendar.get(Calendar.DAY_OF_MONTH) == 1)
			calendar.roll(Calendar.MONTH, false);
		calendar.roll(Calendar.DATE, false);
	}
	
	/**
	 * rolls the calendar a day forward
	 */
	public void nextDay() {
		if(calendar.get(Calendar.MONTH) == 11 && calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
			calendar.roll(Calendar.YEAR, true);
		if(calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			calendar.roll(Calendar.MONTH, true);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		}
		else
			calendar.roll(Calendar.DATE, true);
	}
	
	/**
	 * returns the month as a string given an int
	 * @param target month as an int
	 * @return the month as a string (as in "May", "March", etc )
	 */
	public String getMonth() {
		for(int i = 0; i < months.length; i++)
			if(i == calendar.get(Calendar.MONTH))
				return String.valueOf(months[i]);
		return "";
	}
	
	/**
	 * converts int representation of day into string representation 
	 * @param target day as an int
	 * @return day as a string (as in "Monday", "Friday", etc)
	 */
	public String getDay() {
		for(int i = 0; i < days.length; i++)
			if(i == calendar.get(Calendar.DAY_OF_WEEK) - 1)
				return String.valueOf(days[i]);
		return "";
	}
	
	/**
	 * method to populate the calendar with events from a text file
	 * IMPORTANT: the text file format for each event should be as follows....
	 * Title of event				Ex: Go to class
	 * Date of event				Ex: March 20, 2018
	 * Starting time of event		Ex: 12:00
	 * Ending time of event			Ex: 13:15 (if the event does not have an end time the line will be blank in the file)
	 * .... This method will not function properly if the text file does not follow this format
	 * @param file the name of the file that will be used to load, ideally it will be named 'events.txt'
	 * @throws IOException if there is an error in creating a file or no file is found
	 */
	public void load(String file) throws IOException {
		// checks if events.txt exists, if it does not then a fresh one will be created
		File f = new File(file);
		if(!f.exists()) {
		    Formatter c = new Formatter(file);
		    c.close();
		}
		// events.txt exists and will be used to populate the calendar
		else {
			String line, title = "", date = "", startTime = "", endTime = "";
			Event event;
			int count = 0;
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null) {
				if(count == 0)
					title = line;
				else if(count == 1)
					date = line;
				else if(count == 2)
					startTime = line;
				else if(count == 3)
					endTime = line;
				if(count == 3) {
					event = new Event(title, date, startTime, endTime);
					events.add(event);
				}
				count++;
				if(count > 3)
					count = 0;
			}
			br.close();
		}
	}
	
	/**
	 * method that sorts the events in the calendar from earliest to latest events
	 * uses a reusable sortBy method to do so
	 */
	public void organize() {
		sortBy("year");
		sortBy("month");
		sortBy("day");
		sortBy("time");
	}
	
	/**
	 * method to create and add an event to the calendar, also checks for time conflicts
	 * calendar is manipulated when this method checks for conflicts
	 * @param title 
	 * @param date in MM/DD/YYYY format
	 * @param startTime
	 * @param endTime
	 * @return true if the event was successfully added, false if the event was found to conflict with an existing event
	 */
	public boolean createEvent(String title, String date, String startTime, String endTime) {
		// since date is in MM/DD/YYYY format it has to be converted to Month Day, Year format
		String formatDate = convertDate(date);
		Event newEvent = new Event(title, formatDate, startTime, endTime);
		calendar.set(Calendar.DAY_OF_MONTH, newEvent.parseDay());
		// check if the new event conflicts with an existing event
		if(!checkConflict(newEvent, events)) {
			events.add(newEvent);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * rolls the calendar to the day specified in the param
	 * @param day is the day to go to
	 */
	public void goTo(int day) {
		calendar.set(Calendar.DAY_OF_MONTH, day);
	}
	
	/**
	 * method for Quit function
	 * writes all events populating the calendar onto an events.txt file
	 * events will be written in the same format specified in load function
	 * @param file is events.txt
	 * @throws IOException if no file is found
	 */
	public void record(String file) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for(Event e : events) {
			bw.write(e.getTitle());
			bw.newLine();
			bw.write(e.getDate());
			bw.newLine();
			bw.write(e.getStartTime());
			bw.newLine();
			bw.write(e.getEndTime());
			bw.newLine();
		}
		bw.close();
	}
	
	//************************************** -- Private methods -- ************************************
	/**
	 * method to sort the arraylist of events
	 * @param events the arraylist
	 * @param sortType is the sort criteria, i.e "year" will sort by the year
	 */
	private void sortBy(String sortType) {
		Event temp;
		for(int a = 0; a < events.size(); a++) {
			for(int b = 0; b < events.size(); b++) {
				if(sortType.equals("year")) {
					if(events.get(a).parseYear() < events.get(b).parseYear()) {
						temp = events.get(a);
						events.set(a, events.get(b));
						events.set(b, temp);
					}
				}
				else if(sortType.equals("month")) {
					if(events.get(a).parseMonth() < events.get(b).parseMonth() && events.get(a).parseYear() == events.get(b).parseYear()) {
						temp = events.get(a);
						events.set(a, events.get(b));
						events.set(b, temp);
					}
				}
				else if(sortType.equals("day")) {
					if(events.get(a).parseDay() < events.get(b).parseDay() && events.get(a).parseMonth() == events.get(b).parseMonth() && events.get(a).parseYear() == events.get(b).parseYear()) {
						temp = events.get(a);
						events.set(a, events.get(b));
						events.set(b, temp);
					}
				}
				else if(sortType.equals("time")) {
					if(events.get(a).parseStartT() < events.get(b).parseStartT() && events.get(a).parseDay() == events.get(b).parseDay() && events.get(a).parseMonth() == events.get(b).parseMonth() && events.get(a).parseYear() == events.get(b).parseYear()) {
						temp = events.get(a);
						events.set(a, events.get(b));
						events.set(b, temp);
					}
				}
			}
		}
	}
	
	/**
	 * check if a new event conflicts with an existing event
	 * @param event the new event
	 * @param e the list of existing events to check against
	 * @return true if there is a conflict, false if there is no conflict
	 */
	private boolean checkConflict(Event event, ArrayList<Event> e) {
		ArrayList<Event> eventsInDay = eventsThisDay(eventsThisMonth(e));
		for(Event a : eventsInDay) {
			if(event.parseStartT() >= a.parseStartT() && event.parseStartT() <= a.parseEndT())
				return true;
			if(event.parseEndT() >= a.parseStartT() && event.parseEndT() <= a.parseEndT())
				return true;
			if(event.parseStartT() <= a.parseStartT() && event.parseEndT() >= a.parseEndT())
				return true;
		}
		return false;
	}
	
	/**
	 * filters a list of events to a list of events that occur in a specific day
	 * @param e the list of events
	 * @return list of events that are happening in a specific day
	 */
	private ArrayList<Event> eventsThisDay(ArrayList<Event> e) {
		ArrayList<Event> eventsInDay = new ArrayList<Event>();
		for(Event a : e)
			if(a.parseDay() == calendar.get(Calendar.DAY_OF_MONTH))
				eventsInDay.add(a);
		return eventsInDay;
	}
	
	/**
	 * filters a list of events to a list of events that occur in a specific month
	 * @param e the list of events
	 * @return list of events that are happening in a specific month
	 */
	private ArrayList<Event> eventsThisMonth(ArrayList<Event> e) {
		ArrayList<Event> eventsInMonth = new ArrayList<Event>();
		for(Event a : e) 
			if(a.parseMonth() == calendar.get(Calendar.MONTH) && a.parseYear() == calendar.get(Calendar.YEAR))
				eventsInMonth.add(a);
		return eventsInMonth;
	}
	
	/**
	 * converts the date from MM/DD/YYYY format to month day, year
	 * @param date in MM/DD/YYYY format
	 * @return date in month day, year format
	 */
	private String convertDate(String date) {
		int monthN = Integer.parseInt(date.substring(0, 2));
		String month = String.valueOf(months[monthN - 1]);
		String day = date.substring(3, 5);
		String year = date.substring(6);
		return month + " " + day + ", " + year;
	}
	
	/**
	 * returns a string containing all the days in the current month
	 */
	private String calendarDays() {
		// sets up spacing for 1st week
		String display = "";
		Calendar proxy = (Calendar) calendar.clone();
		proxy.set(Calendar.DAY_OF_MONTH, 1);
		int firstDay = proxy.get(Calendar.DAY_OF_WEEK); // getting when the 1st day starts
		int lineBreaker = 0; // set up a line break after every week (after saturday)
		// spaces for week 1
		for(int i = 0; i < days.length; i++) 
			if(i != firstDay - 1) {
				display += " ,";
				lineBreaker++;
			}
			else
				break;
		// print rest of calendar
		for(int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			display += i + ",";
			lineBreaker++;
			if(lineBreaker % 7 == 0)
				display += "*";
		}
		return display;
	}
}
