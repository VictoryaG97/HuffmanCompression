import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.*;
import java.util.Iterator;
import java.util.Map;

//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.core.*;


public class SingleThread implements Runnable{

	final static Logger logger = Logger.getLogger(SingleThread.class.getName());
	
	private ArrayList<String> data;
	private String thread_name;
	
	HashMap<Character, Integer> frequency_map;
	
	public SingleThread(ArrayList<String> part_data, Integer thread_index, Boolean set_quiet) {
		this.frequency_map = new HashMap<Character, Integer>();
		
		this.data = part_data;
		this.thread_name = "thread" + thread_index.toString() + ".txt";
		
		if (set_quiet) {
			SingleThread.logger.setLevel(Level.OFF);
		}
	}
	
	private void buildFrequencyMap() {
		for (String s : this.data) {
			for (Character c : s.toCharArray()) {
				if (!this.frequency_map.containsKey(c)) {
					this.frequency_map.put(c, 1);
				} else {
					Integer curr_count = this.frequency_map.get(c);
					this.frequency_map.put(c, curr_count + 1);
				}
			}
		}
	}
	
	private void writeToFile() throws IOException{
//		TODO: write frequency tables to .json files
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.writeValue(new File(this.thread_name), this.frequency_map);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.thread_name));
		Iterator<Map.Entry<Character, Integer>> iter = this.frequency_map.entrySet().iterator();
		
		Integer count = 0;
		while(iter.hasNext() && count < this.frequency_map.size()) {
			Map.Entry<Character, Integer> pair = iter.next();
			writer.write(pair.getKey() + "=" + pair.getValue() + "\n");
		}
		writer.close();
	}
	
	public void run() {
		long start_time = System.currentTimeMillis();
		
		String logger_message = Thread.currentThread().getName() + " running...";
		logger.info(logger_message);
		
		this.buildFrequencyMap();
		try {
			this.writeToFile();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		long end_time = System.currentTimeMillis();
        long execution_time = end_time - start_time;
		logger_message = Thread.currentThread().getName() + " exiting."; 
        logger.info(execution_time + logger_message);
        
        this.data.clear();
	}
}
