package tests;

import beans.Localite;
import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.file.FileException;
import ch.jcsinfo.file.FileHelper;
import ch.jcsinfo.file.ObjectCloner;
import ch.jcsinfo.system.StackTracer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
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
public class FileHelperTest {
  static final String CURRENT_DIR = System.getProperty("user.dir");
  static final String PACKAGE_NAME = "src/ch/jcsinfo/file";
  static final String FILE_NAME_1 = "FileLib";
  static final String FILE_NAME_2 = "NewFile";
  static final String FILE_EXT_1 = ".java";
  static final String FILE_EXT_2 = ".class";
  static final String FILE_ADD_PART = "_new";
  static final String XML_FILE = "persistence.xml";
  static final String PROP_FILE = "App.properties";

  static final String PATHNAME = FileHelper.normalizeFileName(CURRENT_DIR + "/" + PACKAGE_NAME);
  static final String RELATIVE_1 = FileHelper.normalizeFileName(PACKAGE_NAME + "/" + FILE_NAME_1 + FILE_EXT_1);
  static final String ABSOLUTE_1 = FileHelper.normalizeFileName(PATHNAME + "/" + FILE_NAME_1 + FILE_EXT_1);
  static final String ABSOLUTE_2 = FileHelper.normalizeFileName(PATHNAME + "/" + FILE_NAME_1 + FILE_EXT_2);
  static final String ABSOLUTE_3 = FileHelper.normalizeFileName(PATHNAME + "/" + FILE_NAME_2 + FILE_EXT_1);
  static final String ABSOLUTE_4 = FileHelper.normalizeFileName(PATHNAME + "/" + FILE_NAME_1 + FILE_ADD_PART + FILE_EXT_1);

  static final String PATH = "data";
  static final String LOC_FILE = "npa_20090825.txt";
  static final String DEP_FILE = "departements.txt";

