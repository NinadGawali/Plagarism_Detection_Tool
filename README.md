# Code Plagiarism Detection Tool - Lab Exam Edition

## 📌 Project Overview

This is a **professional plagiarism detection system** for lab exams and coding assignments, using **5 advanced string matching algorithms** in Java with **detailed line-by-line analysis**.

**Perfect for professors and teaching assistants** to quickly check multiple student submissions and identify potential cheating cases with **exact line numbers and plagiarism percentages**.

### ✨ Key Features

- 🖥️ **Interactive Console Interface** - Professional menu-driven system
- 📁 **Multiple File Support** - Compare 2+ files simultaneously (not just pairs)
- 🔍 **Line-by-Line Detection** - Shows **exact line numbers** where plagiarism occurs
- 📊 **Plagiarism Percentages** - Quantifies how much of each file is copied (e.g., "65.7% plagiarized")
- 🎯 **Code Block Detection** - Identifies consecutive similar sections with locations
- 📝 **Comprehensive Reports** - Detailed reports with line matches, content previews, and statistics
- ⚠️ **Smart Flagging** - Automatically highlights suspicious pairs (≥75% similarity)
- 🧪 **8 Test Samples** - Ready-to-use test files with varying similarity levels
- 🚀 **Fast Processing** - Handles entire class submissions in minutes

### 🆕 NEW in Latest Version:
- ✅ **Exact line numbers** for all plagiarized code
- ✅ **Plagiarism percentage** per file (matched lines / total lines)
- ✅ **Similar code blocks** with start/end lines and similarity scores
- ✅ **Content preview** of matched lines in reports
- ✅ **Enhanced reports** with executive summary and rankings
- ✅ **8 test sample files** for validation

### Ideal For:
- Lab exam plagiarism checking with evidence
- Assignment similarity detection with line numbers
- Academic integrity investigations with detailed reports
- Code submission analysis with quantifiable metrics
- Teaching assistant grading workflow with clear documentation

---

## 🧠 Algorithms Used


## 🧠 Algorithms Used

The system employs 5 different algorithms for comprehensive similarity detection:

| Algorithm | Purpose | Score Range |
|-----------|---------|-------------|
| **N-gram Jaccard Similarity** | Structural similarity (token-level) | 0.0 - 1.0 |
| **Edit Distance (Normalized)** | Character-level changes | 0.0 - 1.0 |
| **KMP (Knuth–Morris–Pratt)** | Exact substring matching | 0.0 or 1.0 |
| **Rabin–Karp** | Hash-based pattern detection | 0.0 or 1.0 |
| **Suffix Array** | Longest common substring | 0.0 or 1.0 |

**How It Works:**
- **Pattern Matching** (KMP, Rabin-Karp, Suffix Array) detect exact copied blocks
- **Similarity Metrics** (N-gram, Edit Distance) catch obfuscated plagiarism
- **Average Score >= 75%** = Automatically flagged as suspicious
- **Line-by-line analysis** (70% threshold) shows exactly which lines were copied
- **Code block detection** groups consecutive similar lines for easy review

### 🆕 Enhanced Detection:
- Compares each line pair using edit distance
- Calculates plagiarism percentage: (matched lines / total lines) × 100
- Groups consecutive matches into blocks
- Provides content preview of matched lines
- Generates comprehensive reports with line numbers

---

## 🚀 Quick Start

### Option 1: Quick Test with Samples (Fastest!)
```batch
cd Project2_java
quick_test.bat
```
This will test the tool with sample files showing varying similarity levels.

### Option 2: Using the Run Batch File
```batch
cd Project2_java
run.bat
```

### Option 3: Manual Compilation
```batch
cd Project2_java
javac -encoding UTF-8 -d bin src\Main.java src\similarity\*.java src\algo\*.java
cd bin
java Project2_java.src.Main
```

### 🧪 Test Sample Files
The tool includes **8 test files** for validation:

| File Pair | Topic | Expected Similarity |
|-----------|-------|---------------------|
| StudentCode1 + 2 | Factorial | **~85%** 🔴 High |
| StudentCode4 + 5 | Sorting | **~88%** 🔴 High |
| StudentCode7 + 8 | Fibonacci | **~90%** 🔴 High |
| StudentCode1 + 3 | Factorial | **~45%** 🟡 Moderate |
| StudentCode1 + 6 | Different topics | **~10%** 🟢 Low |

