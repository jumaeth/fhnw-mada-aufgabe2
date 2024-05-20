package ElGamal;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class ElGamalVerfahren {

    //neutrales Element

    int neutral = 1;

    //ermitteln von ord(2)

    /*
        2^0 = 1
        2^1 = 2*2 mod n = 4
        8
        16
        32


     */


    // Key Genetor Methode
    public static void createKey(BigInteger n) throws IOException {

        // Erstellung des Erzeugers 2
        BigInteger g = BigInteger.valueOf(2);

        Random random = new Random();

        // Generierung eines "b" zwischen 0 bis n - 1
        BigInteger privateKey = new BigInteger(n.bitLength(), random).mod(n);

        // Vom privaten Schlüssel abgeleitet "g^b"
        BigInteger publicKey = g.modPow(privateKey, n);

        System.out.println(publicKey);
        System.out.println(privateKey);


        FileWriter pkWriter = new FileWriter("src/ElGamal/pk.txt");
        FileWriter skWriter = new FileWriter("src/ElGamal/sk.txt");
        pkWriter.write(String.valueOf(publicKey));
        skWriter.write(String.valueOf(privateKey));

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

