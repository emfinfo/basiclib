package ch.jcsinfo.file;

import java.awt.Color;
import java.awt.Font;
import java.util.Properties;

/**
 * Classe qui permet de récupérer des propriétés sophistiquées d'un fichier
 * de ".properties", telles que texte, entier, couleur ou police. Exemples :<br>
 * <pre>
 *   Application.splash.text1=modules chargés
 *   Application.splash.bubble.size=15
 *   Application.splash.bubble.color=#005ed1
 *   Application.splash.font1=Sans Serif-PLAIN-12
 * </pre>
 *
 * @author jcstritt
 */
public class PropertyReader {
  private Properties props ;
  private String propPrefix;

  /**
   * Constructeur qui permet de préciser l'accès au fichier
   * des propriétés, ainsi qu'un préfixe pour les clés de recherche.
   *
   * @param propPath le chemin vers un fichier de propriétés
   * @param propPrefix un préfixe pour la recherche des clés
   */
  public PropertyReader(String propPath, String propPrefix) {
    props = FileHelper.loadProperties(propPath);
    this.propPrefix = propPrefix;
  }

  /**
   * Constructeur qui permet de préciser l'accès au fichier des propriétés.
   *
   * @param propPath le chemin vers un fichier de propriétés
   */
  public PropertyReader(String propPath) {
    this(propPath,"");
  }

  /**
   * Récupère une propriété de type "texte" (String).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def texte par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public String getTextProperty(String key, String def) {
    if (props.isEmpty()) {
      return def;
    } else {
      try {
        String s = props.getProperty(propPrefix + key);
        return s.isEmpty()?def:s;
      } catch (Exception e) {
        return def;
      }
    }
  }

  /**
   * Récupère une propriété de type "texte" (String).
   *
   * @param key nom de la clé de recherche de la propriété
   * @return la propriété recherchée
   */
  public String getTextProperty(String key) {
    return getTextProperty(key, "");
  }

  /**
   * Récupère une propriété de type "entier" (Integer).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def entier par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public int getIntProperty(String key, int def) {
    String s = getTextProperty(key);
    try {
      return Integer.parseInt(s.trim());
    } catch (NumberFormatException e) {
      return def;
    }
  }

  /**
   * Récupère une propriété de type "boolean".
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def entier par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public boolean getBooleanProperty(String key, boolean def) {
    String s = getTextProperty(key);
    return s.equalsIgnoreCase("true");
  }

  /**
   * Récupère une propriété de type "police" (Font).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def police par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public Font getFontProperty(String key, Font def) {
    String s = getTextProperty(key, "");
    if (s.isEmpty()) {
      return def;
    } else {
      String[] t = s.split("-");
      String name = t[0].trim();
      int style = Font.PLAIN;
      int size = 10;
      if (t.length >= 2) {
        t[1] = t[1].trim();
        if (t[1].equalsIgnoreCase("italic")) {
          style = Font.ITALIC;
        } else if (t[1].equalsIgnoreCase("bold")) {
          style = Font.BOLD;
        }
      }
      if (t.length >= 3) {
        size = Integer.parseInt(t[2].trim());
      }
      return new Font(name, style, size);
    }
  }

  /**
   * Récupère une propriété de type "couleur" (Color).
   *
   * @param key nom de la clé de recherche de la propriété
   * @param def texte par défaut si la propriété ne peut être récupérée
   * @return la propriété recherchée
   */
  public Color getColorProperty(String key, Color def) {
    String s = getTextProperty(key, "");
    if (s.isEmpty()) {
      return def;
    } else {
      return Color.decode(s);
    }
  }

  /**
   * Retourne une liste de propriétés prévues pour une base données
   * gérée avec JPA (utile pour supplanter les informations présentes
   * dans le fichier interne persistence.xml). Normalement le fichier
   * propriétés supplémentaires s'appellera "db.properties". Cette liste
   * ne peut être nulle, mais peut être vide.
   *
   * @return une liste de propriétés de DB
   */
  public Properties getDbProperties() {
    final String PREFIX_KEY = "javax.persistence.jdbc."; // JPA 2.0
    final String URL_KEY = "url";
    final String USER_KEY = "user";
    final String PSW_KEY = "psw";
    Properties dbProps = new Properties();

    // url
    String url = getTextProperty(URL_KEY);
    if (!url.isEmpty()) {
      dbProps.put(PREFIX_KEY + URL_KEY, url);
    }

    // user
    String user = getTextProperty(USER_KEY);
    if (!user.isEmpty()) {
      dbProps.put(PREFIX_KEY + USER_KEY, user);
    }

    // psw
    String psw = getTextProperty(PSW_KEY);
    if (!psw.isEmpty()) {
      dbProps.put(PREFIX_KEY + PSW_KEY, psw);
    }

    return dbProps;
  }
  
}
