package tests;

import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.system.StackTracer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    Locale.setDefault(Locale.getDefault());
//    Locale.setDefault(new Locale("en", "UK"));
//    Locale.setDefault(new Locale("fr", "FR"));
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    System.out.println();
  }

  @Test
  public void test01_dateToString() {
    StackTracer.printCurrentTestMethod();
    Date d = null;
    String result = DateTimeLib.dateToString(d, DateTimeLib.ISO8601_DATE_FORMAT);
    
    // on compare le résultat avec celui attendu
    boolean ok = result.length() == DateTimeLib.ISO8601_DATE_FORMAT.length();
    StackTracer.printTestResult("Source", d, "Result", result, "Ok", ok);
    assertTrue(ok);     
  }
  
  @Test
  public void test02_dateToString() {
    StackTracer.printCurrentTestMethod();
    Date d = null;
    String result = DateTimeLib.dateToString(d, DateTimeLib.ISO8601_DATE_FORMAT, "...");

    // on compare le résultat avec celui attendu
    boolean ok = result.length() == 3;
    StackTracer.printTestResult("Source", d, "Result", result, "Ok", ok);
    assertTrue(ok);     
  }  

  @Test
  public void test03_getDatePatternInfo() {
    StackTracer.printCurrentTestMethod();
    String info[] = DateTimeLib.getLocalePatternInfo();
    System.out.println("  - Date separator : " + info[0]);
    System.out.println("  - Date sep regex : " + info[1]);
    System.out.println("  - Date pattern   : " + info[2]);
    System.out.println("  - Time separator : " + info[3]);
    System.out.println("  - Time sep regex : " + info[4]);
    System.out.println("  - Time pattern   : " + info[5]);
    assertTrue(info[1].length() > 0);
  }

  @Test
  public void test04_getNow() {
    StackTracer.printCurrentTestMethod();
    Date d = DateTimeLib.getNow();
    String fmtDate = DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD);
    
    // on compare le résultat avec celui attendu
    boolean ok = d != null;
    StackTracer.printTestResult("Result", fmtDate);
    assertTrue(ok);       
  }

  @Test
  public void test05_getToday() {
    StackTracer.printCurrentTestMethod();
    Date d = DateTimeLib.getToday();
    String fmtDate = DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD);
    
    // on compare le résultat avec celui attendu
    boolean ok = d != null;
    StackTracer.printTestResult("Result", fmtDate);
    assertTrue(ok);       
  }

  @Test
  public void test06_getSmartToday() {
    StackTracer.printCurrentTestMethod();
    String smartToday = DateTimeLib.getSmartToday();
    
    // on compare le résultat avec celui attendu
    boolean ok = !smartToday.isEmpty();
    StackTracer.printTestResult("Result", smartToday);
    assertTrue(ok);       
  }

  @Test
  public void test07_parseIsoDate() {
    StackTracer.printCurrentTestMethod();
    String toParse = "2016-02-29";
    Date d = DateTimeLib.parseIsoDate(toParse);
    String parsed = DateTimeLib.dateToString(d);
    
    // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.isValidDate(d, 29, 2, 2016);
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed);
    assertTrue(ok);    
  }

  @Test
  public void test08_parseIsoDateWithTime() {
    StackTracer.printCurrentTestMethod();
    String toParse = "2016-02-29 23:08:01";
    Date d = DateTimeLib.parseIsoDate(toParse);
    String parsed = DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD);
    
    // on compare le résultat avec celui attendu
    boolean okDate = DateTimeLib.isValidDate(d, 29, 2, 2016);
    boolean okTime = DateTimeLib.isValidTime(d, 23, 8, 1);
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed);
    assertTrue(okDate && okTime);    
  }

  @Test
  public void test09_parseIsoDateWithTime() {
    StackTracer.printCurrentTestMethod();
    String toParse = "2016-02-29T23:08:01";
    Date d = DateTimeLib.parseIsoDate(toParse);
    String parsed = DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD);
    
    // on compare le résultat avec celui attendu
    boolean okDate = DateTimeLib.isValidDate(d, 29, 2, 2016);
    boolean okTime = DateTimeLib.isValidTime(d, 23, 8, 1);
    boolean ok = okDate && okTime;
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed);
    assertTrue(ok);  
  }

  @Test
  public void test10_parseDate() {
    StackTracer.printCurrentTestMethod();
    Date d1 = DateTimeLib.createDate(29, 2, 16);
    String info[] = DateTimeLib.getLocalePatternInfo();
    String toParse = DateTimeLib.dateToString(d1, info[2]);
    Date d2 = DateTimeLib.parseDate(toParse);
    String parsed = DateTimeLib.dateToString(d2);
    
    // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.isValidDate(d2, 29, 2, 2016);
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed);
    assertTrue(ok);   
  }

  @Test
  public void test11_parseDate_startOfMonth() {
    StackTracer.printCurrentTestMethod();
    String info[] = DateTimeLib.getLocalePatternInfo();
    String toParse = "2" + info[0] + "2016";
    Date d = DateTimeLib.parseDate(toParse, false);
    String parsed = DateTimeLib.dateToString(d);
    
    // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.isValidDate(d, 1, 2, 2016);
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed);
    assertTrue(ok);      
  }

  @Test
  public void test12_parseDate_endOfMonth() {
    StackTracer.printCurrentTestMethod();
    String info[] = DateTimeLib.getLocalePatternInfo();
    String toParse = "2" + info[0] + "2016";
    Date d = DateTimeLib.parseDate(toParse, true);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    String parsed = DateTimeLib.dateToString(d);
    
    // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.isValidDate(d, 29, 2, 2016);
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed);
    assertTrue(ok);  
  }

  @Test
  public void test13_parseTime() {
    StackTracer.printCurrentTestMethod();
    String info[] = DateTimeLib.getLocalePatternInfo();
    int hh = 23;
    int mm = 0;
    int ss = 0;
    String toParse = "" + hh + info[3]; // + mm + info[3] + ss;
    Date d = DateTimeLib.parseTime(toParse);
    String parsed = DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD);
    
    // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.isValidTime(d, hh, mm, ss);
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed);
    assertTrue(ok);     
  }

  @Test
  public void test14_parseTime() {
    StackTracer.printCurrentTestMethod();
    String toParse = "23:8:1";
    Date d = DateTimeLib.parseTime(toParse, DateTimeLib.getToday());
    String parsed = DateTimeLib.dateToString(d, DateTimeLib.DATE_TIME_FORMAT_STANDARD);
    
    // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.isValidTime(d, 23, 8, 1);
    StackTracer.printTestResult("To parse", toParse, "Parsed", parsed, "Ok", ok);
    assertTrue(ok);     
  }

  private Date[] checkOneDate(String sDate, boolean last) {
    Date d = DateTimeLib.parseIsoDate(sDate);
    Date dates[] = DateTimeLib.getYearWorkingDates(d, +1); // +1 mois dans le futur
    System.out.println("  - Ref: " + DateTimeLib.dateToString(d, DateTimeLib.ISO8601_DATE_FORMAT));
    System.out.println("  - Date[0]: " + DateTimeLib.dateToString(dates[0]));
    System.out.println("  - Date[1]: " + DateTimeLib.dateToString(dates[1]));
    if (!last) {
      System.out.println();
    }
    return dates;
  }

  @Test
  public void test15_getYearWorkingDates() {
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
  public void test16_dateToLocalDate() {
    StackTracer.printCurrentTestMethod();
    Date d = DateTimeLib.getNow();
    LocalDate localData = DateTimeLib.dateToLocalDate(d);
    

    // on compare le résultat avec celui attendu
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    boolean ok = localData != null;
    StackTracer.printTestResult("Ref", DateTimeLib.dateToString(d), "Result", localData.format(dtf));
    assertTrue(ok);     
  }

  @Test
  public void test17_getMonthDates() {
    StackTracer.printCurrentTestMethod();
    final int MONTHS_OFFSET = -11;
    Date refDate = DateTimeLib.createDate(1, 1, 2017);
    Date refDate2 = DateTimeLib.moveDate(refDate, Calendar.MONTH, MONTHS_OFFSET);
    
    int year = DateTimeLib.getYear(refDate2);
    Date d[] = DateTimeLib.getMonthDates(refDate, MONTHS_OFFSET);
    
    String fmtDate[] = new String[3];
    fmtDate[0] = DateTimeLib.dateToString(refDate2);
    fmtDate[1] = DateTimeLib.dateToString(d[0]);
    fmtDate[2] = DateTimeLib.dateToString(d[1]);
    
    // on compare le résultat avec celui attendu
    int iDebut[] = DateTimeLib.extractDateInfo(d[0]);
    int iFin[] = DateTimeLib.extractDateInfo(d[1]);
    boolean ok1 = iDebut[0] == 1 && iDebut[1] == 2 && iDebut[2] == year;
    boolean ok2 = iFin[0] == 29 && iFin[1] == 2 && iFin[2] == year;    
    boolean ok = ok1 && ok2;
    StackTracer.printTestResult("Ref", fmtDate[0], "Date1", fmtDate[1], "Date2", fmtDate[2]);
    assertTrue(ok);       
  }

  @Test
  public void test18_getYearDates() {
    StackTracer.printCurrentTestMethod();
    Date refDate = DateTimeLib.getToday();
    int year = DateTimeLib.getYear(refDate);
    Date d[] = DateTimeLib.getYearDates();
    
    String fmtDate[] = new String[3];
    fmtDate[0] = DateTimeLib.dateToString(refDate);
    fmtDate[1] = DateTimeLib.dateToString(d[0]);
    fmtDate[2] = DateTimeLib.dateToString(d[1]);
    
    // on compare le résultat avec celui attendu
    int iDebut[] = DateTimeLib.extractDateInfo(d[0]);
    int iFin[] = DateTimeLib.extractDateInfo(d[1]);
    boolean ok1 = iDebut[0] == 1 && iDebut[1] == 1 && iDebut[2] == year;
    boolean ok2 = iFin[0] == 31 && iFin[1] == 12 && iFin[2] == year;    
    StackTracer.printTestResult("Ref", fmtDate[0], "Date1", fmtDate[1], "Date2", fmtDate[2]);
    assertTrue(ok1 && ok2);       
  }

  @Test
  public void test19_getDaysBetweenTwoDates() {
    StackTracer.printCurrentTestMethod();
    Date d[] = new Date[2];
    d[0] = DateTimeLib.createDate(20, 10, 2016);
    d[1] = DateTimeLib.createDate(3, 11, 2016);
    
    String fmtDate[] = new String[2];
    fmtDate[0] = DateTimeLib.dateToString(d[0]);
    fmtDate[1] = DateTimeLib.dateToString(d[1]);      
    int days = DateTimeLib.getDaysBetweenTwoDates(d[0], d[1]);
    
    // on compare le résultat avec celui attendu
    boolean ok = days == 14;
    StackTracer.printTestResult("Date1", fmtDate[0], "Date2", fmtDate[1], "Days", days);
    assertTrue(ok);      
  }

  @Test
  public void test20_getMonday() {
    StackTracer.printCurrentTestMethod();
    Date today = DateTimeLib.getToday();
    Date monday = DateTimeLib.getMonday(today);
    
    String fmtDate[] = new String[2];
    fmtDate[0] = DateTimeLib.dateToString(today);
    fmtDate[1] = DateTimeLib.dateToString(monday);         
    
    // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.getDayOfWeek(monday) == 2;
    StackTracer.printTestResult("Ref", fmtDate[0], "Monday", fmtDate[1]);
    assertTrue(ok);     
  }

  @Test
  public void test21_getFriday() {
    StackTracer.printCurrentTestMethod();
    Date today = DateTimeLib.getToday();
    Date friday = DateTimeLib.getFriday(today);
    
    String fmtDate[] = new String[2];
    fmtDate[0] = DateTimeLib.dateToString(today);
    fmtDate[1] = DateTimeLib.dateToString(friday);       
    
   // on compare le résultat avec celui attendu
    boolean ok = DateTimeLib.getDayOfWeek(friday) == 6;
    StackTracer.printTestResult("Ref", fmtDate[0], "Friday", fmtDate[1]);
    assertTrue(ok);        
  }

  @Test
  public void test22_getMondayFriday() {
    StackTracer.printCurrentTestMethod();
    Date today= DateTimeLib.getToday();
    Date monday = DateTimeLib.getMonday(today);
    Date friday = DateTimeLib.getFriday(today);
    Date d[] = DateTimeLib.getMondayFriday(today);
    
    String fmtDate[] = new String[3];
    fmtDate[0] = DateTimeLib.dateToString(today);
    fmtDate[1] = DateTimeLib.dateToString(d[0]); 
    fmtDate[2] = DateTimeLib.dateToString(d[1]); 
    
    // on compare le résultat avec celui attendu
    boolean ok = d[0].getTime() == monday.getTime() && d[1].getTime() == friday.getTime();
    StackTracer.printTestResult("Ref", fmtDate[0], "Monday", fmtDate[1], "Friday", fmtDate[2]);
    assertTrue(ok); 
  }

  @Test
  public void test23_getWeekDates() {
    StackTracer.printCurrentTestMethod();
    int weekOffset = +1;
    Date refDate = DateTimeLib.getDate(weekOffset * 7);
    Date monday = DateTimeLib.getMonday(refDate);
    Date friday = DateTimeLib.getFriday(refDate);
    Date d[] = DateTimeLib.getWeekDates(weekOffset);

    // on compare le résultat avec celui attendu
    boolean ok = d[0].getTime() == monday.getTime() && d[4].getTime() == friday.getTime();
    StackTracer.printTestResult("Ref", DateTimeLib.dateToString(refDate));
    for (int i = 0; i < d.length; i++) {
      System.out.println("  - day " + i + " : " + DateTimeLib.dateToString(d[i]));
    }
    assertTrue(ok);   
  }

  @Test
  public void test24_getDateIndex() {
    StackTracer.printCurrentTestMethod();
    Date refDate = DateTimeLib.getToday();
    int idx = DateTimeLib.getDateIndex(refDate);

    // contrôle
    Date d[] = DateTimeLib.getMondayFriday(refDate);
    boolean ok1 = idx >= 0
      && refDate.getTime() >= d[0].getTime()
      && refDate.getTime() <= d[1].getTime();
    boolean ok2 = idx < 0;
    
    // on compare le résultat avec celui attendu
    boolean ok = ok1 || ok2;
    StackTracer.printTestResult("Ref", DateTimeLib.dateToString(refDate), "Index", idx, "Ok", ok);
    assertTrue(ok);     
    
  }

}
