package ch.jcsinfo.system;

/**
 * Permet de récupérer des informations telles que la classe ou la méthode courante (très
 * utile pour les messages de debug).
 *
 * @author Jean-Claude Stritt
 */
public class StackTracer {
  /**
   * Méthode privée de récupération d'informations depuis la pile.
   *
   * @param e tableau d'informations
   * @param level niveau (0 = niveau de l'appelant, -1 = premier parent, ...)
   * @param which information à retourner<br> (1=classe, 2=méthode, 3=méthode+classe,
   * 4=classe+méthode)
   *
   * @return l'information récupérée
   */
  private static String trace( StackTraceElement e[], int level, int which ) {
    String answer = "";
    if (e != null && e.length >= level) {
      StackTraceElement s = e[level];
      if (s != null) {
        switch (which) {
          case 1:
            answer = s.getClassName();
            break;
          case 2:
            answer = s.getMethodName();
            break;
          case 3:
            answer = s.getMethodName() + "... (" + s.getClassName() + ")";
            break;
          case 4:
            answer = s.getClassName() + " (" + s.getMethodName() + "): ";
        }
      }
    }
    return answer;
  }

  /**
   * Récupère le nom de la classe courante de l'appelant.
   * 
   * @return le nom de la classe courante
   */
  public static String getCurrentClass() {
    return trace(Thread.currentThread().getStackTrace(), 2, 1);
  }

  /**
   * Récupère la classe parente de -N niveaux au dessus de la classe courante.
   * 
   * @param level un entier négatif représentant le niveau de parenté
   * @return le nom de la classe parente
   */
  public static String getParentClass( int level ) {
    return trace(Thread.currentThread().getStackTrace(), 2 - level, 1);
  }

  /**
   * Récupère la méthode courante de l'appelant.
   * 
   * @return la méthode courante de l'appelant.
   */
  public static String getCurrentMethod() {
    return trace(Thread.currentThread().getStackTrace(), 2, 2);
  }

  /**
   * Affiche la méthode courante de l'appelant pour une méthode de test.
   *
   * @param endMsg le message terminant le nom de la méthode (normalement " ...")
   */
  public static void printCurrentTestMethod( String endMsg ) {
    System.out.println("\n*** " + trace(Thread.currentThread().getStackTrace(), 2, 2) + endMsg);
  }

  /**
   * Affiche dans la console la méthode courante de l'appelant pour une méthode de test.
   */
  public static void printCurrentTestMethod() {
    System.out.println("\n*** " + trace(Thread.currentThread().getStackTrace(), 2, 2) + " ...");
  }
  
  /**
   * Affiche le résultat d'un test. Se trouve dans cette bibliothèque par convenance.
   * 
   * @param expResult le résultat attendu
   * @param result le résultat trouvé
   */
  public static void printTestResult(Object expResult, Object result) {
    System.out.println("  - expResult: " + expResult);
    System.out.println("  - result:    " + result);
  }
  
  /**
   * Affiche le résultat d'un test. Se trouve dans cette bibliothèque par convenance.
   * 
   * @param source l'objet source fourni au test
   * @param result le résultat trouvé
   */
  public static void printTestInfo(Object source, Object result) {
    System.out.println("  - source: " + source);
    System.out.println("  - result: " + result);
  }

  /**
   * Récupère une méthode parente de -N niveaux au dessus de la méthode courante.
   * 
   * @param level un entier négatif représentant le niveau de parenté
   * @return le nom d'une méthode parente
   */
  public static String getParentMethod( int level ) {
    return trace(Thread.currentThread().getStackTrace(), 2 - level, 2);
  }

  /**
   * Récupère la méthode et la classe courante.
   * @return méthode et classe courante
   */
  public static String getCurrentMethodClass() {
    return trace(Thread.currentThread().getStackTrace(), 2, 3);
  }

  /**
   * Récupère la classe et la méthode currente.
   * @return classe et méthode courante
   */
  public static String getCurrentClassMethod() {
    return trace(Thread.currentThread().getStackTrace(), 2, 4);
  }

  /**
   * Récupère le nom d'une propriété. Utile dans les setters de bean pour récupérer ce nom
   * d'après la méthode appelante.
   *
   * @param level le niveau de recherche : <br> -1 si c'est un appel direct depuis le
   * setter<br> -2 si c'est un appel indirect depuis le setter
   * @return nom de la propriété
   */
  public static String getPropertyName( int level ) {
    String methodName = StackTracer.getParentMethod(level);
    String propName = methodName.substring(3, 4).toLowerCase()
            + methodName.substring(4);
    return propName;
  }
}
