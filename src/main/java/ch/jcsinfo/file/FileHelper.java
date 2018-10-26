package ch.jcsinfo.file;

import ch.jcsinfo.system.StackTracer;
import ch.jcsinfo.system.SystemLib;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Classe de méthodes statiques pour aider à la gestion de fichiers.
 *
 * @author Jean-Claude Stritt
 */
public class FileHelper {
  public static final String REGEX_URL1 = "[\\\\]+";
  public static final String REGEX_URL2 = "\\" + "/";
  public static final String REGEX_FDR1 = "[/\\\\]+";
  public static final String REGEX_FDR2 = "\\" + File.separator;

  /**
   * Normalise une URL en la transformant tout en minuscules et en transformant
   * les backslashs en slashs.
   *
   * @param url une URL à normaliser
   * @return l'URL normalisé
   */
  public static String normalizeUrl(String url) {
    return url.toLowerCase().replaceAll(REGEX_URL1, REGEX_URL2);
  }

  /**
   * Normalise un nom de fichier avec son chemin en transformants les slash ou
   * backslash avec le séparateur prévu dans l'OS.
   *
   * @param fileName un nom de fichier à normaliser
   * @return le nom de fichier normalisé
   */
  public static String normalizeFileName(String fileName) {
    return fileName.replaceAll(REGEX_FDR1, REGEX_FDR2);
  }

  /**
   * Extrait le chemin vers un fichier depuis son nom complet.
   *
   * @param filePath le nom complet d'un fichier
   * @return le chemin vers ce fichier
   */
  public static String extractPathName(String filePath) {
    int idx = filePath.lastIndexOf(File.separator);
    return (idx > -1) ? filePath.substring(0, idx) : filePath;
  }

  /**
   * Extrait le nom du fichier sans son chemin depuis un nom complet.
   *
   * @param filePath le nom complet (avec le chemin) d'un fichier
   * @return le nom du fichier sans le chemin
   */
  public static String extractFileName(String filePath) {
    int idx = filePath.lastIndexOf(File.separator);
    return (idx > -1) ? filePath.substring(idx + 1) : filePath;
  }

  /**
   * Extrait uniquement le nom du fichier sans son chemin et sans son extension.
   *
   * @param filePath le nom complet (avec le chemin) d'un fichier
   * @return le nom du fichier sans le chemin, ni l'extension
   */
  public static String extractOnlyName(String filePath) {
    String onlyName = extractFileName(filePath);
    int idx = onlyName.lastIndexOf(".");
    return (idx > -1) ? onlyName.substring(0, idx) : onlyName;
  }

  /**
   * Extrait l'extension du nom (ex: .xls) comprise dans un nom de fichier.
   *
   * @param filePath le nom complet d'un fichier
   * @return l'extension comprise dans le nom du fichier (ou "")
   */
  public static String extractFileExt(String filePath) {
    int idx = filePath.lastIndexOf(".");
    return (idx > -1) ? filePath.substring(idx) : "";
  }

  /**
   * Extrait un nom de fichier y compris son chemin, mais sans son extension.
   *
   * @param filePath le nom complet d'un fichier
   * @return le nom du fichier sans son extension
   */
  public static String extractFileNameWithoutExt(String filePath) {
    int idx = filePath.lastIndexOf(".");
    return (idx > -1) ? filePath.substring(0, idx) : filePath;
  }

  /**
   * Remplace l'extension d'un fichier par une autre à spécifier (ex: ".pdf").
   *
   * @param filePath le nom complet du fichier
   * @param newExt   la nouvelle extension (ex: ".pdf")
   * @return le nouveau nom de fichier avec la nouvelle extension
   */
  public static String replaceFileExt(String filePath, String newExt) {
    return extractFileNameWithoutExt(filePath) + newExt;
  }

  /**
   * Crée un nouveau nom de fichier d'après un ancien donné sous sa forme
   * complète et un nouveau nom (sans l'extension).
   *
   * @param filePath le nom complet du fichier
   * @param onlyName un nouveau nom (sans chemin, ni extension)
   * @return un nouveau nom de fichier
   */
  public static String replaceOnlyName(String filePath, String onlyName) {
    String path = extractPathName(filePath);
    String ext = extractFileExt(filePath);
    return path + File.separator + onlyName + ext;
  }

