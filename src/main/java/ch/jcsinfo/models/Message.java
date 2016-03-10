package ch.jcsinfo.models;

import java.io.Serializable;

/**
 * Modèle pour stocker des messages avec un identificateur.
 *
 * @author Jean-Claude Stritt
 */
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private Integer id;
  private String msg;

  public Message( int id, String msg) {
    this.id = id;
    this.msg = msg;
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

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Message other = (Message) obj;
    return !(this.id == null || !this.id.equals(other.id));
  }

  @Override
  public String toString() {
    return msg;
  }

}

