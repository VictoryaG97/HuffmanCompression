
public class Main {

	public static void main(String[] args) throws Exception{
        ArgParser parser = new ArgParser(args);

        String input_file = parser.cmd.getOptionValue("f");
        Integer max_threads = Integer.parseInt(parser.cmd.getOptionValue("t"));
        Boolean quiet = false;
        if (parser.cmd.hasOption("q")) {
            quiet = true;
        }
        
        MainThread mt = new MainThread(input_file, max_threads);
        mt.runThreads();
    }
}
