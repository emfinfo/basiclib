package ch.jcsinfo.models;

/**
 * Modèle pour stocker une clé et une valeur "string" associé (message).
 * Pratique pour simuler le contenu d'une table simple de base de données
 * avec une clé primaire et une valeur "string". Ces données proviendraient
 * donc plutôt d'un fichier de resources liée à la langue.<br>
 * Par exemple : <br>
 * 0   tri par code article<br>
 * 1   tri par nom d'article<br>
 *
 * @author Jean-Claude Stritt
 */
public class Option {
  private Integer key;
  private String value;

  public Option( int key, String value) {
    this.key = key;
    this.value = value;
  }

  public int getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "[" + key + "]=" + value;
  }

}

