import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class SingleThread implements Runnable{

	final static Logger logger = Logger.getLogger(SingleThread.class.getName());
	
	private ArrayList<String> data;
	private Integer thread_name;
	
	ConcurrentHashMap<Character, Integer> frequency_map;
	
	public SingleThread(ConcurrentHashMap<Character, Integer> main_map, 
						ArrayList<String> part_data, Integer thread_index) {
		frequency_map = new ConcurrentHashMap<Character, Integer>();
		
		data = part_data;
		thread_name = thread_index;
	}
	
	private void buildFrequencyMap() {
		for (String s : data) {
			for (Character c : s.toCharArray()) {
				if (!frequency_map.containsKey(c)) {
					frequency_map.put(c, 1);
				} else {
					Integer curr_count = frequency_map.get(c);
					frequency_map.put(c, curr_count + 1);
				}
			}
		}
	}
	
	public void run() {
		long start_time = System.currentTimeMillis();
		
		String logger_message = Thread.currentThread().getName() + " running...";
		logger.info(logger_message);
		
		buildFrequencyMap();
		System.out.println(Thread.currentThread().getName() + " " + frequency_map);
		
		long end_time = System.currentTimeMillis();
        long execution_time = end_time - start_time;
		logger_message = Thread.currentThread().getName() + " exiting."; 
        logger.info(execution_time + logger_message);
        
        
        data.clear();
	}
}
