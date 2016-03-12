package tests;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.Cypher;
import java.nio.charset.Charset;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test de quelques méthodes de Cypher.
 * 
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CypherTest {
  private static String toEncode = "06h30, Bonjour les amis de la pêche en Gruyère !";
  
  private static String key = "PabloPicasso";
  private static String encoded = "";
  
  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
//    System.out.println("Default charset: " + Charset.defaultCharset().displayName());
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }  

  @Test
  public void test01_encrypt() {
    StackTracer.printCurrentTestMethod();
    encoded = Cypher.encrypt(toEncode, key, Charset.defaultCharset());
    System.out.println("  - to encrypt : " + toEncode);
    System.out.println("  - crypted    : " + encoded);
    assertTrue(!encoded.equals(toEncode));
  }
  
  @Test
  public void test02_decrypt() {
    StackTracer.printCurrentTestMethod();
    String toDecode = encoded;
    String decoded = Cypher.decrypt(toDecode, key, Charset.defaultCharset());
    System.out.println("  - to decrypt : " + toDecode);
    System.out.println("  - decrypted  : " + decoded);
    assertTrue(decoded.equals(toEncode));
  }  

}