**To test:** Enter `../StudentCode1.java` and `../StudentCode2.java` (from bin directory)

---

## 💻 How to Use

1. **Start the tool** - You'll see a professional welcome screen
2. **Select Option 1** - Run Plagiarism Check
3. **Enter file paths** - One per line (absolute or relative paths)
4. **Type 'done'** when finished
5. **Review results** - Console shows all comparisons
6. **Check the log file** - Automatically saved with timestamp

### Example Session

```
File path: C:\Lab\Student1.java
  [OK] Added: C:\Lab\Student1.java

File path: C:\Lab\Student2.java
  [OK] Added: C:\Lab\Student2.java

File path: C:\Lab\Student3.java
  [OK] Added: C:\Lab\Student3.java

File path: done

Files to compare:
  1. C:\Lab\Student1.java
  2. C:\Lab\Student2.java
  3. C:\Lab\Student3.java

Proceed with comparison? (y/n): y
```

---

## 📊 Sample Output

### Console Output (Enhanced)
```
============================================================
PLAGIARISM DETECTION RESULTS
============================================================

Total Comparisons: 3
Suspicious Pairs: 1 (Similarity >= 75%)

------------------------------------------------------------

[Comparison 1] [!] SUSPICIOUS - POSSIBLE PLAGIARISM
File A: StudentCode1.java
File B: StudentCode2.java

  N-gram Jaccard       : 0.856 (85.6%)
  KMP sample match     : 1.000
  Rabin-Karp sample    : 1.000
  Suffix-array sample  : 1.000
  Edit-distance (norm) : 0.892 (89.2%)
  >> AVERAGE SIMILARITY : 0.850 (85.0%) [!]

  PLAGIARISM ANALYSIS:
    File A: 23/35 lines matched (65.7% plagiarized)
    File B: 23/35 lines matched (65.7% plagiarized)
    Total similar line pairs found: 23

  SIMILAR CODE BLOCKS:
    • Lines 8-15 (File A) ↔ Lines 8-15 (File B)
      8 consecutive lines, avg 92.3% similar
    • Lines 18-27 (File A) ↔ Lines 18-27 (File B)
      10 consecutive lines, avg 89.1% similar
------------------------------------------------------------

[SUCCESS] Detailed report saved to: plagiarism_report_20260202_143022.txt
```

### Key Output Features:
- ✅ **Exact line numbers** showing where plagiarism occurs
- ✅ **Plagiarism percentage** (65.7% of file is copied)
- ✅ **Matched line count** (23 out of 35 lines)
- ✅ **Code block details** (location, size, similarity)
- ✅ **Clear visual indicators** ([!] for suspicious pairs)

### Report File Format
All results are automatically saved to `plagiarism_report_YYYYMMDD_HHMMSS.txt`

---

## ⚙️ Requirements

- **Java JDK 8 or above**
- Windows Command Prompt / PowerShell / Terminal

Check Java version:
```bash
java -version
```

---

## 📚 Documentation

- **[QUICK_START.md](Project2_java/QUICK_START.md)** - Complete setup and usage guide
- **[USER_GUIDE.md](Project2_java/USER_GUIDE.md)** - Detailed features and configuration
- **[EXAMPLE_REPORT.md](Project2_java/EXAMPLE_REPORT.md)** - Sample outputs and interpretation

---

## 🎯 Key Features Explained

### 1. Multiple File Support
- Compare 2, 5, 10, or more files at once
- Automatic pairwise comparisons
- Progress indicator shows analysis status

### 2. Line-by-Line Detection
- Identifies exact code sections that match
- Shows line ranges in both files
- Helps pinpoint what was copied

### 3. Automatic Flagging
- Pairs with >= 75% similarity marked as **SUSPICIOUS**
- Visual warnings in console
- Sorted results (most similar first)

### 4. Comprehensive Logging
- Timestamped report files
- Complete metrics for each comparison
- Summary statistics
- View previous reports from menu

### 5. Professional Interface
- Clean menu system
- Real-time progress updates
- Clear status messages
- Interactive file input

---

## 🔧 Configuration

You can adjust detection thresholds in `Main.java`:

```java
private static final double SIMILARITY_THRESHOLD = 0.75;        // 75%
private static final double LINE_SIMILARITY_THRESHOLD = 0.70;   // 70%
```

**After changes, recompile:**
```batch
cd Project2_java
javac -encoding UTF-8 -d bin src\Main.java src\similarity\*.java src\algo\*.java
```

