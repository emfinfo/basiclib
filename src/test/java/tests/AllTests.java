package tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Suite de tests pour cette librairie. Certaines données se trouvent
 * dans le dossier "data" du projet.
 *
 * @author jcstritt
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = {
  JavaLibTest.class,
  SystemLibTest.class,
  InObjectTest.class,
  MathLibTest.class,
  ConvertLibTest.class,
  DateTimeLibTest.class,
  FileHelperTest.class,
  PropertyReaderTest.class,
  BinaryFileReaderTest.class,
  TextFileReaderTest.class,
  PrintHelperTest.class,
  PrefsManagerTest.class
})

public class AllTests {

  @BeforeClass
  public static void setUpClass() throws Exception {
//    System.out.println("Default CHARSET: " + Charset.defaultCharset().name() + "\n");
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

}
