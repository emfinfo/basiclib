package beans;

/**
 *
 * @author jcstritt
 */
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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getMonnaie() {
    return monnaie;
  }

  public void setMonnaie(String monnaie) {
    this.monnaie = monnaie;
  }

  public int getGroupe() {
    return groupe;
  }

  public void setGroupe(int groupe) {
    this.groupe = groupe;
  }

  public int getRang() {
    return rang;
  }

  public void setRang(int rang) {
    this.rang = rang;
  }

  public int getCodeTVA() {
    return codeTVA;
  }

  public void setCodeTVA(int codeTVA) {
    this.codeTVA = codeTVA;
  }

  public boolean isTauxAmbigu() {
    return tauxAmbigu;
  }

  public void setTauxAmbigu(boolean tauxAmbigu) {
    this.tauxAmbigu = tauxAmbigu;
  }

  public int getNbEcriSaisies() {
    return nbEcriSaisies;
  }

  public void setNbEcriSaisies(int nbEcriSaisies) {
    this.nbEcriSaisies = nbEcriSaisies;
  }

  public int getNbEcriComptab() {
    return nbEcriComptab;
  }

  public void setNbEcriComptab(int nbEcriComptab) {
    this.nbEcriComptab = nbEcriComptab;
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

  public void setSoldes(Solde[] soldes) {
    this.soldes = soldes;
  }

  

  @Override
  public String toString() {
    String s = numero + " " + monnaie;
    s += ", groupe: " + groupe;
    s += ", rang: " + rang;
    s += ", code TVA: " + codeTVA;
    s += ", taux amb.: " + tauxAmbigu;
    s += ", saisies:" + nbEcriSaisies;
    s += ", comptab.:" + nbEcriComptab;
    return s;
  }
}
