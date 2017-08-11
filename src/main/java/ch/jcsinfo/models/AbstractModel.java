package ch.jcsinfo.models;

import ch.jcsinfo.system.StackTracer;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import javax.swing.Timer;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 * Modèle abstrait (à étendre) et qui implémente quelques méthodes
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
    lock = true;
    pcs = new SwingPropertyChangeSupport(this, true);

    // crée une méthode de déverrouillage de la vue (pour le timer)
    final ActionListener unlockMe = e -> {
      lock = false;
    };

    // créer le timer pour le déverrouillage de la vue
    timer = new Timer(1000, unlockMe);
    timer.setRepeats(false);
  }



  /**
   * Teste si la vue est actuellement verrouillée (pas de traitement d'événements supplémentaires).
   *
   * @return vrai (true) si le verrouillage de la vue est actuellement actif
   */
  public boolean isLock() {
    return lock;
  }

  /**
   * Verrouille la vue pour une opération qui doit être prioritaire (aucun autre événement
   * ne sera traité pendant que le verrouillage est actif).
   */
  public void lock() {
    lock = true;
    if (timer.isRunning()) {
      timer.stop();
    }
  }

  /**
   * Déverrouille le traitement des événements d'une vue. Un laps de temps (800ms) est alloué
   * avant que le verrou ne soit réellement supprimé. Cela laisse un peu de temps à d'éventuels
   * composants (ComboBox par exemple) de se remettre à jour avant le traitement d'un
   * prochain événement.
   */
  public void unlock() {
    timer.start();
  }



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