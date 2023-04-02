package ch.jcsinfo.util;

import ch.jcsinfo.file.FileException;
import ch.jcsinfo.file.FileHelper;
import ch.jcsinfo.models.Printer;
import ch.jcsinfo.models.PrinterCopy;
import ch.jcsinfo.printing.PrintHelper;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
 * L'application doit très tôt appeler "PrefsManager.initPrefsDefaults"
 * avec le nom de l'application (par exemple "SAFdemo"). Ensuite, cette classe
 * est capable de retrouver des préférences ou de les modifier.<br>
 * Clés dépréciées : <br>
 * - PRINT_OPEN_RESULT, PRINT_ZIP_ALL <br>
 * - DB_DRIVER, DB_URL, DB_USER, DB_PSW <br>
 *
 * @author Jean-Claude Stritt
 */
public class PrefsManager {
  private static Preferences prefs = Preferences.userRoot().node("prefs");  
  private static PrefCase prefCase = PrefCase.STANDARD;
  
  /**
   * Une énumération avec 3 types pour indiquer comment les noms de préférences seront stockés
   * dans la base de registres.
   */
  public static enum PrefCase {
    STANDARD,
    LOWCASE,
    UPCASE
  }

  /**
   * Une liste de préférences assez généralistes pour pouvoir être utilisées
   * dans tout logiciel de type Java.
   */
  public static enum Pref {
    BG_PICTURE_FILENAME, CURRENT_LAF, CURRENT_DPI, SHOW_TOOLBAR,
    PICT_FOLDER, PICT_ZOOM_FACTOR, PICT_PREF_SIZE,

    PRINT_FOLDER_TEMPLATES, PRINT_FOLDER_RESULTS,
    PRINT_DEFAULT_PRINTER, PRINT_DEFAULT_FORMAT,
    PRINT_DEFAULT_OPEN_FORMAT, PRINT_DELETE_FILES_AFTER,
    PRINT_WEB_OPEN_RESULT, PRINT_WEB_ZIP_ALL,
    COPY1_PRINTER, COPY1_TEXT, COPY1_NB,
    COPY2_PRINTER, COPY2_TEXT, COPY2_NB,
    COPY3_PRINTER, COPY3_TEXT, COPY3_NB,
    COPY4_PRINTER, COPY4_TEXT, COPY4_NB,

    DATA_EXPORT_FOLDER, DATA_IMPORT_FOLDER,
    EXCEL_FILENAME, EXCEL_SHEET_INDEX, EXCEL_FIRST_LINE, EXCEL_ID_COLUMN,

    DB_DRIVER, DB_URL, DB_USER, DB_PSW,
    FILTER1_FIELD_IDX, FILTER1_EXPR,
    FILTER2_FIELD_IDX, FILTER2_EXPR,
    FILTER_LOG_AND, FILTER_LOG_OR

  }
  
  
  
  // --------------------METHODES PRIVEES--------------------
  
  /**
   * Prépare un string d'une certaine longueur avec un caractère spécifié.
   * Cela permet de préparer un format genre "#.##" avec un certain nb de décimales pour
   * les méthodes setFloat et setDouble.
   *
   * @param len la longueur à obtenir
   * @param ch le caractère qui remplira la chaine
   * @return le String avec les caractères demandés
   */
  private static String fillString(int len, char ch) {
    char[] array = new char[len];
    Arrays.fill(array, ch);
    return new String(array);
  }

  /**
   * Force le séparateur "." et pas de séparateur de milliers pour les stockages
   * dans les préférences de valeurs "float" ou "double".
   *
   * @return les symboles
   */
  private static DecimalFormatSymbols getDefSymbols() {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    symbols.setDecimalSeparator('.');
//    unusualSymbols.setGroupingSeparator('\'');
    return symbols;
  }
  
  
  
  // --------------------METHODES PUBLIQUES--------------------
  
  /**
   * Récupère le nom du noeud des préférences de l'utilisateur.
   *
   * @return le nom du noeud
   */
  public static String getUserNodeName() {
    return prefs.name();
  }

  /**
   * Permet de spécifier le nom identifiant le noeud dans l'arbre des préférences de l'utilisateur.
   * Le nom abrégé de l'application est souvent utilisé pour cela.
   *
   * @param userNodeName le nom identifiant le noeud pour l'utilisateur courant
   */
  public static void setUserNodeName(String userNodeName) {
    PrefsManager.prefs = Preferences.userRoot().node(userNodeName);
  }  
  
