import java.util.Arrays;


public class FitnessClass implements Comparable<FitnessClass> {

	private int numberofattendance;	//number of attendance in current week that are filled in
	int[] attendances;// an array for the attendances


	public int compareTo(FitnessClass other) //method to compare two numbers of attendances

	{
		int  thisnumberofattendance = this.getnumberofattendance();
		int  othernumberofattendance = other.getnumberofattendance();

		if (thisnumberofattendance < othernumberofattendance)

			return -1;
		else if (thisnumberofattendance == othernumberofattendance)
			return 0;
		else 
			return 1;

	}

	private int getnumberofattendance() //accessor method to get the number of attendance
	{

		return numberofattendance;
	}

	/*The variables for ID number, classname, name of the tutor and start time */
	private String IDnumber;
	private String classname;
	private String tname;
	private int sTime;

	public FitnessClass ()//the default constructor as asked from the exercise
	{

	}

	public FitnessClass(String ID, String name, String tutornames, int classtime)//the constructor that takes as parameters the ID the name of the class the name of the tutor and the time of the class
	{
		IDnumber = ID ;
		classname = name ;
		tname = tutornames ;
		sTime = classtime ;
	}

	public String getIDnumber()//accessor method for IDnumber
	{
		return IDnumber;
	}

	public String getclassname()//accessor method for the name of a class
	{
		return classname;
	}

	public String gettname()//accessor method for the tutor name
	{ 
		return tname; 
	}

	public int getsTime()//accessor method for the start time of a class
	{
		return sTime;
	}

	public void setclassname(String classname)//mutator method for the name of a class
	{ 
		this.classname = classname;
	}
	public void settname(String tname)//mutator method for the name of a tutor

	{
		this.tname = tname;
	}
	public void setsTime(int sTime)//mutator method for the start time of a Fitness class

	{
		this.sTime = sTime;
	}

	public void sortAttendances()//method to sort the attendances
	{
		Arrays.sort(attendances);

	}

	public  int[] getattendances()//accessor method for the array attendances
	{
		return attendances;
	}

	public void setattendances(int [] attendances)//mutator method for the array attendances
	{
		this.attendances = attendances;
	}


	public String getclassID()//accessor method to get the classID 
	{

		return "";
	}







}





