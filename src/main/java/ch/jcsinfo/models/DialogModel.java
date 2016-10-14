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
  private String info;
  private String title;
  private String yesTxt;
  private String noTxt;

  public DialogModel(String title, String info, String yesTxt, String noTxt) {
    this.info = info;
    this.title = title;
    this.yesTxt = yesTxt;
    this.noTxt = noTxt;
  }

  public DialogModel(String title, String info) {
    this(title, info, "yes", "no");
  }

  public String getTitle() {
    return title;
  }

  public String getInfo() {
    return info;
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

  public void setInfo(String info) {
    this.info = info;
  }

}
