import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int priority;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PID: " + pid + ", Arrival Time: " + arrivalTime + ", Burst Time: " + burstTime + ", Priority: " + priority;
    }
}

public class Simulation {
    private static List<Process> processes = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        
        loadProcessData("src/processes.txt");

        /* User Access the Menu */
        boolean exitFlag = false;
        Scanner scanner = new Scanner(System.in);

        while (!exitFlag) {
            System.out.print(menuToStr());

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
            } else {
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
            }

            fileScanner.close();

            System.out.println("Loaded Processes:");
            for (Process p : processes) {
                System.out.println(p);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
    }

    public static String menuToStr() {
        return "Welcome to Process Scheduling Simulation!\n"
                + "Please Select a scheduling algorithm:\n"
                + "[1]:\tFirst Come, First Served (FCFS)\n"
                + "[2]:\tShortest Job First (SJF)\n"
                + "[3]:\tRound Robin (RR)\n"
                + "[4]:\tPriority Scheduling\n"
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
        System.out.println("\nShortest Job First\n");

        // Sorts the processes by arrivalTime to get the "first comers" in order
        processes.sort((p1, p2) -> Integer.compare(p1.arrivalTime, p2.arrivalTime));

        //declare variables
        int currentTime = 0;
        int totalTAT = 0;
        int totalWT = 0;
        List<String> ganttChart = new ArrayList<>();
        List<Integer> timeStamps = new ArrayList<>();
        List<Process> waitingP = new ArrayList<>();

        // Choose the first process with minimum arrival time and burst time
        Process p = processes.get(0);
        int p_index = 0;
        for (int i = 1; i < processes.size(); i++){
            if (processes.get(i).arrivalTime <= p.arrivalTime){
                
                // compare burstTimes
                if (processes.get(i).burstTime < p.burstTime){
                    p = processes.get(i);
                    p_index = i;
                }
            }
            else{
                // Exit choosing the next process
                i = processes.size();
            }

        }
        // to run this process, remove it from available processes to run
        waitingP.add(processes.remove(p_index));

        // the SJF code to calculate and print
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-10s\n","PID", "Arrival", "Burst", "Completion", "TAT", "WT");
        for (Process counter : processes){

            // Choose the process with minimum burst Time among waiting processes
            // Initial Waiting
            p_index = 0;
            p = waitingP.get(p_index);
            // need to choose a process that definitely has lowest burst Time
            if (!waitingP.isEmpty()){
                for (int i = 1; i < waitingP.size(); i++){

                    // compare Burst Times for lowest
                    if (waitingP.get(i).burstTime < p.burstTime){
                        p = waitingP.get(i);
                        p_index = i;
                    }
                }
            }
            // make sure to remove the running process from the waiting queue
            waitingP.remove(p_index);

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

            // Add to the Waiting Processes of all process that have arrived while current Process was running
            for (Process nextP : processes){
                if (nextP.arrivalTime <= currentTime){
                    waitingP.add(nextP);
                    processes.remove(nextP);
                }
            }

            //
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
        for (String ganttStr : ganttChart) {
            System.out.print("  " + ganttStr + "  |");
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
