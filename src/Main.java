public class Main {
    public static void main(String[] args) {
        int taskNumber = -1; // Sentinel meaning "not set" or "invalid"

        // Check for -A in first spot of arg
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-A")) {
                // If true, then check for anything after it
                if (i + 1 >= args.length) {
                    System.out.println("Error: -A requires a value (1 or 2).");
                    return;
                }
                // Parse value after -A to an integer. If it doesn't work, it catches an exception
                try {
                    taskNumber = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("Error: -A value must be an integer.");
                    return;
                }
                i++; // Skip over the value that just finished
            }
        }

        // Cases for each Task (1 or 2)
        if (taskNumber == 1) {
            DiningPhilosophers.run();
        } else if (taskNumber == 2) {
            ReadWrite.run();
        } else {
            System.out.println("Error: Invalid or missing -A argument. Use: -A 1 (Dining Philosophers) or -A 2 (Readers Writers)");
        }
    }
}