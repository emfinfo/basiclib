package helpers;

import beans.Departement;
import ch.jcsinfo.file.BeanExtracter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extracteur d'un département à partir d'une ligne de texte.
 *
 * @author Jean-Claude Stritt
 */
public class DepartementExtracter implements BeanExtracter<Departement> {
  private String sep;

  public DepartementExtracter( String sep ) {
    this.sep = sep;
  }

  @Override
  public Departement textToBean(int idx, String text) {
    String[] tab = text.split(sep);
    Departement d = new Departement();
    if (tab.length > 2) {
      d.setDepartement(tab[0]);
      d.setVille(tab[1]);
      SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
      try {
        Date date = sdf.parse(tab[2]);
        d.setDateCreation(date);
      } catch (ParseException ex) {
      }
    }
    return d;
  }
}
