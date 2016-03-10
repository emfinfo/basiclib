package ch.jcsinfo.util;

/**
 * Interface pour gérer le patron de conception "callback".
 * 
 * @author jcstritt
 */
public interface CallbackHandler {

  void callbackMethod(Object... objects);
  
}
