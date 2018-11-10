package ch.jcsinfo.file;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.util.ConvertLib;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Permet de lire un fichier binaire de type ".DAF" (fichiers des logiciels PSP).
 *
 * @author Jean-Claude Stritt
 */
public class BinaryFileReader {
  private String fileName;
  private int recordSize;
  private DataInputStream dis = null;

  /**
   * Constructeur.
   *
   * @param fileName le nom du fichier à lire
   * @param recordSize la longueur fixe d'un enregistrement
   */
  public BinaryFileReader( String fileName, int recordSize ) {
    this.fileName = fileName;
    this.recordSize = recordSize;
  }

  /**
   * Ouvre le fichier binaire.
   *
   * @throws FileException une exception qu'il faut traiter à un niveau supérieur
   */
  public void open() throws FileException {
    boolean ok = false;
    try {
      dis = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
      ok = true;
    } catch (FileNotFoundException ex) {
      throw new FileException(this.getClass().getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
  }

  /**
   * Saute la lecture d'un certain nombre d'octets.
   *
   * @param nombre le nombre de bytes à sauter
   * @return le nb d'octets sautés
   */
  public int skipBytes( int nombre ) {
    try {
      return dis.skipBytes(nombre);
    } catch (IOException e) {
      return 0;
    }
  }

  /**
   * Retourne le nombre total d'octets du fichier.
   * @return les octets du fichier
   */
  public int numberOfBytes() {
    try {
      return dis.available();
    } catch (IOException ex) {
      return 0;
    }
  }

  /**
   * Retourne la taille d'un enregistrement (getter).
   *
   * @return un entier représentant la taille d'un enregistrement
   */
  public int getRecordSize() {
    return recordSize;
  }

  /**
   * Retourne le nombre total d'enregistrements du fichier.
   *
   * @return un entier représentant le nb total d'enregistrements d'un fichier
   */
  public int numberOfRecords() {
    return Math.round(numberOfBytes() / recordSize);
  }

  /**
   * Lit une chaine de caractères d'une certaine taille de type "mémo".
   *
   * @param size la taille maximale de la chaîne de caractères à lire
   * @param len longueur réelle de la chaîne de caractères à lire
   * @return la chaîne de caractères qui à été lue
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public String readStringMemo( int size, int len ) throws IOException {
    int b;
    char c;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      b = dis.readUnsignedByte();
      c = (char) b;
      sb.append(c);
    }
    dis.skipBytes(size - len);
    return ConvertLib.bufferToString(sb.toString().toCharArray());
  }

  /**
   * Lit une chaine de caractères d'une certaine taille.
   *
   * @param size la taille fixe de la chaîne de caractères à lire
   * @return la chaîne de caractères qui à été lue
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public String readString( int size ) throws IOException {
    int nb = dis.readUnsignedByte();
    return readStringMemo(size, nb);
  }

  /**
   * Lit un certain nombre d'octets et les retourne sous la forme d'un tableau
   * d'entiers non signés de type "short".
   *
   * @param size le nombre d'octets à lire
   * @return un tableau de short avec les octets lus
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public short[] readStringBuffer( int size ) throws IOException {
    int len = dis.readUnsignedByte();
    short[] buffer = new short[len];
    for (int i = 0; i < len; i++) {
      int b = dis.readUnsignedByte();
      buffer[i] = (short) b;
    }
    dis.skipBytes(size - len);
    return buffer;
  }

  /**
   * Lit un nombre entier non signé sur 2 octets (type Pascal "Word").
   *
   * @return le nombre entier lu
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public int readWord() throws IOException {
    return dis.readUnsignedByte() + 256 * dis.readUnsignedByte();
  }

  /**
   * Retourne une valeur booléenne suivant le mot entier lu (Word)
   * stocké dans le fichier.
   *
   * @return un booléen true ou false
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public boolean readBoolean() throws IOException {
    return readWord() != 0;
  }

  /**
   * Lit un nombre entier signé défini sur un nombre d'octets donné.
   *
   * @param nbrOfBytes le nombre d'octets à lire
   * @return le nombre entier lu
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public int readIntNumber(int nbrOfBytes) throws IOException {
    int value = 0;
    for (int i = 0; i < nbrOfBytes; i++) {
      int p = (int) Math.pow(256, i);
      value += dis.readUnsignedByte() * p;
    }
    return value;
  }

  /**
   * Lit un nombre entier signé sur 2 octets.
   *
   * @return le nombre entier lu
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public int readShort() throws IOException {
    return (short)readIntNumber(2);
  }

  /**
   * Lit un nombre entier signé sur 4 octets.
   *
   * @return le nombre entier lu
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public int readInt() throws IOException {
    return readIntNumber(4);
  }

  /**
   * Permet de lire un nombre de type Pascal "Dec6" ou "Dec14".
   * Le retour est un "BigDecimal" en Java.
   *
   * @param n le type du nombre Dec à lire (6 pour Dec6, 14 pour Dec14)
   * @return le nombre BigDecimal lu (pour traduire en entier, ajouter .intValue())
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public BigDecimal readDec( int n ) throws IOException {
    short[] buffer = readStringBuffer(1 + (n / 2));
    return ConvertLib.bcdToBigDecimal(buffer);
  }

  /**
   * Lit une date dans le fichier binaire.
   *
   * @return la date qui à été lue
   * @throws java.io.IOException l'exception à gérer au niveau supérieur
   */
  public Date readDate() throws IOException {
    return ConvertLib.intToDate(readWord());
  }

  /**
   * Ferme le fichier binaire.
   */
  public void closeFile() {
    try {
      if (dis != null) {
        dis.close();
      }
    } catch (IOException ex) {
    }
  }
}
