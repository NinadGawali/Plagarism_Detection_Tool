# Plagiarism Detection Tool - Quick Start Guide

## ✅ Setup Complete!

Your plagiarism detection tool has been successfully enhanced with all requested features!

## 🎯 New Features Added

### 1. **Professional Console Interface**
   - Welcome screen with ASCII art border
   - Interactive menu system
   - Progress indicators during analysis
   - Clear status messages with [OK], [ERROR], [SUCCESS] indicators

### 2. **Multiple File Comparison**
   - Compare 2 or more files simultaneously
   - Automatic pairwise comparisons
   - Handles any number of student submissions

### 3. **Line-by-Line Plagiarism Detection**
   - Identifies specific line ranges with high similarity
   - Shows exact sections that may be plagiarized
   - Configurable similarity threshold (currently 70%)

### 4. **Automatic Logging**
   - All results saved to timestamped text files
   - Format: `plagiarism_report_YYYYMMDD_HHMMSS.txt`
   - Comprehensive reports with all metrics
   - View previous reports from within the tool

### 5. **Suspicious Pair Detection**
   - Automatically flags pairs with >= 75% similarity
   - Visual warnings with [!] indicators
   - Sorted results (most similar first)

## 🚀 How to Run

### Option 1: Using the Batch File (Easiest)
```batch
1. Double-click "run.bat"
2. The tool will compile and start automatically
```

### Option 2: Manual Compilation
```batch
cd Project2_java
javac -encoding UTF-8 -d bin src\Main.java src\similarity\*.java src\algo\*.java
cd bin
java Project2_java.src.Main
```

## 📝 Usage Example

### Step-by-Step Walkthrough

1. **Start the Tool**
   - You'll see a welcome screen
   - Main menu with 3 options

2. **Select Option 1: Run Plagiarism Check**
   ```
   Enter file paths one by one:
   
   File path: C:\Lab\Student1.java
   File path: C:\Lab\Student2.java
   File path: C:\Lab\Student3.java
   File path: done
   ```

3. **Review and Confirm**
   - Tool shows all files to be compared
   - Type 'y' to proceed

4. **View Results**
   - Real-time progress indicator
   - Detailed similarity metrics for each pair
   - Suspicious pairs highlighted with [!]
   - Line ranges showing similar code sections

5. **Check the Report**
   - Automatically saved to a .txt file
   - Contains all details for documentation

### Lab Exam Scenario

**Situation**: You have 5 students who submitted code for a lab exam

```
Project2_java\
├── run.bat
├── bin\
└── submissions\
    ├── Alice_Lab1.java
    ├── Bob_Lab1.java
    ├── Charlie_Lab1.java
    ├── Diana_Lab1.java
    └── Eve_Lab1.java
```

**Run the check:**
1. Start the tool: `run.bat`
2. Choose option 1
3. Enter each file path (or drag-and-drop files into terminal)
4. Type "done"
5. Confirm with "y"

**Results:**
- Total comparisons: 10 (5 students × 4 pairs each)
- Suspicious pairs will be flagged
- Line-by-line details for suspicious cases
- Full report saved automatically

## 📊 Understanding the Output

### Similarity Metrics

Each comparison shows 5 different algorithms:

1. **N-gram Jaccard** (0.0 - 1.0)
   - Measures structural similarity
   - High values = similar code structure

2. **KMP sample match** (0.0 or 1.0)
   - Binary: pattern found or not
   - 1.0 = exact code segment found

3. **Rabin-Karp sample** (0.0 or 1.0)
   - Binary: pattern found or not
   - 1.0 = exact code segment found

4. **Suffix-array sample** (0.0 or 1.0)
   - Binary: pattern found or not
   - 1.0 = exact code segment found

5. **Edit-distance (normalized)** (0.0 - 1.0)
   - Character-level similarity
   - High values = minimal changes between files

6. **AVERAGE SIMILARITY**
   - Overall score (average of all 5)
   - **>= 75% = SUSPICIOUS [!]**

### Interpretation Guide

| Average Similarity | Status | Action |
|-------------------|--------|--------|
| 0% - 50% | Normal | No action needed |
| 50% - 74% | Medium | Review recommended |
| 75% - 100% | **SUSPICIOUS** | Manual investigation required |

### Line-by-Line Results Example

```
Similar Line Ranges:
  * Lines 15-45 (File A) <-> Lines 18-48 (File B) - 92.3% similar
  * Lines 67-89 (File A) <-> Lines 71-93 (File B) - 88.7% similar
```

**This means:**
- Lines 15-45 in File A match lines 18-48 in File B with 92.3% similarity
- Lines 67-89 in File A match lines 71-93 in File B with 88.7% similarity

## ⚙️ Configuration

You can adjust thresholds in [Main.java](Project2_java/src/Main.java):

```java
// Line 13-14
private static final double SIMILARITY_THRESHOLD = 0.75;        // 75%
private static final double LINE_SIMILARITY_THRESHOLD = 0.70;   // 70%
```

