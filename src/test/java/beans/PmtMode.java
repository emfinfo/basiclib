
package beans;


/**
 *
 * @author jcstritt
 */
public class PmtMode {
  private int status; // TP LongInt
  private String abrev;  // TP String[6]
  private String designation; // TP String[30]
  private int noCompte; // TP Dec6

  @Override
  public String toString() {
    return abrev + " - " + designation + " compte=" + noCompte;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getAbrev() {
    return abrev;
  }

  public void setAbrev(String abrev) {
    this.abrev = abrev;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public int getNoCompte() {
    return noCompte;
  }

  public void setNoCompte(int noCompte) {
    this.noCompte = noCompte;
  }

}



