package ch.jcsinfo.file;

import ch.jcsinfo.system.StackTracer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sérialisation et désérialisation d'un objet.
 *
 * @author Jean-Claude Stritt
 */
public class ObjectCloner {
  private static final String FILE_NAME = "obj.ser";
  private static Logger logger = LoggerFactory.getLogger(ObjectCloner.class);

  private ObjectCloner() {
  }
  
  /**
   * Méthode privée pour afficher un message de log.
   * 
   * @param method la méthode où a eu lieu l'erreur
   * @param msg le message d'erreur à afficher
   * @param err le type d'erreur
   */
  private static void log(String method, String msg, String err) {
    logger.error("{} « {} »", method, msg + ": " + err);
  }

  /**
   * Permet de cloner un objet quelconque.
   *
   * @param orig l'objet à cloner
   * @return un nouvel objet
   */
  public static Object clone(Object orig) {
    ObjectOutputStream out;
    ObjectInputStream in;
    Object newObj = null;
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      out = new ObjectOutputStream(bos);
      out.writeObject(orig);
      ByteArrayInputStream bi = new ByteArrayInputStream(bos.toByteArray());
      in = new ObjectInputStream(bi);
      newObj = in.readObject();
      out.close();
      in.close();
    } catch (IOException ex) {
      log(StackTracer.getCurrentMethod(), IOException.class.getSimpleName(), ex.getMessage());
    } catch (ClassNotFoundException ex) {
      log(StackTracer.getCurrentMethod(), ClassNotFoundException.class.getSimpleName(), ex.getMessage());
    }
    return newObj;
  }
  
  /**
   * Copy rapide d'un objet.
   * 
   * @param orig l'objet original à copier
   * @return l'objet copié
   */
  public static Object fastcopy(Object orig) {
    Object obj = null;
    try {
      // Write the object out to a byte array
      FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(fbos);
      out.writeObject(orig);
      out.flush();
      out.close();

      // Retrieve an input stream from the byte array and read
      // a copy of the object back in. 
      ObjectInputStream in
              = new ObjectInputStream(fbos.getInputStream());
      obj = in.readObject();
    } catch (IOException ex) {
      log(StackTracer.getCurrentMethod(), IOException.class.getSimpleName(), ex.getMessage());
    } catch (ClassNotFoundException ex) {
      log(StackTracer.getCurrentMethod(), ClassNotFoundException.class.getSimpleName(), ex.getMessage());
    }
    return obj;
  }
  

  /**
   * Sérialise un objet dans un fichier binaire sur le disque.
   *
   * @param obj l'objet à sérialiser.
   */
  static public void serialize(Object obj) {
    ObjectOutput out;
    try {
      OutputStream os = new FileOutputStream(FILE_NAME);
      out = new ObjectOutputStream(os);
      out.writeObject(obj);
      out.close();
    } catch (Exception ex) {
      log(StackTracer.getCurrentMethod(), Exception.class.getSimpleName(), ex.getMessage());
    }
  }

  /**
   * Désérialise un objet sérialisé dans un fichier disque.
   *
   * @return l'objet désérialisé.
   */
  static public Object deserialize() {
    ObjectInput in;
    try {
      InputStream is = new FileInputStream(FILE_NAME);
      in = new ObjectInputStream(is);
      Object newObj = in.readObject();
      in.close();
      return newObj;
    } catch (IOException ex) {
      log(StackTracer.getCurrentMethod(), IOException.class.getSimpleName(), ex.getMessage());
    } catch (ClassNotFoundException ex) {
      log(StackTracer.getCurrentMethod(), ClassNotFoundException.class.getSimpleName(), ex.getMessage());
    }
    return null;
  }
  
  
}