**After changing:**
```batch
cd Project2_java
javac -encoding UTF-8 -d bin src\Main.java src\similarity\*.java src\algo\*.java
```

## 📋 Sample Report Structure

```
================================================================================
PLAGIARISM DETECTION REPORT
================================================================================
Generated: 2026-01-22 16:54:06

FILES ANALYZED:
--------------------------------------------------------------------------------
1. C:\Lab\Student1.java
2. C:\Lab\Student2.java
3. C:\Lab\Student3.java

SIMILARITY THRESHOLD: 75.0%
LINE SIMILARITY THRESHOLD: 70.0%

================================================================================
DETAILED COMPARISON RESULTS
================================================================================

[Comparison 1] [!] SUSPICIOUS - POSSIBLE PLAGIARISM
--------------------------------------------------------------------------------
File A: C:\Lab\Student1.java
File B: C:\Lab\Student3.java

Similarity Metrics:
  N-gram Jaccard       : 0.892 (89.2%)
  KMP sample match     : 1.000
  Rabin-Karp sample    : 1.000
  Suffix-array sample  : 1.000
  Edit-distance (norm) : 0.856 (85.6%)
  >> AVERAGE SIMILARITY : 0.870 (87.0%) [!]

Similar Code Sections:
  * Lines 15-45 (File A) <-> Lines 18-48 (File B) - 92.3% similar
  * Lines 67-89 (File A) <-> Lines 71-93 (File B) - 88.7% similar
--------------------------------------------------------------------------------

... (more comparisons) ...

================================================================================
SUMMARY
================================================================================
Total Comparisons: 3
Suspicious Pairs: 1
Clean Pairs: 2

================================================================================
END OF REPORT
================================================================================
```

## 🔍 Tips for Best Results

1. **File Organization**
   - Keep all submissions in one folder
   - Use descriptive names: `StudentName_Assignment.java`

2. **Batch Processing**
   - Check entire class at once
   - More files = better pattern detection

3. **Review Process**
   - Focus on [!] SUSPICIOUS pairs first
   - Check line-by-line details
   - Manually verify suspicious cases
   - Consider false positives (e.g., template code)

4. **Documentation**
   - Keep all report files
   - Use timestamps to track checks
   - Reports can be evidence for academic integrity cases

## 🛠️ Troubleshooting

### "File not found" error
- Use absolute paths: `C:\Full\Path\To\File.java`
- Or relative from bin folder: `..\..\SampleA.java`
- Check file permissions

### Compilation errors
- Make sure all .java files are in correct folders:
  - `src\Main.java`
  - `src\similarity\*.java`
  - `src\algo\*.java`
- Use UTF-8 encoding flag: `-encoding UTF-8`

### Out of memory (many/large files)
```batch
cd bin
java -Xmx2g Project2_java.src.Main
```

### No previous reports showing
- Reports are saved in current directory
- Check where you ran the program from
- Look for `plagiarism_report_*.txt` files

## 📁 Project Structure

```
Plagarism_Detection_Tool\
├── SampleA.java                    (Sample test files)
├── SampleB.java
├── Project2_java\
│   ├── run.bat                     (Quick run script)
│   ├── USER_GUIDE.md              (Detailed guide)
│   ├── QUICK_START.md             (This file)
│   ├── bin\                       (Compiled classes)
│   └── src\
│       ├── Main.java              (Enhanced main program)
│       ├── algo\                  (Algorithm implementations)
│       │   ├── EditDistance.java
│       │   ├── KMP.java
│       │   ├── NGram.java
│       │   ├── RabinKarp.java
│       │   ├── SuffixArray.java
│       │   └── Trie.java
│       └── similarity\            (Facade and scores)
│           ├── SimilarityFacade.java
│           └── SimilarityScores.java
└── plagiarism_report_*.txt        (Generated reports)
```

## ✨ What Changed from Original?

### Before:
- Command-line arguments only
- Only 2 files at a time
- No line-by-line analysis
- No logging
- Basic console output

### After:
- Interactive menu system
- Multiple files (2+)
- Line-by-line plagiarism detection
- Automatic logging to timestamped files
- Professional console interface
- View previous reports
- Suspicious pair highlighting
- Progress indicators
- Comprehensive reports

## 🎓 Use Cases

1. **Lab Exam Grading**
   - Quick check of all submissions
   - Identify potential cheating
   - Document findings

2. **Assignment Verification**
   - Regular homework checks
   - Track similarity trends
   - Early detection

3. **Code Review**
   - Compare different versions
   - Find duplicate code
   - Refactoring opportunities

4. **Academic Integrity**
   - Evidence collection
   - Pattern analysis
   - Fair assessment

## 📞 Next Steps

1. ✅ Tool is ready to use
2. ✅ Test with sample files (SampleA.java, SampleB.java)
3. ✅ Review generated reports
4. ✅ Adjust thresholds if needed
5. ✅ Start checking real submissions!

---

**Ready to detect plagiarism? Run the tool and type 1! 🚀**
