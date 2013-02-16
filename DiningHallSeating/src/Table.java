import java.util.*;

public class Table
{
	private Vector IDList = new Vector();
	private Vector genderHist = new Vector();
	private Vector gradesHist = new Vector();
	private Vector facIndexes = new Vector();
	
	private int tableNum=0;					//table number
	private String tableName = "";			//faculty at table
	private int tableCapacity = 0;			//total chairs at table
	private int seatsTakenByFac = 0;		//seats taken by Fac
	private int MAXCAPACITY;				//seats available to students
	private int floatStatus = 0;			//0 = nonFloater;  1 = Floater table
	
	//constructors *************************
	public Table()
	{
		
	}
	
	public Table(Vector IDs, int tnum)
	{
		tableNum=tnum;
		IDList = IDs;
		InitHistograms();
	}

	public Table(int tnum, int capacity)
	{
		tableNum=tnum;
		MAXCAPACITY=capacity;
		InitHistograms();
	}
	
	public Table(int tablenum, int capacity, String facName)
	{
		tableNum=tablenum;
		MAXCAPACITY=capacity;
		tableName = facName;
		InitHistograms();
	}
	
	public Table(int num, String facName, int capacity, int takenByFac, int flStatus)
	{
		tableNum = num;
		tableName = facName;
		tableCapacity = capacity;
		seatsTakenByFac = takenByFac;
		MAXCAPACITY = capacity-seatsTakenByFac;
		floatStatus = flStatus;
		InitHistograms();
	}
	
	public Table(Table oldTable)
	{
		tableNum = oldTable.getTableNum();
		tableName = oldTable.getName();
		floatStatus = oldTable.floatStatus;
		IDList = oldTable.getIDList();
		MAXCAPACITY = oldTable.getCapacity();
		InitHistograms();
		genderHist = oldTable.getGenderHist();
		gradesHist = oldTable.getGradesHist();
	}
	
	// modifiers ***********************************
	public void reset()
	{
		MAXCAPACITY = tableCapacity - seatsTakenByFac;
		IDList.clear();
		genderHist.clear();
		gradesHist.clear();
		InitHistograms();
	}
	
	private void descendSortIDs()
	{
		Object[] S = IDList.toArray();
		Arrays.sort(S);
		
		for(int i = 0; i < S.length; i++)
		{
			
			IDList.set(S.length - i - 1, (String) S[i] );	
		}
	
	}
	
	private Vector getGradesHist()
	{	
		return gradesHist;
	}
	
	private Vector getGenderHist()
	{	
		return genderHist;
	}
	
	private void InitHistograms()
	{	
		for(int i=0; i<4; i++)
		{
			genderHist.add( new Integer(0) );
			gradesHist.add( new Integer(0) );
		}	
	}
	
	public void addFac(int index)
	{
		facIndexes.add( new Integer(index) );	
	}
	
	public void removeFac(int index)
	{
		facIndexes.remove(index);	
	}
	
	public void addID(String ID)
	{
		if(IDList.size() < MAXCAPACITY)
		{
			IDList.add(ID);
			
			if( ID.charAt(1) == 'M' )
					genderHist.set(0,new Integer(( (Integer)genderHist.get(0) ).intValue()+1) );
			else
				genderHist.set(1,new Integer(( (Integer)genderHist.get(1) ).intValue()+1) );
			
			int gradeHistIndex = Integer.parseInt( ID.substring(0,1) );
			gradesHist.set(gradeHistIndex, new Integer(( (Integer)gradesHist.get(gradeHistIndex) ).intValue()+1) );
			
		}
		else
		{
			System.out.println("Table " + tableNum + " already full.");
		}
		descendSortIDs();
		
	}
	
	public boolean removeID(String ID)
	{
		boolean OK = false;
		OK = IDList.remove(ID);	//If false, then ID not found!
		IDList.remove(ID);
		System.out.print(IDList);
		if( OK )
		{
			int gradeHistIndex = Integer.parseInt( ID.substring(0,1) );
			gradesHist.set(gradeHistIndex, new Integer(( (Integer)gradesHist.get(gradeHistIndex) ).intValue()-1) );

			if( ID.charAt(1) == 'M' )
					genderHist.set(0,new Integer(( (Integer)genderHist.get(0) ).intValue()-1) );
				else
					genderHist.set(1,new Integer(( (Integer)genderHist.get(1) ).intValue()-1) );
		}
		return OK;
	}
	
	private void makeGenderHistogram()
	{
		for(int i=0; i<IDList.size(); i++)
		{
			String thisID = new String( (String)IDList.get(i) );
			if( thisID.charAt(1) == 'M' )
					genderHist.set(0,new Integer(( (Integer)genderHist.get(0) ).intValue()+1) );
				else
					genderHist.set(1,new Integer(( (Integer)genderHist.get(1) ).intValue()+1) );
		}			
	}
	
	private void makeGradesHistogram()
	{	
		for(int i=0; i<IDList.size(); i++)
		{
			String thisID = new String( (String)IDList.get(i) );
			int gradeHistIndex = Integer.parseInt( thisID.substring(0,1) );
			gradesHist.set(gradeHistIndex, new Integer(( (Integer)gradesHist.get(gradeHistIndex) ).intValue()+1) );
		}
	}
	
	/*public void setTableNumber(int n)
	{
		tableNum=n;
	}*/
	
	/*public void setTableCapacity(int capacity)
	{
		tableCapacity=capacity;
	}*/
	
	/*public void setTableName(String name)
	{
		tableName=name;
	}*/
	
	
	
	// accessors **************************************
	public boolean isFloater()
	{
		return floatStatus == 1;
	}
	public String getName()
	{
		return tableName;
	}
	
	public int getMales()
	{
		return ((Integer)genderHist.get(0)).intValue();
	}
	
	public int getFemales()
	{
		return ((Integer)genderHist.get(1)).intValue();
	}
	
	public int getNines()
	{
		return ((Integer)gradesHist.get(0)).intValue();
	}
	
	public int getTens()
	{
		return ((Integer)gradesHist.get(1)).intValue();
	}
	
	public int getElevens()
	{
		return ((Integer)gradesHist.get(2)).intValue();
	}
	
	public int getTwelves()
	{
		return ((Integer)gradesHist.get(3)).intValue();
	}
	
	public int getCapacity()			//Maximum table capacity
	{
		return MAXCAPACITY;
	}
	
	public int getNumEmptySeats()
	{
		return MAXCAPACITY-IDList.size();
	}
	
	public int getNumStudents()		//Number of students at a table
	{
		return IDList.size();
	}
	
	public void replaceTableList(Vector IDs)
	{
		IDList = IDs;
	}
	
	
	/*public int getNumFaculty()			//Number of faculty at a table
	{
		return facultyNum;
	}*/
	
	public int getTableNum()
	{
		return tableNum;
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	public boolean filled()
	{
		return (IDList.size() >= MAXCAPACITY);
	}
	
	public Vector getIDList()
	{
		return IDList;
	}
}