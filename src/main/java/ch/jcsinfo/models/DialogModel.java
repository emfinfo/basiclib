package ch.jcsinfo.models;

/**
 * Modèle pour stocker les informations d'affichage dans une boîte
 * de dialogue (par exemple une popup) :<br>
 * - message d'info<br>
 * - titre de la boîte de dialogue<br>
 * - texte pour le bouton "oui"<br>
 * - texte pour le bouton "non"
 *
 * @author jcstritt
 */
public class DialogModel {
  private String msg;
  private String title;
  private String yesTxt;
  private String noTxt;

  public DialogModel(String msg, String title, String yesTxt, String noTxt) {
    this.msg = msg;
    this.title = title;
    this.yesTxt = yesTxt;
    this.noTxt = noTxt;
  }

  public DialogModel(String msg, String title) {
    this(msg, title, "yes", "no");
  }

  public String getMsg() {
    return msg;
  }

  public String getTitle() {
    return title;
  }

  public String getYesTxt() {
    return yesTxt;
  }

  public String getNoTxt() {
    return noTxt;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

}
