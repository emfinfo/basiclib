package ch.jcsinfo.models;

/**
 * Modèle pour stocker les informations d'affichage d'une message
 * dans une popup (titre, message d'info, texte poour le bouton "oui"
 * et texte pour le bouton "non").
 *
 * @author jcstritt
 */
public class InfoModel {
  private String title;
  private String info;
  private String yes;
  private String no;

  public InfoModel(String title, String info, String yes, String no) {
    this.title = title;
    this.info = info;
    this.yes = yes;
    this.no = no;
  }

  public InfoModel(String title, String info) {
    this(title, info, "yes", "no");
  }

  public String getTitle() {
    return title;
  }

  public String getInfo() {
    return info;
  }

  public String getYes() {
    return yes;
  }

  public String getNo() {
    return no;
  }

}
