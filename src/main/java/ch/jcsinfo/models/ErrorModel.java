package ch.jcsinfo.models;

/**
 * Modèle pour gérer des messages d'erreur avec le titre qui sera
 * présenté dans une boîte de dialogue (popup).
 *
 * @author Jean-Claude Stritt
 */
public class ErrorModel {
  private int id;
  private boolean ok;
  private String title;
  private String msg;

  public ErrorModel( boolean ok, String title, String msg ) {
    this.ok = ok;
    this.title = title;
    this.msg = msg;
  }

  public ErrorModel( ) {
    this(true, "", "");
  }

  public int getId() {
    return id;
  }

  public void setId( int id ) {
    this.id = id;
  }

  public boolean isOk() {
    return ok;
  }

  public void setOk( boolean ok ) {
    this.ok = ok;
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

  @Override
  public String toString() {
    return "Id: " + getId() + " ok: " + isOk()+" msg: "+getMsg();
  }

}

