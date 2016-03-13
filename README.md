# BasicLib
A Java library with some basic methods (most are static methods) for all your needs when you are starting an application. Comments are in french in the source code. You find classes and methods for :
- some date and time methods (see DateTimeLib class)
- some classes & methods to read files (see files in ch.jcsinfo.file package)
- some mathematical methods (see MathLib class)
- some generic model classes (see files in ch.jcsinfo.models)
- some printer classes & methods (see files in ch.jcsinfo.printing package)
- some system classes & methods (see files in ch.jcsinfo.system package)
- some util classes & methods (callback, simple cypher, conversion types, prefs manager, screen info, etc)

You can download and open this project in NetBeans 8.1. I'ts a Java 8 maven project. So, dependencies are loaded automaticly from maven central. There are some test classes where you can learn how to use this library.

In Terminal or console Windows, you can start "test" with a Maven command :
- mvn test
 
You can check a specific test with (for example) :
- mvn test -Dtest=BinaryFileReaderTest

You can recreate site "javadoc" with :
- mvn site

    http://jcstritt.emf-informatique.ch/basiclib/project-info<br>
    http://jcstritt.emf-informatique.ch/basiclib/project-javadoc

