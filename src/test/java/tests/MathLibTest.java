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
 * Test des méthodes principales de la classe correspondante.
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MathLibTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_roundValue() {
    StackTracer.printCurrentTestMethod();
    final double delta = 1e-15;

    final float p1 = 0.25f;
    final double v1a = 4.62499999;
    final double v1b = 4.625;
    final double r1a = 4.50;
    final double r1b = 4.75;

    final float p2 = 0.5f;
    final double v2a = 4.74999999;
    final double v2b = 4.75;
    final double r2a = 4.5;
    final double r2b = 5.0;

    final float p3 = 10f;
    final double v3a = 104.499999;
    final double v3b = 105;
    final double r3a = 100;
    final double r3b = 110;

    final float p4 = 0.05f;
    final double v4a = 104.499999;
    final double v4b = 155;
    final double r4a = 104.50;
    final double r4b = 155;
    
    
    System.out.println("  - " + v1a + " rounded to " + p1 + " = " + MathLib.roundDoubleValue(v1a, p1));
    System.out.println("  - " + v1b + " rounded to " + p1 + " = " + MathLib.roundDoubleValue(v1b, p1));

    System.out.println("  - " + v2a + " rounded to " + p2 + " = " + MathLib.roundDoubleValue(v2a, p2));
    System.out.println("  - " + v2b + " rounded to " + p2 + " = " + MathLib.roundDoubleValue(v2b, p2));

    System.out.println("  - " + v3a + " rounded to " + p3 + " = " + MathLib.roundDoubleValue(v3a, p3));
    System.out.println("  - " + v3b + " rounded to " + p3 + " = " + MathLib.roundDoubleValue(v3b, p3));

    System.out.println("  - " + v4a + " rounded to " + p4 + " = " + MathLib.convertToBigDecimal(v4a, p4));
    System.out.println("  - " + v4b + " rounded to " + p4 + " = " + MathLib.convertToBigDecimal(v4b, p4));
    
    assertEquals(MathLib.roundDoubleValue(v1a, p1), r1a, delta);
    assertEquals(MathLib.roundDoubleValue(v1b, p1), r1b, delta);

    assertEquals(MathLib.roundDoubleValue(v2a, p2), r2a, delta);
    assertEquals(MathLib.roundDoubleValue(v2b, p2), r2b, delta);

    assertEquals(MathLib.roundDoubleValue(v3a, p3), r3a, delta);
    assertEquals(MathLib.roundDoubleValue(v3b, p3), r3b, delta);

    assertEquals(MathLib.convertToBigDecimal(v4a, p4).doubleValue(), r4a, delta);
    assertEquals(MathLib.convertToBigDecimal(v4b, p4).doubleValue(), r4b, delta);
    
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
