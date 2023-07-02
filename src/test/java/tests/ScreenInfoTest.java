package tests;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.ScreenInfo;
import java.awt.Font;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test de 2 méthodes de ScreenInfo.
 * 
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScreenInfoTest {
  
  public ScreenInfoTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }

  @Test
  public void test01_getTextWidth() {
    StackTracer.printCurrentTestMethod();
    
    // on teste avec le mot "Bonjour" et une police choisie
    Font font = new Font("Arial", Font.PLAIN, 8);
    int result = ScreenInfo.getTextWidth("Bonjour", font);
    
    // on compare le résultat avec celui attendu
    boolean ok = result == 26;
    StackTracer.printTestResult("Width", result);    
    assertTrue(ok); 
  }

  @Test
  public void test02_getTextWidthMm() {
    StackTracer.printCurrentTestMethod();

    // on teste avec le mot "Bonjour" et une police choisie
    Font font = new Font("Arial", Font.PLAIN, 8);
    double result = ScreenInfo.getTextWidthMm("Bonjour", font);
    
    // on compare le résultat avec celui attendu
    boolean ok = result > 9.0; // [mm]
    StackTracer.printTestResult("Width", result);    
    assertTrue(ok); 
  }
  
}
