@echo off
REM Plagiarism Detection Tool - Compile and Run Script

echo ========================================
echo Plagiarism Detection Tool
echo Compile and Run Script
echo ========================================
echo.

REM Navigate to project directory
cd /d "%~dp0"

REM Clean and create bin directory
if exist bin rmdir /s /q bin
mkdir bin

echo Compiling Java files...
javac -encoding UTF-8 -d bin src\Main.java src\similarity\*.java src\algo\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Starting Plagiarism Detection Tool...
echo ========================================
echo.

REM Run the program
cd bin
java Project2_java.src.Main

cd ..
pause
