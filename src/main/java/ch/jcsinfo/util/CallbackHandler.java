package ch.jcsinfo.util;

/**
 * Interface pour gérer le patron de conception "callback".
 * 
 * @author Jean-Claude Stritt
 */
public interface CallbackHandler {

  void callbackMethod(Object... objects);
  
}
