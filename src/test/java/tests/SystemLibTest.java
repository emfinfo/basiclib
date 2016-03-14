package tests;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.system.SystemLib;
import java.lang.reflect.Method;
import java.util.List;
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
  private static String TEST_STRING = "BONJOUR";
  private static String TEST_METHOD = "replace";

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
  public void test02_findMethod() {
    StackTracer.printCurrentTestMethod();
    StringBuilder sb = new StringBuilder(TEST_STRING);
    Method m = SystemLib.findMethod(sb, TEST_METHOD, int.class, int.class, String.class);
    StackTracer.printTestInfo("« " + TEST_METHOD + " » in " + StringBuilder.class.getSimpleName(), m);
    assertTrue(m != null);
  }

  @Test
  public void test03_callMethod() {
    StackTracer.printCurrentTestMethod(" « " + TEST_METHOD + " »");
    StringBuilder s = new StringBuilder(TEST_STRING);
    Method m = SystemLib.findMethod(s, TEST_METHOD, int.class, int.class, String.class);
    boolean ok = m != null;
    if (ok) {
      Object result = SystemLib.callMethod(s, m, 0, 3, "SE");
      ok = result != null;
      if (ok) {
        String strResult = ((StringBuilder) result).toString();
        StackTracer.printTestInfo(TEST_STRING, strResult);
      }
    }
    assertTrue(ok);
  }
  
  @Test
  public void test04_isMacOrWindows() {
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
  public void test05_getMemoryUsage() {
    StackTracer.printCurrentTestMethod();
    float[] result = SystemLib.getMemoryUsage();
    System.out.println("  - used memory:  " + String.format("%.1f", result[0]) + " Mb");
    System.out.println("  - free memory:  " + String.format("%.1f", result[1]) + " Mb");
    System.out.println("  - total memory: " + String.format("%.1f", result[2]) + " Mb");
    System.out.println("  - max memory:   " + String.format("%.1f", result[3]) + " Mb");
    assertTrue(result[0] > 0);
  }

}
