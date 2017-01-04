package serobot.jobselection;

import java.util.ArrayList;

import serobot.jobparser.Canceled;
import serobot.jobparser.JobLine;

public class CancelPrediction {
	ArrayList<Canceled> canceleds;
	double biggestWeight = 7.54;
	double biggestReward = 23.68;
	double biggestQuantity = 8.0;
	
	double weightSplit = 3;
	double rewardSplit = 3;
	double quantitySplit = 3;
	
	double weightSplitValue = biggestWeight / weightSplit;
	double rewardSplitValue = biggestReward / rewardSplit;
	double quantitySplitValue = biggestQuantity / quantitySplit;
	
	int itemsCancelled = 0;
	int itemsCompleted = 0;
	
	int[] weightsCancelled;
	int[] rewardsCancelled;
	int[] quantitiesCancelled;
	
	int[] weightsCompleted;
	int[] rewardsCompleted;
	int[] quantitiesCompleted;
	
	public CancelPrediction (ArrayList<Canceled> canceleds){
		this.canceleds = canceleds;
		this.weightsCancelled = new int[(int) weightSplit];
		this.rewardsCancelled = new int[(int) rewardSplit];
		this.quantitiesCancelled = new int[(int) quantitySplit];
		this.weightsCompleted = new int[(int) weightSplit];
		this.rewardsCompleted = new int[(int) rewardSplit];
		this.quantitiesCompleted = new int[(int) quantitySplit];
	}
	
	public void Compute (){
		for (Canceled canceled : canceleds){
			for (JobLine jobLine : canceled.getJob().getLines()) {
				//Devide biggestWeight by weightSplit
				//Loop through from i = 1 to weightSplit
				//is item > weightSplit devidedthing * i-1 and < devidedthing * i
				//if it is weights[i]++]
				
				if (canceled.isCanceled()){
					for (int i = 1; i <= weightSplit ; i++){
						if ((jobLine.getItem().getWeight() < weightSplitValue * i) && (jobLine.getItem().getWeight() > weightSplitValue * i-1)){
							weightsCancelled[i-1] ++;
							itemsCancelled++;
						}
					}
				}
				if (!canceled.isCanceled()){
					for (int i = 1; i <= weightSplit ; i++){
						if ((jobLine.getItem().getWeight() < weightSplitValue * i) && (jobLine.getItem().getWeight() > weightSplitValue * i-1)){
							weightsCompleted[i-1] ++;
							itemsCompleted ++;
						}
					}
				}
				if (canceled.isCanceled()){
					for (int i = 1; i <= rewardSplit ; i++){
						if ((jobLine.getItem().getWeight() < rewardSplitValue * i) && (jobLine.getItem().getWeight() > rewardSplitValue * i-1)){
							rewardsCancelled[i-1] ++;
							itemsCancelled++;
						}
					}
				}
				if (!canceled.isCanceled()){
					for (int i = 1; i <= rewardSplit ; i++){
						if ((jobLine.getItem().getWeight() < rewardSplitValue * i) && (jobLine.getItem().getWeight() > rewardSplitValue * i-1)){
							rewardsCompleted[i-1] ++;
							itemsCompleted ++;
						}
					}
				}
				if (canceled.isCanceled()){
					for (int i = 1; i <= quantitySplit ; i++){
						if ((jobLine.getItem().getWeight() < quantitySplitValue * i) && (jobLine.getItem().getWeight() > quantitySplitValue * i-1)){
							quantitiesCancelled[i-1] ++;
							itemsCancelled++;
						}
					}
				}
				if (!canceled.isCanceled()){
					for (int i = 1; i <= quantitySplit ; i++){
						if ((jobLine.getItem().getWeight() < quantitySplitValue * i) && (jobLine.getItem().getWeight() > quantitySplitValue * i-1)){
							quantitiesCompleted[i-1] ++;
							itemsCompleted ++;
						}
					}
				}
						
			}
		}
		for (int i = 0; i < quantitiesCancelled.length ; i++){
			System.out.println(quantitiesCancelled[i]/itemsCancelled);
		}
		for (int i = 0; i < quantitiesCompleted.length ; i++){
			System.out.println(quantitiesCompleted[i]/itemsCompleted);
		}
	}
}
