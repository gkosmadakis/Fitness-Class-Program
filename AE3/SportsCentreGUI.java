import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.util.*;
import java.io.*;
import javax.swing.JFrame;
import java.awt.Color;

/*
 * Defines a GUI that displays details of a FitnessProgram object
 * and contains buttons enabling access to the required functionality.
 */
public class SportsCentreGUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* GUI JButtons */
	private JButton closeButton, attendanceButton, GuideButton;
	private JButton addButton, deleteButton;
	FitnessClass fclass;
	/*GUI JTextFields */
	private JTextField idIn, classIn, tutorIn;

	/* Display of class timetable */
	private JTextArea display;

	/* Display of attendance information */
	ReportFrame report = new ReportFrame();
	private String ClassID,DisplayClasses,DisplayTutors,displayline,displayline2,Classtimes,DisplayAddedTutors,DisplayAddedClasses;
	private  String GetID;

	/* Names of input text files */
	private final String classesInFile = "ClassesIn.txt";
	private final String classesOutFile = "ClassesOut.txt";
	File AttendancesInCopy = new File ("AttendancesInCopy.txt");
	File AttendancesIn = new File("AttendancesIn.txt");
	private String classnames,tutornames,NewIDfound;
	String classID="";
	String classTutorName="";
	String classtime;
	public int []Times;
	private int [] TimeofClasses;
	private static String [] Tutors;
	private String [] NameoftheTutors;
	private String [] AlltheIDs;
	private String [] NameOfClass,NameofTutors;
	private static String [] Classes;
	private  String [] ClassesInOrder,IDsInOrder;
	private int count,buttoncounter,lines;
	LineNumberReader AttendancesInlines;
	private static ArrayList<String> AddedClasses,AddedTutors;
	private boolean addbuttonisPressed=false;
	public static boolean ClassDeleted;
	public static int DeleteCount;
	/*
	 * Constructor for AssEx3GUI class
	 */
	public SportsCentreGUI() 
	{

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Boyd-Orr Sports Centre");
		setSize(900, 380);
		display = new JTextArea();
		ReadAttendancesIn();
		ReadAndDisplayTimeTable();
		display.setFont(new Font("Courier", Font.PLAIN, 14));

		add( display, BorderLayout.CENTER);
		layoutTop();
		layoutBottom();
		IDsInOrder = new String [7];
		IDsInOrder= new String [] {"jy1","lb1","mp1","","","mp2","ls1"};
		ClassesInOrder= new String[7];
		System.arraycopy(NameOfClass, 0, ClassesInOrder, 0, NameOfClass.length );

	}

	public static void ReordertheClasses (String[] NameOfClass, int pos) 
	{
		String yoga = NameOfClass[NameOfClass.length-2];
		String badminton = NameOfClass[NameOfClass.length-1];
		String step = NameOfClass[NameOfClass.length-6];
		String pilates = NameOfClass[NameOfClass.length-7];
		String Available = NameOfClass[NameOfClass.length-4];

		// Move yoga to index [0]
		System.arraycopy(NameOfClass, pos-2, NameOfClass, pos-2 + 1, NameOfClass.length - pos);
		// Move badminton to index [1]
		System.arraycopy(NameOfClass, pos-1, NameOfClass, pos, NameOfClass.length - pos);
		// Move pilates to index [5]
		System.arraycopy(NameOfClass, pos+2, NameOfClass, pos+3, NameOfClass.length - 6);
		// Move step to index [6]
		System.arraycopy(NameOfClass, pos+1, NameOfClass, pos+4, NameOfClass.length -6);
		//Move Available to [3],[4]
		System.arraycopy(NameOfClass, pos+4, NameOfClass, pos+1, NameOfClass.length -6);
		NameOfClass[pos-2] = yoga;
		NameOfClass[pos-1]= badminton;
		NameOfClass[pos+3]=pilates;
		NameOfClass[pos+4]=step;
		NameOfClass[pos+1]=Available;
		NameOfClass[pos+2]=Available;

	}

	public static void ReordertheTutors (String [] NameofTutors, int pos)
	{
		String jane = NameofTutors[NameofTutors.length-4];
		String liz = NameofTutors[NameofTutors.length-3];
		String Available = NameofTutors[NameofTutors.length-2];
		String mandy = NameofTutors[NameofTutors.length-7];
		String linda =NameofTutors[NameofTutors.length-6];

		//Move jane to index [0]
		System.arraycopy(NameofTutors, pos-2, NameofTutors, pos-2 + 1, NameofTutors.length - 6);
		// Move liz to index [1]
		System.arraycopy(NameofTutors, pos-1, NameofTutors, pos, NameofTutors.length - 6);
		//Move Available to [3],[4]
		System.arraycopy(NameofTutors, pos+4, NameofTutors, pos+1, NameofTutors.length -6);
		//Move mandy to index [5]
		System.arraycopy(NameofTutors, pos-2, NameofTutors, pos+3, NameofTutors.length -6);
		//Move linda to index [6]
		System.arraycopy(NameofTutors, pos-1, NameofTutors, pos+4, NameofTutors.length -6);
		NameofTutors[pos-2]= jane;
		NameofTutors[pos-1]=liz;
		NameofTutors[pos+1]=Available;
		NameofTutors[pos+2]=Available;
		NameofTutors[pos+3]=mandy;
		NameofTutors[pos+4]=linda;
	}
	/*
	 * Creates the FitnessProgram list ordered by start time
	 * using data from the file ClassesIn.txt
	 */

	public void ReadAttendancesIn ()
	{
		File inputFile = new File("AttendancesIn.txt");
		lines =0;

		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));

			while((reader.readLine()!=null)) 
			{
				lines++;
			}
			reader.close(); 
		}	catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public void ReadAndDisplayTimeTable()//this method reads from the file ClassesIn.Process the info and display them in a static manner.
	{
		displayline="";//this is the to display the second line with the names of the classes
		displayline2="";//this is to display the third line with the names of the tutors
		Times = new int []  { 9,10,11,12,13,14,15};
		NameoftheTutors = new String[] {"mandy","linda","mandy","jane","liz","",""};
		TimeofClasses = new int [5];

		Classes = new String [lines];

		Tutors = new String [lines];
		AlltheIDs = new String[7];
		try 
		{
			FileReader reader = new FileReader(classesInFile);
			Scanner in = new Scanner(reader);

			while (in.hasNextLine()) 
			{ 
				for (int i=0; i<TimeofClasses.length; i++)
				{
					String line = in.nextLine();
					String [] token = line.split(" ");
					ClassID = token[0];
					classnames = token[1];
					tutornames = token[2];
					classtime = token[3];	
					int startTime = Integer.parseInt(classtime);				
					fclass = new FitnessClass (ClassID, classnames, tutornames, startTime);
					TimeofClasses[i]=Integer.parseInt(classtime);
					Classes [i]= classnames;
					Tutors[i]= tutornames;
					AlltheIDs[i]= ClassID;
				}
			} 
			reader.close();

		}  catch (IOException e) 
		{
			System.out.println("File not found");
		}

		Arrays.sort(TimeofClasses);
		int NewTimeofClasses []= new int [7];
		System.arraycopy(TimeofClasses, 0, NewTimeofClasses, 0, 5);
		NameOfClass = new String [7];
		NameofTutors = new String[7];
		System.arraycopy(NameoftheTutors, 0, NameofTutors, 0, NameoftheTutors.length);
		int count=0;
		int count2=0;
		for (int i=0; i<Times.length; i++)
		{
			if (Times[i]==(9)|| Times[i]==(10)||Times[i]==(11)||Times[i]==(14)||Times[i]==(15))
			{
				NameOfClass[i]= Classes[i-count];
			}

			else 
			{
				NameOfClass[i]="Available";
				count++;
			}
		}

		for (int j=0; j<NameofTutors.length; j++)
		{
			if (NameofTutors[j].equals("mandy")||NameofTutors[j].equals("linda")||NameofTutors[j].equals("liz")||NameofTutors[j].equals("jane"))
			{
				NameofTutors[j]=Tutors[j-count2];
			}	

			else if (NameofTutors[j].equals(""))
			{
				NameofTutors[j]= "         ";
				count2++;
			}
		}

		Classtimes =(Times[0]+"-10"+"\t"+"\t"+Times[1]+"-11"+"\t"+"\t"+Times[2]+"-12"
				+"\t"+"\t"+Times[3]+"-13"+"\t"+"\t"+Times[4]+"-14"+"\t"+"\t"+Times[5]+"-15"+"\t"
				+"\t"+Times[6]+"-16");//this is for the first line that displays the times

		ReordertheClasses(NameOfClass, 2);
		AddedClasses = new ArrayList<String>();
		AddedClasses.add(Classes[0]);
		AddedClasses.add(Classes[1]);
		AddedClasses.add(Classes[2]);
		AddedClasses.add(Classes[3]);
		AddedClasses.add(Classes[4]);
		AddedTutors = new ArrayList<String>();
		AddedTutors.add(Tutors[0]);
		AddedTutors.add(Tutors[1]);
		AddedTutors.add(Tutors[2]);
		AddedTutors.add(Tutors[3]);
		AddedTutors.add(Tutors[4]);
		DisplayClasses = NameOfClass[0]+"\t"+"\t"+NameOfClass[1]+"\t"+NameOfClass[2]+"\t"+"\t"+NameOfClass[3]+
				"\t"+NameOfClass[4]+"\t"+NameOfClass[5]+"\t"+"\t"+NameOfClass[6];

		DisplayTutors = NameofTutors[3]+"\t"+"\t"+NameofTutors[4]+"\t"+"\t"+NameofTutors[0]+"\t"+"\t"+ ""+"\t"+"\t"+""+"\t"+"\t"+ 
				NameofTutors[0]+"\t"+"\t"+NameofTutors[1];
		displayline=DisplayClasses;//add displayline1 and classnames
		displayline2=DisplayTutors;//the same for the displayline2
		display.setText(Classtimes+"\n"+displayline+"\n"+displayline2); //call the method that displays the timetable
	}

	// Instantiates timetable display and adds it to GUI
	public void updateDisplay() 
	{
		display.setText(classnames);// it updates the display

	}

	// adds buttons to top of GUI
	public void layoutTop() 
	{
		JPanel top = new JPanel();
		closeButton = new JButton("Save and Exit");
		closeButton.addActionListener(this);
		top.add(closeButton);
		attendanceButton = new JButton("View Attendances");
		attendanceButton.addActionListener(this);
		GuideButton= new JButton("Guide");
		GuideButton.addActionListener(this);
		top.add(attendanceButton);
		top.add(GuideButton);
		add(top, BorderLayout.NORTH);
	}

	/*
	 * adds labels, text fields and buttons to bottom of GUI
	 */
	public void layoutBottom() 
	{
		// instantiate panel for bottom of display
		JPanel bottom = new JPanel(new GridLayout(3, 3));

		// add upper label, text field and button
		JLabel idLabel = new JLabel("Enter Class Id");
		bottom.add(idLabel);
		idIn = new JTextField();
		bottom.add(idIn);
		JPanel panel1 = new JPanel();
		addButton = new JButton("Add");
		addButton.addActionListener(this);
		panel1.add(addButton);
		bottom.add(panel1);

		// add middle label, text field and button
		JLabel nmeLabel = new JLabel("Enter Class Name");
		bottom.add(nmeLabel);
		classIn = new JTextField();
		bottom.add(classIn);
		JPanel panel2 = new JPanel();
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		panel2.add(deleteButton);
		bottom.add(panel2);

		// add lower label text field and button
		JLabel tutLabel = new JLabel("Enter Tutor Name");
		bottom.add(tutLabel);
		tutorIn = new JTextField();
		bottom.add(tutorIn);

		add(bottom, BorderLayout.SOUTH);
	}

	public void processAdding()//method to process the add of a class 
	{
		FitnessProgram fitProgram= new FitnessProgram();
		fitProgram.InsertFitnessClass(fclass);
	}


	public void processDeletion()//method to process the deletion of a class  
	{
		FitnessProgram fitProgram= new FitnessProgram();
		fitProgram.deleteFitnessClass(ClassID);
	}


	public void UpdateAttendancesIn()//this method is to change the attendances file when classes are deleted. 
	//The attendance report will take then this information to create the attendance report. 
	{
		GetID = idIn.getText();
		File inputFile = new File("AttendancesIn.txt");
		File tempFile = new File ("TempAttendancesIn.txt");

		try{
			try{

				BufferedReader reader = new BufferedReader(new FileReader(inputFile));
				BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

				for (String s:IDsInOrder)//the line to be removed is the one that matches with the ID the user types to delete
				{
					if( GetID.equals(s))
					{
						String lineToRemove=s;

						String currentLine;
						while((currentLine = reader.readLine()) != null) 
						{
							// trim newline when comparing with lineToRemove
							String trimmedLine = currentLine.substring(0,3).trim();
							if(trimmedLine.equals(lineToRemove)) continue;
							writer.write(currentLine + System.getProperty("line.separator")); 
						}
					}
				}

				String line="";
				while ((line =reader.readLine())!=null)
				{
					writer.append(line+"\n");

				}

				for (String s2: IDsInOrder)//search in all the IDs and if the one the user typed is not in the IDs then it is a new one.
				{
					NewIDfound=null;

					if (!GetID.equals(s2) && addbuttonisPressed)//this is executed only when the user adds a new class ID
					{
						NewIDfound= GetID;	
					}
				}

				if (NewIDfound!=null)//we want to write on the file only when a new ID is found
				{
					writer.append(NewIDfound+ " "+"0"+" "+"0"+" "+"0"+" "+"0"+" "+"0"+"\n");
				}

				writer.close();

				reader.close(); 

				if(inputFile.delete())
				{
					tempFile.renameTo(inputFile);//rename the file to its original name AttendancesIn.txt
				}

			} 	catch (FileNotFoundException e) 
			{

				e.printStackTrace();
			}

		}		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
	}


	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource()==attendanceButton)
		{
			System.out.println("You pressed the attendanceButton button");
			{	
				report.ReadAndShowAttendancies();	

				if (report!=null)
				{
					report.northPanel();// i am calling the method northPanel which is in the ReportFrame class and it is supposed to show the window
				}
			}
		}
		else

			if (ae.getSource()==addButton)
			{
				GetID = idIn.getText();
				if (idIn.getText().equals(null)||idIn.getText().equals("")||idIn.getText().equals(" "))
				{
					JOptionPane.showMessageDialog(null, "ID field is empty! Please provide an id and try again.", "Result Summary" , JOptionPane.ERROR_MESSAGE);
				}

				else if (classIn.getText().equals(null)||classIn.getText().equals("")||classIn.getText().equals(" "))
				{
					JOptionPane.showMessageDialog(null, "Class field is empty! Please provide a class name and try again.", "Result Summary" , JOptionPane.ERROR_MESSAGE);
				}

				else if (tutorIn.getText().equals(null)||tutorIn.getText().equals("")||tutorIn.getText().equals(" "))
				{
					JOptionPane.showMessageDialog(null, "Tutor field is empty! Please provide a tutor name and try again.", "Result Summary" , JOptionPane.ERROR_MESSAGE);
				}

				else if (GetID.equals(IDsInOrder[0])||GetID.equals(IDsInOrder[1])
						||GetID.equals(IDsInOrder[2])
						||GetID.equals(IDsInOrder[3])||GetID.equals(IDsInOrder[4])
						||GetID.equals(IDsInOrder[5])||GetID.equals(IDsInOrder[6]))

				{
					JOptionPane.showMessageDialog(null, "ID typed already exists. Please provide another id and try again.", "Result Summary" , JOptionPane.ERROR_MESSAGE);
				}
				else if (AddedClasses.size()<7)
				{	

					addbuttonisPressed=true;
					fclass.getclassID();

					processAdding();//the method that process the add of a Fitness Class.
					if (buttoncounter==0)
					{
						ReordertheTutors(NameofTutors, 2);
						buttoncounter++;
					}

					UpdateAttendancesIn();
					ReadAttendancesIn();

					int count=0;
					DisplayAddedTutors="";
					DisplayAddedClasses="";
					for (int i=0; i<ClassesInOrder.length; i++)
					{

						if (ClassesInOrder[i].equals("Available") && count==0) 
						{
							IDsInOrder[i]=idIn.getText();
							ClassesInOrder[i]=classIn.getText();
							NameofTutors[i]=tutorIn.getText();
							System.arraycopy(ClassesInOrder, i, ClassesInOrder, i, ClassesInOrder.length -6);
							System.arraycopy(NameofTutors, i, NameofTutors, i, NameofTutors.length -6);
							System.arraycopy(IDsInOrder, i, IDsInOrder, i, IDsInOrder.length -6);
							AddedClasses.add(ClassesInOrder[i]);
							AddedTutors.add(NameofTutors[i]);
							count++;
						}

						DisplayAddedTutors=NameofTutors[0]+"\t"+"\t"+NameofTutors[1]+"\t"+"\t"+NameofTutors[2]+"\t"+"\t"+NameofTutors[3]
								+"\t"+"\t"+NameofTutors[4]+"    "+"\t"+NameofTutors[5]+"\t"+"\t"+NameofTutors[6];

						DisplayAddedClasses=ClassesInOrder[0]+" \t"+"\t"+ClassesInOrder[1]+"\t"+ClassesInOrder[2]+" \t"+ClassesInOrder[3]+
								" \t"+ClassesInOrder[4]+""+"   \t"+ClassesInOrder[5]+" \t"+ClassesInOrder[6];

						display.setText(Classtimes+"\n"+DisplayAddedClasses+"\n"+DisplayAddedTutors);//till here
					}//end of for loop

					System.out.println("You pressed the addButton button");
				}//end of else	
				else if (AddedClasses.size()==7)

				{
					JOptionPane.showMessageDialog(null, "Classes are full. You can not add more classes.", "Result Summary" , JOptionPane.ERROR_MESSAGE);
				}
			}//end of if add

			else
				if (ae.getSource()==deleteButton)
				{    			
					GetID = idIn.getText();

					if (idIn.getText().equals(null)||idIn.getText().equals("")||idIn.getText().equals(" "))
					{
						JOptionPane.showMessageDialog(null, "ID field is empty! Please provide an id and try again.", "Result Summary" , JOptionPane.ERROR_MESSAGE);
					}	


					else if (!GetID.equals(IDsInOrder[0])&&!GetID.equals(IDsInOrder[1])
							&&!GetID.equals(IDsInOrder[2])
							&&!GetID.equals(IDsInOrder[3])&&!GetID.equals(IDsInOrder[4])
							&&!GetID.equals(IDsInOrder[5])&&!GetID.equals(IDsInOrder[6]))

					{
						JOptionPane.showMessageDialog(null, "There is no class that matches the ID you provided. Please try with another ID.", "Result Summary" , JOptionPane.ERROR_MESSAGE);
					}

					else if (GetID!=null)
					{			
						addbuttonisPressed=false;
						ClassDeleted=false;

						processDeletion();//the process deletion
						count=0;

						for (int i=0; i<ClassesInOrder.length; i++)
						{
							if (GetID.equals(IDsInOrder[i]))
							{
								String ClassFound = ClassesInOrder[i];//assign the ID in the class

								if (ClassFound.equals(ClassesInOrder[i])){

									AddedClasses.remove(ClassesInOrder[i]);
									AddedTutors.remove(NameofTutors[i]);
									ClassesInOrder[i]="Available";
								}

								ReordertheClasses(NameOfClass, 2);	

								for (String s:ClassesInOrder){
									if (s.equals(ClassFound)){

										s=ClassesInOrder[i];

										System.arraycopy(ClassesInOrder, i, ClassesInOrder, i, ClassesInOrder.length -6);

									}

									for (int j=0; j<ClassesInOrder.length; j++){

										if (ClassesInOrder[j].equals("Available") && count==0){

											ClassDeleted=true;
											DeleteCount++;
											count++;
										}
									}
									DisplayClasses=ClassesInOrder[0]+" \t"+ClassesInOrder[1]+"\t"+ClassesInOrder[2]+" \t"+
											ClassesInOrder[3]+" \t"+ClassesInOrder[4]+"   \t"+ClassesInOrder[5]+" \t"+ClassesInOrder[6];

									if (DisplayAddedTutors!=null){
										display.setText(Classtimes+"\n"+DisplayClasses+"\n"+DisplayAddedTutors);
									}

									else 
									{
										display.setText(Classtimes+"\n"+DisplayClasses+"\n"+displayline2);}

									UpdateAttendancesIn();

								}
								//System.out.println("You pressed the deleteButton button");
							}
						}
						
					}
				}//end of if delete


				else
					if (ae.getSource()==closeButton)//this is the code for the save and exit. The data are written in the file ClassesOut and the program exits.
					{
						FileWriter writer =null;
						try{
							try{
								writer = new FileWriter(classesOutFile);	

								if (AddedClasses.size()==5 && ClassDeleted==false) 
								{
									ReordertheTutors(NameofTutors,2);//if there are no additions/
									ReordertheClasses(NameOfClass,2);//deletions,classes and tutors 
									//must be reordered
								} 
								else if (AddedClasses.size()<5 && ClassDeleted==true ) 
								{
									ReordertheTutors(NameofTutors,2);//if deletions have been made
									ReordertheClasses(NameOfClass,2);//testing showed that classes shall
									//be reordered.
								} 
								for (int i=0; i<ClassesInOrder.length; i++){
									String Time = Integer.toString(Times[i]) ;
									writer.write(IDsInOrder[i]+ " ");
									writer.write(ClassesInOrder[i], 0, ClassesInOrder[i].length());
									writer.write(" ");
									writer.write(NameofTutors[i], 0, NameofTutors[i].length());
									writer.write(" ");
									writer.write(Time ,0, Time.length() );
									writer.write(System.lineSeparator());
								}
							}
							finally{
								if (writer!=null) writer.close();
							}
						}
						catch (IOException e) {
							System.out.println("File error");
						}
						System.out.println("You pressed the save & exit button");
						ResetAttendancesFile();//csll the method to bring AttendancesIn file in its					
						//initial state with the initial attendances.
						System.exit(1);		
					}//end of if close

					else 
						if (ae.getSource()==GuideButton){

							JFrame frame = new JFrame("Instructions");
							// Create the StyleContext, the document and the pane
							StyleContext sc = new StyleContext();
							final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
							JTextPane pane = new JTextPane(doc);
							pane.setBackground(Color.yellow);
							SimpleAttributeSet set = new SimpleAttributeSet();
							set = new SimpleAttributeSet();
							StyleConstants.setFontSize(set, 14);

							// Create and add the style
							final javax.swing.text.Style heading2Style = sc.addStyle("Heading2", null);
							heading2Style.addAttribute(StyleConstants.FontSize, new Integer(14));
							heading2Style.addAttribute(StyleConstants.FontFamily, "arial");
							heading2Style.addAttribute(StyleConstants.Bold, new Boolean(true));
							heading2Style.addAttribute(StyleConstants.Underline, new Boolean(true));
							try {
								try {
									// Add the text to the document
									doc.insertString(0, text, set);

									// Finally, apply the style to the heading
									doc.setParagraphAttributes(0, 1, heading2Style, false);

								} catch (BadLocationException l) {
								}
							} catch (Exception q) {
								System.out.println("Exception when constructing document: " + q);
								System.exit(1);
							}
							frame.getContentPane().add(new JScrollPane(pane));
							frame.setSize(550, 370);


							frame.setVisible(true);

						}

	}// end of actionPerformed(ActionEvent ae)


	public static final String text = "Welcome to Fitness Classes Application program\n"
			+"\n"
			+"Here you can:\n"+
			"1. View fitness classes as well as their attendances.\n" +
			"2. Add a new Fitness Class that will initially have 0 attendances.\n" +
			"3. Delete a Fitness Class and its attendances. To delete a class, you will need\n" +
			"the id of each class. So for the existing ones the id's are jy1 for yoga, \n"+
			"lb1 for badminton, mp1 for pilates, mp2 for pilates and ls1 for step.\n"+
			"4. View in a separate window all the classes, tutors, attendances of each class " +
			"and overall average attendance.\n"+
			"5. The application when exits writes to the file AttendancesOut the current\n" +
			"classes and their attendances. It is recommended to end the application by\n" +
			"pressing the Save & Exit button. Otherwise there might be problems when trying "+
			"to launch it again. Enjoy!"+
			"\n\n"
			+"Developer: George Kosmadakis";

	private  void ResetAttendancesFile()//this method will bring Attendances In file to its normal state.
										//When additions/deletions are made, this file is changing
	{									//so the application fails to start again if this file is changed.				
										//it is called on pressing the Save&exit button.
									
		String line = null;
		ArrayList<String> lines = new ArrayList<String>();  
		FileReader fr = null;
		PrintWriter fw = null;
		try 
		{
			try {
				fr = new FileReader("AttendancesInCopy.txt");   
				BufferedReader br = new BufferedReader(fr);   
				fw = new PrintWriter("AttendancesIn.txt");   
				while((line=br.readLine()) != null) {    
					lines.add(line);   
					fw.write(line);
					fw.write("\n");
				}   
			}
			finally {
				// close the input file assuming it was successfully opened
				if (fr != null) fr.close();
				// close the output file assuming it was successfully opened
				if (fw != null) fw.close();
			}
		}
		catch(IOException exception){
			System.out.println("Error processing file: " + exception);
		}   
	}   


	public String GetID() 
	{
		return GetID;
	}

	public static ArrayList<String> getAddedClasses() 
	{
		return AddedClasses;
	}

	public static ArrayList<String> getAddedTutors() 
	{
		return AddedTutors;
	}
	public static boolean ClassDeleted ()//to indicate when classes are deleted.it is passed on the
	{									//ReportFrame. Helps to reorder or not the classes.
		return ClassDeleted;
	}

	public static int DeleteCount() 
	{
		return DeleteCount;
	}



}	



