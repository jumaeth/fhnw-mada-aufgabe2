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
            nodeList.forEach(node -> System.out.println(node.getFrequency()));

            //



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
