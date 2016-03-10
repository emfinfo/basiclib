package beans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Jean-Claude Stritt
 */
public class Departement implements Serializable {
   private int pkDep;
   private String departement;
   private String ville;
   private Date dateCreation;


   public int getPkDep() {
      return pkDep;
   }

   public void setPkDep(int pkDep) {
      this.pkDep = pkDep;
   }

  public String getDepartement() {
      return departement;
   }

   public void setDepartement(String departement) {
      this.departement = departement;
   }

  public String getVille() {
      return ville;
   }

   public void setVille(String ville) {
      this.ville = ville;
   }

   public Date getDateCreation() {
      return dateCreation;
   }

   public void setDateCreation(Date dateCreation) {
      this.dateCreation = dateCreation;
   }

  @Override
  public boolean equals( Object obj ) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Departement other = (Departement) obj;
    if (this.pkDep != other.pkDep) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + this.pkDep;
    return hash;
  }

  @Override
  public String toString() {
    return getPkDep() + ". " + getDepartement() + " " + getVille() + " " +
      getDateCreation();
  }

}
