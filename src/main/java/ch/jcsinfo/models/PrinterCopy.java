package ch.jcsinfo.models;

/**
 * Modèle pour mémoriser les informations nécessaires à la gestion des
 * options de copie sur une imprimante.
 *
 * @author Jean-Claude Stritt
 */
public class PrinterCopy {
  protected int id;
  protected String printerName;
  protected Integer tray;
  protected String text;
  protected Integer nb;

  public PrinterCopy (int id, String printerName, int tray, String text, int nb){
    this.id = id;
    this.printerName = printerName;
    this.tray = tray;
    this.text = text;
    this.nb = nb;
  }

  public int getId() {
    return id;
  }

  public void setId( int id ) {
    this.id = id;
  }

  public String getPrinterName() {
    return printerName;
  }

  public void setPrinterName( String printerName ) {
    this.printerName = printerName;
  }

  public int getTray() {
    return tray;
  }

  public void setTray( int tray ) {
    this.tray = tray;
  }

  public String getText() {
    return text;
  }

  public void setText( String text ) {
    this.text = text;
  }

  public int getNb() {
    return nb;
  }

  public void setNb( int nbOfCopies ) {
    this.nb = nbOfCopies;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 13 * hash + this.id;
    return hash;
  }

  @Override
  public boolean equals( Object obj ) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PrinterCopy other = (PrinterCopy) obj;
    return this.id == other.id;
  }

  @Override
  public String toString() {
    return getText();
  }

}
