package beans;

import lombok.Data;

/**
 *
 * @author jcstritt
 */
@Data
public class Compte {

  private int status;
  private int numero;
  private String designation;
  private String monnaie;
  private int groupe;
  private int rang;
  private int codeTVA;
  private boolean tauxAmbigu;
  private int nbEcriSaisies;
  private int nbEcriComptab;
  private Solde[] soldes;

  public Compte() {
    soldes = new Solde[6];
  }

  @Override
  public String toString() {
    String s = numero + " " + monnaie + " - " + designation + " - " + groupe + " - ";
    s += " rang: " + rang;
    s += " code TVA: " + codeTVA;
    s += " taux amb.: " + tauxAmbigu;
    s += " saisies:" + nbEcriSaisies;
    s += " comptab.:" + nbEcriComptab;
    return s;
  }

  public String getSoldes() {
    String s = "";
    for (int i = 0; i < soldes.length; i++) {
      Solde solde = soldes[i];
      s += "         s[" + i + "] = " + solde.getMontant() + " " + solde.isAuCredit();
      if (i<(soldes.length-1)) {
        s += "\n";
      }
    }
    return s;
  }
}
