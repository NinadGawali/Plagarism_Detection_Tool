@echo off
echo ================================================
echo PLAGIARISM DETECTION TOOL - QUICK TEST
echo ================================================
echo.
echo This script will test the enhanced plagiarism detection
echo with sample files showing varying levels of similarity.
echo.
echo Test Sets:
echo 1. High Similarity: StudentCode1 vs StudentCode2 (Factorial)
echo 2. High Similarity: StudentCode4 vs StudentCode5 (Sorting)
echo 3. High Similarity: StudentCode7 vs StudentCode8 (Fibonacci)
echo 4. Moderate: StudentCode1 vs StudentCode3 (Different factorial)
echo.
pause

cd bin

echo.
echo ================================================
echo Starting Plagiarism Detection Tool...
echo ================================================
echo.
echo Instructions:
echo 1. Select Option 1 (Run Plagiarism Check)
echo 2. Enter the following file paths when prompted:
echo.
echo    ..\StudentCode1.java
echo    ..\StudentCode2.java
echo    ..\StudentCode4.java
echo    ..\StudentCode5.java
echo    done
echo.
echo 3. Review the detailed report that will be generated
echo.
pause

java Project2_java.src.Main

echo.
echo ================================================
echo Test Complete!
echo ================================================
echo Check the generated report file: plagiarism_report_*.txt
echo.
pause
