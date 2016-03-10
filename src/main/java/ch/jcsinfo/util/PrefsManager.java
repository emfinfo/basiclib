package ch.jcsinfo.util;

import ch.jcsinfo.file.FileHelper;
import ch.jcsinfo.printing.PrintHelper;
import ch.jcsinfo.models.Printer;
import ch.jcsinfo.models.PrinterCopy;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.print.PrintService;

/**
 * Cette classe opère sur les préférences de l'application.<br>
 * <br>
 * Sur Windows, cela va écrire des clés dans la base de registre sous :<br>
 * - HKEY_CURRENT_USER/Software/JavaSoft/Prefs/nom_application<br>
 * <br>
 * Sur MacOS, cela va écrire dans un fichier "com.apple.java.util.prefs.plist" sous :<br>
 * - /Users/nom_user/Library/Preferences<br>
 * <br>
 * L'application doit très tôt appeler "PrefsManager.getInstance.initPrefsDefaults"
 * avec le nom de l'application (par exemple "SAFdemo"). Ensuite, cette classe
 * est capable de retrouver des préférences ou de les modifier.<br>
 * Clés dépréciées : <br>
 * - PRINT_OPEN_RESULT, PRINT_ZIP_ALL <br>
 * - DB_DRIVER, DB_URL, DB_USER, DB_PSW <br>
 *
 * @author Jean-Claude Stritt
 */
public class PrefsManager {
  private static String nodeID = "";

  /**
   * Une liste de préférences assez généralistes pour pouvoir être utilisée
   * dans tout logiciel de type Java standalone.
   */
  public static enum Pref {
    BG_PICTURE_FILENAME, CURRENT_LAF, CURRENT_DPI,
    PICT_FOLDER, PICT_ZOOM_FACTOR, PICT_PREF_SIZE,
    PRINT_FOLDER_TEMPLATES, PRINT_FOLDER_RESULTS,
    PRINT_DEFAULT_PRINTER, PRINT_DEFAULT_TRAY,
    PRINT_DEFAULT_FORMAT, PRINT_DEFAULT_OPEN_FORMAT, PRINT_DELETE_FILES_AFTER,
    COPY1_PRINTER, COPY2_PRINTER, COPY3_PRINTER, COPY4_PRINTER,
    COPY1_TEXT, COPY2_TEXT, COPY3_TEXT, COPY4_TEXT,
    COPY1_NB, COPY2_NB, COPY3_NB, COPY4_NB,
    DISCOUNT_RATE, VAT_RATE, DELAY_DAYS, FIRST_CODE_A, FIRST_CODE_B,
    EXCEL_FILENAME, EXCEL_SHEET_INDEX, EXCEL_FIRST_LINE, EXCEL_ID_COLUMN,
    SIMULATION_MODE, MESURE_MODE,
    FILTER1_FIELD_IDX, FILTER2_FIELD_IDX, FILTER1_EXPR, FILTER2_EXPR,
    FILTER_LOG_AND, FILTER_LOG_OR, EXPORT_FOLDER, IMPORT_FOLDER,
    PRINT_OPEN_RESULT, PRINT_ZIP_ALL,
    DB_DRIVER, DB_URL, DB_USER, DB_PSW
  }

  /**
   * Récupère toutes les préférences settées pour le noeud ce cette application.
   * 
   * @return une liste des préférences
   */
  public static Preferences getPrefs() {
    Preferences prefs = Preferences.userRoot().node(nodeID);
    return prefs;
  }

  /**
   * Récupère une valeur de préférence de type "String".
   * 
   * @param keyName un nom de clé pour rechercher une préférence
   * @return la valeur de la clé sous la forme d'un String
   */
  public static String getValue( String keyName ) {
    String pr = "";
    Preferences prefs = getPrefs();
    if (prefs != null) {
      pr = prefs.get(keyName.toLowerCase(), "");
      if (pr == null) {
        pr = "";
      }
    }
    return pr;
  }

  /**
   * Récupère une valeur de préférence de type "String".
   * 
   * @param key une clé de type Pref à retrouver
   * @return la valeur de la clé sous la forme d'un String
   */
  public static String getValue(Pref key) {
    return getValue(key.toString());
  }

  /**
   * Mémorise une valeur de préférence de type "String".
   * 
   * @param keyName un nom de clé pour mettre à jour une préférence
   * @param value la valeur de la clé à mettre à jour
   */
  public static void setValue( String keyName, String value ) {
    Preferences prefs = getPrefs();
    prefs.put(keyName.toLowerCase(), value);
  }

  /**
   * Mémorise une valeur de préférence de type "String".
   * 
   * @param key une clé de type Pref à mettre à jour
   * @param value une valeur String à mettre à jour pour la clé donnée
   */
  public static void setValue(Pref key, String value) {
    setValue(key.toString(), value);
  }

