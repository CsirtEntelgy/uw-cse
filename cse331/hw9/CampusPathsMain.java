package hw9;

import hw8.*;
import java.io.IOException;
import javax.swing.JOptionPane;
import hw6.MarvelParser.MalformedDataException;

/**
 * CampusPathsMain is a class that provokes the CampusPathsGUI class
 * with appropriate input and prompts the GUI.
 * <p>
 *
 * @author Young Bin Cho
 */

public class CampusPathsMain {
	
	/**
	 * main method for executing the program
	 * @param args command lines
	 * @throws MalformedDataException if files under hw8/data are corrupt
	 * @throws IOException if some files couldn't be read
	 * @throws NullPointerException if some variables weren't initialized
	 */
	public static void main(String args[]) throws MalformedDataException, IOException {
		try {
			//parsing and creating new map object
			CampusMap cm = new CampusMap("campus_buildings.dat", "campus_paths.dat");
			//initializing the GUI
			CampusPathsGUI cg = new CampusPathsGUI(cm, "campus_map.jpg");
			//prompting the run method
			cg.showGUI();
		} catch (MalformedDataException e) {
			JOptionPane.showMessageDialog(null, 
        			"campus_buildings.dat or campus_paths.dat in src/hw8/data is corrupt", 
        			"InfoBox: " + "Error", JOptionPane.INFORMATION_MESSAGE);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null, 
        			"Some variables not initialized: critical error", 
        			"InfoBox: " + "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}