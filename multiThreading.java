import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.lang.System;

/**
 * Class multithreading that extends Thread
 */
public class multiThreading extends Thread {

    // Create matrices dist and graph
    public static int[][] dist;
    public static int[][] graph;

    // Initialize infinity as INF
    final static int INF = 9999999;

    // Create integer variables n, k and i
    private int n, k, i;
    // Create Semaphores readLock and writeLock
    private static Semaphore readLock;
    private static Semaphore writeLock;

    /**
     * Function called multiThreading which takes parameters:
     * 
     * @param n is the number of nodes
     * @param k is the indexing for outer loop
     * @param i is the indexing for inner loop
     */
    public multiThreading(int n, int k, int i) {
        this.n = n;
        this.k = k;
        this.i = i;
    }

    // Worker function called run for thread
    public void run() {
        try {
            // for loop for calculating distance
            for (int j = 0; j < n; j++) {
                // acquire read lock
                readLock.acquire();
                if (dist[i][k] + dist[k][j] < dist[i][j]) {
                    // release read lock
                    readLock.release();
                    // acquire write lock
                    writeLock.acquire();
                    dist[i][j] = dist[i][k] + dist[k][j];
                    // release write lock
                    writeLock.release();
                } else {
                    // release read lock
                    readLock.release();
                }
            }
            // else catch exception
        } catch (Exception exp) {
            System.out.println("Message: " + exp);
        }
    }

    /**
     * Main function
     * 
     * @param args is argument of String array
     */
    public static void main(String[] args) {
        // Initialize Scanner as userInput
        Scanner userInput = new Scanner(System.in);
        // Initialize variables
        int edge, node;
        System.out.println("Input: ");

        // Take values for number of edges and nodes
        edge = userInput.nextInt();
        node = userInput.nextInt();
        // Initialize graph and dist to integer matrices with
        // n number of rows and columns
        graph = new int[node][node];
        dist = new int[node][node];

        // Initialize matrices that takes node as parameter
        initializeMatrices(node);
        // Put values in matrices that takes parameter edge and userInput
        putValuesInMatrices(edge, userInput);
        // Initialize t as array of threads
        multiThreading t[] = new multiThreading[node];
        // Initialize readLock semaphore with n number of threads
        readLock = new Semaphore(node);
        // Initialize writeLock semaphore which allows only one
        // thread at at time to update the cache
        writeLock = new Semaphore(1);

        // Start timer with unit nano seconds
        float startTime = System.nanoTime();
        // Initialize threads and joins that takes parameter node and t
        initializeThreadAndJoin(node, t);

        // Stop timer with unit nano seconds
        float stopTime = System.nanoTime();
        // Calculate time taken stop time - start time in seconds
        float timeTaken = ((stopTime - startTime) / 1000000000);
        // Print output that takes edge as parameter
        printOutput(edge);
        // Print time with 5 sig figures
        System.out.println("Time: " + String.format("%.5f", timeTaken));
        // Close scanner userInput
        userInput.close();
    }

    /**
     * Function that initialize matrices accordingly
     * 
     * @param num is the number of nodes
     */
    public static void initializeMatrices(int num) {
        // for loop that initialize graph by 0 and dist with 0 in diagonal
        // and INF to other
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                graph[i][j] = 0;
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = INF;
                }
            }
        }
    }

    /**
     * Function that puts values in matrices in graph and dist
     * 
     * @param edge      is the number of edges
     * @param userInput is the input given by user
     */
    public static void putValuesInMatrices(int edge, Scanner userInput) {
        // for loop that takes user input for ui, vi, and wi and puts
        // values in the graph and dist matrices accordingly
        int ui, vi, wi;
        for (int i = 0; i < edge; i++) {
            ui = userInput.nextInt();
            vi = userInput.nextInt();
            wi = userInput.nextInt();
            while (wi < 0) {
                System.out.println("The input is less than zero.");
                wi = userInput.nextInt();
            }
            graph[ui - 1][vi - 1] = 1;
            graph[vi - 1][ui - 1] = 1;
            dist[ui - 1][vi - 1] = wi;
            dist[vi - 1][ui - 1] = wi;
        }
    }

    /**
     * Function that initialize and starts threads and joins them
     * 
     * @param num is the number of nodes
     * @param t   is the thread from multithreading array
     */
    public static void initializeThreadAndJoin(int num, multiThreading[] t) {
        // for loop that initializes each thread with parameters num, i and k
        for (int k = 0; k < num; k++) {
            for (int i = 0; i < num; i++) {
                t[i] = new multiThreading(num, i, k);
                // Start thread in ith index
                t[i].start();
                try {
                    // joins thread in ith index

                    t[i].join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Function that prints output
     * 
     * @param edge is the number of edges
     */
    public static void printOutput(int edge) {
        // Print dist matrix
        System.out.println("Output: ");
        for (int i = 0; i < edge; i++) {
            for (int j = 0; j < edge; j++) {
                System.out.print(dist[i][j] == INF ? "INF  " : dist[i][j] + "  ");
            }
            System.out.println();
        }
    }

    /**
     * Function that acquires write lock
     * 
     * @throws InterruptedException throws exception if thread is interrupted
     */
    public void getWriteLock() throws InterruptedException {
        writeLock.acquire();
    }

    /**
     * Function that release write lock
     * 
     * @throws InterruptedException throws exception if thread is interrupted
     */
    public void releaseWriteLock() {
        writeLock.release();
    }

    /**
     * Function that acquires read lock
     * 
     * @throws InterruptedException throws exception if thread is interrupted
     */
    public void getReadLock() throws InterruptedException {
        readLock.acquire();
    }

    /**
     * Function that release read lock
     * 
     * @throws InterruptedException throws exception if thread is interrupted
     */
    public void releaseReadLock() {
        readLock.release();
    }
}
