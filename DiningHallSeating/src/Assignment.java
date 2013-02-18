import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;


public class Assignment {
	
	
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
