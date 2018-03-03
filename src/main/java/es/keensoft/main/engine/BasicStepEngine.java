package es.keensoft.main.engine;

import java.util.List;

import es.keensoft.bean.Input;
import es.keensoft.bean.Output;

/**
 *  Basic engine to control step-by-step truck movements
 *  
 *  - Ensure that movements are correct
 *  - Any available truck is selected to start a ride
 * 
 *  Scorings (something below that numbers may mean that solution is not right)
 *  
 *  A - example                 4 points
 *  B - should be easy    158,890 points
 *  C - no hurry        7,985,753 points
 *  D - metropolis      6,763,245 points
 *  E - high bonus     13,661,075 points
 */
public class BasicStepEngine extends StepEngineTemplate {
	
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
			
			// Start ride for any available truck
			for (int ride = 0; ride < pendingRides.size(); ride++) {
				
				// Find a truck for a pickable ride
				if (pendingRides.get(ride) && step >= input.getRide().get(ride).getStartingTime()) {
					Integer truck = availableTruck();
					if (truck >= 0) {
						startRide(ride, truck);
						// Delivered ride
						pendingRides.set(ride, false);
						// Updating bonus counters
						if (pendingRides.get(ride) && step == input.getRide().get(ride).getStartingTime()) {
							System.out.println("BONUS! Starting ride exactly at its earlier allowed step!");
							startingInTime++;
						}
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
	 * Find any available truck
	 * @return
	 */
	private static Integer availableTruck() {
		for (int truck = 0; truck < travelingVehicles.size(); truck++) {
			if (travelingVehicles.get(truck).getTime() == 0) {
				return truck;
			}
		}
		return -1;
	}
	
}
