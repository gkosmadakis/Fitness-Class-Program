import java.util.*;



public class FitnessProgram {
	private final int MAXCLASSES = 7;//instance variable for the maximum number of classes
	private int numAttendances;//instance variable for the number of attendances
	private FitnessClass [] fclasses;//an array for the classes
	private FitnessClass [] attendances;//an array for the attendances


	FitnessClass [] arraylist = new FitnessClass[7];//instantiate the array



	public FitnessProgram()//the default constructor
	{	
	}

	public FitnessClass insertFitnessClass(String classIDnumber)//method to insert FitnessClass object to or from a list. it searches the list on Fitness Class ID.
	{
		FitnessClass fclass = null;

		if(numAttendances==MAXCLASSES)
			System.out.println("Schedule full. It's not possible to insert class ");
		else if(findClass(classIDnumber)!=null)
			System.out.println("Class ID"+classIDnumber+"already exists!");
		else
		{

			fclass = new FitnessClass(classIDnumber, classIDnumber, classIDnumber, MAXCLASSES);
			fclasses[numAttendances] = fclass;
			numAttendances++;
		}
		return fclass;
	}


	public void  InsertFitnessClass( FitnessClass fclass)//method to get the start time and add the fclass with the array fclasses.
	{   int sTime=0;
	sTime = fclass.getsTime();
	sTime = sTime-9;
	if (fclasses!=null)
	{
		fclasses[sTime]=fclass;
	}
	}

	public FitnessClass findClass(String classIDnumber)////method to return a Fitness Class object with a given ID number in the array
	{
		for (int i=0; i< numAttendances; i++){
			if (attendances[i]!=null)
			{	FitnessClass fclass = attendances[i];
			if (fclass.getclassID().equals(classIDnumber))
				return fclass;}
		}
		return null;
	}

	public FitnessClass deleteFitnessClass(String classID)//method to delete fitness class object to or from a list
	{
		boolean found = false;
		int index = 0;
		while (index<numAttendances && !found)
		{
			FitnessClass fclass = attendances[index];
			if(fclass.getclassID()==classID)
			{
				attendances[index] = attendances[numAttendances-1];
				numAttendances--;
				found= true;				
			}
			else
				index++;
		}
		if(!found)
		{
			//System.out.println("No class with number"+classID+"exists");
		}
		return null;
	}


	public int getnumAttendances()// method to return numattendances
	{
		return numAttendances;
	}

	public int getMAXCLASSES()// method to return MAXCLASSES
	{
		return MAXCLASSES;

	}

	public FitnessClass[] getfclasses()//method to return fclasses
	{
		return fclasses;
	}

	public FitnessClass [] getattendances()//method to return attendances
	{
		return attendances;
	}

	public Object getfClass()//method to return the Fitness Class object at index X
	{
		return getfClass();
	}

	public FitnessClass getclassTime()//method to return the Fitness Class starting at a particular time.
	{
		if (numAttendances==0) return null;
		FitnessClass  classTime = attendances[0];
		for (int i = 1; i< numAttendances; i++)
		{
			FitnessClass fClass = attendances[i];
			if (fClass.getsTime()> classTime.getsTime())
			{
				classTime = fClass;
			}
		}
		return classTime;
	}



	public FitnessClass [] sortAttendance()// method to return a list sorted in non-increasing order on the average attendance
	{
		Arrays.sort(attendances, 0 , numAttendances);
		return attendances;
	}


}




