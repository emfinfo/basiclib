package tests;

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

  public JavaLibTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Test
  public void testGetJavaPlatformBits() {
    StackTracer.printCurrentTestMethod();
    int result = JavaLib.getJavaPlatformBits();
    System.out.println("- Java " + result + " bits");
    assertTrue(result == 32 || result == 64);
  }

  @Test
  public void testGetJavaClassVersion() {
    StackTracer.printCurrentTestMethod();
    String filename = "target/classes/ch/jcsinfo/system/JavaLib.class";
    int[] result = JavaLib.getJavaClassVersion(filename);
    System.out.println("- Java major: " + result[0]);
    System.out.println("- Java minor: " + result[1]);
    assertTrue(result[0] > 0);
  }

  /**
   * Test of getJavaClassPlatform method, of class JavaLib.
   */
  @Test
  public void testGetJavaClassPlatform() {
    StackTracer.printCurrentTestMethod();
    String filename = "target/classes/ch/jcsinfo/system/JavaLib.class";
    String result = JavaLib.getJavaClassPlatform(filename);
    System.out.println("- Java platform: " + result);
    assertTrue(result.contains("JDK") || result.contains("J2SE"));
  }

}
