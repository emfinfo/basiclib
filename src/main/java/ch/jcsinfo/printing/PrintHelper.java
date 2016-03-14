package ch.jcsinfo.printing;

import ch.jcsinfo.math.MathLib;
import ch.jcsinfo.models.Printer;
import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;

/**
 * Classe qui offre quelques méthodes pour gérer la gestion de l'impression sur une
 * imprimante.
 *
 * @author Jean-Claude Stritt
 */
public class PrintHelper {
  public static final double A4_WIDTH = 21.0; // cm
  public static final double A4_HEIGHT = 29.7; // cm
  public static final int A4_WIDTH_PT = MathLib.cmToPt(A4_WIDTH);
  public static final int A4_HEIGHT_PT = MathLib.cmToPt(A4_HEIGHT);
  public static final Paper DINA4;
  public static final Paper DINA5;

  /**
   * Crée un format de papier au standard A4
   */
  static {
    Paper paper = new Paper();
    paper.setSize(A4_WIDTH_PT, A4_HEIGHT_PT);
    paper.setImageableArea(0, 0, A4_WIDTH_PT, A4_HEIGHT_PT);
    DINA4 = paper;
  }

  /**
   * Crée un format de papier au standard A5
   */
  static {
    Paper paper = new Paper();
    paper.setSize(A4_WIDTH_PT, A4_HEIGHT_PT / 2);
    paper.setImageableArea(0, 0, A4_WIDTH_PT, A4_HEIGHT_PT);
    DINA5 = paper;
  }

  /**
   * Retourne un tableau de toutes les imprimantes installées.
   * 
   * @return un tableau des imprimantes installées
   */
  public static Printer[] getArrayOfPrinters() {
    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
    Printer printers[] = new Printer[services.length];
    for (int i = 0; i < services.length; i++) {
      printers[i] = new Printer(services[i]);
//      System.out.println("PRINTER: "+printers[i].getPrintService().getName());
    }
    return printers;
  }
  
  /**
   * Retourne une liste de toutes les imprimantes.
   * 
   * @return une liste des imprimantes installées
   */
  public static List<Printer> getListOfPrinters() {
    return new ArrayList<>(Arrays.asList(getArrayOfPrinters()));
  }  

  /**
   * Retourne un tableau avec les noms de chaque imprimante dans le système.
   * 
   * @return un tableau avec les noms d'imprimante
   */
  public static String[] getArrayOfPrinterNames() {
    PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
    String printers[] = new String[services.length];
    for (int i = 0; i < services.length; i++) {
      printers[i] = services[i].getName();
//      System.out.println("PRINTER: " + printers[i]);
    }
    return printers;
  }  
  
  /**
   * Retourne une liste des noms d'imprimantes dans le système.
   * 
   * @return Une liste des noms d'imprimantes
   */
  public static List<String> getListOfPrinterNames() {
    return new ArrayList<>(Arrays.asList(getArrayOfPrinterNames()));
  }
  
  
  /**
   * Trouve le gestionnaire de l'imprimante par défaut.
   * 
   * @return un objet de type PrintService (gestionnaire d'impression)
   */
  public static PrintService findDefaultPrintService() {
    return PrintServiceLookup.lookupDefaultPrintService();
  }

  /**
   * Trouve un gestionnaire d'imprimante en spécifiant une partie de son nom.
   *
   * @param printerName le nom de l'imprimante à rechercher (peut être null)
   * @return le gestionnaire d'imprimante trouvé (ou null)
   */
  public static PrintService findPrintService( String printerName ) {
    PrintService printService = null;
    if (printerName == null || printerName.isEmpty()) {
      printService = PrintServiceLookup.lookupDefaultPrintService();
    } else {
      PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
      for (PrintService service : services) {
        String prtName = service.getName();
        if (prtName.contains(printerName)) {
          printService = service;
          break;
        }
      }
    }
    return printService;
  }

  
  /**
   * Trouve un set d'attributs d'une imprimante spécifiée par son service disponible.
   *
   * @param printer un service d'imprimante
   * @return un set d'attributs
   */
  @SuppressWarnings("unchecked")
  public static Set<Attribute> getPrinterAttributes( PrintService printer ) {
    Set<Attribute> set = new LinkedHashSet<>();

    //get the supported docflavors, categories and attributes
    Class<? extends Attribute>[] categories = (Class<? extends Attribute>[]) printer.getSupportedAttributeCategories();
    DocFlavor[] flavors = printer.getSupportedDocFlavors();
    AttributeSet attributes = printer.getAttributes();

    //get all the avaliable attributes
    for (Class<? extends Attribute> category : categories) {
      for (DocFlavor flavor : flavors) {
        //get the value
        Object value = printer.getSupportedAttributeValues(category, flavor, attributes);

        //check if it's something
        if (value != null) {
          //if it's a SINGLE attribute...
          if (value instanceof Attribute) {
            set.add((Attribute) value); //...then add it
          } //if it's a SET of attributes...
          else if (value instanceof Attribute[]) {
            set.addAll(Arrays.asList((Attribute[]) value)); //...then add its childs
          }
        }
      }
    }
    return set;
  }

