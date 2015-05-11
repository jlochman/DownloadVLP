package commandLineArgs;

import com.beust.jcommander.JCommander;
import commandLineArgs.commandArgs;

public class commandLineArgsService {

	public commandArgs parseAndSaveArgs(String[] args) {
		 commandArgs commandArgs = new commandArgs();
		 JCommander jc = new JCommander(commandArgs, args);
		 if(commandArgs.isHelp()) {
			 jc.usage();
			 return null;
		 } 
		 return commandArgs; 
	}

}