  /**
   * Récupère une valeur de préférence de type "boolean".
   * 
   * @param keyName le nom d'une clé contenant un "booléen" à retrouver
   * @return la valeur de cette clé sous la forme d'un booléen
   */
  public static boolean getBoolean(String keyName) {
    String s = getValue(keyName);
    return s.equalsIgnoreCase("true");
  }

  /**
   * Récupère une valeur de préférence de type "boolean".
   * 
   * @param key une clé de type Pref à retrouver
   * @return la valeur de cette clé sous la forme d'un booléen
   */
  public static boolean getBoolean(Pref key) {
    return getBoolean(key.toString());
  }

  /**
   * Mémorise une valeur de préférence de type "boolean".
   * 
   * @param keyName le nom d'une clé à rechercher
   * @param value une valeur booléenne à mettre à jour
   */
  public static void setBoolean(String keyName, boolean value) {
    setValue(keyName, value ? "true" : "false");
  }

  /**
   * Mémorise une valeur de préférence de type "boolean".
   * 
   * @param key une clé de type Pref à rechercher
   * @param value une valeur booléenne à mettre à jour
   */
  public static void setBoolean(Pref key, boolean value) {
    setBoolean(key.toString(), value);
  }

  /**
   * Récupère une valeur de préférence de type "int" (Integer).
   * 
   * @param keyName un nom de clé à rechercher
   * @return la valeur de cette clé qui doit contenir un entier
   */
  public static int getInt(String keyName) {
    return ConvertLib.stringToInt(getValue(keyName));
  }

  /**
   * Récupère une valeur de préférence de type "int" (Integer).
   * 
   * @param key une clé de type Pref à rechercher
   * @return la valeur de cette clé qui doit contenir un entier
   */
  public static int getInt(Pref key) {
    return getInt(key.toString());
  }

  /**
   * Mémorise une valeur de type int (Integer).
   * 
   * @param keyName un nom de clé à rechercher
   * @param value une valeur de type Integer à mettre à jour
   */
  public static void setInt(String keyName, int value) {
    setValue(keyName, ""+value);
  }

  /**
   * Mémorise une valeur de type "int" (Integer).
   * 
   * @param key une clé de type Pref à rechercher
   * @param value une valeur de type Integer à mettre à jour
   */
  public static void setInt(Pref key, int value) {
    setInt(key.toString(), value);
  }

  /**
   * Récupère une valeur de préférence de type float.
   * 
   * @param keyName un nom de clé à rechercher
   * @return la valeur de cette clé (nombre réel de type float)
   */
  public static float getFloat(String keyName) {
    return ConvertLib.stringToFloat(getValue(keyName));
  }

  /**
   * Récupère une valeur de préférence de type float.
   * 
   * @param key une clé de type Pref à rechercher
   * @return la valeur de cette clé (nombre réel de type float)
   */
  public static float getFloat(Pref key) {
    return getFloat(key.toString());
  }

  /**
   * Mémorise une valeur de type float.
   * @param keyName un nom de clé à rechercher
   * @param value une valeur réelle (float) à mettre à jour
   */
  public static void setFloat(String keyName, float value) {
    setValue(keyName, ""+value);
  }

  /**
   * Mémorise une valeur de type float.
   * 
   * @param key une clé de type Pref à rechercher
   * @param value une valeur réelle (float) à mettre à jour
   */
  public static void setFloat(Pref key, float value) {
    setFloat(key.toString(), value);
  }


  /**
   * Récupére les propriétés de connexion stockées dans les préférences.
   * 
   * @return une liste de propriétés de connexion
   */
  public static Properties getConnectionProperties() {
    Properties props = new Properties();
    String prefixKey = "javax.persistence.jdbc."; // JPA 2.0
    props.put(prefixKey + "driver", getValue(Pref.DB_DRIVER));
    props.put(prefixKey + "url", getValue(Pref.DB_URL));
    props.put(prefixKey + "user", getValue(Pref.DB_USER));
    props.put(prefixKey + "password", getValue(Pref.DB_PSW));
    return props;
  }

  /**
   * Retourne la dimension retaillée d'une photo d'après le "zoom factor".
   *
   * @param origSize la taille originale d'une image
   * @return la dimension retaillée
   */
  public static Dimension getScaledPictureSize(Dimension origSize) {
    Dimension d = new Dimension(origSize);
    Float pzf;
    String v = getValue(Pref.PICT_ZOOM_FACTOR);
    try {
      pzf = Float.parseFloat(v.substring(0, v.indexOf(" %"))) / 100;
      d.setSize((int) (origSize.width * pzf), (int) (origSize.height * pzf));
    } catch (NumberFormatException ex) {
    }
    return d;
  }

  /**
   * Mémorise la dimension préférée des images.
   *
   * @param prefSize la dimension préférée
   */
  public static void setPrefPictureSize(Dimension prefSize) {
    String value = String.valueOf(prefSize.width) + "x" + String.valueOf(
      prefSize.height);
    setValue(Pref.PICT_PREF_SIZE, value);
  }