  public FileHelperTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    File absFile = new File(ABSOLUTE_1);
    File relFile = new File(RELATIVE_1);

//    System.out.println("User dir: ");
//    System.out.println("  - " +CURRENT_DIR + "\n");

//    System.out.println("Absolute file path and name: ");
//    System.out.println("  - " + absFile.getPath());
//    System.out.println("  - " + absFile.getName() + "\n");

//    System.out.println("Relative file path and name: ");
//    System.out.println("  - " + relFile.getPath());
//    System.out.println("  - " + relFile.getName());

  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_extractPathName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractPathName(ABSOLUTE_1);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(PATHNAME);
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test02_extractFileName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractFileName(ABSOLUTE_1);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(FILE_NAME_1 + FILE_EXT_1);
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test03_extractOnlyName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractOnlyName(ABSOLUTE_1);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals("FileLib");
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test04_extractFileExt() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractFileExt(ABSOLUTE_1);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(FILE_EXT_1);
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test05_replaceFileExt() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.replaceFileExt(ABSOLUTE_1, FILE_EXT_2);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(ABSOLUTE_2);
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test06_replaceOnlyName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.replaceOnlyName(ABSOLUTE_1, FILE_NAME_2);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(ABSOLUTE_3);
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test07_addToFileName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.addToFileName(ABSOLUTE_1, FILE_ADD_PART);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(ABSOLUTE_4);
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test08_getRelativePath() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.getRelativePath(ABSOLUTE_1, CURRENT_DIR);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(RELATIVE_1);
    StackTracer.printTestResult("Source", ABSOLUTE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test09_getAbsolutePath() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.getAbsolutePath(RELATIVE_1);

    // on compare le résultat avec celui attendu
    boolean ok = result.equals(ABSOLUTE_1);
    StackTracer.printTestResult("Source", RELATIVE_1, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test10_getFolderFiles() {
    StackTracer.printCurrentTestMethod();
    List<String> files = new ArrayList<>();
    try {
      files = FileHelper.getFolderFiles(PATH, ".txt");
    } catch (FileException ex) {
    }
    boolean ok = files.size() == 2;
    StackTracer.printTestResult("Source", PATH + " (.txt)", "Nb", files.size());
    assertTrue(ok);
    if (ok) {
      System.out.println();
      for (String fname : files) {
        System.out.println("    " + fname);
      }
    }
  }

  @Test
  public void test11_filePathToUrl() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath(XML_FILE);
    String result = FileHelper.filePathToURL(filePath);

    // on compare le résultat avec celui attendu
    boolean ok = result.length() > filePath.length();
    StackTracer.printTestResult("Source", filePath, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test12_urlToFilePath() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath(XML_FILE);
    String url = FileHelper.filePathToURL(filePath);
    String result = FileHelper.urlToFilePath(url);

    // on compare le résultat avec celui attendu
    boolean ok = result.length() > 0;
    StackTracer.printTestResult("Source", url, "Result", result);
    assertTrue(ok);
  }

  @Test
  public void test13_loadProperties() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath("data/" + PROP_FILE);
    String relPath = FileHelper.getRelativePath(filePath, CURRENT_DIR);

    // le résultat attendu
    Properties props = new Properties();
    try {
      props = FileHelper.loadProperties(filePath);
    } catch (FileException ex) {
      System.out.println(ex);
    }

    // on compare le résultat avec celui attendu
    boolean ok = props.size() > 0;
    StackTracer.printTestResult("Source", relPath, "Size", props.size());
    if (ok) {
      System.out.println();
      for (Map.Entry<Object, Object> entry : props.entrySet()) {
        System.out.println("    " + entry.getKey() + " = " + entry.getValue());
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test14_loadXmlProperties() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath("data/" + XML_FILE);
    String relPath = FileHelper.getRelativePath(filePath, CURRENT_DIR);

    // le résultat attendu
    Properties props = new Properties();
    try {
      props = FileHelper.loadXmlProperties(filePath);
    } catch (FileException ex) {
      System.out.println(ex);
    }

    // on compare le résultat avec celui attendu
    boolean ok = props.size() > 0;
    StackTracer.printTestResult("Source", relPath, "Size", props.size());
    if (ok) {
      System.out.println();
      for (Map.Entry<Object, Object> entry : props.entrySet()) {
        System.out.println("    " + entry.getKey() + " = " + entry.getValue());
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test15_cloneObject() throws FileException {
    StackTracer.printCurrentTestMethod();
    DateTimeLib.chronoReset();
    Localite loc1 = new Localite(1, 1700, "Fribourg", "FR");
    Localite loc2 = (Localite) ObjectCloner.clone(loc1);
    StackTracer.printTestResult("Source", loc1, "Clone", loc2, "Time [ms]", DateTimeLib.chronoStringElapsedTime());
    assertTrue(loc1.equals(loc2));
  }

  @Test
  public void test16_factcopyObject() throws FileException {
    StackTracer.printCurrentTestMethod();
    DateTimeLib.chronoReset();
    Localite loc1 = new Localite(2, 1630, "Bulle", "FR");
    Localite loc2 = (Localite) ObjectCloner.fastcopy(loc1);
    StackTracer.printTestResult("Source", loc1, "Copy", loc2, "Time [ms]", DateTimeLib.chronoStringElapsedTime());
    assertTrue(loc1.equals(loc2));
  }

  @Test
  public void test17_serialize_deserialize() throws FileException {
    StackTracer.printCurrentTestMethod();
    DateTimeLib.chronoReset();
    Localite loc1 = new Localite(3, 3000, "Berne", "BE");
    ObjectCloner.serialize(loc1);
    Localite loc2 = (Localite) ObjectCloner.deserialize();
    StackTracer.printTestResult("Source", loc1, "Copy", loc2, "Time [ms]", DateTimeLib.chronoStringElapsedTime());
    assertTrue(loc1.equals(loc2));
  }

}
