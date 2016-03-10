package ch.jcsinfo.printing;

import javax.print.DocPrintJob;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

/**
 * Classe pour gérer le déroulement d'un job d'impression.
 *
 * @author jcstritt
 */
public class PrintJobWatcher {
  private boolean done = false;

  /**
   * Constructeur.
   *
   * @param job un travail d'impression de type DocPrintJob
   */
  public PrintJobWatcher( DocPrintJob job ) {
    job.addPrintJobListener(new PrintJobAdapter() {
      @Override
      public void printJobCanceled( PrintJobEvent pje ) {
        allDone();
      }

      @Override
      public void printJobCompleted( PrintJobEvent pje ) {
        allDone();
      }

      @Override
      public void printJobFailed( PrintJobEvent pje ) {
        allDone();
      }

      @Override
      public void printJobNoMoreEvents( PrintJobEvent pje ) {
        allDone();
      }

      void allDone() {
        synchronized (PrintJobWatcher.this) {
          PrintJobWatcher.this.notify();
          done = true;
          // System.out.println("Printing done ...");
        }
      }
    });
  }

  /**
   * Attend que le job d'impression soit terminé.
   */
  public synchronized void waitForDone() {
    try {
      while (!done) {
        wait();
      }
    } catch (InterruptedException e) {
    }
  }
}
