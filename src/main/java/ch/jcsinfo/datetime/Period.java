package ch.jcsinfo.datetime;

import java.util.Date;

/**
 * Permet d'encapsuler toutes les données d'une période.<br> 
 * Utile par exemple pour gérer des absences.
 *
 * @author jcstritt
 */
public class Period {

  private Date startDate;
  private int startHour;
  private Date endDate;
  private int endHour;

  public Period(Date startDate, int startHour, Date endDate, int endHour) {
    this.startDate = startDate;
    this.startHour = startHour;
    this.endDate = endDate;
    this.endHour = endHour;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public int getStartHour() {
    return startHour;
  }

  public void setStartHour(int startHour) {
    this.startHour = startHour;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public int getEndHour() {
    return endHour;
  }

  public void setEndHour(int endHour) {
    this.endHour = endHour;
  }

}
