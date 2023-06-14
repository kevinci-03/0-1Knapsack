import java.io.*;
import java.util.*;

public class DynProg {

    public static void main(String[] args) {

        int n, capacity;
        int[] values, weights;
        ArrayList<String> lines = new ArrayList<>();
        String line;
        String filePath = "/home/k3p4/calpoly/csc_349/Asgn3-0-1Knapsack/easy20.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            n = Integer.parseInt(lines.remove(0));
            capacity = Integer.parseInt(lines.remove(lines.size() - 1));
            values = new int[n];
            weights = new int[n];
            for (int i = 0; i < lines.size(); i++) {
                String[] splitLines = lines.get(i).trim().split("\\s+");
                values[i] = Integer.parseInt(splitLines[1]);
                weights[i] = Integer.parseInt(splitLines[2]);

            }
            long startTime = System.nanoTime();
            knapsackDP(values, weights, capacity, n);
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            double ms = executionTime / 1_000_000.0;
            System.out.println("Execution time: " + ms + " milliseconds");
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void knapsackDP(int[] values, int[] weights, int capacity, int n) {

        // Create table using Dynamic Programming
        int items = n;
        int[][] table = new int[items + 1][capacity + 1];
        for (int i = 1; i <= items; i++) {
            int weight = weights[i - 1];
            int value = values[i - 1];
            for (int j = 1; j <= capacity; j++) {
                if (weight <= j) {
                    // If current item can be included we take the max value of including it and excluding it
                    table[i][j] = Math.max(table[i - 1][j], table[i - 1][j - weight] + value);
                }
                else {
                    // If current item weight exceeds the remaining capacity then we exclude the item
                    table[i][j] = table[i - 1][j];
                }
            }
        }

        // Setup variables for use in Back Track
        int result = table[items][capacity];
        int w = capacity;
        int totalWeight = 0;
        StringBuilder output = new StringBuilder();

        // Start Back-Track for printing
        for (int i = n; i > 0 && result > 0; i--) {
            if (result == table[i - 1][w]) {
                // If the result is still the same then we just continue and move to the previous item
                continue;
            }
            else {
                // If the result changes then we add the current item to the sack
                output.insert(0, i + " ");
                totalWeight += weights[i - 1];
                result = result - values[i - 1];
                w = w - weights[i - 1];
            }
        }
        output.insert(0, "Items: ");
        output.insert(0, "Dynamic Programming Solution: Value " + table[items][capacity] + ", Weight " + totalWeight + "\n");
        System.out.println(output.toString());

    }

}