package tests;

import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.ConvertLib;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class ConvertLibTest {
  static String TEST_LINE = "0 103814 091216 07:23:48 A0 M  002095KERASTONE SA                            ENTREPRISE                              KERASTONE SA                            RTE DE FORMANGUEIRES 9                  1782 BELFAUX                              026/475 36 46    026 475 38 08   001409FRANCO:PAUDEX                                000810 005GCN lavÇ 0/8 10%              01.4285000 T 000227FR 151689 Sallin Transports        4 essieux benne moser    E0Ö facturer               1franco 2 00061.00 Fr/m3          013280 0009.300 0009.300 M  00025.20 00028.00  00061.00 00567.30                                                   ";

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  // retourne 123456 codé dans un buffer en BCD
  private short[] getShortBcdNumber() {
    short[] b = new short[4];

    // "123456" codé en BCD dans un buffer
    b[0] = 0; // signe et nb de décimales
    b[1] = 1 * 16 + 2;
    b[2] = 3 * 16 + 4;
    b[3] = 5 * 16 + 6;
    return b;
  }

  @Test
  public void test01_bcdToString() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    String expResult = "123456";

    // ce même nombre codé en BCD dans un buffer
    short[] b = getShortBcdNumber();

    // on convertit et on affiche le résultat
    String result = ConvertLib.bcdToString(b);
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  /**
   * Test of bcdToInt method, of class ConvertLib.
   */
  @Test
  public void test02_bcdToInt() {
    StackTracer.printCurrentTestMethod();

    // le nombre attendu
    int expResult = 123456;

    // ce même nombre codé en BCD dans un buffer
    short[] b = getShortBcdNumber();

    // on convertit et on affiche le résultat
    int result = ConvertLib.bcdToInt(b);
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test03_bcdToBigDecimal() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    String expResult = "-987654321000.99";

    // le même nombre codé en BCD dans un buffer
    short[] b = new short[8];
    b[0] = 16 + 2; // signe négatif + nb de décimales
    b[1] = 9 * 16 + 8;
    b[2] = 7 * 16 + 6;
    b[3] = 5 * 16 + 4;
    b[4] = 3 * 16 + 2;
    b[5] = 1 * 16 + 0;
    b[6] = 0 * 16 + 0;
    b[7] = 9 * 16 + 9;

    // on convertit et on affiche le résultat
    String result = ConvertLib.bcdToBigDecimal(b).toPlainString();
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  /**
   * Test of encryptedBufferToString method, of class ConvertLib.
   */
  @Test
  public void test04_bufferToString() {
    StackTracer.printCurrentTestMethod();

    // le resultat attendu
    // String expResult = "èéêëç";
    String expResult = "èéêëç";

    // ce même résultat codé en "CP850" (DOS) dans un buffer de caractères
    char[] buffer = new char[5];
    buffer[0] = 0x8A; // è
    buffer[1] = 0x82; // é
    buffer[2] = 0x88; // ê
    buffer[3] = 0x89; // ë
    buffer[4] = 0x87; // ç

    // on convertit et on affiche le résultat
    String result = ConvertLib.bufferToString(buffer);
    StackTracer.printTestResult(expResult, result);
    boolean ok = result.equals(expResult);
    assertTrue(ok);
  }

  @Test
  public void test05_intToDate() {
    StackTracer.printCurrentTestMethod();

    // on simule une date (celle d'aujourd'hui) au format "entier"
    GregorianCalendar now = new GregorianCalendar();
    int d = now.get(Calendar.DATE);
    int m = now.get(Calendar.MONTH) + 1;
    int y = now.get(Calendar.YEAR) % 100;
    int iDate = y * 512 + m * 32 + d;

    // on enlève les secondes de "now" (pour la comparaison)
    now.set(Calendar.HOUR_OF_DAY, 0);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);
    now.set(Calendar.MILLISECOND, 0);
    Date expResult = now.getTime();

    // on convertit et on affiche le résultat
    Date result = ConvertLib.intToDate(iDate);
    StackTracer.printTestInfo(iDate, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test06_isIntNumber() {
    StackTracer.printCurrentTestMethod();
    String t[] = {"1245", "1245.6", "1245f"};
    boolean ok[] = new boolean[3];

    // on teste les 3 exemples
    for (int i = 0; i < ok.length; i++) {
      ok[i] = ConvertLib.isIntNumber(t[i]);
      System.out.println("  - isIntNumber(" + t[i] + ") = " + ok[i]);
    }
    assertTrue(ok[0] && !ok[1] && !ok[2]);
  }

  @Test
  public void test07_stringToInt() { // int -2^32 ... 0 ... 2^31-1
    StackTracer.printCurrentTestMethod();
    String t[] = {"-2147483648", "1", "2147483647"};
    int i1 = (int) -Math.pow(2, 32);
    int i2 = (int) (Math.pow(2, 31) - 1);
    int expected[] = {i1, 1, i2};
    int results[] = new int[3];
    boolean ok[] = new boolean[3];

    // on teste les 3 exemples
    for (int i = 0; i < ok.length; i++) {
      results[i] = ConvertLib.stringToInt(t[i]);
      ok[i] = results[i] == expected[i];
      System.out.println("  - stringToInt(" + t[i] + ") = " + results[i]);
    }
    assertTrue(ok[0] && ok[1] && ok[2]);
  }

  @Test
  public void test08_stringToLong() { // int -2^64 ... 0 ... 2^63-1
    StackTracer.printCurrentTestMethod();
    String t[] = {"-9223372036854775808", "1", "9223372036854775807"};
    long l1 = (long) -Math.pow(2, 64);
    long l2 = (long) (Math.pow(2, 63) - 1);
    long expected[] = {l1, 1, l2};
    long results[] = new long[3];
    boolean ok[] = new boolean[3];

    // on teste les 3 exemples
    for (int i = 0; i < ok.length; i++) {
      results[i] = ConvertLib.stringToLong(t[i]);
      ok[i] = results[i] == expected[i];
      System.out.println("  - stringToLong(" + t[i] + ") = " + results[i]);
    }
    assertTrue(ok[0] && ok[1] && ok[2]);
  }

  @Test
  public void test09_stringToFloat() {
    StackTracer.printCurrentTestMethod();
    String t[] = {"-3.14159f", "1", "+3.4e+38"};
    float f1 = -3.14159f;
    float f2 = +3.4e+38f;
    float expected[] = {f1, 1f, f2};
    float results[] = new float[3];
    boolean ok[] = new boolean[3];
    // on teste les 3 exemples
    for (int i = 0; i < ok.length; i++) {
      results[i] = ConvertLib.stringToFloat(t[i]);
      ok[i] = results[i] == expected[i]; //  < SMALLEST_FLOAT;
      System.out.println("  - stringToFloat(" + t[i] + ") = " + results[i]);
    }
    assertTrue(ok[0] && ok[1] && ok[2]);
  }

  @Test
  public void test10_stringToDouble() {
    StackTracer.printCurrentTestMethod();
    String t[] = {"-3.14159d", "1.", "1.7e+308"};
    double d1 = -3.14159d;
    double d2 = +1.7e+308d;
    double expected[] = {d1, 1d, d2};
    double results[] = new double[3];
    boolean ok[] = new boolean[3];

    // on teste les 3 exemples
    for (int i = 0; i < ok.length; i++) {
      results[i] = ConvertLib.stringToDouble(t[i]);
      ok[i] = results[i] == expected[i];
      System.out.println("  - stringToDouble(" + t[i] + ") = " + results[i]);
    }
    assertTrue(ok[0] && ok[1] && ok[2]);
  }


 @Test
  public void test11_fillString1() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    String expResult = "00000";

    // on convertit et on affiche le résultat
    String result = ConvertLib.fillString(5, '0');
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test12_fillString2() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    String expResult = "Help000000";

    // on convertit et on affiche le résultat
    String result = ConvertLib.fillString(10, '0', "Help");
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test13_getString() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    String expResult = "07:23:48";

    // on convertit et on affiche le résultat
    String result = ConvertLib.getString(TEST_LINE, 17, 8);
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test14_getInt() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    int expResult = 103814;

    // on convertit et on affiche le résultat
    int result = ConvertLib.getInt(TEST_LINE, 3, 6);
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test15_getLong() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    long expResult = 103814;

    // on convertit et on affiche le résultat
    long result = ConvertLib.getLong(TEST_LINE, 3, 6);
    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test16_getDate() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    Date expResult = DateTimeLib.createDate(9, 12, 2016);

    // on convertit et on affiche le résultat
    Date result = ConvertLib.getDate(TEST_LINE, 10, "DDMMYY");

    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test17_getHour() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    Date expResult = DateTimeLib.createDate(9, 12, 2016, 7, 23, 48);

    // on convertit et on affiche le résultat
    Date date = ConvertLib.getDate(TEST_LINE, 10, "DDMMYY");
    Date result = ConvertLib.getHour(TEST_LINE, 17, "HH:MM:SS", date);

    StackTracer.printTestResult(expResult, result);
    assertEquals(expResult, result);
  }

  @Test
  public void test18_getFloat() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    float expResult = 1.4285000f;

    // on convertit et on affiche le résultat
    float result = ConvertLib.getFloat(TEST_LINE, 364, 10);
    StackTracer.printTestResult(expResult, result);
    assertTrue(result == expResult);
  }

  @Test
  public void test19_getDouble() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    double expResult = 1.4285000d;

    // on convertit et on affiche le résultat
    double result = ConvertLib.getDouble(TEST_LINE, 364, 10);
    StackTracer.printTestResult(expResult, result);
    assertTrue(result == expResult);
  }

  @Test
  public void test20_getBigDecimal() {
    StackTracer.printCurrentTestMethod();

    // le résultat attendu
    BigDecimal expResult = new BigDecimal("25.20");

    // on convertit et on affiche le résultat
    BigDecimal result = ConvertLib.getBigDecimal(TEST_LINE, 532, 8);
    StackTracer.printTestResult(expResult, result);
    assertTrue(result.compareTo(expResult) == 0);
  }

}
