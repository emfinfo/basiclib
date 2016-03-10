package ch.jcsinfo.models;

/**
 * Ce genre de bean est nécessaire lors de l'extraction d'un nom de classe
 * contenu dans un fichier de type texte. Exemple de commande pour générer
 * un tel fichier :<br>
 * <pre>
 *   java -verbose:class ... &gt; nom_fichier.txt
 * </pre>
 * Exemple du contenu généré :<br>
 * <pre>
 *  [Loaded org.jdesktop.application.AbstractBean from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/lib/AppFramework.jar]
 *  [Loaded org.jdesktop.application.Application from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/lib/AppFramework.jar]
 *  [Loaded org.jdesktop.application.SingleFrameApplication from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/lib/AppFramework.jar]
 *  [Loaded app.App from file:/B:/Projets/NetBeans/SAF/SAFdemo/dist/SAFdemo.jar]
 *  ...
 * </pre>
 * 
 * @author StrittJC
 */
public class ClassInfo {
  private String className;
  
  public ClassInfo() {
    className = "";
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

}