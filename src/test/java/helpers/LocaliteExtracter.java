package helpers;

import beans.Localite;
import ch.jcsinfo.file.BeanExtracter;

/**
 * Extracteur d'une localite à partir d'une ligne de texte.
 *
 * @author Jean-Claude Stritt
 */
public class LocaliteExtracter implements BeanExtracter<Localite> {

  private String sep;

  public LocaliteExtracter(String sep) {
    this.sep = sep;
  }

  @Override
  public Localite textToBean(String text) {
    String[] tab = text.split(sep);
    Localite l = new Localite();
    if (tab.length > 6) {
      l.setNpa(Integer.parseInt(tab[2]));
      l.setPkLoc(l.getNpa() * 100 + Integer.parseInt(tab[3]));
      l.setLocalite(tab[4]);
      l.setCanton(tab[6]);
    }
    return l;
  }

}
