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
        
        // You will have to change
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
        System.out.println("FCFS");
    }

    public static void sjf() {
        System.out.println("SJF");
    }

    public static void rr() {
        System.out.println("RR");
    }

    public static void ps() {
        System.out.println("PS");
    }
}
