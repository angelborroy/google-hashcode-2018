package es.keensoft.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.keensoft.bean.Input;
import es.keensoft.bean.InputLine;
import es.keensoft.bean.Output;
import es.keensoft.bean.TravelingVehicle;

/**
 * Engine to control step-by-step truck movements
 *
 */
public class StepEngine {
	
	/**
	 * Start steps count from 0 to steps number
	 * @param input
	 * @return
	 */
	public static List<Output> start(Input input) {
		
		Map<Integer, List<Integer>> outputCache = new TreeMap<Integer, List<Integer>>();
		
		Integer steps = input.getSteps();
		List<Output> output = new ArrayList<Output>();
		
		// Initialize traveling vehicles
		List<TravelingVehicle> travelingVehicles = new ArrayList<TravelingVehicle>();
		for (int i = 0; i < input.getVehicles(); i++) {
			TravelingVehicle tv = new TravelingVehicle();
			tv.setTime(0);
			tv.setRide(-1);
			travelingVehicles.add(tv);
		}
		
		// Initialize pending rides
		List<Boolean> pendingRides = new ArrayList<Boolean>();
		for (int i = 0; i < input.getRides(); i++) {
			pendingRides.add(true);
		}
		
		// Steps looping
		for (int i = 0; i < steps; i++) {
			
			System.out.println("Step " + i);
			
			nextStep(travelingVehicles, pendingRides, outputCache);
			
			List<Integer> orderedPendingRides = getOrderedPendingRides(pendingRides, input.getRide(), i, input);
			
			// Start ride while any truck is available
			for (int j = 0; j < 	orderedPendingRides.size(); j++) {
				Integer truck = availableTruck(travelingVehicles);
				Integer ride = orderedPendingRides.get(j);
				if (truck >= 0) {
					TravelingVehicle tv = new TravelingVehicle();
					tv.setTime(getDistance(input.getRide().get(ride)));
					tv.setRide(ride);
					travelingVehicles.set(truck, tv);
					pendingRides.set(ride, false);
					System.out.println("Truck " + truck + " starting ride of distance " + travelingVehicles.get(truck));
				}
			}
		}
		
		// Convert to output format
		for (Integer truck : outputCache.keySet()) {
			Output line = new Output();
			line.setVehicle(outputCache.get(truck).size());
			line.setRides(outputCache.get(truck));
			output.add(line);
		}
		
		// Counting uncovered rides
		int notCovered = 0;
		for (int i = 0; i < input.getRides(); i++) {
			if (pendingRides.get(i)) {
				notCovered++;
			}
		}
		System.out.println("Not covered rides: " + notCovered);
		
		return output;
		
	}
	
	/**
	 * Build an ordered list (prioritizing first) of pending rides
	 * 
	 * @param pendingRides
	 * @param rides
	 * @param step
	 * @param input
	 * @return
	 */
	private static List<Integer> getOrderedPendingRides(List<Boolean> pendingRides, List<InputLine> rides, Integer step, Input input) {
		
		Map<Integer, Integer> pendingScores = new TreeMap<Integer, Integer>();
		
		// Order by distance: the nearer the first
		for (int i = 0; i < pendingRides.size(); i++) {
			if (pendingRides.get(i)) {
				Integer distance = getDistance(rides.get(i));
				pendingScores.put(i, distance);
			}
		}
		// Top priority for bonus: prioritizing starting time at current step
		for (Integer ride : pendingScores.keySet()) {
			if (input.getRide().get(ride).getStartingTime() == step) {
				pendingScores.put(ride, -1);
			} 
		}
		// High priority for finishing in time
		for (Integer ride : pendingScores.keySet()) {
			Integer distance = pendingScores.get(ride);
			if (input.getRide().get(ride).getFinishingTime() <= (distance + step)) {
				pendingScores.put(ride, 0);
			}
		}		
		
		return new ArrayList<Integer>(pendingScores.keySet());
	}
	
	/**
	 * Calculate travel distance
	 * @param ride
	 * @return
	 */
	private static Integer getDistance(InputLine ride) {
		return Math.abs(ride.getStartingCol() - ride.getEndingCol()) +
			   Math.abs(ride.getStartingRow() - ride.getEndingRow());
	}
	
	/**
	 * Find first available truck
	 * @param travelingVehicles
	 * @return
	 */
	private static Integer availableTruck(List<TravelingVehicle> travelingVehicles) {
		for (int i = 0; i < travelingVehicles.size(); i++) {
			if (travelingVehicles.get(i).getTime() == 0) return i;
		}
		return -1;
	}
	
	/**
	 * Update travel distances and detect trucks finishing rides
	 * @param travelingVehicles
	 * @param pendingRides
	 * @param result
	 */
	private static void nextStep(List<TravelingVehicle> travelingVehicles, List<Boolean> pendingRides, Map<Integer, List<Integer>> result) {
		
		for (int i = 0; i < travelingVehicles.size(); i++) {
			if (travelingVehicles.get(i).getTime() > 0) {
			    travelingVehicles.get(i).setTime(travelingVehicles.get(i).getTime() - 1);
			}
			if (travelingVehicles.get(i).getTime() == 0 && travelingVehicles.get(i).getRide() != -1) {
				if (result.get(i) == null) {
					result.put(i, new ArrayList<Integer>());
				}
				System.out.println("Vehicle " + i + " finished travel " + travelingVehicles.get(i).getRide());
				result.get(i).add(travelingVehicles.get(i).getRide());
				travelingVehicles.get(i).setRide(-1);
			}
		}
		
	}
	
	
}
