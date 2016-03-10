package ch.jcsinfo.system;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;

/**
 * Classe de méthodes statiques concernant l'OS.
 *
 * @author Jean-Claude Stritt
 */
public class SystemLib {

  /**
   * Ouvre une application du bureau basée sur le fichier transmis.
   * Ainsi, un fichier .pdf devrait s'ouvrir automatiquement avec Acrobat Reader
   * par exemple. Cette méthode devrait fonctionner avec tous les OS.
   *
   * @param myFile le fichier à ouvrir
   */
  public static void openDesktopFile(String myFile) {

    // on vérifie que la classe SystemLib soit bien supportée
    if (Desktop.isDesktopSupported()) {

      // on récupère une instance du bureau
      Desktop desktop = Desktop.getDesktop();

      // on vérifie que la fonction open est bien supportée
      if (desktop.isSupported(Desktop.Action.OPEN)) {

        // on lance l'application associée au fichier pour l'ouvrir
        try {
          desktop.open(new File(myFile));
          LoggerFactory.getLogger(SystemLib.class).debug("[{}] - Open file {} ...",
                  StackTracer.getCurrentMethod(), myFile);
        } catch (IOException ex) {
          LoggerFactory.getLogger(SystemLib.class).error(
                  "[{}] - Error when opening {} !",
                  StackTracer.getCurrentMethod(), myFile);
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
   * Trouve une méthode par introspection dans la classe de l'objet "source"
   * fourni. Si elle n'est pas trouvée, la méthode est encore recherchée
   * dans la classe parente (classe mère).
   *
   * @param source     l'objet où rechercher la méthode
   * @param methodName le nom de la méthode recherchée
   * @return la méthode si elle a été trouvée, autrement null
   */
  // idem, mais on donne l'objet
  public static Method findMethod(Object source, String methodName) {
    Method m = null;

    // on recherche la méthode dans la classe de l'objet "source"
    try {
      m = source.getClass().getDeclaredMethod(methodName);
    } catch (NoSuchMethodException | SecurityException e) {
    }

    // on recherche la méthode dans la classe parente
    if (m == null) {
      try {
        Class<?> c = source.getClass();
        Class<?> parent = c.getSuperclass();
//        System.out.println("Class: " + c.getSimpleName());
//        System.out.println("Parent: " + parent.getSimpleName());
        m = parent.getDeclaredMethod(methodName);
      } catch (NoSuchMethodException | SecurityException e) {
      }
    }

    // si pas trouvé, on met un message dans le fichier de log
    if (m == null) {
      LoggerFactory.getLogger(SystemLib.class).debug("{} - {}",
              StackTracer.getCurrentMethod(), "method not found !");
    }
    return m;
  }

  /**
   * Appel d'une méthode sans paramètre contenue dans un objet source.
   *
   * @param source l'objet "source" où se trouve la méthode
   * @param method la méthode à appeler
   */
  public static void callMethod(Object source, Method method) {
    final Method myMethod = method;
    try {
      Object[] args = new Object[0];
      myMethod.setAccessible(true); // ajout JCS 9.2.2014
      myMethod.invoke(source, args);
    } catch (IllegalAccessException e) {
      LoggerFactory.getLogger(SystemLib.class).error("{} - {}",
              StackTracer.getCurrentMethod(), method.getName() + " (IllegalAccessException)");
    } catch (IllegalArgumentException e) {
      LoggerFactory.getLogger(SystemLib.class).error("{} - {}",
              StackTracer.getCurrentMethod(), method.getName()+ " (IllegalArgumentException)");
    } catch (InvocationTargetException e) {
//      System.err.println("An InvocationTargetException was caught!");
//      Throwable cause = e.getCause();
//      System.out.format("Invocation of %s failed because of: %s%n", method, cause.getMessage());
      LoggerFactory.getLogger(SystemLib.class).error("{} - {}",
              StackTracer.getCurrentMethod(), method.getName()+ " (InvocationTargetException)");
    }
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
  public static boolean isMac() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("mac"));
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
      System.out.println("Exception error : " + ex.getMessage());
    }
//    System.out.println("\n"
//              + "Old CHARSET: " + cs.name() + "\n"
//              + "New CHARSET: " + System.getProperty("file.encoding")
//              + "\nàâäÂÄéèêëîïÎÏÊËôöÔÖûüùÛÜÿç");
  }
  
  /**
   * Change automatiquement le CHARSET selon l'OS.
   */
  public static void changeCharset() {
    if (isMac()) {
      changeCharset("macroman");
    } else {
      changeCharset("iso-8859-1");
    }
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
   * Reset les données pour log4J en relisant son fichier de propriétés.
   *
   * @param cl ClassLoader peut être obtenu par :<br>
   * ClassLoader cl = MaClasse.class.getClassLoader();
   */
  public static void resetLog4j(ClassLoader cl) {
    String propFile = "log4j.properties";
    LogManager.resetConfiguration();
    URL log4jUrl = cl.getResource(propFile);
    if (log4jUrl != null) {
      PropertyConfigurator.configure(log4jUrl);
    } else {
      LoggerFactory.getLogger(SystemLib.class).error(
              "[{}] - file {} not found error !",
              StackTracer.getCurrentMethod(), propFile);
    }
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
    final int NBDECS = 1;
    final float MB = 1024*1024;
    float result[] = new float[4];
    Runtime r = Runtime.getRuntime();
    result[0] = (r.totalMemory() - r.freeMemory()) / MB;
    result[1] = r.freeMemory() / MB;
    result[2] = r.totalMemory() / MB;
    result[3] = r.maxMemory() / MB;
//    System.out.println(">>> used memory:  "+String.format("%.1f", result[0]));
//    System.out.println(">>> free memory:  "+String.format("%.1f", result[1]));
//    System.out.println(">>> total memory: "+String.format("%.1f", result[2]));
//    System.out.println(">>> max memory:   "+String.format("%.1f", result[3]));
    return result;  
  }
  

}
