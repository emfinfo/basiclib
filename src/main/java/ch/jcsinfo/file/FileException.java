package ch.jcsinfo.file;

/**
 * Exception de fichier.
 * 
 * @author jcstritt
 */
public class FileException extends Exception {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * 
   * @param className the class name
   * @param methodName the methodName
   * @param msg the error message
   */
  public FileException(String className,  String methodName, String msg) {
    super("File error detected in: " + methodName + ", class: " + className + "\n" + msg);
  }

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public String getMessage() {
    return super.getMessage();
  }

}
