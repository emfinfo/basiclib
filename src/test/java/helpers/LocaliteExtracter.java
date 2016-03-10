package helpers;

import beans.Localite;
import ch.jcsinfo.file.TextFileExtracter;
/**
 *
 * @author Jean-Claude Stritt
 * @version 1.0 / 11-OCT-2009
 */
public class LocaliteExtracter implements TextFileExtracter<Localite> {
  private String sep;

  public LocaliteExtracter( String sep ) {
    this.sep = sep;
  }

  @Override
   public Localite extract (String line, int lineNbr) {
     String[] tab = line.split(sep);
     Localite l = new Localite();
     if (tab.length>6) {
       l.setNpa(Integer.parseInt(tab[2]));
       l.setPkLoc(l.getNpa()*100 + Integer.parseInt(tab[3]));
       l.setLocalite(tab[4]);
       l.setCanton(tab[6]);
     }
     return l;
   }

}
