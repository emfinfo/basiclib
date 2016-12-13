# BasicLib 1.0.7 - december 2016
A Java library with some basic methods (most are static) for all your needs when you are starting an application. Comments are in french in the source code, but methods are in english. You find classes and methods to :
- manage date and time (see DateTimeLib class);
- read files (see files in ch.jcsinfo.file package);
- make mathematical operations (see MathLib class);
- use generic model classes (see files in ch.jcsinfo.models);
- manage system printers (see files in ch.jcsinfo.printing package);
- use some system operations (see files in ch.jcsinfo.system package);
- use callback, simple cypher, conversion types, prefs manager, screen info, etc.

You can download and open this project in NetBeans 8.1. It's a Java 8 maven project. So, dependencies are loaded automaticly from maven central. There are some test classes where you can learn how to use this library.

In MacOS terminal or Windows console, you can start the "test" suite with a Maven command :
- mvn test

You can check a specific test with (for example) :
- mvn test -Dtest=BinaryFileReaderTest

Documentation :<br>
    http://jcstritt.emf-informatique.ch/doc/basiclib<br>

New in release 1.0.7 (13.12.2016) :
* Class "TextFileExtracter" has been renamed "BeanExtracter" and abstract method "extract" has been renamed "textToBean" .
* New methods "getXXX" in ConvertLib to extract information from a text line.

New in release 1.0.6 (24.11.2016) :
* In method "getWorkYearDates" of DateTimeLib, you can now specify a "monthsOffset" to compute last date of a period.

New in release 1.0.5 (19-21.10.2016) :
* DateTimeLib methods "getCalendarYearDates" are renamed "getYearDates"
* DateTimeLib methods "getOnYearDates" are renamed "getWorkYearDates"
* New added DateTimeLib methods "getMonthDates" to return limit dates in a month
* update in "InObject.fieldsToString" for boolean type

New in release 1.0.4 (11-15.10.2016) :
* class "Message" is renamed "Option" and new class DialogModel is introducted for JOptionPane messages
* "findMethod", "callMethod" are moved from "SystemLib" to new class "InObject" (introspection into an object)
* new "callGetter" in "InObject", only for "getter" methods (methods without parameter)
* new "fieldsToString" method in "InObject" can retrieve all private fields of an entity bean object (ex. of output: localite={pkLoc: 1, npa: 1700, localite: Fribourg, canton: FR} )
* DATE_TIME_FORMAT_STANDARD in DateTimeLib is now ""d.M.yy HH:mm:ss"

New in release 1.0.3 (1.10.2016):
* "roundValueToBigDecimal" is now named "convertToBigDecimal"
* Change release numbering from MAJOR.MINOR to MAJOR.MINOR.PATCH

