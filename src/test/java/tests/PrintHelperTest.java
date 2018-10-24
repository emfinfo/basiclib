package tests;

import ch.jcsinfo.models.Printer;
import ch.jcsinfo.printing.PrintHelper;
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
  private static String TEST_PRINTER_NAME;
//  private static final String RESULTS_FOLDER = "reports/results";
//  private static final String TEST_DOCUMENT_NAME = "report_test_file.pdf";
//  private static final String TEST_DOCUMENT_NAME = "safdemo_report_2012_11_17_184259.pdf";

//  private static String docURL;
//  private static String pdfURL;
//  private static String odtURL;

  @BeforeClass
  public static void setUpClass() {
//    System.setProperty("sun.java2d.print.polling", "true");
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
    
    // on compare le résultat avec celui attendu
    boolean ok = printers.size() > 0;
    StackTracer.printTestResult("Nb", printers.size());
    assertTrue(ok);    
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

    // on compare le résultat avec celui attendu
    boolean ok = ps != null && !ps.getName().isEmpty();
    StackTracer.printTestResult("Result", ps);
    if (ok) {
      TEST_PRINTER_NAME = ps.getName();
    }
    assertTrue(ok);
  }

  @Test
  public void test03_findPrintService() {
    StackTracer.printCurrentTestMethod();
    PrintService ps = PrintHelper.findPrintService(TEST_PRINTER_NAME);
    
    // on compare le résultat avec celui attendu
    boolean ok = ps != null && ps.getName().contains(TEST_PRINTER_NAME);
    StackTracer.printTestResult("Search", TEST_PRINTER_NAME, "Found", ps);
    assertTrue(ok);  
  }

  @Test
  public void test04_getPrinterAttributes() {
    StackTracer.printCurrentTestMethod();
    Set<Attribute> aset = PrintHelper.getPrinterAttributes(TEST_PRINTER_NAME);

    // on compare le résultat avec celui attendu
    boolean ok = !aset.isEmpty();
    StackTracer.printTestResult("Source", TEST_PRINTER_NAME, "Nb", aset.size());
    if (ok) {
      System.out.println();
      Attribute attr[] = Arrays.copyOf(aset.toArray(), aset.size(), Attribute[].class);
      for (int i = 0; i < attr.length; i++) {
        Attribute attribute = attr[i];
        System.out.println("    " + ConvertLib.formatNumber(i + 1, "00") + ". " + attribute);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test05_getPrinterJobProperties() {
    StackTracer.printCurrentTestMethod();
    Map<String, Object> map = PrintHelper.getPrinterJobProperties(TEST_PRINTER_NAME);
    Set<Map.Entry<String, Object>> entries = map.entrySet();

    // on compare le résultat avec celui attendu
    boolean ok = !entries.isEmpty();
    StackTracer.printTestResult("Source", TEST_PRINTER_NAME, "Nb", entries.size());
    if (ok) {
      System.out.println();
      int n = 1;
      for (Map.Entry<String, Object> entry : entries) {
        String key = entry.getKey();
        String value = (entry.getValue() != null) ? entry.getValue().toString():"null";
        System.out.printf("    " + ConvertLib.formatNumber(n, "00") + ". %s = %s%n", key, value);
        n++;
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test06_getPaperFormatsArray() {
    StackTracer.printCurrentTestMethod();
    Media[] medias = PrintHelper.getPaperFormatsArray(TEST_PRINTER_NAME);

    // on compare le résultat avec celui attendu
    boolean ok = medias.length > 0;
    StackTracer.printTestResult("Source", TEST_PRINTER_NAME, "Nb", medias.length);
    if (ok) {
      System.out.println();
      for (int i = 0; i < medias.length; i++) {
        Media media = medias[i];
        System.out.println("    " + ConvertLib.formatNumber(i + 1, "00") + ". " + media);
      }
    }
    assertTrue(ok);
  }

}
