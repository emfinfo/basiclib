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
   * @param orig l'objet à cloner
   * @return un nouvel objet
   * @throws FileException l'exception à traiter à un niveau supérieur
   */
  public static Object clone(Object orig) throws FileException {
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
    } catch (IOException | ClassNotFoundException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return newObj;
  }
  
  /**
   * Copy rapide d'un objet.
   * 
   * @param orig l'objet original à copier
   * @return l'objet copié
   * @throws FileException l'exception à traiter à un niveau supérieur
   */
  public static Object fastcopy(Object orig) throws FileException {
    Object obj = null;
    try {
      // Write the object out to a byte array
      FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
      try (ObjectOutputStream out = new ObjectOutputStream(fbos)) {
        out.writeObject(orig);
        out.flush();
      }

      // Retrieve an input stream from the byte array and read
      // a copy of the object back in. 
      ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
      obj = in.readObject();
    } catch (IOException | ClassNotFoundException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return obj;
  }
  
  /**
   * Sérialise un objet dans un fichier binaire sur le disque.
   *
   * @param obj l'objet à sérialiser.
   * @throws FileException l'exception à traiter à un niveau supérieur
   */
   public static void serialize(Object obj) throws FileException {
    ObjectOutput out;
    try {
      OutputStream os = new FileOutputStream(FILE_NAME);
      out = new ObjectOutputStream(os);
      out.writeObject(obj);
      out.close();
    } catch (IOException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
  }

  /**
   * Désérialise un objet sérialisé dans un fichier disque.
   *
   * @return l'objet désérialisé.
   * @throws FileException l'exception à traiter à un niveau supérieur
   */
  public static Object deserialize() throws FileException {
    ObjectInput in;
    try {
      InputStream is = new FileInputStream(FILE_NAME);
      in = new ObjectInputStream(is);
      Object newObj = in.readObject();
      in.close();
      return newObj;
    } catch (IOException | ClassNotFoundException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
  }
  
  
}
