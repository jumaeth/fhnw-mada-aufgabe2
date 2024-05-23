package elgamal;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static elgamal.ElGamalVerfahren.encrypt;
import static elgamal.ElGamalVerfahren.writeKeys;

public class Decrypt {
    public static void decrypt() throws IOException {

        // Macht aus der Hexadezimalzahl einen Biginteger "n"

        String hexString = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1"
                + "29024E088A67CC74020BBEA63B139B22514A08798E3404DD"
                + "EF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245"
                + "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7ED"
                + "EE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D"
                + "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F"
                + "83655D23DCA3AD961C62F356208552BB9ED529077096966D"
                + "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3B"
                + "E39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9"
                + "DE2BCBF6955817183995497CEA956AE515D2261898FA0510"
                + "15728E5A8AACAA68FFFFFFFFFFFFFFFF";
        BigInteger n = new BigInteger(hexString, 16);

        InputStream in = Files.newInputStream(Path.of("src/elgamal/chiffre.txt"));

        Scanner scanner = new Scanner(in);
        System.out.println("Entschlüsslung:");

        try (OutputStream out = Files.newOutputStream(Path.of("src/elgamal/text-d.txt"))) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
            BufferedWriter writer = new BufferedWriter(outputStreamWriter);

            InputStream in2 = Files.newInputStream((Path.of("src/elgamal/sk.txt")));
            Scanner scanner3 = new Scanner(in2);

            String skBString = scanner3.nextLine();


            BigInteger skB = new BigInteger(skBString);

            while (scanner.hasNextLine()) {

                String[] y1y2s = scanner.nextLine().split(",");

                String y1 = y1y2s[0].replace("(", "");

                String y2 = y1y2s[1].replace(")", "").replace(";", "");

                BigInteger inverseY1 = new BigInteger(y1).modPow(skB, n).modInverse(n);

                BigInteger asciiDecrypt = new BigInteger(y2).multiply(inverseY1).mod(n);

                String asciiString = asciiDecrypt.toString();

                System.out.print((char) Integer.parseInt(asciiString));

                writer.write((char) Integer.parseInt(asciiString));
            }
            writer.close();
        }
    }

    public static void main(String[] args) {

        try {
            writeKeys();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            encrypt();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            decrypt();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
