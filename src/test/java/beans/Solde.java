package beans;

import java.math.BigDecimal;

/**
 *
 * @author jcstritt
 */
public class Solde {
    private BigDecimal montant;
    private boolean auCredit;

  public Solde() {
  }

  public Solde( BigDecimal montant, boolean auCredit ) {
    this.montant = montant;
    this.auCredit = auCredit;
  }

  public BigDecimal getMontant() {
    return montant;
  }

  public void setMontant(BigDecimal montant) {
    this.montant = montant;
  }

  public boolean isAuCredit() {
    return auCredit;
  }

  public void setAuCredit(boolean auCredit) {
    this.auCredit = auCredit;
  }

}
