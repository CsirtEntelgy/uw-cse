package hw9;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import hw8.*;

/**
 * CampusPathsGUI is a class for generating a usable GUI for navigating the map around UW campus.
 * <p>
 *
 * CampusPathsGUI has the following functionalities:
 * 1) Create a new GUI interface
 * 2) Search and display the shortest path between selected buildings
 * 3) Reset the interface to initial state
 * <p>
 *
 * @author Young Bin Cho
 */

public class CampusPathsGUI{
	
	/**frame that is the interface that interacts with the user*/
	private JFrame frame;
	/**CampusMap object holding the map around UW campus*/
	private CampusMap map;
	/**list of all buildings in the map*/
	private List<CampusNode<Point2D>> buildings;
	/**file representing the image file of the map*/
	private File mapImageFile;
	
	/**
	 * constructs a new CampusPathsGUI out of given map and name of image file
	 * @param map the CampusMap that represents the UW campus
	 * @param mapImageFileName name of the image file to read in
	 * @effects creates new CampusPathsGUI object 
	 * @requires map != null
	 * @throws IOException if given file cannot be read or is invalid
	 */
	public CampusPathsGUI(CampusMap map, String mapImageFileName) throws IOException {
		//initializing class variables
		this.frame = new JFrame();
		this.map = map;
		this.buildings = new ArrayList<CampusNode<Point2D>>();
		//populating list of buildings with appropriate ones and sort it
		for(CampusNode<Point2D> c : map.getAllNodes()) {
			if(!c.getLongName().equals("") && !c.getShortName().equals(""))
				buildings.add(c);
		}
		Collections.sort(buildings);
		this.mapImageFile = new File("src/hw8/data/"+mapImageFileName);
	}
	
