package tests;

import ch.jcsinfo.file.FileHelper;
import ch.jcsinfo.system.StackTracer;
import java.io.File;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test de quelques méthodes importantes de FileHelper
 *
 * @author J.-C. Stritt
 */
public class FileHelperTest {
  static final String CURRENT_DIR = System.getProperty("user.dir");
  static final String PACKAGE_NAME = "src/ch/jcsinfo/file";
  static final String FILE_NAME_1 = "FileLib";
  static final String FILE_NAME_2 = "NewFile";
  static final String FILE_EXT_1 = ".java";
  static final String FILE_EXT_2 = ".class";
  static final String FILE_ADD_PART = "_new";

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
    StackTracer.printCurrentTestMethod();
    System.out.println("User dir: ");
    System.out.println("- " +CURRENT_DIR + "\n");

    File absFile = new File(ABSOLUTE_1);
    File relFile = new File(RELATIVE_1);

    System.out.println("Absolute file path and name: ");
    System.out.println("- " + absFile.getPath());
    System.out.println("- " + absFile.getName() + "\n");

    System.out.println("Relative file path and name: ");
    System.out.println("- " + relFile.getPath());
    System.out.println("- " + relFile.getName());
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Test
  public void testExtractPathName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractPathName(ABSOLUTE_1);
    System.out.println(result);
    assertEquals(PATHNAME, result);
  }

  @Test
  public void testExtractFileName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractFileName(ABSOLUTE_1);
    System.out.println(result);
    assertEquals(FILE_NAME_1 + FILE_EXT_1, result);
  }

  @Test
  public void testExtractOnlyName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractOnlyName(ABSOLUTE_1);
    System.out.println(result);
    assertEquals("FileLib", result);
  }

  @Test
  public void testExtractFileExt() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.extractFileExt(ABSOLUTE_1);
    System.out.println(result);
    assertEquals(FILE_EXT_1, result);
  }

  @Test
  public void testReplaceFileExt() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.replaceFileExt(ABSOLUTE_1, FILE_EXT_2);
    System.out.println(result);
    assertEquals(ABSOLUTE_2, result);
  }

  @Test
  public void testReplaceFileName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.replaceOnlyName(ABSOLUTE_1, FILE_NAME_2);
    System.out.println(result);
    assertEquals(ABSOLUTE_3, result);
  }

  @Test
  public void testAddToFileName() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.addToFileName(ABSOLUTE_1, FILE_ADD_PART);
    System.out.println(result);
    assertEquals(ABSOLUTE_4, result);
  }


  @Test
  public void testGetRelative() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.getRelativePath(ABSOLUTE_1, CURRENT_DIR);
    System.out.println(result);
    assertEquals(RELATIVE_1, result);
  }

//  @Test
//  public void testGetRelativePathsUnixy() {
//    assertEquals(FileHelper.normalizeFile("stuff/xyz.dat"), FileHelper.getRelativePath(
//      "/var/data/stuff/xyz.dat", "/var/data/"));
//    assertEquals(FileHelper.normalizeFile("../../b/c"), FileHelper.getRelativePath(
//      "/a/b/c", "/a/x/y/"));
//    assertEquals(FileHelper.normalizeFile("../../b/c"), FileHelper.getRelativePath(
//      "/m/n/o/a/b/c", "/m/n/o/a/x/y/"));
//  }
//
//  @Test
//  public void testGetRelativePathFileToFile() {
//    String target = "C:\\Windows\\Boot\\Fonts\\chs_boot.ttf";
//    String base = "C:\\Windows\\Speech\\Common\\sapisvr.exe";
//
//    String relPath = FileHelper.getRelativePath(target, base);
//    assertEquals("..\\..\\..\\Boot\\Fonts\\chs_boot.ttf", relPath);
//  }
//
//  @Test
//  public void testGetRelativePathDirectoryToFile() {
//    String target = "C:\\Windows\\Boot\\Fonts\\chs_boot.ttf";
//    String base = "C:\\Windows\\Speech\\Common";
//
//    String relPath = FileHelper.getRelativePath(target, base);
//    assertEquals("..\\..\\Boot\\Fonts\\chs_boot.ttf", relPath);
//  }
//
//  @Test
//  public void testGetRelativePathDifferentDriveLetters() {
//    String target = "D:\\sources\\recovery\\RecEnv.exe";
//    String base = "C:\\Java\\workspace\\AcceptanceTests\\Standard test data\\geo\\";
//
//    //  Should just return the target path because of the incompatible roots.
//    String relPath = FileHelper.getRelativePath(target, base);
//    assertEquals(target, relPath);
//  }

  @Test
  public void testGetAbsolutePath() {
    StackTracer.printCurrentTestMethod();
    String result = FileHelper.getAbsolutePath(RELATIVE_1);
    System.out.println(result);
    assertEquals(ABSOLUTE_1, result);
  }

  @Test
  public void testGetFolderFiles() {
    StackTracer.printCurrentTestMethod();
    List<String> files = FileHelper.getFolderFiles(PATH, ".txt");
    boolean ok = files != null && files.size()==2;
    assertTrue(ok);
    if (ok) {
      for (String fname : files) {
        System.out.println("- " + fname);
      }
    }
  }

  @Test
  public void testFilePathToUrl() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath("build.xml");
    String result = FileHelper.filePathToURL(filePath);
    boolean ok = result.length() > filePath.length();
    System.out.println("- filePath: " + filePath);
    System.out.println("- result:   " + result);
    assertTrue(ok);
  }

  @Test
  public void testUrlToFilePath() {
    StackTracer.printCurrentTestMethod();
    String filePath = FileHelper.getAbsolutePath("build.xml");
    String url = FileHelper.filePathToURL(filePath);
    String result = FileHelper.urlToFilePath(url);
    boolean ok = result.length() > 0;
    System.out.println("result: " + result);
    assertTrue(ok);
  }

}
