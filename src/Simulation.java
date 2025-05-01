import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ProcessThread extends Thread {
    int pid, burstTime;

    public ProcessThread(int pid, int burstTime) {
        this.pid = pid;
        this.burstTime = burstTime;
    }

    public void run() {
        System.out.println("[Process " + pid + "] started.");
        try {
            Thread.sleep(burstTime * 1000L);
        } catch (InterruptedException e) {
            System.out.println("[Process " + pid + "] interrupted.");
        }
        System.out.println("[Process " + pid + "] finished.");
    }
}

class Philosopher extends Thread {
    private final int id;
    private final Lock leftFork;
    private final Lock rightFork;
    private final int duration;

    public Philosopher(int id, Lock leftFork, Lock rightFork, int duration) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.duration = duration;
    }

    public void run() {
        System.out.println("[Philosopher " + id + "] Thinking...");
        try {
            Thread.sleep(duration * 500L);
        } catch (InterruptedException ignored) {}

        System.out.println("[Philosopher " + id + "] Waiting for forks...");

        Lock firstFork = (id % 2 == 0) ? leftFork : rightFork;
        Lock secondFork = (id % 2 == 0) ? rightFork : leftFork;

        synchronizedForkPickup(firstFork, secondFork);
    }

    private void synchronizedForkPickup(Lock first, Lock second) {
        first.lock();
        try {
            System.out.println("[Philosopher " + id + "] Picked up first fork.");
            second.lock();
            try {
                System.out.println("[Philosopher " + id + "] Picked up second fork.");
                System.out.println("[Philosopher " + id + "] Eating...");
                Thread.sleep(duration * 500L);  
                System.out.println("[Philosopher " + id + "] Finished eating.");
            } catch (InterruptedException ignored) {
            } finally {
                second.unlock();
                System.out.println("[Philosopher " + id + "] Released second fork.");
            }
        } finally {
            first.unlock();
            System.out.println("[Philosopher " + id + "] Released first fork.");
        }
    }
}

public class Simulation {

    public static void main(String[] args) {
        System.out.println("Welcome to Process and Synchronization Simulation!");

        Scanner scanner = new Scanner(System.in);
        boolean exitFlag = false;

        while (!exitFlag) {
            System.out.print(menuToStr());

            if (scanner.hasNextInt()) {
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        System.out.println("Simulating Processes with Threads...");
                        List<ProcessThread> processThreads = loadProcessData("src/processes.txt");

                        for (Thread t : processThreads) {
                            t.start();
                        }

                        for (Thread t : processThreads) {
                            try {
                                t.join();
                            } catch (InterruptedException e) {
                                System.out.println("Thread join interrupted.");
                            }
                        }
                        System.out.println("All processes have finished.\n");
                        break;

                    case 2:
                        System.out.println("Running Dining Philosophers Simulation...");
                        diningPhilosophers("src/processes.txt");
                        break;

                    case 9:
                        exitFlag = true;
                        System.out.println("Exiting simulation.");
                        break;

                    default:
                        System.out.println("Invalid input. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        scanner.close();
    }

    public static List<ProcessThread> loadProcessData(String fileName) {
        List<ProcessThread> threads = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine();  
            }

            while (fileScanner.hasNext()) {
                int pid = fileScanner.nextInt();
                int arrivalTime = fileScanner.nextInt();  // Unused kept for formating sake
                int burstTime = fileScanner.nextInt();
                int priority = fileScanner.nextInt();     // Unused kept for formating sake
                threads.add(new ProcessThread(pid, burstTime));
            }

            System.out.println("Processes loaded: " + threads.size());
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
        }
        return threads;
    }

    public static void diningPhilosophers(String fileName) {
        List<int[]> philosopherData = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine();  
            }

            while (fileScanner.hasNext()) {
                int id = fileScanner.nextInt();
                fileScanner.nextInt(); // arrival time
                int burst = fileScanner.nextInt();
                fileScanner.nextInt(); // priority
                philosopherData.add(new int[]{id, burst});
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            return;
        }

        int numPhilosophers = philosopherData.size();
        Lock[] forks = new ReentrantLock[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new ReentrantLock();
        }

        Philosopher[] philosophers = new Philosopher[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            int id = philosopherData.get(i)[0];
            int burst = philosopherData.get(i)[1];
            Lock leftFork = forks[i];
            Lock rightFork = forks[(i + 1) % numPhilosophers];
            philosophers[i] = new Philosopher(id, leftFork, rightFork, burst);
        }

        for (Philosopher p : philosophers) {
            p.start();
        }

        for (Philosopher p : philosophers) {
            try {
                p.join();
            } catch (InterruptedException ignored) {}
        }

        System.out.println("Dining Philosophers simulation completed.\n");
    }

    public static String menuToStr() {
        return "\nPlease select an option:\n"
                + "[1]: Simulate Processes with Threads\n"
                + "[2]: Solve Dining Philosophers with Locks\n"
                + "[9]: Exit Simulation\n"
                + "User Input:\t";
    }
}