	/**
	 * creates, populates, and shows frame
	 * @throws IOException if populateFrame throws IOException
	 */
	private void createFrame() throws IOException {
		//creating frame
		frame = new JFrame("UW Campus Paths");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setPreferredSize(new Dimension(810,670));
		//string array of building names
		String[] tempArray = new String[buildings.size()];
		for(int i = 0; i < tempArray.length; i++)
			tempArray[i] = buildings.get(i).getLongName() + " (" + buildings.get(i).getShortName() + ")";
		//populating frame
		populateFrame(tempArray);
		//displaying frame
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * creates and places the components inside main frame
	 * @param arr list of buildings
	 * @throws IOException if image couldn't be read
	 */
	private void populateFrame(String[] arr) throws IOException {
		//creating components
		JLabel fromJLabel = new JLabel("From:");
		JLabel toJLabel = new JLabel("To:");
		JLabel campusMapJLabel = new JLabel("Campus Map:");
		JButton searchJButton = new JButton("Search Path");
		JButton resetJButton = new JButton("Reset");
		JButton exitJButton = new JButton("Exit");
		JComboBox<String> fromJComboBox = new JComboBox<String>(arr);
		JComboBox<String> toJComboBox = new JComboBox<String>(arr);
		JLabel mapJLabel = new JLabel();
		//setting the locations for the components
		fromJLabel.setBounds(20, 10, 50, 10);
		toJLabel.setBounds(400, 10, 50, 10);
		campusMapJLabel.setBounds(360, 95, 100, 10);
		searchJButton.setBounds(20, 65, 120, 20);
		resetJButton.setBounds(360, 65, 80, 20);
		exitJButton.setBounds(695, 65, 80, 20);
		fromJComboBox.setBounds(20, 35, 375, 20);
		toJComboBox.setBounds(400, 35, 375, 20);
		mapJLabel.setBounds(20, 115, 755, 503);
		//setting the initial selected index
		fromJComboBox.setSelectedIndex(0);
		toJComboBox.setSelectedIndex(0);
		//adding the components to frame
		frame.getContentPane().add(fromJLabel);
		frame.getContentPane().add(toJLabel);
		frame.getContentPane().add(campusMapJLabel);
		frame.getContentPane().add(searchJButton);
		frame.getContentPane().add(resetJButton);
		frame.getContentPane().add(exitJButton);
		frame.getContentPane().add(fromJComboBox);
		frame.getContentPane().add(toJComboBox);
		frame.getContentPane().add(mapJLabel);
		//setting the map to the initial map
		mapJLabel.setIcon(new ImageIcon(ImageIO.read(mapImageFile).
				getScaledInstance(755, 503, Image.SCALE_SMOOTH)));
		/**
		 * event listener for clicking the search button
		 * takes the fields inside the combo box and applies search
		 * marks the buildings and the minimum cost path between them
		 */
		searchJButton.addActionListener(e -> {
			//temporary image file
			BufferedImage tempImage = null;
			//reading in the file
		    try {
				tempImage = ImageIO.read(mapImageFile);
			} catch (IOException i) {
            	JOptionPane.showMessageDialog(null, 
            			"campus_map.jpg in src/hw8/data is corrupt", 
            			"InfoBox: " + "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		    //creating Graphics2D instance
			Graphics2D g2d = tempImage.createGraphics();
			g2d.setPaint(Color.RED);
			//setting stroke of g2d
			float dash1[] = {10.0f};
			g2d.setStroke(new BasicStroke
					(5.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10.0f, dash1, 0.0f));
			//finding the shortest path
			CampusPath<Point2D,Double> tempPath = 
					map.findPath(buildings.get(fromJComboBox.getSelectedIndex()).getShortName(), 
								 buildings.get(toJComboBox.getSelectedIndex()).getShortName());
			//marking the starting and the ending points
			g2d.draw(new Ellipse2D.Double(tempPath.getPath().get(0).getFrom().getPoint().getX(), 
					tempPath.getPath().get(0).getFrom().getPoint().getY(), 50, 50));
			g2d.draw(new Ellipse2D.Double(tempPath.getLatest().getTo().getPoint().getX(), 
					tempPath.getLatest().getTo().getPoint().getY(), 50, 50));
			//drawing all the paths
			for(CampusEdge<Point2D,Double> c : tempPath.getPath())
				g2d.draw(new Line2D.Double(c.getFrom().getPoint(), c.getTo().getPoint()));
			//below are methods to zoom in to the particular section of the map
			Rectangle2D rect = new Rectangle2D.Double();
			rect.setRect(tempPath.getPath().get(0).getFrom().getPoint().getX(), 
					tempPath.getPath().get(0).getFrom().getPoint().getY(), 0, 0);
			rect.add(tempPath.getLatest().getTo().getPoint());
			tempImage = tempImage.getSubimage((int) rect.getMinX() - 200, (int) rect.getMinY() - 200, 
						(int) rect.getWidth() + 400, (int) rect.getHeight() + 400);
			//setting the map image to the new one
			mapJLabel.setIcon(new ImageIcon(tempImage.getScaledInstance(755, 503, Image.SCALE_SMOOTH)));
	    });
		
		/**
		 * event listener for clicking the reset button
		 * resets the application to initial state
		 */
		resetJButton.addActionListener(e -> {
			fromJComboBox.setSelectedIndex(0);
			toJComboBox.setSelectedIndex(0);
			try {
				mapJLabel.setIcon(new ImageIcon(ImageIO.read(mapImageFile).
								  	  getScaledInstance(755, 503, Image.SCALE_SMOOTH)));
			} catch (IOException i) {
            	JOptionPane.showMessageDialog(null, 
            			"campus_map.jpg in src/hw8/data is corrupt", 
            			"InfoBox: " + "Error", JOptionPane.INFORMATION_MESSAGE);
			}
	    });
		
		/**
		 * event listener for clicking the exit button
		 * closes the application
		 */
		exitJButton.addActionListener(e -> {
			frame.dispose();
	    });
	}
	
	/**
	 * prompts the creatFrame method, running the GUI as an
	 * application
	 */
	public void showGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createFrame();
				} catch (IOException e) {
                	JOptionPane.showMessageDialog(null, 
                			"campus_map.jpg in src/hw8/data is corrupt", 
                			"InfoBox: " + "Error", JOptionPane.INFORMATION_MESSAGE);
				}
            }
        });
    }
}