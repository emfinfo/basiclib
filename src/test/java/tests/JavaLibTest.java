package tests;

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
 * Test de quelques méthodes de JavaLib.
 * 
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JavaLibTest {
  private static String CLASS_TO_CHECK = "target/test-classes/beans/Compte.class";

  @BeforeClass
  public static void setUpClass() {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
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
    System.out.println("  - " + result);
    assertTrue(!result.isEmpty());
  }

  @Test
  public void test02_getJavaVersionBits() {
    StackTracer.printCurrentTestMethod();
    int result = JavaLib.getJavaVersionBits();
    System.out.println("  - " + result + " bits");
    assertTrue(result == 32 || result == 64);
  }

  @Test
  public void test03_getJavaClassVersion() {
    StackTracer.printCurrentTestMethod();
    int[] result = JavaLib.getJavaClassVersion(CLASS_TO_CHECK);
    System.out.println("  - major: " + result[0]);
    System.out.println("  - minor: " + result[1]);
    assertTrue(result[0] > 0);
  }

  @Test
  public void test04_getJavaClassPlatform() {
    StackTracer.printCurrentTestMethod();
    String result = JavaLib.getJavaClassPlatform(CLASS_TO_CHECK);
    System.out.println("  - " + result);
    assertTrue(result.contains("JDK") || result.contains("J2SE"));
  }


}
