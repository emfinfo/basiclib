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
  public void test01_getJavaPlatformBits() {
    StackTracer.printCurrentTestMethod();
    int result = JavaLib.getJavaPlatformBits();
    System.out.println("  - Java " + result + " bits");
    assertTrue(result == 32 || result == 64);
  }

  @Test
  public void test02_getJavaClassVersion() {
    StackTracer.printCurrentTestMethod();
    int[] result = JavaLib.getJavaClassVersion(CLASS_TO_CHECK);
    System.out.println("  - Java major: " + result[0]);
    System.out.println("  - Java minor: " + result[1]);
    assertTrue(result[0] > 0);
  }

  @Test
  public void test03_getJavaClassPlatform() {
    StackTracer.printCurrentTestMethod();
    String result = JavaLib.getJavaClassPlatform(CLASS_TO_CHECK);
    System.out.println("  - Java platform: " + result);
    assertTrue(result.contains("JDK") || result.contains("J2SE"));
  }

}
