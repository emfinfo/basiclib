package tests;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.Cypher;
import java.nio.charset.Charset;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jcstritt
 */
public class CypherTest {
  private static String toEncode = "06h30, Bonjour les amis de la pêche en Gruyère !";
  
  private static String key = "PabloPicasso";
  private static String encoded = "";
  
  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("Default charset: " + Charset.defaultCharset().displayName());
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }  

  @Test
  public void testEncrypt() {
    StackTracer.printCurrentTestMethod();
    encoded = Cypher.encrypt(toEncode, key, Charset.defaultCharset());
    System.out.println("- à coder  : " + toEncode);
    System.out.println("- codé     : " + encoded);
    assertTrue(!encoded.equals(toEncode));
  }
  
  @Test
  public void testDecrypt() {
    StackTracer.printCurrentTestMethod();
    String toDecode = encoded;
    String decoded = Cypher.decrypt(toDecode, key, Charset.defaultCharset());
    System.out.println("- à décoder: " + toDecode);
    System.out.println("- décodé   : " + decoded);
    assertTrue(decoded.equals(toEncode));
  }  

}
