package serobot.pcinterface;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import serobot.jobparser.Item;
import serobot.jobparser.Job;
import serobot.jobparser.JobLine;

/**
 * The panel which contains the 3 tables (Jobs, items in job and item information)
 * When the user clicks on a job, the items in the job are displayed in the second table.
 * When the user clicks on an item, the item information is displayed in the third table.
 * @author Aaquib Naved
 *
 */
public class TablePanel extends JPanel implements Observer {
	
	private PCInterfaceDataModel model;
	private List<Job> jobList;
	private DefaultTableModel jobsModel;
	private DefaultTableModel itemsModel;
	private DefaultTableModel itemInfoModel;
	private JTable jobs;
	private JTable items;
	private JTable itemInfo;
	private List<Item> itemsInJob;
	private Job selectedJob;
	private Job previousSelectedJob = new Job("test");
	private Item selectedItem;
	private int tabIndex;
	
	public TablePanel(PCInterfaceDataModel model) {
		super();
		this.model = model;
		this.jobList = model.getUpcomingJobs();
		this.selectedJob = model.getSelectedJob();
		this.selectedItem = model.getSelectedItem();
		this.itemsInJob = new ArrayList<Item>();
		
		jobsModel = new NonEditableTableModel();
		
		jobs = new JTable(jobsModel);
		jobsModel.addColumn("Jobs");
		
		for (Job job : jobList) {
			jobsModel.addRow(new Object[]{"ID: " + job.getID()});
		}
		jobs.setPreferredScrollableViewportSize(new Dimension(200, 100));
		JScrollPane jobListPane = new JScrollPane(jobs);
		add(jobListPane);
		
		jobs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jobs.getSelectionModel().addListSelectionListener(e -> {
			if (jobs.getSelectionModel().getMinSelectionIndex() >= 0) {
				if (model.getTabIndex() == 0) {
					selectedJob = model.getUpcomingJobs().get(jobs.getSelectionModel().getMinSelectionIndex());
				}
				else if (model.getTabIndex() == 1) {
					selectedJob = model.getCompletedJobs().get(jobs.getSelectionModel().getMinSelectionIndex());
				}
				else if (model.getTabIndex() == 2) {
					selectedJob = model.getOngoingJobs().get(jobs.getSelectionModel().getMinSelectionIndex());
				}
				else {
					selectedJob = model.getUpcomingJobs().get(jobs.getSelectionModel().getMinSelectionIndex());
				}
				model.setSelectedJob(selectedJob);
			}
			});
		
		itemsModel = new NonEditableTableModel();
		
		items = new JTable(itemsModel);
		itemsModel.addColumn("Items");
		
		if (selectedJob != null) {
			for (JobLine jobLine : selectedJob.getLines()) {
				itemsModel.addRow(new Object[]{"\t JobLine Item ID: " + jobLine.getItem().getID() + " - Count: " + jobLine.getCount()});
				itemsInJob.add(jobLine.getItem());
			} 
		}
		items.setPreferredScrollableViewportSize(new Dimension(200, 100));
		JScrollPane itemListPane = new JScrollPane(items);
		add(itemListPane);
		
		items.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		items.getSelectionModel().addListSelectionListener(e -> {
			if (items.getSelectionModel().getMinSelectionIndex() >= 0) {
				selectedItem = itemsInJob.get(items.getSelectionModel().getMinSelectionIndex());
				model.setSelectedItem(selectedItem);
			}
			});
		
		itemInfoModel = new NonEditableTableModel();
		
		itemInfo = new JTable(itemInfoModel);
		itemInfoModel.addColumn("Item Info");
		
		if (selectedItem != null) {
			itemInfoModel.addRow(new Object[]{"ID: " + selectedItem.getID()});
			itemInfoModel.addRow(new Object[]{"ID: " + selectedItem.getXPosition()});
			itemInfoModel.addRow(new Object[]{"ID: " + selectedItem.getYPosition()});
			itemInfoModel.addRow(new Object[]{"ID: " + selectedItem.getReward()});
			itemInfoModel.addRow(new Object[]{"ID: " + selectedItem.getWeight()});
		}
		itemInfo.setPreferredScrollableViewportSize(new Dimension(200, 100));
		JScrollPane itemInfoListPane = new JScrollPane(itemInfo);
		add(itemInfoListPane);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		this.selectedJob = model.getSelectedJob();
		this.selectedItem = model.getSelectedItem();
		int rowCount;
		
		if (model.getTabIndex() != tabIndex) {
			tabIndex = model.getTabIndex();
			model.setSelectedJob(null);
			model.setSelectedItem(null);
			if (model.getTabIndex() == 0) {
				jobList = model.getUpcomingJobs();
			}
			else if (model.getTabIndex() == 1) {
				jobList = model.getCompletedJobs();
			}
			else if (model.getTabIndex() == 2) {
				jobList = model.getOngoingJobs();
			}
			if (model.getTabIndex() <= 2) {
				rowCount = jobsModel.getRowCount();
				//Remove rows one by one from the end of the table
				for (int i = rowCount - 1; i >= 0; i--) {
					jobsModel.removeRow(i);
				}
				for (Job job : jobList) {
					//jobs.addElement("ID: " + job.getID());
					jobsModel.addRow(new Object[] { "ID: " + job.getID() });
				} 
			} 
		}
		items.getColumnModel().getColumn(0).setHeaderValue("Items");
		
		
		if (!jobList.equals(model.getUpcomingJobs())) {
			rowCount = jobsModel.getRowCount();
			//Remove rows one by one from the end of the table
			for (int i = rowCount - 1; i >= 0; i--) {
				jobsModel.removeRow(i);
			}
			for (Job job : jobList) {
				//jobs.addElement("ID: " + job.getID());
				jobsModel.addRow(new Object[] { "ID: " + job.getID() });
			} 
		}
		jobList = model.getUpcomingJobs();
		
		if (previousSelectedJob != selectedJob) {
			itemsInJob = new ArrayList<Item>();
			rowCount = itemsModel.getRowCount();
			//Remove rows one by one from the end of the table
			for (int i = rowCount - 1; i >= 0; i--) {
				itemsModel.removeRow(i);
			}
			if (selectedJob != null) {
				for (JobLine jobLine : selectedJob.getLines()) {
					itemsInJob.add(jobLine.getItem());
					itemsModel.addRow(new Object[] {
							"\t JobLine Item ID: " + jobLine.getItem().getID() + " - Count: " + jobLine.getCount() });
					//items.addElement("\t JobLine Item ID: " + jobLine.getItem().getID() + " - Count: " + jobLine.getCount());
				}
			} 
		}
		if (selectedJob != null) {
			items.getColumnModel().getColumn(0).setHeaderValue("Job ID: " + selectedJob.getID());
		}
		previousSelectedJob = model.getSelectedJob();
		
		rowCount = itemInfoModel.getRowCount();
		//Remove rows one by one from the end of the table
		for (int i = rowCount - 1; i >= 0; i--) {
		    itemInfoModel.removeRow(i);
		}
		if (selectedItem != null) {
			itemInfoModel.addRow(new Object[]{"ID: " + selectedItem.getID()});
			itemInfoModel.addRow(new Object[]{"X: " + selectedItem.getXPosition()});
			itemInfoModel.addRow(new Object[]{"Y: " + selectedItem.getYPosition()});
			itemInfoModel.addRow(new Object[]{"Reward: " + selectedItem.getReward()});
			itemInfoModel.addRow(new Object[]{"Weight: " + selectedItem.getWeight()});
		}
		
		repaint();
	}
}
