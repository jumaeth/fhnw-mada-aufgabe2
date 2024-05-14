package src.Huffman;

public class Node implements Comparable<Node> {
    private int frequency;
    private char c;
    private Node leftNode = null;
    private Node rightNode = null;

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

    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setLeftNode(Node n) {
        this.leftNode = n;
    }

    public void setRightNode(Node n) {
        this.rightNode = n;
    }

    @Override
    public int compareTo(Node node) {
        return this.frequency - node.frequency;
    }
}
