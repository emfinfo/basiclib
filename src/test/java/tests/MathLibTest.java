package tests;

import ch.jcsinfo.math.MathLib;
import ch.jcsinfo.system.StackTracer;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MathLibTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
    System.out.println("Current charset = " );
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }
  
  @Test
  public void test01_roundValue() {
    StackTracer.printCurrentTestMethod();
    final double delta = 1e-15;

    final double p1 = 0.25;
    final double v1a = 4.62499999;
    final double v1b = 4.625;
    final double r1a = 4.50;
    final double r1b = 4.75;

    final double p2 = 0.5;
    final double v2a = 4.74999999;
    final double v2b = 4.75;
    final double r2a = 4.5;
    final double r2b = 5.0;

    final double p3 = 10;
    final double v3a = 104.499999;
    final double v3b = 105;
    final double r3a = 100;
    final double r3b = 110;

    System.out.println("  - " + v1a + " rounded to " + p1 + " = " + MathLib.roundValue(v1a, p1));
    System.out.println("  - " + v1b + " rounded to " + p1 + " = " + MathLib.roundValue(v1b, p1));

    System.out.println("  - " + v2a + " rounded to " + p2 + " = " + MathLib.roundValue(v2a, p2));
    System.out.println("  - " + v2b + " rounded to " + p2 + " = " + MathLib.roundValue(v2b, p2));

    System.out.println("  - " + v3a + " rounded to " + p3 + " = " + MathLib.roundValue(v3a, p3));
    System.out.println("  - " + v3b + " rounded to " + p3 + " = " + MathLib.roundValue(v3b, p3));

    assertEquals(MathLib.roundValue(v1a, p1), r1a, delta);
    assertEquals(MathLib.roundValue(v1b, p1), r1b, delta);

    assertEquals(MathLib.roundValue(v2a, p2), r2a, delta);
    assertEquals(MathLib.roundValue(v2b, p2), r2b, delta);

    assertEquals(MathLib.roundValue(v3a, p3), r3a, delta);
    assertEquals(MathLib.roundValue(v3b, p3), r3b, delta);

  }

  @Test
  public void test02_columnNameToIndex() {
    StackTracer.printCurrentTestMethod();
    String t[] = {"A", "Z", "AA", "FB"};
    double expected[] = {0, 25, 26, 157};
    int results[] = new int[4];
    boolean ok[] = new boolean[4];

    // on teste les 4 exemples
    for (int i = 0; i < ok.length; i++) {
      results[i] = MathLib.columnNameToIndex(t[i]);
      ok[i] = results[i] == expected[i];
      System.out.println("  - columnNameToIndex("+t[i]+") = "+ results[i]);
    }
    assertTrue(ok[0] && ok[1] && ok[2]);
  }


}
