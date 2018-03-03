package es.keensoft.main.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import es.keensoft.bean.Input;
import es.keensoft.bean.InputLine;
import es.keensoft.bean.Output;
import es.keensoft.bean.TravelingVehicle;

/**
 * Engine to control step-by-step truck movements
 * This class was developed during the contest, probably it includes different issues
 *
 */
public class ContestStepEngine {
	
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
			tv.setCol(0);
			tv.setRow(0);
			travelingVehicles.add(tv);
		}
		
		// Initialize pending rides
		List<Boolean> pendingRides = new ArrayList<Boolean>();
		for (int i = 0; i < input.getRides(); i++) {
			pendingRides.add(true);
		}
		
		// Steps looping
		for (int step = 0; step < steps; step++) {
			
			System.out.println("Step " + step);
			
			nextStep(travelingVehicles, pendingRides, outputCache);
			
			List<Integer> orderedPendingRides = getOrderedPendingRides(pendingRides, input.getRide(), step, input);
			
			// Start ride for nearer available truck
			for (int j = 0; j < 	orderedPendingRides.size(); j++) {
				Integer ride = orderedPendingRides.get(j);
				Integer truck = availableTruck(travelingVehicles, input.getRide().get(ride));
				Integer earlierStarting = input.getRide().get(ride).getStartingTime();
				if (step >= earlierStarting && truck >= 0) {
					TravelingVehicle tv = new TravelingVehicle();
					tv.setTime(
							getDistance(input.getRide().get(ride)) +
							Math.abs(travelingVehicles.get(truck).getCol() - input.getRide().get(ride).getStartingCol()) +
							Math.abs(travelingVehicles.get(truck).getRow() - input.getRide().get(ride).getStartingRow()));
					tv.setRide(ride);
					tv.setCol(input.getRide().get(ride).getStartingCol());
					tv.setRow(input.getRide().get(ride).getStartingRow());
					tv.setTargetCol(input.getRide().get(ride).getEndingCol());
					tv.setTargetRow(input.getRide().get(ride).getEndingRow());
					travelingVehicles.set(truck, tv);
					pendingRides.set(ride, false);
					System.out.println("Truck " + truck + " starting ride " + travelingVehicles.get(truck).getRide() + " of distance " + travelingVehicles.get(truck).getTime());
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
		for (int ride = 0; ride < input.getRides(); ride++) {
			if (pendingRides.get(ride)) {
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
			if (input.getRide().get(ride).getStartingTime() <= step) {
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
		
		// Order by criteria
		Set<Map.Entry<Integer, Integer>> orderedPendingScores = entriesSortedByValues(pendingScores);
		List<Integer> result = new ArrayList<Integer>();
		for (Entry<Integer, Integer> entry : orderedPendingScores) {
			result.add(entry.getKey());
		}
		return result;
		
	}
	
	static <K,V extends Comparable<? super V>>
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
	    SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
	        new Comparator<Map.Entry<K,V>>() {
	            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
	                int res = e1.getValue().compareTo(e2.getValue());
	                return res != 0 ? res : 1;
	            }
	        }
	    );
	    sortedEntries.addAll(map.entrySet());
	    return sortedEntries;
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
	 * Find nearer available truck
	 * @param travelingVehicles
	 * @return
	 */
	private static Integer availableTruck(List<TravelingVehicle> travelingVehicles, InputLine ride) {
		Map<Integer, Integer> distanceTruck = new TreeMap<Integer, Integer>(); 
		for (int i = 0; i < travelingVehicles.size(); i++) {
			if (travelingVehicles.get(i).getTime() == 0) {
				Integer distanceToStarting = 
						Math.abs(ride.getStartingCol() - travelingVehicles.get(i).getCol()) +
						Math.abs(ride.getStartingRow() - travelingVehicles.get(i).getRow());
				distanceTruck.put(distanceToStarting, i);
			}
		}
		if (!distanceTruck.isEmpty()) {
			return distanceTruck.values().iterator().next();
		} else {
			return -1;
		}
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
					result.put(i, new LinkedList<Integer>());
				}
				System.out.println("Truck " + i + " finished travel " + travelingVehicles.get(i).getRide());
				result.get(i).add(travelingVehicles.get(i).getRide());
				travelingVehicles.get(i).setRide(-1);
				travelingVehicles.get(i).setCol(travelingVehicles.get(i).getTargetCol());
				travelingVehicles.get(i).setRow(travelingVehicles.get(i).getTargetRow());
			}
		}
		
	}
	
}
