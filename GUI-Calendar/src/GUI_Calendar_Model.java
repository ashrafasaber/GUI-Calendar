import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI_Calendar_Model {
	int monthInd;
	   TreeMap<GregorianCalendar, TreeSet<Event>> eventsMap;
	public static GregorianCalendar calendarN = new GregorianCalendar();
	public GregorianCalendar calendar = new GregorianCalendar();
	String[] monthNames = {"January","February","March","April","May","June","July","August",
			"September","October","November","December"};
	String[] dayNames = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	ArrayList<ChangeListener> changeListeners = new ArrayList<>();
	int clickedDay, daysInMonth, yearN, monthN,month,year;
	int currentYear, currentMonth, currentDay;
	String dd;
	boolean monthChanged = false;
	private static Scanner fileScanner;

	/**
	 * Constructor for the GUI_Calendar_Model
	 */
	GUI_Calendar_Model(){
		eventsMap = new TreeMap<>();
		clickedDay=calendarN.get(Calendar.DAY_OF_MONTH);
		daysInMonth=calendarN.getActualMaximum(Calendar.DAY_OF_MONTH);// getting the days in a month
		yearN=calendarN.get(Calendar.YEAR);
		monthN=calendarN.get(Calendar.MONTH);
		dd=monthNames[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.YEAR);
			load();
	}
	/**
	 * Returns the daysInMonth->Maximum days in a Month
	 * @return daysInMonth: The maximum number of days in a month
	 */
	public int getDaysInMonth(){
		return daysInMonth;
	}

	/**
	 * Returns the daysInMonth->Maximum days in a Month for a specific calendar
	 * @return daysInMonth: The maximum number of days in a month
	 */
	public int getDaysInMonthGC(GregorianCalendar calendar){
		daysInMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH); 
		return daysInMonth;
	}
	/**
	 * This function attaches a change listener to the changeListeners ArrayList
	 * @param changeListener
	 */
	public void attach(ChangeListener changeListener)
	{
		changeListeners.add(changeListener);
	}
	/**
	 * This function updates all change listeners in the ArrayList
	 */
	public void updateListeners(){
		for(ChangeListener changeListener: changeListeners )
		{changeListener.stateChanged(new ChangeEvent(this));}
	}
	/**
	 * This function sets the clicked day 
	 */
	public void setClickedDay(int day)
	{
		clickedDay=day;
	}
	/**
	 * This function returns the clicked day
	 */
	public int getClickedDay()
	{
		return clickedDay;
	}
	/**
	 * This function returns the current year
	 * @return yearN: current year
	 */
	public int getYearN()
	{ 	
		yearN=calendarN.get(Calendar.YEAR);
		return yearN;
	}
	/**
	 * This function returns the current month
	 * @return monthN: current month
	 */
	public int getMonthN()
	{ 	
		monthN=calendarN.get(Calendar.MONTH);
		return monthN;
	}
	/** This function returns the  year
	 * @return year:   year
	 */
	public int getYear()
	{ 	
		year=calendar.get(Calendar.YEAR);
		return year;
	}
	/**
	 * This function returns the  month
	 * @return month:  month
	 */
	public int getMonth()
	{ 	
		month=calendar.get(Calendar.MONTH);
		return month;
	}
	/**
	 * This function gets the numerical value of the day of the week
	 */
	public int getDayOfWeek(int dayOfMonth){
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * This function gets the numerical value of the day of the week (current)
	 */
	public int getDayOfWeekN( ){
		return calendarN.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * This function updates the value of the calendar and moves it to the following Month
	 * @return  
	 */  
	public void nextMonth( ) {
		calendar.add(Calendar.MONTH, 1);

		daysInMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		monthChanged=true;
	}
	/**
	 * This function updates the value of the calendar and moves it to the previous Month
	 * @return previousMonth
	 */  
	public void previousMonth() {
		calendar.add(Calendar.MONTH, -1);

		daysInMonth=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		monthChanged=true;
	}	

	/**
	 * This function returns a boolean value indicating whether or not the
	 * month had been altered due to user interaction
	 * @return monthChanged
	 */
	public boolean monthChanged(){
		return monthChanged;
	} 


	/**
	 * This function sets the monthChanged value to false
	 * @return monthChanged
	 */
	public boolean monthChangedReset(){
		return monthChanged=false;
	}

	/**
	 * This function moves the selected day forward with a value of 1
	 *  
	 */  
	public void nextDay( ) { 
		clickedDay++;
		if(clickedDay>getDaysInMonth( ))
		{	nextMonth( );
		}
	 	calendar.add(Calendar.DAY_OF_WEEK, 1);
		int m=calendar.get(Calendar.MONTH);
		int y=calendar.get(Calendar.YEAR);
		dd=monthNames[m]+" "+y;

	}

	public String getDD(){
		return dd;
	}
	public int getFirstDayOfMonthIndex(){
		return calendar.get(Calendar.DAY_OF_WEEK)-1;
	}
	/**
	 * This function moves the selected day backward with a value of -1
	 *  
	 */  
	public void previousDay( ) { 
		clickedDay--;
		if(clickedDay<getFirstDayOfMonthIndex()) // if less than first day in month
		{	previousMonth( );
		clickedDay=getDaysInMonth(); // the selected day is the last day of the previous month
		}
		calendar.add(Calendar.DAY_OF_WEEK, -1);
		
		int m=calendar.get(Calendar.MONTH);
		int y=calendar.get(Calendar.YEAR);
		dd=monthNames[m]+" "+y;
	}

	public int getStartOfMonth(GregorianCalendar calendar){
		return calendar.get(GregorianCalendar.DAY_OF_WEEK);
	}
 

	/**
	 * Creates an Event
	 * @param title
	 * @param inputDate
	 * @param startTime
	 * @param endTime
	 */
	public void createEvent(String title,String inputDate,String startTime,String endTime){
		// create an event object
		Event newEvent = new Event(title, inputDate, startTime, endTime);  
		// add event object to EventsMap 
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(newEvent.getUserInputStringToDate());

		if(!checkEventCollision(newEvent)){
			if(eventsMap.containsKey(c)){
				eventsMap.get(c).add(newEvent);  //accessing the tree set inside the tree map
			} else{
				TreeSet<Event> events = new TreeSet<>(); 
				events.add(newEvent);
				eventsMap.put(c, events);
			}

		} 
	}

	/**
	 * Checks event Collision
	 * @param event
	 * @return collision
	 */
	public  boolean checkEventCollision(Event event){  
		boolean collision=false;
		for(GregorianCalendar key : eventsMap.keySet()){
			
			if(key.getTime().equals(event.userInputStringToDate))
			{
				for(Event e : eventsMap.get(key)){
					if(e.startTime.equals(event.startTime) )
					{
						System.out.println("Collision!!!!!");
						collision=true;
						JOptionPane.showMessageDialog(null, "Collision!!!!"+ 
						" ","", JOptionPane.ERROR_MESSAGE);	
					}
				}
			}
		}
		return collision;
	}
	
	/**
	 * Function to convert a String to a GregorianCalendar object
	 * @param string
	 * @return c
	 */
	public GregorianCalendar stringToGC(String string){
		
		Date dummyDate = null;
		GregorianCalendar c = new GregorianCalendar();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		try{
			dummyDate = dateFormatter.parse(string);
			c.setTime(dummyDate);
		}
		catch(ParseException e) {
			System.out.println(e.getMessage());
		}
		return c;
	}
	


	/**
	 * Checks the eventsTree map and prints the treeSet associated with the key ()
	 * @return s
	 */
	public String getEventsOnThisDay(String date){ 
		String s = "";

		String title="",startT="",endT="";
		Date dummyDate = null;
		GregorianCalendar c = new GregorianCalendar();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

		try{
			dummyDate = dateFormatter.parse(date);
			c.setTime(dummyDate);
		}
		catch(ParseException e) {
			System.out.println(e.getMessage());
		}
		//  check the events map if that key (GC Object from string conversion) exists.	
		TreeSet<Event> events = new TreeSet<>();
		
		//printDay(c);
		if (eventsMap.containsKey(c))
		{
			events = eventsMap.get(c);
			if (!events.equals(null))
				for (Event e : events){
					//	s += e.toString() + "\n";
					title=e.getTitle();
					startT=e.getStartTime();
					endT=e.getEndTime();		
					System.out.println(title+" "+startT+" - "+endT+"\n");
				}
		}
		else 
			System.out.println("No Events Scheduled");

		return s;
	}

	public void setCurrentYear(int y){this.currentYear=y;}
	public void setCurrentDay(int d){this.currentDay=d;}
	public void setCurrentMonth(int m){this.currentMonth=m;}
	public int getCurrentYear(){return currentYear;}
	public int getCurrentDay(){return currentDay;}
	public int getCurrentMonth(){return currentMonth;}

 
	/**
	 * Quits and saves events 
	 * @throws IOException
	 */	
	public  void quit(){ 
		String title="",date="",startT="",endT="";
		try{
			PrintWriter printWriter = new PrintWriter("events.txt");
			for(GregorianCalendar key : eventsMap.keySet()){

				for (Event e : eventsMap.get(key)){
					title=e.getTitle();
					date=e.getInputDate();
					startT=e.getStartTime();
					endT=e.getEndTime();
					printWriter.println(title+" "+date+" "+startT+" "+endT);
				}
			}
			printWriter.close();
			System.exit(0);
		}
		catch(IOException e){System.out.println("IOException");}
	}

	/**
	 * Loads events
	 */		
	public void load(){
	try{
			fileScanner = new Scanner(new File("events.txt"));
		}
		catch(Exception e){
			System.out.println("could not find file");
		}
		if(fileScanner.hasNext() != true)
		{
			System.out.println("This is the first run");	
		}else
		{     
			while(fileScanner.hasNext())
			{
				String titleReader = fileScanner.next(); 
				String dateReader = fileScanner.next(); // reading Date
				String startTimeReader = fileScanner.next(); // reading Start Time
				String endTimeReader = fileScanner.next(); // reading End Time
				createEvent(titleReader,dateReader,startTimeReader,endTimeReader);
			}
		}
		fileScanner.close();	
	}
 
}
