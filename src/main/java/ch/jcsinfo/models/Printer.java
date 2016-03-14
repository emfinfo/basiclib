package ch.jcsinfo.models;

import javax.print.PrintService;

/**
 * Modèle pour mémoriser l'état d'une imprimante.
 *
 * @author Jean-Claude Stritt
 */
public class Printer {
  private PrintService printService;

  public Printer( PrintService printService ) {
    this.printService = printService;
  }

  public PrintService getPrintService() {
    return printService;
  }

  public void setPrintService( PrintService printService ) {
    this.printService = printService;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 71 * hash + (this.printService != null ? this.printService.hashCode() : 0);
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
    final Printer other = (Printer) obj;
    return this.printService == other.printService || (this.printService != null && this.printService.equals(other.printService));
  }

  @Override
  public String toString() {
    return printService.getName();
  }

}