  /**
   * Trouve un set d'attributs d'une imprimante spécifiée par son nom (même partiel).
   *
   * @param printerName un nom d'imprimante
   * @return un set d'attributs
   */
  public static Set<Attribute> getPrinterAttributes( String printerName ) {
    PrintService ps = findPrintService(printerName);
    return getPrinterAttributes(ps);
  }

  /**
   * Retourne une carte (HashMap) des propriétés de job d'impression supportées par une
   * imprimante spécifiée sous la forme d'un "PrintService".
   *
   * @param ps un objet printService d'une imprimante
   * @return un objet de type HashMap avec toutes les propriétés d'un job d'impression
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> getPrinterJobProperties( PrintService ps ) {
    Map<String, Object> map = new HashMap<>();
    if (ps != null) {
      Class<? extends Attribute>[] supportedAttributes = (Class<? extends Attribute>[]) ps.getSupportedAttributeCategories();
      for (int i = 0, max = supportedAttributes.length; i < max; i++) {
        Class<? extends Attribute> attr = supportedAttributes[i];
        map.put(supportedAttributes[i].getSimpleName(), ps.getDefaultAttributeValue(attr));
      }
    }
    return map;
  }

  /**
   * Retourne une carte (HashMap) des propriétés de job d'impression supportées par une
   * imprimante spécifiée par son nom (même partiel).
   *
   * @param printerName un nom d'imprimante
   * @return un objet de type HashMap avec toutes les propriétés d'un job d'impression
   */
  public static Map<String, Object> getPrinterJobProperties( String printerName ) {
    PrintService ps = findPrintService(printerName);
    return getPrinterJobProperties(ps);
  }

  /**
   * Récupère une liste de tous les formats de papier supportés par un service
   * d'imprimante.
   *
   * @param ps un objet PrintService
   * @return la liste recherchée
   */
  public static List<Media> getPaperFormats( PrintService ps ) {
    Media[] medias = (Media[]) ps.getSupportedAttributeValues(Media.class, null, null);
    List<Media> list = new ArrayList<>();
    for (Media media : medias) {
      if (media instanceof MediaSizeName) {
        list.add(media);
      }
    }
    return list;
  }

  /**
   * Récupère une liste de tous les formats de papier supportés par une imprimante.
   *
   * @param printerName un nom d'imprimante (même partiel)
   * @return la liste recherchée
   */
  public static List<Media> getPaperFormats( String printerName ) {
    PrintService ps = findPrintService(printerName);
    return getPaperFormats(ps);
  }

  /**
   * Récupère un tableau de tous les formats de papier supportés par un service
   * d'imprimante.
   *
   * @param ps un objet PrintService
   * @return le tableau recherché
   */
  public static Media[] getPaperFormatsArray( PrintService ps ) {
    List<Media> list = getPaperFormats(ps);
    return Arrays.copyOf(list.toArray(), list.size(), Media[].class);
  }

  /**
   * Récupère un tableau de tous les formats de papier supportés par une imprimante.
   *
   * @param printerName un nom d'imprimante (même partiel)
   * @return le tableau recherché
   */
  public static Media[] getPaperFormatsArray( String printerName ) {
    PrintService ps = findPrintService(printerName);
    return getPaperFormatsArray(ps);
  }

  /**
   * Récupère une liste de tous les bacs supportés par un service d'imprimante.
   *
   * @param ps un objet PrintService
   * @return la liste recherchée
   */
  public static List<Media> getPaperTrays( PrintService ps ) {
    Media[] medias = (Media[]) ps.getSupportedAttributeValues(Media.class, null, null);
    List<Media> list = new ArrayList<>();
    for (Media media : medias) {
      if (media instanceof MediaTray) {
        list.add(media);
      }
    }
    return list;
  }

  /**
   * Récupère une liste de tous les bacs supportés par une imprimante.
   *
   * @param printerName nom d'imprimante (même partiel)
   * @return la liste recherchée
   */
  public static List<Media> getPaperTrays( String printerName ) {
    PrintService ps = findPrintService(printerName);
    return getPaperTrays(ps);
  }

  /**
   * Récupère un tableau de tous les bacs supportés par un service d'imprimante.
   *
   * @param ps un objet PrintService
   * @return la liste recherchée
   */
  public static Media[] getPaperTraysArray( PrintService ps ) {
    List<Media> list = getPaperTrays(ps);
    return Arrays.copyOf(list.toArray(), list.size(), Media[].class);
  }

