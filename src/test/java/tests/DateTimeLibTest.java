package tests;

import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.system.StackTracer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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
public class DateTimeLibTest {

  @BeforeClass
  public static void setUpClass() throws Exception {
    System.out.println("\n>>> " + StackTracer.getCurrentClass() + " <<<");
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_stringToDate() {
    StackTracer.printCurrentTestMethod();
    String sDate = "28.2.2015";
    Date d = DateTimeLib.stringToDate(sDate);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(calendar.get(Calendar.YEAR)==2015 && calendar.get(Calendar.MONTH)==1 && calendar.get(Calendar.DAY_OF_MONTH)==28);
  }

  @Test
  public void test02_stringToDateStartOfMonth() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2.2015";
    Date d = DateTimeLib.stringToDate(sDate, false);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(calendar.get(Calendar.YEAR)==2015 && calendar.get(Calendar.MONTH)==1 && calendar.get(Calendar.DAY_OF_MONTH)==1);
  }

  @Test
  public void test03_stringToDateEndOfMonth() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2.2015";
    Date d = DateTimeLib.stringToDate(sDate, true);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(calendar.get(Calendar.YEAR)==2015 && calendar.get(Calendar.MONTH)==1 && calendar.get(Calendar.DAY_OF_MONTH)==28);
  }

  @Test
  public void test04_isoStringToDate() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2015-02-28";
    Date d = DateTimeLib.isoStringToDate(sDate);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(calendar.get(Calendar.YEAR)==2015 && calendar.get(Calendar.MONTH)==1 && calendar.get(Calendar.DAY_OF_MONTH)==28);
  }

  @Test
  public void test05_timeStringToDate() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2015-02-28";
    String sTime = "13:48:12";
    Date d1 = DateTimeLib.isoStringToDate(sDate);
    Date d2 = DateTimeLib.timeStringToDate(sTime, d1);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d2);
    System.out.println("  - to parse : " + sTime);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d2, "dd.MM.yyyy HH:mm:ss"));
    boolean okDate = calendar.get(Calendar.YEAR)==2015 && calendar.get(Calendar.MONTH)==1 && calendar.get(Calendar.DAY_OF_MONTH)==28;
    boolean okTime = calendar.get(Calendar.HOUR_OF_DAY)==13 && calendar.get(Calendar.MINUTE)==48 && calendar.get(Calendar.SECOND)==12;
    assertTrue(okDate && okTime);
  }

  private String[] checkOneDate(String sDate, boolean last) {
    Date d = DateTimeLib.isoStringToDate(sDate);
    Date dates[] = DateTimeLib.getWorkYearDates(d, +1); // +1 mois dans le futur
    String res[] = new String[2];
    res[0] = DateTimeLib.dateToString(dates[0], DateTimeLib.ISO8601_FORMAT_SHORT);
    res[1] = DateTimeLib.dateToString(dates[1], DateTimeLib.ISO8601_FORMAT_SHORT);
    System.out.println("  - date[0] : " + res[0]);
    System.out.println("  - date[1] : " + res[1]);
    if (!last) {
      System.out.println();
    }
    return res;
  }

  @Test
  public void test06_getWorkYearDates() {
    StackTracer.printCurrentTestMethod();
    String r[];
    boolean ok[] = new boolean[4];

    r = checkOneDate ("2016-01-28", false);
    ok[0] = r[0].equals("2015-01-01") && r[1].equals("2016-02-29");

    r = checkOneDate ("2016-02-28", false);
    ok[1] = r[0].equals("2015-01-01") && r[1].equals("2016-03-31");

    r = checkOneDate ("2016-03-28", false);
    ok[2] = r[0].equals("2015-01-01") && r[1].equals("2016-04-30");

    r = checkOneDate ("2016-04-28", true);
    ok[3] = r[0].equals("2015-05-01") && r[1].equals("2016-05-31");

    assertTrue(ok[0] && ok[1] && ok[2] && ok[3]);
  }

  @Test
  public void test07_dateToLocalDate() {
    StackTracer.printCurrentTestMethod();
    Date d = DateTimeLib.getDate();

    LocalDate ld = DateTimeLib.dateToLocalDate(d);
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    System.out.println("  - to convert : " + DateTimeLib.dateToString(d, "dd.MM.yyyy HH:mm:ss"));
    System.out.println("  - result     : " + ld.format(dtf));
    assertTrue(ld != null);
  }

  @Test
  public void test08_getMonthDates() {
    StackTracer.printCurrentTestMethod();
    Date dSource = DateTimeLib.createDate(1, 1, 2017);
    int year = DateTimeLib.getYear(dSource) - 1;
    int monthsOffset = -11;
    Date d[] = DateTimeLib.getMonthDates(dSource, monthsOffset);

    int iDebut[] = DateTimeLib.extractDateInfo(d[0]);
    int iFin[] = DateTimeLib.extractDateInfo(d[1]);
    boolean ok1 = iDebut[0] == 1 && iDebut[1] == 2 && iDebut[2] == year;
    boolean ok2 = iFin[0] == 29 && iFin[1] == 2 && iFin[2] == year;

    System.out.println("  - source   : " + DateTimeLib.dateToString(dSource) + ", offset: " + monthsOffset + " months");
    System.out.println("  - result 1 : " + DateTimeLib.dateToString(d[0], DateTimeLib.DATE_FORMAT_SHORT) + ", ok1: " + ok1);
    System.out.println("  - result 2 : " + DateTimeLib.dateToString(d[1], DateTimeLib.DATE_FORMAT_SHORT) + ", ok2: " + ok2);

    assertTrue(ok1 && ok2);
  }

  @Test
  public void test09_getYearDates() {
    StackTracer.printCurrentTestMethod();
    Date dSource = DateTimeLib.getDate();
    int year = DateTimeLib.getYear(dSource);
    Date d[] = DateTimeLib.getYearDates();

    int iDebut[] = DateTimeLib.extractDateInfo(d[0]);
    int iFin[] = DateTimeLib.extractDateInfo(d[1]);
    boolean ok1 = iDebut[0] == 1 && iDebut[1] == 1 && iDebut[2] == year;
    boolean ok2 = iFin[0] == 31 && iFin[1] == 12 && iFin[2] == year;

    System.out.println("  - source   : " + DateTimeLib.dateToString(dSource));
    System.out.println("  - result 1 : " + DateTimeLib.dateToString(d[0], DateTimeLib.DATE_FORMAT_SHORT) + ", ok1: " + ok1);
    System.out.println("  - result 2 : " + DateTimeLib.dateToString(d[1], DateTimeLib.DATE_FORMAT_SHORT) + ", ok2: " + ok2);

    assertTrue(ok1 && ok2);
  }

  @Test
  public void test10_getDaysBetweenTwoDates() {
    StackTracer.printCurrentTestMethod();
    Date theLaterDate = DateTimeLib.createDate(20, 10, 2016);
    Date theEarlierDate = DateTimeLib.createDate(3, 11, 2016);

    int days = DateTimeLib.getDaysBetweenTwoDates(theLaterDate, theEarlierDate);
    System.out.println("  - laterDate   : " + DateTimeLib.dateToString(theLaterDate));
    System.out.println("  - earlierDate : " + DateTimeLib.dateToString(theEarlierDate));
    System.out.println("  - days between= " + days);
    assertTrue(days==14);
  }

  @Test
  public void test11_getMonday() {
    StackTracer.printCurrentTestMethod();
    Date currentDate = DateTimeLib.getDate();
    Date mondayDate = DateTimeLib.getMonday(currentDate);
    System.out.println("  - source : " + DateTimeLib.dateToString(currentDate));
    System.out.println("  - monday : " + DateTimeLib.dateToString(mondayDate));
    assertTrue(DateTimeLib.getDayOfWeek(mondayDate) == 2);
  }

  @Test
  public void test12_getFriday() {
    StackTracer.printCurrentTestMethod();
    Date currentDate = DateTimeLib.getDate();
    Date fridayDate = DateTimeLib.getFriday(currentDate);
    System.out.println("  - source : " + DateTimeLib.dateToString(currentDate));
    System.out.println("  - friday : " + DateTimeLib.dateToString(fridayDate));
    assertTrue(DateTimeLib.getDayOfWeek(fridayDate) == 6);
  }


}
