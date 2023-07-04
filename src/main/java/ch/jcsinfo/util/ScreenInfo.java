package ch.jcsinfo.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
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
  
  /**
   * Retourne la largeur d'un texte (en pt) pour une police fournie.
   * 
   * @param text le texte dont on veut connaitre la largeur en [pt]
   * @param font une police fournie pour calculer la largeur
   * @return la largeur en [pt]
   */
  public static int getTextWidth(String text, Font font) {
    // Create a temporary image with a Graphics object
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();

    // Set font and rendering hints
    Graphics2D g2d = (Graphics2D) g;
    g2d.setFont(font);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Get the font metrics for the given text and font
    FontMetrics fontMetrics = g2d.getFontMetrics();
    int width = fontMetrics.stringWidth(text);

    // Dispose the graphics object and image
    g.dispose();
    image.flush();

    return width;
  }  
  
  /**
   * Retourne la largeur d'un texte en [mm] pour une police fournie.
   * 
   * @param text le texte dont on veut connaitre la largeur en [mm]
   * @param font une police fournie pour calculer la largeur
   * @return la largeur en [mm]
   */
  public static double getTextWidthMm (String text, Font font) {
    int pt = getTextWidth(text, font);
    return 25.4 / 72 * pt;
  }

}