  /**
   * Ajoute une sous-chaîne à un nom de fichier existant.
   *
   * @param filePath le nom complet du fichier
   * @param addPart  une partie à ajouter au nom (ex: "_new")
   * @return un nouveau nom de fichier
   */
  public static String addToFileName(String filePath, String addPart) {
    String path = extractPathName(filePath);
    String name = extractOnlyName(filePath);
    String ext = extractFileExt(filePath);
    return path + File.separator + name + addPart + ext;
  }

  /**
   * Retourne le répertoire courant d'où est lancé l'application.
   *
   * @return le dossier courant de l'application
   */
  public static String getCurrentDir() {
    return System.getProperty("user.dir");
  }

  /**
   * Récupère une liste de fichiers filtrée pour un répertoire donné.
   *
   * @param folder   le dossier où rechercher les fichiers
   * @param fileName le nom partiel des fichier à filtrer
   *
   * @return une liste avec les noms de fichiers
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static List<String> getFolderFiles(String folder, String fileName) throws FileException {
    List<String> files = new ArrayList<>();
    Path path = Paths.get(folder);
    try (Stream<Path> filePathStream = Files.walk(path)) {
      filePathStream.forEach(filePath -> {
        if (Files.isRegularFile(filePath) && filePath.toString().toLowerCase().contains(fileName)) {
//          System.out.println(filePath);
          files.add(filePath.toString());
        }
      });
    } catch (IOException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return files;
  }

  /**
   * Retrouve le chemin relatif d'un fichier en lui enlevant une base (souvent
   * le répertoire courant).
   * <BR>
   *
   * @see "http://stackoverflow.com/questions/204784"
   *
   * @param filePath le nom complet du fichier avec son chemin
   * @param basePath la base du chemin à soustraire (rép. courant par exemple)
   *
   * @return le chemin relatif vers le fichier
   */
  public static String getRelativePath(String filePath, String basePath) {
    File target = new File(filePath);
    File base = new File(basePath);
    StringBuilder result = new StringBuilder();

    try {
      String[] baseComponents = base.getCanonicalPath().split(Pattern.quote(
        File.separator));
      String[] targetComponents = target.getCanonicalPath().split(Pattern.quote(
        File.separator));

      // skip common components
      int index = 0;
      for (; index < targetComponents.length && index < baseComponents.length; ++index) {
        if (!targetComponents[index].equals(baseComponents[index])) {
          break;
        }
      }
      if (index != baseComponents.length) {
        // backtrack to base directory
        for (int i = index; i < baseComponents.length; ++i) {
          result.append("..");
          result.append(File.separator);
        }
      }
      for (; index < targetComponents.length; ++index) {
        result.append(targetComponents[index]);
        result.append(File.separator);
      }
      if (!target.getPath().endsWith("/") && !target.getPath().endsWith("\\")) {
        // remove final path separator
        result.delete(result.length() - "/".length(), result.length());
      }
    } catch (IOException ex) {
      result.append(filePath);
    }
    return result.toString();
  }

  /**
   * Retourne le chemin relatif depuis le répertoire courant.
   *
   * @param filePath le nom complet du fichier avec son chemin
   * @return le chemin relatif vers le fichier
   */
  public static String getRelativePathFromCurDir(String filePath) {
    String basePath = getCurrentDir();
    String rel = getRelativePath(filePath, basePath);
    int p = rel.indexOf(":");
    if (p > 0 && rel.contains("..")) {
      rel = rel.substring(p - 1);
    }
    return rel;
  }

  /**
   * Retrouve l'accès complet à un fichier alors que celui-ci est donné sous la
   * forme d'un chemin relatif.
   *
   * @param relativeName le nom du fichier sous forme relative
   * @return le nom complet pour l'accès au fichier
   */
  public static String getAbsolutePath(String relativeName) {
    String p = "";
    if (relativeName != null && !relativeName.isEmpty()) {
      File file = new File(relativeName);
      p = file.getAbsolutePath();
    }
    return p;
  }

