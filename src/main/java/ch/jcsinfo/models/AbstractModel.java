package ch.jcsinfo.models;

import ch.jcsinfo.system.StackTracer;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import javax.swing.Timer;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 * Modèle abstrait à étendre et qui implémente quelques méthodes
 * de base pour gérer un modèle accessible à un contrôleur ou à une vue.
 *
 * @author Jean-Claude Stritt
 */
public class AbstractModel {
  private final SwingPropertyChangeSupport pcs;
  private boolean lock;
  private Timer timer;

  /**
   * Modes possibles lors des opérations CRUD.
   */
  public static enum CRUD {NONE, CREATE, READ, UPDATE, DELETE, DELETE_IMPOSSIBLE};


  /**
   * Constructeur.
   */
  public AbstractModel() {
    pcs = new SwingPropertyChangeSupport(this, true);

    // crée une méthode de déverrouillage de la vue (pour le timer)
    final ActionListener unlockMe = e -> {
      lock = false;
    };

    // créer le timer pour le déverrouillage de la vue
    timer = new Timer(800, unlockMe);
    timer.setRepeats(false);
    lock = true;
  }

  public boolean isLock() {
    return lock;
  }

  public void lock() {
    lock = true;
    if (timer.isRunning()) {
      timer.stop();
    }
  }

  public void unlock() {
    timer.start();
  }


  /**
   * Fixe le support pour la détection des changements dans une propriété Swing.
   */
//  public void setPropertyChangeSupport(SwingPropertyChangeSupport pcs) {
//    this.pcs = pcs;
//  }

  /**
   * Ajoute un écouteur pour "Better Beans Binding" (BBB).
   *
   * @param listener un écouteur à ajouter
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
//    System.out.println("addPropertyChangeListener: "+listener.getClass().getName());
    pcs.addPropertyChangeListener(listener);
  }

  /**
   * Supprime l'écouteur spécifié en paramètre.
   *
   * @param listener un écouteur à supprimer
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
//    System.out.println("removePropertyChangeListener: "+listener.toString());
    pcs.removePropertyChangeListener(listener);
  }

  /**
   * Permet de lancer au besoin un événement de changement sur une propriété.
   *
   * @param oldValue ancienne valeur de la propriété
   * @param newValue nouvelle valeur de la propriété
   */
  public void setValue(Object oldValue, Object newValue) {
    String propName = StackTracer.getPropertyName(-2);
//    System.out.println("propName: "+propName+ " " + oldValue + " " + newValue);
    pcs.firePropertyChange(propName, oldValue, newValue);
  }

  /*
   * Supprime tous les listeners qui écoutent ce modèle.
   */
//  public void removeAllListeners() {
//    PropertyChangeListener[] listeners = pcs.getPropertyChangeListeners();
////    System.out.println("AbstractModel.removeAllListeners: " + listeners.length + " removed");
//    for (int i=0; i<listeners.length-1; i++) {
//      removePropertyChangeListener(listeners[i]);
//    }
//  }

}