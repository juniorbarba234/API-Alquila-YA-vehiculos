@echo off
cd /d "%~dp0"
if not defined JAVA_HOME for /d %%J in ("%ProgramFiles%\Java\jdk-*") do if exist "%%~fJ\bin\java.exe" set "JAVA_HOME=%%~fJ"
echo Iniciando AlquilaYa. Cuando termine, abre http://localhost:8080
echo Deja esta ventana abierta mientras uses la interfaz.
call mvnw.cmd spring-boot:run
pause
