# Plagiarism Detection Tool - User Guide

## Overview
This enhanced plagiarism detection tool allows you to compare multiple student code submissions simultaneously and identify potential plagiarism cases.

## Features

### 1. **Multiple File Comparison**
- Compare 2 or more files in a single run
- Performs pairwise comparisons between all submitted files
- Shows progress during analysis

### 2. **Advanced Similarity Detection**
The tool uses 5 different algorithms:
- **N-gram Jaccard Similarity**: Measures structural similarity
- **KMP Pattern Matching**: Detects exact pattern occurrences
- **Rabin-Karp Algorithm**: Fast substring searching
- **Suffix Array Search**: Efficient pattern matching
- **Edit Distance**: Normalized Levenshtein distance

### 3. **Line-by-Line Analysis**
- For suspicious pairs (≥75% similarity), the tool identifies specific line ranges
- Shows which sections of code are highly similar
- Helps pinpoint exact plagiarized segments

### 4. **Automatic Logging**
- All results are automatically saved to timestamped text files
- Reports include:
  - Detailed similarity metrics
  - Suspicious pair identification
  - Line-by-line comparison results
  - Summary statistics

### 5. **Professional Console Interface**
- Welcome screen with ASCII art
- Interactive menu system
- View previous reports
- Color-coded warnings for suspicious cases

## How to Use

### Compile the Project
```bash
cd Project2_java
javac -d bin src/Main.java src/similarity/*.java src/algo/*.java
```

### Run the Tool
```bash
cd bin
java Project2_java.src.Main
```

### Using the Menu

**Option 1: Run Plagiarism Check**
1. Select option 1 from the main menu
2. Enter file paths one by one (absolute or relative paths)
3. Type "done" when you've entered all files
4. Confirm to proceed with the analysis
5. View results on screen
6. Results are automatically saved to a log file

**Example file entry:**
```
File path: ../SampleA.java
File path: ../SampleB.java
File path: C:\Users\Student1\Submission.java
File path: done
```

**Option 2: View Previous Reports**
- Lists all previously generated reports
- Select a report number to view its contents

**Option 3: Exit**
- Closes the application

## Understanding Results

### Similarity Metrics
- **0.0 - 0.5**: Low similarity (Normal)
- **0.5 - 0.75**: Medium similarity (Review recommended)
- **0.75 - 1.0**: High similarity (⚠ SUSPICIOUS - Possible plagiarism)

### Report Files
Reports are saved as: `plagiarism_report_YYYYMMDD_HHMMSS.txt`

Each report includes:
- Timestamp
- List of analyzed files
- Detailed comparison results for each pair
- Similar line ranges for suspicious pairs
- Summary statistics

## Thresholds (Configurable in Main.java)

```java
SIMILARITY_THRESHOLD = 0.75        // 75% - marks pairs as suspicious
LINE_SIMILARITY_THRESHOLD = 0.70   // 70% - for line-by-line detection
```

## Tips for Best Results

1. **File Naming**: Use descriptive names for student submissions (e.g., `StudentName_Assignment.java`)
2. **Batch Processing**: You can check an entire class's submissions in one run
3. **Review Process**: 
   - Focus on pairs marked as "SUSPICIOUS"
   - Check the similar line ranges to understand what code was copied
   - Review original submissions manually for final confirmation

## Example Workflow

**Lab Exam Scenario:**
1. Collect all student submissions in a folder
2. Run the plagiarism detection tool
3. Enter all submission file paths
4. Review the console output for suspicious pairs
5. Open the generated report file for detailed analysis
6. Investigate suspicious cases manually
7. Keep the report as documentation

## Output Example

```
═══════════════════════════════════════════════════════════
PLAGIARISM DETECTION RESULTS
═══════════════════════════════════════════════════════════

Total Comparisons: 10
Suspicious Pairs: 2 (Similarity >= 75%)

────────────────────────────────────────────────────────────

[Comparison 1] ⚠ SUSPICIOUS
File A: Student1.java
File B: Student5.java

  N-gram Jaccard       : 0.892 (89.2%)
  KMP sample match     : 1.000
  Rabin-Karp sample    : 1.000
  Suffix-array sample  : 1.000
  Edit-distance (norm) : 0.856 (85.6%)
  ► AVERAGE SIMILARITY : 0.870 (87.0%) ⚠

  Similar Line Ranges:
    • Lines 15-45 (File A) ↔ Lines 18-48 (File B) - 92.3% similar
    • Lines 67-89 (File A) ↔ Lines 71-93 (File B) - 88.7% similar
```

## Troubleshooting

**File not found errors:**
- Make sure file paths are correct
- Use absolute paths if relative paths don't work
- Check file permissions

**Out of memory errors:**
- Large files may require more heap space
- Run with: `java -Xmx2g Project2_java.src.Main`

**No reports directory:**
- Reports are saved in the current working directory
- Make sure you have write permissions

## Technical Details

- **Language**: Java
- **Algorithms**: N-gram, KMP, Rabin-Karp, Suffix Array, Edit Distance
- **Minimum Files**: 2
- **Maximum Files**: Unlimited (limited by system memory)
- **Supported File Types**: Any text-based code files

## Support

For issues or questions, refer to the source code documentation or contact your system administrator.
