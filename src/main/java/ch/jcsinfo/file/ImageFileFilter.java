package ch.jcsinfo.file;

import java.io.File;
import java.io.FileFilter;

/**
 * Une classe de filtre pour un JFileChooser.
 *
 * @author StrittJC
 */
public class ImageFileFilter implements FileFilter {

    private final String[] okFileExtensions =
      new String[]{"jpg", "png"};

    /**
     * Surcharge de la méthode qui accepte un fichier ou pas
     * suivant le filtre.
     *
     * @param file le fichier testé
     * @return TRUE si le fichier est accepté d'après le filtre
     */
    @Override
    public boolean accept(File file) {
      for (String extension : okFileExtensions) {
        if (file.isFile() && file.getName().toLowerCase().endsWith(extension)) {
          return true;
        }
      }
      return false;
    }
  }

