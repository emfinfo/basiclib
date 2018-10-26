package tests;

import beans.Compte;
import beans.PmtMode;
import beans.Solde;
import ch.jcsinfo.file.BinaryFileReader;
import ch.jcsinfo.file.FileException;
import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.ConvertLib;
import java.math.BigDecimal;
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
public class BinaryFileReaderTest {
  static final String COMPTES = "data/109_C.DAF";
  static final int COMPTES_REC_SIZE = 141;
  static final String PMTMODES = "data/PAYMMODE.DAF";
  static final int PMTMODES_REC_SIZE = 47;
  static final String PWD = "XMY556P";
  static final boolean SHOW_DETAILS = false; // à votre bon vouloir

  static BinaryFileReader readerComptes;
  static BinaryFileReader readerPmtModes;

  @BeforeClass
  public static void setUpClass() throws Exception {
    readerComptes = new BinaryFileReader(COMPTES, COMPTES_REC_SIZE);
    readerPmtModes = new BinaryFileReader(PMTMODES, PMTMODES_REC_SIZE);
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    readerComptes.closeFile();
    readerPmtModes.closeFile();
    System.out.println();
  }

  @Test
  public void test01_open() {
    StackTracer.printCurrentTestMethod();
    boolean ok = false;
    try {
      ok = readerComptes.open();
    } catch (FileException ex) {
    }
    StackTracer.printTestResult("Source", COMPTES, "Open", ok);
    assertTrue(ok);
  }

  @Test
  public void test02_numberOfBytes() {
    StackTracer.printCurrentTestMethod();
    int bytes = readerComptes.numberOfBytes();
    boolean ok = bytes > 0;
    StackTracer.printTestResult("Source", COMPTES, "Bytes", bytes, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test03_numberOfRecords() {
    StackTracer.printCurrentTestMethod();
    int nb = readerComptes.numberOfRecords();
    boolean ok = nb > 1;
    StackTracer.printTestResult("Source", COMPTES, "Nb", nb, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test04_skipBytes() {
    StackTracer.printCurrentTestMethod();
    int nb1 = readerComptes.getRecordSize();
    int nb2 = readerComptes.skipBytes(readerComptes.getRecordSize());
    boolean ok = nb2 == nb1;
    StackTracer.printTestResult("Source", COMPTES, "Nb1", nb1, "Nb2", nb2, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test05_readComptes() throws Exception {
    StackTracer.printCurrentTestMethod();
    int nb = readerComptes.numberOfRecords();
    boolean ok = nb > 1;
    StackTracer.printTestResult("Source", COMPTES, "Nb", nb, "Ok", ok);
    System.out.println();
    for (int i = 0; i < nb; i++) {
      Compte c = new Compte();
      if (readerComptes.readInt() == 0) {
        c.setNumero(Integer.parseInt(readerComptes.readString(6)));
//        c.setDesignation(readerComptes.readEncryptedString(40, PWD));
        c.setDesignation(readerComptes.readString(40));
        c.setMonnaie(readerComptes.readString(4));
        c.setGroupe(Integer.parseInt(readerComptes.readString(3)));
        c.setRang(readerComptes.readWord());
        c.setCodeTVA(readerComptes.readWord());
        c.setTauxAmbigu(readerComptes.readBoolean());
        c.setNbEcriSaisies(readerComptes.readInt());
        c.setNbEcriComptab(readerComptes.readInt());

        Solde[] soldes = new Solde[6];
        for (int j = 0; j < soldes.length; j++) {
          BigDecimal mt = readerComptes.readDec(14);
          boolean auCredit = readerComptes.readBoolean();
          soldes[j] = new Solde();
          soldes[j].setAuCredit(auCredit);
          soldes[j].setMontant(mt);
        }
        c.setSoldes(soldes);
        System.out.println("    " + ConvertLib.formatNumber(i+1, "00") + " - " + c);
        if (SHOW_DETAILS) {
          System.out.println(c.getSoldes());
        }
      } else { // compte effacé: status = -1
        readerComptes.skipBytes(readerComptes.getRecordSize() - 4);
      }
    }
    assertTrue(ok);
  }

  @Test
  public void test06_open() {
    StackTracer.printCurrentTestMethod();
    boolean ok = false;
    try {
      ok = readerPmtModes.open();
    } catch (FileException ex) {
    }
    StackTracer.printTestResult("Source", PMTMODES, "Open", ok);
    assertTrue(ok);
  }

  @Test
  public void test07_numberOfBytes() {
    StackTracer.printCurrentTestMethod();
    int bytes = readerPmtModes.numberOfBytes();
    boolean ok = bytes > 0;
    StackTracer.printTestResult("Source", PMTMODES, "Bytes", bytes, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test08_numberOfRecords() {
    StackTracer.printCurrentTestMethod();
    int nb = readerPmtModes.numberOfRecords();
    boolean ok = nb > 1;
    StackTracer.printTestResult("Source", PMTMODES, "Nb", nb, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test09_skipBytes() {
    StackTracer.printCurrentTestMethod();
    int nb1 = readerPmtModes.getRecordSize();
    int nb2 = readerPmtModes.skipBytes(readerPmtModes.getRecordSize());
    boolean ok = nb2 == nb1;
    StackTracer.printTestResult("Source", PMTMODES, "Nb1", nb1, "Nb2", nb2, "Ok", ok);
    assertTrue(ok);
  }

  @Test
  public void test10_readPmtModes() throws Exception {
    StackTracer.printCurrentTestMethod();
    int nb = readerPmtModes.numberOfRecords();
    boolean ok = nb > 1;
    StackTracer.printTestResult("Source", PMTMODES, "Nb", nb, "Ok", ok);
    System.out.println();
    for (int i = 0; i < nb; i++) {
      PmtMode pm = new PmtMode();
      if (readerPmtModes.readInt() == 0) {
        pm.setAbrev(readerPmtModes.readString(6));
        pm.setDesignation(readerPmtModes.readString(30));
        pm.setNoCompte(readerPmtModes.readDec(6).intValue());
        System.out.println("    " + pm);
      } else { // pmtMode effacé : status = -1
        readerPmtModes.skipBytes(readerPmtModes.getRecordSize() - 4);
      }
    }
    assertTrue(nb > 1);
  }
}
