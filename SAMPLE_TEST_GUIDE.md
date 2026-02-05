# Enhanced Plagiarism Detection Tool

## New Features

### 1. **Comprehensive Sample Files**
8 diverse Java sample files have been created for testing:
- **StudentCode1.java** & **StudentCode2.java** - Similar factorial implementations (High Similarity)
- **StudentCode3.java** - Different factorial implementation (Moderate Similarity)
- **StudentCode4.java** & **StudentCode5.java** - Similar array sorting implementations (High Similarity)
- **StudentCode6.java** - String manipulation (Different)
- **StudentCode7.java** & **StudentCode8.java** - Similar Fibonacci implementations (High Similarity)

### 2. **Detailed Line-by-Line Plagiarism Detection**
The tool now provides:
- **Exact line numbers** where plagiarism is detected
- **Plagiarism percentage** for each file
- **Line-by-line similarity scores**
- **Detection of consecutive similar code blocks**
- **Content preview** of matched lines

### 3. **Enhanced Reporting**

#### Console Output Now Shows:
- Overall similarity percentage
- Plagiarism percentage per file (X% of file is plagiarized)
- Number of matched lines vs total lines
- Similar code blocks with line ranges
- Average similarity for each block

#### Detailed Report File Includes:
- **Executive Summary**: High-level overview of all comparisons
- **Detection Parameters**: Thresholds and settings used
- **File Information**: Total lines and matched lines per file
- **Similarity Metrics**: All algorithm scores
- **Line-by-Line Analysis**:
  - Similar code blocks with exact line ranges
  - Individual line matches with similarity percentages
  - Content preview of matched lines
- **Comprehensive Statistics**: Percentages and counts

### 4. **New Detection Capabilities**

#### Plagiarism Percentage Calculation:
```
Plagiarism % = (Matched Lines / Total Lines) × 100
```

#### Code Block Detection:
- Identifies consecutive similar lines
- Groups them into blocks
- Reports block location, size, and average similarity

#### Line Matching:
- Compares each non-comment, non-empty line
- Uses edit distance for similarity calculation
- Threshold: 70% similarity for line matches

## How to Use

### Quick Test with Sample Files:

1. **Compile the project:**
```bash
cd Project2_java
javac -d bin src/**/*.java
```

2. **Run the tool:**
```bash
cd bin
java Project2_java.src.Main
```

3. **Select Option 1** (Run Plagiarism Check)

4. **Enter file paths** (use these test files):
```
../StudentCode1.java
../StudentCode2.java
../StudentCode3.java
done
```

### Expected Output Example:

```
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
```

### Detailed Report:

After each run, a comprehensive report is saved as:
```
plagiarism_report_YYYYMMDD_HHMMSS.txt
```

This report includes:
- Executive summary with high-risk pairs
- Detailed analysis for each comparison
- Line-by-line matches with content previews
- Code block locations and statistics

## Test Scenarios

### High Similarity (>75%):
- StudentCode1.java vs StudentCode2.java (~85%)
- StudentCode4.java vs StudentCode5.java (~88%)
- StudentCode7.java vs StudentCode8.java (~90%)

### Moderate Similarity (40-75%):
- StudentCode1.java vs StudentCode3.java (~45%)
- StudentCode2.java vs StudentCode3.java (~45%)

### Low Similarity (<40%):
- StudentCode1.java vs StudentCode4.java (~15%)
- StudentCode1.java vs StudentCode6.java (~10%)

## Report Interpretation

### Plagiarism Levels:
- **>75%**: HIGH RISK - Likely plagiarism
- **50-75%**: MODERATE - Investigate further
- **<50%**: LOW - Likely coincidental or common patterns

### Key Metrics:
1. **Overall Similarity**: Average of all 5 algorithms
2. **Plagiarism %**: Percentage of file that matches another
3. **Line Pairs**: Number of similar line matches found
4. **Block Count**: Number of consecutive similar sections

## Technical Details

### Algorithms Used:
1. **N-gram Jaccard Similarity**: Structural similarity
2. **KMP Pattern Matching**: Exact substring matching
3. **Rabin-Karp**: Efficient pattern matching
4. **Suffix Array**: Advanced substring search
5. **Edit Distance**: Character-level similarity

### Thresholds:
- Overall Similarity: 75%
- Line-by-Line: 70%
- Minimum Block Size: 2 consecutive lines

## Files Modified/Created

### New Files:
- `StudentCode1.java` through `StudentCode8.java` - Test samples
- `Project2_java/src/similarity/LineMatchDetail.java` - Line matching class
- `SAMPLE_TEST_GUIDE.md` - This guide

### Enhanced Files:
- `Project2_java/src/Main.java` - Major enhancements for detailed reporting

## Tips for Best Results

1. **Use multiple test files**: Compare 3+ files for comprehensive analysis
2. **Check the detailed report**: Console output is summarized; report has full details
3. **Review code blocks**: Focus on blocks with >80% similarity and >5 lines
4. **Consider context**: Some similarity is expected (imports, basic syntax)

## Example Command Sequence

```
Enter file paths (one per line).
Type 'done' when finished:

File path: ../StudentCode1.java
  [OK] Added: ../StudentCode1.java

File path: ../StudentCode2.java
  [OK] Added: ../StudentCode2.java

File path: ../StudentCode4.java
  [OK] Added: ../StudentCode4.java

File path: ../StudentCode5.java
  [OK] Added: ../StudentCode5.java

File path: done
```

This will generate 6 comparisons (all pairs) with detailed analysis for each.

## Troubleshooting

**Issue**: "File not found"
- Use absolute paths or paths relative to the `bin` directory
- Verify files exist with `ls` or `dir`

**Issue**: "Class not found"
- Recompile: `javac -d bin src/**/*.java`
- Run from `bin` directory

**Issue**: No similar blocks detected
- Files may be genuinely different
- Check that line similarity threshold (70%) isn't too high
- Verify files contain actual code (not just comments)

---

**Enjoy comprehensive plagiarism detection with detailed line-by-line analysis!**
