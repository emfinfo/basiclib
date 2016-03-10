
package beans;

import lombok.Data;

/**
 *
 * @author jcstritt
 */
@Data
public class PmtMode {
  private int status; // TP LongInt
  private String abrev;  // TP String[6]
  private String designation; // TP String[30]
  private int noCompte; // TP Dec6

  @Override
  public String toString() {
    return abrev + " - " + designation + " compte=" + noCompte;
  }

}



