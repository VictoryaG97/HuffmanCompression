import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class MainThread implements Runnable {

    final static Logger logger = Logger.getLogger(MainThread.class.getName());

    // private Thread t;
    private ArrayList<String> data;
    private int thread_name;
    private HashMap<Character, Integer> frequency_map;    

    public MainThread(ArrayList<String> part_data, int thread_index) {
        data = part_data;
        thread_name = thread_index;
        frequency_map = new HashMap<>();
    }

    private HashMap<Character, Integer> addFrequenciesFromBuffer(char[] buffer, HashMap<Character, Integer> map) {
		
		for (char character : buffer) {
			if(!map.containsKey(character)){
			    map.put(character, 1);
			}
			else{
				int curr_count = map.get(character);
				map.put(character, curr_count+1);
			}
		}
		return map;
	}

    private void buildFrequencyMap() {
        for (String buffer : data) {
            frequency_map = addFrequenciesFromBuffer(buffer.toCharArray(), frequency_map);
        }
    }

    public HashMap<Character, Integer>getFrequencyMap() {
        return frequency_map;
    }

    @Override
    public void run() {
        String logger_message = Thread.currentThread().getName() + " running..."; 
        logger.info(logger_message);

        buildFrequencyMap();
        System.out.println(getFrequencyMap());

        logger_message = Thread.currentThread().getName() + " exiting."; 
        logger.info(logger_message);
    }
}