---

## 📊 Understanding Results

### Similarity Score Interpretation

| Average Score | Status | Meaning |
|--------------|--------|---------|
| 0% - 50% | ✅ Normal | Expected variation |
| 50% - 74% | ⚠️ Medium | Review recommended |
| 75% - 100% | 🚨 SUSPICIOUS | Likely plagiarism |

### Algorithm Interpretation

- **High Edit Distance + N-gram** → Plagiarism with minor changes
- **High KMP/Rabin-Karp/Suffix** → Exact copy detected
- **High Edit + Low Pattern Match** → Variable renaming, reformatting

---

## 📂 Project Structure

```
Plagarism_Detection_Tool\
├── README.md                           # This file
├── SampleA.java                        # Test file 1
├── SampleB.java                        # Test file 2
└── Project2_java\
    ├── run.bat                         # Quick run script
    ├── QUICK_START.md                  # Quick start guide
    ├── USER_GUIDE.md                   # Detailed manual
    ├── EXAMPLE_REPORT.md               # Sample output
    ├── bin\                            # Compiled classes
    └── src\
        ├── Main.java                   # Main program (Enhanced)
        ├── algo\                       # Algorithm implementations
        │   ├── EditDistance.java
        │   ├── KMP.java
        │   ├── NGram.java
        │   ├── RabinKarp.java
        │   ├── SuffixArray.java
        │   └── Trie.java
        └── similarity\                 # Facade classes
            ├── SimilarityFacade.java
            └── SimilarityScores.java
```

---

## 🎓 Use Cases

### 1. Lab Exam Checking
**Scenario:** 30 students, 1-hour coding exam

**Process:**
1. Collect all 30 submissions
2. Run the tool
3. Enter all file paths (or drag and drop)
4. Get 435 comparisons in ~2 minutes
5. Focus on the 5-10 suspicious pairs
6. Review line-by-line details
7. Interview students if needed

**Result:** Save 8-10 hours of manual checking!

### 2. Weekly Assignment Grading
- Quick similarity check before detailed grading
- Early warning system for repeat offenders
- Track patterns across multiple assignments

### 3. Final Project Verification
- Compare with previous years' submissions
- Check against online code repositories
- Verify group work is properly collaborative

---

## 🚀 What's New in This Version?

### Before (Original)
- ❌ Command-line only (no menu)
- ❌ Only 2 files at a time
- ❌ No line-by-line analysis
- ❌ No logging
- ❌ Basic text output
- ❌ Manual result interpretation

### After (Enhanced)
- ✅ Professional console interface
- ✅ Multiple files (2+)
- ✅ Line-by-line plagiarism detection
- ✅ Automatic timestamped logging
- ✅ Suspicious pair auto-flagging
- ✅ View previous reports
- ✅ Progress indicators
- ✅ Comprehensive documentation

---

## 🛠️ Troubleshooting

### File Not Found
```
[ERROR] File not found: submissions/test.java
```
**Solution:** Use absolute paths or check the file exists

### Compilation Errors
**Solution:** Ensure UTF-8 encoding:
```batch
javac -encoding UTF-8 -d bin src\Main.java src\similarity\*.java src\algo\*.java
```

### Out of Memory (Large Files)
**Solution:** Increase heap size:
```batch
java -Xmx2g Project2_java.src.Main
```

---

## 📝 License

This project is open source and available for educational purposes.

---

## 👨‍💻 Contributing

This is a lab exam tool. Feel free to:
- Report bugs
- Suggest features
- Improve algorithms
- Add support for other languages (Python, C++, etc.)

---

## 🎯 Quick Reference

| Task | Command |
|------|---------|
| Compile | `cd Project2_java && javac -encoding UTF-8 -d bin src\Main.java src\similarity\*.java src\algo\*.java` |
| Run | `cd bin && java Project2_java.src.Main` |
| Quick Run | `cd Project2_java && run.bat` |

---

## 📞 Support

For detailed usage instructions, see:
- [QUICK_START.md](Project2_java/QUICK_START.md) - Get started in 5 minutes
- [USER_GUIDE.md](Project2_java/USER_GUIDE.md) - Complete feature documentation
- [EXAMPLE_REPORT.md](Project2_java/EXAMPLE_REPORT.md) - Sample outputs

---

**Ready to catch plagiarism? Run `run.bat` and start checking! 🚀**