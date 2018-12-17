echo %CD%
java -cp %CD%\lib\*;%CD%\bin org.testng.TestNG testng.xml
PAUSE