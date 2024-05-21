package huffman;

import java.nio.file.Path;

public class DecodeOnly {

    // Encoded file with Byte Array
    private static final Path ENCODED_FILE = Path.of("src/huffman/output.dat");
    public static void main(String[] args) {
        HuffmanKodierung.decodeFile(ENCODED_FILE.toString());
    }
}