  /**
   * Teste si les noms de paramètres sont en minuscules.
   * 
   * @return true si les noms sont en minuscules
   */
  public static PrefCase getPrefCase() {
    return prefCase;
  }

  /**
   * Mémorise le type de casse pour les noms des préférences.
   * 
   * @param prefCase une valeur de l'énumération PrefCase
   */
  public static void setPrefCase(PrefCase prefCase) {
    PrefsManager.prefCase = prefCase;
  }
  
  /**
   * Retourne le nom d'une préférence soit en minuscules, soit en majuscules.
   * 
   * @param pref une préférence de type String ou Enum
   * @return le nom de préférence en minuscules ou majuscules
   */
  public static String getKey(Object pref) {
    String key = pref.toString();
    PrefCase pc = getPrefCase();
    if (pc == PrefCase.LOWCASE) {
      key = key.toLowerCase();
    } else if (pc == PrefCase.UPCASE) {
      key = key.toUpperCase();    
    }
    return key;
  }
  
  /**
   * Permet de tester s'il faut initialiser l'un des paramètres par défaut
   * de l'application. Cela est certainement nécessaire au démarrage de l'application
   * lors de la première exécution du logiciel.
   *
   * @param nodeName généralement, le nom de l'application comme nom du noeud des préférences
   * @param prefCase comment les noms de préférences seront stockés (STANDARD, LLOWCASE et UPCASE
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static void initPrefsDefaults(String nodeName, PrefCase prefCase) throws FileException {
    setUserNodeName(nodeName);
    setPrefCase(prefCase);
    Properties props = FileHelper.loadProperties("defaults.properties");
    if (!props.isEmpty()) {
      
      // tri sur les noms des propriétés
      List<String> preferences = new ArrayList<>();
      for (String pref : props.stringPropertyNames()) {
        preferences.add(pref.toLowerCase());
      }
      Collections.sort(preferences);

      // boucle pour créer les clés par défaut
      for (String pref : preferences) {
        String value = props.getProperty(pref);
        
        // traitement spécial pour la résolution écran
        if (pref.equals("current_dpi")) {
          List<String> res = ScreenInfo.getScreenResolutions();
          for (String s : res) {
            if (s.contains(value)) {
              value = s;
              break;
            }
          }
        }
        
        // normalisation des préférences de dossiers ou fichiers
        if (pref.contains("folder") || pref.contains("filename")) {
          value = FileHelper.normalizeFileName(value);
        }
        
        // normalisation d'une URL
        if (pref.contains("db_url")) {
          value = FileHelper.buildNewDbUrl(value);
        }
        
        // si la préférence n'existe pas, on l'ajoute
        if (getValue(pref).isEmpty()) {
//          System.out.println(pref+"="+value);
          setValue(pref, value);
        }

      }

    }

  }  
  
  
  
  /**
   * Récupère une valeur de préférence de type "String".
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de la préférence sous la forme d'un String
   */
  public static String getValue(Object pref) {
   return prefs.get(getKey(pref), "").trim();
  }

  /**
   * Mémorise une valeur de préférence de type "String".
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur String à mémoriser
   */
  public static void setValue(Object pref, String value) {
    prefs.put(getKey(pref), value.trim());
  }

  
  
  /**
   * Récupère une valeur de préférence de type "boolean".
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette préférence sous la forme d'un booléen
   */
  public static boolean getBoolean(Object pref) {
    String s = getValue(pref);
    return s.equalsIgnoreCase("true");    
  }

  /**
   * Mémorise une valeur de préférence de type "boolean".
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur booléenne à mémoriser
   */
  public static void setBoolean(Object pref, boolean value) {
    setValue(pref, value ? "true" : "false");    
  }

  

  /**
   * Récupère une valeur de préférence de type "int" (Integer).
   * Si la préférence ne peut être lue, 0 est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette préférence
   */
  public static int getInt(Object pref) {
    return prefs.getInt(getKey(pref), 0);
  }

  /**
   * Mémorise une valeur de type int (Integer).
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type Integer à mémoriser
   */
  public static void setInt(Object pref, int value) {
    setValue(pref, "" + value);
  }
  
  
  
