import java.util.Scanner;
import java.lang.System;

public class singleThreading extends Thread {

    public static int[][] dist;
    public static int[][] graph;

    final static int INF = 9999999;

    private int n;

    public singleThreading(int n) {
        this.n = n;
    }

    public void run() {

        for (int k = 0; k < n; ++k) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (dist[i][k] + dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        int m, n, ui, vi, wi = 0;
        System.out.println("Input: ");

        m = userInput.nextInt();
        n = userInput.nextInt();
        graph = new int[n][n];
        dist = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = 0;
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = INF;
                }
            }
        }
        for (int i = 0; i < m; i++) {

            ui = userInput.nextInt();
            vi = userInput.nextInt();
            wi = userInput.nextInt();
            graph[ui - 1][vi - 1] = 1;
            graph[vi - 1][ui - 1] = 1;
            dist[ui - 1][vi - 1] = wi;
            dist[vi - 1][ui - 1] = wi;
        }

        float startTime = System.nanoTime();
        System.out.println("Start time: " + startTime);
        singleThreading t = new singleThreading(n);

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        float stopTime = System.nanoTime();
        System.out.println("Stop time: " + stopTime);
        float timeTaken = ((stopTime - startTime) / 1000000000);

        System.out.println("Output: ");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(dist[i][j] + "  ");
            }
            System.out.println();
        }
        System.out.println("Time: " + String.format("%.5f", timeTaken));
        userInput.close();
    }
}
