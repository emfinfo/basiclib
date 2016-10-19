package ch.jcsinfo.datetime;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Classe de méthodes statiques permettant des conversions
 * facilitées entre date, heure et chaînes de caractères (String).
 *
 * @author Jean-Claude Stritt
 *
 */
public class DateTimeLib {
  public final static String DATE_FORMAT_STANDARD = "d.M.yyyy";
  public final static String DATE_FORMAT_SHORT = "dd.MM.yy";
  public final static String TIME_FORMAT_STANDARD = "HH:mm:ss";
  public final static String TIME_FORMAT_SHORT = "HH:mm";
  public final static String ISO8601_FORMAT_STANDARD = "yyyy-MM-dd HH:mm:ss";
  public final static String ISO8601_FORMAT_SHORT = "yyyy-MM-dd";
  public final static String DATE_TIME_FORMAT_STANDARD = "d.M.yy HH:mm:ss";
  public final static String DATE_TIME_FORMAT_FILENAME = "yyyy_MM_dd_HHmmss_SSS";
  public final static String DATE_TIME_FORMAT_SPECIAL = "dd-MM-yyyy HH:mm";

  private static long timeStamp = 0;

  /**
   * Retourne le format locale d'une date avec ou sans heure.
   *
   * @param format un format de date et/ou heure
   * @return le format local d'une date
   */
  public static SimpleDateFormat getLocaleFormat(String format) {
    Locale locale = Locale.getDefault();
    return new SimpleDateFormat(format, locale);
  }

  /**
   * Crée une date en fournissant toutes les données nécessaires.<br>
   *
   * @param day le jour
   * @param month le mois
   * @param year l'année
   * @param hour l'heure
   * @param min les minutes
   * @param sec les secondes
   * @return une date d'après les informations données
   */
  public static Date createDate(int day, int month, int year, int hour, int min, int sec) {

    // récupère un objet calendrier et le remplit avec la date spécifiée
    Calendar cal = new GregorianCalendar();

    // met les infos de temps à zéro
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.MONTH, month-1);
    cal.set(Calendar.YEAR, year);

