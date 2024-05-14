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

            //Count Chars in File
            Map<Character, Integer> charCount = getCharacterAmount(bufferedReader);
            System.out.println(charCount);

        } catch (IOException e) {
            System.out.println("Error while reading from file dec_tab-mada.txt");
        }
    }

    private static Map<Character, Integer> getCharacterAmount(BufferedReader bufferedReader) throws IOException {
        Map<Character, Integer> charCount = new HashMap<>();
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
