package tests;

import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.system.StackTracer;
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
  public void test02_stringToDateDebutDeMois() {
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
  public void test03_stringToDateFinDeMois() {
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
    System.out.println("  - parsed    : " + DateTimeLib.dateToString(d));
    assertTrue(calendar.get(Calendar.YEAR)==2015 && calendar.get(Calendar.MONTH)==1 && calendar.get(Calendar.DAY_OF_MONTH)==28);
  }  
  
  private String[] checkOneDate(String sDate) {
    Date d = DateTimeLib.isoStringToDate(sDate);
    Date dates[] = DateTimeLib.getOneYearDates(d);
    String res[] = new String[2];
    res[0] = DateTimeLib.dateToString(dates[0], DateTimeLib.ISO8601_FORMAT_SHORT);
    res[1] = DateTimeLib.dateToString(dates[1], DateTimeLib.ISO8601_FORMAT_SHORT);
    System.out.println("  - date[0] : " + res[0]);
    System.out.println("  - date[1] : " + res[1] + "\n");  
    return res;
  }
  
  @Test
  public void test05_getExtendedCalendarYearDates() {
    StackTracer.printCurrentTestMethod();
    String r[];
    boolean ok[] = new boolean[4];
    
    r = checkOneDate ("2016-01-28");
    ok[0] = r[0].equals("2015-01-01") && r[1].equals("2016-01-31");
    
    r = checkOneDate ("2016-02-28");
    ok[1] = r[0].equals("2015-01-01") && r[1].equals("2016-02-29");
    
    r = checkOneDate ("2016-03-28");
    ok[2] = r[0].equals("2015-01-01") && r[1].equals("2016-03-31");
    
    r = checkOneDate ("2016-04-28");
    ok[3] = r[0].equals("2015-05-01") && r[1].equals("2016-04-30");
    
    assertTrue(ok[0] && ok[1] && ok[2] && ok[3]);
  }    
  
}
