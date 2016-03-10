package ch.jcsinfo.util;

import ch.jcsinfo.models.ErrorModel;
import java.util.HashMap;

/**
 * Manager d'erreurs qui permet d'ajouter plusieurs tests d'erreur
 * dans une hmap et de retourner la première erreur réelle.
 *
 * @author jcstritt
 */
public class ErrorManager {
  HashMap<Integer, ErrorModel> hmap;
  int id = 1;

  public ErrorManager() {
    hmap = new HashMap<>();
  }

  public void addError (ErrorModel errm) {
    errm.setId(id);
    hmap.put(id, errm);
    id++;
  }

 /**
   * Une méthode pour retrouver la première erreur réelle.
   * S'il n'y en a pas, retourne une erreur où ok = true.
   *
   * @return un objet de type ErrorModel
   */
  public ErrorModel getFirstMessage() {
    ErrorModel errm = new ErrorModel();
    errm.setOk(true);
    int i = 1;
    boolean ok = true;
    while (ok && i <= hmap.size()) {
      errm = hmap.get(i);
      ok = errm.isOk();
      i++;
    }
    return errm;
  }
  
 /**
   * Une méthode pour retrouver toutes les erreurs sous la forme
   * d'un seul message.
   *
   * @param errorTitle un titre pour l'erreur
   * @return un objet de type ErrorModel
   */
  public ErrorModel getAllMessages(String errorTitle) {
    ErrorModel errm = new ErrorModel();
    int i = 1;
    boolean ok = true;
    String errorMsg = "";
    while (i <= hmap.size()) {
      errm = hmap.get(i);
      if (!errm.isOk()) {
        if (i > 1) {
          errorMsg += "\n";
        }
        errorMsg += errm.getMsg();
      }
      ok = ok && errm.isOk();
      i++;
    }
    errm.setId(1);
    errm.setOk(ok);
    errm.setMsg(errorMsg);
    errm.setTitle(errorTitle);
    return errm;
  }  

}
