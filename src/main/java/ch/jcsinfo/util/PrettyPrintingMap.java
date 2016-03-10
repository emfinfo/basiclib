package ch.jcsinfo.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Classe qui permet de convertir rapidement le contenu d'une map en une chaine
 * de caractères (String) affichable facilement par la suite.<br>
 * Exemple d'utilisation :<br>
 * System.out.println(new PrettyPrintingMap(myMap));
 *
 * @author jcstritt
 * @param <K> le type pour les clés
 * @param <V> le type pour les valeurs
 */
public class PrettyPrintingMap<K, V> {

  private Map<K, V> map;

  /**
   * Constructeur.
   *
   * @param map la map à convertir en String
   */
  public PrettyPrintingMap(Map<K, V> map) {
    this.map = map;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator<Entry<K, V>> iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<K, V> entry = iter.next();
      sb.append(entry.getKey());
      sb.append('=').append('"');
      sb.append(entry.getValue());
      sb.append('"');
      if (iter.hasNext()) {
        sb.append('\n');
      }
    }
    return sb.toString();

  }
}
