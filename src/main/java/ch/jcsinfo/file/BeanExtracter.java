package ch.jcsinfo.file;

/**
 * Interface qui spécifie la forme que doit prendre une méthode d'extraction
 * de données lors de la lecture d'un fichier au format texte. S'utilise en
 * conjonction avec la classe "TextFileReader". Voir les exemples d'utilisation
 * dans les classes de test.
 *
 * @author Jean-Claude Stritt
 */
public interface BeanExtracter<E> {

  /**
   * Permet de convertir une ligne de texte en un objet de type E.
   *
   * @param idx index du numéro de ligne lu (commence à 1)
   * @param text une ligne de texte dont il faut extraire un bean
   * 
   * @return un bean de type ClassInfo avec le nom d'une classe
   */
  public E textToBean (int idx, String text);

}
