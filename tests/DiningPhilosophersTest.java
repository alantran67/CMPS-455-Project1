import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Standalone regression checks; no changes to the team's Main class required. */
public class DiningPhilosophersTest {
    public static void main(String[] args) throws Exception {
        int[][] cases = {{5, 10}, {3, 2}, {2, 10}, {10, 25}, {1, 1}};
        for (int repeat = 0; repeat < 3; repeat++) {
            for (int[] values : cases) check(values[0], values[1], values[0] + "\n" + values[1] + "\n");
        }
        check(2, 3, "abc\n0\n-2\n1.5\n2147483648\n2\nno\n0\n3\n");
        require(capture("").contains("was not started"), "EOF before P");
        require(capture("2\n").contains("was not started"), "EOF before M");
        System.out.println("All Dining Philosophers checks passed.");
    }

    private static String capture(String input) throws Exception {
        InputStream oldInput = System.in;
        PrintStream oldOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Throwable[] failure = new Throwable[1];
        try (PrintStream captured = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(captured);
            Thread runner = new Thread(() -> {
                try {
                    DiningPhilosophers.run();
                    System.out.println("RUN RETURNED");
                } catch (Throwable e) {
                    failure[0] = e;
                }
            });
            // Workers inherit daemon status, allowing a failed timeout to exit.
            runner.setDaemon(true);
            runner.start();
            runner.join(10_000);
            require(!runner.isAlive(), "Simulation timed out (possible deadlock)");
            require(failure[0] == null, "Simulation threw: " + failure[0]);
            return output.toString(StandardCharsets.UTF_8);
        } finally {
            System.setIn(oldInput);
            System.setOut(oldOutput);
        }
    }

    private static void check(int p, int m, String input) throws Exception {
        String output = capture(input);
        Pattern action = Pattern.compile("Philosopher (\\d+) (.*)");
        Pattern pickup = Pattern.compile("picked up chopstick (\\d+)\\.");
        Pattern release = Pattern.compile("released chopstick (\\d+)\\.");
        Pattern eating = Pattern.compile("is eating meal (\\d+) of (\\d+) for ([3-6]) cycles\\.");
        int[] owners = new int[p];
        Set<Integer> meals = new HashSet<>();
        Set<Integer> eaters = new HashSet<>();
        int entered = 0, ready = 0, left = 0, finished = 0;
        for (String line : output.split("\\R")) {
            Matcher event = action.matcher(line);
            if (!event.find()) continue;
            int id = Integer.parseInt(event.group(1));
            String text = event.group(2);
            if (text.equals("enters the dining room.")) entered++;
            if (text.equals("sits down.") || text.startsWith("is thinking")) {
                require(entered == p, "Activity before everyone entered");
            }
            Matcher pick = pickup.matcher(text), drop = release.matcher(text), eat = eating.matcher(text);
            if (pick.matches()) {
                int stick = Integer.parseInt(pick.group(1)) - 1;
                require(owners[stick] == 0, "Chopstick simultaneously owned");
                owners[stick] = id;
            }
            if (eat.matches()) {
                int meal = Integer.parseInt(eat.group(1));
                require(meal >= 1 && meal <= m && meals.add(meal), "Invalid/duplicate meal");
                require(owners[id - 1] == id && owners[id % p] == id, "Eating without chopsticks");
                eaters.add(id);
            }
            if (text.startsWith("finished meal")) finished++;
            if (drop.matches()) {
                int stick = Integer.parseInt(drop.group(1)) - 1;
                require(owners[stick] == id, "Release by non-owner");
                owners[stick] = 0;
            }
            if (text.equals("is ready to leave the dining room.")) ready++;
            if (text.equals("leaves the dining room.")) {
                require(ready == p && finished == m, "Premature departure");
                left++;
            }
        }
        require(meals.size() == m && finished == m, "Wrong meal total");
        require(left == p, "Not all philosophers left");
        for (int owner : owners) require(owner == 0, "Unreleased chopstick");
        require(p == 1 || m == 1 || eaters.size() > 1, "No progress by multiple philosophers");
        require(output.endsWith("Final number of meals eaten: " + m + System.lineSeparator()
                + "RUN RETURNED" + System.lineSeparator()), "Incorrect summary/return order");
        System.out.println("PASS P=" + p + ", M=" + m + "; meals=" + meals.size()
                + ", philosophers that ate=" + eaters.size());
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
