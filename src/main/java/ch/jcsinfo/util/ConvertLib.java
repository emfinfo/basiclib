package ch.jcsinfo.util;

import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.math.MathLib;
import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Librairie de méthodes statiques qui permettent la conversion de données
 * provenant d'anciens fichiers binaires utilisés dans les programmes "PSP".<br>
 * <br>
 * Quelques autres méthodes de conversion sur des objets ont aussi été
 * rajoutées.
 *
 * @author Jean-Claude Stritt
 */
public class ConvertLib {

  private static final Charset DOS_CHARSET = Charset.forName("CP850");
  private static final Charset JAVA_CHARSET = Charset.forName("UTF-16BE");

  private static boolean isNegativ(short[] b) {
    return (b.length > 0) ? b[0] / 16 != 0 : false;
  }

  private static int getDecDot(short[] b) {
    return (b.length > 0) ? b[0] % 16 : 0;
  }

  /**
   * Retourne un string représentant un nombre entier. En entrée, on doit
   * fournir
   * un tableau d'octets qui stocke 2 digits/octet (on dit que les chiffres sont
   * codés en BCD -Binary Coded Digit-, donc comme de l'hexadécimal).
   *
   * @param b un tableau de short contenant 2 BCD / octet
   * @return un string avec la valeur
   */
  public static String bcdToString(short[] b) {
    String result = "";
    for (int i = 1; i < b.length; i++) {
      result = result + Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
  }

  /**
   * Permet de retrouver la valeur d'un nombre stocké en BCD sur plusieurs
   * octets et de le retourner comme un nombre entier.
   *
   * @param b nombre à convertir sous la forme d'un tableau de short
   * @return valeur du nombre converti en int
   */
  public static int bcdToInt(short[] b) {
    int bd = Integer.valueOf(bcdToString(b));
    if (isNegativ(b)) {
      bd = -bd;
    }
    return bd;
  }

  /**
   * Retourne un nombre bigDecimal d'après un tableau de nombres BCD.
   *
   * @param b un tableau avec 2 digits par octet
   * @return valeur du nombre converti en bigDecimal
   */
  public static BigDecimal bcdToBigDecimal(short[] b) {
    BigDecimal bd = BigDecimal.ZERO;
    if (b.length > 0) {
      int nbDec = getDecDot(b);
      String s = bcdToString(b);
      bd = new BigDecimal(s);
      bd = bd.scaleByPowerOfTen(-nbDec);
      if (isNegativ(b)) {
        bd = bd.negate();
      }
    }
    return bd;
  }

  /**
   * Conversion d'un buffer de caractères en string vers un charset
   * donné. Dans cette méthode, un octet nul est rajouté avant chaque octet
   * du buffer, car un string java est toujours codé sur 2 octets (UTF-16BE).
   *
   * @param buffer un tableau de caractères
   * @return le buffer converti en chaine dans le charset spécifié
   */
  public static String bufferToString(char[] buffer) {
    String s = "";
    if (buffer.length > 0) {
      CharBuffer cb = CharBuffer.wrap(buffer);
      byte cvtBuffer[] = JAVA_CHARSET.encode(cb).array();
      s = new String(cvtBuffer, DOS_CHARSET);
      s = s.replace(String.valueOf('\0'), "");
    }
    return s;
  }

//  /**
//   * Décrypte une chaîne de caractères stockée en ASCII (DOS-850) dans
//   * un tableau d'entiers de type short.
//   *
//   * @param buffer un tableau de caractères codés en ASCII dans des short
//   * @param key    la clé pour décrypter le buffer
//   * @return une chaîne de caractères décryptée
//   */
//  public static String encryptedBufferToString(short[] buffer, String key) {
//    String result = Cypher.shortDosArrayToString(buffer, key);
//    // result = bufferToString(result.toCharArray());
//    return Cypher.substDecrypt(result);
//  }

  /**
   * Convertit un entier représentant une date en une date standard Java.
   * Attention, dans l'entier, l'année est présente sans le siècle.
   *
   * @param iDate un entier stockant une date (512*aa + 32*mm + jj)
   * @return une date Java
   */
  public static Date intToDate(int iDate) {
    int jour = iDate % 32;
    int mois = (iDate / 32) % 16;
    int year = iDate / 512;
    if (iDate != 0) {
      if (year < 80) {
        year = year + 2000;
      } else {
        year = year + 1900;
      }
    }
    Calendar c = new GregorianCalendar(year, mois - 1, jour);
    return c.getTime();
  }



  /**
   * Teste si une chaîne spécifiée contient un code numérique entier.
   *
   * @param value la valeur à tester
   * @return true (vrai) si la valeur spécifiée est un nombre entier
   */
  public static boolean isIntNumber(String value) {
    try {
      Integer.parseInt(value);
    } catch (NumberFormatException ex) {
      return false;
    }
    return true;
  }

  /**
   * Convertit un string contenant un nombre entier en nombre entier de type "int".
   *
   * @param sValue la valeur string représentant un nombre entier
   * @return la valeur convertie en entier de type "int"
   */
  public static int stringToInt(String sValue) {
    int value = 0;
    try {
      value = Integer.parseInt(sValue);
    } catch (NumberFormatException ex) {
    }
    return value;
  }

  /**
   * Convertit un string contenant un nombre entier en nombre entier de type "long".
   *
   * @param sValue la valeur string représentant un nombre entier
   * @return la valeur convertie en entier de type "long"
   */
  public static long stringToLong(String sValue) {
    long value = 0;
    try {
      value = Long.parseLong(sValue);
    } catch (NumberFormatException ex) {
    }
    return value;
  }

  /**
   * Convertit un string contenant un nombre décimal en nombre flottant de type "float".
   *
   * @param sValue la valeur string représentant un nombre décimal
   * @return un nombre de type "float"
   */
  public static float stringToFloat(String sValue) {
    float value = 0f;
    try {
      value = Float.parseFloat(sValue);
    } catch (NumberFormatException ex) {
    }
    return value;
  }

  /**
   * Convertit un string contenant un nombre décimal en nombre flottant de type "double".
   *
   * @param sValue la valeur string représentant un nombre décimal
   * @return un nombre de type "double"
   */
  public static double stringToDouble(String sValue) {
    double value = 0d;
    try {
      value = Double.parseDouble(sValue);
    } catch (NumberFormatException ex) {
    }
    return value;
  }





  /**
   * Extrait une sous-chaîne de caractères depuis une position et une longueur données.
   *
   * @param text un texte dans lequel on extrait la sous-chaîne
   * @param pos  la position du début du nombre (base 1)
   * @param len  la longueur totale de caractères représentant le nombre
   * @return la sous-chaîne extraite
   */
  public static String getString(String text, int pos, int len) {
    int j = pos - 1;
    return text.substring(j, j + len).trim();
  }

  /**
   * Convertit un nombre entier contenu dans un string en Integer.
   *
   * @param text un texte dans lequel se trouve le nombre
   * @param pos  la position du début du nombre (base 1)
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un entier
   */
  public static int getInt(String text, int pos, int len) {
    return stringToInt(getString(text, pos, len));
  }

  /**
   * Convertit un nombre entier contenu dans un string en type Long.
   *
   * @param text un texte dans lequel se trouve le nombre
   * @param pos  la position du début du nombre (base 1)
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un entier de type long
   */
  public static long getLong(String text, int pos, int len) {
    return stringToLong(getString(text, pos, len));
  }

  /**
   * Convertit un nombre flottant contenu dans un string en type "float".
   *
   * @param text un texte dans lequel se trouve le nombre
   * @param pos  la position du début du nombre (base 1)
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un flottant de type "float"
   */
  public static float getFloat(String text, int pos, int len) {
    return stringToFloat(getString(text, pos, len));
  }

  /**
   * Convertit un nombre flottant contenu dans un string en type "double".
   *
   * @param text un texte dans lequel se trouve le nombre
   * @param pos  la position du début du nombre (base 1)
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un flottant de type "double"
   */
  public static double getDouble(String text, int pos, int len) {
    return stringToDouble(getString(text, pos, len));
  }

  /**
   * Convertit un nombre (un prix par ex.) contenu dans un string en BigDecimal.
   *
   * @param text un texte dans lequel se trouve le nombre
   * @param pos  la position du début du nombre (base 1)
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un BigDecimal
   */
  public static BigDecimal getBigDecimal(String text, int pos, int len) {
    BigDecimal result = BigDecimal.ZERO;
    String s = getString(text, pos, len);
    if (!s.isEmpty()) {
      if (s.lastIndexOf(".") < 0) {
        String frs = getString(text, pos, len - 2);
        String cts = getString(text, pos + frs.length(), 2);
        s = frs + "." + cts;
      }
      result = new BigDecimal(s);
    }
    return result;
  }

  /**
   * Extrait "1" ou "0" d'une chaine de caractères et le convertit en booléen.
   *
   * @param text un texte dans lequel se trouve le booléen
   * @param pos  la position du booléen (base 1)
   * @return true si la valeur est un "1" autrement false
   */
  public static boolean getBoolean(String text, int pos) {
    return getString(text, pos, 1).equals("1");
  }

  /**
   * Convertit une date contenue dans un texte en une vraie date java.util.Date.
   * Si la date ne peut être lue, retourne le 1.1.1970 comme date.
   *
   * @param text un texte dans lequel se trouve la date
   * @param pos  la position du début de la date (base 1)
   * @param fmt  le format de la date (ex: "YYMMDD", "DDMMYY" ou YYYY-MM-DD -ISO-)
   * @return la date au format java.util.Date
   */
  public static Date getDate(String text, int pos, String fmt) {
    Date date = DateTimeLib.createDate(1, 1, 1970);
    String s = getString(text, pos, fmt.length());
    String fmt2 = fmt.toUpperCase();

    if (fmt2.length() == 10 && fmt2.contains("-")) {
      Date d = DateTimeLib.parseIsoDate(s);
      if (d != null) {
        date = d;
      }
    } else if (fmt2.length() != 6) {
      Date d = DateTimeLib.parseDate(s);
      if (d != null) {
        date = d;
      }
    } else {
      int posDD = fmt2.indexOf("DD") + 1;
      int posMM = fmt2.indexOf("MM") + 1;
      int posYY = fmt2.indexOf("YY") + 1;
      if (posDD >= 1 && posMM >= 1 && posYY >= 1) {
        int yearDigits = fmt2.length() - fmt2.replace("Y", "").length();
        int dd = getInt(s, posDD, 2);
        int mm = getInt(s, posMM, 2);
        int yy = getInt(s, posYY, yearDigits);
        if (yearDigits == 2) {
          int siecle = DateTimeLib.getYear(DateTimeLib.getToday()) / 100;
          yy = siecle * 100 + yy;
        }
        date = DateTimeLib.createDate(dd, mm, yy);
      }
    }

    return date;
  }

  /**
   * Convertit une heure contenue dans un texte en une vraie date java.util.Date
   * avec l'heure.
   *
   * @param text un texte dans lequel se trouve l'heure
   * @param pos  la position du début de l'heure (base 1)
   * @param fmt  le format de l'heure (ex: "HH:MM:SS")
   * @param date une date qui complète l'heure
   * @return la date avec l'heure au format java.util.Date
   */
  public static Date getHour(String text, int pos, String fmt, Date date) {
    Date date2 = DateTimeLib.setTime(date, 0, 0, 0);
    String s = getString(text, pos, fmt.length());

    String fmt2 = fmt.toUpperCase();
    int posHH = fmt2.indexOf("HH") + 1;
    int posMM = fmt2.indexOf("MM") + 1;
    int posSS = fmt2.indexOf("SS") + 1;
    if (posHH >= 1 && posMM >= 1 && posSS >= 1) {
      int hh = getInt(s, posHH, 2);
      int mm = getInt(s, posMM, 2);
      int ss = getInt(s, posSS, 2);
      date2 = DateTimeLib.setTime(date, hh, mm, ss);
    }
    return date2;
  }




  /**
   * Convertit un objet en String s'il est différent de nul.
   *
   * @param aValue un objet à convertir
   * @param maxLen longueur maximale du string
   * @return un String
   */
  public static String objectToString(Object aValue, int maxLen) {
    String s = "";
    if (aValue != null) {
      s = (String) aValue;
      s = s.substring(0, Math.min(s.length(), maxLen));
    }
    return s;
  }

  /**
   * Convertit un objet (String) en entier.
   * En cas de problème, retourne 0.
   *
   * @param aValue la valeur à convertir
   * @return la valeur convertie en entier
   */
  public static int objectToInt(Object aValue) {
    int value = 0;
    try {
      value = (Integer) aValue;
    } catch (Exception ex) {
//      System.out.println("objectToInt: "+ex.getMessage());
    }
    return value;
  }

  /**
   * Convertit un objet (String) en Double.
   * En cas de problème, retourne 0.
   *
   * @param aValue la valeur à convertir
   * @return la valeur convertie en double
   */
  public static double objectToDouble(Object aValue) {
    double dValue = 0;
    try {
      dValue = (Double) aValue;
    } catch (Exception ex) {
//      System.out.println("objectToDouble: "+ex.getMessage());
    }
    return dValue;
  }

  /**
   * Convertit un objet (String) en BigDecimal en spécifiant la précision
   * (multiple).
   *
   * @param aValue la valeur à convertir
   * @param p      la précision désirée (multiple)
   * @return la valeur convertie en BigDecimal
   */
  public static BigDecimal objectToBigDecimal(Object aValue, float p) {
    double d = objectToDouble(aValue);
    return BigDecimal.valueOf(MathLib.roundDoubleValue(d, p));
  }

  /**
   * Convertit un objet (Date) en une vraie date Java.
   *
   * @param aValue la valeur à convertir
   * @return une java.util.date
   */
  public static Date objectToDate(Object aValue) {
    Date dValue = null;
    try {
      dValue = (Date) aValue;
    } catch (Exception ex) {
//      System.out.println("objectToDate: "+ex.getMessage());
    }
    return dValue;
  }



  /**
   * Prépare un string d'une certaine longueur avec un caractère spécifié.
   *
   * @param len la longueur à obtenir
   * @param ch le caractère qui remplira la chaine
   * @return le String avec les caractères demandés
   */
  public static String fillString(int len, char ch) {
    char[] array = new char[len];
    Arrays.fill(array, ch);
    return new String(array);
  }

  /**
   * Prépare un string avec un paramètre à gauche et le caractère demandé
   * à droite si la longueur spécifiée n'est pas atteinte.
   *
   * @param len la longueur à obtenir
   * @param ch le caractère qui remplira la chaine
   * @param param le paramètre String à mettre à gauche
   * @return le String avec les caractères demandés
   */
  public static String fillString(int len, char ch, String param) {
    String p = (param != null) ? param : "";
    return (p + fillString(len, ch)).substring(0, len);
  }

  /**
   * Prépare un string avec des espaces sur la longueur spécifiée.
   *
   * @param len la longueur à obtenir
   * @return le String avec les espaces désirés
   */
  public static String getBlankString(int len) {
    return fillString(len, ' ');
  }

  /**
   * Prépare un string avec un paramètre à gauche et des espaces à droite
   * si la longueur spécifiée n'est pas atteinte.
   *
   * @param len   la longueur à obtenir
   * @param param le paramètre String à mettre à gauche
   * @return le String avec le paramètre à gauche et les espaces à droite
   */
  public static String getBlankString(int len, String param) {
    return fillString(len, ' ', param);
  }

  /**
   * Remplace une chaîne par une autre, mais seulement les "n" premières.
   *
   * @param s          la chaîne originale
   * @param findStr    la sous-chaîne à chercher et remplacer
   * @param replaceStr la sous-chaîne de remplacement
   * @param howMany    le nombre de remplacement à faire
   * @return la chaîne modifiée avce les remplacements
   */
  public static String replace(String s, String findStr, String replaceStr, int howMany) {
    StringBuffer sb = new StringBuffer();
    Pattern p = Pattern.compile(findStr);
    Matcher m = p.matcher(s);
    int count = 0;
    while (m.find()) {
      if (count++ < howMany) {
        m.appendReplacement(sb, replaceStr);
      }
    }
    m.appendTail(sb);
    // System.out.println(sb);
    return sb.toString();
  }



  /**
   * Retourne les symboles pour le point décimal et le séparateur
   * de milliers, ceci d'après les données locales.
   *
   * @return les symboles
   */
  public static DecimalFormatSymbols getUnusualSymbols() {
    DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.getDefault());
    unusualSymbols.setDecimalSeparator('.');
    unusualSymbols.setGroupingSeparator('\'');
    return unusualSymbols;
  }

