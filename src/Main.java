import java.io.*;
import java.util.*;

/*      input format
            1 5     1st column == arrival/ready time
            2 3     2nd column == execution time
            3 1
*/

public class Main {
    static String fileName = "C:\\khaled\\CU\\Winter 2024\\Coen 346\\Job_Scheduler_Java\\src\\input.txt";

    public static void main(String[] args)
    {
        PrintStream originalOut = System.out;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(".\\src\\output.txt");
            PrintStream newOut = new PrintStream(new BufferedOutputStream(fileOutputStream), true);
            System.setOut(newOut);

            System.out.println("Running process scheduler!");

            // Read input file to create Process objects
            //starts at index 0
            List<Process> wProcesses = readProcessesFromFile(fileName);

            // Initialize the Scheduler and add processes to it
            Scheduler scheduler = new Scheduler();

            for(int i = 0; i < wProcesses.size(); i++)
            {
                scheduler.addProcess(wProcesses.get(i));
            }

            // Start the Scheduler
            scheduler.start();
            try {
                scheduler.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

            fileOutputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private static List<Process> readProcessesFromFile(String fileName)
    {
        List<Process> processes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int id = 1;
            while ((line = br.readLine()) != null)
            {
                String[] parts = line.split("\\s+");
                if (parts.length == 2)
                {
                    int arrivalTime = Integer.parseInt(parts[0]);
                    int burstTime = Integer.parseInt(parts[1]);
                    processes.add(new Process(id++, arrivalTime, burstTime));
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
            e.printStackTrace();
        }
        return processes;
    }
}
