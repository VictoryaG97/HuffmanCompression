import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception{
        ArgParser parser = new ArgParser(args);

        String input_file = parser.cmd.getOptionValue("f");
        Integer max_threads = Integer.parseInt(parser.cmd.getOptionValue("t"));
        Boolean quiet = false;
        if (parser.cmd.hasOption("q")) {
            quiet = true;
        }

        BuildFreqTable bft = new BuildFreqTable(input_file, max_threads);
        bft.runThreads();
        // HashMap<Character, Integer> frequency_map = new HashMap<>();
        // frequency_map = bft.getFrequencyMap();

        // System.out.println(frequency_map);
    }
}