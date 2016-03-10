package tests;

import beans.Departement;
import beans.Localite;
import ch.jcsinfo.file.TextFileReader;
import ch.jcsinfo.system.StackTracer;
import helpers.DepartementExtracter;
import helpers.LocaliteExtracter;
import java.nio.charset.Charset;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test des méthodes de TextFileReader.
 *
 * @author J.-C. Stritt
 */
public class TextFileReaderTest {
  static final String PATH = "data";
  static final String LOC_FILE = "npa_20090825.txt";
  static final String DEP_FILE = "departements.txt";
  static final boolean SHOW_DETAILS = true;

  public TextFileReaderTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("Default charset: " + Charset.defaultCharset().displayName());
//    changeCharset("utf-8"); 
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Test
  public void testTextFileReadNPA() {
    StackTracer.printCurrentTestMethod();
    Class<Localite> cl = Localite.class;
    TextFileReader<Localite> fileDao = new TextFileReader<>(new LocaliteExtracter("\t"));
    List<Localite> list = fileDao.textFileRead(PATH + "/" + LOC_FILE);
    boolean ok = list != null && list.size() > 0;
    if (ok && SHOW_DETAILS) {
      int n = 0;
      for (Localite localite : list) {
        if (n % 200 == 0) {
          System.out.println(n + ". " + localite.getNpa() + " " + localite.getLocalite());
        }  
        n++;
      }
    }
    assertTrue(ok);
  }

  @Test
  public void testTextFileReadDepartement() {
    StackTracer.printCurrentTestMethod();
    Class<Departement> cl = Departement.class;
    TextFileReader<Departement> fileDao = new TextFileReader<>(new DepartementExtracter(";"));
    List<Departement> list = fileDao.textFileRead(PATH + "/" + DEP_FILE);
    boolean ok = list != null && list.size() > 0;
    assertTrue(ok);
    if (ok && SHOW_DETAILS) {
      for (Departement departement : list) {
        System.out.println("- " + departement.getDepartement());
      }
    }
  }

}
