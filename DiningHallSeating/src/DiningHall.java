//Andrew Poole, David Gal
//Dining Hall Beta 9
//Approximate Starting Date: 12/1/01
//Finish Date: 

import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

public class DiningHall extends JFrame implements ActionListener {
	/********************************* Globals *******************************************/
	private static String savedAction = new String(); // stores last-clicked
														// table icon
	private static String fileName = new String(); // for file save and retrieve

	public static Random R = new Random(); // random# gen for shuffling students

	public static EasyReader con = new EasyReader(); // global console
	
	public static Assignment assigner = new Assignment();

	// /////////////////////////////////// Faculty, Students, Tables// ////////////////////////////
	private static Vector Faculty = new Vector(); // database of all faculty
	private static Vector Students = new Vector(); // database of all students
	private static Vector DiningRoom = new Vector(); // all tables

	private static Vector xVals = new Vector();
	private static Vector yVals = new Vector();

	private static Vector unAssignedStudents = new Vector(); // unassigned
																// people

	private static Vector StuIDs = new Vector();

	// //////////////////////////////////// GUI ///////////////////////////////////////
	JToolBar myTools = new JToolBar();
	JMenuBar myMenu = new JMenuBar();
	public static JTabbedPane setup = new JTabbedPane();
	public static Container contentPane = new Container();

	/////////////////////////////////// Output /////////////////////////////////////
	public static DefaultListModel tableRoster = new DefaultListModel(); // pane 1
	public static JList studentsAtTable = new JList(tableRoster);

	public static DefaultTableModel stuInfo = new DefaultTableModel(); // pane 2
	public static JTable studentTable = new JTable(stuInfo);

	public static DefaultTableModel tableInfo = new DefaultTableModel(); // pane 3
	public static JTable tableTable = new JTable(tableInfo);

	public static DefaultListModel gradeInfo = new DefaultListModel(); // pane 4
	public static JList gradeHistogram = new JList(gradeInfo);

	public static DefaultListModel genderInfo = new DefaultListModel(); // pane 5
	public static JList genderHistogram = new JList(genderInfo);

	// ***********************************************************************************************//
	// ////////////////////////////// INITIALIZATION AND GUI SET-UP	// //////////////////////////////////

	public DiningHall() {
		setSize(1000, 750);
		contentPane = getContentPane();

		try {

			UIManager GUI = new UIManager();
			//UIManager.setLookAndFeel(GUI.getCrossPlatformLookAndFeelClassName());

		} catch (Exception e) {
		}
		;

		initMenuBar(myMenu);
		initMyTools(myTools);

		readTableFile("Data/tables0910.txt");
		readStuFile("Data/Students0910.txt");
		readCoordFile("Data/coordinates.txt");
		setup = makePanes();
		contentPane.add(setup);

		initStuIDVec();
	}

	protected void initMyTools(JToolBar myToolBar) {
		//dont know what this should do...were they trying to incorporate image controls?
		//ask kmeyer if this would be worth implementing
		
		JButton buttonToAdd;

		buttonToAdd = new JButton("HELLO?!?!?", new ImageIcon("Data/saveIcon.gif"));

		myToolBar.add(buttonToAdd);
	}

	protected void initMenuBar(JMenuBar menuBar) {
		JMenu menu;
		JMenuItem menuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// /////////////////////////FILE MENU///////////////////////
		menu = new JMenu("File");
		menuBar.add(menu);

		menuItem = new JMenuItem("Export Seating Arrangement...");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Import Seating Arrangement...");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Quit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		/////////////////////////VIEW STUDENT INFO//////////////////////////////
		//this probably isn't the best place for this code...where should I put it?
		//it works but should probs be moved to a more appropriate location...
		
		studentsAtTable.addMouseListener(new MouseAdapter() { // view student info by clicking on  name
					public void mouseClicked(MouseEvent evt) {
						JList list = (JList) evt.getSource();
						if (evt.getClickCount() == 2) { //double click
							String selectedItem = (String) list
									.getSelectedValue();
							String first = selectedItem.substring(0,
									selectedItem.indexOf(" ")); // get first name
							String lastHalf = selectedItem
									.substring(selectedItem.indexOf(" ") + 1);
							String last = lastHalf.substring(0,
									lastHalf.indexOf(" ")); // get last name

							System.out.println(first + " " + last);
							viewAStudent(first, last); // display student's info in JOptionPane
						}
					}
				});

		/////////////////////////TOOLS MENU/////////////////////
		menu = new JMenu(" Seating Tools");
		menuBar.add(menu);

		menuItem = new JMenuItem("Seat Students at Random");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Seat Students by Advisor");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Seat Students by Birthday");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu = new JMenu(" Table Tools");
		menuBar.add(menu);