  /**
   * Récupère un tableau de tous les bacs supportés par une imprimante.
   *
   * @param printerName nom d'imprimante (même partiel)
   * @return la liste recherchée
   */
  public static Media[] getPaperTraysArray( String printerName ) {
    PrintService ps = findPrintService(printerName);
    return getPaperTraysArray(ps);
  }

  /**
   * Prépare un set d'attributs pour une requête d'impression. Attention,
   * si "sizeName" est différent de null, cette information surclasse
   * l'information du bac (tray).
   *
   * @param ps un service d'impression lié à une imprimante
   * @param tray le no du bac (1, 2, 3 ...)
   * @param sizeName la grandeur de la page (ex: MediaSizeName.ISO_A4)
   * @param portrait le mode portrait ou paysage
   * @param pages la plage des pages à imprimer (null ou "" = tout)
   * @param copies le nb de copies (&gt; 0, autrement la valeur par défaut)
   *
   * @return un set d'attributs
   */
  public static PrintRequestAttributeSet buildPrintRequestAttributeSet( PrintService ps,
          int tray, Media sizeName, boolean portrait, String pages, int copies ) {
    PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
    if (ps != null) {

      // on ajoute quelques options
      attributes = new HashPrintRequestAttributeSet();

      // le type de bac (supprime le format de page si donné)
      if (tray > 0) {
        Media mt[] = getPaperTraysArray(ps);
        attributes.add(mt[tray - 1]);
      }

      // la taille de la page
      if (sizeName != null) {
        attributes.add(sizeName); //MediaSizeName.ISO_A4
      }

      // le mode portrait ou paysage.
      if (portrait) {
        attributes.add(OrientationRequested.PORTRAIT);
      } else {
        attributes.add(OrientationRequested.LANDSCAPE);
      }

      // les pages
      if (pages != null && !pages.isEmpty()) {
        attributes.add(new PageRanges(pages));
      }

      // le nb de copies
      if (copies > 0) {
        attributes.add(new Copies(copies));
      }

    }
    return attributes;
  }

  /**
   * Prépare un set d'attributs pour une requête d'impression. Attention,
   * si "sizeName" est différent de null, cette information surclasse
   * l'information du bac (tray).
   *
   * @param PrinterName un nom d'imprimante (même incomplet)
   * @param tray un no du bac (1, 2, 3 ...)
   * @param sizeName la grandeur de la page (ex: MediaSizeName.ISO_A4)
   * @param portrait le mode portrait ou paysage
   * @param pages la plage des pages à imprimer (null ou "" = tout)
   * @param copies le nb de copies (&gt; 0, autrement la valeur par défaut)
   *
   * @return un set d'attributs
   */
  public static PrintRequestAttributeSet buildPrintRequestAttributeSet( String PrinterName,
          int tray, Media sizeName, boolean portrait, String pages, int copies ) {
    PrintRequestAttributeSet aset = null;
    PrintService ps = findPrintService(PrinterName);
    if (ps != null) {
      aset = buildPrintRequestAttributeSet(ps, tray, sizeName, portrait, pages, copies);
    }
    return aset;
  }

  /**
   * Prépare un set d'attributs pour une requête d'impression
   * (version simplifiée).
   *
   * @param ps un service d'impression d'une imprimante
   * @param tray un no du bac (1, 2, 3 ...)
   * @param pages la plage des pages à imprimer
   * @param copies le nb de copies (1 = defaut)
   *
   * @return un set d'attributs
   */
  public static PrintRequestAttributeSet buildPrintRequestAttributeSet( PrintService ps,
          int tray, String pages, int copies ) {
    return buildPrintRequestAttributeSet(ps, tray, null, true, pages, copies);
  }

  /**
   * Préparer un set d'attributs pour une requête d'impression
   * (version simplifiée).
   *
   * @param PrinterName un nom d'imprimante (même partiel)
   * @param tray un no du bac (1, 2, 3 ...)
   * @param pages la plage des pages à imprimer
   * @param copies le nb de copies (1 = defaut)
   *
   * @return un set d'attributs
   */
  public static PrintRequestAttributeSet buildPrintRequestAttributeSet( String PrinterName,
          int tray, String pages, int copies ) {
    PrintRequestAttributeSet aset = null;
    PrintService ps = findPrintService(PrinterName);
    if (ps != null) {
      aset = buildPrintRequestAttributeSet(ps, tray, null, true, pages, copies);
    }
    return aset;
  }

  /**
   * Affiche un set d'attributs (sur la console) pour une requête d'impression.
   *
   * @param aset un set d'attributs de la classe PrintRequestAttributeSet
   */
  public static void showAttributes( PrintRequestAttributeSet aset ) {
    Attribute tab[] = aset.toArray();
    for (int i = 0; i < tab.length; i++) {
      Attribute attr = tab[i];
      System.out.println("  - " + i + ". " + attr.getName() + " " + attr.toString());
    }
  }

}
