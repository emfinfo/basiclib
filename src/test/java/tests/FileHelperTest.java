package tests;

import ch.jcsinfo.file.FileHelper;
import ch.jcsinfo.system.StackTracer;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
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
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
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
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals(PATHNAME, result);
  }

  @Test
  public void test02_extractFileName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractFileName(ABSOLUTE_1);
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals(FILE_NAME_1 + FILE_EXT_1, result);
  }

  @Test
  public void test03_extractOnlyName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractOnlyName(ABSOLUTE_1);
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals("FileLib", result);
  }

  @Test
  public void test04_extractFileExt() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractFileExt(ABSOLUTE_1);
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals(FILE_EXT_1, result);
  }

  @Test
  public void test05_replaceFileExt() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.replaceFileExt(ABSOLUTE_1, FILE_EXT_2);
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals(ABSOLUTE_2, result);
  }

  @Test
  public void test06_replaceOnlyName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.replaceOnlyName(ABSOLUTE_1, FILE_NAME_2);
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals(ABSOLUTE_3, result);
  }

  @Test
  public void test07_addToFileName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.addToFileName(ABSOLUTE_1, FILE_ADD_PART);
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals(ABSOLUTE_4, result);
  }

  @Test
  public void test08_getRelativePath() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.getRelativePath(ABSOLUTE_1, CURRENT_DIR);
    StackTracer.printTestInfo(ABSOLUTE_1, result);
    assertEquals(RELATIVE_1, result);
  }

  @Test
  public void test09_getAbsolutePath() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.getAbsolutePath(RELATIVE_1);
    StackTracer.printTestInfo(RELATIVE_1, result);
    assertEquals(ABSOLUTE_1, result);
  }

  @Test
  public void test10_getFolderFiles() {
    StackTracer.printCurrentTestMethod();
    List<String> files = FileHelper.getFolderFiles(PATH, ".txt");
    boolean ok = files != null && files.size()==2;
    StackTracer.printTestInfo(PATH + " (.txt)", files.size());
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
    StackTracer.printTestInfo(filePath, result);
    boolean ok = result.length() > filePath.length();
    assertTrue(ok);
  }

  @Test
  public void test12_urlToFilePath() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath(XML_FILE);
    String url = FileHelper.filePathToURL(filePath);
    String result = FileHelper.urlToFilePath(url);
    StackTracer.printTestInfo(url, result);
    boolean ok = result.length() > 0;
    assertTrue(ok);
  }

  @Test
  public void test13_loadProperties() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath("data/" + PROP_FILE);
    Properties properties = FileHelper.loadProperties(filePath);
    StackTracer.printTestInfo(FileHelper.getRelativePath(filePath, CURRENT_DIR), properties.size());
    boolean ok = properties.size() > 0;
    if (ok) {
      System.out.println();
      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        System.out.println("    " + entry.getKey() + " = " + entry.getValue());
      }
    }
    assertTrue(ok);
  }  
  
  @Test
  public void test14_loadXmlProperties() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath("data/" + XML_FILE);
    Properties properties = FileHelper.loadXmlProperties(filePath);
    StackTracer.printTestInfo(FileHelper.getRelativePath(filePath, CURRENT_DIR), properties.size());
    boolean ok = properties.size() > 0;
    if (ok) {
      System.out.println();
      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
        System.out.println("    " + entry.getKey() + " = " + entry.getValue());
      }
    }
    assertTrue(ok);
  }  
  
}
