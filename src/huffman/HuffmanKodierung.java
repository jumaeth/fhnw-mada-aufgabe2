package huffman;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HuffmanKodierung {
    public static void main(String[] args) {

        // File which needs to be encoded
        Path fileToEncode = Path.of("src/Huffman/toEncode.txt");

        try (InputStream inEncode = Files.newInputStream(fileToEncode)) {

            // Read File
            InputStreamReader inputReader = new InputStreamReader(inEncode);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            // Count Chars in File
            Map<Character, Integer> charFrequency = getCharFrequency(bufferedReader);
            System.out.println("\nNumber of Chars in File");
            System.out.println(charFrequency);

            // Add Node for every char to list and sort List by lowest frequency
            List<Node> nodeList = new ArrayList<>();
            for(Map.Entry<Character,Integer> e : charFrequency.entrySet()) {
                nodeList.add(new Node(e.getValue(), e.getKey()));
            }
            nodeList.sort(Node::compareTo);
            System.out.println("\nChar List sorted by frequency");
            nodeList.forEach(node -> System.out.print(node.getFrequency() + " "));

            // Generate Huffman Tree
            Node top = null;
            for(int i = 0; i < nodeList.size() - 1; i = i + 2) {

                // Die zwei Knoten (englisch: Node) mit der tiefsten Frequency auslesen
                Node first = nodeList.get(i);
                Node second = nodeList.get(i + 1);

                // Die zwei Knoten zu einem neuen Knoten verschmelzen und Frequency berechenen
                // Das '!' bezeichnet leer, weil nur in den Blätter Values enthalten sind
                Node merged = new Node(first.getFrequency() + second.getFrequency(), '!');

                // Die beiden Blätter des neuen Knoten speichern
                merged.setLeftNode(first);
                merged.setRightNode(second);

                // Oberster Knoten markieren
                top = merged;

                // Verschmolzener Knoten der Liste hinzufügen
                nodeList.add(merged);

                // Liste neu sortieren
                nodeList.sort(Node::compareTo);
            }

            // Generate Huffman Table
            HashMap<Character,String> huffmanTable = new HashMap<>();
            generateHuffmanTable(top, "", huffmanTable);
            System.out.println("\n\nHuffman Table");
            System.out.println(huffmanTable);

            // Write Huffman Table to file
            writeHuffmanTable(huffmanTable);



            System.out.println();
            nodeList.forEach(node -> System.out.print(node.getChar() + " " + node.getFrequency() + "   "));
        } catch (IOException e) {
            System.out.println("Error while reading from file dec_tab-mada.txt");
        }
    }

    private static void writeHuffmanTable(HashMap<Character, String> huffmanTable) {
        Path huffmanTableFile = Path.of("src/Huffman/dec_tab.txt");
        try(OutputStream out = Files.newOutputStream(huffmanTableFile)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            for(Map.Entry<Character,String> e : huffmanTable.entrySet()) {
                buffered.write(e.getKey() + ":" + e.getValue() + "-");
            }
        } catch (IOException e) {
            System.out.println("Error while writing Huffman Table to file");
        }
        System.out.println();
    }

    private static void generateHuffmanTable(Node top, String code, HashMap<Character, String> huffmanTable) {
        // Blatt wurde erreicht
        if(top.getRightNode() == null && top.getLeftNode() == null) {
            huffmanTable.put(top.getChar(), code);
            return;
        }
        // Blatt wurde noch nicht erreicht
        generateHuffmanTable(top.getLeftNode(), code + "0", huffmanTable);
        generateHuffmanTable(top.getRightNode(), code + "1", huffmanTable);
    }

    private static Map<Character, Integer> getCharFrequency(BufferedReader bufferedReader) throws IOException {
        Map<Character, Integer> charCount = new TreeMap<>();
        String line = bufferedReader.readLine();

        while (line != null) {
            for(int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if(charCount.containsKey(c)) {
                    charCount.put(line.charAt(i), charCount.get(c) + 1);
                } else {
                    charCount.put(line.charAt(i), 1);
                }
            }
            line = bufferedReader.readLine();
        }
        return charCount;
    }
}
