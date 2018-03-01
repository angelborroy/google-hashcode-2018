package es.keensoft.main;

import java.io.File;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import es.keensoft.bean.Input;
import es.keensoft.bean.Output;
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
		List<Output> outputs = StepEngine.start(in);
				
		// Write output file
		File outFile = new File(ps.getProperty("fileOut").toString());
		Translator.writeOutput(outputs, outFile, in.getVehicles());

	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
