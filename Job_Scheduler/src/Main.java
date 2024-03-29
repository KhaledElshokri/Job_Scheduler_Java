import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*      input format
            1 5     1st column == arrival/ready time
            2 3     2nd column == execution time
            3 1
*/

public class Main {
    static String fileName = "C:\\khaled\\CU\\Winter 2024\\Coen 346\\Job_Scheduler_Java\\Job_Scheduler\\src\\input.txt";

    public static void main(String[] args)
    {
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

        // TODO: Output the results to output.txt
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
