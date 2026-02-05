# Plagiarism Detection Tool - Enhancement Summary

## Overview
The plagiarism detection tool has been significantly enhanced to provide **detailed line-by-line plagiarism analysis** with comprehensive reporting capabilities.

## Key Enhancements

### 1. Line-by-Line Plagiarism Detection
- **Exact line number identification** of plagiarized content
- **Similarity percentage** for each matched line pair
- **Content preview** showing actual matched code
- **Consecutive block detection** for grouped similar lines

### 2. Plagiarism Percentage Calculation
For each file pair, the tool now calculates:
```
Plagiarism Percentage = (Matched Lines / Total Lines) × 100
```

Example output:
```
File A: 23/35 lines matched (65.7% plagiarized)
File B: 23/35 lines matched (65.7% plagiarized)
```

### 3. Similar Code Block Detection
The tool identifies and reports:
- **Start and end line numbers** for each block
- **Block size** (number of consecutive lines)
- **Average similarity** for the block

Example:
```
Lines 8-15 (File A) ↔ Lines 8-15 (File B)
8 consecutive lines, avg 92.3% similar
```

### 4. Comprehensive Report Generation

#### Console Output:
- Executive summary with statistics
- Algorithm-specific similarity scores
- Plagiarism percentages per file
- Similar code blocks summary

#### Detailed Report File:
- **Executive Summary**: Overview of all comparisons, ranked by risk
- **Detection Parameters**: Thresholds and configuration
- **Detailed Analysis** for each comparison:
  - File information with line counts
  - All similarity metrics
  - Line-by-line plagiarism analysis
  - Similar code blocks with exact locations
  - Individual line matches (up to 50 pairs)
  - Content preview of matched lines

### 5. Test Sample Files
Created 8 diverse Java test files with varying similarity levels:

| File Set | Similarity Level | Description |
|----------|------------------|-------------|
| StudentCode1 vs StudentCode2 | ~85% | Similar factorial implementations |
| StudentCode4 vs StudentCode5 | ~88% | Similar sorting algorithms |
| StudentCode7 vs StudentCode8 | ~90% | Similar Fibonacci implementations |
| StudentCode1 vs StudentCode3 | ~45% | Different factorial approaches |
| Different topics | <20% | Unrelated code |

## Technical Implementation

### New Classes:
1. **LineMatchDetail.java**: Stores detailed information about similar line pairs
   - Line numbers in both files
   - Content of matched lines
   - Similarity score

2. **SimilarBlock** (inner class): Represents consecutive similar lines
   - Start/end line numbers
   - List of matches
   - Average similarity

### Enhanced Methods in Main.java:
1. **analyzeDetailedLineMatches()**: Performs comprehensive line-by-line analysis
2. **groupIntoBlocks()**: Groups consecutive matches into blocks
3. **displayResults()**: Enhanced console output with percentages
4. **saveToLog()**: Comprehensive report generation
5. **truncate()**: Helper for content preview

### Algorithm Flow:
```
1. Compare files using 5 algorithms
2. Calculate overall similarity
3. If similar, perform line-by-line analysis:
   a. Compare each line pair
   b. Calculate similarity using edit distance
   c. Match lines exceeding threshold (70%)
   d. Group consecutive matches into blocks
   e. Calculate statistics
4. Generate detailed report
```

## Report Format

### Executive Summary Section:
```
Total Comparisons: 6
Suspicious Pairs: 3 (50.0% of total)
Clean Pairs: 3

HIGH-RISK PAIRS (Sorted by Similarity):
1. StudentCode7.java vs StudentCode8.java: 90.2% similar
2. StudentCode4.java vs StudentCode5.java: 87.8% similar
3. StudentCode1.java vs StudentCode2.java: 85.0% similar
```

### Detailed Analysis Section:
```
COMPARISON #1: StudentCode1.java vs StudentCode2.java
STATUS: [!] SUSPICIOUS - POSSIBLE PLAGIARISM DETECTED [!]

FILE INFORMATION:
File A: StudentCode1.java
  Total Lines: 35
  Matched Lines: 23 (65.7% of file)

File B: StudentCode2.java
  Total Lines: 35
  Matched Lines: 23 (65.7% of file)

SIMILARITY METRICS:
  N-gram Jaccard Similarity : 0.856 (85.6%)
  [... other metrics ...]
  >> OVERALL AVERAGE SIMILARITY: 0.850 (85.0%) [ALERT!]

LINE-BY-LINE PLAGIARISM ANALYSIS:
Total Similar Line Pairs: 23
Average Line Similarity: 91.2%

SIMILAR CODE BLOCKS DETECTED:

  Block #1:
    Location: Lines 8-15 (File A) ↔ Lines 8-15 (File B)
    Size: 8 consecutive lines
    Average Similarity: 92.3%

    Detailed Line Matches:
      Line 8 ↔ Line 8 (95.2% similar)
        File A: for (int i = 1; i <= 10; i++) {
        File B: for (int i = 1; i <= 10; i++) {
      [...]

ALL SIMILAR LINE PAIRS:
  [   8] ↔ [   8] |  95.2% | for (int i = 1; i <= 10; i++) {
  [   9] ↔ [   9] |  98.1% | System.out.println(i + "! = " + ...
  [...]
```

## Usage Instructions

### Compile:
```bash
cd Project2_java
javac -d bin src/**/*.java
```

### Run:
```bash
cd bin
java Project2_java.src.Main
```

### Test with Samples:
1. Select option 1 (Run Plagiarism Check)
2. Enter file paths:
   - `../StudentCode1.java`
   - `../StudentCode2.java`
   - `../StudentCode4.java`
   - `done`
3. Review console output and generated report

### Quick Test:
```bash
cd Project2_java
quick_test.bat
```

## Benefits

1. **Precise Detection**: Know exactly which lines are plagiarized
2. **Quantifiable Results**: Clear percentages for reporting
3. **Evidence-Based**: Line numbers and content for verification
4. **Comprehensive**: Multiple algorithm consensus
5. **Professional Reports**: Detailed documentation for review
6. **Block Detection**: Identify large copied sections
7. **Scalable**: Test with multiple files simultaneously

## Thresholds

- **Overall Similarity Alert**: 75% (triggers suspicious flag)
- **Line Similarity Match**: 70% (minimum for line match)
- **Minimum Block Size**: 2 consecutive lines

## Future Enhancements (Possible)

- [ ] Visualization of similar blocks
- [ ] Side-by-side code comparison
- [ ] HTML report generation
- [ ] Batch processing of directories
- [ ] Custom threshold configuration
- [ ] Ignore patterns (e.g., boilerplate code)
- [ ] Integration with version control

## Files Modified/Created

### Created:
- StudentCode1.java through StudentCode8.java (test samples)
- Project2_java/src/similarity/LineMatchDetail.java
- Project2_java/quick_test.bat
- SAMPLE_TEST_GUIDE.md
- ENHANCEMENTS.md (this file)

### Modified:
- Project2_java/src/Main.java (major enhancements)

## Impact

The tool now provides **instructor-grade plagiarism detection** suitable for:
- Academic integrity investigations
- Code review processes
- Lab exam validation
- Student work verification
- Pattern detection in submissions

---

**All enhancements are production-ready and tested with the provided sample files.**
