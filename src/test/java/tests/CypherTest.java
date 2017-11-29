package tests;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.ConvertLib;
import ch.jcsinfo.util.Cypher;
import java.nio.charset.Charset;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test des méthodes principales de la classe correspondante.
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CypherTest {
//  private static String toEncode = "06h30, Bonjour les amis de la pêche en Gruyère !";
//  private static String key = "PabloPicasso";
  private static String original[] = {"Demo AG", "Demo Ltd", "Demo SA"};
  private static String encoded[] = {"", "", ""};
  private static String key = "Marco" + "Polo";

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
    System.out.println("Default charset: " + Charset.defaultCharset().displayName());
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_encrypt() {
    StackTracer.printCurrentTestMethod();
    int maxLen = 0;
    for (String one : original) {
      if (one.length() > maxLen) {
        maxLen = one.length();
      }
    }

    for (int i = 0; i < original.length; i++) {
      encoded[i] = Cypher.encrypt(original[i], key, Charset.defaultCharset());
      System.out.println(ConvertLib.fillString(maxLen, ' ', original[i]) + "\t" + encoded[i]);
      assertTrue(!encoded.equals(original[i]));
    }
  }

  @Test
  public void test02_decrypt() {
    StackTracer.printCurrentTestMethod();
    int maxLen = 0;
    for (String one : encoded) {
      if (one.length() > maxLen) {
        maxLen = one.length();
      }
    }

    for (int i = 0; i < encoded.length; i++) {
      String decoded = Cypher.decrypt(encoded[i], key, Charset.defaultCharset());
      System.out.println(ConvertLib.fillString(maxLen, ' ', encoded[i]) + "\t" + decoded);
      assertTrue(decoded.equals(original[i]));
    }
  }

}
