package es.keensoft.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import es.keensoft.bean.Input;
import es.keensoft.bean.InputLine;
import es.keensoft.bean.Output;

/**
 * Translate functions for Java Beans and Files 
 *
 */
public class Translator {
	
	/**
	 * Translate input file into Java Beans
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Input getInput(File file) throws Exception {
		Input input = new Input();
		boolean firstLine = true;
		List<InputLine> rides = new ArrayList<InputLine>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
    	            String[] numbers = line.split(" ");
		    	    if (firstLine) {
	    	        	    input.setRows(Integer.parseInt(numbers[0]));
	    	        	    input.setCols(Integer.parseInt(numbers[1]));
	    	        	    input.setVehicles(Integer.parseInt(numbers[2]));
	    	        	    input.setRides(Integer.parseInt(numbers[3]));
	    	        	    input.setBonus(Integer.parseInt(numbers[4]));
	    	        	    input.setSteps(Integer.parseInt(numbers[5]));
	    	        	    firstLine = false;
		    	    } else {
		    	    	    InputLine ride = new InputLine();
		    	    	    ride.setStartingRow(Integer.parseInt(numbers[0]));
		    	    	    ride.setStartingCol(Integer.parseInt(numbers[1]));
		    	    	    ride.setEndingRow(Integer.parseInt(numbers[2]));
		    	    	    ride.setEndingCol(Integer.parseInt(numbers[3]));
		    	    	    ride.setStartingTime(Integer.parseInt(numbers[4]));
		    	    	    ride.setFinishingTime(Integer.parseInt(numbers[5]));
		    	    	    rides.add(ride);
		    	    }
		    }
		}
		input.setRide(rides);
		return input;
	}
	
	/**
	 * Translate Java beans into output file
	 * @param outputs
	 * @param outFile
	 * @param vehicules
	 * @throws Exception
	 */
	public static void writeOutput(List<Output> outputs, File outFile, Integer vehicules) throws Exception {
		boolean first = true;
		int lines = 0;
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)))) {
			for (Output output : outputs) {
				if (first) {
					writer.write("" + output.getVehicle());
					first = false;
				} else {
					writer.write(System.lineSeparator() + output.getVehicle());
				}
				for (Integer ride : output.getRides()) {
					writer.write(" " + ride);
				}
				lines++;
			}
			while (lines < vehicules) {
				writer.write(System.lineSeparator() + "0");
				lines++;
			}
		}
	}

}