		menuItem = new JMenuItem("Edit a table");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Delete a Table ");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Add a Table ");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu = new JMenu(" Faculty Tools");
		menuBar.add(menu);

		/*
		menuItem = new JMenuItem("Edit a Teacher");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Delete a Teacher ");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Add a Teacher ");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		*/
		
		menuItem = new JMenuItem("Locate a Teacher");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu = new JMenu(" Student Tools");
		menuBar.add(menu);

		/*
		menuItem = new JMenuItem("Edit a Student");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Delete a Student ");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		
		menuItem = new JMenuItem("Add a Student ");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		*/
		menuItem = new JMenuItem("Add a student to a table...");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Remove a student from a table...");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Swap two Students...");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Locate a Student");
		menuItem.addActionListener(this);
		menu.add(menuItem);

	}

	protected JTabbedPane makePanes() {
		JTabbedPane mainPane = new JTabbedPane();
		// tab 1
		Component mapPanel = makeMapArea();
		mainPane.addTab("Dining Room Layout", null, mapPanel,
				"Use this window to arrange seating");

		// tab 2
		JPanel stuPanel = new JPanel(false);
		stuPanel.setLayout(new GridLayout(1, 1));

		updateStudentPane();
		JScrollPane stuInfoPane = new JScrollPane(studentTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		stuPanel.add(stuInfoPane);

		Component studentList = stuPanel;
		mainPane.addTab("List by Student", null, studentList,
				"Use this window to view the assignments by student");

		// tab 3
		JPanel tablePanel = new JPanel(false);
		tablePanel.setLayout(new GridLayout(1, 1));

		updateTablePane();
		JScrollPane tableInfoPane = new JScrollPane(tableTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tablePanel.add(tableInfoPane);

		Component tableList = tablePanel;
		mainPane.addTab("List by Table", null, tableList,
				"Use this tab to view the assignments by table.");

		// tab 4
		JPanel analysisPanel = new JPanel(false);
		analysisPanel.setLayout(new GridLayout(1, 2));

		updateGenderPane();
		JScrollPane genderInfoPane = new JScrollPane(gradeHistogram,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		analysisPanel.add(genderInfoPane);

		updateGradePane();
		JScrollPane gradeInfoPane = new JScrollPane(gradeHistogram,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		analysisPanel.add(gradeInfoPane);

		mainPane.addTab("Seating Statistics", null, analysisPanel,
				"Use this tab to see summary statistics.");

		return mainPane;
	}

	protected JSplitPane makeMapArea() {
		JScrollPane studentListSection = new JScrollPane(studentsAtTable,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane mapSection = new JScrollPane(makeMap(),
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JSplitPane mapArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				mapSection, studentListSection);
		mapArea.setDividerLocation(570);
		return mapArea;
	}

	protected JPanel makeMap() {
		ImageIcon mapImage = new ImageIcon("Data/dhall.gif");
		JLabel map = new JLabel(mapImage);

		JPanel myMap = new JPanel();
		myMap.setLayout(null);

		Insets insets = myMap.getInsets();
		map.setBounds(insets.left, insets.top, 640, 726);

		myMap.add(map);
		JButton unAssigned = new JButton("unassigned");
		unAssigned.setBorderPainted(false);
		unAssigned.addActionListener(this);
		myMap.add(unAssigned);
		unAssigned.setBounds(377 + insets.left, 496 + insets.top, 50, 50);
		unAssigned.setOpaque(false);
		unAssigned.setContentAreaFilled(false);
		unAssigned.setBorderPainted(false);

		for (int i = 0; i < 28; i++) {
			String buttonName = new String(new Integer(i + 1).toString());

			JButton table = new JButton(buttonName);
			table.setBorderPainted(false);
			table.addActionListener(this);
			myMap.add(table);
			table.setBounds(((Integer) xVals.get(i)).intValue() + insets.left,
					((Integer) yVals.get(i)).intValue() + insets.top, 50, 50);
			table.setOpaque(false);
			table.setContentAreaFilled(false);
			table.setBorderPainted(false);
		}
		return myMap;
	}

	// ***********************************************************************************************
	///////////////////////////////////////EVENT HANDLING/////////////////////////////////////////////


	public void actionPerformed(ActionEvent e) {
		String action = new String(e.getActionCommand());

		// CLICK ON A TABLE ICON
		if (action.length() == 1 || action.length() == 2) {
			savedAction = action;
			showTableInfo(action);
		} else if (action.equals("unassigned")) // Unassigned Table Clicked
		{
			showUnAssigned();
		}
		// DROPDOWN MENU ACTIONS
		else if (action.equals("Quit")) {
			updateOnExit(Students);
			System.exit(0);
		} else if (action.equals("Add a student to a table...")) {
			addStudentToTable();
			updateAll();
		} else if (action.equals("Swap two Students...")) {
			SwapStudents();
			updateAll();
		} else if (action.equals("Remove a student from a table...")) {
			removeStudentFromTable();
			updateAll();
		} else if (action.equals("Export Seating Arrangement...")) {
			fileName = JOptionPane
					.showInputDialog("Give the filename to use (no path): ");
			JOptionPane.showMessageDialog(null,
					"Saving seating arrangment to    Lists/" + fileName);
			writeTableAssignmentByTable("Lists/" + fileName);
			writeTableAssignmentByStudent("Lists/" + fileName);
		} else if (action.equals("Import Seating Arrangement...")) {
			fileName = JOptionPane
					.showInputDialog("this feature not activated: ");
			
		} else if (action.equals("Seat Students at Random")) {
			updateTables();
			assignHall();
			while(!allGood()) //if students are not seated according to criteria
				assignHall(); 
			updateAll(); //only display the seated students when they are seated correctly
			updateOnExit(Students);
		} else if (action.equals("Seat Students by Birthday")){
			assignByBirthday();
			updateAll();
		} else if (action.equals("Seat Students by Advisor")) {
			//assignByAdvisor();
			updateAll();
		}
		
		else if (action.equals("Locate a Teacher")) { 
			String lName = JOptionPane.showInputDialog ("Please enter the faculty member's last name."); 
			String gend =  JOptionPane.showInputDialog("And their gender (m/f).");
			viewAFacMember(lName, gend);
		}
		
		//I added this to help myself track students and their table paths without having to click on every table to find them
		else if (action.equals("Locate a Student")) { 
			String first = JOptionPane.showInputDialog("Please enter the student's first name: ");
			String last = JOptionPane.showInputDialog("Please enter the student's last name: ");
			viewAStudent(first, last);
		}
	}

	// ***********************************************************************************************
	/////////////////////////////////////////FILE READING///////////////////////////////////////

	
	
	//this method now fills the Faculty vector with appropriate Teacher objects
	private void readTableFile(String facFileName){
		EasyReader inFile = new EasyReader(facFileName);
		
		//whitespace regex splitter
		Pattern splitter = Pattern.compile("\\s+");
		if (inFile.bad()) 
			JOptionPane.showMessageDialog(null, facFileName + "not found");
		String first = inFile.readLine(); //get rid of column headings
		String str;
		int count = 1;
		while ((str = inFile.readLine()) != null){
			System.out.println(str);
			
			//returns array of elements split by the regex
			String parts[] = splitter.split(str);
			
			String aName = new String();
			int seats = 0;
			int seatsTaken;
			int flStatus;
			
			if(parts.length > 0){
				
				System.out.println(count);
				
				///if the table has two teachers and isn't a floater table
				if(count != 3 && count != 7 && count != 8 && count != 9 && count != 15){
				boolean genderOne = false;
				boolean genderTwo = false;
				aName = parts[0] + " " + parts [1] + " " + parts[2] + " " + parts[3];
				seats = Integer.parseInt(parts[4]);
				seatsTaken = Integer.parseInt(parts[5]);
				flStatus = Integer.parseInt(parts[6]);
				Table addTable = new Table(count, aName, seats, seatsTaken,
						flStatus);
				DiningRoom.addElement(addTable);
				
				if(parts[0].equals("Mr."))
					genderOne = true;
				else if(parts[0].equals("Ms.") || parts[0].equals("Mrs."))
					genderOne = false;
				Teacher teachOne = new Teacher(genderOne, parts[1], seats, count);
				Faculty.addElement(teachOne);
				//System.out.println(teachOne.getName());
				
				if(parts[2].equals("Mr."))
					genderTwo = true;
				else if(parts[2].equals("Ms.") || parts[0].equals("Mrs."))
					genderTwo = false;
				Teacher teachTwo = new Teacher(genderTwo, parts[3], seats, count);
				Faculty.addElement(teachTwo);
				//System.out.println(teachTwo.getName());
				count++;
				}
				
				//if the table only has one teacher
				else if(count == 3 || count == 7){
					boolean gender = false;
					aName = parts[0] + " " + parts [1];
					seats = Integer.parseInt(parts[2]);
					seatsTaken = Integer.parseInt(parts[3]);
					flStatus = Integer.parseInt(parts[4]);
					Table addTable = new Table(count, aName, seats, seatsTaken,
							flStatus);
					DiningRoom.addElement(addTable);
					
					if(parts[0].equals("Mr."))
						gender = true;
					else if(parts[0].equals("Ms.") || parts[0].equals("Mrs."))
						gender = false;
					Teacher teach = new Teacher(gender, parts[1], seats, count);
					Faculty.addElement(teach);
					//System.out.println(teach.getName());
					count++;
				}
				
				//three teachers at 15
				else if(count == 15){
					boolean genderOne = false;
					boolean genderTwo = false;
					boolean genderThree = false;
					aName = parts[0] + " " + parts [1] + " " + parts[2] + " " + parts[3] + " " + parts[4] + " " + parts[5];
					seats = Integer.parseInt(parts[6]);
					seatsTaken = Integer.parseInt(parts[7]);
					flStatus = Integer.parseInt(parts[8]);
					Table addTable = new Table(count, aName, seats, seatsTaken,
							flStatus);
					DiningRoom.addElement(addTable);
					
					if(parts[0].equals("Mr."))
						genderOne = true;
					else if(parts[0].equals("Ms.") || parts[0].equals("Mrs."))
						genderOne = false;
					Teacher teachOne = new Teacher(genderOne, parts[1], seats, count);
					Faculty.addElement(teachOne);
					
					if(parts[2].equals("Mr."))
						genderTwo = true;
					else if(parts[2].equals("Ms.") || parts[0].equals("Mrs."))
						genderTwo = false;
					Teacher teachTwo = new Teacher(genderTwo, parts[3], seats, count);
					Faculty.addElement(teachTwo);
					
					if(parts[4].equals("Mr."))
						genderTwo = true;
					else if(parts[4].equals("Ms.") || parts[0].equals("Mrs."))
						genderTwo = false;
					Teacher teachThree = new Teacher(genderTwo, parts[3], seats, count);
					Faculty.addElement(teachTwo);
					count++;
					
				}
				
				//floater tables (no teachers)
				else if(count == 8 || count == 9){
					aName = parts[0] + " " + parts [1] + " " + parts[3] + " " + parts[4];
					seats = Integer.parseInt(parts[4]);
					seatsTaken = Integer.parseInt(parts[5]);
					flStatus = Integer.parseInt(parts[6]);
					Table addTable = new Table(count, aName, seats, seatsTaken,
							flStatus);
					DiningRoom.addElement(addTable);
					
					count++;
				}
					
				
			}
		}
	}	

	private static void readStuFile(String stuFileName) {
		try {
			EasyReader input = new EasyReader(stuFileName);
			if (input.bad()) {
				System.err.println("Can't open " + stuFileName);
				System.exit(1);
			}

			Vector lineOfWords = new Vector();
			String tmpWord = "";

			input.readLine(); // Gets rid of column headings!

			int whichStudent = 0; // String counter used for IDs
			int counter = 0;
			int prevClass = 9;

			while (counter <= Students.size()) {
				System.out.print(counter + " ");
				lineOfWords.clear();

				while (lineOfWords.size() < 17) {
					tmpWord = input.readWord();
					lineOfWords.add(tmpWord);
				}

				// /////////////////// DAY STUDENT? ////////////////////
				if (((String) lineOfWords.get(0)).charAt(0) == '*')
					; //do nothing 
				else {

					//////////////////////MAKE GENDER////////////////////////

					String tempGender = lineOfWords.get(7).toString();
					boolean gender = false;
					if (tempGender.equals("1")) {
						gender = true;
					} // male
						// System.out.println( "male" );

					/////////////////////MAKE BIRTHDAY///////////////////////

					String Birthday = new String();
					Birthday = makeBirthday(lineOfWords);
					// System.out.println(Birthday);

					////////////////////MAKE CLASS///////////////////////////

					String classString = new String((String) lineOfWords.get(6));
					Integer classNum = new Integer(classString);

					// if we hit a new class, we're now on the first student
					if (classNum.intValue() != prevClass) 
					{
						whichStudent = 0;
						prevClass = classNum.intValue();
					}
					//System.out.println(classString + " ");

					///////////////////////INSTANTIATE STUDENT////////////////////


					Integer thisStudent = new Integer(whichStudent);
					String thisStuNum = new String(thisStudent.toString()); // make a number (which student are we on?)																

					String IDNum = new String(); // then make ID number based on that info
					// JOptionPane.showMessageDialog(null,classNum.intValue() +
					// " " + gender + " " + thisStuNum);
					IDNum = makeIDNum(classNum.intValue(), gender, thisStuNum);

					// table lastTable firstName lastName gender class advisor
					// student ID BDay assigned
					Student toadd = new Student(30,
							Integer.parseInt(lineOfWords.get(14).toString()),
							Integer.parseInt(lineOfWords.get(15).toString()),
							Integer.parseInt(lineOfWords.get(16).toString()),
							lineOfWords.get(1).toString(), lineOfWords.get(0)
									.toString(), gender, classNum.intValue(),
							lineOfWords.get(3).toString(), IDNum, Birthday,
							false); //30 is the "unassigned" table (doesn't factor into lastTable value)
					whichStudent++;

					Students.addElement(toadd);
					unAssignedStudents.addElement(IDNum);
					prevClass = toadd.getGrade();
					counter++;
				}

				input.readLine();

			}
		} catch (java.lang.NullPointerException e) {
			System.out.println(e.getMessage());
		} 
	}

	private static void readCoordFile(String coordFileName) {
		EasyReader inFile = new EasyReader(coordFileName);
		if (inFile.bad()) {
			JOptionPane.showMessageDialog(null, coordFileName + "not found");
		}

		else {
			String firstline = inFile.readLine(); // toss the column headings
			while (!inFile.eof()) {
				Integer xCoord = new Integer(inFile.readInt());
				xVals.add(xCoord);
				Integer yCoord = new Integer(inFile.readInt());
				yVals.add(yCoord);
			}
		}
		inFile.readLine();
	}

	private static void initStuIDVec() {
		StuIDs.clear();
		unAssignedStudents.clear();
		for (int i = 0; i < Students.size(); i++) {
			Student thisStu = new Student((Student) Students.get(i));
			StuIDs.add(thisStu.getID());
			unAssignedStudents.add(thisStu.getID());
		}
		System.out.println("initStuIDVec");
		for (int i = 0; i < unAssignedStudents.size(); i++)
			System.out.print(unAssignedStudents.get(i) + " ");
	}

	// ************************************************************************************************
	//////////////////////////////////////////HELPER FUNCTIONS////////////////////////////////////////

	/* work on if have time
	private static String convertDormName(String dormInits){
		String dormName= "";
		if(dormInits.equals("MS"))
			dormName = Middle
	}
	*/
	
	private static String makeBirthday(Vector thisLine) {
		// insert a 0 if necessary for MONTH and DAY (YEAR is always 4 digits)
		String BDay = new String();

		if (thisLine.get(8).toString().length() == 2) // BIRTH(MONTH)
		{
			BDay += thisLine.get(8).toString();
			BDay += "/";
		}

		else {
			BDay += "0"; // add a '0' b/c month is 1 digit
			BDay += thisLine.get(8).toString();
			BDay += "/";
		}

		if (thisLine.get(9).toString().length() == 2) // BIRTH(DAY)
		{
			BDay += thisLine.get(9).toString();
			BDay += "/";
		}

		else {
			BDay += "0"; // add a zero b/c day is 1 digit
			BDay += thisLine.get(9).toString();
			BDay += "/";
		}

		BDay += thisLine.get(10);

		return BDay;
	}

	private static String makeIDNum(int whatGrade, boolean whatGender,
			String whichStudent) {
		// ///////////////GRADE/////////////////
		String grade = String.valueOf(whatGrade - 9); // Convert int to String
		// if(grade.length() < 2) { grade = "0" + grade; }

		// /////////////GENDER//////////////////
		String gender = new String();
		if (whatGender) {
			gender = "M";
		} else {
			gender = "F";
		}

		// /////////////NUMBER//////////////////
		String stuNum = new String(whichStudent);
		while (stuNum.length() < 3)
			stuNum = "0" + stuNum;

		// /////////////FINAL ID///////////////
		String finalID = new String();
		finalID = grade + gender + stuNum;

		return finalID;
	}

	private static String convertGender(boolean a) {
		if (a)
			return "Male";
		return "Female";
	}

	public static int getIndexOfStu(String IDnum) {
		// String ID = new String(IDnum);

		int counter = 0;

		Student thisStudent = new Student();

		while ((counter < Students.size())) {
			thisStudent = (Student) Students.get(counter);
			if (thisStudent.getID().equals(IDnum))
				return counter;
			counter++;
		}
		return -9;
	}

	public static int findIndexInVec(Vector thisVec, String ID) {
		for (int i = 0; i < thisVec.size(); i++) {
			if (((String) thisVec.get(i)).equals(ID))
				return i;
		}
		return -9;
	}


	public static int getIndexOfFac(String lName, boolean gender) { 
		Teacher thisTeach = new Teacher(); 
		for(int i = 0; i < Faculty.size(); i++) {
			thisTeach = (Teacher)Faculty.get(i);
			if(thisTeach.getName().equals(lName) && thisTeach.getGender() == gender){
				System.out.println("index of faculty :" + i);
				return i;
			}
				
		} 
		return -99; 
	}
	 

	private static int findFirstEmptyTable(int tNum) {
		int i = (tNum + 1) % 27;
		while (i != tNum) {
			if (!((Table) DiningRoom.elementAt(i)).filled())
				return i;
			else
				i = (i + 1) % 27;
		}
		return -9;
	}


	private static void displayStudents() // tester function
	{
		for (int i = 0; i < Students.size(); i++) {
			Student thisStu = new Student((Student) Students.get(i));
			System.out
					.println(thisStu.getfName() + " " + thisStu.getTableNum());
		}
	}

	private static void displayTables() // tester function
	{
		for (int i = 0; i < DiningRoom.size(); i++) {
			Table thisTable = new Table((Table) DiningRoom.get(i));
			Vector thisTab = thisTable.getIDList();
			System.out.println("Table has " + thisTab.size() + " people.");
			for (int j = 0; j < thisTab.size(); j++) {
				System.out.println(thisTab.get(j));
			}
		}
	}

	// ************************************************************************************************
	/////////////////////////////////////////DISPLAY FUNCTIONS/////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////

	private static void viewAStudent(String first, String last) {
		String allInfo = new String();
		if (studentExists(first, last) >= 0) {
			Student tempStudent = new Student(
					(Student) Students.get(studentExists(first, last)));
			allInfo = ("Name: " + tempStudent.getfName() + " " + tempStudent
					.getlName());
			allInfo += ("\n" + "ID Number: " + tempStudent.getID());
			allInfo += ("\n" + "Gender: " + convertGender(tempStudent
					.getGender()));
			allInfo += ("\n" + "Grade: " + tempStudent.getGrade());
			allInfo += ("\n" + "Advisor: " + tempStudent.getAdvisor());
			allInfo += ("\n" + "Birthday: " + tempStudent.getBirthDay());
			
			if(tempStudent.getTableNum() == 30)
				allInfo += ("\n" + "Current Table Number: Unassigned");
			else
				allInfo += ("\n" + "Current Table Number: " + tempStudent
					.getTableNum());
			
			allInfo += ("\n" + "Last Three Tables: " + tempStudent
					.getLastTableOne() + "->" + tempStudent.getLastTableTwo() + "->" + tempStudent.getLastTableThree());
		}
		JOptionPane.showMessageDialog(null, allInfo);
	}

	
	  private static void viewAFacMember(String lName, String gender) { 
		  boolean gend = false;
	
		  if(gender.equals("m")) 
			  gend = true; 
		  else if(gender.equals("f")) 
			  gend = false; 
	  
		  int index = getIndexOfFac(lName, gend); 
		  String allInfo = new String();
	  
		  if(index >= 0) { 
			  Teacher t = (Teacher)Faculty.get(index);
			  System.out.println(t.getName());
			  allInfo = "Name: " + t.getName();
			  allInfo += ("\n" + "Gender: " + convertGender(t.getGender()));
			  allInfo += ("\n" + "Seats Taken: " + t.getSeats());
			  allInfo += ("\n" + "Table Number: " + t.getTableNum());
		  }
		  	  JOptionPane.showMessageDialog(null, allInfo); 
	}

	private static void showTableInfo(String whichTable){ //pane 1
		tableRoster.clear();
		Integer thisTable = new Integer(whichTable);
		int tableIndex = thisTable.intValue() - 1;

		tableRoster.addElement("[" + (tableIndex + 1) + "] "
				+ ((Table) DiningRoom.get(tableIndex)).getTableName());

		tableRoster.addElement(((Table) DiningRoom.get(tableIndex)).getMales()
				+ "  " + ((Table) DiningRoom.get(tableIndex)).getFemales()
				+ "      " + ((Table) DiningRoom.get(tableIndex)).getNines()
				+ "  " + ((Table) DiningRoom.get(tableIndex)).getTens() + "  "
				+ ((Table) DiningRoom.get(tableIndex)).getElevens() + "  "
				+ ((Table) DiningRoom.get(tableIndex)).getTwelves());

		tableRoster.addElement("----------------------------");
		tableRoster.addElement("");

		for (int i = 0; i < ((Table) DiningRoom.get(tableIndex)).getIDList()
				.size(); i++) {
			int index = getIndexOfStu((String) ((Table) DiningRoom
					.get(tableIndex)).getIDList().get(i));
			Student thisStudent = new Student((Student) Students.get(index));
			tableRoster.addElement(thisStudent.getfName() + " "
					+ thisStudent.getlName() + " (" + thisStudent.getGrade()
					+ ")");
		}

		studentsAtTable = new JList(tableRoster);
	}

	private static void updateGenderPane() // pane 4
	{
		genderInfo.clear();
		genderInfo.addElement(" ");
		genderInfo.addElement(" ");

		genderInfo.addElement("                  Males");
		genderInfo
				.addElement("_______________________________________________");
		for (int i = 0; i < 10; i++) {
			String lineOut = new String();
			lineOut += " " + new Integer(i).toString() + ".| ";
			int count = 0;
			for (int tableIndex = 0; tableIndex < DiningRoom.size(); tableIndex++) {
				if (((Table) DiningRoom.get(tableIndex)).getMales() == i) {

					if (tableIndex < 9)
						lineOut += " " + new Integer(tableIndex + 1).toString()
								+ "   ";
					else
						lineOut += new Integer(tableIndex + 1).toString()
								+ "  ";

					count++;
				}
			}
			genderInfo.addElement(lineOut);
		}

		genderInfo.addElement(" ");
		genderInfo.addElement(" ");

		genderInfo.addElement("                  Females");
		genderInfo
				.addElement("_______________________________________________");
		for (int i = 0; i < 10; i++) {
			String lineOut = new String();
			lineOut += " " + new Integer(i).toString() + ".| ";
			int count = 0;
			for (int tableIndex = 0; tableIndex < DiningRoom.size(); tableIndex++) {
				if (((Table) DiningRoom.get(tableIndex)).getFemales() == i) {

					if (tableIndex < 9)
						lineOut += " " + new Integer(tableIndex + 1).toString()
								+ "   ";
					else
						lineOut += new Integer(tableIndex + 1).toString()
								+ "  ";

					count++;
				}
			}
			genderInfo.addElement(lineOut);
		}

		gradeHistogram = new JList(genderInfo);
	}

	private static void updateGradePane() // pane 4
	{
		gradeInfo.clear();

		gradeInfo.addElement("                  Sophomores");
		gradeInfo.addElement("_______________________________________________");
		for (int i = 0; i < 8; i++) {
			String lineOut = new String();
			lineOut += " " + new Integer(i).toString() + ".| ";
			int count = 0;
			for (int tableIndex = 0; tableIndex < DiningRoom.size(); tableIndex++) {
				if (((Table) DiningRoom.get(tableIndex)).getTens() == i) {

					if (tableIndex < 9)
						lineOut += " " + new Integer(tableIndex + 1).toString()
								+ "   ";
					else
						lineOut += new Integer(tableIndex + 1).toString()
								+ "  ";

					count++;
				}
			}
			gradeInfo.addElement(lineOut);
		}

		gradeInfo.addElement(" ");
		gradeInfo.addElement(" ");

		gradeInfo.addElement("                  Juniors");
		gradeInfo.addElement("_______________________________________________");
		for (int i = 0; i < 8; i++) {
			String lineOut = new String();
			lineOut += " " + new Integer(i).toString() + ".| ";
			int count = 0;
			for (int tableIndex = 0; tableIndex < DiningRoom.size(); tableIndex++) {
				if (((Table) DiningRoom.get(tableIndex)).getElevens() == i) {

					if (tableIndex < 9)
						lineOut += " " + new Integer(tableIndex + 1).toString()
								+ "   ";
					else
						lineOut += new Integer(tableIndex + 1).toString()
								+ "  ";

					count++;
				}
			}
			gradeInfo.addElement(lineOut);
		}

		gradeInfo.addElement(" ");
		gradeInfo.addElement(" ");

		gradeInfo.addElement("                  Seniors");
		gradeInfo.addElement("_______________________________________________");
		for (int i = 0; i < 8; i++) {
			String lineOut = new String();
			lineOut += " " + new Integer(i).toString() + ".| ";
			int count = 0;
			for (int tableIndex = 0; tableIndex < DiningRoom.size(); tableIndex++) {
				if (((Table) DiningRoom.get(tableIndex)).getTwelves() == i) {

					if (tableIndex < 9)
						lineOut += " " + new Integer(tableIndex + 1).toString()
								+ "   ";
					else
						lineOut += new Integer(tableIndex + 1).toString()
								+ "  ";

					count++;
				}
			}
			gradeInfo.addElement(lineOut);
		}

		gradeHistogram = new JList(gradeInfo);
	}

	private static void showUnAssigned() {
		tableRoster.clear();
		tableRoster.addElement("Unassigned Students");
		tableRoster.addElement("----------------------------");
		tableRoster.addElement("");

		for (int i = 0; i < unAssignedStudents.size(); i++) {
			String name = ((Student) Students
					.elementAt(getIndexOfStu((String) unAssignedStudents.get(i))))
					.getfName();
			name += " "
					+ ((Student) Students
							.elementAt(getIndexOfStu((String) unAssignedStudents
									.get(i)))).getlName();
			name += " ("
					+ ((Student) Students
							.elementAt(getIndexOfStu((String) unAssignedStudents
									.get(i)))).getGrade();
			name += ")";
			tableRoster.addElement(name);
		}
		studentsAtTable = new JList(tableRoster);
	}

	private static void updateStudentPane() // pane 2
	{
		Vector labels = new Vector();
		labels.add(" ");
		labels.add("Name");
		labels.add("Grade");
		labels.add("Table Number");

		Vector lines = new Vector();

		Student tempStudent = new Student();

		for (int i = 0; i < Students.size(); i++) {
			Vector line = new Vector();
			tempStudent = (Student) Students.get(i);
			line.add(new Integer(i));
			line.add(tempStudent.getfName() + " " + tempStudent.getlName());
			Integer thisGrade = new Integer(tempStudent.getGrade());
			line.add(thisGrade);
			Integer thisTable = new Integer(tempStudent.getTableNum());
			line.add(thisTable);

			lines.add(line);
		}

		stuInfo.setDataVector(lines, labels);
		studentTable = new JTable(stuInfo);
	}

	private static void updateTablePane() // pane 3
	{
		Vector labels = new Vector();
		labels.add("Table");
		labels.add("Name");

		Vector lines = new Vector();

		for (int k = 0; k < DiningRoom.size(); k++) // for each table
		{
			Vector stu = new Vector(((Table) DiningRoom.get(k)).getIDList());

			int thisStuIndex = 0;
			Student thisStu = new Student();

			for (int j = 0; j < stu.size(); j++) {
				Vector line = new Vector();
				String thisStudentID = new String((String) stu.get(j));
				thisStuIndex = getIndexOfStu(thisStudentID);
				thisStu = (Student) Students.get(thisStuIndex);

				line.add(new Integer(k + 1));
				line.add(thisStu.getfName() + " " + thisStu.getlName());
				lines.add(line);
			}
		}

		tableInfo.setDataVector(lines, labels);
		tableTable = new JTable(tableInfo);
	}

	private static void updateAll() {
		updateStudentPane();
		updateTablePane();
		updateGenderPane();
		updateGradePane();
	}

	// *********************************************************************************************************
	/////////////////////////////////////// TEXT FILE OUTPUT ///////////////////////////////////////////////////

	private void updateArchiveFile(Vector stuList){
		File file = new File(
				"/Users/Shelley/Documents/DiningHallSeating/DHS/DiningHallSeating/Data/"
						+ "Students0910.txt");
		
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
			
			//regular expression (it matches one or more whitespace characters)
			Pattern splitter = Pattern.compile("\\s+");
		DataInputStream in = new DataInputStream(fis);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String str;
		StringBuilder fileContent = new StringBuilder();
		int count = 0;

		
		while ((str = br.readLine()) != null) {
			System.out.println(str);
			
			//returns an array of the line split into elements by whitespace
			String parts[] = splitter.split(str);
			
			if (parts.length > 0) {
					Student stu = (Student) stuList.get(count);
					
					int max = parts.length;

					String newLine = new String();
					for(int i = 0; i < max; i++){
						newLine += parts[i] + " ";
					newLine += stu.getTableNum();	
					System.out.println(newLine);
					fileContent.append(newLine);
					fileContent.append("\n");
					count++;
				}

			}

		}
		FileWriter writer = new FileWriter(
				"/Users/Shelley/Documents/DiningHallSeating/DHS/DiningHallSeating/Data/"
						+ "Students0910.txt");
		BufferedWriter bwrite = new BufferedWriter(writer);
		bwrite.write(fileContent.toString());
		bwrite.close();
		in.close();
	} catch (Exception e) {// Catch exception if any
		System.err.println("Error: " + e.getMessage());
		e.printStackTrace();
	}
}
	
	private void updateOnExit(Vector stuList) {
		//for some reason FileInputStream won't recognize a file unless the explicit file path is included
		//is there a way to remedy this? EasyReader doesn't require this extra info
		File file = new File(
				"/Users/Shelley/Documents/DiningHallSeating/DHS/DiningHallSeating/Data"
						+ "Students0910.txt");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			
			//regular expression (it matches one or more whitespace characters)
			Pattern splitter = Pattern.compile("\\s+");

			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str;
			StringBuilder fileContent = new StringBuilder();			
			String first = br.readLine(); //takes column headings out of the loop but still adds them to the edited file
			fileContent.append(first);
			fileContent.append("\n");
			int count = 0;

			while ((str = br.readLine()) != null) {
				System.out.println(str);
				
				//returns an array of the line split into elements by whitespace
				String parts[] = splitter.split(str);
				
				if (parts.length > 0) {
					if (parts[0].charAt(0) == '*') { // do not change lastTable value for day students
						String newLine = parts[0] + " " + parts[1] + " "
								+ parts[2] + " " + parts[3] + " " + parts[4]
								+ " " + parts[5] + " " + parts[6] + " "
								+ parts[7] + " " + parts[8] + " " + parts[9]
								+ " " + parts[10] + " " + parts[11] + " "
								+ parts[12] + " " + parts[13] + " " + parts[14]
								+ " " + parts[15] + " " + parts[16];
						System.out.println(newLine);
						fileContent.append(newLine);
						fileContent.append("\n");
					} else {
						Student stu = (Student) stuList.get(count);

						//replace old table values with new ones
						parts[14] = String.valueOf(stu.getTableNum());
						parts[15] = String.valueOf(stu.getLastTableOne());
						parts[16] = String.valueOf(stu.getLastTableTwo());
						
						System.out.println("This student's last table was: "
								+ parts[14]);
						String newLine = parts[0] + " " + parts[1] + " "
								+ parts[2] + " " + parts[3] + " " + parts[4]
								+ " " + parts[5] + " " + parts[6] + " "
								+ parts[7] + " " + parts[8] + " " + parts[9]
								+ " " + parts[10] + " " + parts[11] + " "
								+ parts[12] + " " + parts[13] + " " + parts[14]
								+ " " + parts[15] + " " + parts[16];
						System.out.println(newLine);
						fileContent.append(newLine);
						fileContent.append("\n");
						count++; //the count is only incremented for boarding students
					}

				}

			}
			FileWriter writer = new FileWriter(
					"/Users/Shelley/Documents/DiningHallSeating/DHS/DiningHallSeating/Data"
							+ "Students0910.txt");
			BufferedWriter bwrite = new BufferedWriter(writer);
			bwrite.write(fileContent.toString());
			bwrite.close();
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void writeTableAssignmentByTable(String fileName) {
		EasyWriter tableByTable = new EasyWriter(fileName + "_byTable.txt"); // "table_assignment_by_table.txt");
		if (tableByTable.bad()) {
			System.err.println("Error creating " + fileName + "_byTable.txt\n");
		}

		for (int i = 0; i < DiningRoom.size(); i++) {
			Table thisTable = new Table((Table) DiningRoom.get(i));

			// if ( i > 6 )
			// tableByTable.print("[" + (i+3) + "] " );
			// else
			tableByTable.print("[" + (i + 1) + "] ");

			tableByTable.print(((Table) DiningRoom.get(i)).getTableName()
					+ "\n");
			tableByTable.print("------------------------------\n");
			tableByTable.print("\n");

			for (int j = 0; j < ((Table) DiningRoom.get(i)).getIDList().size(); j++) {
				Student thisKid = (Student) Students
						.get(getIndexOfStu((String) thisTable.getIDList()
								.get(j)));
				tableByTable.print(thisKid.getfName() + " "
						+ thisKid.getlName() + "\n");
			}
			tableByTable.print("\n\n\n\n");
		}
		tableByTable.close();
	}

	private static void writeTableAssignmentByStudent(String fileName) {
		int thisGrade, prevGrade;
		EasyWriter tableByStudent = new EasyWriter(fileName + "_byStudent.txt"); // "table_assignment_by_student.txt");
		if (tableByStudent.bad()) {
			System.err
					.println("Error creating table_assignment_by_student.txt\n");
		}

		tableByStudent.print("Formal Dinner Seating by Student \n");
		tableByStudent.print("--------------------------------- \n\n\n");

		prevGrade = 9;
		for (int i = 0; i < StuIDs.size(); i++) {
			Student thisStu = (Student) Students
					.get(getIndexOfStu((String) StuIDs.get(i)));
			thisGrade = thisStu.getGrade();
			if (thisGrade != prevGrade) {
				tableByStudent.print("\n");
				tableByStudent.print("\n");
				tableByStudent.print("\n");
			}

			tableByStudent.print(thisStu.getfName() + " " + thisStu.getlName()
					+ " - ");
			// if ( thisStu.getTableNum() > 6 )
			// tableByStudent.print( (thisStu.getTableNum()+2) + "\n");
			// else
			tableByStudent.print(thisStu.getTableNum() + "\n");

			prevGrade = thisGrade;
		}
		tableByStudent.close();
	}

	// ************************************************************************************************
	// ////////////////////////////////////////MOVING STUDENTS AROUND///////////////////////////////////////////
	private static int studentExists(String fName, String lName) {
		Student tempStudent = new Student();

		for (int i = 0; i < Students.size(); i++) {
			tempStudent = (Student) Students.get(i);
			if ((tempStudent.getfName().equals(fName))
					&& (tempStudent.getlName().equals(lName)))
				return i;
		}
		return -9999;
	}

	private static void addStudentToTable() {
		String lName = new String();
		String fName = new String();

		lName = JOptionPane
				.showInputDialog("Please enter the last name of the student to add.");
		fName = JOptionPane.showInputDialog("Now enter his/her first name.");

		int indexOfStu = studentExists(lName, fName);

		if (indexOfStu != -9999) // if student exists
		{
			Integer oldTable = new Integer(
					((Student) Students.get(indexOfStu)).getTableNum());
			Integer newtable = new Integer(
					JOptionPane
							.showInputDialog("At which table would you like to place "
									+ fName + "?"));

			if (!(((Table) DiningRoom.get(newtable.intValue() - 1)).filled())) {
				((Student) Students.get(indexOfStu)).setTableNum(newtable
						.intValue());

				if (oldTable.intValue() == 27) {
					unAssignedStudents.remove(/*
											 * findIndexInVec(unAssignedStudents,
											 */((Student) Students
							.get(indexOfStu)).getID());
				} else {
					((Table) DiningRoom.get(oldTable.intValue() - 1))
							.removeID(((Student) Students.get(indexOfStu))
									.getID());
				}

				((Table) DiningRoom.get(newtable.intValue() - 1))
						.addID(((Student) Students.get(indexOfStu)).getID());

				JOptionPane.showMessageDialog(null, fName
						+ " has been moved from " + oldTable + " to table "
						+ newtable + ".");
			} else
				JOptionPane.showMessageDialog(null, "Sorry.  Table is full.");
		} else
			JOptionPane.showMessageDialog(null,
					"Sorry.  Student not found.  Please re-enter a name.");
	}

	private static void removeStudentFromTable() {
		String fName = new String();
		String lName = new String();

		lName = JOptionPane
				.showInputDialog("Please enter the last name of the student to remove.");
		fName = JOptionPane.showInputDialog("Now enter his/her first name.");

		int indexOfStudent = studentExists(lName, fName);
		System.out.println(indexOfStudent);
		if (indexOfStudent >= 0) {
			Student thisStu = new Student(
					(Student) Students.get(indexOfStudent));
			String hisID = new String(thisStu.getID());

			// int indexOfTable = findWhereStudentSits(lName, fName);
			int indexOfTable = thisStu.getTableNum();
			System.out.println(indexOfTable);
			if (indexOfTable >= 0) // if table exists
			{
				((Table) DiningRoom.get(indexOfTable - 1)).removeID(hisID);
				for (int i = 0; i < ((Table) DiningRoom.get(indexOfTable - 1))
						.getIDList().size(); i++) {
					System.out.println(((Table) DiningRoom
							.get(indexOfTable - 1)).getIDList().elementAt(i));
				}
				JOptionPane.showMessageDialog(null, fName + " " + lName
						+ " has been removed from table " + indexOfTable);
				((Student) Students.get(getIndexOfStu(hisID))).setTableNum(27);
				unAssignedStudents.addElement(hisID);
				// update table
			}

		} else
			JOptionPane.showMessageDialog(null, "Sorry.  Student not found.");
	}

	private static void SwapStudents() {
		int indexOfStu1 = -9;
		int indexOfStu2 = -9;

		String lName = new String();
		String fName = new String();

		boolean okStu1 = false;
		boolean okStu2 = false;
		boolean okToSwap = false;

		int studentOn = 1;

		while (!(okToSwap)) {
			fName = JOptionPane
					.showInputDialog("Please enter enter the student's first name.");
			lName = JOptionPane.showInputDialog("Please enter the #"
					+ studentOn + " student's last name.");
			
			int indexOfStu = studentExists(fName, lName);

			if (indexOfStu != -9999) // if student exists
			{
				JOptionPane.showMessageDialog(null, "Student #" + studentOn
						+ " was found at index " + indexOfStu + ".");
				if (studentOn == 1) // if we're on the first stu,
				{
					indexOfStu1 = indexOfStu; // student 1 exists.
					okStu1 = true;
					studentOn = 2;
				}

				else if (studentOn == 2) // if we're on the second stu,
				{
					indexOfStu2 = indexOfStu; // student 2 exists
					okStu2 = true;
					studentOn = 0;
				}
			}

			else
				JOptionPane.showMessageDialog(null,
						"Sorry.  Student not found.  Please re-enter a name.");

			if (okStu1 && okStu2) // if both students exist,
				okToSwap = true; // OK to swap
		}
		Swap(indexOfStu1, indexOfStu2);
	}

	private static void Swap(int stu1Index, int stu2Index) {
		// get data
		Student stu1 = (Student) Students.get(stu1Index);
		Student stu2 = (Student) Students.get(stu2Index);

		int origTable1 = stu1.getTableNum();
		System.out.println(stu1.getfName() + ": " + (stu1.getTableNum()));
		int origTable2 = stu2.getTableNum();
		System.out.println(stu2.getfName() + ": " + (stu2.getTableNum()));

		String origID1 = stu1.getID();
		String origID2 = stu2.getID();


		
		// modify tables
		
		//refill table 1
		((Table) DiningRoom.get(origTable1 - 1)).removeID(origID1);
		Vector origTable1IDs = ((Table) DiningRoom.get(origTable1 - 1)).getIDList();
		origTable1IDs.add(origID2);
		((Table) DiningRoom.get(origTable1 - 1)).replaceTableList(origTable1IDs);
		
		//refill table 2
		((Table) DiningRoom.get(origTable2 - 1)).removeID(origID2);
		Vector origTable2IDs = ((Table) DiningRoom.get(origTable2 - 1)).getIDList();
		origTable2IDs.add(origID1);
		System.out.print(origTable2IDs);
		((Table) DiningRoom.get(origTable2 - 1)).replaceTableList(origTable2IDs);

		// modify students
		stu1.setTableNum(origTable2);
		System.out.println(stu1.getfName() + ": " + (stu1.getTableNum()));
		stu2.setTableNum(origTable1);
		System.out.println(stu2.getfName() + ": " + (stu2.getTableNum()));
		
		JOptionPane.showMessageDialog(null, stu1.getfName() + " " + stu1.getlName()
				+ " has been swapped to table " + (stu1.getTableNum()) + ".");
		JOptionPane.showMessageDialog(null,  stu2.getfName() + " " + stu2.getlName()
				+ " has been swapped to table " + (stu2.getTableNum()) + ".");

		// update table screen
		//updateAll();
		//if (!savedAction.equals(""))
			showTableInfo(savedAction); // most recent table click saved here
	}

	// *************************************************************************************************
		// /////////////////////////////////////////BIRTHDAY ASSIGNMENT///////////////////////////////////////
	
	
	public static Vector vectorByBirthday(Vector thisVector){
		return thisVector;
		//List list = new Vector();
		
		
		
	}
	
	public static void assignByBirthday(){
		Vector birthVec = vectorByBirthday(Students); 
		
		int count = 0;
		Table t = (Table) DiningRoom.get(count);
		while(!t.filled() && birthVec.size() > 0){
			String stuID = ((Student) (birthVec.get(0))).getID(); 
			t.addID(stuID);
			birthVec.remove(0);
		}
		count++;
	}
	
	// *************************************************************************************************
	// /////////////////////////////////////////RANDOM ASSIGNMENT///////////////////////////////////////

	public static Vector shuffle(Vector thisVector) {

		java.util.List list;

		list = Arrays.asList(thisVector.toArray());

		Collections.shuffle(list);

		Vector newVector = new Vector(list);

		return newVector;
	}

	private static int computeFloaters() {
		int totalSeatsAvailable = 0;

		for (int i = 0; i < (DiningRoom.size() - 1); i++) {
			totalSeatsAvailable += ((Table) DiningRoom.get(i)).getCapacity();
		}

		return Students.size() - totalSeatsAvailable;
	}

	public static void assignFloaters(int floaterNum) {
		int tableCounter = 0;
		int stuIndex;

		for (int i = 0; i < floaterNum; i++) {
			stuIndex = R.nextInt(StuIDs.size() - 1);
			assignStudent(25, (String) StuIDs.get(stuIndex));
			StuIDs.remove(stuIndex);

			if (tableCounter >= 25) // if we're at the last table, reset
				tableCounter = 0; // 25 tables + 1 floater w/ infinite seats
			else
				tableCounter++; // mod??
		}
	}

	//before assignStudent() can be run the tables must be updated to their most recent values
	private static void updateTables(){ 
		for(int k= 0;k < Students.size(); k++){
			Student stu = (Student) Students.get(k);
			
			//since 30 is the unassigned table values are not changed until student has been assigned
			if(stu.getTableNum() != 30){
				stu.setLastTableThree(stu.getLastTableTwo());
				stu.setLastTableTwo(stu.getLastTableOne());
				stu.setLastTableOne(stu.getTableNum());
			}
		}
	}
	
	public static void assignStudent(int thisTable, String thisID) {
		// find an empty table if necessary
		Table T = new Table((Table) DiningRoom.get(thisTable));
		char Gr = thisID.charAt(0);
		int indexOfThisStu = getIndexOfStu(thisID);
		Student stu = (Student) Students.get(indexOfThisStu);

		if (T.filled()) {
			thisTable = findFirstEmptyTable(thisTable);
			T = new Table((Table) DiningRoom.get(thisTable));
		}

		if (stu.getLastTableOne() != (thisTable + 1) && stu.getLastTableTwo() != (thisTable + 1) && stu.getLastTableThree() != (thisTable + 1) && !T.filled()) { //no table reassignment or assignment to full table
			System.out.println("This kid's last three tables: " + stu.getLastTableOne() + ":" + stu.getLastTableTwo() + ":" + stu.getLastTableThree() + " are not the same as this table: " + (thisTable + 1));
			System.out.println(stu.getfName() + " " + stu.getlName() + " is safe to go here!");
			// modify the student's record
			((Student) Students.get(indexOfThisStu)).setTableNum(thisTable + 1);
			((Student) Students.get(indexOfThisStu)).changeAssign(true);

			// modify the table
			if (thisTable >= 0) {
				((Table) DiningRoom.get(thisTable)).addID(thisID);
				System.out.println(((Student) Students.get(indexOfThisStu)) .getfName() + " added to Table " + (thisTable + 1));

			}
		} else { //increment table # until requirements are met
			while (stu.getLastTableOne() == (thisTable + 1) || stu.getLastTableTwo() == (thisTable + 1) || stu.getLastTableThree() == (thisTable + 1) || T.filled()) {
				thisTable = (thisTable + 1) % 27;
				T = new Table((Table) DiningRoom.get(thisTable));
			}
			if (stu.getLastTableOne() != (thisTable + 1) & stu.getLastTableTwo() != (thisTable + 1) && stu.getLastTableThree() != (thisTable + 1) && !T.filled()) {
				System.out.println("This kid's last three tables: " + stu.getLastTableOne() + ":" + stu.getLastTableTwo() + ":" + stu.getLastTableThree() + " are not the same as this table: " + (thisTable + 1));
				System.out.println(stu.getfName() + " " + stu.getlName() + " is safe to go here!");
				// modify the student's record
				((Student) Students.get(indexOfThisStu)) .setTableNum(thisTable + 1);
				((Student) Students.get(indexOfThisStu)).changeAssign(true);

				// modify the table
				if (thisTable >= 0) {
					((Table) DiningRoom.get(thisTable)).addID(thisID);
					System.out.println(((Student) Students.get(indexOfThisStu)) .getfName() + " added to Table " + (thisTable + 1));

				}
			}
		}
	}
	


	private static void See(Vector V) {
		java.util.List list;

		list = Arrays.asList(V.toArray());

		int tenth = list.size() / 10 - 1;
		System.out.println(tenth);
		for (int i = 0; i < tenth; i++)
			System.out.println(list.get(i).toString() + " "
					+ list.get(i + tenth).toString() + " "
					+ list.get(i + 2 * tenth).toString() + " "
					+ list.get(i + 3 * tenth).toString() + " "
					+ list.get(i + 4 * tenth).toString() + " "
					+ list.get(i + 5 * tenth).toString() + " "
					+ list.get(i + 6 * tenth).toString() + " "
					+ list.get(i + 7 * tenth).toString() + " "
					+ list.get(i + 8 * tenth).toString() + " "
					+ list.get(i + 9 * tenth).toString());
		System.out.println();
		System.out.println();
	}

	private static void Sort(Vector V) {
		Object[] S = V.toArray();
		Arrays.sort(S);

		int tenth = S.length / 10 - 1;
		for (int i = 0; i < tenth; i++)
			System.out.println(S[i].toString() + " " + S[i + tenth].toString()
					+ " " + S[i + 2 * tenth].toString() + " "
					+ S[i + 3 * tenth].toString() + " "
					+ S[i + 4 * tenth].toString() + " "
					+ S[i + 5 * tenth].toString() + " "
					+ S[i + 6 * tenth].toString() + " "
					+ S[i + 7 * tenth].toString() + " "
					+ S[i + 8 * tenth].toString() + " "
					+ S[i + 9 * tenth].toString());
		System.out.println();
		System.out.println();
	}

	public static Vector AssignAllFrosh(Vector unAssigned, Integer tIndex) {
		Vector froshOnly = new Vector();
		System.out.println("unAssigned size " + unAssigned.size());
		for (int i = 0; i < unAssigned.size(); i++)
			System.out.print(unAssigned.get(i) + " ");

		int i = 0;
		while (i < unAssigned.size()) {
			if (((String) unAssigned.get(i)).charAt(0) == '0') {
				froshOnly.add(unAssigned.get(i));
				unAssigned.remove(i);
			} else
				i++;
		}

		System.out.println();
		for (i = 0; i < froshOnly.size(); i++)
			System.out.print(froshOnly.get(i) + " ");
		int tableIndex = tIndex.intValue();
		froshOnly = shuffle(froshOnly);

		String thisFroshID = new String();

		System.out.println("Frosh size " + froshOnly.size());

		while (froshOnly.size() > 0) {
			int index = 1;
			thisFroshID = (String) froshOnly.get(0);
			if (!(tableIndex == 7 || tableIndex == 8)) // frosh can't be at
														// floater tables
			{
				assignStudent(tableIndex, thisFroshID);
				froshOnly.remove(0);

			}
			tableIndex = (tableIndex + 1) % 27;
		}

		System.out.println("frosh " + tableIndex);
		tIndex = new Integer(tableIndex); // returned by reference to assignHall

		return unAssigned;
	}

	public static Vector AssignOneClass(char cNum, Vector unAssigned,
			Integer tIndex) {
		Vector a27Class = new Vector();
		System.out.println("Assigning 27 of: " + cNum);
		int i = 0;
		int count = 0;
		while (count < 27 && i < unAssigned.size()) {
			System.out.println("i " + i + "\tcount: " + count);
			if (((String) unAssigned.get(i)).charAt(0) == cNum) {
				System.out.print((String) unAssigned.get(i) + " ");
				a27Class.add(unAssigned.get(i));
				unAssigned.remove(i);
				count++;
			}
			i++;
		}

		int tableIndex = tIndex.intValue();
		a27Class = shuffle(a27Class);

		String thisStuID = new String();

		while (a27Class.size() > 0) {
			int index = 1;
			thisStuID = (String) a27Class.get(0);
			assignStudent(tableIndex, thisStuID);
			a27Class.remove(0);
			tableIndex = (tableIndex + 1) % 27;
		}
		System.out.println("a27Class " + tableIndex);
		tIndex = new Integer(tableIndex); // returned by reference to assignHall

		return unAssigned;
	}

	public static void assignHall() {
		initStuIDVec();

		for (int i = 0; i < DiningRoom.size(); i++) {
			Table thisTable = new Table((Table) DiningRoom.get(i));
			thisTable.reset();
		}

		Integer startTableNum = new Integer(R.nextInt(27)); // pick a random
															// starting table

		unAssignedStudents = AssignAllFrosh(unAssignedStudents, startTableNum);
		unAssignedStudents = AssignOneClass('1', unAssignedStudents, startTableNum); // assign upperclassmen
		unAssignedStudents = AssignOneClass('2', unAssignedStudents, startTableNum); // one per table
		unAssignedStudents = AssignOneClass('3', unAssignedStudents, startTableNum);

		unAssignedStudents = shuffle(unAssignedStudents);

		String thisStuID = new String();
		int tableIndex = startTableNum.intValue();
		System.out.println("AssignHall" + tableIndex);

		while (unAssignedStudents.size() > 0) { // Just use the first index since the vector is shuffled
			thisStuID = (String) unAssignedStudents.get(0);
			assignStudent(tableIndex, thisStuID);
			unAssignedStudents.remove(0);
			tableIndex = (tableIndex + 1) % 27;
		}

		// update table screen
		if (!savedAction.equals(""))
			showTableInfo(savedAction); // most recent table click saved here

		System.out.println("done!");
	}

	private static boolean allGood() { //returns true if each table has met the requirements
		int count = 0;
		for(int i = 0;i < DiningRoom.size(); i++){
			Table t = (Table)DiningRoom.elementAt(i);
			if(i != 7 && i != 8){ //checks tables w/ frosh
				System.out.println(t.getMales() + "-" + t.getFemales() + "-" + t.getNines() + "-" + t.getTens() + "-" + t.getElevens() + "-" + t.getTwelves());
				if( t.getMales() > 0 && t.getFemales() > 0 && t.getNines() > 0 && t.getTens() > 0 && t.getElevens() > 0 && t.getTwelves() > 0)
					count++;
			}
			else{ //no frosh at floater tables (8 & 9)
				if(t.getMales() > 0 && t.getFemales() > 0 && t.getNines() == 0 && t.getTens() > 0 && t.getElevens() > 0 && t.getTwelves() > 0)
					count++;
			}
		}
		return count == DiningRoom.size(); 
	}

}