  /**
   * Récupère la dimension préférée pour une image.
   * 
   * @return la dimension préférée pour une image
   */
  public static Dimension getPrefPictureSize() {
    Dimension d = new Dimension(1, 1);
    String v = getValue(Pref.PICT_PREF_SIZE);
    int i = v.indexOf("x");
    if (i > 0) {
      try {
        int width = Integer.parseInt(v.substring(0, i));
        int height = Integer.parseInt(v.substring(i + 1));
        d.setSize(width, height);
      } catch (NumberFormatException ex) {
      }
    }
    return d;
  }

  /**
   * Récupère le ratio des DPI, 1.0 étant le 100%.
   * 
   * @return le ratio des DPI
   */
  public static float getDpiRatio() {
    String s = getValue(Pref.CURRENT_DPI);
    int i1 = s.lastIndexOf("(") + 1;
    int i2 = s.lastIndexOf(")") - 1;
    float result = 1f;
    try {
      result = Float.parseFloat(s.substring(i1, i2)) / 100f;
    } catch (NumberFormatException e) {
    }
    return result;
  }

  /**
   * Retourne un objet imprimante "Printer" d'après le nom enregistré
   * dans les préférences.
   *
   * @param key une clé sous la forme d'un String
   * @return un objet "imprimante" de type Printer
   */
  public static Printer getPrinter (String key) {
    PrintService prtSrv = PrintHelper.findPrintService(getValue(key));
//    System.out.println("key: "+ key + " value: "+getValue(key) + "prtSrv: "+prtSrv);
    return new Printer(prtSrv);
  }

  /**
   * Retourne un objet imprimante "Printer" d'après le nom enregistré
   * dans les préférences.
   *
   * @param key une clé de la classe Pref
   * @return un objet "imprimante" de type Printer
   */
  public static Printer getPrinter (Pref key) {
    return getPrinter(key.toString());
  }

  /**
   * Retourne un objet "PrinterCopy" qui contient les options
   * d'impression pour une copie spécialisée sur une imprimante.
   *
   * @param id un identificateur de la copie (de 1 à 4)
   * @return  un objet "PrinterCopy"
   */
  public static PrinterCopy getPrinterCopy(int id) {
    String PREFIX = "COPY" + id + "_";
    String printerName = getValue(PREFIX + "PRINTER"); //getPrinter(PREFIX + "PRINTER");
    int tray = getInt(PREFIX + "TRAY");
    String txt = getValue(PREFIX + "TEXT");
    int nb = getInt(PREFIX + "NB");
    return new PrinterCopy(id, printerName, tray, txt, nb);
  }

  /**
   * Mémorise les informations de type "PrinterCopy" qui définit les options
   * d'impression pour une copie spécialisée sur une imprimante.
   *
   * @param pc un objet "PrinterCopy"
   */
  public static void setPrinterCopy(PrinterCopy pc) {
    String PREFIX = "COPY" + pc.getId() + "_";
    setValue(PREFIX + "PRINTER", pc.getPrinterName());
    setInt(PREFIX + "TRAY", pc.getTray());
    setValue(PREFIX + "TEXT", pc.getText());
    setInt(PREFIX + "NB", pc.getNb());
  }

  /**
   * Retourne un tableau avec 4 objets "PrinterCopy" représentant 4 copies
   * d'impression possibles.
   *
   * @return un tableau avec 4 objets "PrinterCopy"
   */
  public static PrinterCopy[] getPrinterCopies() {
    PrinterCopy prtCopies[] = new PrinterCopy[4];
    for (int i = 0; i < 4; i++) {
      prtCopies[i] = getPrinterCopy(i+1);
    }
    return prtCopies;
  }

  /**
   * Permet de tester s'il faut initialiser l'un des paramètres par défaut
   * de l'application. Cela est certainement nécessaire au démarrage de l'application
   * lors de la première exécution du logiciel.
   * 
   * @param appTitle le titre de l'application
   */
  public static void initPrefsDefaults(String appTitle) {
    nodeID = appTitle;
    Properties props = FileHelper.loadProperties("defaults.properties");
    if (!props.isEmpty()) {
      // tri sur les noms des propriétés
      List<String> keys = new ArrayList<>();
      for(String key : props.stringPropertyNames()) {
        keys.add(key);
      }
      Collections.sort(keys);
      
      // boucle pour créer les clés par défaut
      for (String key : keys) {
        String lcKey = key.toLowerCase();
        String value = props.getProperty(lcKey);
        if (lcKey.equalsIgnoreCase("current_dpi")) {
          List<String> res = ScreenInfo.getScreenResolutions();
          for (String s : res) {
            if (s.contains(value)) {
              value = s;
              break;
            }
          }
        }
        if (lcKey.contains("folder") || lcKey.contains("filename")) {
          value = FileHelper.normalizeFileName(value);
        }
        if (lcKey.contains("db_url")) {
          value = FileHelper.buildNewDbUrl(value);
        }
        if (getValue(lcKey).isEmpty()) {
//          System.out.println(lcKey+"="+value);
          setValue(lcKey, value);
        }
        
      }
      
    }
    
  }

}
