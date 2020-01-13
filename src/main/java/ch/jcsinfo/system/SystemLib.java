package ch.jcsinfo.system;

import ch.jcsinfo.file.FileException;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/**
 * Classe de méthodes statiques concernant l'OS.
 *
 * @author Jean-Claude Stritt
 */
public class SystemLib {

  private SystemLib() {
  }

  
  
  /**
   * Reset les données pour log4J en relisant son fichier de propriétés.
   *
   * @param cl ClassLoader peut être obtenu par :<br>
   * ClassLoader cl = MaClasse.class.getClassLoader();
   */
  public static void resetLog4j(ClassLoader cl) {
    String propFile = "log4j.properties";
    LogManager.resetConfiguration();
    URL url = cl.getResource(propFile);
    if (url != null) {
      PropertyConfigurator.configure(url);
    }
  }

  
  
  /**
   * Ouvre une application du bureau basée sur le fichier transmis.
   * Ainsi, un fichier .pdf devrait s'ouvrir automatiquement avec Acrobat Reader
   * par exemple. Cette méthode devrait fonctionner avec tous les OS.
   *
   * @param fileName le fichier à ouvrir (avec son chemin)
   * @throws FileException l'exception qu'il faut traiter à un niveau supérieur
   */
  public static void openDesktopFile(String fileName) throws FileException {

    // on vérifie que la classe SystemLib soit bien supportée
    if (Desktop.isDesktopSupported()) {

      // on récupère une instance du bureau
      Desktop desktop = Desktop.getDesktop();

      // on vérifie que la fonction open est bien supportée
      if (desktop.isSupported(Desktop.Action.OPEN)) {

        // on lance l'application associée au fichier pour l'ouvrir
        try {
          desktop.open(new File(fileName));
        } catch (IOException ex) {
          throw new FileException(SystemLib.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
        }
      }
    }
  }



  /**
   * Met l'application en attente pour un certain nombre de millisecondes.
   *
   * @param ms nombre de millisecondes
   */
  public static void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
    }
  }



  /**
   * Change de charset avec celui spécifié en paramètre.<br>
   * La méthode getCharsetList peut retourner la liste des charsets valides.
   *
   * @param ch le nom d'un charset valide.
   */
  public static void changeCharset(String ch) {
    Charset cs = Charset.defaultCharset();
    System.setProperty("file.encoding", ch);
    try {
      PrintStream ps = new PrintStream(System.out, true, ch);
      System.setOut(ps);
    } catch (UnsupportedEncodingException ex) {
    }
//    System.out.println("\n"
//              + "Old CHARSET: " + cs.name() + "\n"
//              + "New CHARSET: " + System.getProperty("file.encoding")
//              + "\nàâäÂÄéèêëîïÎÏÊËôöÔÖûüùÛÜÿç");
  }

  /**
   * Retourne une liste des charsets disponible sur l'OS.
   *
   * @return une liste des charsets possibles sur l'OS
   */
  public static List<String> getCharsetList() {
    List<String> list = new ArrayList<>();
    for (Charset charset : Charset.availableCharsets().values()) {
      list.add(charset.displayName());
    }
    return list;
  }



  /**
   * Retourne TRUE si on se trouve sur un OS de type Windows.
   *
   * @return true si Windows
   */
  public static boolean isWindows() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("win"));
  }

  /**
   * Retourne TRUE si on se trouve sur un OS de type Mac.
   *
   * @return true si MacOS
   */
  public static boolean isMacOS() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("mac"));
  }
  
  /**
   * Retoune le nom du système d'exploitation sous-jacent, ainsi que
   * sa version entre paranthèses.
   * 
   * @return le nom du système d'exploitation et sa version
   */
  public static String getOS() {
    return System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")";
  }



  /**
   * Retourne un tableau d'informations sur l'utilisation de la mémoire.<br>
   * result[0] = la mémoire utilisée<br>
   * result[1] = la mémoire encore libre<br>
   * result[2] = le total de la mémoire à disposition<br>
   * result[3] = la mémoire maximale à disposition<br>
   *
   * @return un tableau avec les 4 informations ci-dessus
   */
  public static float[] getMemoryUsage() {
    final float MB = 1024 * 1024;
    float result[] = new float[4];
    Runtime r = Runtime.getRuntime();
    result[0] = (r.totalMemory() - r.freeMemory()) / MB;
    result[1] = r.freeMemory() / MB;
    result[2] = r.totalMemory() / MB;
    result[3] = r.maxMemory() / MB;
    return result;
  }

}
