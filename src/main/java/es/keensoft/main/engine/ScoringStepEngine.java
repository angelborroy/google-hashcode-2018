package es.keensoft.main.engine;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.keensoft.bean.Input;
import es.keensoft.bean.InputLine;
import es.keensoft.bean.Output;

/**
 *  Scoring engine to control step-by-step truck movements
 *  
 *  - Ensure that movements are correct
 *  - Consider bonus conditions to pick a truck for a ride
 *    - To complete a ride before its latest finish
 *    - To start a ride exactly in its earliest allowed start step
 * 
 *  Scorings
 *  
 *  A - example                 4 points
 *  B - should be easy    175,671 points
 *  C - no hurry        8,016,571 points
 *  D - metropolis      6,906,728 points
 *  E - high bonus     20,828,717 points
 */
public class ScoringStepEngine extends StepEngineTemplate {
	
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
			
			// Top priority for rides starting exactly in its earliest
			for (int ride = 0; ride < pendingRides.size(); ride++) {
				
				// Find a truck for a pickable ride
				if (pendingRides.get(ride) && step == input.getRide().get(ride).getStartingTime()) {
					Integer truck = availableTruck(input.getRide().get(ride));
					if (truck >= 0) {
						startRide(ride, truck);
						// Delivered ride
						pendingRides.set(ride, false);
						System.out.println("BONUS! Starting ride exactly at its earlier allowed step!");
						startingInTime++;
					}
					
				}
				
			}
			
			// Start ride for available truck that ends the ride first
			for (int ride = 0; ride < pendingRides.size(); ride++) {
				
				// Find a truck for a pickable ride
				if (pendingRides.get(ride) && step > input.getRide().get(ride).getStartingTime()) {
					Integer truck = availableTruck(input.getRide().get(ride));
					if (truck >= 0) {
						startRide(ride, truck);
						// Delivered ride
						pendingRides.set(ride, false);
						if (travelingVehicles.get(truck).getTime() + step < input.getRide().get(ride).getFinishingTime()) {
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
		System.out.println("Completing bonus: " + startingInTime);
		
		prepareOutputFormat();
		return output;
		
	}
	
	/**
	 * Find available truck that delivers the ride before 
	 * @param ride
	 * @return
	 */
	protected static Integer availableTruck(InputLine ride) {
		Map<Integer, Integer> distanceTruck = new TreeMap<Integer,Integer>(); 
		for (int truck = 0; truck < travelingVehicles.size(); truck++) {
			if (travelingVehicles.get(truck).getTime() == 0) {
				// Blind to trucks delivering at the same time
				distanceTruck.put(getDistance(ride, travelingVehicles.get(truck)), truck);
			}
		}
		if (!distanceTruck.isEmpty()) {
			return distanceTruck.get(distanceTruck.keySet().iterator().next());
		} else {
		    return -1;
		}
	}
	
}
