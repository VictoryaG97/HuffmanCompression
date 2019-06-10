import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.*;
import java.util.ArrayList;


public class MainThread {

	final static Logger logger = Logger.getLogger(MainThread.class.getName());
	
	private ArrayList<String> file_data;
	private Integer threads_count;
	private int buffers_per_thread = 0;
    private Thread jobs[];
    private Boolean is_quiet;
	
	public MainThread(String filepath, Integer tasks, Boolean set_quiet) throws IOException {
		this.file_data = new ArrayList<String>();
		this.threads_count = tasks;
		this.is_quiet = set_quiet;
		
		if (this.is_quiet) {
			MainThread.logger.setLevel(Level.OFF);
		}
		this.readFile(filepath);
	}
	
	private void setThreadParams(Integer file_size) {
		if (this.threads_count > 1) {
			this.buffers_per_thread = (file_size / (this.threads_count-1));
		} else {
			this.buffers_per_thread = 0;
		}
		jobs = new Thread[this.threads_count];
	}
	
	private void readFile(String file_path) throws IOException {
        logger.info("Started reading the input file...");
        File input_file = new File(file_path);

        if (!input_file.exists()) {
            System.out.println("File not found!");
            System.exit(0);
        }

        BufferedReader file_reader = new BufferedReader(new FileReader(input_file));

        String current_line;
        while ((current_line = file_reader.readLine()) != null) {
            this.file_data.add(current_line);
        }
        this.setThreadParams(file_data.size());
        file_reader.close();
        logger.info("Finished reading the input file.");
    }
	
	private ArrayList<String> readPartOfFile(Integer position, Integer buff_count) {
        ArrayList<String> part = new ArrayList<>();

        int buffer_index;
        for (buffer_index = position; buffer_index < position + buff_count; buffer_index++) {
            part.add(this.file_data.get(buffer_index));
        }
        return part;
    }
	
	private void createThread(Integer position, Integer buff_count, Integer thread_index) {
		ArrayList<String> part_data = readPartOfFile(position, buff_count);
		
		SingleThread new_thread = new SingleThread(part_data, thread_index, this.is_quiet);
		
		Thread tr = new Thread(new_thread);
		jobs[thread_index] = tr;
	}
	
	public void runThreads() throws InterruptedException {
		Integer position = 0;
		logger.info("Starting reading the file to compress...");
		
		// creating given count - 1,
        // because we are not sure that we will need the last one
        for (int i = 0; i < (this.threads_count - 1); i++) {
            this.createThread(position, this.buffers_per_thread,i);

            String log_message = "Thread-" + (i + 1) + " started";
            logger.info(log_message);

            position += this.buffers_per_thread;
        }
        
        // check if we need the last thread
        Integer last_thread_position = this.buffers_per_thread * (this.threads_count - 1);
        if (this.file_data.size() - last_thread_position > 0) {
        	this.createThread(last_thread_position,
        			(this.file_data.size() - last_thread_position),
        			this.threads_count-1);
        	
        	String log_message = "Thread-" + (threads_count) + " started";
            logger.info(log_message);
        } else {
        	// the last thread is not needed
        	threads_count--;
        }
        
        this.file_data.clear();
        
        long start_time = System.currentTimeMillis();
        // start threads
        for (int i = 0; i < this.threads_count; i++) {
            this.jobs[i].start();
        }
        
        // run threads
        for (int i = 0; i < this.threads_count; i++) {
            this.jobs[i].join();
        }
        
        long end_time = System.currentTimeMillis();
        long execution_time = end_time - start_time;
        
        System.out.println("Threads used in current run: " + threads_count);
        System.out.println("Total execution time for current run (millis): " + execution_time);
	}
}
