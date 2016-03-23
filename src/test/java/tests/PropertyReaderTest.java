package tests;

import ch.jcsinfo.file.PropertyReader;
import ch.jcsinfo.system.StackTracer;
import java.awt.Color;
import java.awt.Font;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * Test des méthodes principales de la classe correspondante.
 * 
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PropertyReaderTest {
  static final String PROP_FILE = "App.properties";
  
  static PropertyReader pr;

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
    pr = new PropertyReader("data/" + PROP_FILE);
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_getTextProperty() {
    StackTracer.printCurrentTestMethod();
    String s = pr.getTextProperty("Application.vendorId", "EIKON");
    StackTracer.printTestInfo(PROP_FILE, s);
    assertTrue(s.equals("EMF"));
  }

  @Test
  public void test02_getIntProperty() {
    StackTracer.printCurrentTestMethod();
    int i = pr.getIntProperty("Application.splash.bubble.size", 0);
    StackTracer.printTestInfo(PROP_FILE, i);
    assertTrue(i > 0);
  }

  @Test
  public void test03_getBooleanProperty() {
    StackTracer.printCurrentTestMethod();
    boolean b = pr.getBooleanProperty("Application.autostart", false);
    StackTracer.printTestInfo(PROP_FILE, b);
    assertTrue(b);
  }

  @Test
  public void test04_getFontProperty() {
    StackTracer.printCurrentTestMethod();
    Font f1 = new Font("Arial", 0, 12);
    Font f2  = pr.getFontProperty("Application.bgText1.font", f1);
    StackTracer.printTestInfo(PROP_FILE, f2.getFontName());
    assertTrue(f2.getFontName().contains("Comic"));
  }

  @Test
  public void test05_getColorProperty() {
    StackTracer.printCurrentTestMethod();
    Color c1 = Color.red;
    Color c2  = pr.getColorProperty("Application.bgText1.color", c1);
    StackTracer.printTestInfo(PROP_FILE, c2.toString());
    assertTrue(!c2.equals(c1));
  }

  @Test
  public void test06_getDbProperties() {
    StackTracer.printCurrentTestMethod();
    Properties p = pr.getDbProperties();
    StackTracer.printTestInfo(PROP_FILE, p.size());
    assertTrue(p.size() > 0);
  }
}
