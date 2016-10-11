package tests;

import beans.Localite;
import ch.jcsinfo.system.MethodHelper;
import ch.jcsinfo.system.StackTracer;
import java.lang.reflect.Method;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test des méthodes principales de la classe correspondante.
 * On regarde dans un objet "StringBuilder" si la méthode
 * "replace" existe et on essaye de l'exécuter.
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MethodHelperTest {
  private static String TEST_STRING = "BONJOUR";
  private static String TEST_METHOD1 = "replace";
  private static String TEST_METHOD2 = "getLocalite";

  @BeforeClass
  public static void setUpClass() {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println();
  }

  @Test
  public void test01_findMethod() {
    StackTracer.printCurrentTestMethod();
    StringBuilder source = new StringBuilder(TEST_STRING);
    Method m = MethodHelper.findMethod(source, TEST_METHOD1, int.class, int.class, String.class);
    StackTracer.printTestInfo("« " + TEST_METHOD1 + " » in " + StringBuilder.class.getSimpleName(), m);
    assertTrue(m != null);
  }

  @Test
  public void test02_callMethod() { // n'importe quelle méthode de la classe
    StackTracer.printCurrentTestMethod(" « " + TEST_METHOD1 + " »");
    StringBuilder source = new StringBuilder(TEST_STRING);
    Method method = MethodHelper.findMethod(source, TEST_METHOD1, int.class, int.class, String.class);
    boolean ok = method != null;
    if (ok) {
      Object result = MethodHelper.callMethod(source, method, 0, 3, "SE");
      ok = result != null;
      if (ok) {
        String strResult = ((StringBuilder) result).toString();
        StackTracer.printTestInfo(TEST_STRING, strResult);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test03_callMethod() { // un getter d'une classe entité
    StackTracer.printCurrentTestMethod(" « " + TEST_METHOD2 + " »");
    Localite source = new Localite(1, 3000, "Berne", "BE");
    Object result = MethodHelper.callMethod(source, TEST_METHOD2);
    boolean ok = result != null;
    if (ok) {
       StackTracer.printTestInfo("un objet Localite Berne", result);
    }
    assertTrue(ok);
  }

  @Test
  public void test04_fieldsToString() { //
    StackTracer.printCurrentTestMethod();
    Localite source = new Localite(1, 1700, "Fribourg", "FR");
    String result = MethodHelper.fieldsToString(source);
    boolean ok = !result.isEmpty();
    if (ok) {
       StackTracer.printTestInfo("un objet Localite Fribourg", result);
    }
    assertTrue(ok);
  }

}
