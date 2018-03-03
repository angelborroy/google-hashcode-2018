package es.keensoft.main.engine;

import java.util.List;

import es.keensoft.bean.Input;
import es.keensoft.bean.InputLine;
import es.keensoft.bean.Output;
import es.keensoft.bean.TravelingVehicle;

/**
 *  Best choice engine to control step-by-step truck movements
 *  
 *  - Ensure that movements are correct
 *  - Consider bonus conditions to pick a truck for a ride
 *    - To complete a ride before its latest finish
 *    - To start a ride exactly in its earliest allowed start step
 *  - Evaluate and score every available ride for a free truck
 * 
 *  Scorings
 *  
 *  A - example                 4 points
 *  B - should be easy    154,016 points
 *  C - no hurry        9,427,222 points
 *  D - metropolis      7,378,026 points
 *  E - high bonus     13,699,510 points
 */
public class BestChoiceStepEngine extends StepEngineTemplate {
	
	/**
	 * Start steps count from 0 to steps number
	 * @param inputParam
	 * @return
	 */
	public static List<Output> start(Input inputParam) {
		
		// Initialization
		input = inputParam;
		setDataDefaultValues();
		
		// Steps looping
		Integer steps = input.getSteps();
		for (int step = 0; step < steps; step++) {
			
			System.out.println("Step " + step);
			
			nextStep();
			
			// Find best choice for every free truck
			for (int i = 0; i < travelingVehicles.size(); i++) {
				TravelingVehicle truck = travelingVehicles.get(i);
				if (truck.getTime() == 0) {
					Integer bestRide = -1;
					Integer currentBestScoring = 0;
					for (int j = 0; j < pendingRides.size(); j++) {
						InputLine ride = input.getRide().get(j);
						if (pendingRides.get(j) && step >= ride.getStartingTime()) {
							Integer scoring = 0;
							if (pendingRides.get(j) && step == ride.getStartingTime()) {
								scoring = scoring + input.getBonus();
							}
							if (truck.getTime() + step < ride.getFinishingTime()) {
								scoring = scoring + getRawDistance(ride);
							}
							if (scoring > currentBestScoring) {
								currentBestScoring = scoring;
								bestRide = j;
							}
						}
					}
					// Start travel with best ride available
					if (bestRide != -1) {
						startRide(bestRide, i);
						// Delivered ride
						pendingRides.set(bestRide, false);
						// Updating bonus counters
						if (pendingRides.get(bestRide) && step == input.getRide().get(bestRide).getStartingTime()) {
							System.out.println("BONUS! Starting ride exactly at its earlier allowed step!");
							startingInTime++;
						}
						if (travelingVehicles.get(i).getTime() + step < input.getRide().get(bestRide).getFinishingTime()) {
							System.out.println("BONUS! This ride will end before its latest finish!");
							deliveredInTime++;
						}
					}
				}
			}
			
		}
		
		countUncoveredRides();
		
		// Bonus report
		System.out.println("Starting bonus: " + startingInTime);
		System.out.println("Completing bonus: " + deliveredInTime);
		
		prepareOutputFormat();
		return output;
		
	}
	
}
