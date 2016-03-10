package ch.jcsinfo.models;

import java.util.Observable;

/**
 * Classe de type singleton qui permet de créer un sujet observable par
 * la modification d'un état interne (entier "state").
 *
 * @author jcstritt
 */
public class ObservableModel extends Observable {
  private static ObservableModel instance = null;
  private int state;
  private Object[] objects;
  
  private ObservableModel() {
    objects = new Object[2];
  }

  public synchronized static ObservableModel getInstance() {
    if (instance == null) {
      instance = new ObservableModel();
    }
    return instance;
  }  
  
  public int getState() {
    return state;
  }

  public Object[] getObjects() {
    return objects;
  }
  
  public Object getFirstObject() {
    return objects[0];
  }  
  
  public Object getSecondObject() {
    return objects[1];
  }   

  public void setState(int state, Object...objects) {
    this.state = state;
//    System.out.println("ObservableModel: setting state to " + state);
    switch (objects.length) {
      case 2:
        this.objects[0] = objects[0];
        this.objects[1] = objects[1];
        break;
      case 1:
        this.objects[0] = objects[0];
        this.objects[1] = null;
        break;
      default:
        this.objects[0] = null;
        this.objects[1] = null;
        break;
    }
    setChanged();
    notifyObservers();
  }

}
