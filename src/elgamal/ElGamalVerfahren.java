package elgamal;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

public class ElGamalVerfahren {

    // Macht aus der Hexadezimalzahl einen Biginteger "n"
    private static String hexString = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD1"
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
    private static BigInteger n = new BigInteger(hexString, 16);


    // Generierung einer random Zahl für den private Key Konstruktor
    private static Random random = new Random();


    // Privater Schlüssel: Generierung eines "b" zwischen 0 bis n - 1
    private static BigInteger b = new BigInteger(n.bitLength(), random).mod(n);


    // Erstellung des Erzeugers 2
    private static BigInteger g = BigInteger.valueOf(2);


    // Vom privaten Schlüssel abgeleitet "g^b"
    private static BigInteger publicKey = g.modPow(b, n);


    // Key Generator Methode
    public static void writeKey() throws IOException {

        // Schreiben des public Keys in die pk.txt Datei
        Path pk = Path.of("src/elgamal/pk.txt");
        try(OutputStream out = Files.newOutputStream(pk)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            buffered.write(b.toString());
            buffered.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Schreiben des secret Keys in die sk.txt Datei
        Path sk = Path.of("src/elgamal/sk.txt");
        try(OutputStream out = Files.newOutputStream(sk)) {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            BufferedWriter buffered = new BufferedWriter(writer);
            buffered.write(publicKey.toString());
            buffered.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void encrypt() throws IOException {
        // Liest die zu verschlüsselnde Nachricht aus der text.txt Datei raus
        Path toEncrypt = Path.of("src/elgamal/text.txt");
        InputStream in = Files.newInputStream(toEncrypt);
        InputStreamReader inReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inReader);
        String line = bufferedReader.readLine();

        String encription = "";

        while (line != null) {
            encription = encription + line;
            line = bufferedReader.readLine();
        }
            System.out.println(encription);

        // Liest den privaten Schlüssel aus der pk.txt Datei
        Path pk = Path.of("src/elgamal/pk.txt");
        InputStream inPk = Files.newInputStream(pk);

        Scanner scanner = new Scanner(inPk);

        String pkString = scanner.next();

        BigInteger pkBigInteger = new BigInteger(pkString);


        // Umwandlung der Nachricht in ASCII Codes


        try (OutputStream out = Files.newOutputStream(Path.of("src/elgamal/chiffre.txt"))) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            for (int i = 0; i < encription.length(); i++) {

                // Generierung des zufälligen "a"
                BigInteger a = new BigInteger(n.bitLength(), random).mod(n);

                // g^a berechnen
                BigInteger gHochA = g.modPow(a, n);

                // g^a*b berechnen
                BigInteger aMalB = a.multiply(b);
                BigInteger gHochBA = g.modPow(aMalB, n);
                int ascii = encription.charAt(i);

                // g^a*b mit Nachricht verknüpfen
                BigInteger asciiEncrypt = gHochBA.multiply(BigInteger.valueOf(ascii)).mod(n);
                bufferedWriter.write("("+gHochA+","+asciiEncrypt+");\n");
                }
            }
    }

    public static void decrypt() throws IOException {

        InputStream in = Files.newInputStream(Path.of("src/elgamal/chiffre.txt"));

        Scanner scanner = new Scanner(in);

        String[] y1y2s = scanner.nextLine().split(",");

        String y1 = y1y2s[0].replace("(", "");

        String y2 = y1y2s[1].replace(")", "").replace(";", "");

        BigInteger inverseY1 = new BigInteger(y1).modPow(b, n).modInverse(n);

        BigInteger asciiDecrypt = new BigInteger(y2).multiply(inverseY1).mod(n);

        String asciiString = asciiDecrypt.toString();

        System.out.println((char)Integer.parseInt(asciiString));
    }

    public static void main(String[] args) {

        try {
            writeKey();
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

