package beans;

import java.io.Serializable;

/**
 *
 * @author StrittJC
 */
public class Localite implements Serializable {
  private static final long serialVersionUID = 1L;
  private long pkLoc;
  private int npa;
  private String localite;
  private String canton;

  public Localite() {
  }

  public Localite(long pkLoc, int npa, String localite, String canton) {
    this.pkLoc = pkLoc;
    this.npa = npa;
    this.localite = localite;
    this.canton = canton;
  }

  public long getPkLoc() {
    return pkLoc;
  }

  public void setPkLoc( long pkLoc ) {
    this.pkLoc = pkLoc;
  }

  public int getNpa() {
    return npa;
  }

  public void setNpa( int npa ) {
    this.npa = npa;
  }

  public String getLocalite() {
    return localite;
  }

  public void setLocalite( String localite ) {
    this.localite = localite;
  }

  public String getCanton() {
    return canton;
  }

  public void setCanton( String canton ) {
    this.canton = canton;
  }

  @Override
  public boolean equals( Object obj ) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Localite other = (Localite) obj;
    if (this.pkLoc != other.pkLoc) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + (int) (this.pkLoc ^ (this.pkLoc >>> 32));
    return hash;
  }

  @Override
  public String toString() {
    return getPkLoc() + " " + getNpa() + " " + getLocalite() + " (" + getCanton() +
      ")";
  }
}