  /**
   * Formate un nombre (double, float, etc) d'après un format spécifié,
   * en tenant compte des données locales.
   *
   * @param number le nombre à formater
   * @param format le format pour le formatage
   * @return le nombre formaté
   */
  public static String formatNumber(Object number, String format) {
    DecimalFormat df = new DecimalFormat(format, getUnusualSymbols());
    return df.format(number);
  }

  /**
   * Formate un nombre (double, float, etc) contenant un pourcentage
   * comme un taux TVA par exemple.
   *
   * @param rating un pourcentage de type quelconque à formater
   * @return le nombre formaté
   */
  public static String formatRating(Object rating) {
    return formatNumber(rating, "0.##");
  }

  /**
   * Formate un montant d'argent à deux chiffres après la virgule en supprimant
   * le point décimal et en insérant des "0" devant ce montant. Cela sert
   * par exemple dans la création de fichier de paiements de type Credit 3 V11.
   *
   * @param mt le montant à formater
   * @param len la longueur finale du montant formaté
   * @return le montant formaté
   */
  public static String formatAmount(BigDecimal mt, int len) {
    String base = fillString(len, '0');
    String montant = formatNumber(mt.doubleValue(), "#0.00").replace(".", "");
    String formatted = base + montant;
    return formatted.substring(formatted.length()-len);
  }

