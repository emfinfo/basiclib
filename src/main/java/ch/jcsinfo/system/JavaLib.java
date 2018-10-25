package ch.jcsinfo.system;

import ch.jcsinfo.file.FileException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Classe avec des méthodes statiques pour connaitre quelques informations de
 * l'environnement Java.
 *
 * @author Jean-Claude Stritt
 */
public class JavaLib {

  /**
   * Retourne la version Java par défaut utilisé par l'OS.
   * 
   * @return la version Java tournant sur l'OS
   */
  public static String getJavaVersion() {
    return System.getProperty("java.version");
  }
  
  /**
   * Retourne le nb de bits (32 ou 64) de l'actuel JRE utilisé.
   *
   * @return un entier 32 ou 64 (bits)
   */
  public static int getJavaVersionBits() {
    return Integer.parseInt(System.getProperty("sun.arch.data.model"));
  }

  /**
   * Retourne 2 valeurs correspondant à la version majeur et mineur de l'environnement de
   * compilation utilisé lors de la dernière compilation.
   *
   * @param filename un nom de fichier ".class" à tester
   * @return un tableau avec 2 valeurs (valeurs "majeur", "mineur")
   * @throws FileException une exception qu'il faut traiter à un niveau supérieure
   */
  public static int[] getJavaClassVersion(String filename) throws FileException {
    int[] data = new int[]{0, 0};
    DataInputStream in;
    try {
      in = new DataInputStream(new FileInputStream(filename));
      int magic = in.readInt();
      if (magic == 0xcafebabe) {
        data[1] = in.readUnsignedShort();
        data[0] = in.readUnsignedShort();
      }
      in.close();
    } catch (IOException ex) {
      throw new FileException(JavaLib.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return data;
  }

  /**
   * Retourne le nom de la plateforme Java (ex: J2SE 7) qui a été utilisée lors de la
   * dernière compilation d'une classe Java.
   *
   * @param filename un nom de fichier ".class" pour tester le nom de la plateforme
   * @return la plateforme Java utilisée
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static String getJavaClassPlatform(String filename) throws FileException {
    final String J2SE = "J2SE ";
    int[] data = getJavaClassVersion(filename);
    String res;
    switch (data[0]) {
      case 52:
        res = J2SE + "8";
        break;
      case 51:
        res = J2SE + "7";
        break;
      case 50:
        res = J2SE + "6.0";
        break;
      case 49:
        res = J2SE + "5.0";
        break;
      default:
        res = "JDK 1." + (data[0] - 44);
    }
    return res;
  }  
  
}
