@echo off

set file=%1
set databaseName=%2
set tableName=%3
set hostName=%4
set portName=%4
set userName=%5
set password=%6
set executable=Prueba/out/artifacts/Prueba_jar/Prueba.jar

java -jar %executable% %file% %databaseName% %tableName% %hostName% %portName% %userName% %password%

echo "Done."
@echo on