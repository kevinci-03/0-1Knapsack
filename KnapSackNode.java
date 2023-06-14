public class KnapSackNode {

    public int value;
    public int weight;
    public int level;
    public int index;
    public double bound;
    public KnapSackNode parent;

    public KnapSackNode(int value, int weight, int level, double bound) {
        this.value = value;
        this.weight = weight;
        this.level = level;
        this.bound = bound;
    }
    
}