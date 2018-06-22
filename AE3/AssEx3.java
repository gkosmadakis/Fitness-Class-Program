import javax.swing.JFrame;


public class AssEx3 {

	public static void main(String[] args) {
		/*	The main method is responsible to instantiate and display a SportsCentreGUI object*/
		SportsCentreGUI display = new SportsCentreGUI();
		display.setVisible(true);
		display.ReadAndDisplayTimeTable();
	}

}
