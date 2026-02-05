# Example Report Output

This document shows what a typical plagiarism detection report looks like.

## Console Output Example

```
============================================================
||                                                          ||
||                PLAGIARISM DETECTION TOOL                 ||
||             Lab Exam Code Similarity Checker             ||
||                                                          ||
============================================================
Welcome! This tool helps detect code similarity between
multiple student submissions using advanced algorithms.
============================================================

------------------------------------------------------------
MAIN MENU:
------------------------------------------------------------
1. Run Plagiarism Check
2. View Previous Reports
3. Exit
------------------------------------------------------------
Enter your choice (1-3): 1

============================================================
PLAGIARISM CHECK
============================================================

Enter file paths (one per line).
Type 'done' when finished:
(You can use absolute paths or relative paths from current directory)

File path: submissions/Alice.java
  [OK] Added: submissions/Alice.java

File path: submissions/Bob.java
  [OK] Added: submissions/Bob.java

File path: submissions/Charlie.java
  [OK] Added: submissions/Charlie.java

File path: done

Files to compare:
  1. submissions/Alice.java
  2. submissions/Bob.java
  3. submissions/Charlie.java

Proceed with comparison? (y/n): y

------------------------------------------------------------
ANALYZING FILES...
------------------------------------------------------------
Progress: 3/3 comparisons

Analysis complete!

============================================================
PLAGIARISM DETECTION RESULTS
============================================================

Total Comparisons: 3
Suspicious Pairs: 1 (Similarity >= 75%)

------------------------------------------------------------

[Comparison 1] [!] SUSPICIOUS
File A: Alice.java
File B: Charlie.java

  N-gram Jaccard       : 0.923 (92.3%)
  KMP sample match     : 1.000
  Rabin-Karp sample    : 1.000
  Suffix-array sample  : 1.000
  Edit-distance (norm) : 0.901 (90.1%)
  >> AVERAGE SIMILARITY : 0.965 (96.5%) [!]

  Similar Line Ranges:
    * Lines 5-23 (File A) <-> Lines 5-23 (File B) - 98.5% similar
    * Lines 30-47 (File A) <-> Lines 30-47 (File B) - 95.2% similar
    * Lines 52-68 (File A) <-> Lines 52-68 (File B) - 97.8% similar
------------------------------------------------------------

[Comparison 2]
File A: Alice.java
File B: Bob.java

  N-gram Jaccard       : 0.456 (45.6%)
  KMP sample match     : 0.000
  Rabin-Karp sample    : 0.000
  Suffix-array sample  : 0.000
  Edit-distance (norm) : 0.523 (52.3%)
  >> AVERAGE SIMILARITY : 0.196 (19.6%)
------------------------------------------------------------

[Comparison 3]
File A: Bob.java
File B: Charlie.java

  N-gram Jaccard       : 0.489 (48.9%)
  KMP sample match     : 0.000
  Rabin-Karp sample    : 0.000
  Suffix-array sample  : 0.000
  Edit-distance (norm) : 0.545 (54.5%)
  >> AVERAGE SIMILARITY : 0.207 (20.7%)
------------------------------------------------------------

[SUCCESS] Report saved to: plagiarism_report_20260122_153045.txt
```

## Generated Text File Report

**File: plagiarism_report_20260122_153045.txt**

