public class Simulation {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        /* User Access the Menu */
        boolean exitFlag = false;
        while (!exitFlag){
            System.out.print(menuToStr());
            int option = System.in.read();

            switch (option) {
                case 1:
                    fcfs();
                case 2:
                    sjf();
                case 3:
                    rr();
                case 4:
                    ps();
                case 9:
                    exitFlag = true;
                default:
                    // Error Handling Needed
                    break;
            }

        }
        

    }

    public static String menuToStr(){
        String output = "";

        output += "Welcome to Process Scheduling Simulation!\n";
        output += "Please Select a scheduling algorithm:\n";
        output += "[1]:\tFirst Come, First Served (FCFS)\n"
                + "[2]:\tShortest Job First (SJF)\n"
                + "[3]:\tRound Robin (RR)\n"
                + "[4]:\tPriority Scheduling\n"
                + "[9]:\tExit Simulation\n";
        output += "User Input:\t";

        return output;
    }

    // First Come, First Served
    public static void fcfs(){

        System.out.println("FCFS");

    }

    // Shortest Job First
    public static void sjf(){

        System.out.println("SJF");

    }

    // Round Robin
    public static void rr(){

        System.out.println("RR");

    }

    // Priority Scheduling
    public static void ps(){

        System.out.println("PS");

    }


}
