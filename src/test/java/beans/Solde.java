package beans;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author jcstritt
 */
@Data
public class Solde {
    private BigDecimal montant;
    private boolean auCredit;

  public Solde() {
  }

  public Solde( BigDecimal montant, boolean auCredit ) {
    this.montant = montant;
    this.auCredit = auCredit;
  }

}
