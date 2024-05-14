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
            Map<Character, Integer> charCount = getCharAmount(bufferedReader);
            System.out.println("Number of Chars in File");
            System.out.println(charCount);

            // Calculate probability for each char
            Map<Character, Double> charProbability = getCharProbability(charCount);
            System.out.println("Percentage Value of each char");
            System.out.println(charProbability);

        } catch (IOException e) {
            System.out.println("Error while reading from file dec_tab-mada.txt");
        }
    }

    private static Map<Character, Integer> getCharAmount(BufferedReader bufferedReader) throws IOException {
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

    private static Map<Character, Double> getCharProbability(Map<Character, Integer> charCount) {
        Map<Character, Double> charProbability = new HashMap<>();
        int totalCharAmount = 0;

        // Get total Amount of chars
        for(Character c : charCount.keySet()) {
            totalCharAmount = totalCharAmount + charCount.get(c);
        }

        // Calculate Probability
        for(Character c : charCount.keySet()) {
            double percentageValueSingleChar =  100.00 / totalCharAmount;
            double percentageValueCurrentChar = charCount.get(c) * percentageValueSingleChar;
            charProbability.put(c, percentageValueCurrentChar);
        }

        return charProbability;
    }

}
