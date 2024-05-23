package elgamal;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

public class ElGamalVerfahren {

    // Macht aus der gegebenen Hexadezimalzahl einen Biginteger "n"
    public static String nString = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1"
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
    private static BigInteger n = new BigInteger(nString, 16);


    // Generierung einer random Zahl für den private Key Konstruktor
    private static final Random random = new Random();


    // Privater Schlüssel: Generierung eines "b" zwischen 0 bis n - 1
    private static final BigInteger sk = new BigInteger(n.bitLength(), random).mod(n);


    // Erstellung des gegebenen Erzeugers 2
    private static final BigInteger g = BigInteger.valueOf(2);


    // Öffentlicher Schlüssel: Vom privaten Schlüssel abgeleitet "g^b"
    private static final BigInteger pk = g.modPow(sk, n);


    // Methode für die Keys in die enstprechenden files zu schreiben
    public static void writeKeys() throws IOException {

        // Schreiben des public Keys in die pk.txt Datei
        Path pk = Path.of("src/elgamal/pk.txt");
        try (OutputStream out = Files.newOutputStream(pk)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            buffered.write(ElGamalVerfahren.pk.toString());
            buffered.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Schreiben des secret Keys in die sk.txt Datei
        Path sk = Path.of("src/elgamal/sk.txt");
        try (OutputStream out = Files.newOutputStream(sk)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            buffered.write(ElGamalVerfahren.sk.toString());
            buffered.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void encrypt() throws IOException {

        // Liest die zu verschlüsselnde Nachricht aus der text.txt Datei raus
        Path toEncrypt = Path.of("src/elgamal/text.txt");
        InputStream in = Files.newInputStream(toEncrypt);
        Scanner scanner = new Scanner(in);

        String stringToEncrypt = "";

        // Nimmt jede Linie zum Verschlüsseln
        while (scanner.hasNextLine()) {
            stringToEncrypt = stringToEncrypt + scanner.nextLine() + "\n";
        }

        // Liest den public key aus der pk.txt Datei
        Path pk = Path.of("src/elgamal/pk.txt");
        InputStream inPk = Files.newInputStream(pk);

        Scanner scanner2 = new Scanner(inPk);

        String pkString = scanner2.nextLine();

        BigInteger pkBigInteger = new BigInteger(pkString);


        // ASCII Codes mit ElGamal verschlüsseln

        try (OutputStream out = Files.newOutputStream(Path.of("src/elgamal/chiffre.txt"))) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            // Generierung des zufälligen "a"
            BigInteger a = new BigInteger(n.bitLength(), random).mod(n);

            // g^a berechnen
            BigInteger gHochA = g.modPow(a, n);

            for (int i = 0; i < stringToEncrypt.length(); i++) {

                // g^b*a berechnen
                BigInteger gHochBA = pkBigInteger.modPow(a, n);
                int ascii = stringToEncrypt.charAt(i);

                // g^a*b mit Nachricht (x) verknüpfen
                BigInteger asciiEncrypt = gHochBA.multiply(BigInteger.valueOf(ascii)).mod(n);
                bufferedWriter.write("(" + gHochA + "," + asciiEncrypt + ");\n");
            }
            bufferedWriter.close();
        }

    }
}

