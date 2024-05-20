package elgamal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class ElGamalVerfahren {


    public static void main(String[] args) throws IOException {

        // File which needs to be encoded
        Path fileToEncode = Path.of("src/ElGamal/text.txt");

        try (InputStream inEncode = Files.newInputStream(fileToEncode)) {

            // Read File which needs to be encoded
            InputStreamReader inputReader = new InputStreamReader(inEncode);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
        }
    }
}