  /**
   * Teste si un nom de fichier existe.
   *
   * @param fName le nom du fichier
   * @return TRUE si le fichier existe
   */
  public static boolean isFileExists(String fName) {
    File file = new File(fName);
    URI uri = file.toURI();
    return Files.exists(Paths.get(uri));
  }

  /**
   * Convertit une URL identifiant un fichier en une URL standard.
   *
   * @param filePath le nom du fichier avec son chemin
   * @return une URL au format String
   */
  public static String filePathToURL(String filePath) {
    File f = new File(filePath);
    StringBuilder sb = new StringBuilder("file:///");
    try {
      sb.append(f.getCanonicalPath().replace('\\', '/'));
    } catch (IOException e) {
    }
    return sb.toString();
  }

  /**
   * Retourne le nom complet d'un fichier d'après son URL.
   *
   * @param urlFilePath l'accès au fichier d'après son URL
   * @return un nom de fichier normalisé Windows, Mac, Unix, etc.
   */
  public static String urlToFilePath(String urlFilePath) {
    String filePath = urlFilePath;
    File f;
    URL url;
    try {
      url = new URL(urlFilePath);
      try {
        f = new File(url.toURI());
      } catch (URISyntaxException e) {
        f = new File(url.getPath());
      }
      try {
        filePath = f.getCanonicalPath();
      } catch (IOException ex) {
        System.out.println(new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage()));
      }
    } catch (MalformedURLException ex) {
      System.out.println(new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage()));
    }
    return filePath;
  }

  /**
   * Efface un fichier spécifié par son nom complet.
   *
   * @param filePath un nom de fichier à effacer
   * @return true si l'effacement s'est bien déroulé
   */
  public static boolean deleteFile(String filePath) {
    boolean ok = false;
    try {
      File file = new File(filePath);
      if (file.delete()) {
        ok = true;
      }
    } catch (Exception ex) {
      System.out.println(new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage()));
    }
    return ok;
  }

  /**
   * Efface un fichier spécifié par son nom complet.
   *
   * @param urlFilePath une url désignant un fichier
   * @return true si l'effacement a pu se faire, false autrement
   */
  public static boolean deleteFileWithUrl(String urlFilePath) {
    String filePath = urlToFilePath(urlFilePath);
    return deleteFile(filePath);
  }

  /**
   * Extrait un nom de fichier d'une URL de de base de donnée.
   *
   * @param urlString l'URL d'une base de données
   * @return le nom du fichier extrait de l'URL
   */
  public static String extractFileFromDbUrl(String urlString) {
    String nzUrl = normalizeUrl(urlString);
    String dbName = urlString;
    int idx = nzUrl.indexOf("//localhost:");
    if (idx > 0) {
      dbName = urlString.substring(idx);
    } else {
      idx = nzUrl.indexOf("//");
      if (idx > 0) {
        dbName = urlString.substring(idx + 2);
      } else {
        idx = nzUrl.indexOf("dbq=");
        if (idx > 0) {
          dbName = urlString.substring(idx + 4);
        }
      }
    }
    return normalizeFileName(dbName);
  }

  /**
   * Extrait le nom du driver (pilote) JDBC utilisé.
   *
   * @param urlString l'URL d'une base de données
   * @return la partie "driver" de l'URL
   */
  public static String extractDriverFromDbUrl(String urlString) {
    String nzUrl = normalizeUrl(urlString);
    String driverName = "";
    int idx = nzUrl.indexOf("://");
    if (idx > 0) {
      driverName = urlString.substring(0, idx + 3);
    }
    return driverName;
  }

  /**
   * Reconstruit une URL de DB d'après une URL existante. La méthode extrait le
   * nom driver et le fichier, recherche le répertoire courant, puis reconstruit
   * l'URL avec ces éléments. Le nom du fichier est encore précédé de "data/" si
   * "data/" existe dans l'URL d'origine.
   *
   * @param urlString l'URL d'une base de données
   * @return une nouvelle URL reconstruite selon les indications ci-dessus
   */
  public static String buildNewDbUrl(String urlString) {
    String nzUrl = normalizeUrl(urlString);
    String dbName = extractFileName(extractFileFromDbUrl(urlString));
    String driverName = extractDriverFromDbUrl(urlString);
    String currentDir = getCurrentDir();
    String newUrl = driverName + currentDir;
    int idx = nzUrl.indexOf("data/");
    if (idx >= 0) {
      newUrl = newUrl + File.separator + "data";
    }
    newUrl = normalizeUrl(newUrl + File.separator + dbName);
//    System.out.println("dbName: " + dbName + " driverName: " + driverName
//      + " currentDir: " + currentDir + " newUrl: " + newUrl);
    return newUrl;
  }

  /**
   * Teste si une URL de base de donnée est correcte. Dans le cas d'URL de
   * fichier, la méthode teste également si le fichier existe.
   *
   * @param urlString l'URL à tester
   * @return TRUE si l'URL est correct
   */
  public static boolean checkDataBaseURL(String urlString) {
    String nzUrl = normalizeUrl(urlString);
    String dbName = urlString;
    boolean fileOk = true;
    int idx = nzUrl.indexOf("//localhost:");
    if (idx > 0) {
      dbName = urlString.substring(idx);
    } else {
      idx = nzUrl.indexOf("//");
      if (idx > 0) {
        dbName = urlString.substring(idx);
        fileOk = isFileExists(dbName);
        dbName = "file:" + dbName;
      } else {
        idx = nzUrl.indexOf("dbq=");
        if (idx > 0) {
          dbName = urlString.substring(idx + 4);
          fileOk = isFileExists(dbName);
          dbName = "file://" + dbName;
        }
      }
    }

    try {
      URL url = new URL(dbName);
    } catch (MalformedURLException ex) {
      fileOk = false;
    }
    return fileOk;
  }

  /**
   * Teste si une classe (un pilote JDBC par exemple) peut être chargée.
   *
   * @param className la classe qu'il faut tester
   * @return TRUE si la classe peut être chargée
   */
  public static boolean checkClass(String className) {
    try {
      Class<?> cls = Class.forName(className);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  /**
   * Retourne un "inputStream" (flot d'entrée) vers un fichier ou une image qui
   * se trouverait dans le chemin de l'application (classpath).
   *
   * @param fname nom d'un fichier dans les resources de l'application
   * @return un canal (inputstream) ouvert sur le fichier
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static InputStream getResourceInputStream(String fname) throws FileException {
    InputStream is = null;
    String clName = StackTracer.getParentClass(-2);
    try {
      Class<?> cl = Class.forName(clName);
      is = cl.getClassLoader().getResourceAsStream(fname);
    } catch (ClassNotFoundException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    }
    return (is != null) ? new BufferedInputStream(is) : is;
  }

  /**
   * Retourne un "inputStream" (flot d'entrée) vers un fichier ou une image.
   * Dans le cas où "fname1" n'est pas trouvé, on recherche "fname2" dans les
   * resources de l'application (classpath).
   *
   * @param fname1 nom d'un fichier hors champ de l'application
   * @param fname2 nom d'un fichier dans les resources internes
   * @return un canal (inputstream) ouvert sur le fichier
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static InputStream getInputStream(String fname1, String fname2) throws FileException {
    InputStream is;
    try {
      is = new FileInputStream(fname1);
    } catch (FileNotFoundException ex1) {
      is = getResourceInputStream(fname2);
    }
    return is;
  }

  /**
   * Retourne un objet "flux d'entrée" vers un fichier qui peut se trouver soit
   * dans les resources du projet Java, soit dans le chemin spécifié.
   *
   * @param fileName un nom de fichier avec son chemin d'accès
   * @return un flux de type "InputStream"
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static InputStream getInputStream(String fileName) throws FileException {
    InputStream is;
    try {
      is = new FileInputStream(fileName);
    } catch (FileNotFoundException ex1) {
      is = getResourceInputStream(fileName);
    }
    return is;
  }

  /**
   * Charge un set de propriétés depuis un fichier .properties. Cette méthode a
   * la particularité d'essayer d'ouvrir le fichier de propriété d'abord par son
   * chemin dans le système de resources et s'il ne trouve pas le fichier,
   * d'essayer par un chemin absolu (dans le répertoire courant par exemple).
   *
   * @param fileName l'accès vers un fichier de propriétés
   * @return un set de propriétés
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static Properties loadProperties(String fileName) throws FileException {
    Properties props = new Properties();
    InputStream is = getInputStream(fileName);
    if (is != null) {
      try {
        props.load(is);
        is.close();
      } catch (IOException ex) {
        throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
      }
    }
    return props;
  }

  /**
   * Charge un set de propriétés depuis un fichier XML (normalement
   * persistence.xml). Les tag "property" sont recherchés.
   *
   * @param fileName le nom d'un fichier XML
   * @return un set de propriétés sous la forme clé-valeur.
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static Properties loadXmlProperties(String fileName) throws FileException {
    Properties props = new Properties();
    InputStream is = getInputStream(fileName);
    if (is != null) {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      try {
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("property");
        for (int i = 0; i < nodes.getLength(); i++) {
          Node node = nodes.item(i);
          if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element prop = (Element) node;
            props.put(prop.getAttribute("name"), prop.getAttribute("value"));
//            System.out.println(i + ". " + prop.getAttribute("name") + "=" + prop.getAttribute("value"));
          }
        }
      } catch (ParserConfigurationException | SAXException | IOException ex) {
        System.out.println(new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage()));
      }
    }
    return props;
  }

  /**
   * Ouvre un document sur le bureau (spécifié par son nom complet).
   *
   * @param docPath le nom complet du document (fichier) avec son chemin
   * @return true (vrai) si le document a été trouvé (et normalement ouvert)
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static boolean openDocument(String docPath) throws FileException {
    boolean ok = isFileExists(docPath);
    if (ok) {
      SystemLib.openDesktopFile(docPath);
    }
    return ok;
  }

  /**
   * Ouvre un document sur le bureau (spécifié par son URL).
   *
   * @param urlName une URL de fichier
   * @return true (vrai) si le document a été trouvé et normalement ouvert
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static boolean openDocumentWithUrl(String urlName) throws FileException {
    String docName = urlToFilePath(urlName);
    return openDocument(docName);
  }

  private static String check(String filename) {
    return filename.replaceAll("[?:\\\\/*\\\"\\\"<>|]", "-").replace("..", ".");
  }

  /**
   * Copie un fichier d'un endroit vers un autre via des flux de données.
   *
   * @param sourceFile le chemin vers le fichier à copier (de type File)
   * @param targetDirectory le dossier de destination (de type String)
   * @param targetFilename le nom du fichier de destination (de type String)
   * @param deleteOnExit true si l'on veut effacer le fichier source
   * @return l'URI du fichier créé
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static URI copyFile(File sourceFile, File targetDirectory, String targetFilename, boolean deleteOnExit) throws FileException {
    targetFilename = check(targetFilename);
    FileOutputStream out;
    File outp;
    try (InputStream in = new FileInputStream(sourceFile)) {
      if (targetDirectory != null) {
        outp = new File(targetDirectory + File.separator + targetFilename);
        if (!targetDirectory.exists()) {
          targetDirectory.mkdirs();
        }
        outp.delete();
        out = new FileOutputStream(targetDirectory + File.separator + targetFilename);
      } else {
        outp = new File(targetFilename);
        outp.delete();
        out = new FileOutputStream(targetFilename);
      }
      
      // copie des octets depuis le flux entrant vers le flux sortant
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } catch (IOException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    } 
    return outp.toURI();
  }

  /**
   * Copie un fichier d'un endroit vers un autre via des flux de données
   *
   * @param sourceFile le fichier source de type File
   * @param destFile le fichier de destination de type File
   *
   * @return l'URI du fichier de destination
   * @throws FileException l'exception à gérer au niveau supérieur
   */
  public static URI copyFile(File sourceFile, File destFile) throws FileException {
    OutputStream out;
    try (InputStream in = new FileInputStream(sourceFile)) {
      out = new FileOutputStream(destFile);
      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } catch (IOException ex) {
      throw new FileException(FileHelper.class.getSimpleName(), StackTracer.getCurrentMethod(), ex.getMessage());
    } 
    return destFile.toURI();
  }

}
