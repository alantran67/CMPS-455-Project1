import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class DiningPhilosophers {
    // One simulation cycle is 20 milliseconds, keeping demonstrations short.
    private static final int CYCLE_MILLIS = 20;

    public static void run() {
        // Do not close System.in: the surrounding CLI may still need it.
        Scanner input = new Scanner(System.in);
        int count = readPositive(input, "Number of philosophers (P): ");
        if (count == -1) return;
        int total = readPositive(input, "Total number of meals (M): ");
        if (total == -1) return;

        DiningRoom room = new DiningRoom(count, total);
        Philosopher[] philosophers = new Philosopher[count];
        for (int i = 0; i < count; i++) {
            philosophers[i] = new Philosopher(i, room);
        }
        for (Philosopher philosopher : philosophers) philosopher.start();

        // An interrupt does not abandon workers or leave an incomplete simulation.
        boolean interrupted = false;
        for (Philosopher philosopher : philosophers) {
            boolean joined = false;
            while (!joined) {
                try {
                    philosopher.join();
                    joined = true;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        }
        // join provides visibility; there are no remaining writers here.
        System.out.println("Final number of meals eaten: " + room.completed);
        if (interrupted) Thread.currentThread().interrupt();
    }

    private static int readPositive(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!input.hasNext()) {
                System.out.println("Input ended; Dining Philosophers was not started.");
                return -1;
            }
            try {
                int value = Integer.parseInt(input.next());
                if (value > 0) return value;
            } catch (NumberFormatException e) {
                // Reject non-integers and values outside the int range.
            }
            System.out.println("Please enter a positive integer (1 through 2147483647).");
        }
    }

    // A one-use semaphore barrier; use separate instances for entry and departure.
    private static class Barrier {
        private final Semaphore mutex = new Semaphore(1, true);
        private final Semaphore gate = new Semaphore(0, true);
        private final int participants;
        private int arrived;

        Barrier(int participants) {
            this.participants = participants;
        }

        void await() {
            mutex.acquireUninterruptibly();
            try {
                if (++arrived == participants) gate.release(participants);
            } finally {
                mutex.release();
            }
            gate.acquireUninterruptibly();
        }
    }

    private static class DiningRoom {
        final Semaphore[] chopsticks;
        final Semaphore admission;
        final Semaphore mealMutex = new Semaphore(1, true);
        final Barrier entry;
        final Barrier departure;
        final int total;
        int claimed;
        int completed;

        DiningRoom(int count, int total) {
            this.total = total;
            chopsticks = new Semaphore[count];
            for (int i = 0; i < count; i++) chopsticks[i] = new Semaphore(1, true);
            // At most P - 1 contenders breaks the circular wait for P > 1.
            admission = new Semaphore(Math.max(1, count - 1), true);
            entry = new Barrier(count);
            departure = new Barrier(count);
        }

        boolean mealsRemain() {
            mealMutex.acquireUninterruptibly();
            try {
                return claimed < total;
            } finally {
                mealMutex.release();
            }
        }

        int claimMeal() {
            mealMutex.acquireUninterruptibly();
            try {
                // The check and increment must happen in the SAME critical section.
                return claimed < total ? ++claimed : 0;
            } finally {
                mealMutex.release();
            }
        }

        void completeMeal() {
            mealMutex.acquireUninterruptibly();
            try {
                completed++;
            } finally {
                mealMutex.release();
            }
        }
    }

    static class Philosopher extends Thread {
        private final int id;
        private final DiningRoom room;
        private boolean interrupted;

        Philosopher(int index, DiningRoom room) {
            super("Philosopher-" + (index + 1));
            this.id = index + 1;
            this.room = room;
        }

        @Override
        public void run() {
            say("enters the dining room.");
            room.entry.await();
            try {
                say("sits down.");
                while (room.mealsRemain()) {
                    int cycles = randomCycles();
                    say("is thinking for " + cycles + " cycles.");
                    pause(cycles);
                    if (!eatOneMeal()) break;
                }
            } finally {
                say("is ready to leave the dining room.");
                // No chopsticks/admission permits are held at this barrier.
                room.departure.await();
                say("leaves the dining room.");
                if (interrupted) interrupt();
            }
        }

        private boolean eatOneMeal() {
            int left = id - 1;
            int right = id % room.chopsticks.length;
            room.admission.acquireUninterruptibly();
            try {
                room.chopsticks[left].acquireUninterruptibly();
                try {
                    say("picked up chopstick " + (left + 1) + ".");
                    // Yield experiment: temporarily insert Thread.yield(); here.
                    // P = 1: both neighbor positions identify the SAME chopstick.
                    // Acquire/release it only once to avoid waiting on ourselves.
                    if (right != left) room.chopsticks[right].acquireUninterruptibly();
                    try {
                        if (right != left) say("picked up chopstick " + (right + 1) + ".");
                        int meal = room.claimMeal();
                        if (meal == 0) return false;
                        int cycles = randomCycles();
                        say("is eating meal " + meal + " of " + room.total
                                + " for " + cycles + " cycles.");
                        // Maximum-parallelism experiment: instrument active eaters
                        // around this pause using a separate semaphore-protected counter.
                        // Do not hold mealMutex during eating: non-neighbors can overlap.
                        pause(cycles);
                        room.completeMeal();
                        say("finished meal " + meal + ".");
                        return true;
                    } finally {
                        if (right != left) releaseChopstick(right);
                    }
                } finally {
                    releaseChopstick(left);
                }
            } finally {
                room.admission.release();
            }
        }

        private void releaseChopstick(int index) {
            // Log while still owning it so pickup/release traces remain ordered.
            try {
                say("released chopstick " + (index + 1) + ".");
            } finally {
                room.chopsticks[index].release();
            }
        }

        private static int randomCycles() {
            return ThreadLocalRandom.current().nextInt(3, 7);
        }

        private void pause(int cycles) {
            for (int i = 0; i < cycles; i++) {
                boolean slept = false;
                while (!slept) {
                    try {
                        Thread.sleep(CYCLE_MILLIS);
                        slept = true;
                    } catch (InterruptedException e) {
                        // Finish claimed meals and both barriers, then restore the flag.
                        interrupted = true;
                    }
                }
            }
        }

        private void say(String action) {
            System.out.println("Philosopher " + id + " " + action);
        }
    }
}