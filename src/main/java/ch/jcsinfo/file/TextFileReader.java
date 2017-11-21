package ch.jcsinfo.file;

import ch.jcsinfo.system.StackTracer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Permet de lire un fichier texte pour en extraire une liste de beans.
 *
 * @author Jean-Claude Stritt
 * @param <E> le type de classe-entité à extraire
 */
public class TextFileReader<E> {
  private BeanExtracter<E> extracter;
  private Logger logger;

  /**
   * Constructeur.
   *
   * @param extracter l'extracteur de bean à appliquer à chaque ligne de données
   */
  public TextFileReader(BeanExtracter<E> extracter) {
    this.extracter = extracter;
    this.logger = LoggerFactory.getLogger(getClass());
  }

  /**
   * Méthode privée pour lire les lignes de texte du fichier avec extraction
   * dans une liste de beans.
   *
   * @param br un reader (canal de lecture) ouvert sur un fichier texte
   * @return une liste de bean "E" extraits avec l'objet "extracter"
   * @throws IOException ce type d'erreur est remonté un niveau en dessus
   */
  private List<E> readData(BufferedReader br) throws IOException {
    List<E> ar = null;
    String line;
    if (br != null) {
      ar = new ArrayList<>();

      // boucle sur toutes les lignes du fichier
      int idx = 0;
      while ((line = br.readLine()) != null) {
        idx++;
        E e = extracter.textToBean(idx, line);
        if (e != null) {
          ar.add(e);
        }
      }
    }
    return ar;
  }

  /**
   * Lecture du fichier avec extraction des données.
   *
   * @param fileName un nom de fichier avec son chemin
   * @param csName   un nom de "charset" (exemple: "Windows-1250")
   * @return une liste de bean "E" extraits avec l'objet "extracter"
   */
  public List<E> read(String fileName, String csName) {
    List<E> ar = null;
    BufferedReader br = null;
    try {

      // sélection d'un charset pour lire correctement le fichier
      Charset cs = Charset.forName(csName);

      // ouverture du fichier
      br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), cs));

      // lecture des données
      ar = readData(br);

    } catch (IOException ex) {
      logger.error("{} « {} »", StackTracer.getCurrentMethod(), ex.getMessage());
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException ex) {
        }
      }
    }
    return ar;
  }

  /**
   * Version simplifiée de la méthode précédente. La lecture se fait automatiquement en "UTF-8".
   *
   * @param fileName un nom de fichier avec son chemin
   * @return une liste de bean "E" extraits avec l'objet "extracter"
   */
  public List<E> read(String fileName) {
    return TextFileReader.this.read(fileName, "UTF-8");
  }

  /**
   * Cette implémentation pemet en entrée un objet de type "InputStream".
   *
   * @param inputStream un objet "fichier" de type "InputStream"
   * @return une liste de bean "E" extraits avec l'objet "extracter"
   */
  public List<E> read(InputStream inputStream) {
    List<E> ar = null;
    BufferedReader br = null;
    try {
      // transformation en BufferedReader
      br = new BufferedReader(new InputStreamReader(inputStream));

      // lecture des données
      ar = readData(br);

    } catch (IOException ex) {
      logger.error("{} « {} »", StackTracer.getCurrentMethod(), ex.getMessage());
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException ex) {
        }
      }
    }
    return ar;
  }

}
