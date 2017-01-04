package serobot.pcinterface;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class TabPanel extends JPanel {
	
	/**
	 * A panel which contains the tabbed pane.
	 * Contains 4 tabs (Upcoming Jobs, Completed Jobs, Ongoing jobs and Errors).
	 * @param model
	 */
	public TabPanel(PCInterfaceDataModel model) {
		super();
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Upcoming Jobs", new JPanel());
	    tabbedPane.addTab("Completed Jobs", new JPanel());
	    tabbedPane.addTab("Ongoing Jobs", new JPanel());
	    tabbedPane.addTab("Errors", new JPanel());
	    tabbedPane.addChangeListener(e -> model.setTabIndex(tabbedPane.getSelectedIndex()));
	    tabbedPane.setPreferredSize(new Dimension(600, 30));
	    add(tabbedPane);
		
	}

}
