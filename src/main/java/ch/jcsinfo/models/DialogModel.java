package ch.jcsinfo.models;

/**
 * Modèle pour stocker les informations d'affichage dans une boîte
 * de dialogue (par exemple une popup) :<br>
 * - titre de la boîte de dialogue<br>
 * - message d'info<br>
 * - texte pour le bouton "oui"<br>
 * - texte pour le bouton "non"
 *
 * @author jcstritt
 */
public class DialogModel {
  private String title;
  private String msg;
  private String yesTxt;
  private String noTxt;

  public DialogModel(String title, String msg, String yesTxt, String noTxt) {
    this.title = title;
    this.msg = msg;
    this.yesTxt = yesTxt;
    this.noTxt = noTxt;
  }

  public DialogModel(String title, String msg) {
    this(title, msg, "yes", "no");
  }

  public String getTitle() {
    return title;
  }

  public String getMsg() {
    return msg;
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
