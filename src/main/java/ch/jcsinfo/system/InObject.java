package ch.jcsinfo.system;

import ch.jcsinfo.datetime.DateTimeLib;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de méthodes statiques permettant l'introspection et des appels
 * de méthodes programmés depuis un objet "source".
 *
 * @author Jean-Claude Stritt
 */
public class InObject {
  private static Logger logger = LoggerFactory.getLogger(InObject.class);

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
      } catch (NoSuchMethodException ex) {
        logger.error("{} « {} »", StackTracer.getCurrentMethod(), methodName + " " + NoSuchMethodException.class.getSimpleName());
      } catch (SecurityException ex) {
        logger.error("{} « {} »", StackTracer.getCurrentMethod(), methodName + " " + SecurityException.class.getSimpleName());
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
   * Appelle une méthode getter par introspection et récupère donc sa valeur.
   *
   * @param source l'objet où rechercher le getter (généralement un entity-bean)
   * @param getterName le nom de la méthode à appeler
   * @return un objet de tout type contenant la valeur
   */
  public static Object callGetter(Object source, String getterName) {
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
   */
  public static String fieldsToString(Object source, String dateFormat, boolean dispFirstAsObject) {
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
   */
  public static String fieldsToString(Object source) {
    return fieldsToString(source, DateTimeLib.DATE_FORMAT_STANDARD, true);
  }

  /**
   * Prépare une chaine de caractères avec tous les champs privés et leurs valeurs
   * pour un objet source.
   *
   * @param source l'objet source où rechercher les champs
   * @param dispFirstAsObject true si on désire mettre un pseudo-object devant la liste des champs
   * @return une chaîne de caractères avec tous les champs
   */
  public static String fieldsToString(Object source, boolean dispFirstAsObject) {
    return fieldsToString(source, DateTimeLib.DATE_FORMAT_STANDARD, dispFirstAsObject);
  }

}
