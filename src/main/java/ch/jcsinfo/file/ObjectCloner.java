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
   * @param original l'objet à cloner
   * @return un nouvel objet
   * @throws ch.jcsinfo.file.FileException l'exception levée au moindre problème
   */
   public static Object clone(Object original) throws FileException {
    Object copy = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
      out.writeObject(original);
      ByteArrayInputStream bi = new ByteArrayInputStream(bos.toByteArray());
      try (ObjectInputStream in = new ObjectInputStream(bi)) {
        copy = in.readObject();
      } catch (ClassNotFoundException ex) {
        throw new FileException(ObjectCloner.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
      }
    } catch (IOException ex) {
      throw new FileException(ObjectCloner.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return copy;
  }

  /**
   * Copy rapide d'un objet.
   *
   * @param original l'objet original à copier
   * @return l'objet copié
   * @throws ch.jcsinfo.file.FileException l'exception levée au moindre problème
   */
  public static Object fastcopy(Object original) throws FileException {
    Object copy = null;
    FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
    try (ObjectOutputStream out = new ObjectOutputStream(fbos)) {
      out.writeObject(original);
      out.flush();
      try (ObjectInputStream in = new ObjectInputStream(fbos.getInputStream())) {
        copy = in.readObject();
      } catch (ClassNotFoundException ex) {
        throw new FileException(ObjectCloner.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
      }
    } catch (IOException ex) {
      throw new FileException(ObjectCloner.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return copy;
  }

  /**
   * Sérialise un objet dans un fichier binaire sur le disque.
   *
   * @param obj l'objet à sérialiser.
   * @throws ch.jcsinfo.file.FileException l'exception levée au moindre problème
   */
   public static void serialize(Object obj) throws FileException {
    try (OutputStream os = new FileOutputStream(FILE_NAME);) {
      ObjectOutput out = new ObjectOutputStream(os);
      out.writeObject(obj);
    } catch (IOException ex) {
      throw new FileException(ObjectCloner.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
  }

  /**
   * Désérialise un objet sérialisé dans un fichier disque.
   *
   * @throws ch.jcsinfo.file.FileException l'exception levée au moindre problème
   * @return l'objet désérialisé.
   */
  public static Object deserialize() throws FileException {
    Object result = null;
    try (InputStream is = new FileInputStream(FILE_NAME)) {
      ObjectInput in = new ObjectInputStream(is);
      result = in.readObject();
    } catch (IOException | ClassNotFoundException ex) {
      throw new FileException(ObjectCloner.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return result;
  }


}
