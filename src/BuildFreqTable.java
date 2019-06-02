import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class BuildFreqTable {

    final static Logger logger = Logger.getLogger(BuildFreqTable.class.getName());

    private ArrayList<String> file_data;
    private int threads_count;
    private int buffers_per_thread = 0;
    private Thread jobs[];
    private HashMap<Character, Integer> frequency_map;

    public BuildFreqTable(String filepath, int tasks_count) throws IOException{
        file_data = new ArrayList<>();
        frequency_map = new HashMap<>();
        threads_count = tasks_count;

        readFile(filepath);
    }

    private void setThreadParams(int file_lines, int threads_count) {
        if (threads_count > 1) {
            buffers_per_thread = (file_lines / threads_count);
        }
        jobs = new Thread[threads_count];
    }

    private void readFile(String filepath) throws IOException {
        logger.info("Started reading the input file...");
        File input_file = new File(filepath);

        BufferedReader file_reader = new BufferedReader(new FileReader(input_file));

        String current_line;
        while ((current_line = file_reader.readLine()) != null) {
            file_data.add(current_line);
        }
        setThreadParams(file_data.size(), threads_count);
        logger.info("Finished reading the input file.");
    }

    private ArrayList<String> readPartOfFile(int position, int size_to_read) {
        ArrayList<String> part = new ArrayList<>();

        int buffer_index;
        System.out.println("Position" +position);
        System.out.println("Size to read " + size_to_read);
        for (buffer_index = position; buffer_index < position + size_to_read; buffer_index++) {
            part.add(file_data.get(buffer_index));
        }
        return part;
    }

    private void createThread(int position, int size_to_read, int thread_index) {
        ArrayList<String> part_data;
        part_data = readPartOfFile(position, size_to_read);

        MainThread main_thread = new MainThread(part_data, thread_index);

        Thread tr = new Thread(main_thread);
        jobs[thread_index] = tr;
    }

    public void runThreads() throws InterruptedException {
        int position = 0;
        logger.info("Starting reading the file to compress...");

        for (int i = 0; i < threads_count; i++) {
            createThread(position, buffers_per_thread, i);

            String log_message = "Thread " + (i + 1) + " started";
            logger.info(log_message);

            position += buffers_per_thread;
        }

        long start_time = System.currentTimeMillis();
        // start threads
        for (int i = 0; i < threads_count; i++) {
            jobs[i].start();
        }

        for (int i = 0; i < threads_count; i++) {
            jobs[i].join();
        }

        long end_time = System.currentTimeMillis();
        long execution_time = end_time - start_time;

        System.out.println("Threads used in current run: " + threads_count);
        System.out.println("Total execution time for current run (millis): " + execution_time);
    }
}