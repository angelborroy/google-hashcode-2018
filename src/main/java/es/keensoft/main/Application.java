package es.keensoft.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import es.keensoft.bean.Input;
import es.keensoft.bean.Output;
import es.keensoft.main.engine.BasicStepEngine;
import es.keensoft.main.engine.BestChoiceStepEngine;
import es.keensoft.main.engine.ScoringStepEngine;
import es.keensoft.util.Translator;

/**
 * Main class to be launched from command line
 */
@SpringBootApplication
public class Application implements CommandLineRunner {
	
	@Override
	public void run(String... args) throws Exception {

		// Read input file
		PropertySource<?> ps = new SimpleCommandLinePropertySource(args);
		File inFile = new File(ps.getProperty("fileIn").toString());
		Input in = Translator.getInput(inFile);
		
		// Start step engine
		List<Output> outputs = new ArrayList<Output>();
		String engine = ps.getProperty("engine").toString();
		System.out.println("Starting simulation with " + engine + " engine");
		switch(engine) {
		    case "BASIC":
		    	    outputs = BasicStepEngine.start(in);
		    	    break;
		    case "SCORING":
		    	    outputs = ScoringStepEngine.start(in);
		    	    break;
		    case "BEST_CHOICE":
		    	    outputs = BestChoiceStepEngine.start(in);
		    	    break;
			default:
				System.out.println("Engine of type " + engine + " is not available");
				System.exit(-1);
		}
		
		// Write output file
		File outFile = new File(ps.getProperty("fileOut").toString());
		Translator.writeOutput(outputs, outFile, in.getVehicles());

	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
