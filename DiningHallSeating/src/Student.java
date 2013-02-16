

//David Gal, Andrew Poole, Julien Rhodes

public class Student {
	public int mytableNum;
	public int myLastTableOne;
	public int myLastTableTwo;
	public int myLastTableThree;
	public String myfName;
	public String mylName;
	public boolean myGender;
	public int myGrade;
	public String myAdvisor;
	public String myBirthDay;
	public String myID;
	public boolean myAssigned;
	
	////////////////////////////////////////CONSTRUCTORS/////////////////////////////////
	
	public Student(int tableNum,int lastTableOne, int lastTableTwo, int lastTableThree, String fName, String lName, boolean gender, int grade, String advisor, String ID, String birthday, boolean assign)
	{
		mytableNum = tableNum;
		myLastTableOne = lastTableOne;
		myLastTableTwo = lastTableTwo;
		myLastTableThree = lastTableThree;
		myfName = fName;
		mylName = lName;
		myGender = gender;
		myGrade = grade;
		myAdvisor = advisor;
		myID = ID;
		myBirthDay = birthday;
		myAssigned = assign;
	}
	
	public Student()
	{
		mytableNum = 0;
		myLastTableOne = 50;
		myLastTableTwo = 50;
		myLastTableThree = 50;
		myfName = "";
		mylName = "";
		myGender = true;
		myGrade = 9;
		myAdvisor = "";
		myID = "00000";
		myBirthDay = null;
		myAssigned = false;
	}
	
	public Student(Student oldStudent)
	{
		mytableNum = oldStudent.getTableNum();
		myLastTableOne = oldStudent.getLastTableOne();
		myLastTableTwo = oldStudent.getLastTableTwo();
		myLastTableThree = oldStudent.getLastTableThree();
		myfName = oldStudent.getfName();
		mylName = oldStudent.getlName();
		myGender = oldStudent.getGender();
		myGrade = oldStudent.getGrade();
		myAdvisor = oldStudent.getAdvisor();
		myID = oldStudent.getID();
		myBirthDay = oldStudent.getBirthDay();
		myAssigned = oldStudent.getAssigned();
	}
	
	////////////////////////////////////MODIFIERS////////////////////////////////////////
	
	void setTableNum(int tableNum)
	{
		mytableNum=tableNum;					//this is a vector index; NOT the 
	}											//    dining room numbering system
	
	void setLastTableOne(int lastTableOne)
	{
		myLastTableOne = lastTableOne;
	}
	
	void setLastTableTwo(int lastTableTwo)
	{
		myLastTableTwo = lastTableTwo;
	}
	
	void setLastTableThree(int lastTableThree)
	{
		myLastTableThree = lastTableThree;
	}
	
	void setName(String first, String last)
	{
		myfName=first;
		mylName=last;
	}
	
	void setGender(boolean gender)
	{
		myGender=gender;
	}
	
	void setGrade(int grade)
	{
		myGrade=grade;
	}

	void setAdvisor(String advisor)
	{
		myAdvisor=advisor;
	}
	
	void setID(String ID)
	{
		myID=ID;
	}
	
	void setBirthDay(String day)
	{
		myBirthDay=day;
	}
	
	void changeAssign(boolean assigned)
	{
		myAssigned=assigned;
	}
	
	/////////////////////////////////////ACCESSORS///////////////////////////////////////
	
	int getTableNum()
	{
		return mytableNum;		//this is a vector index; NOT the 
	}							//    dining room numbering system
	
	int getLastTableOne()
	{
		return myLastTableOne;
	}
	
	int getLastTableTwo()
	{
		return myLastTableTwo;
	}
	
	int getLastTableThree()
	{
		return myLastTableThree;
	}
	
	String getfName()
	{
		return myfName;
	}
	
	String getlName()
	{
		return mylName;
	}
	
	boolean getGender()
	{
		return myGender;
	}
	
	int getGrade()
	{
		return myGrade;
	}
	
	String getAdvisor()
	{
		return myAdvisor;
	}
	
	String getID()
	{
		return myID;
	}

	String getBirthDay()
	{
		return myBirthDay;
	}
	
	boolean getAssigned()
	{
		return myAssigned;
	}
}