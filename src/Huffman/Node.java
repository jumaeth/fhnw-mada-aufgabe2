package src.Huffman;

public class Node implements Comparable<Node> {
    private int frequency;
    private char c;
    private Node left = null;
    private Node right = null;

    public Node(int frequency, char c) {
        this.frequency = frequency;
        this.c = c;
    }

    public char getC() {
        return c;
    }

    public int getFrequency() {
        return frequency;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public int compareTo(Node node) {
        return this.frequency - node.frequency;
    }
}
