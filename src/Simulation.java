import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Simulation {
    private static List<Process> processes = new ArrayList<>();
    private static List<ProcessThread> procThreads = new ArrayList<>();


    public static void main(String[] args) {
        System.out.println("Hello, World!");
        
        loadProcessData("src/processes.txt");

        /* User Access the Menu */
        boolean exitFlag = false;
        Scanner scanner = new Scanner(System.in);

        while (!exitFlag) {

            // Display the Menu
            System.out.print(menuToStr());

            // Take the User's selection and execute it
            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        fcfs();
                        break;
                    case 2:
                        sjf();
                        break;
                    case 3:
                        rr();
                        break;
                    case 4:
                        ps();
                        break;
                    case 9:
                        exitFlag = true;
                        System.out.println("Exiting simulation");
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        break;
                }
            }
            // User selected invalid input 
            else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        scanner.close();
    }

    public static void loadProcessData(String fileName) {
        try {
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);

            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine(); 
            }

            while (fileScanner.hasNext()) {
                int pid = fileScanner.nextInt();
                int arrivalTime = fileScanner.nextInt();
                int burstTime = fileScanner.nextInt();
                int priority = fileScanner.nextInt();
                
                processes.add(new Process(pid, arrivalTime, burstTime, priority));
                procThreads.add(new ProcessThread(pid, burstTime));
            }

            fileScanner.close();

            // Display all the Loaded Processes from processes.txt
            System.out.println("Loaded Processes:");
            for (Process p : processes) {
                System.out.println(p);
            }

            // Same thing, but with ProcessThreads
            System.out.println("Loaded Processes w/ ProcessThread: ");
            for (ProcessThread pt : procThreads){
                System.out.println(pt);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    public static String menuToStr() {
        return "Welcome to Process Scheduling Simulation 2!\n"
                + "Please Enjoy the Synchronization problem simulation of:\n"
                + "[1]:\tDining Philosophers || First Come, First Served (FCFS)\n"
                + "[2]:\tReaders-Writers || Shortest Job First (SJF)\n"
                + "[3]:\tProducer-Consumer || Round Robin (RR)\n"
                + "[4]:\tN/A || Priority Scheduling\n"
                + "[9]:\tExit Simulation\n"
                + "User Input:\t";
    }

    public static void fcfs() {
        System.out.println("\nFirst Come, First Served (FCFS)\n");

        // Sorts the processes by arrivalTime to get the "first comers" in order
        processes.sort((p1, p2) -> Integer.compare(p1.arrivalTime, p2.arrivalTime));

        //declare variables
        int currentTime = 0;
        int totalTAT = 0;
        int totalWT = 0;
        List<String> ganttChart = new ArrayList<>();
        List<Integer> timeStamps = new ArrayList<>();

        // the FCFS code to calculate and print
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s\n","PID", "Arrival", "Burst", "Completion", "TAT", "WT");
        for (Process p : processes) {
            //to handle gaps in arrival time e.g p1 finishes at 4 but p2 doesnt start till 6
            //current time would hold 4 since that is when last process finished but newest process arrives
            // at 6 so we must account for the 2 seconds passed 
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;  
            }

            //calculate variables
            int startTime = currentTime;
            int completionTime = currentTime + p.burstTime;
            int turnaroundTime = completionTime - p.arrivalTime;
            int waitingTime = turnaroundTime - p.burstTime;

            totalTAT += turnaroundTime;
            totalWT += waitingTime;

            //track details for gantt chart printing
            ganttChart.add("P" + p.pid);
            timeStamps.add(startTime);

            //print details of process handled in FCSC
            System.out.printf("%-5d %-10d %-10d %-10d %-10d %-10d\n", p.pid, p.arrivalTime, p.burstTime, completionTime, turnaroundTime, waitingTime);

            //current time is when the process completes in FCFS unless there is a gap as handled above
            currentTime = completionTime;
        }
        //add final time for gantt chart
        timeStamps.add(currentTime);

        //needed variables calculated and printed
        int n = processes.size();
        System.out.println("\nAverage Turnaround Time: " + (double) totalTAT / n);
        System.out.println("Average Waiting Time: " + (double) totalWT / n);

        //Gantt Chart printing
        System.out.println("\nGantt Chart:");
        System.out.print(" ");
        System.out.println();

        System.out.print("|");
        for (String p : ganttChart) {
            System.out.print("  " + p + "  |");
        }
        System.out.println();


        for (int i = 0; i < timeStamps.size(); i++) {
            System.out.printf("%-6d ", timeStamps.get(i));
        }
        System.out.println();
        System.out.println();
    }
    

    public static void sjf() {
        System.out.println("\nShortest Job First (SJF)\n");

        // Sorts the processes by arrivalTime to get the "first comers" in order
        processes.sort((p1, p2) -> Integer.compare(p1.burstTime, p2.burstTime));

        //declare variables
        int currentTime = 0;
        int totalTAT = 0;
        int totalWT = 0;
        List<String> ganttChart = new ArrayList<>();
        List<Integer> timeStamps = new ArrayList<>();

        // the FCFS code to calculate and print
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s\n","PID", "Arrival", "Burst", "Completion", "TAT", "WT");
        for (Process p : processes) {
            //to handle gaps in arrival time e.g p1 finishes at 4 but p2 doesnt start till 6
            //current time would hold 4 since that is when last process finished but newest process arrives
            // at 6 so we must account for the 2 seconds passed 
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;  
            }

            //calculate variables
            int startTime = currentTime;
            int completionTime = currentTime + p.burstTime;
            int turnaroundTime = completionTime - p.arrivalTime;
            int waitingTime = turnaroundTime - p.burstTime;

            totalTAT += turnaroundTime;
            totalWT += waitingTime;

            //track details for gantt chart printing
            ganttChart.add("P" + p.pid);
            timeStamps.add(startTime);

            //print details of process handled in FCSC
            System.out.printf("%-5d %-10d %-10d %-10d %-10d %-10d\n", p.pid, p.arrivalTime, p.burstTime, completionTime, turnaroundTime, waitingTime);

            //current time is when the process completes in FCFS unless there is a gap as handled above
            currentTime = completionTime;
        }
        //add final time for gantt chart
        timeStamps.add(currentTime);

        //needed variables calculated and printed
        int n = processes.size();
        System.out.println("\nAverage Turnaround Time: " + (double) totalTAT / n);
        System.out.println("Average Waiting Time: " + (double) totalWT / n);

        //Gantt Chart printing
        System.out.println("\nGantt Chart:");
        System.out.print(" ");
        System.out.println();

        System.out.print("|");
        for (String p : ganttChart) {
            System.out.print("  " + p + "  |");
        }
        System.out.println();


        for (int i = 0; i < timeStamps.size(); i++) {
            System.out.printf("%-6d ", timeStamps.get(i));
        }
        System.out.println();
        System.out.println();
    }

    public static void rr() {
        System.out.println("RR");
    }

    public static void ps() {
        System.out.println("PS");
    }
}
