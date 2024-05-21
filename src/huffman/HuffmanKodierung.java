package huffman;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HuffmanKodierung {

    // Output File for Byte Array
    private static final Path OUTPUT_FILE = Path.of("src/huffman/output.dat");

    // Path to Huffman Table
    private static final Path HUFFMAN_TABLE_PATH = Path.of("src/huffman/dec_tab.txt");

    // File which needs to be encoded
    private static final Path FILE_TO_ENCODE = Path.of("src/Huffman/toEncode.txt");

    public static void main(String[] args) {
        try (InputStream inEncode = Files.newInputStream(FILE_TO_ENCODE)) {

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
            System.out.println("\n\n Selbst Generierter Huffman Table");
            System.out.println(huffmanTable);

            // Write Huffman Table to file
            writeHuffmanTable(huffmanTable);

            // Generate Bitstring
            String bitString = generateBitstring(huffmanTable, FILE_TO_ENCODE);
            System.out.println("Bitstring: ");
            System.out.println(bitString);

            // Generate byteArray from Bitstring
            byte[] byteArray = bitStringToByteArr(bitString);
            System.out.println(Arrays.toString(byteArray));

            // Write byteArray to File
            FileOutputStream fos = new FileOutputStream(OUTPUT_FILE.toString());
            fos.write(byteArray);
            fos.close();

            decodeFile(OUTPUT_FILE.toString());
        } catch (IOException e) {
            System.out.println("Error while reading from file output.dat");
        }
    }

    public static void decodeFile(String path) {
        // Read File
        String binary = readEncodedFile(path);

        // remove last ...1000
        binary = adjustLastBits(binary);

        // Decode Binary String
        String decodedMessage = decodeBitstring(binary);
        System.out.println(decodedMessage);
    }

    // Cut off last ...1000
    private static String adjustLastBits(String binary) {
        int index = binary.length() - 1;
        while(binary.charAt(index) == '1') {
            index = index - 1;
        }
        binary = binary.substring(0,index - 1);
        System.out.println(binary);
        return binary;
    }

    private static String readEncodedFile(String path) {
        File file = new File(path);
        byte[] bFile = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bFile);
            String binary = byteArrToBitString(bFile);
            System.out.println(binary);
            fis.close();
            return binary;
        } catch (IOException e) {
            System.out.println("Error while reading from file output.dat");
        }
        return "";
    }

    // Decode Bitstring with huffmanTable
    private static String decodeBitstring(String binary) {
        Map<String, Character> huffmanTable = readHuffmanTable();

        StringBuilder result = new StringBuilder();
        StringBuilder currentBitString = new StringBuilder();

        for (char bit : binary.toCharArray()) {
            currentBitString.append(bit);
            if (huffmanTable.containsKey(currentBitString.toString())) {
                result.append(huffmanTable.get(currentBitString.toString()));
                currentBitString.setLength(0); // clear currentBitString
            }
        }

        return result.toString();
    }

    // Reads Huffman Table from File
    private static Map<String, Character> readHuffmanTable() {
        Map<String, Character> huffmanTable = new HashMap<>();
        try(InputStream in = Files.newInputStream(HUFFMAN_TABLE_PATH)) {
            //Read File with Huffman Table in it
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader buffered = new BufferedReader(reader);


            //Read every line of File and save the chars with their binary code to map
            String huffmanTableAsString = buffered.readLine();
            while(huffmanTableAsString != null) {
                String[] huffmanTableElements = huffmanTableAsString.split("-");
                for(String s : huffmanTableElements) {
                    String[] huffmanTableAtoms = s.split(":");
                    huffmanTable.put(huffmanTableAtoms[1], (char) Integer.parseInt(huffmanTableAtoms[0]));
                }
                huffmanTableAsString = buffered.readLine();
            }
            System.out.println("\nEingelesener Huffman Table");
            System.out.println(huffmanTable);
        } catch (IOException e) {
            System.out.println("Error while reading from dec_tab-mada.txt");
        }
        return huffmanTable;
    }

    // Convert Byte Array to Bitstring
    public static String byteArrToBitString(byte[] bytes) {
        StringBuilder bitString = new StringBuilder();
        for (byte b : bytes) {
            String binaryString = Integer.toBinaryString(b & 0xFF);
            while (binaryString.length() < 8) { // pad with leading zeros
                binaryString = "0" + binaryString;
            }
            bitString.append(binaryString);
        }
        return bitString.toString();
    }

    // Convert Bitstring to Byte Array
    public static byte[] bitStringToByteArr(String bitString) {
        int byteLength = bitString.length() / 8;
        byte[] bytes = new byte[byteLength];
        for (int i = 0; i < byteLength; i++) {
            String byteString = bitString.substring(8 * i, 8 * i + 8);
            int byteValue = Integer.parseInt(byteString, 2);
            bytes[i] = (byte) byteValue;
        }
        return bytes;
    }

    // Generate Bitstring from File with Huffman Table
    private static String generateBitstring(HashMap<Character, String> huffmanTable, Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            String bitString = "";

            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader buffered = new BufferedReader(reader);

            // Add Values from Huffman Table to one Bitstring
            String line = buffered.readLine();
            while (line != null) {
                for(int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    bitString = bitString + huffmanTable.get(c);
                }
                line = buffered.readLine();
            }

            // Add one 1. Then add 0 until bitString length can be divided by 8
            bitString = bitString + "1";
            while(bitString.length() % 8 != 0) {
                bitString = bitString + "0";
            }
            return bitString;
        } catch (IOException e) {
            System.out.println("Error while writing Bitstring");
        }
        return null;
    }

    private static void writeHuffmanTable(HashMap<Character, String> huffmanTable) {
        try(OutputStream out = Files.newOutputStream(HUFFMAN_TABLE_PATH)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            for(Map.Entry<Character,String> e : huffmanTable.entrySet()) {
                buffered.write((int)e.getKey() + ":" + e.getValue() + "-");
            }
            buffered.close();
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
