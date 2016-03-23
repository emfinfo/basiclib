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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de méthodes statiques concernant l'OS.
 *
 * @author Jean-Claude Stritt
 */
public class SystemLib {

  private static Logger logger = LoggerFactory.getLogger(SystemLib.class);

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
      logger.debug("{} with « {} » ok !", StackTracer.getCurrentMethod(), propFile);
    }
  }

  /**
   * Ouvre une application du bureau basée sur le fichier transmis.
   * Ainsi, un fichier .pdf devrait s'ouvrir automatiquement avec Acrobat Reader
   * par exemple. Cette méthode devrait fonctionner avec tous les OS.
   *
   * @param fileName le fichier à ouvrir (avec son chemin)
   */
  public static void openDesktopFile(String fileName) {

    // on vérifie que la classe SystemLib soit bien supportée
    if (Desktop.isDesktopSupported()) {

      // on récupère une instance du bureau
      Desktop desktop = Desktop.getDesktop();

      // on vérifie que la fonction open est bien supportée
      if (desktop.isSupported(Desktop.Action.OPEN)) {

        // on lance l'application associée au fichier pour l'ouvrir
        try {
          desktop.open(new File(fileName));
          logger.debug("{} « {} » ok !", StackTracer.getCurrentMethod(), fileName);
        } catch (IOException ex) {
          logger.error("{} « {} »", StackTracer.getCurrentMethod(), fileName + " (" + IOException.class.getSimpleName() + ")");
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
      logger.debug("{} now for « {} » ms", StackTracer.getCurrentMethod(), ms);
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
      logger.debug("{} to « {} » ok !", StackTracer.getCurrentMethod(), ch);
    } catch (UnsupportedEncodingException ex) {
      logger.error("{} « {} »", StackTracer.getCurrentMethod() + "(" + ch + ")", UnsupportedEncodingException.class.getSimpleName());
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
   * Trouve une méthode par introspection dans la classe de l'objet "source"
   * fourni. Si elle n'est pas trouvée, la méthode est encore recherchée
   * dans la classe parente (classe mère).
   *
   * @param source         l'objet où rechercher la méthode
   * @param methodName     le nom de la méthode recherchée
   * @param parameterTypes un ou plusieurs types des paramètres de la méthode recherchée
   * @return la méthode si elle a été trouvée, autrement null
   */
  // idem, mais on donne l'objet
//  public static Method findMethod(Object source, String methodName, Class<?>... parameterTypes) {
  public static Method findMethod(Object source, String methodName, Class<?>... parameterTypes) {
    Method m = null;

    // on recherche la méthode dans la classe de l'objet "source"
    try {
      Class<?> cl = source.getClass();
//      Method[] methods = cl.getDeclaredMethods();
//      for (Method method : methods) {
//        System.out.println("  - "+method.getName());
//      }
      m = cl.getDeclaredMethod(methodName, parameterTypes);
    } catch (NoSuchMethodException | SecurityException ex) {
    }

    // on recherche la méthode dans la classe parente
    if (m == null) {
      try {
        Class<?> c = source.getClass();
        Class<?> parent = c.getSuperclass();
//        System.out.println("Class: " + c.getSimpleName());
//        System.out.println("Parent: " + parent.getSimpleName());
        m = parent.getDeclaredMethod(methodName);
      } catch (NoSuchMethodException | SecurityException ex) {
      }
    }

    // si pas trouvé, on met un message dans le fichier de log
    if (m == null) {
      logger.debug("{}(« {} ») not found !", StackTracer.getCurrentMethod(), methodName);
    }
    return m;
  }
  
  /**
   * Appel d'une méthode contenue dans un objet source.
   *
   * @param source     l'objet "source" où se trouve la méthode
   * @param method     la méthode à appeler
   * @param parameters les éventuels paramètres de la méthode appelée
   * @return un objet de retour éventuel (certaines méthodes retournent void)
   */
  public static Object callMethod(Object source, Method method, Object... parameters) {
    final Method myMethod = method;
    Object result = null;
    try {
//      Object[] parameters = new Object[0];
      myMethod.setAccessible(true); // ajout JCS 9.2.2014
      result = myMethod.invoke(source, parameters);
    } catch (IllegalAccessException ex) {
      logger.error("{} « {} »", StackTracer.getCurrentMethod(), method.getName() + " " + IllegalAccessException.class.getSimpleName());
    } catch (IllegalArgumentException ex) {
      logger.error("{} « {} »", StackTracer.getCurrentMethod(), method.getName() + " " + IllegalArgumentException.class.getSimpleName());
    } catch (InvocationTargetException ex) {
      logger.error("{} « {} »", StackTracer.getCurrentMethod(), method.getName() + " " + InvocationTargetException.class.getSimpleName());
    }
    return result;
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
