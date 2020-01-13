package tests;

import ch.jcsinfo.file.FileException;
import ch.jcsinfo.file.FileHelper;
import ch.jcsinfo.system.JavaLib;
import ch.jcsinfo.system.StackTracer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * Test des méthodes principales de la classe correspondante.
 * 
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaLibTest {
  private static String CLASS_TO_CHECK = "target/test-classes/beans/Compte.class";

  @BeforeClass
  public static void setUpClass() {
    if (!FileHelper.isFileExists(CLASS_TO_CHECK)) {
      CLASS_TO_CHECK = "build/test/classes/beans/Compte.class";
    }
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println();
  }

  @Test
  public void test01_getJavaVersion() {
    StackTracer.printCurrentTestMethod();
    String result = JavaLib.getJavaVersion();
    
    // on compare le résultat avec celui attendu
    boolean ok = !result.isEmpty();
    StackTracer.printTestResult("Version", result);
    assertTrue(ok);
  }

  @Test
  public void test02_getJavaDataModel() {
    StackTracer.printCurrentTestMethod();
    int result = JavaLib.getJavaDataModel();
    
    // on compare le résultat avec celui attendu
    boolean ok = result == 32 || result == 64;
    StackTracer.printTestResult("Bits", result);
    assertTrue(ok);    
  }
  
  @Test
  public void test03_getJavaFullVersion() {
    StackTracer.printCurrentTestMethod();
    String result = JavaLib.getJavaFullVersion();
    
    // on compare le résultat avec celui attendu
    boolean ok = !result.isEmpty() && (result.contains("32") || result.contains("64"));
    StackTracer.printTestResult("Version", result);
    assertTrue(ok);  
  }  

  @Test
  public void test04_getJavaClassVersion() {
    StackTracer.printCurrentTestMethod();
    int[] result = new int[2];
    try {
      result = JavaLib.getJavaClassVersion(CLASS_TO_CHECK);
    } catch (FileException ex) {
    }
    boolean ok = result[0] > 0;
    StackTracer.printTestResult("Major", result[0], "Minor", result[1]);
    assertTrue(ok);     
  }

  @Test
  public void test05_getJavaClassPlatform() {
    StackTracer.printCurrentTestMethod();
    String result = "";
    try {
      result = JavaLib.getJavaClassPlatform(CLASS_TO_CHECK);
    } catch (FileException ex) {
    }
    boolean ok = result.contains("JDK") || result.contains("J2SE");
    StackTracer.printTestResult("Platform", result);
    assertTrue(ok);         
  }


}
