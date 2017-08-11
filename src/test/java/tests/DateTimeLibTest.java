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
  public void test01_dateToString() {
    StackTracer.printCurrentTestMethod();
    Date d = null;

    String result = DateTimeLib.dateToString(d, DateTimeLib.ISO8601_FORMAT_SHORT);
    System.out.println("  - value : null date");
    System.out.println("  - result : " + result + " len="+ result.length());
    assertTrue(result.length() == DateTimeLib.ISO8601_FORMAT_SHORT.length());

    result = DateTimeLib.dateToString(d, DateTimeLib.ISO8601_FORMAT_SHORT, "...");
    System.out.println("  - result : " + result + " len="+ result.length());
    assertTrue(result.length() == 3);
  }

  @Test
  public void test02_getDatePatternInfo() {
    StackTracer.printCurrentTestMethod();
    String info[] = DateTimeLib.getLocalePatternInfo();
    System.out.println("  - date separator : " + info[0]);
    System.out.println("  - date sep regex : " + info[1]);
    System.out.println("  - date pattern   : " + info[2]);
    System.out.println("  - time separator : " + info[3]);
    System.out.println("  - time sep regex : " + info[4]);
    System.out.println("  - time pattern   : " + info[5]);
    assertTrue(info[1].length() > 0);
  }

  @Test
  public void test03_getNow() {
    StackTracer.printCurrentTestMethod();
    Date d = DateTimeLib.getNow();
    System.out.println("  - result : " + DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD));
    assertTrue(d != null);
  }

  @Test
  public void test04_getToday() {
    StackTracer.printCurrentTestMethod();
    Date d = DateTimeLib.getToday();
    System.out.println("  - result : " + DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD));
    assertTrue(d != null);
  }

  @Test
  public void test05_getSmartToday() {
    StackTracer.printCurrentTestMethod();
    String smartToday = DateTimeLib.getSmartToday();
    System.out.println("  - result : " + smartToday);
    assertTrue(!smartToday.isEmpty());
  }

  @Test
  public void test06_parseIsoDate() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2016-02-29";
    Date d = DateTimeLib.parseIsoDate(sDate);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(DateTimeLib.isValidDate(d, 29, 2, 2016));
  }

  @Test
  public void test07_parseIsoDateWithTime() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2016-02-29 23:08:01";
    Date d = DateTimeLib.parseIsoDate(sDate);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d, "dd.MM.yyyy HH:mm:ss"));
    boolean okDate = DateTimeLib.isValidDate(d, 29, 2, 2016);
    boolean okTime = DateTimeLib.isValidTime(d, 23, 8, 1);
    assertTrue(okDate && okTime);
  }

  @Test
  public void test08_parseDate() {
    StackTracer.printCurrentTestMethod();
    String sDate = "29.2.16";
    Date d = DateTimeLib.parseDate(sDate);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(DateTimeLib.isValidDate(d, 29, 2, 2016));
  }

  @Test
  public void test09_parseDate_startOfMonth() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2.2016";
    Date d = DateTimeLib.parseDate(sDate, false);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(DateTimeLib.isValidDate(d, 1, 2, 2016));
  }

  @Test
  public void test10_parseDate_endOfMonth() {
    StackTracer.printCurrentTestMethod();
    String sDate = "2.2016";
    Date d = DateTimeLib.parseDate(sDate, true);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    System.out.println("  - to parse : " + sDate);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d));
    assertTrue(DateTimeLib.isValidDate(d, 29, 2, 2016));
  }

  @Test
  public void test11_parseTime() {
    StackTracer.printCurrentTestMethod();
    String sTime = "23:8:1";
    Date d = DateTimeLib.parseTime(sTime);
    System.out.println("  - to parse : " + sTime);
    System.out.println("  - parsed   : " + DateTimeLib.dateToString(d, "HH:mm:ss"));
    assertTrue(DateTimeLib.isValidTime(d, 23, 8, 1));
  }

  private Date[] checkOneDate(String sDate, boolean last) {
    Date d = DateTimeLib.parseIsoDate(sDate);
    Date dates[] = DateTimeLib.getYearWorkingDates(d, +1); // +1 mois dans le futur
    System.out.println("  - source  : " + DateTimeLib.dateToString(d, DateTimeLib.ISO8601_FORMAT_SHORT));
    System.out.println("  - date[0] : " + DateTimeLib.dateToString(dates[0]));
    System.out.println("  - date[1] : " + DateTimeLib.dateToString(dates[1]));
    if (!last) {
      System.out.println();
    }
    return dates;
  }

  @Test
  public void test12_getYearWorkingDates() {
    StackTracer.printCurrentTestMethod();
    Date dates[];
    boolean ok[] = new boolean[4];

    dates = checkOneDate("2016-01-28", false);
    ok[0] = DateTimeLib.isValidDate(dates[0],  1, 1, 2015)
         && DateTimeLib.isValidDate(dates[1], 29, 2, 2016);

    dates = checkOneDate("2016-02-28", false);
    ok[1] = DateTimeLib.isValidDate(dates[0],  1, 1, 2015)
         && DateTimeLib.isValidDate(dates[1], 31, 3, 2016);

    dates = checkOneDate("2016-03-28", false);
    ok[2] = DateTimeLib.isValidDate(dates[0],  1, 1, 2015)
         && DateTimeLib.isValidDate(dates[1], 30, 4, 2016);

    dates = checkOneDate("2016-04-28", true);
    ok[3] = DateTimeLib.isValidDate(dates[0],  1, 5, 2015)
         && DateTimeLib.isValidDate(dates[1], 31, 5, 2016);

    assertTrue(ok[0] && ok[1] && ok[2] && ok[3]);
  }

  @Test
  public void test13_dateToLocalDate() {
    StackTracer.printCurrentTestMethod();
    Date d = DateTimeLib.getNow();
    LocalDate localDate = DateTimeLib.dateToLocalDate(d);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    System.out.println("  - to convert : " + DateTimeLib.dateToString(d, "dd.MM.yyyy HH:mm:ss"));
    System.out.println("  - result     : " + localDate.format(dtf));
    assertTrue(localDate != null);
  }

  @Test
  public void test14_getMonthDates() {
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
  public void test15_getYearDates() {
    StackTracer.printCurrentTestMethod();
    Date dSource = DateTimeLib.getToday();
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
  public void test16_getDaysBetweenTwoDates() {
    StackTracer.printCurrentTestMethod();
    Date theLaterDate = DateTimeLib.createDate(20, 10, 2016);
    Date theEarlierDate = DateTimeLib.createDate(3, 11, 2016);

    int days = DateTimeLib.getDaysBetweenTwoDates(theLaterDate, theEarlierDate);
    System.out.println("  - laterDate   : " + DateTimeLib.dateToString(theLaterDate));
    System.out.println("  - earlierDate : " + DateTimeLib.dateToString(theEarlierDate));
    System.out.println("  - days between= " + days);
    assertTrue(days == 14);
  }

  @Test
  public void test17_getMonday() {
    StackTracer.printCurrentTestMethod();
    Date refDate = DateTimeLib.getToday();
    Date mondayDate = DateTimeLib.getMonday(refDate);
    System.out.println("  - ref. date : " + DateTimeLib.dateToString(refDate));
    System.out.println("  - monday    : " + DateTimeLib.dateToString(mondayDate));
    assertTrue(DateTimeLib.getDayOfWeek(mondayDate) == 2);
  }

  @Test
  public void test18_getFriday() {
    StackTracer.printCurrentTestMethod();
    Date refDate = DateTimeLib.getToday();
    Date fridayDate = DateTimeLib.getFriday(refDate);
    System.out.println("  - ref. date : " + DateTimeLib.dateToString(refDate));
    System.out.println("  - friday    : " + DateTimeLib.dateToString(fridayDate));
    assertTrue(DateTimeLib.getDayOfWeek(fridayDate) == 6);
  }

  @Test
  public void test19_getMondayFriday() {
    StackTracer.printCurrentTestMethod();
    Date refDate = DateTimeLib.getToday();
    Date monday = DateTimeLib.getMonday(refDate);
    Date friday = DateTimeLib.getFriday(refDate);
    Date mfDates[] = DateTimeLib.getMondayFriday(refDate);
    System.out.println("  - ref. date : " + DateTimeLib.dateToString(refDate));
    for (int i = 0; i < mfDates.length; i++) {
      System.out.println("  - day " + i + " : " + DateTimeLib.dateToString(mfDates[i]));
    }
    assertTrue(mfDates[0].getTime() == monday.getTime() && mfDates[1].getTime() == friday.getTime());
  }

  @Test
  public void test20_getWeekDates() {
    StackTracer.printCurrentTestMethod();
    int weekOffset = +1;
    Date refDate = DateTimeLib.getDate(weekOffset * 7);
    Date monday = DateTimeLib.getMonday(refDate);
    Date friday = DateTimeLib.getFriday(refDate);
    Date wwDates[] = DateTimeLib.getWeekDates(weekOffset);
    System.out.println("  - ref. date : " + DateTimeLib.dateToString(refDate));
    for (int i = 0; i < wwDates.length; i++) {
      System.out.println("  - day " + i + " : " + DateTimeLib.dateToString(wwDates[i]));
    }
    assertTrue(wwDates[0].getTime() == monday.getTime() && wwDates[4].getTime() == friday.getTime());
  }

  @Test
  public void test21_getDateIndex() {
    StackTracer.printCurrentTestMethod();
    Date refDate = DateTimeLib.getToday();
    int pos = DateTimeLib.getDateIndex(refDate);
    System.out.println("  - ref. date : " + DateTimeLib.dateToString(refDate));
    System.out.println("  - date pos. : " + pos);

    Date mfDates[] = DateTimeLib.getMondayFriday(refDate);
    boolean ok1 = pos >= 0
      && refDate.getTime() >= mfDates[0].getTime()
      && refDate.getTime() <= mfDates[1].getTime();
    boolean ok2 = pos < 0;
    assertTrue(ok1 || ok2);
  }

}
