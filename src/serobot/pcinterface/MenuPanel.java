package serobot.pcinterface;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Creates a menubar which allows the user to manually search for and select the jobs, items and locations csv files.
 * @author Aaquib Naved
 *
 */
public class MenuPanel extends JPanel {
	
	public MenuPanel(PCInterfaceDataModel model) {
		super();
		
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menubar.add(menu);
		
		JMenuItem importJobFile = new JMenuItem("Import Jobs File",
                KeyEvent.VK_T);
		importJobFile.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		importJobFile.getAccessibleContext().setAccessibleDescription(
				"Select file to import the job list.");
		importJobFile.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    model.setJobFilePath(selectedFile.getAbsolutePath());
			}
		});
		menu.add(importJobFile);
		
		JMenuItem importItemFile = new JMenuItem("Import Items File",
                KeyEvent.VK_T);
		importItemFile.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		importItemFile.getAccessibleContext().setAccessibleDescription(
				"Select file to import the item list.");
		importItemFile.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    model.setItemFilePath(selectedFile.getAbsolutePath());
			}
		});
		menu.add(importItemFile);
		
		JMenuItem importLocationsFile = new JMenuItem("Import Locations File",
                KeyEvent.VK_T);
		importLocationsFile.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		importLocationsFile.getAccessibleContext().setAccessibleDescription(
				"Select file to import the locations list.");
		importLocationsFile.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fileChooser.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    model.setItemFilePath(selectedFile.getAbsolutePath());
			}
		});
		menu.add(importLocationsFile);
		
		JMenu menu1 = new JMenu("Another Menu");
		menu1.setMnemonic(KeyEvent.VK_A);
		menu1.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menubar.add(menu1);
		
		JMenuItem menuItem2 = new JMenuItem("A text-only menu item",
                KeyEvent.VK_T);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem2.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menu1.add(menuItem2);
		
		add(menubar);
	}

}