  /**
   * Récupère une valeur de préférence de type "long" (Long Integer).
   * Si la préférence ne peut être lue, 0 est retournée.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette préférence
   */
  public static long getLong(Object pref) {
    return prefs.getLong(getKey(pref), 0);
  }

  /**
   * Mémorise une valeur de type long (Long Integer).
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type Long à mémoriser
   */
  public static void setLong(Object pref, long value) {
    setValue(pref, "" + value);
  }  

  
  
  /**
   * Récupère une valeur de préférence de type "float".
   * Si la préférence ne peut être lue, 0f est retourné.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette clé (nombre réel de type float)
   */
  public static float getFloat(Object pref) {
    return prefs.getFloat(getKey(pref), 0f);
  }

  /**
   * Mémorise une valeur de type "float".
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type "float" à mémoriser
   * @param nbOfDecs nombre de decimales à mémoriser
   */
  public static void setFloat(Object pref, float value, int nbOfDecs) {
    String fmt = (nbOfDecs > 0) ? "0." + fillString(nbOfDecs, '0') : "0";
    setValue(pref, new DecimalFormat(fmt, getDefSymbols()).format(value));
  }



  /**
   * Récupère une valeur de préférence de type "double".
   * Si la préférence ne peut être lue, 0d est retourné.
   *
   * @param pref une préférence de type String ou Enum
   * @return la valeur de cette préférence
   */
  public static double getDouble(Object pref) {
    return prefs.getDouble(getKey(pref), 0d);
  }

  /**
   * Mémorise une valeur de type "double".
   *
   * @param pref une préférence de type String ou Enum
   * @param value une valeur de type "double" à mémoriser
   * @param nbOfDecs nombre de decimales à mémoriser
   */
  public static void setDouble(Object pref, double value, int nbOfDecs) {
    String fmt = (nbOfDecs > 0) ? "0." + fillString(nbOfDecs, '0') : "0";
    setValue(pref, new DecimalFormat(fmt, getDefSymbols()).format(value));
  }

  /**
   * Récupère un objet sérialisé dans une préférence.
   *
   * @param pref une préférence de type String ou Enum
   * @return l'objet désérialisé ou null;
   */
  public static Object getObject(Object pref) {
    Object obj = null;
    byte[] bytes = prefs.getByteArray(getKey(pref), new byte[0]);
    if (bytes.length > 0) {
      try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
        obj = in.readObject();
      } catch (ClassNotFoundException | IOException ex) {
      }
    }
    return obj;
  }

  /**
   * Mémorise un objet quelconque dans un tableau d'octets stockés dans les préférences de l'application.
   *
   * @param pref une préférence de type String ou Enum
   * @param value un objet de type quelconque, mais qui implémente la classe Serializable
   */
  public static void setObject(Object pref, Object value) {
    setValue(pref, "");
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ObjectOutput out;
    try {
      out = new ObjectOutputStream(os);
      out.writeObject(value);
      out.flush();
      byte[] bytes = os.toByteArray();
      prefs.putByteArray(getKey(pref), bytes);
    } catch (IOException ex) {
    } finally {
      try {
        os.close();
      } catch (IOException ex) {
      }
    }
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
   * Récupère la dimension mémorisée pour une image.
   *
   * @return la dimension mémorisée
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
   * Mémorise la dimension préférée des images.
   *
   * @param prefSize la dimension préférée
   */
  public static void setPrefPictureSize(Dimension prefSize) {
    String value = String.valueOf(prefSize.width) + "x" + String.valueOf(prefSize.height);
    setValue(Pref.PICT_PREF_SIZE, value);
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
   * @param pref une préférence de type String ou Enum
   * @return un objet "imprimante" de type Printer
   */
  public static Printer getPrinter(Object pref) {
    PrintService prtSrv = PrintHelper.findPrintService(getValue(pref));
//    System.out.println("key: "+ key + " value: "+getValue(key) + "prtSrv: "+prtSrv);
    return new Printer(prtSrv);
  }

  /**
   * Retourne un objet "PrinterCopy" qui contient les options
   * d'impression pour une copie spécialisée sur une imprimante.
   *
   * @param id un identificateur de la copie (de 1 à 4)
   * @return un objet "PrinterCopy"
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
      prtCopies[i] = getPrinterCopy(i + 1);
    }
    return prtCopies;
  }


}
