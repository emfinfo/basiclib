package ch.jcsinfo.math;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Classe de méthodes statiques qui étendent l'utilisation de la classe Math.
 *
 * @author Jean-Claude Stritt
 */
public class MathLib {

  private static final int TWIPS_PER_INCH = 1440; // twips/inch
  private static final double CM_PER_INCH = 2.54; // cm/inch
  private static final double TWIPS_PER_CM = TWIPS_PER_INCH / CM_PER_INCH; // twips/cm
  private static final double ONE_PT_INCH = 1 / 72; // inch
  private static final double ONE_PT_CM = CM_PER_INCH / 72; // cm

  /**
   * Trouve un entier au hasard entre un minimum et un maximum.
   *
   * @param min la valeur minimale de l'entier à trouver
   * @param max la valeur maximale de l'entier à trouver
   * @return l'entier trouvé
   */
  public static int randomInt(int min, int max) {
    return min + (int) (Math.random() * ((max - min) + 1));
  }

  /**
   * Remplit une "chaine de caractères" avec des caractères au hasard.
   *
   * @param first le plus petit caractère prévu dans le résultat
   * @param last le plus grand caractère prévu dans le résultat
   * @param length la longueur attendue de la chaîne de caractères
   *
   * @return une chaîne remplie de caractères tirés au hasard
   */
  public static String randomStr(char first, char last, int length) {
    String s = "";
    int i1 = (int) first;
    int i2 = (int) last;
    for (int i = 0; i < length; i++) {
      s = s + (char) randomInt(i1, i2);
    }
    return s;
  }

  /**
   * Remplit une "chaine de caractères" avec des caractères au hasard, mais la
   * première lettre est en majuscule et les autres en minuscules.
   *
   * @param length la longueur attendue de la chaîne de caractères
   *
   * @return une chaîne remplie de caractères tirés au hasard
   */
  public static String randomName(int length) {
    String s1 = randomStr('A', 'Z', 1);
    String s2 = randomStr('a', 'z', length - 1);
    return s1 + s2;
  }

  /**
   * Retourne une date aléatoire pour une palette d'années fournie.
   *
   * @param firstYear la première année
   * @param lastYear la dernière année
   *
   * @return une date aléatoire dans la palette d'années fournie
   */
  public static Date randomDate(int firstYear, int lastYear) {
    int months[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int year = randomInt(firstYear, lastYear);
    int month = randomInt(1, 12);
    int maxDays = months[month - 1];

    // récupère un objet calendrier et le remplit avec la date spécifiée
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    if (cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365 && month == 2) {
      maxDays++;
    }
    int day = randomInt(1, maxDays);

    // met les infos de temps à zéro
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.YEAR, year);

    // met les infos de temps à zéro
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    // retourne la date
    Date date = cal.getTime();
    return date;
  }

  /**
   * Retourne un boolean aléatoire.
   *
   * @return false ou true au hasard
   */
  public static boolean randomBoolean() {
    return randomInt(0, 1) == 1;
  }

  /**
   * Convertit un boolean en nombre (0=false, 1=true).
   *
   * @param value la valeur booléenne àconvertir
   * @return un 0 ou un 1 suivant la valeur du booléen
   */
  public static int booleanToInt(boolean value) {
    return (value) ? 1 : 0;
  }

  /**
   * Arrondit un nombre réel à un certain nb de décimales.
   *
   * @param value la valeur "float" à arrondir
   * @param nbOfDecs le nombre de décimales
   * @return le nombre arrondi de type BigDecimal
   */
  public static BigDecimal convertToBigDecimal(double value, int nbOfDecs) {
    BigDecimal bd = BigDecimal.valueOf(value);
    bd = bd.setScale(nbOfDecs, BigDecimal.ROUND_HALF_UP);
    //  ligne suivante éventuellement importante à afficher si problème d'arrondi
//    System.out.println("valueToBigDecimal, value: " + value + ", result: " + bd);
    return bd;
  }

  /**
   * Arrondit un nombre réel à un certain nb de décimales.
   *
   * @param value la valeur à arrondir
   * @param multiple le multiple pour l'arrondi (0.5 par exemple)
   * @return la valeur arrondie de type BigDecimal
   */
  public static BigDecimal convertToBigDecimal(double value, float multiple) {
    BigDecimal a = new BigDecimal(String.valueOf(multiple));
    return MathLib.convertToBigDecimal(Math.round(value / multiple) * multiple, a.scale());
  }

  /**
   * Arrondit un nombre réel de type "float" à un certain nb de décimales.
   *
   * @param value la valeur à arrondir
   * @param nbOfDecs le nombre de décimales
   * @return le nombre arrondi
   */
  public static float roundFloatValue(float value, int nbOfDecs) {
    return MathLib.convertToBigDecimal(value, nbOfDecs).floatValue();
  }

  /**
   * Arrondit une valeur "float" à un multiple donné.
   *
   * @param value la valeur à arrondir
   * @param multiple le multiple pour l'arrondi (0.5 par exemple)
   * @return la valeur arrondie
   */
  public static float roundFloatValue(float value, float multiple) {
    BigDecimal a = new BigDecimal(String.valueOf(multiple));
    return roundFloatValue(Math.round(value / multiple) * multiple, a.scale());
  }

