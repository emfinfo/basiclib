package ch.jcsinfo.util;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Classe avec des méthodes basiques d'encryptage : <br>
 * - par substitution d'alphabets<br>
 * - par manipulation de bits (xor)<br>
 * 
 * @author jcstritt
 */
public class Cypher {

  private static final String[] ALPHABETS = {
    "1AQW2 ZSX3ED(C4RFV)5TGB6*YHN7UJ8IK9OL0PMazertyuiopmlkjhgfdsqwxcvbn",
    "aetuopiyrzqdgjlmkhfsw(cbnv xN1B*2V3C4X5W6)Q7S8D9F0MLKJHGAPZOEIRUTY",
    "qWA0192XsZ8374EdC65VfRTgBNh*YUjIkOlmPMpoLiKJuyHn(t GbrFveD)czSxaQw",
    "XYZACB*FDEHIJMLKNOPSQRTUVWzxycbaedfjihklmponr(qsuvtw6 52879G40)3g1"};
  private static final int SPACE_POS = ALPHABETS[0].indexOf(" ");
  public static final Charset DOS_CHARSET = Charset.forName("CP850"); 
  public static final Charset JAVA_CHARSET = Charset.forName("UTF-16BE");
  public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
  public static final Charset UTF16_CHARSET = Charset.forName("UTF-16");
  public static final Charset ISO88591_CHARSET = Charset.forName("ISO-8859-1");

  public static String substEncrypt( String toEncode ) {
    String encoded = "";
    if (!toEncode.isEmpty()) {
      int j = 0;
      for (int i = 0; i < toEncode.length(); i++) {
        char c = toEncode.charAt(i);
        int p = ALPHABETS[0].indexOf(c);
        if (p >= 0) {
          encoded += ALPHABETS[1 + j].charAt(p);
        } else {
          encoded += c;
        }
        if (p == SPACE_POS) {
          j = (j + 1) % 3;
        }
      }
    }
    return encoded;
  }

  public static String substDecrypt( String toDecode ) {
    String decoded = "";
    if (!toDecode.isEmpty()) {
      int j = 0;
      for (int i = 0; i < toDecode.length(); i++) {
        char c = toDecode.charAt(i);
        int p = ALPHABETS[1 + j].indexOf(c);
        if (p >= 0) {
          decoded += ALPHABETS[0].charAt(p);
        } else {
          decoded += c;
        }
        if (p == SPACE_POS) {
          j = (j + 1) % 3;
        }
      }
    }
    return decoded;
  }
  
  public static byte[] xorProcess( byte[] expBytes, byte[] keyBytes ) {
    byte[] result = new byte[expBytes.length];
    for (int i = 0; i < expBytes.length; i++) {
      result[i] += expBytes[i] ^ keyBytes[i % keyBytes.length];
    }
    return result;
  }
  
  public static String shortDosArrayToString( short[] buffer, String key ) {
    String result = "";
    if (buffer.length > 0) {
      byte[] bytesBuff = new byte[buffer.length];
      byte[] keyBytes = key.trim().getBytes();
      for (int i = 0; i < buffer.length; i++) {
        bytesBuff[i] = (byte) (buffer[i]);
      }
      byte[] resultBytes = xorProcess(bytesBuff, keyBytes);
      result = new String(resultBytes, DOS_CHARSET);
    }
    return result;
  }
  
  public static String encrypt( String exp, String key, Charset cs ) {
    byte[] expBytes = exp.trim().getBytes(cs);
    byte[] keyBytes = key.trim().getBytes();
    return Base64.getEncoder().encodeToString(xorProcess(expBytes, keyBytes));
  }
  
  public static String decrypt( String exp, String key, Charset cs ) {
    byte[] expBytes = Base64.getDecoder().decode(exp.trim());
    byte[] keyBytes = key.trim().getBytes();
    byte[] resultBytes = xorProcess(expBytes, keyBytes);
    return new String(resultBytes, cs);
  }
  
}
