package tests;

import ch.jcsinfo.printing.PrintHelper;
import ch.jcsinfo.models.Printer;
import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.ConvertLib;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.standard.Media;
import org.junit.AfterClass;
import static org.junit.Assert.*;
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
public class PrintHelperTest {
  private static final String TEST_PRINTER_NAME = "HP LaserJet P3010 Series";
//  private static final String TEST_PRINTER_NAME = "HP Laserjet 2430 (A45)";
//  private static final String RESULTS_FOLDER = "reports/results";
//  private static final String TEST_DOCUMENT_NAME = "report_test_file.pdf";
//  private static final String TEST_DOCUMENT_NAME = "safdemo_report_2012_11_17_184259.pdf";

//  private static String docURL;
//  private static String pdfURL;
//  private static String odtURL;

  @BeforeClass
  public static void setUpClass() {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
//    docURL = FileLib.filePathToURL(RESULTS_FOLDER + File.separator + TEST_DOCUMENT_NAME);
//    pdfURL = FileLib.replaceFileExt(docURL, ".pdf");
//    odtURL = FileLib.replaceFileExt(docURL, ".odt");
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }  
  
  @Test
  public void test01_getListOfPrinters() {
    StackTracer.printCurrentTestMethod();
    List<Printer> printers = PrintHelper.getListOfPrinters();
    boolean ok = printers.size() > 0;
    StackTracer.printTestInfo("System", printers.size());
    if (ok) {
      System.out.println();
      int n = 1;
      for (Printer printer : printers) {
        System.out.println("    " + ConvertLib.formatNumber(n, "00") + ". " + printer);
        n++;
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test02_findDefaultPrintService() {
    StackTracer.printCurrentTestMethod();
    PrintService ps = PrintHelper.findDefaultPrintService();
    boolean ok = ps != null && !ps.getName().isEmpty();
    if (ok) {
      System.out.println("  - defaut print service: " + ps.getName());
    }
    assertTrue(ok);
  }

  @Test
  public void test03_findPrintService() {
    StackTracer.printCurrentTestMethod(" for " + TEST_PRINTER_NAME + " ...");
    PrintService ps = PrintHelper.findPrintService(TEST_PRINTER_NAME);
    boolean ok = ps != null && ps.getName().contains(TEST_PRINTER_NAME);
    if (ok) {
      System.out.println("  - print service found: " + ps.getName());
    }
    assertTrue(ok);
  }

  @Test
  public void test04_getPrinterAttributes() {
    StackTracer.printCurrentTestMethod();
    Set<Attribute> aset = PrintHelper.getPrinterAttributes(TEST_PRINTER_NAME);
    boolean ok = !aset.isEmpty();
    StackTracer.printTestInfo(TEST_PRINTER_NAME, aset.size());
    if (ok) {
      System.out.println();
      Attribute attr[] = Arrays.copyOf(aset.toArray(), aset.size(), Attribute[].class);
      for (int i = 0; i < attr.length; i++) {
        Attribute attribute = attr[i];
        System.out.println("    " + ConvertLib.formatNumber(i+1,"00") + ". " + attribute);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test05_getPrinterJobProperties() {
    StackTracer.printCurrentTestMethod();
    Map<String, Object> map = PrintHelper.getPrinterJobProperties(TEST_PRINTER_NAME);
    Set<Map.Entry<String, Object>> entries = map.entrySet();
    boolean ok = !entries.isEmpty();
    StackTracer.printTestInfo(TEST_PRINTER_NAME, entries.size());
    if (ok) {
      System.out.println();
      int n = 1;
      for (Map.Entry<String, Object> entry : entries) {
        String key = entry.getKey();
        String value = entry.getValue().toString();
        System.out.printf("    " + ConvertLib.formatNumber(n, "00") + ". %s = %s%n", key, value);
        n++;
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test06_getPaperTrays() {
    StackTracer.printCurrentTestMethod();
    Media[] medias = PrintHelper.getPaperTraysArray(TEST_PRINTER_NAME);
    boolean ok = medias.length > 0;
    StackTracer.printTestInfo(TEST_PRINTER_NAME, medias.length);
    if (ok) {
      System.out.println();
      for (int i = 0; i < medias.length; i++) {
        Media media = medias[i];
        System.out.println("    " + (i+1) + ". " + media);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test07_getPaperFormatsArray() {
    StackTracer.printCurrentTestMethod();
    Media[] medias = PrintHelper.getPaperFormatsArray(TEST_PRINTER_NAME);
    boolean ok = medias.length > 0;
    StackTracer.printTestInfo(TEST_PRINTER_NAME, medias.length);
    if (ok) {
      System.out.println();
      for (int i = 0; i < medias.length; i++) {
        Media media = medias[i];
        System.out.println("    " + ConvertLib.formatNumber(i+1,"00") + ". " + media);
      }
    }
  }

}
