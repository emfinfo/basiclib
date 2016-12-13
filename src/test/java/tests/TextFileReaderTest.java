package tests;

import beans.Departement;
import beans.Localite;
import ch.jcsinfo.file.TextFileReader;
import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.ConvertLib;
import helpers.DepartementExtracter;
import helpers.LocaliteExtracter;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test des méthodes principales de la classe correspondante.
 * 
 * @author J.-C. Stritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextFileReaderTest {
  static final String PATH = "data";
  static final String LOC_FILE = "npa_20090825.txt";
  static final String DEP_FILE = "departements.txt";
  static final boolean SHOW_DETAILS = true;

  public TextFileReaderTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_textFileRead_NPA() {
    StackTracer.printCurrentTestMethod();
    Class<Localite> cl = Localite.class;
    TextFileReader<Localite> fileDao = new TextFileReader<>(new LocaliteExtracter("\t"));
    List<Localite> list = fileDao.read(PATH + "/" + LOC_FILE);
    boolean ok = list != null && list.size() > 0;
    StackTracer.printTestResult(LOC_FILE, list.size());
    if (ok && SHOW_DETAILS) {
      System.out.println();
      int n = 0;
      for (Localite localite : list) {
        if (n % 200 == 0) {
          System.out.println("    " + ConvertLib.formatNumber(n, "0000") + ". " + localite.getNpa() + " " + localite.getLocalite());
        }  
        n++;
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test02_textFileRead_Departement() {
    StackTracer.printCurrentTestMethod();
    Class<Departement> cl = Departement.class;
    TextFileReader<Departement> fileDao = new TextFileReader<>(new DepartementExtracter(";"));
    List<Departement> list = fileDao.read(PATH + "/" + DEP_FILE);
    boolean ok = list != null && list.size() > 0;
    StackTracer.printTestResult(DEP_FILE, list.size());
    assertTrue(ok);
    if (ok && SHOW_DETAILS) {
      System.out.println();
      int n = 1;
      for (Departement departement : list) {
        System.out.println("    " + ConvertLib.formatNumber(n, "00")+ ". " + departement.getDepartement());
        n++;
      }
    }
  }

}
