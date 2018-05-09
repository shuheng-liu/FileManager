package Test;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class CryptoTest {
    public static void main(String[] args) {
        try {
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();

            Cipher desCipher;
            desCipher = Cipher.getInstance("DES");


            byte[] text = "No body can see me .".getBytes("UTF8");
            byte[] text1 = Arrays.copyOfRange(text, 0, 10);
            byte[] text2 = Arrays.copyOfRange(text, 10, 20);

            desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            byte[] textEncrypted = desCipher.doFinal(text);
            byte[] textEncrypted1 = desCipher.doFinal(text1);
            byte[] textEncrypted2 = desCipher.doFinal(text2);

            String s = new String(textEncrypted);
            String s1 = new String(textEncrypted1);
            String s2 = new String(textEncrypted2);
            System.out.println(s);
            System.out.println(s1);
            System.out.println(s2);

            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
            byte[] textDecrypted = desCipher.doFinal(textEncrypted);
            byte[] textDecrypted1 = desCipher.doFinal(textEncrypted1);
            byte[] textDecrypted2 = desCipher.doFinal(textEncrypted2);

            s = new String(textDecrypted);
            s1 = new String(textDecrypted1);
            s2 = new String(textDecrypted2);
            System.out.println(s);
            System.out.println(s1);
            System.out.println(s2);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        CryptoTest test = new CryptoTest();

    }

//    KeyGenerator keyGenerator;
//    SecretKey desKey;
//    Cipher desCipher;


//    public CryptoTest(){
//        try {
//            keyGenerator = KeyGenerator.getInstance("DES");
//            desKey = keyGenerator.generateKey();
//            desCipher = Cipher.getInstance("DES");
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
//            e.printStackTrace();
//        }
//    }

//    public void decrypt(InputStream in, OutputStream out) {
//        try {
//            byte[] textEncrypted = new byte[128];
//            int len;
//            while ((len = in.read(textEncrypted)) != -1) {
//                desCipher.init(Cipher.DECRYPT_MODE, desKey);
//                byte[] textDecrypted = desCipher.doFinal(textEncrypted);
//                out.write(textDecrypted, 0, len);
//            }
//        } catch (InvalidKeyException | BadPaddingException | IOException | IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void encrypt(InputStream in, OutputStream out) {
//        try {
//            byte[] text = new byte[128];
//            int len;
//            while ((len = in.read(text)) != -1) {
//                desCipher.init(Cipher.ENCRYPT_MODE, desKey);
//                byte[] textEncrypted = desCipher.doFinal(text);
//                out.write(textEncrypted, 0, len);
//            }
//        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
}
