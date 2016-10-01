package ch.jcsinfo.util;

import ch.jcsinfo.datetime.DateTimeLib;
import ch.jcsinfo.math.MathLib;

/**
 * Classe abstraite de type "adapter" pour implémenter les méthodes
 * de l'interface "CallbackHandler".
 * 
 * @author Jean-Claude Stritt
 */
public abstract class CallbackAdapter implements CallbackHandler {
  protected String title = "";
  protected int size = 1;
  protected int step = 1;
  
  private int computeStep(int nb)  {
//    int cStep = Math.max((int)MathLib.roundValue((float)nb / 10, 1f), 1);
//    System.out.println("CallbackAdapter: size="+nb + ", step="+cStep);
//    return cStep;
    return Math.max((int)MathLib.roundFloatValue((float)nb / 10, 1f), 1);
  }

  public void setTitle(String title) {
    this.title = title;
    DateTimeLib.chronoReset();
  }

  public void setSize(int size) {
    this.size = size;
    step = computeStep(size);
    DateTimeLib.chronoReset();
  }

  public void setInfo(String title, int size) {
    this.title = title;
    this.size = size;
    step = computeStep(size);
    DateTimeLib.chronoReset();
  }

  @Override
  public void callbackMethod(Object... objects) {
  }

}
