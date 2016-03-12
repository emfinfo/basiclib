package tests;

import ch.jcsinfo.file.FileHelper;
import ch.jcsinfo.system.JavaLib;
import ch.jcsinfo.system.StackTracer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jcstritt
 */
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
  }

  @Test
  public void testGetJavaPlatformBits() {
    StackTracer.printCurrentTestMethod();
    int result = JavaLib.getJavaPlatformBits();
    System.out.println("  - Java " + result + " bits");
    assertTrue(result == 32 || result == 64);
  }

  @Test
  public void testGetJavaClassVersion() {
    StackTracer.printCurrentTestMethod();
    int[] result = JavaLib.getJavaClassVersion(CLASS_TO_CHECK);
    System.out.println("  - Java major: " + result[0]);
    System.out.println("  - Java minor: " + result[1]);
    assertTrue(result[0] > 0);
  }

  /**
   * Test of getJavaClassPlatform method, of class JavaLib.
   */
  @Test
  public void testGetJavaClassPlatform() {
    StackTracer.printCurrentTestMethod();
    String result = JavaLib.getJavaClassPlatform(CLASS_TO_CHECK);
    System.out.println("  - Java platform: " + result);
    assertTrue(result.contains("JDK") || result.contains("J2SE"));
  }

}
