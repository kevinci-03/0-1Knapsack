import java.util.*;
import java.io.*;

public class BandB {

    public static KnapSackNode bestNode = null;

    public static void main(String[] args) {

        List<Item> items = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        int n, capacity;
        String line;
        String filePath = "/home/k3p4/calpoly/csc_349/Asgn3-0-1Knapsack/easy20.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            n = Integer.parseInt(lines.remove(0));
            capacity = Integer.parseInt(lines.remove(lines.size() - 1));
            for (int i = 0; i < lines.size(); i++) {
                String[] splitLines = lines.get(i).trim().split("\\s+");
                int itemNum = Integer.parseInt(splitLines[0]);
                int value = Integer.parseInt(splitLines[1]);
                int weight = Integer.parseInt(splitLines[2]);
                Item item = new Item(value, weight, itemNum, (double) value / weight);
                items.add(item);
            }
            // Sort items based on value to weight ratio            
            Collections.sort(items, (a, b) -> Double.compare(b.ratio, a.ratio));
            long startTime = System.nanoTime();
            knapsackBB(items, capacity, n);
            List<Item> usedItems = listItems(items);
            int totalWeight = 0;
            // Traverse list of items used to add total weight
            for (Item item : usedItems) {
                totalWeight += item.weight;
            }
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            double ms = executionTime / 1_000_000.0;
            System.out.println("Using Branch and Bound, the best feasible solution found: Value " + bestNode.value + ", Weight " + totalWeight);
            System.out.print("Items: ");
            // Sort in ascneding order
            usedItems.sort(Comparator.comparingInt(item -> item.itemNum));
            for (Item item : usedItems) {
                System.out.print(item.itemNum + " ");
            }
            System.out.println();
            System.out.println("Execution time: " + ms + " milliseconds");
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void knapsackBB(List<Item> items, int capacity, int n) {

        KnapSackNode node = new KnapSackNode(0, 0, 0 , 0);  // Dummy Node to start branch
        int maxVal = 0;
        Queue<KnapSackNode> queue = new PriorityQueue<>(Comparator.comparingDouble(item -> item.bound));
        node.bound = getBound(node, capacity, n, items);
        queue.add(node);
        while (!queue.isEmpty()) {
            node = queue.poll();
            if (node.bound > maxVal && node.level < n) {
                // Make a new node with values as if we picked up the item
                KnapSackNode withItem = new KnapSackNode(node.value + items.get(node.level).value, node.weight + items.get(node.level).weight, node.level + 1, 0);
                withItem.parent = node;
                withItem.index = node.level + 1;
                // Check if the current node we have is better than the bestNode that we currently have
                if (withItem.weight <= capacity && withItem.value > maxVal) {
                    maxVal = withItem.value;
                    bestNode = withItem;
                }
                withItem.bound = getBound(withItem, capacity, n, items);
                // Check if node is within the bound and is promising then we add it to the PQ
                if (withItem.bound > maxVal) {
                    queue.add(withItem);
                }
                // Make a new node with values as if we didn't pick up the item
                KnapSackNode withoutItem = new KnapSackNode(node.value, node.weight, node.level + 1, 0);
                withoutItem.parent = node;
                withoutItem.index = -1;
                withoutItem.bound = getBound(withoutItem, capacity, n, items);
                // Check if node is within the bound and is promising then we add it to the PQ
                if (withoutItem.bound > maxVal) {
                    queue.add(withoutItem);
                }
            }
        }
    }

    public static double getBound(KnapSackNode node, int capacity, int n, List<Item> items) {

        if (node.weight >= capacity) {
            return 0.0;  // Not a feasible option so we return 0.0
        }
        int j = node.level;
        int totalWeight = node.weight;
        double bound = node.value;
        // Loop to include items in the sack until the capacity is reached
        while (j < n && totalWeight + items.get(j).weight <= capacity) {
            totalWeight += items.get(j).weight;
            bound += items.get(j).value;
            j++;
        }
        // If the sack is not full and there is still items then we calculate the bound including the next item
        if (j < n && totalWeight != capacity) {
            bound += (capacity - totalWeight) * items.get(j).ratio;
        }
        return bound;
    }

    public static List<Item> listItems(List<Item> items) {

        List<Item> usedItems = new ArrayList<>();
        KnapSackNode cur = bestNode;
        // Traverse back to the parents to get the items that we did pick up
        while (cur.parent != null) {
            if (cur.index != -1) {
                usedItems.add(0, items.get(cur.index - 1));
            }
            cur = cur.parent;
        }
        return usedItems;
    }

}
