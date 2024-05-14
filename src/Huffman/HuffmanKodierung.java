package src.Huffman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HuffmanKodierung {
    public static void main(String[] args) {

        // File which needs to be encoded
        Path fileToEncode = Path.of("src/Huffman/toEncode.txt");

        try (InputStream inEncode = Files.newInputStream(fileToEncode)) {

            // Read File which needs to be encoded
            InputStreamReader inputReader = new InputStreamReader(inEncode);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            // Count Chars in File
            Map<Character, Integer> charFrequency = getCharFrequency(bufferedReader);
            System.out.println("Number of Chars in File");
            System.out.println(charFrequency);

            // Add Node for every char to list and sort List by lowest frequency
            List<Node> nodeList = new ArrayList<>();
            for(Map.Entry<Character,Integer> e : charFrequency.entrySet()) {
                nodeList.add(new Node(e.getValue(), e.getKey()));
            }
            nodeList.sort(Node::compareTo);
            System.out.println("Char List sorted by frequency");
            nodeList.forEach(node -> System.out.print(node.getFrequency() + " "));

            // Huffman Kodierung
            Node top = null;
            for(int i = 0; i < nodeList.size(); i = i + 2) {

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

                // TODO
                // Kodierungstabelle generieren, grobes Vorgehen:
                // Bei top Node starten
                // Falls Input 0: linker Node abrufen
                // Falls Input 1: rechter Node abrufen
                // Solange bis beide Nodes links und rechts Null sind (also keine mehr übrig, dann sind wir am Blatt angelangt)
                // Char dieses Nodes abrufen und speichern

            }
        } catch (IOException e) {
            System.out.println("Error while reading from file dec_tab-mada.txt");
        }
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
