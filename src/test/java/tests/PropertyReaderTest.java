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
    pr = new PropertyReader("data/" + PROP_FILE);
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_getTextProperty() {
    StackTracer.printCurrentTestMethod();
    String KEY = "Application.vendorId";
    String result = pr.getTextProperty(KEY, "EIKON");
    
    // on compare le résultat avec celui attendu
    boolean ok = result.equals("EMF");
    StackTracer.printTestResult("Source", PROP_FILE, "Key", KEY, "Value", result);
    assertTrue(ok);      
  }

  @Test
  public void test02_getIntProperty() {
    StackTracer.printCurrentTestMethod();
    String KEY = "Application.splash.bubble.size";
    int result = pr.getIntProperty(KEY, 0);
    
    // on compare le résultat avec celui attendu
    boolean ok = result > 0;
    StackTracer.printTestResult("Source", PROP_FILE, "Key", KEY, "Value", result);
    assertTrue(ok);        
  }

  @Test
  public void test03_getBooleanProperty() {
    StackTracer.printCurrentTestMethod();
    String KEY = "Application.autostart";
    boolean result = pr.getBooleanProperty(KEY, false);
    
    // on compare le résultat avec celui attendu
    boolean ok = result==true;
    StackTracer.printTestResult("Source", PROP_FILE, "Key", KEY, "Value", result);
    assertTrue(ok);         
  }

  @Test
  public void test04_getFontProperty() {
    StackTracer.printCurrentTestMethod();
    String KEY = "Application.bgText1.font";
    Font f1 = new Font("Arial", 0, 12);
    Font f2  = pr.getFontProperty(KEY, f1);
    
    // on compare le résultat avec celui attendu
    boolean ok = f2.getFontName().contains("Comic");
    StackTracer.printTestResult("Source", PROP_FILE, "Key", KEY, "Value", f2);
    assertTrue(ok);   
  }

  @Test
  public void test05_getColorProperty() {
    StackTracer.printCurrentTestMethod();
    String KEY = "Application.bgText1.color";
    Color c1 = Color.red;
    Color c2  = pr.getColorProperty(KEY, c1);
    
    // on compare le résultat avec celui attendu
    boolean ok = !c2.equals(c1);
    StackTracer.printTestResult("Source", PROP_FILE, "Key", KEY, "Value", c2.toString());
    assertTrue(ok);       
  }

  @Test
  public void test06_getDbProperties() {
    StackTracer.printCurrentTestMethod();
    Properties p = pr.getDbProperties();
    
    // on compare le résultat avec celui attendu
    boolean ok = p.size() > 0;
    StackTracer.printTestResult("Source", PROP_FILE, "Result", p.size());
    assertTrue(ok);       
  }
}
