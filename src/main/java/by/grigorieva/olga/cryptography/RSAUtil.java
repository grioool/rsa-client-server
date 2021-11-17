package by.grigorieva.olga.cryptography;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSAUtil {

    public static void main(String[] args) {
        try {
            RSAUtil cypher = new RSAUtil();
            RSAUtil decypher = new RSAUtil();
            cypher.setForeignPublicKey(decypher.getPublic());

            System.out.println(cypher.encrypt("Plain"));
            System.out.println(decypher.decrypt(cypher.encrypt("Plain")));

//            byte[] plain = "Plain".getBytes(StandardCharsets.UTF_8);
//            System.out.println(Arrays.toString(plain));
//
//            byte[] encoded = cypher.encrypt(plain);
//            System.out.println(Arrays.toString(encoded));
//
//            byte[] decoded = decypher.decrypt(encoded);
//            System.out.println(Arrays.toString(decoded));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    private final KeyFactory keyFactory;
    private final Cipher cipher;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private PublicKey foreignPublicKey;

    public RSAUtil() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.keyFactory = KeyFactory.getInstance("RSA");
        this.cipher = Cipher.getInstance("RSA");

        KeyPair pair = generateKeyPair();
        this.publicKey = pair.getPublic();
        this.privateKey = pair.getPrivate();
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public void setForeignPublicKey(byte[] publicKey) throws InvalidKeySpecException {
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        foreignPublicKey = keyFactory.generatePublic(encodedKeySpec);
    }

    public byte[] getPublic() {
        return Base64.getEncoder().encode(publicKey.getEncoded());
    }

    public byte[] encrypt(byte[] plain) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        cipher.init(Cipher.ENCRYPT_MODE, foreignPublicKey);
        return cipher.doFinal(plain);
    }

    public byte[] decrypt(byte[] encoded) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encoded);
    }

    private byte[] perBlockDoFinal(byte[] encoded) throws IllegalBlockSizeException, BadPaddingException {
        byte[] decoded = new byte[cipher.getOutputSize(encoded.length)];
        int i = 0;
        while(i < encoded.length) {
            byte[] buffer = Arrays.copyOfRange(encoded, i, i + cipher.getBlockSize());
            buffer = cipher.doFinal(buffer);
            System.arraycopy(buffer, 0, decoded, i, cipher.getBlockSize());

            i += cipher.getBlockSize();
        }
        return decoded;
    }

    public String encrypt(String plain) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        return Base64.getEncoder().encodeToString(encrypt(
                Base64.getEncoder().withoutPadding().encode(plain.getBytes())
        ));
    }

    public String decrypt(String encoded) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        return new String(Base64.getDecoder().decode(decrypt(
                Base64.getDecoder().decode(encoded))));
    }
}
