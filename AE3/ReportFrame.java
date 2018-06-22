import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.util.Collections;


/*
 * Class to define window in which attendance report is displayed.
 */
public class ReportFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String attendancesFile = "AttendancesIn.txt";
	public JTextArea AttendancestextArea;
	private static final int FRAME_WIDTH = 700;//width of the window Attendances Report
	private static final int FRAME_HEIGHT = 250;//height of the Attendances Report
	private JLabel Idlabel, Classlabel, Tutorlabel, Attendanceslabel, AverageAttendancelabel,DoubleLine;//i used Jlabel for the first line
	private String ClassID,classnames,tutornames;//the variables
	FitnessClass fclass;// the fclass object
	public String firstAttendance,secondAttendance,thirdAttendance,fourAttendance,fiveAttendance;
	public String AttendanceLine;
	private int [] TakeAvgsFromLines;
	private double overallAvg;
	private int count;

	public ReportFrame()//this is the default constructor where i have put some information for the window Attendances Report
	{
		setSize(FRAME_WIDTH, FRAME_HEIGHT);//this is the size of the window Attendances Report
		setTitle ("Attendance Report");//the title
		AttendancestextArea = new JTextArea(5,9);

	}

	public void ReadAndShowAttendancies()//this method reads from the file AttendancesIn. 
										//makes reordering of the classes and tutors to be
	{									//presented properly
										//and processing average attendances and overall average
		AttendanceLine="";
		DecimalFormat df = new DecimalFormat("#.00"); 
		try {
			FileReader reader = new FileReader(attendancesFile);

			ArrayList<String> AddedClasses = SportsCentreGUI.getAddedClasses();
			ArrayList<String> AddedTutors = SportsCentreGUI.getAddedTutors();
			if (AddedClasses.size()==5 && SportsCentreGUI.ClassDeleted()==false && count==0){
				Collections.swap(AddedClasses, 3, 0);//the swap of the classes must happen only once
				Collections.swap(AddedClasses, 2, 1);//otherwise, classes that shouldnt change
				Collections.swap(AddedTutors, 3, 0);//are swapped. it works for 5,6,7 classes.
				Collections.swap(AddedTutors, 2, 1);
				count++;
			}

			else if (AddedClasses.size()==5 && SportsCentreGUI.ClassDeleted()==true && SportsCentreGUI.DeleteCount()==2 && count==0){
				Collections.swap(AddedClasses, 1, 0);//when classes are five and 
				Collections.swap(AddedTutors, 1, 0);//two classes have been deleted
				count++;
			}
			else if (AddedClasses.size()==5 && SportsCentreGUI.ClassDeleted()==true && SportsCentreGUI.DeleteCount()==1 && count==0){
				Collections.swap(AddedClasses, 3, 0);//when classes are five and 
				Collections.swap(AddedClasses, 2, 1);//one class have been deleted
				Collections.swap(AddedTutors, 3, 0);
				Collections.swap(AddedTutors, 2, 1);
				count++;
			}
			else if (AddedClasses.size()==6 && SportsCentreGUI.ClassDeleted()==false && count==0){
				Collections.swap(AddedClasses, 3, 0);//when the user adds one class and then presses
				Collections.swap(AddedClasses, 2, 1);//the view attendances button for the first time
				Collections.swap(AddedTutors, 3, 0);//the classes must swap
				Collections.swap(AddedTutors, 2, 1);
				count++;
			}
			else if (AddedClasses.size()==7 && SportsCentreGUI.ClassDeleted()==false && count==0){
				Collections.swap(AddedClasses, 3, 0);//when the user adds two classes and then presses
				Collections.swap(AddedClasses, 2, 1);//the view attendances button for the first time
				Collections.swap(AddedTutors, 3, 0);//the classes must swap
				Collections.swap(AddedTutors, 2, 1);
				count++;
			}
			TakeAvgsFromLines  =new int [AddedClasses.size()];
			Scanner in = new Scanner(reader);
			while (in.hasNextLine()) {

				for (int i=0; i<AddedClasses.size(); i++ ){
					
					String line = in.nextLine();
					String [] token = line.split(" ");
					ClassID = token[0];
					classnames = AddedClasses.get(i);
					tutornames = AddedTutors.get(i);
					firstAttendance=token[1];
					secondAttendance=token[2];
					thirdAttendance=token[3];
					fourAttendance=token[4];
					fiveAttendance=token[5];	

					TakeAvgsFromLines[i] =Integer.parseInt(firstAttendance)+Integer.parseInt(secondAttendance)
							+Integer.parseInt(thirdAttendance)+Integer.parseInt(fourAttendance)+Integer.parseInt(fiveAttendance);

					AttendanceLine+=(ClassID+"\t"+classnames+"\t"+tutornames+"\t"+
							String.format("%6s",firstAttendance)+String.format("%6s",secondAttendance)+String.format("%6s",thirdAttendance)+
							String.format("%6s",fourAttendance)+String.format("%6s",fiveAttendance)+"\t"+String.format("%1$,.2f",(double)TakeAvgsFromLines[i]/5)+"\n");
					
				}
				int ClassNum2= AddedClasses.size();
				Double Avg=0.0;
				for (int i=0; i<TakeAvgsFromLines.length; i++){
					Avg += (double)TakeAvgsFromLines[i]/5;
				}
				overallAvg= (double) Avg/ ClassNum2;
			}

			AttendancestextArea.setText(AttendanceLine+"\n" +"\t\t\t\t\tOverall Average: "+df.format(overallAvg));

			reader.close();
		}
		catch (IOException e) {
			System.out.println("File not found");
		}
	}

	public  JTextArea addTextArea () //method to build the report for display on the JTextArea
	{	   
		return AttendancestextArea;
	}

	public void northPanel()//this is the method to build the window Attendances Report
	{					//Jlabels and formatting is here. 

		ReportFrame frame = new ReportFrame();//this is to display the new window when the View Attendances button is pressed
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		frame.setVisible(true);

		Idlabel = new JLabel("ID"+String.format("%23s",""));

		Classlabel = new JLabel("Class"+String.format("%18s",""));
		Tutorlabel = new JLabel("Tutor"+String.format("%22s",""));
		Attendanceslabel = new JLabel("Attendances"+String.format("%25s",""));
		AverageAttendancelabel = new JLabel("Average Attendance");
		DoubleLine = new JLabel ("<html> <body>==============================================================================</body> </html>");
		Container content = frame.getContentPane();
		content.setLayout(new FlowLayout(FlowLayout.LEFT));

		setLayout(new BoxLayout(content,  BoxLayout.X_AXIS));

		content.add(Idlabel);
		content.add(Classlabel);
		content.add(Tutorlabel);
		content.add(Attendanceslabel);
		content.add(AverageAttendancelabel);
		content.add(DoubleLine);
		content.add(AttendancestextArea);

	}  

}	