```
================================================================================
PLAGIARISM DETECTION REPORT
================================================================================
Generated: 2026-01-22 15:30:45

FILES ANALYZED:
--------------------------------------------------------------------------------
1. submissions/Alice.java
2. submissions/Bob.java
3. submissions/Charlie.java

SIMILARITY THRESHOLD: 75.0%
LINE SIMILARITY THRESHOLD: 70.0%

================================================================================
DETAILED COMPARISON RESULTS
================================================================================

[Comparison 1] [!] SUSPICIOUS - POSSIBLE PLAGIARISM
--------------------------------------------------------------------------------
File A: submissions/Alice.java
File B: submissions/Charlie.java

Similarity Metrics:
  N-gram Jaccard       : 0.923 (92.3%)
  KMP sample match     : 1.000
  Rabin-Karp sample    : 1.000
  Suffix-array sample  : 1.000
  Edit-distance (norm) : 0.901 (90.1%)
  >> AVERAGE SIMILARITY : 0.965 (96.5%) [!]

Similar Code Sections:
  * Lines 5-23 (File A) <-> Lines 5-23 (File B) - 98.5% similar
  * Lines 30-47 (File A) <-> Lines 30-47 (File B) - 95.2% similar
  * Lines 52-68 (File A) <-> Lines 52-68 (File B) - 97.8% similar
--------------------------------------------------------------------------------

[Comparison 2] Normal
--------------------------------------------------------------------------------
File A: submissions/Alice.java
File B: submissions/Bob.java

Similarity Metrics:
  N-gram Jaccard       : 0.456 (45.6%)
  KMP sample match     : 0.000
  Rabin-Karp sample    : 0.000
  Suffix-array sample  : 0.000
  Edit-distance (norm) : 0.523 (52.3%)
  >> AVERAGE SIMILARITY : 0.196 (19.6%)
--------------------------------------------------------------------------------

[Comparison 3] Normal
--------------------------------------------------------------------------------
File A: submissions/Bob.java
File B: submissions/Charlie.java

Similarity Metrics:
  N-gram Jaccard       : 0.489 (48.9%)
  KMP sample match     : 0.000
  Rabin-Karp sample    : 0.000
  Suffix-array sample  : 0.000
  Edit-distance (norm) : 0.545 (54.5%)
  >> AVERAGE SIMILARITY : 0.207 (20.7%)
--------------------------------------------------------------------------------

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

## What This Tells You

### Key Findings

1. **Alice and Charlie: SUSPICIOUS (96.5% similarity)**
   - Very high similarity across all metrics
   - Multiple sections with 95%+ line-by-line similarity
   - Lines 5-23, 30-47, and 52-68 are nearly identical
   - **Action Required**: Manual investigation needed

2. **Alice and Bob: Normal (19.6% similarity)**
   - Low similarity
   - No suspicious patterns
   - **Status**: Acceptable variation

3. **Bob and Charlie: Normal (20.7% similarity)**
   - Low similarity
   - No suspicious patterns
   - **Status**: Acceptable variation

### Interpretation

**Likely Scenario:**
- Alice and Charlie may have copied from each other
- Bob's submission appears original
- Specific line ranges identified for manual review

**Next Steps:**
1. Open Alice.java and Charlie.java
2. Compare lines 5-23, 30-47, and 52-68
3. Interview students if necessary
4. Document decision with this report

## Metrics Explained

### N-gram Jaccard (0.923)
- Measures structural similarity
- 92.3% means very similar code structure
- High value = likely copied

### Pattern Matching (KMP, Rabin-Karp, Suffix Array)
- All showing 1.000 = exact pattern found
- First 80 characters of one file found in the other
- Strong indicator of copying

### Edit Distance (0.901)
- Character-level comparison
- 90.1% similar after normalizing for length
- Only ~10% of characters are different

### Average Similarity (0.965)
- Overall score: 96.5%
- Well above 75% threshold
- Flagged as SUSPICIOUS

## Real-World Use Case

**Scenario**: Professor's Lab Exam

**Situation:**
- 30 students, 1-hour lab exam
- Everyone writes a sorting algorithm
- Need to check for cheating

**Process:**
1. Collect all 30 .java files
2. Run plagiarism detection tool
3. Enter all 30 file paths
4. Let tool analyze (30×29/2 = 435 comparisons)
5. Review suspicious pairs only

**Result:**
- 435 comparisons in ~2 minutes
- Maybe 5-10 suspicious pairs
- Focus investigation on those specific cases
- Line-by-line details show exactly what was copied
- Report documents findings for records

**Time Saved:**
- Manual review: 8-10 hours
- Tool + focused review: 1-2 hours
- 80-90% time savings!

---

**This is what you'll see when you run the tool!** 🎯
