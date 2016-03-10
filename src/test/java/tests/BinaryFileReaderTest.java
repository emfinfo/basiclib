package tests;

import beans.Compte;
import beans.Solde;
import beans.PmtMode;
import ch.jcsinfo.file.BinaryFileReader;
import ch.jcsinfo.system.StackTracer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author jcstritt
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BinaryFileReaderTest {
  static final String COMPTES = "data/109_C.DAF";
  static final int COMPTES_REC_SIZE = 141;
  static final String PMTMODES = "data/PAYMMODE.DAF";
  static final int PMTMODES_REC_SIZE = 47;
  static final String PSW = "XMY556P";
  static final boolean SHOW_DETAILS = false; // à votre bon vouloir

  static BinaryFileReader readerComptes;
  static BinaryFileReader readerPmtModes;

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("Default charset: " + Charset.defaultCharset().displayName());
    readerComptes = new BinaryFileReader(COMPTES, COMPTES_REC_SIZE);
    readerPmtModes = new BinaryFileReader(PMTMODES, PMTMODES_REC_SIZE);
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    readerComptes.closeFile();
    readerPmtModes.closeFile();
  }

  @Test
  public void test01_Open() {
    StackTracer.printCurrentTestMethod();
    boolean ok1 = readerComptes.open();
    boolean ok2 = readerPmtModes.open();
    assertTrue(ok1 && ok2);
  }

  @Test
  public void test02_NumberOfBytes() {
    StackTracer.printCurrentTestMethod();
    int result = readerComptes.numberOfBytes();
    System.out.println("result=" + result);
    boolean ok = result > 0;
    assertTrue(ok);
  }

  @Test
  public void test03_NumberOfRecords() {
    StackTracer.printCurrentTestMethod();
    int expResult = 0;
    int result = readerComptes.numberOfRecords();
    boolean ok = result > 1;
    System.out.println("result=" + result);
    assertTrue(ok);
  }

  @Test
  public void test04_SkipBytes() {
    StackTracer.printCurrentTestMethod();
    int nb1 = readerComptes.getRecordSize();
    int nb2 = readerComptes.skipBytes(readerComptes.getRecordSize());
    System.out.println("- to skip: " + nb1);
    System.out.println("- skipped: " + nb2);
    assertEquals(nb2, nb1);
  }

  @Test
  public void test05_LitComptes() throws Exception {
    StackTracer.printCurrentTestMethod();
    int nb = readerComptes.numberOfRecords();
    for (int i = 0; i < nb; i++) {
      Compte c = new Compte();
      if (readerComptes.readInt() == 0) {
        c.setNumero(Integer.parseInt(readerComptes.readString(6)));
        c.setDesignation(readerComptes.readEncryptedString(40, PSW));
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
        System.out.println((i + 1) + ". " + c);
        if (SHOW_DETAILS) {
          System.out.println(c.getSoldes());
        }
        // readerComptes.skipBytes(66);
      } else {
        readerComptes.skipBytes(readerComptes.getRecordSize() - 4);
      }
    }
    boolean ok = nb > 1;
    assertTrue(ok);
  }

  @Test
  public void test06_LitPmtModes() throws Exception {
    StackTracer.printCurrentTestMethod();
    int nb = readerPmtModes.numberOfRecords();
    for (int i = 0; i < nb; i++) {
      PmtMode pm = new PmtMode();
      if (readerPmtModes.readInt() == 0) {
        pm.setAbrev(readerPmtModes.readString(6));
        pm.setDesignation(readerPmtModes.readString(30));
        pm.setNoCompte(readerPmtModes.readDec(6).intValue());
        System.out.println(i + ". " + pm);
      } else {
        readerPmtModes.skipBytes(readerPmtModes.getRecordSize() - 4);
      }
    }
    boolean ok = nb > 1;
    assertTrue(ok);
  }
}