    // met les infos de temps à zéro
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, min);
    cal.set(Calendar.SECOND, sec);
    cal.set(Calendar.MILLISECOND, 0);

      // retourne la date
    Date date = cal.getTime();
    return date;
  }

  /**
   * Crée une date en fournissant le jour, le mois et l'année.<br>
   * L'heure est mise à zéro.
   *
   * @param day le jour
   * @param month le mois
   * @param year l'année
   * @return une date d'après les informations données
   */
  public static Date createDate(int day, int month, int year) {
    return createDate(day, month, year, 0, 0, 0);
  }


  /**
   * Complète une date donnée en fournissant l'heure, les minutes, les secondes.
   *
   * @param date une date à compléter avec l'heure
   * @param hour l'heure
   * @param min les minutes
   * @param sec les secondes
   * @return un temps construit avec la date spécifiée et les informations fournies
   */
  public static Date setTime(Date date, int hour, int min, int sec) {
    int day = getDay(date);
    int month = getMonth(date);
    int year = getYear(date);
    return createDate(day, month, year, hour, min, sec);
  }

  /**
   * Efface les informations de temps (HH:MM:SS:mm) dans une date donnée.
   *
   * @param date une date quelconque
   * @return une date sans les informations de temps
   */
  public static Date eraseTime(Date date) {
    return setTime(date, 0, 0, 0);
  }


  /**
   * Retourne une date augmentée ou diminuée d'un certain nombre de jours ou de mois spécifié.
   * Le type doit être spécifié. Toute les informations sur l'heure sont mises à zéro.
   *
   * @param d la date d'origine
   * @param type le type de décalage (Calendar.DAY_OF_MONTH ou Calendar.MONTH)
   * @param nb le nombre de jours ou de mois à décaler
   * @return la nouvelle date
   */
  public static Date moveDate(Date d, int type, int nb) {
    Calendar c = new GregorianCalendar();
    c.setTime(d);
    c.add(type, nb);
    return eraseTime(c.getTime());
  }

  /**
   * Retourne une date augmentée ou diminuée d'un certain nombre de jours spécifié. Toutes
   * les informations sur l'heure sont mises à zéro.
   *
   * @param d la date d'origine
   * @param days le décalage en jours voulu
   * @return la date d'origine décalée d'un certain nombre de jours
   */
  public static Date moveDate(Date d, int days) {
    return moveDate(d, Calendar.DAY_OF_MONTH, days);
  }


  /**
   * Retourne la date du jour (avec heure, minutes, secondes, ms aussi).
   *
   * @return la date du jour
   */
  public static Date getDate() {
    Calendar c = new GregorianCalendar();
    return c.getTime();
  }

  /**
   * Retourne la date du jour augmentée ou diminuée d'un certain nombre de jours
   * spécifié. Toutes les informations sur l'heure sont mises à zéro.
   *
   * @param days le décalage en jours voulu
   * @return la date du jour décalé d'un certain nombre de jours
   */
  public static Date getDate(int days) {
    return moveDate(getDate(), days);
  }


  /**
   * Retourne la date de début d'une période.
   *
   * @param period un objet de type Period (avec des dates et des heures de début et fin)
   * @return une date de début de période
   */
  public static Date getStartDate(Period period) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(period.getStartDate());
    cal.add(Calendar.HOUR_OF_DAY, period.getStartHour());
    return cal.getTime();
  }

  /**
   * Retourne la date de fin d'une période.
   *
   * @param period un objet de type Period (avec des dates et des heures de début et fin)
   * @return une date de début de période
   */
  public static Date getEndDate(Period period) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(period.getEndDate());
    cal.add(Calendar.HOUR_OF_DAY, period.getEndHour());
    return cal.getTime();
  }

  /**
   * Extrait l'année d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return l'année extrait de la date donnée
   */
  public static int getYear(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.YEAR);
  }

  /**
   * Extrait le mois d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return le mois extrait de la date donnée
   */
  public static int getMonth(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.MONTH) + 1;
  }

  /**
   * Retourne le nombre de jours maximal dans un mois en tenant compte des années
   * bissextiles.
   *
   * @param date la date dont il faut examiner le mois
   * @return le nombre de jours maximal dans le mois extrait de la date
   */
  public static int getMonthMaxDay(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * Retourne le nom du mois courant dans une chaîne de caractères.
   *
   * @param month le numéro du mois (1...12)
   * @return le nom du mois en clair
   */
  public static String getMonthName(int month) {
    return new DateFormatSymbols().getMonths()[month - 1];
  }

  /**
   * Extrait le jour d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return le jour extrait de la date donnée
   */
  public static int getDay(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Retourne un tableau d'entiers avec le jour, le mois et l'année
   * extraits de la date spécifiée en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau d'entiers avec le jour, le mois et l'année
   */
  public static int[] extractDateInfo(Date date) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    int[] info = new int[3];
    info[0] = c.get(Calendar.DAY_OF_MONTH);
    info[1] = c.get(Calendar.MONTH) + 1;
    info[2] = c.get(Calendar.YEAR);
    return info;
  }

  /**
   * Retourne un tableau avec les dates de début et de fin de mois pour la date spécifiée.
   * On peut également fournir un offset en mois pour décaler cette date.
   *
   * @param curDate la date courante
   * @param monthsOffset un offset en nombre de mois (positif ou négatif)
   * @return un tableau avec les deux dates calculées
   */
  public static Date[] getMonthDates(Date curDate, int monthsOffset) {
    Date retDates[] = new Date[2];

    // recalcule une date avec l'offset des mois fourni
    Date newDate = moveDate(curDate, Calendar.MONTH, monthsOffset);

    // extrait les infos de la date calculée
    int info[] = extractDateInfo(newDate);

    // retourne les dates de début et fin de mois
    retDates[0] = createDate(1, info[1], info[2]);
    retDates[1] = createDate(getMonthMaxDay(retDates[0]), info[1], info[2]);
    return retDates;
  }

  /**
   * Retourne un tableau avec les dates de début et de fin de mois par rapport
   * à la date courante du jour.
   *
   * On peut également fournir un offset en mois pour décaler cette date.
   *
   * @param monthsOffset un offset en nombre de mois (positif ou négatif)
   * @return un tableau avec les deux dates calculées
   */
  public static Date[] getMonthDates(int monthsOffset) {
    return getMonthDates(getDate(), monthsOffset);
  }


  /**
   * Retourne un tableau avec 2 dates qui correspondent au 1.1 et au 31.12 de l'année civile
   * extraite de la date courante fournie.
   *
   * @param curDate une date courante spécifiée
   * @return un tableau avec 2 dates
   */
  public static Date[] getYearDates(Date curDate) {
     Date retDates[] = new Date[2];
     int curDateInfo[] = extractDateInfo(curDate);
     retDates[0] = createDate(1,   1, curDateInfo[2]);
     retDates[1] = createDate(31, 12, curDateInfo[2]);
     return retDates;
  }

  /**
   * Retourne un tableau avec 2 dates qui correspondent au 1.1 et au 31.12
   * de l'année civile en cours.
   *
   * @return un tableau avec les 2 dates calculées
   */
  public static Date[] getYearDates() {
    return getYearDates(getDate());
  }

  /**
   * Retourne un tableau avec 2 dates qui correspondent à :<br>
   * date[0] : la date de début du mois d'une année auparavant (-12 mois) par rapport à la date fournie<br>
   * date[1] : la date de fin de mois de la date courante spécifiée<br>
   * <br>
   * Si la date fournie est avant le mois d'avril, les premiers mois de l'année
   * précédente sont également inclus dans date[0]. Donc, au final date[0]
   * correspond à 12 jusqu'à 15 mois antérieurs à date[1].
   *
   * @param curDate une date courante spécifiée
   * @return un tableau avec les 2 dates calculées
   */
  public static Date[] getWorkYearDates(Date curDate) {
    Date retDates[] = new Date[2];

    // date courante (spécifiée)
    int curDateinfo[] = extractDateInfo(curDate);
    int nbMonths = (curDateinfo[1] < 4) ? 12 + curDateinfo[1] : 12;

    // date correspondante à 12 jusqu'à 15 mois avant la date spécifiée
    Date oldDate = moveDate(curDate, Calendar.MONTH, -nbMonths + 1);
    int oldDateInfo[] = extractDateInfo(oldDate);

    // préparation des 2 dates
    retDates[0] = createDate(1, oldDateInfo[1], oldDateInfo[2]);
    retDates[1] = createDate(getMonthMaxDay(curDate), curDateinfo[1], curDateinfo[2]);
    return retDates;
  }

  /**
   * Retourne un tableau avec 2 dates qui correspondent à :<br>
   * date[0] : la date de début du mois d'une année auparavant (-12 mois) par rapport à la date courante du jour<br>
   * date[1] : la date de fin de mois pour cette même date courante du jour<br>
   * <br>
   * Si la date est avant le mois d'avril, les premiers mois de l'année précédente sont également inclus dans date[0].
   * Donc, au final date[0] correspond à 12 jusqu'à 15 mois antérieurs à la date finale (date[1]).
   *
   * @return un tableau avec les 2 dates calculées
   */
  public static Date[] getWorkYearDates() {
    return getWorkYearDates(getDate());
  }

  /**
   * Extrait le jour de la semaine (0=dimanche à 7 samedi).
   *
   * @param date une date au format java.util.Date
   * @return int le jour dans la semaine de la date spécifiée (0=dimanche à 7 samedi)
   */
  public static int getDayOfWeek(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_WEEK);
  }

  /**
   * Calcule le nombre de jours entre 2 dates en tenant compte de l'heure d'été.
   *
   * @param oldDate la date la plus vieille
   * @param newDate la date la plus récente
   * @return int le nombre de jours entre les deux dates
   */
  public static int getDaysBetweenTwoDates(Date oldDate, Date newDate) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(oldDate);
    long dayBefore = oldDate.getTime() + cal.get(Calendar.DST_OFFSET);
    cal.setTime(newDate);
    long dayAfter = newDate.getTime() + cal.get(Calendar.DST_OFFSET);
    return (int) ((dayBefore - dayAfter) / (1000L * 60 * 60 * 24));
  }

  /**
   * Teste si une année donnée est une année bissextile.
   *
   * @param year l'année à tester
   * @return true si c'est une année bissextile
   */
  public static boolean isLeapYear(int year) {
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.YEAR, year);
    return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
  }

  /**
   * Calcule et retourne l'âge d'une personne d'après sa date de naissance et la date
   * courante.
   *
   * @param birthDate la date de naissance (java.util.Date)
   * @return l'âge sous la forme d'un entier (int)
   */
  public static int getCurrentAge(Date birthDate) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(birthDate);
    GregorianCalendar now = new GregorianCalendar();
    int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
    if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
            || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
            && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
      res--;
    }
    return res;
  }

  /**
   * Compare 2 dates en enlevant les informations de temps qui y seraient stockées.
   *
   * @param d1 la première date
   * @param d2 une seconde date à comparer
   * @return true si les 2 dates sont identiques
   */
  public static boolean compareDates(Date d1, Date d2) {
    return eraseTime(d1).getTime() == eraseTime(d2).getTime();
  }

  /**
   * Retourne les heures d'une date donnée.
   *
   * @param date une date (avec l'heure)
   * @return les heures de cette date
   */
  public static int getHour(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Retourne les minutes d'une date donnée (avec l'heure).
   *
   * @param date une date (avec l'heure)
   * @return les minutes de cette date avec heure
   */
  public static int getMinute(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.MINUTE);
  }

  /**
   * Retourne les secondes d'une date donnée (avec l'heure).
   *
   * @param date une date (avec l'heure)
   * @return les minutes de cette date avec heure
   */
  public static int getSecond(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.SECOND);
  }

  /**
   * Retourne un tableau d'entiers avec l'heure, les minutes, les secondes
   * et les milisecondes de la date spécifiée en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau[4] d'entiers avec hh, mm, ss, ms
   */
  public static int[] extractTimeInfo( Date date ) {
    Calendar c = new GregorianCalendar();
    c.setTime(date);
    int[] info = new int[4];
    info[0] = c.get(Calendar.HOUR_OF_DAY);
    info[1] = c.get(Calendar.MINUTE);
    info[2] = c.get(Calendar.SECOND);
    info[3] = c.get(Calendar.MILLISECOND);
    return info;
  }

  /**
   * Retourne un tableau d'entiers avec le jour, le mois, l'année, l'heure,
   * les minutes, les secondes et les milisecondes de la date spécifiée
   * en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau[7] avec JJ, MM, AA, hh, mm, ss, ms
   */
  public static int[] extractDateTimeInfo( Date date ) {
    Calendar c = new GregorianCalendar();
    int[] info = new int[7];
    info[0] = c.get(Calendar.DAY_OF_MONTH);
    info[1] = c.get(Calendar.MONTH) + 1;
    info[2] = c.get(Calendar.YEAR);
    info[3] = c.get(Calendar.HOUR_OF_DAY);
    info[4] = c.get(Calendar.MINUTE);
    info[5] = c.get(Calendar.SECOND);
    info[6] = c.get(Calendar.MILLISECOND);
    return info;
  }



  /**
   * Convertit une chaîne de caractères (String) représentant une date en une date de la
   * classe java.util.Date. Cette version accepte les années avec ou sans le siècle et
   * également les dates sans l'année (cela prend alors l'année en cours).
   *
   * @param sDate la chaîne contenant une date
   * @return une date de la classe java.util.Date
   */
  public static Date stringToDate(String sDate) {
    String nDate = sDate;
    Date date = null;
    String t[] = nDate.split("\\.");
    if (t.length == 2) {
      nDate += "." + getYear(getDate());
      t = nDate.split("\\.");
    }
    if (t.length == 3) {
      SimpleDateFormat ldf;
      if (t[2].length() == 2) {
        ldf = getLocaleFormat(DATE_FORMAT_SHORT);
      } else {
        ldf = getLocaleFormat(DATE_FORMAT_STANDARD);
      }
      ldf.setLenient(false);
      try {
        date = ldf.parse(nDate);
      } catch (ParseException ex) {
        date = null;
      }
    }
    return date;
  }

  /**
   * Convertit une chaîne de caractères (String) représentant une date en une date de la
   * classe java.util.Date. Cette version accepte les années avec ou sans le siècle et
   * également les dates sans le jour. Si lastDayOfMonth=false, alors le 1er jour
   * du mois est pris, autrement c'est le dernier jour du mois.
   *
   * @param sDate la chaîne contenant une date
   * @param lastDayOfMonth force la fin de mois si le string contient le mois+l'année
   * @return une date de la classe java.util.Date
   */
  public static Date stringToDate(String sDate, boolean lastDayOfMonth) {
    String nDate = sDate;
    String t[] = nDate.split("\\.");
    Date d = stringToDate(nDate);
    if (t.length == 2) {
      nDate = "1." + nDate;
      d = stringToDate(nDate);
      if (lastDayOfMonth) {
        d = createDate(getMonthMaxDay(d), getMonth(d), getYear(d));
      }
    }
    return d;
  }

  private static int stringToInt(String sValue) {
    int value = 0;
    try {
      value = Integer.parseInt(sValue);
    } catch (NumberFormatException ex) {
    }
    return value;
  }

  /**
   * Convertit une chaîne de caractères (String) représentant une date au format
   * ISO 8601 (ex 2015-12-31) en une date de la classe java.util.Date.
   *
   * @param sDate la chaîne ISO8601 contenant une date
   * @return une date de la classe java.util.Date
   */
  public static Date isoStringToDate(String sDate) {
    Date d = null;
    if (sDate.length() == 10) {
      int year = stringToInt(sDate.substring(0, 4));
      int month = stringToInt(sDate.substring(5, 7));
      int day = stringToInt(sDate.substring(8));
      d = createDate(day, month, year);
    }
    return d;
  }

  /**
   * Convertit un temps journalier au format hh:mm:ss en une date Java standard.
   * Le temps est complété avec une date fournie.
   *
   * @param sTime une chaine de caractères au format hh:mm:ss
   * @param d     une date pour compléter le temps
   * @return une date Java standard
   */
  public static Date timeStringToDate(String sTime, Date d) {
    int hh = (sTime.length() >= 2) ? stringToInt(sTime.substring(0, 2)) : 0;
    int mm = (sTime.length() >= 5) ? stringToInt(sTime.substring(3, 5)) : 0;
    int ss = (sTime.length() >= 8) ? stringToInt(sTime.substring(6, 8)) : 0;
    return setTime(d, hh, mm, ss);
  }



  /**
   * Convertit une date (java.util.Date) vers une représentation String construite avec le
   * format demandé.
   *
   * @param date une date de la classe java.util.Date
   * @param format un format de date (ou de temps) sous la forme d'un String
   * @return la même date au format String
   */
  public static String dateToString(Date date, String format) {
    String sDate = "...";
    if (date != null) {
      SimpleDateFormat ldf = getLocaleFormat(format);
      sDate = ldf.format(date);
    }
    return sDate;
  }

  /**
   * Convertit une date (java.util.Date) vers une représentation String standard.
   *
   * @param date une date de la classe java.util.Date
   * @return la même date au format String
   */
  public static String dateToString(Date date) {
    return dateToString(date, DATE_FORMAT_STANDARD);
  }

  /**
   * Convertit une date avec heure vers une représentation String standard.
   *
   * @param dateTime une date-heure de la classe java.util.Date
   * @return la même date-heure au format String
   */
  public static String dateTimeToString(Date dateTime) {
    return dateToString(dateTime, DATE_TIME_FORMAT_STANDARD);
  }

  /**
   * Retourne la date du jour avec le mois en clair.
   *
   * @return la date avec le mois en claire (ex: 1er janvier 2013)
   */
  public static String getToday() {
    Date d = getDate();
    int day = getDay(d);
    int month = getMonth(d);
    int annee = getYear(d);
    return ((day == 1) ? "1er" : String.valueOf(day))
            + " " + getMonthName(month) + " " + String.valueOf(annee);
  }



  /**
   * Convertit une date de type java.sql.Date vers une date de type java.util.Date.
   *
   * @param sqlDate une date au format java.sql.Date
   * @return une date au format java.util.Date
   */
  public static Date sqldateToDate(java.sql.Date sqlDate) {
    return new java.util.Date(sqlDate.getTime());
  }

  /**
   * Convertit une sqldate en sa représentation "chaîne de caractères".
   *
   * @param sqldate une date de la classe java.sql.Date
   * @param format le format pour la date à formater
   * @return une chaîne avec la représentation de la date
   */
  public static String sqldateToString(java.sql.Date sqldate, String format) {
    Date date = sqldateToDate(sqldate);
    return dateToString(date, format);
  }

  /**
   * Convertit une représentation de date (String) en une date
   * de la classe java.sql.Date.
   *
   * @param sDate une chaîne avec une date à l'intérieur
   * @return une date de la class java.sql.Date
   */
  public static java.sql.Date stringToSqldate(String sDate) {
    java.sql.Date sqlDate = null;
    Date d = stringToDate(sDate);
    if (d!=null) {
      sqlDate = new java.sql.Date(d.getTime());
    }
    return sqlDate;
  }



  /**
   * Cette méthode transforme un objet Date en un objet LocalDate (Java 8).
   * On définit la zone horaire de l'objet LocalDate à la zone horaire du système.
   *
   * @param date l'objet Date à transformer en LocalDate
   * @return l'objet LocalDate correspondant à la date de type Date.
   */
  public static LocalDate dateToLocalDate(Date date) {
    if (date != null) {
      return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    } else {
      return null;
    }
  }


  /**
   * Cette méthode transforme un objet LocalDate (Java 8) en un objet Date.
   * L'objet LocalDate ne possédant pas d'heure, on lui donne minuit et la zone horaire du système.
   *
   * @param localDate l'objet LocalDate à transformer en Date
   * @return l'objet Date correspondant à l'objet LocalDate
   */
  public static Date localDateToDate(LocalDate localDate) {
    if (localDate != null) {
      return Date.from(Instant.from(localDate.atStartOfDay(ZoneId.systemDefault())));
    } else {
      return null;
    }
  }



  /**
   * Teste si une date au format string est valide.
   *
   * @param sDate une date au format String
   * @return true (vrai) si la date est vailde, false (faux) autrement
   */
  public static boolean isValidDate(String sDate) {
    return stringToDate(sDate) != null;
  }

  /**
   * Teste si une date au format standard java.util.Date est valide, en testant si l'année
   * est dans une fourchette de +- un nombre d'années spécifié par rapport à l'année
   * courante.
   *
   * @param date une date au format java.util.Date à tester (null est accepté comme ok)
   * @param minusYears un nb d'années en dessous que l'on accepte
   * @param plusYears un nb d'années en dessus que l'on accepte
   *
   * @return true (vrai) si la date est dans la fourchette, false (faux) autrement
   */
  public static boolean isValidDateWithYear(Date date, int minusYears, int plusYears) {
    boolean ok = date == null;
    if (!ok) {
      int currentYear = getYear(getDate());
      int dateYear = getYear(date);
      int limits[] = new int[2];
      limits[0] = currentYear - minusYears;
      limits[1] = currentYear + plusYears;
      ok = dateYear >= limits[0] && dateYear <= limits[1];
    }
    return ok;
  }

  /**
   * Teste si une date au format String est valide, en testant encore si l'année est dans
   * une fourchette de +- un nombre d'années spécifié par rapport à l'année courante.
   *
   * @param sDate une date au format String à tester
   * @param minusYears un nb d'années en dessous que l'on accepte
   * @param plusYears un nb d'années en dessus que l'on accepte
   *
   * @return true (vrai) si la date est dans la fourchette, false (faux) autrement
   */
  public static boolean isValidDateWithYear(String sDate, int minusYears, int plusYears) {
    Date d = stringToDate(sDate);
    return d != null && isValidDateWithYear(d, minusYears, plusYears);
  }

  /**
   * Vérification si une nouvelle période (par ex. d'absence) ne chevauche pas une plus
   * ancienne.
   *
   * @param newPeriod nouvelle date et periode de début et de fin dans la journée
   * @param oldPeriod ancienne date et periode de début et de fin dans la journée
   * @return true si la nouvelle période ne chavauche PAS une plus ancienne
   */
  public static boolean isNewPeriodOk(Period newPeriod, Period oldPeriod) {
    boolean ok = false;
    Date newStartDate = getStartDate(newPeriod);
    Date newEndDate = getEndDate(newPeriod);
    Date oldStartDate = getStartDate(oldPeriod);
    Date oldEndDate = getEndDate(oldPeriod);
//    System.out.println("nouvelle début: " + dateTimeToString(newStartDate)
//            + "   fin: " + dateTimeToString(newEndDate));
//    System.out.println("ancienne début: " + dateTimeToString(oldStartDate)
//            + "   fin: " + dateTimeToString(oldEndDate));
    if (newStartDate.getTime() <= oldStartDate.getTime()) {
      if (oldStartDate.getTime() <= newEndDate.getTime()) {
        ok = true;
      }
    } else {
      if (oldEndDate.getTime() >= newStartDate.getTime()) {
        ok = true;
      }
    }
    return ok;
  }



  /**
   * Reset du chnonomètre interne de cette classe.
   */
  public static void chronoReset() {
    GregorianCalendar now = new GregorianCalendar();
    timeStamp = now.getTime().getTime();
  }

  /**
   * Retourne sous forme numérique (float) le temps écoulé depuis la mise à zéro du
   * chronomètre interne.
   *
   * @return un temps universel sous la forme d'un nombre réel.
   */
  public static float chronoElapsedTime() {
    GregorianCalendar now = new GregorianCalendar();
    return (float) (now.getTime().getTime() - timeStamp) / 1000;
  }

  /**
   * Retourne sous forme d'une chaîne de caractères String le temps écoulé depuis la mise
   * à zéro du chronomètre interne.
   *
   * @return le nombre de secondes écoulés jusqu'au 1/1000s
   */
  public static String chronoStringElapsedTime() {
    Locale locale = Locale.getDefault();
    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(locale);
    df.applyPattern("#,##0.000");
    return df.format(chronoElapsedTime());
  }

}
