package ch.jcsinfo.file;

import ch.jcsinfo.models.ClassInfo;


/**
 * Implémentation de l'interface TextFileExtracter. Elle contient une méthode
 * "extract" qui permet d'extraire d'une chaîne de caractères passée en
 * paramètre, un bean de type "ClassInfo". Exemple de lignes de texte où
 * l'information à extraire se trouve en 2ème position :<br>
 * <pre>
 *  [Loaded org.jdesktop.application.AbstractBean from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/lib/AppFramework.jar]
 *  [Loaded org.jdesktop.application.Application from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/lib/AppFramework.jar]
 *  [Loaded org.jdesktop.application.SingleFrameApplication from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/lib/AppFramework.jar]
 *  [Loaded app.App from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/SAFdemo.jar]
 *  ...
 * </pre>
 * Ce genre d'extraction est nécessaire, par exemple,  lors de la lecture d'un
 * fichier de type texte généré avec la commande :<br>
 * <pre>
 *   java -verbose:class ...  &gt; nom_fichier.txt
 * </pre>
 *
 * @author Jean-Claude Stritt
 */
public class ClassInfoExtracter implements TextFileExtracter<ClassInfo> {

  private String sep;

  /**
   * Constructeur qui permet par son paramètre de passer le caractère "séparateur"
   * des informations.
   *
   * @param sep le caractère "séparateur"
   */
  public ClassInfoExtracter(String sep) {
    this.sep = sep;
  }

  /**
   * Implémentation de la méthode d'extraction d'un bean de type "ClassInfo"
   * depuis une ligne de texte passée en paramètre. L'information recherchée
   * se trouve en 2ème position.
   *
   * @param line ligne de texte à traiter
   * @return un bean de type ClassInfo avec le nom d'une classe
   */
  @Override
  public ClassInfo extract(String line, int lineNbr) {
    String[] tab = line.split(sep);
    ClassInfo ci = new ClassInfo();
    if (tab.length > 3) {
      ci.setClassName(tab[1]);
    }
    return ci;
  }
}
