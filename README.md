# BasicLib 1.0.13 - november 2017
A Java library with some basic methods (most are static) for all your needs when you are starting an application. Comments are in french in the source code, but methods are in english. You find classes and methods to :
- manage date and time (see DateTimeLib class);
- read files (see files in ch.jcsinfo.file package);
- make mathematical operations (see MathLib class);
- use generic model classes (see files in ch.jcsinfo.models);
- manage system printers (see files in ch.jcsinfo.printing package);
- use some system operations (see files in ch.jcsinfo.system package);
- use callback, simple cypher, conversion types, prefs manager, screen info, etc.

You can download and open this project in NetBeans 8.2. It's a Java 8 maven project. So, dependencies are loaded automaticly from maven central. There are some test classes where you can learn how to use this library.

In MacOS terminal or Windows console, you can start the "test" suite with a Maven command :
- mvn test

You can check a specific test with (for example) :
- mvn test -Dtest=BinaryFileReaderTest

Documentation :<br>
    http://jcstritt.emf-informatique.ch/doc/basiclib<br>

New in release 1.0.13 (21.11.2017) :
* textToBean (int idx, String text) method in BeanExtracter has now a new parameter "idx" (index of the readed line, begin with 1)

New in release 1.0.12 (11.11.2017) :
* parseIsoDate in DateTimeLib support now new format with time
* Small change in InObject.fieldsToString
* Adding formatDate in ConvertLib

New in release 1.0.11 (11.8.2017) :
* Better results in conversion with null values in dateToString (DateTimeLib) and fillString (ConvertLib).
* change in AbstractModel

New in release 1.0.10 (5.3.2017) :
* New pref enum SHOW_TOOLBAR in PrefsManager.java

New in release 1.0.9 (2-4.2.2017) :
* DateTimeLib class has been completly restructured and tested.
* New validation methods in this class (isValidDate, isValidTime, etc).
* Some methods of this class have been renamed (see below).
* stringTo... methods have been renamed "parseDate", "parseTime", "parseIsoDate".
* "getDate()" has been renamed "getNow()" and returns current date and time
* old "getToDay()" has been renamed "getSmartToday()"
* new "getToday()" method returns current date without time
* "getDate(int days)" remains the same
* "getWeekWorkingDates" has been renamed "getWeekDates"
* "isValidDateWithYear" methods have been renamed "isValidDate"

New in release 1.0.8 (3-4.1.2017) :
* DateTimeLib: new methods "getMonday", "getFriday", "getMondayFriday", "getWeekWorkingDates" && "getDateIndex".
* DateTimeLib: "getWorkYearDates" has been renamed "getYearWorkingDates".

New in release 1.0.7 (13-23.12.2016) :
* New "lock" & "unlock" methods in AbstractModel to manage "locking" operations on View.
* Class "TextFileExtracter" has been renamed "BeanExtracter" and abstract method "extract" has been renamed "textToBean" .
* Methode "textFileRead" in TextFileRead has been renamed "read".
* New methods "getXXX" in ConvertLib to extract information from a text line.
* Change algorithm in "getDaysBetweenTwoDates" (DateTimeLib).

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

