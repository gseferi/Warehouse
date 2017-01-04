package serobot.pcinterface;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RewardPanel extends JPanel implements Observer {

	private PCInterfaceDataModel model;
	private JLabel totalReward;
	
	public RewardPanel(PCInterfaceDataModel model) {
		super();
		this.model = model;
		totalReward = new JLabel("Total Reward: " + model.getTotalReward());
		add(totalReward);
	}
	
	@Override
	public void update(Observable o, Object arg) {

		double reward = 0;
		for (int i = 0; i<model.getCompletedJobs().size(); i++) {
			for (int j = 0; j<model.getCompletedJobs().get(i).getLines().size(); j++) {
				reward += model.getCompletedJobs().get(i).getLine(j).getItem().getReward();
			}
		}
		model.setTotalReward(reward);
		
		totalReward.setText("Total Reward: " + model.getTotalReward());
	}

}
