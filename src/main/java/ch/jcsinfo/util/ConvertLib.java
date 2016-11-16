package ch.jcsinfo.util;

import ch.jcsinfo.math.MathLib;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

  /**
   * Décrypte une chaîne de caractères stockée en ASCII (DOS-850) dans
   * un tableau d'entiers de type short.
   *
   * @param buffer un tableau de caractères codés en ASCII dans des short
   * @param key    la clé pour décrypter le buffer
   * @return une chaîne de caractères décryptée
   */
  public static String encryptedBufferToString(short[] buffer, String key) {
    String result = Cypher.shortDosArrayToString(buffer, key);
    // result = bufferToString(result.toCharArray());
    return Cypher.substDecrypt(result);
  }

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
   * Convertit un string contenant un nombre entier en nombre entier de type
   * "int".
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
   * Convertit un nombre entier contenu dans un string en Integer.
   *
   * @param line une ligne de caractères où se trouve le nombre
   * @param pos  la position du début du nombre
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un entier
   */
  public static int stringToInt(String line, int pos, int len) {
    String s = line.substring(pos, pos + len).trim();
    return stringToInt(s);
  }

  /**
   * Convertit un string contenant un nombre entier en nombre entier de type
   * "long".
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
   * Convertit un nombre entier contenu dans un string en type Long.
   *
   * @param line une ligne de caractères où se trouve le nombre
   * @param pos  la position du début du nombre
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un entier de type long
   */
  public static long stringToLong(String line, int pos, int len) {
    String s = line.substring(pos, pos + len).trim();
    return stringToLong(s);
  }

  /**
   * Convertit un string contenant un nombre décimal en nombre flottant de type
   * "float".
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
   * Convertit un nombre flottant contenu dans un string en type "float".
   *
   * @param line une ligne de caractères où se trouve le nombre
   * @param pos  la position du début du nombre
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un flottant de type "float"
   */
  public static float stringToFloat(String line, int pos, int len) {
    String s = line.substring(pos, pos + len).trim();
    return stringToFloat(s);
  }

  /**
   * Convertit un string contenant un nombre décimal en nombre flottant de type
   * "double".
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
   * Convertit un nombre flottant contenu dans un string en type "double".
   *
   * @param line une ligne de caractères où se trouve le nombre
   * @param pos  la position du début du nombre
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un flottant de type "double"
   */
  public static double stringToDouble(String line, int pos, int len) {
    String s = line.substring(pos, pos + len).trim();
    return stringToDouble(s);
  }

  /**
   * Convertit un nombre (un prix par ex.) contenu dans un string en BigDecimal.
   *
   * @param line une ligne de caractères où se trouve le nombre
   * @param pos  la position du début du nombre
   * @param len  la longueur totale de caractères représentant le nombre
   * @return le nombre sous la forme d'un BigDecimal
   */
  public static BigDecimal stringToBigDecimal(String line, int pos, int len) {
    String s = line.substring(pos, pos + len).trim();
    return BigDecimal.valueOf(stringToDouble(s));
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
   * Convertit un object en String s'il est différent de null.
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
    return (param + fillString(len, ch)).substring(0, len);
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
   * Convertit un tableau d'octets en une chaine de caractères représentant les
   * valeurs
   * hexadécimales de ces octets.
   *
   * @param bytes le tableau d'octets
   * @return une chaîne représentant le contenu du tableau en hexadécimal
   */
  public static String getHexString(byte[] bytes) {
    String result = "";
    for (int i = 0; i < bytes.length; i++) {
      result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
  }

  /**
   * Convertit les octets d'une chaîne de caractères de type String en une
   * représentation hexadécimale.
   *
   * @param exp une expression de type String à convertir en hexadécimal
   * @return l'expression convertie en hexadécimal
   */
  public static String getHexString(String exp) {
    return getHexString(exp.getBytes());
  }

  /**
   * Permet de hâcher un string avec SHA-256.
   *
   * @param toHash une chaîne de caractère à hâcher avec SHA-256
   * @return la chaîne hâchée
   */
  public static String hashWithSHA256(String toHash) {
    String hash = "";
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(toHash.getBytes("UTF-8"));
      hash = getHexString(md.digest());
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
//      System.out.println("Erreur: " + ex.getMessage());
    }
    return hash;
  }

  /**
   * Rehâche une clé de 128 caractères avec le double sel contenu dans la clé.
   * Cette clé est composée comme suit :<br>
   * sel1 + motDePasse + sel2<br>
   * 48c  + 64c        + 16c = 128c<br>
   *
   * @param key la clé contenant le mot de passe déjà pré-hâché et un double sel
   * @param params éventuellement la clé de la BD pour y récupérer les sels
   * @return la clé rehâchée pouvant être considérée comme le mot de passe final
   */
  public static String rehashKeyWithSalt(String key, Object... params) {
    String s1 = key.substring(0, 48);
    String s2 = key.substring(112);
    if (params.length == 1) {
      String dbKey = (String)params[0];
      s1 = dbKey.substring(0, 48);
      s2 = dbKey.substring(112);
    }
    String salt = s1 + s2;
    String pwd = key.substring(48, 112);
    String result = s1 + hashWithSHA256(pwd + salt) + s2;
//    System.out.println("key:    " + key);
//    System.out.println("pwd:    " + pwd);
//    System.out.println("salt:   " + salt);
//    System.out.println("result: " + result);
    return result;
  }

  /**
   * Hache une nouvelle combinaison (nom de login + mot de passe).
   *
   * @param loginName un String avec un nom de login (username)
   * @param pwd un String avec un mot de passe
   *
   * @return le mot de passe haché sur 128 car.
   */
  public static String hashNewKey(String loginName, String pwd) {
    String s1 = getHexString(MathLib.randomStr('A', 'z', 24));
    String s2 = getHexString(MathLib.randomStr('A', 'z', 8));
    String key = s1 + hashWithSHA256(loginName + hashWithSHA256(pwd)) + s2;
    return rehashKeyWithSalt(key);
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
