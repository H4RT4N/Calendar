

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

/**
 * @author Harry Tran
 * @version final
 * This class is the view in the gui calendar project
 */
public class GuiCalendar {

private MyCalendar calendar;
	
	/**
	 * ctor for GuiCalendar, initializes a MyCalendar as the controller
	 */
	public GuiCalendar() {
		calendar = new MyCalendar();
	}
	
	// getter for MyCalendar
	public MyCalendar getCalendar() {return calendar;}
	
	/**
	 * sets all the buttons in an arraylist to white
	 * @param buttons the list of buttons
	 */
	public void repaintButtons(ArrayList<JButton> buttons) {
		for(JButton b: buttons)
			b.setBackground(Color.WHITE);
	}
	
	/**
	 * sets the text in the day panel to match the current day
	 * @param date the date on the day panel
	 * @param eventList the list of events on the day panel
	 */
	public void updateDayDisplay(JLabel date, JTextArea eventList) {
		date.setText(calendar.getDay() + ", " + calendar.getMonth() + " " + calendar.getCalendar().get(Calendar.DAY_OF_MONTH));
		eventList.setText(calendar.displayEvents());
	}
	
	/**
	 * paints the month panel with the day panel attacted to it
	 * @param monthPanel
	 * @param date
	 * @param eventList
	 * @param monthUtil
	 */
	public void paintDisplay(JPanel monthPanel, JLabel date, JTextArea eventList, JPanel monthUtil) {
		monthPanel.removeAll(); // clean up the display
		// Month & Year
		JButton month = new JButton();
		month.setText(calendar.fetchMonth());
		month.setPreferredSize(new Dimension(385, 35));
		month.setBackground(Color.WHITE);
		month.setBorderPainted(false);
		monthPanel.add(month, BorderLayout.NORTH);
		// Su Mo Tu We Th Fr Sa
		JPanel body = new JPanel();
		ArrayList<JButton> buttons = new ArrayList<JButton>();
		body.setBackground(Color.WHITE);
		body.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		String dayLine = calendar.fetchDayLine();
		int x = 0;
		int y = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		for(int i = 0; i <= 20; i += 3) {
			JButton weekDay = new JButton(dayLine.substring(i, i + 2));
			weekDay.setPreferredSize(new Dimension(55,35));
			weekDay.setBackground(Color.WHITE);
			weekDay.setBorderPainted(false);
			gc.gridx = x;
			gc.gridy = y;
			body.add(weekDay, gc);
			x++;
			x = x % 7;
		}
		y++;
		// Days of the month
		String calendarDays = calendar.fetchCalendarBody();
		for(int i = 0; i < calendarDays.length(); i++) {
			if(calendarDays.substring(i, i+1).equals("*"))
				y++;
			if(!calendarDays.substring(i, i+1).equals(",") && !calendarDays.substring(i, i+1).equals("*")) {
				String day = "";
				while(!calendarDays.substring(i, i+1).equals(",")) { 
					day += calendarDays.substring(i, i+1);
					i++;
				}
				JButton dayOfMonth = new JButton(day);
				dayOfMonth.setPreferredSize(new Dimension(55,35));
				dayOfMonth.setBorderPainted(false);
				gc.gridx = x;
				gc.gridy = y;
				dayOfMonth.setBackground(Color.WHITE);
				if(!dayOfMonth.getText().equals(" ") && calendar.getCalendar().get(Calendar.DAY_OF_MONTH) == Integer.parseInt(dayOfMonth.getText()))
					dayOfMonth.setBackground(Color.LIGHT_GRAY);
				if(!dayOfMonth.getText().equals(" ")) {
					buttons.add(dayOfMonth);
					dayOfMonth.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							repaintButtons(buttons);
							int day = Integer.parseInt(dayOfMonth.getText());
							dayOfMonth.setBackground(Color.LIGHT_GRAY);
							calendar.goTo(day);
							updateDayDisplay(date, eventList);
						}
					});
				}
				body.add(dayOfMonth, gc);
				x++;
				x = x % 7;
			}
		}	
		body.setPreferredSize(new Dimension(385, 35 * y));
		monthPanel.add(body, BorderLayout.CENTER);
		monthPanel.add(monthUtil, BorderLayout.SOUTH);
	}

	/**
	 * builds a frame and displays the gui calendar
	 * @throws IOException if events.txt is not found
	 */
	public void view() throws IOException {
		// load events
		calendar.load("events.txt");
		// organize events
		calendar.organize();
		
		// create frame
		JFrame jf = new JFrame();
		jf.setLayout(new BorderLayout());
		
		// Day View *******************************************************************************************************************************************************************************************
		JPanel dayPanel = new JPanel();
		dayPanel.setLayout(new BorderLayout());
		dayPanel.setPreferredSize(new Dimension(300, 40*8));
		dayPanel.setBackground(Color.BLACK);
		// Date
		JLabel date = new JLabel(calendar.getDay() + ", " + calendar.getMonth() + " " + calendar.getCalendar().get(Calendar.DAY_OF_MONTH));
		date.setForeground(Color.WHITE);
		dayPanel.add(date, BorderLayout.NORTH);
		// Event List
		JTextArea eventList = new JTextArea();
		eventList.setEditable(true);
		eventList.setBackground(Color.DARK_GRAY);
		eventList.setForeground(Color.WHITE);
		eventList.setText("Event name: start time - end time (24 hour clock) \n \n" + calendar.displayEvents());
		dayPanel.add(eventList, BorderLayout.CENTER);
		JScrollPane bar = new JScrollPane(eventList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		dayPanel.add(bar);
		//****************************************************************************************************************************************************************************
		// Month View ****************************************************************************************************************************************************
		JPanel monthPanel = new JPanel();
		monthPanel.setLayout(new BorderLayout());
		monthPanel.setPreferredSize(new Dimension(395, 40*9));
		// Utility for month view
		JPanel monthUtil = new JPanel();
		monthUtil.setPreferredSize(new Dimension(385, 35));
		monthUtil.setLayout(new FlowLayout());
		monthUtil.setBackground(Color.WHITE);
		JButton prevMonth = new JButton("<="); // button for previous month
		prevMonth.setBorderPainted(false);
		prevMonth.setBackground(Color.WHITE);
		prevMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calendar.prevMonth();
				updateDayDisplay(date, eventList);
				paintDisplay(monthPanel, date, eventList, monthUtil);
			}
		});
		JButton nextMonth = new JButton("=>"); // button for next month
		nextMonth.setBorderPainted(false);
		nextMonth.setBackground(Color.WHITE);
		nextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calendar.nextMonth();
				updateDayDisplay(date, eventList);
				paintDisplay(monthPanel, date, eventList, monthUtil);
			}
		});
		JButton quit = new JButton("Quit"); // button for quit function
		quit.setBorderPainted(false);
		quit.setBackground(Color.BLACK);
		quit.setForeground(Color.WHITE);
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try { 
					calendar.record("events.txt");
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
				jf.dispose();
			}
		});
		monthUtil.add(prevMonth);
		monthUtil.add(nextMonth);
		monthUtil.add(quit);
		// end of utility
		paintDisplay(monthPanel, date, eventList, monthUtil);
		// end of month view **************************************************************************************************************************************************************************
		// Utility for day view  ****************************************************************************************************************************************
		JPanel dayUtil = new JPanel();
		dayUtil.setPreferredSize(new Dimension(385, 35));
		dayUtil.setLayout(new FlowLayout());
		dayUtil.setBackground(Color.WHITE);
		JButton prevDay = new JButton("<="); // button for previous day
		prevDay.setBorderPainted(false);
		prevDay.setBackground(Color.WHITE);
		prevDay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calendar.prevDay();
				updateDayDisplay(date, eventList);
				paintDisplay(monthPanel, date, eventList, monthUtil);
			}
		});
		JButton nextDay = new JButton("=>"); // button for next day
		nextDay.setBorderPainted(false);
		nextDay.setBackground(Color.WHITE);
		nextDay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calendar.nextDay();
				updateDayDisplay(date, eventList);
				paintDisplay(monthPanel, date, eventList, monthUtil);
			}
		});
		dayUtil.add(prevDay);
		dayUtil.add(nextDay);
		// Button that creates events 
		JButton create = new JButton("Create Event");
		create.setBackground(Color.RED);
		create.setForeground(Color.WHITE);
		create.setBorderPainted(false);
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane createEvent = new JOptionPane();
				String name = createEvent.showInputDialog("Name of Event");
				String dateOf = createEvent.showInputDialog("Date of Event (MM/DD/YYYY)");
				String startTime = createEvent.showInputDialog("Starting Time (24 hour format)");
				String endTime = createEvent.showInputDialog("Ending Time (24 hour format)");
				if (!calendar.createEvent(name, dateOf, startTime, endTime)) 
					createEvent.showMessageDialog(null, "Error, there is a time conflict between an existing event and the new event. Please try again.");
				else
					createEvent.showMessageDialog(null, "Event has been created.");
				calendar.organize();
				updateDayDisplay(date, eventList);
			}
		});
		dayUtil.add(create);
		// end of utility for day view **************************************************************************
		dayPanel.add(dayUtil, BorderLayout.SOUTH);
		//********************************************************************************************************************************************************
		
		jf.getContentPane().add(monthPanel, BorderLayout.WEST);
		jf.getContentPane().add(dayPanel, BorderLayout.CENTER);
		jf.pack();
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
