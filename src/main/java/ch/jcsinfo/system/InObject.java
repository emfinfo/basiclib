package ch.jcsinfo.system;

import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.file.FileException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

/**
 * Classe de méthodes statiques permettant l'introspection et des appels
 * de méthodes programmés depuis un objet "source".
 *
 * @author Jean-Claude Stritt
 */
public class InObject {

  private InObject() {
  }

  /**
   * Trouve une méthode par introspection dans la classe de l'objet "source"
   * fourni. Si elle n'est pas trouvée, la méthode est encore recherchée
   * dans la classe parente (classe mère).
   *
   * @param source l'objet où rechercher la méthode
   * @param methodName le nom de la méthode recherchée
   * @param parameterTypes un ou plusieurs types des paramètres de la méthode recherchée
   * @return la méthode si elle a été trouvée, autrement null
   * @throws FileException l'exception qu'il faut traiter à un niveau supérieur
   */
  public static Method findMethod(Object source, String methodName, Class<?>... parameterTypes) throws FileException {
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
        throw new FileException(InObject.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
      }
    }
    return m;
  }

  /**
   * Appel d'une méthode contenue dans un objet source.
   *
   * @param source l'objet "source" où se trouve la méthode
   * @param method la méthode à appeler
   * @param parameters les éventuels paramètres de la méthode appelée
   * @return un objet de retour éventuel (certaines méthodes retournent void)
   */
  public static Object callMethod(Object source, Method method, Object... parameters) {
    final Method myMethod = method;
    Object result = null;
    try {
      myMethod.setAccessible(true); // ajout JCS 9.2.2014
      result = myMethod.invoke(source, parameters);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
      System.out.println("callMethod error: "+ex.getMessage());
    }
    return result;
  }

  /**
   * Appelle une méthode getter par introspection et récupère donc sa valeur.
   *
   * @param source l'objet où rechercher le getter (généralement un entity-bean)
   * @param getterName le nom de la méthode à appeler
   * @return un objet de tout type contenant la valeur
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static Object callGetter(Object source, String getterName) throws FileException {
    Object result = null;
    Method method = findMethod(source, getterName);
    if (method != null) {
      result = callMethod(source, method);
    }
    return result;
  }

  /**
   * Prépare une chaine de caractères avec tous les champs privés et leurs valeurs
   * pour un objet source de type "entity-bean". Utile donc pour déboguer le contenu
   * d'un object d'une classe de ce type.<br>
   * <br>Exemple :<br>
   * <code>
   * public String toString() {
   *   return fieldsToString(this);
   * }
   * </code>
   * <br>
   *
   * @param source l'objet source où rechercher les champs
   * @param dateFormat le format pour l'affichage des dates
   * @param dispFirstAsObject true si on désire mettre un pseudo-object devant la liste des champs
   * @return une chaîne de caractères avec tous les champs
   * @throws FileException l'exception qu'il faut traiter à un niveau supérieur
   */
  public static String fieldsToString(Object source, String dateFormat, boolean dispFirstAsObject) throws FileException {
    StringBuilder result = new StringBuilder();

    // débute par la classe simulant un objet
    if (dispFirstAsObject) {
      String srceObj = source.getClass().getSimpleName();
      result.append(Character.toLowerCase(srceObj.charAt(0)));
      result.append(srceObj.substring(1));
      result.append("=");
    }
    result.append("{");

    // détermine les champs déclarés dans la classe spécifiée
    Field[] fields = source.getClass().getDeclaredFields();

    // ajoute chaque champ (avec sa valeur) à la chaîne finale
    int cnt = 0;
    for (Field field : fields) {
      boolean ok1 = (field.getModifiers() &  Modifier.STATIC) == 0;
      boolean ok2 = ((field.getModifiers() &  Modifier.PRIVATE) > 0)
                 && ((field.getModifiers() &  Modifier.TRANSIENT) == 0);
      if (ok1 && ok2) {
        cnt++;
        if (cnt > 1) {
          result.append(", ");
        }
        result.append(field.getName());
        result.append(": ");
//        System.out.println("Field: "+field.getName()+", type: " + field.getType().getSimpleName());
        String sType = field.getType().getSimpleName().toLowerCase();
        String getType = (sType.equals("boolean"))?"is":"get";
        String methodName = getType + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Object value = callGetter(source, methodName);
        if (sType.equals("date") && value != null) {
          Date d1 = (Date) value;
          Date d2 = DateTimeLib.eraseTime(d1);
          if (d1.getTime() == d2.getTime()) {
            value = DateTimeLib.dateToString(d1, dateFormat);
          } else {
            value = DateTimeLib.dateTimeToString(d1);
          }
        }
        result.append(value);
      }
    }
    result.append("}");
    return result.toString();
  }

  /**
   * Prépare une chaine de caractères avec tous les champs privés et leurs valeurs
   * pour un objet source.
   *
   * @param source l'objet source où rechercher les champs
   * @return une chaîne de caractères avec tous les champs
   * @throws FileException l'exception qu'il faut traiter à un niveau supérieur
   */
  public static String fieldsToString(Object source) throws FileException {
    return fieldsToString(source, DateTimeLib.DATE_FORMAT_STANDARD, true);
  }

  /**
   * Prépare une chaine de caractères avec tous les champs privés et leurs valeurs
   * pour un objet source.
   *
   * @param source l'objet source où rechercher les champs
   * @param dispFirstAsObject true si on désire mettre un pseudo-object devant la liste des champs
   * @return une chaîne de caractères avec tous les champs
   * @throws FileException l'exception qu'il faut traiter à un niveau supérieur
   */
  public static String fieldsToString(Object source, boolean dispFirstAsObject) throws FileException {
    return fieldsToString(source, DateTimeLib.DATE_FORMAT_STANDARD, dispFirstAsObject);
  }

}
