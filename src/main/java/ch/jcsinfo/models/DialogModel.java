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
  private String info;
  private String title;
  private String yesTxt;
  private String noTxt;

  public DialogModel(String info, String title, String yesTxt, String noTxt) {
    this.info = info;
    this.title = title;
    this.yesTxt = yesTxt;
    this.noTxt = noTxt;
  }

  public DialogModel(String info, String title) {
    this(info, title, "yes", "no");
  }

  public String getInfo() {
    return info;
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

}
