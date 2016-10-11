package tests;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.system.SystemLib;
import java.util.List;
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
public class SystemLibTest {

  @BeforeClass
  public static void setUpClass() {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
    SystemLib.resetLog4j(SystemLibTest.class.getClassLoader());
    SystemLib.changeCharset("UTF-8");
//    SystemLib.sleep(1000);
//    SystemLib.openDesktopFile("src/test/resources/log4j.properties");
  }

  @AfterClass
  public static void tearDownClass() {
    System.out.println();
  }

  @Test
  public void test01_getCharsetList() {
    StackTracer.printCurrentTestMethod();
    List<String> list = SystemLib.getCharsetList();
    StackTracer.printTestInfo("System", list.size());
    boolean ok = list != null && list.size() > 0;
    if (ok) {
      System.out.println();
      for (String charset : list) {
        if (charset.contains("UTF")) {
          System.out.println("    " + charset);
        }
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test02_isMacOrWindows() {
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
  public void test03_getMemoryUsage() {
    StackTracer.printCurrentTestMethod();
    float[] result = SystemLib.getMemoryUsage();
    System.out.println("  - used memory:  " + String.format("%.1f", result[0]) + " Mb");
    System.out.println("  - free memory:  " + String.format("%.1f", result[1]) + " Mb");
    System.out.println("  - total memory: " + String.format("%.1f", result[2]) + " Mb");
    System.out.println("  - max memory:   " + String.format("%.1f", result[3]) + " Mb");
    assertTrue(result[0] > 0);
  }

}
