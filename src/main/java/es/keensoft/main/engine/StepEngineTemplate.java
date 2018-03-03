package es.keensoft.main.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.keensoft.bean.Input;
import es.keensoft.bean.InputLine;
import es.keensoft.bean.Output;
import es.keensoft.bean.TravelingVehicle;

public class StepEngineTemplate {
	
	// Static shared variables make it easier coding static methods
	
	// Beans translated from input file
	static Input input;

	// Beans to be translated to output file
	static List<Output> output;
	
	// Data structure to store output data: (Truck ID, List of Rides) 
	static Map<Integer, List<Integer>> outputCache;
	
	// Traffic control: truck position, ride assignment and time to deliver
	static List<TravelingVehicle> travelingVehicles;
	
	// Rides not delivered yet
	static List<Boolean> pendingRides;
	
	// Bonus 
	static Integer startingInTime;
	static Integer deliveredInTime;
	
	/**
	 * Initial data
	 */
	protected static void setDataDefaultValues() {
		
		outputCache = new TreeMap<Integer, List<Integer>>();
		output = new ArrayList<Output>();
		
		// Initialize traveling vehicles
		travelingVehicles = new ArrayList<TravelingVehicle>();
		for (int i = 0; i < input.getVehicles(); i++) {
			
			TravelingVehicle tv = new TravelingVehicle();
			// Time to finish current ride
			tv.setTime(0);
			// Current ride
			tv.setRide(-1);
			// Current position (col, row)
			tv.setCol(0);
			tv.setRow(0);
			
			travelingVehicles.add(tv);
			
		}
		
		// Initialize pending rides
		pendingRides = new ArrayList<Boolean>();
		for (int i = 0; i < input.getRides(); i++) {
			pendingRides.add(true);
		}
		
		// Bonus counters
		startingInTime = 0;
		deliveredInTime = 0;
		
	}
		
	/**
	 * Calculate travel distance
	 * @param ride
	 * @return
	 */
	protected static Integer getRawDistance(InputLine ride) {
		return Math.abs(ride.getStartingCol() - ride.getEndingCol()) +
			   Math.abs(ride.getStartingRow() - ride.getEndingRow());
	}
	
	/**
	 * Calculate travel distance including current truck position
	 * @param ride
	 * @param truck
	 * @return
	 */
	protected static Integer getDistance(InputLine ride, TravelingVehicle truck) {
		return getRawDistance(ride) +
			Math.abs(truck.getCol() - ride.getStartingCol()) +
			Math.abs(truck.getRow() - ride.getStartingRow());
	}
	
	/**
	 * Truck starts a ride from initial position
	 * @param ride
	 * @param truck
	 */
	protected static void startRide(Integer ride, Integer truck) {
		
		// Assign ride to a truck and start driving 
		TravelingVehicle tv = new TravelingVehicle();
		tv.setTime(getDistance(input.getRide().get(ride), travelingVehicles.get(truck)));
		tv.setRide(ride);
		// Initial position
		tv.setCol(input.getRide().get(ride).getStartingCol());
		tv.setRow(input.getRide().get(ride).getStartingRow());
		// End position
		tv.setTargetCol(input.getRide().get(ride).getEndingCol());
		tv.setTargetRow(input.getRide().get(ride).getEndingRow());
		travelingVehicles.set(truck, tv);
		System.out.println("Truck " + truck + " starting ride " + travelingVehicles.get(truck).getRide() + " of distance " + travelingVehicles.get(truck).getTime());
		
	}
	
	/**
	 * Update travel distances and detect trucks finishing rides
	 */
	protected static void nextStep() {
		
		for (int i = 0; i < travelingVehicles.size(); i++) {
			
			// Move traveling trucks to next position 
			if (travelingVehicles.get(i).getTime() > 0) {
			    travelingVehicles.get(i).setTime(travelingVehicles.get(i).getTime() - 1);
			}
			
			// Release trucks delivering ride in this step
			if (travelingVehicles.get(i).getTime() == 0 && travelingVehicles.get(i).getRide() != -1) {

				// Store ride delivered 
				if (outputCache.get(i) == null) {
					outputCache.put(i, new LinkedList<Integer>());
				}
				System.out.println("Truck " + i + " finished travel " + travelingVehicles.get(i).getRide());
				outputCache.get(i).add(travelingVehicles.get(i).getRide());
				
				// Release truck for next ride and update to current position 
				travelingVehicles.get(i).setRide(-1);
				travelingVehicles.get(i).setCol(travelingVehicles.get(i).getTargetCol());
				travelingVehicles.get(i).setRow(travelingVehicles.get(i).getTargetRow());
				
			}
		}
		
	}
	
	/**
	 * Copy cache data to output beans
	 */
	protected static void prepareOutputFormat() {
		for (Integer truck : outputCache.keySet()) {
			Output line = new Output();
			line.setVehicle(outputCache.get(truck).size());
			line.setRides(outputCache.get(truck));
			output.add(line);
		}
	}
	
	/**
	 * Print uncovered rides number
	 */
	protected static void countUncoveredRides() {
		int notCovered = 0;
		for (int ride = 0; ride < input.getRides(); ride++) {
			if (pendingRides.get(ride)) {
				notCovered++;
			}
		}
		System.out.println("Not covered rides: " + notCovered);
	}
	
}
