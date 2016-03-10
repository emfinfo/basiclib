package ch.jcsinfo.file;

/**
 * Interface qui spécifie la forme que doit prendre une méthode d'extraction
 * de données lors de la lecture d'un fichier au format texte.
 *
 * @author Jean-Claude Stritt
 */
public interface TextFileExtracter<E> {

  public E extract (String line, int lineNbr);

}
