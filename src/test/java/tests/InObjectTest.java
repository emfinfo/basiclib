package tests;

import beans.Localite;
import ch.jcsinfo.system.InObject;
import ch.jcsinfo.system.StackTracer;
import java.lang.reflect.Method;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test des méthodes principales de la classe InObject.
 * .
 * On regarde dans un objet "StringBuilder" si la méthode
 * "replace" existe et on essaye de l'exécuter.
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InObjectTest {
  private static String TEST_STRING = "BONJOUR";
  private static String TEST_METHOD1 = "replace";
  private static String TEST_METHOD2 = "getLocalite";

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println();
  }

  @Test
  public void test01_findMethod() {
    StackTracer.printCurrentTestMethod();
    StringBuilder source = new StringBuilder(TEST_STRING);
    Method m = InObject.findMethod(source, TEST_METHOD1, int.class, int.class, String.class);
    
    // on compare le résultat avec celui attendu
    boolean ok = m != null;
    StackTracer.printTestResult("Source", source, "Found", m.getName(), "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test02_callMethod() { 
    StackTracer.printCurrentTestMethod(" « " + TEST_METHOD1 + " »");
    StringBuilder source = new StringBuilder(TEST_STRING);
    Method m = InObject.findMethod(source, TEST_METHOD1, int.class, int.class, String.class);
    boolean ok = m != null;
    String strResult = "";
    if (ok) {
      Object result = InObject.callMethod(source, m, 0, 3, "SE");
      ok = result != null;
      if (ok) {
        strResult = ((StringBuilder) result).toString();
      }
    }
    StackTracer.printTestResult("Source", TEST_STRING, "Result", strResult, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test03_callMethod() { 
    StackTracer.printCurrentTestMethod(" « " + TEST_METHOD2 + " »");
    Localite source = new Localite(1, 3000, "Berne", "BE");
    Object result = InObject.callGetter(source, TEST_METHOD2);

    // on compare le résultat avec celui attendu
    boolean ok = result != null;
    StackTracer.printTestResult("Source", source, "Result", result, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test04_fieldsToString() {
    StackTracer.printCurrentTestMethod();
    Localite source = new Localite(1, 1700, "Fribourg", "FR");
    String result = InObject.fieldsToString(source);

    // on compare le résultat avec celui attendu
    boolean ok = !result.isEmpty();
    StackTracer.printTestResult("Source", source, "Result", result, "Ok", ok);
    assertTrue(ok);
  }

}
