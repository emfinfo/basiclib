package ch.jcsinfo.file;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sérialisation et désérialisation d'un objet.
 *
 * @author Jean-Claude Stritt
 */
public class ObjectCloner {
  private static final String FILE_NAME = "obj.ser";

  private ObjectCloner() {
  }

  /**
   * Permet de cloner un objet quelconque.
   *
   * @param original l'objet à cloner
   * @return un nouvel objet clone de celui fourni
   */
   public static Object clone(Object original) {
    Object copy = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
      out.writeObject(original);
      ByteArrayInputStream bi = new ByteArrayInputStream(bos.toByteArray());
      try (ObjectInputStream in = new ObjectInputStream(bi)) {
        copy = in.readObject();
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(ObjectCloner.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (IOException ex) {
      Logger.getLogger(ObjectCloner.class.getName()).log(Level.SEVERE, null, ex);
    }
    return copy;
  }

  /**
   * Copy rapide d'un objet.
   *
   * @param original l'objet original à copier
   * @return un nouvel objet, copie exact de celui fourni
   */
  public static Object fastcopy(Object original) {
    Object copy = null;
    FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(fbos)) {
      out.writeObject(original);
      out.flush();
      try (ObjectInputStream in = new ObjectInputStream(fbos.getInputStream())) {
        copy = in.readObject();
      } catch (ClassNotFoundException ex) {
        Logger.getLogger(ObjectCloner.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (IOException ex) {
      Logger.getLogger(ObjectCloner.class.getName()).log(Level.SEVERE, null, ex);
    }
    return copy;
  }

  /**
   * Sérialise un objet dans un fichier binaire sur le disque.
   *
   * @param obj l'objet à sérialiser.
   */
   public static void serialize(Object obj) {
    try (OutputStream os = new FileOutputStream(FILE_NAME);) {
      ObjectOutput out = new ObjectOutputStream(os);
      out.writeObject(obj);
    } catch (IOException ex) {
      Logger.getLogger(ObjectCloner.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Désérialise un objet sérialisé dans un fichier disque.
   *
   * @return l'objet désérialisé.
   */
  public static Object deserialize() {
    Object result = null;
    try (InputStream is = new FileInputStream(FILE_NAME)) {
      ObjectInput in = new ObjectInputStream(is);
      result = in.readObject();
    } catch (IOException | ClassNotFoundException ex) {
      Logger.getLogger(ObjectCloner.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
  }


}
