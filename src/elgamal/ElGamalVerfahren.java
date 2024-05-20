package ElGamal;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class ElGamalVerfahren {

    // Key Genetor Methode
    public static void createKey(BigInteger n) throws IOException {

        // Erstellung des Erzeugers 2
        BigInteger g = BigInteger.valueOf(2);

        // Generierung einer random Zahl für den private Key Konstruktor
        Random random = new Random();

        // Generierung eines "b" zwischen 0 bis n - 1
        BigInteger privateKey = new BigInteger(n.bitLength(), random).mod(n);

        // Vom privaten Schlüssel abgeleitet "g^b"
        BigInteger publicKey = g.modPow(privateKey, n);

        // Schreiben des private Keys in die pk.txt Datei
        Path pk = Path.of("src/ElGamal/pk.txt");
        try(OutputStream out = Files.newOutputStream(pk)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            buffered.write(privateKey.toString());
            buffered.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Schreiben des public Keys in die sk.txt Datei
        Path sk = Path.of("src/ElGamal/sk.txt");
        try(OutputStream out = Files.newOutputStream(sk)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            buffered.write(publicKey.toString());
            buffered.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
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

        try {
            createKey(n);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}

