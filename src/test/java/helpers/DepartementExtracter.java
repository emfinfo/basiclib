package helpers;

import beans.Departement;
import ch.jcsinfo.file.TextFileExtracter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Jean-Claude Stritt
 * @version 1.0 / 11-OCT-2009
 */
public class DepartementExtracter implements TextFileExtracter<Departement> {
  private String sep;

  public DepartementExtracter( String sep ) {
    this.sep = sep;
  }

  @Override
  public Departement extract( String line, int lineNbr ) {
    String[] tab = line.split(sep);
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
