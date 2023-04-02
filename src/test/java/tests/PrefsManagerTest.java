package tests;

import beans.Classe;
import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.PrefsManager;
import ch.jcsinfo.util.PrefsManager.PrefCase;
import org.junit.AfterClass;
import static org.junit.Assert.*;
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
public class PrefsManagerTest {

  @BeforeClass
  public static void setUpClass() {
    PrefsManager.setUserNodeName("PrefsManagerTest");
    PrefsManager.setPrefCase(PrefCase.UPCASE);
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println();
  }

  @Test
  public void test01_setAndGetValue() {
    final String PREF = "value";
    final String TOTEST = "Hello";
    
    StackTracer.printCurrentTestMethod();
    PrefsManager.setValue(PREF, TOTEST);
    String result = PrefsManager.getValue(PREF);
    
    // on compare le résultat avec celui attendu
    boolean ok = result.compareTo(TOTEST) == 0;
    StackTracer.printTestResult(PrefsManager.getKey(PREF), result);
    assertTrue(ok);
  }
  
  @Test
  public void test02_setAndGetBoolean() {
    final String PREF = "boolean";
    final boolean TOTEST = true;
    
    StackTracer.printCurrentTestMethod();
    PrefsManager.setBoolean(PREF, TOTEST);
    boolean result = PrefsManager.getBoolean(PREF);
    
    // on compare le résultat avec celui attendu
    boolean ok = result == TOTEST;
    StackTracer.printTestResult(PrefsManager.getKey(PREF), result);
    assertTrue(ok);
  }  
  
  @Test
  public void test03_setAndGetInt() {
    final String PREF = "integer";
    final int TOTEST = 32767;

    StackTracer.printCurrentTestMethod();
    PrefsManager.setInt(PREF, TOTEST);
    int result = PrefsManager.getInt(PREF);
    
    // on compare le résultat avec celui attendu
    boolean ok = result == TOTEST;
    StackTracer.printTestResult(PrefsManager.getKey(PREF), result);
    assertTrue(ok);
  }    
  
  @Test
  public void test04_setAndGetLong() {
    final String PREF = "long";
    final int TOTEST = 123456;

    StackTracer.printCurrentTestMethod();
    PrefsManager.setLong(PREF, TOTEST);
    long result = PrefsManager.getLong(PREF);
    
    // on compare le résultat avec celui attendu
    boolean ok = result == TOTEST;
    StackTracer.printTestResult(PrefsManager.getKey(PREF), result);
    assertTrue(ok);
  }      
  
  @Test
  public void test05_setAndGetFloat() {
    final String PREF = "float";
    final float TOTEST = 3.14159f;

    StackTracer.printCurrentTestMethod();
    PrefsManager.setFloat(PREF, TOTEST, 2);
    float result = PrefsManager.getFloat(PREF);
    
    // on compare le résultat avec celui attendu
    boolean ok = result == 3.14f;
    StackTracer.printTestResult(PrefsManager.getKey(PREF), result);
    assertTrue(ok);
  }      
  
  @Test
  public void test06_setAndGetDouble() {
    final String PREF = "double";
    final double TOTEST = 3.14159d;

    StackTracer.printCurrentTestMethod();
    PrefsManager.setDouble(PREF, TOTEST, 4);
    double result = PrefsManager.getDouble(PREF);
    
    // on compare le résultat avec celui attendu
    boolean ok = result == 3.1416d;
    StackTracer.printTestResult(PrefsManager.getKey(PREF), result);
    assertTrue(ok);
  }        
  
  @Test
  public void test07_setAndGetObject() {
    final String PREF = "object";
    final Classe TOTEST = new Classe("1i1", "Informatique");

    StackTracer.printCurrentTestMethod();
    PrefsManager.setObject(PREF, TOTEST);
    Classe result = (Classe) PrefsManager.getObject(PREF);

    // on compare le résultat avec celui attendu
    boolean ok = result.toString().equals(TOTEST.toString());
    StackTracer.printTestResult(PrefsManager.getKey(PREF), result);
    assertTrue(ok);

  }  
  
  
}
