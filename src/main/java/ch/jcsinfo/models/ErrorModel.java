package ch.jcsinfo.models;

/**
 * Modèle pour gérer des messages d'erreur avec le titre qui sera
 * présenté dans une boîte de dialogue (popup).
 *
 * @author Jean-Claude Stritt
 */
public class ErrorModel {
  private int id;
  private String msg;
  private String title;
  private boolean ok;

  public ErrorModel( String msg, String title, boolean ok) {
    this.msg = msg;
    this.title = title;
    this.ok = ok;
  }

  public ErrorModel( ) {
    this("", "", true);
  }

  public int getId() {
    return id;
  }

  public void setId( int id ) {
    this.id = id;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg( String msg ) {
    this.msg = msg;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle( String title ) {
    this.title = title;
  }

  public boolean isOk() {
    return ok;
  }

  public void setOk( boolean ok ) {
    this.ok = ok;
  }

  @Override
  public String toString() {
    return "Id: " + getId() + ", ok: " + isOk()+", msg: "+getMsg();
  }

}

