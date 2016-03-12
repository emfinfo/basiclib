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
 * @author Jean-Claude Stritt
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={
  MathLibTest.class,
  ConvertLibTest.class,
  FileHelperTest.class,
  BinaryFileReaderTest.class,
  TextFileReaderTest.class,
  CypherTest.class,
  PrintHelperTest.class,
  JavaLibTest.class})
public class AllTests {

  @BeforeClass
  public static void setUpClass() throws Exception {
//    System.out.println("Default CHARSET: " + Charset.defaultCharset().name() + "\n");
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

}