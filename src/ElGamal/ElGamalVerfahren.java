package src.Huffman;

public class HuffmanKodierung {


public static void main(String[] args) {

        // File which needs to be encoded
        Path fileToEncode = Path.of("src/ElGamal/text.txt");

        try (InputStream inEncode = Files.newInputStream(fileToEncode)) {

            // Read File which needs to be encoded
            InputStreamReader inputReader = new InputStreamReader(inEncode);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
        }

}