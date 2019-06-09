import org.apache.commons.cli.*;


public class ArgParser {
	
	Options options;
    CommandLine cmd;

    public ArgParser(String[] args) {
        options = new Options();
        setOptions();
        parseOptions(args);
    }

    public void setOptions() {
        // Define arguments
        Option input_file = new Option("f", "file", true, "input file path");
        input_file.setRequired(true);
        options.addOption(input_file);

        Option max_threads = new Option("t", "tasks", true, "max tasks count");
        max_threads.setRequired(true);
        options.addOption(max_threads);

        Option quiet = new Option("q", "quiet", false, "set quiet mode");
        options.addOption(quiet);
    }

    public void parseOptions(String[] args) {

        // Define parser
        CommandLineParser parser = new DefaultParser();
        // Define helper
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse(this.options, args);
            
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
    }
}
