package ch.jcsinfo.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * Méthodes statiques pour récupérer les informations d'un écran.
 *
 * @author Jean-Claude Stritt
 */
public class ScreenInfo {

  /**
   * Récupère le numéro de l'écran par rapport à un composant affiché. Attention
   * le numéro 0 n'est pas forcément l'écran principal qui est celui dont la valeur
   * x commence à 0.
   *
   * @param comp un composant quelconque (une fenêtre par exemple)
   * @return le numéro 0, 1, ... (ID) de l'écran
   */
  public static int getScreenID( Component comp ) {
    int scrID = 0;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gd = ge.getScreenDevices();
    for (int i = 0; i < gd.length; i++) {
      GraphicsConfiguration gc = gd[i].getDefaultConfiguration();
      Rectangle r = gc.getBounds();
      if (r.contains(comp.getLocation())) {
        scrID = i;
      }
    }
    return scrID;
  }

  /**
   * Retourne un tableau avec la position et la taille des écrans.
   *
   * @return un tableau de Rectangle
   */
  public static Rectangle[] getScreensBounds() {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] devices = ge.getScreenDevices();
    Rectangle r[] = new Rectangle[devices.length];
    int j = 0;
    for (GraphicsDevice gd : devices) {
      GraphicsConfiguration gc = gd.getDefaultConfiguration();
      r[j] = gc.getBounds();
//      System.out.println("x="+r[j].x+", y="+r[j].y + ", w="+r[j].width+ ", h="+r[j].height);
      j++;
    }
   return r;
  }

  /**
   * Récupère la dimension (largeur, hauteur) en px d'un écran spécifié.
   *
   * @param scrID le n° d'écran (0, 1, ...)
   * @return la dimension (largeur, hauteur) en pixels de l'écran spécifié
   */
  public static Dimension getScreenSize( int scrID ) {
    Dimension d = new Dimension(0, 0);
    if (scrID >= 0) {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      DisplayMode mode = ge.getScreenDevices()[scrID].getDisplayMode();
      d.setSize(mode.getWidth(), mode.getHeight());
    }
    return d;
  }

  /**
   * Permet de récupérer la largeur en pixels d'un écran spécifié.
   *
   * @param scrID le n° d'écran (0, 1, ...)
   * @return la largeur en px de l'écran spécifié
   */
  public static int getScreenWidth( int scrID ) {
    Dimension d = getScreenSize(scrID);
    return d.width;
  }

  /**
   * Permet de récupérer la hauteur en pixels d'un écran spécifié.
   *
   * @param scrID le n° d'écran (0, 1, ...)
   * @return la hauteur en px de l'écran spécifié
   */
  public static int getScreenHeight( int scrID ) {
    Dimension d = getScreenSize(scrID);
    return d.height;
  }

  /**
   * Récupère la résolution de l'écran en DPI.
   * 
   * @return un entier représentant la résolution en DPI
   */
  public static int getScreenResolution() {
    return Toolkit.getDefaultToolkit().getScreenResolution();
  }

  /**
   * Récupère une liste de 10 résolutions valides pour l'écran en cours.
   * 
   * @return liste des résolutions
   */
  public static List<String> getScreenResolutions() {
    int res = ScreenInfo.getScreenResolution();
    List<String> l = new ArrayList<>();
    for (float f = 0.5f; f < 3.01f; f += 0.1f) {
      l.add(Math.round(res * f) + " DPI (" + Math.round(f * 100f + 0.499f) + " %)");
    }
    return l;
  }
  
  public static Point getShiftedChildWindowPos(Window parent, Window child) {
//    System.out.println("parent: "+parent.getName()+", child: "+child.getName());
    Point pos = new Point();
    Rectangle r[] = getScreensBounds();
    int screenID = getScreenID(parent);
    int xmax = Math.min(parent.getX()+parent.getWidth()+50, r[screenID].width);
    int ymax = Math.min(parent.getY()+parent.getHeight()+50, r[screenID].height);
    pos.x = xmax - (int)child.getMinimumSize().getWidth();
    pos.y = ymax - (int)child.getMinimumSize().getHeight();
    return pos;
  }

}
