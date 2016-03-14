package tests;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.system.SystemLib;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemLibTest {

  @BeforeClass
  public static void setUpClass() {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
    SystemLib.resetLog4j(SystemLibTest.class.getClassLoader());
    SystemLib.changeCharset("UTF");
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println();
  }

  @Test
  public void test07_isMacOrWindows() {
    StackTracer.printCurrentTestMethod();
    String os = System.getProperty("os.name");
    boolean isMac = SystemLib.isMacOS();
    boolean isWin = SystemLib.isWindows();
    boolean ok = (isMac && !isWin) || (isWin && !isMac);
    System.out.println("  - isMacOS: " + isMac + " (" + os + ")");
    System.out.println("  - isWindows: " + isWin + " (" + os + ")");
    assertTrue(ok);
  }

  @Test
  public void test09_getMemoryUsage() {
    StackTracer.printCurrentTestMethod();
    float[] result = SystemLib.getMemoryUsage();
    System.out.println("  - used memory:  " + String.format("%.1f", result[0]) + " Mb");
    System.out.println("  - free memory:  " + String.format("%.1f", result[1]) + " Mb");
    System.out.println("  - total memory: " + String.format("%.1f", result[2]) + " Mb");
    System.out.println("  - max memory:   " + String.format("%.1f", result[3]) + " Mb");
    assertTrue(result[0] > 0);
  }

}
