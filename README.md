# BasicLib 1.4.0 - january 2020
A Java library with some basic methods (most are static) for all your needs when you are starting an application. Comments are in french in the source code, but methods are in english. You find classes and methods to :
- manage date and time (see DateTimeLib class);
- read files (see files in ch.jcsinfo.file package);
- make mathematical operations (see MathLib class);
- use generic model classes (see files in ch.jcsinfo.models);
- manage system printers (see files in ch.jcsinfo.printing package);
- use some system operations (see files in ch.jcsinfo.system package);
- use callback, conversion types, prefs manager, screen info, etc.

You can download and open this project in NetBeans 8.2 or higher. It's a Java 8 maven project. So, dependencies are loaded automaticly from maven central. There are some test classes where you can learn how to use this library.

In MacOS terminal or Windows console, you can start the "test" suite with a Maven command :
- mvn test

You can check a specific test with (for example) :
- mvn test -Dtest=BinaryFileReaderTest

Documentation :<br>
    http://www.jcsinfo.ch/doc/basiclib<br>

New in release 1.4.0 (13.01.2020) :
* JavaLib.getJavaVersionBits has been renamed "JavaLib.getJavaDataModel"
* new method JavaLib.getJavaFullVersion() to return version with 32 ou 64 bits info 
* new method SystemLib.getOs() that returns Operating System name with version
* tests methods adapted to these new mathods

New in release 1.3.4 (03.01.2019) :
* all static methods in "ObjectCloner" use now "try with resources"

New in release 1.3.3 (08.11.2018) :
* method "open " in BinaryFileReader no more return a boolean, but throws a FileException

New in release 1.3.2 (26.10.2018) :
* all file methods now throws a FileException
* there is no more a use of a logger in this library
* delete method "printTestInfo" in StackTracer
* enhancement of method "printTestResult" in StackTracer
* use of "printTestResult" in all test classes

New in release 1.3.1 (6.6.2018) :
* remove getLocalDateConverter to don't have a JavaFX dependence in this library

New in release 1.2.3 (16.5.2018) :
* correct a bug in "isFileExists" method (crash on Windows if the file not exists, ok on MacOS)
* use of lombok 1.16.20

New in release 1.2.2 (10.3.2018) :
* add a new method getLocalDateConverter for JavaFx DatePicker
* correct some bugs in methods parseDate, parseTime (DateTimeLib) when Locale is changed
* correct getSmartToday method in DateTimeLib (now works for french, german, english)
* update of test methods for DateTimeLib

New in release 1.2.1 (17.2.2018) :
* delete of method createDate for a hexadecimal timestamp

New in release 1.2.0 (6.2.2018) :
* All class, methods for encryption and hashing are now out of this library
* The are now in new CypherLib 1.0.1 library

New in release 1.1.1 (1.1.2018) :
* DateTimeLib: new method "createDate" from a timestamp represented in a hexadecimal char. string.

New in release 1.1.0 (29.12.2017) :
* ConvertLib: method "rehashKeyWithSalt" has been renamed "computeServerKey".
* ConvertLib: method "hashNewKey" has been renamed "computeClientKey".

New in release 1.0.13 (21.11.2017) :
* textToBean (int idx, String text) method in BeanExtracter has now a new parameter "idx" (index of the readed line, begin with 1)
* getLocalePatternInfo() adapted because some errors on MacOS High Sierra
* parseDate(String sDate, boolean lastDayOfMonth) adapted to new getLocalePatternInfo

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