  /**
   * Arrondit un nombre réel de type "double" à un certain nb de décimales.
   *
   * @param value la valeur à arrondir
   * @param nbOfDecs le nombre de décimales
   * @return le nombre arrondi
   */
  public static double roundDoubleValue(double value, int nbOfDecs) {
    return MathLib.convertToBigDecimal(value, nbOfDecs).doubleValue();
  }

  /**
   * Arrondit une valeur "double" à un multiple donné.
   *
   * @param value la valeur à arrondir
   * @param multiple le multiple pour l'arrondi (0.5 par exemple)
   * @return la valeur arrondie
   */
  public static double roundDoubleValue(double value, float multiple) {
    BigDecimal a = new BigDecimal(String.valueOf(multiple));
    return roundDoubleValue(Math.round(value / multiple) * multiple, a.scale());
  }

  /**
   * Retourne le nombre de digits d'un nombre entier de type Integer
   *
   * @param number le nombre entier à tester
   * @return un entier représentant le nombre de digits du nombre
   */
  public static int getNbrOfDigits(long number) {
    return (number == 0)?1:(1 + (int) Math.floor(Math.log10(Math.abs(number))));
  }

  /**
   * Retourne l'index maximal d'une liste, 0 au minimum.
   *
   * @param list la liste à traiter
   * @return l'index maximal de la liste
   */
  public static int getLastIndex(List<?> list) {
    return Math.max(0, list.size() - 1);
  }

  /**
   * Retourne l'index précédent, 0 étant le minimum non dépassé.
   *
   * @param index la valeur de l'index à diminuer
   * @return l'index diminué de 1, minimum 0
   */
  public static int getPreviousIndex(int index) {
    return Math.max(0, index - 1);
  }

  /**
   * Retourne l'index suivant dans une liste, l'index maximal étant la longueur
   * de la liste - 1.
   *
   * @param index la valeur actuel de l'index dans la liste
   * @param list la liste à traiter
   * @return l'index augmenté de 1, maximum = list.size() - 1
   */
  public static int getNextIndex(int index, List<?> list) {
    return Math.min(index + 1, getLastIndex(list));
  }

  /**
   * Contrôle et retourne l'index courant pour une liste donnée, l'index minimal
   * étant 0 et l'index maximal étant la longueur de la liste - 1.
   *
   * @param index la valeur actuel de l'index dans la liste
   * @param list la liste à traiter
   * @return l'index courant, minimum=0, maximum=list.size() - 1
   */
  public static int checkIndex(int index, List<?> list) {
    return Math.max(0, Math.min(index, getLastIndex(list)));
  }

  /**
   * Convertit des [twips] en [cm] en arrondissant le calcul à un nb de
   * décimales spécifié.
   *
   * @param twips le nombre de twips à convertir
   * @param nbOfDecs le nombre de décimales pour l'arrondi
   * @return des twips convertis en [cm]
   */
  public static double twipsToCm(int twips, int nbOfDecs) {
    return roundDoubleValue((double) twips / TWIPS_PER_CM, nbOfDecs);
  }

  /**
   * Convertit des [twips] en [mm] en arrondissant le calcul à un nb de
   * décimales spécifié.
   *
   * @param twips le nombre de twips à convertir
   * @param nbOfDecs le nombre de décimales pour l'arrondi
   * @return des twips convertis en [mm]
   */
  public static double twipsToMm(int twips, int nbOfDecs) {
    return roundDoubleValue((double) twips / TWIPS_PER_CM * 100, nbOfDecs);
  }

  /**
   * Convertit des [cm] en [twips] (1440 twips/pouce) qui sont arrondis à
   * l'entier le plus proche.
   *
   * @param cm les centimètres à convertir
   * @return le résultat de la conversion en [twips]
   */
  public static int cmToTwips(double cm) {
    return (int) roundDoubleValue(cm * TWIPS_PER_CM, 0);
  }

  /**
   * Convertit des [cm] en [pt] (1/72 de pouce) qui sont arrondis à l'entier le
   * plus proche.
   *
   * @param cm les centimètres à convertir
   * @return le résultat de la conversion en [pt]
   */
  public static int cmToPt(double cm) {
    return (int) roundDoubleValue(cm / ONE_PT_CM, 0);
  }

  /**
   * Convertit des [pt] (1/72 de pouce) en [cm].
   *
   * @param pt un nombre de points typographiques [pt]
   * @param nbOfDecs le nombre de décimales pour l'arrondi
   * @return les [pt] traduits en [cm]
   */
  public static float ptToCm(float pt, int nbOfDecs) {
    float f = pt * (float) ONE_PT_CM;
    return roundFloatValue(f, nbOfDecs);
  }

  /**
   * Convertit un nom de colonne Excel en index (BASE 0).<br>
   * D'après le blog http://www.gregbugaj.com/?p=244
   *
   * @param columnName un nom de colonne (ex: "AA")
   * @return l'index correspondant (BASE 0)
   */
  public static int columnNameToIndex(String columnName) {
    columnName = columnName.toUpperCase();
    short value = 0;
    for (int i = 0, k = columnName.length() - 1; i < columnName.length(); i++, k--) {
      int alphabetIdx = ((short) columnName.charAt(i)) - 64;
      int delta;
      if (k == 0) {
        delta = alphabetIdx - 1;
      } else {
        if (alphabetIdx == 0) {
          delta = (26 * k);
        } else {
          delta = (alphabetIdx * 26 * k);
        }
      }
      value += delta;
    }
    return value;
  }

}