  /**
   * Formate un entier en insérant des "0" devant ce nombre. Cela sert
   * par exemple dans la création de fichier de paiements de type Credit 3 V11.
   *
   * @param nb un nombre entier à formater
   * @param len la longueur finale du montant formaté
   * @return le montant formaté
   */
  public static String formatInteger(int nb, int len) {
    String base = fillString(len, '0');
    String entier = formatNumber(nb, "#0");
    String formatted = base + entier;
    return formatted.substring(formatted.length()-len);
  }

  /**
   * Formate une date au format "yyMMdd", utile par exemple dans la création
   * de fichier de paiements de type Credit 3 V11.
   *
   * @param d une date à formater
   * @return la date formatée
   */
  public static String formatDate(Date d) {
    SimpleDateFormat ldf = DateTimeLib.getLocaleFormat("yyMMdd");
    return ldf.format(d);
  }

  

  /**
   * Convertit une Map en un set de type Properties.
   *
   * @param <K> type de la clé
   * @param <V> type de la valeur
   * @param map la map à convertir
   * @return un objet de type Properties
   */
  public static <K, V> Properties mapToProperties(Map<K, V> map) {
    Properties props = new Properties();
    Set<Map.Entry<K, V>> entries = map.entrySet();
    for (Map.Entry<K, V> entry : entries) {
      String key = entry.getKey().toString();
      String value = entry.getValue().toString();
      props.put(key, value);
//      System.out.printf("%s = %s%n", key, value);
    }
    return props;
  }

}
