package tests;

import ch.jcsinfo.printing.PrintHelper;
import ch.jcsinfo.models.Printer;
import ch.jcsinfo.system.StackTracer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.standard.Media;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jcstritt
 */
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
//    docURL = FileLib.filePathToURL(RESULTS_FOLDER + File.separator + TEST_DOCUMENT_NAME);
//    pdfURL = FileLib.replaceFileExt(docURL, ".pdf");
//    odtURL = FileLib.replaceFileExt(docURL, ".odt");
  }

  @Test
  public void testGetPrintersList() {
    StackTracer.printCurrentTestMethod();
    List<Printer> result = PrintHelper.getListOfPrinters();
    boolean ok = result.size() > 0;
    if (ok) {
      int i = 0;
      for (Printer printer : result) {
        System.out.println("- " + i + ". " + printer);
        i++;
      }
    }
    assertTrue(ok);
  }

  @Test
  public void testFindDefaultPrintService() {
    StackTracer.printCurrentTestMethod();
    PrintService ps = PrintHelper.findDefaultPrintService();
    boolean ok = ps != null && !ps.getName().isEmpty();
    if (ok) {
      System.out.println("- defaut print service: " + ps.getName());
    }
    assertTrue(ok);
  }

  @Test
  public void testFindPrintService() {
    StackTracer.printCurrentTestMethod(" for " + TEST_PRINTER_NAME + " ...");
    PrintService ps = PrintHelper.findPrintService(TEST_PRINTER_NAME);
    boolean ok = ps != null && ps.getName().contains(TEST_PRINTER_NAME);
    if (ok) {
      System.out.println("- print service found: " + ps.getName());
    }
    assertTrue(ok);
  }

  @Test
  public void testGetPrinterAttributes() {
    StackTracer.printCurrentTestMethod(" for " + TEST_PRINTER_NAME + " ...");
    Set<Attribute> aset = PrintHelper.getPrinterAttributes(TEST_PRINTER_NAME);
    boolean ok = !aset.isEmpty();
    if (ok) {
      Attribute attr[] = Arrays.copyOf(aset.toArray(), aset.size(), Attribute[].class);
      for (int i = 0; i < attr.length; i++) {
        Attribute attribute = attr[i];
        System.out.println("- " + i + ". " + attribute);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void testGetPrinterJobProperties() {
    StackTracer.printCurrentTestMethod(" for " + TEST_PRINTER_NAME + " ...");
    Map<String, Object> map = PrintHelper.getPrinterJobProperties(TEST_PRINTER_NAME);
    Set<Map.Entry<String, Object>> entries = map.entrySet();
    boolean ok = !entries.isEmpty();
    if (ok) {
      for (Map.Entry<String, Object> entry : entries) {
        String key = entry.getKey();
        String value = entry.getValue().toString();
        System.out.printf("- %s = %s%n", key, value);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void testGetPaperTrays() {
    StackTracer.printCurrentTestMethod(" for " + TEST_PRINTER_NAME + " ...");
    Media[] medias = PrintHelper.getPaperTraysArray(TEST_PRINTER_NAME);
    boolean ok = medias.length > 0;
    if (ok) {
      for (int i = 0; i < medias.length; i++) {
        Media media = medias[i];
        System.out.println("- " + i + ". " + media);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void testGetPaperFormats() {
    StackTracer.printCurrentTestMethod(" for " + TEST_PRINTER_NAME + " ...");
    Media[] medias = PrintHelper.getPaperFormatsArray(TEST_PRINTER_NAME);
    boolean ok = medias.length > 0;
    if (ok) {
      for (int i = 0; i < medias.length; i++) {
        Media media = medias[i];
        System.out.println("- " + i + ". " + media);
      }
    }
  }

}
