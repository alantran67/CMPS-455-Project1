import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class ReadWrite {

    static int numReaders;
    static int numWriters;
    static int maxReaders;

    // Number of readers that have completed the current batch
    static int readersCompleted = 0;

    // Protects readersCompleted
    static Semaphore mutex = new Semaphore(1);

    // Initially allow the first N readers
    static Semaphore readerStart;

    // Coordinator waits here
    static Semaphore writerStart = new Semaphore(0);

    public static void run() {
        System.out.println("Starting Readers-Writers Problem");

        Scanner scanner = new Scanner(System.in);

        System.out.print("How many reading threads should be created? (integer from 1-10000):");
        numReaders = scanner.nextInt();

        System.out.print("How many writer threads should be created (integer from 1-10000):");
        numWriters = scanner.nextInt();

        System.out.print("How many readers should be allowed to read at once?");
        maxReaders = scanner.nextInt();

        readerStart = new Semaphore(maxReaders);

        Thread[] readers = new Thread[numReaders];
        Thread[] writers = new Thread[numWriters];

        // Create readers
        for (int i = 0; i < numReaders; i++) {

            final int id = i + 1;

            readers[i] = new Thread(() -> {

                try {

                    // Wait for permission to read
                    readerStart.acquire();

                    System.out.println("R" + id + " began reading");

                    // Simulate reading
                    Thread.sleep(500);

                    System.out.println("R" + id + " finished reading");

                    mutex.acquire();

                    readersCompleted++;

                    /*
                     * Once N readers have completed,
                     * allow a coordinator to run.
                     */
                    if (readersCompleted == maxReaders) {

                        readersCompleted = 0;

                        writerStart.release();
                    }

                    mutex.release();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Create writers
        for (int i = 0; i < numWriters; i++) {

            final int id = i + 1;

            writers[i] = new Thread(() -> {

                try {

                    // Wait for N readers
                    writerStart.acquire();

                    System.out.println("W" + id + " began writing");

                    // Simulate coordination
                    Thread.sleep(1000);

                    System.out.println("W" + id + " finished writing");

                    /*
                     * Allow the next N readers to run.
                     */
                    readerStart.release(maxReaders);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Start all threads
        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread writer : writers) {
            writer.start();
        }

        // Wait for readers
        for (Thread reader : readers) {

            try {
                reader.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Wait for writers
        for (Thread writer : writers) {

            try {
                writer.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("All writers have finished.");
        System.out.println("Program Exiting.");

        scanner.close();
    }
}