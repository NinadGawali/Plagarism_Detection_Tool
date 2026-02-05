# Plagiarism Detection Tool - What's New! 🎯

## ✨ Major New Features

### 📍 **Line-by-Line Detection**
Now you can see **EXACTLY** where plagiarism occurs!

**Before:**
```
StudentCode1.java vs StudentCode2.java: 85% similar
```

**Now:**
```
StudentCode1.java vs StudentCode2.java: 85% similar

PLAGIARISM ANALYSIS:
  File A: 23/35 lines matched (65.7% plagiarized)
  File B: 23/35 lines matched (65.7% plagiarized)

SIMILAR CODE BLOCKS:
  • Lines 8-15 (File A) ↔ Lines 8-15 (File B)
    8 consecutive lines, avg 92.3% similar
  
  • Lines 18-27 (File A) ↔ Lines 18-27 (File B)
    10 consecutive lines, avg 89.1% similar
```

### 📊 **Plagiarism Percentages**
Clear, quantifiable metrics for each file:
- **65.7% plagiarized** means over half the file is copied
- **23 out of 35 lines** matched
- **Average 91.2% similarity** on matched lines

### 📝 **Detailed Reports**
Comprehensive reports with:
- Executive summary ranking pairs by risk
- Line-by-line match details
- Code block locations
- Content previews
- Statistics and percentages

### 🧪 **8 Test Sample Files**
Ready-to-use test files with known similarity levels:
- **High Similarity (>80%)**: StudentCode1+2, 4+5, 7+8
- **Moderate (40-75%)**: StudentCode1+3
- **Low (<40%)**: Cross-topic comparisons

## 📖 Quick Start Guide

### 1. **Compile the Tool**
```bash
cd Project2_java
javac -d bin src/**/*.java
```

### 2. **Run the Tool**
```bash
cd bin
java Project2_java.src.Main
```

### 3. **Test with Samples**
When prompted, enter:
```
..\StudentCode1.java
..\StudentCode2.java
..\StudentCode4.java
..\StudentCode5.java
done
```

### 4. **Review Results**
- Console shows summary
- Detailed report saved as `plagiarism_report_YYYYMMDD_HHMMSS.txt`

## 🎯 Understanding the Output

### Console Output Example:
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
```

### What This Means:
- ✅ **Overall Similarity: 85%** - Very high, indicates plagiarism
- ✅ **65.7% plagiarized** - About 2/3 of each file is copied
- ✅ **23 line pairs matched** - Specific evidence
- ✅ **2 code blocks found** - Consecutive copied sections
- ✅ **Lines 8-15 and 18-27** - Exact locations

## 📋 Report File Structure

### Section 1: Executive Summary
Lists all suspicious pairs ranked by similarity

### Section 2: Detailed Analysis
For each comparison:
1. **File Information**
   - Total lines
   - Matched lines
   - Plagiarism percentage

2. **Similarity Metrics**
   - All 5 algorithm scores
   - Overall average

3. **Line-by-Line Analysis**
   - Similar code blocks
   - Line-by-line matches
   - Content previews

### Example Report Snippet:
```
COMPARISON #1: StudentCode1.java vs StudentCode2.java
STATUS: [!] SUSPICIOUS - POSSIBLE PLAGIARISM DETECTED [!]

FILE INFORMATION:
File A: C:\...\StudentCode1.java
  Total Lines: 35
  Matched Lines: 23 (65.7% of file)

File B: C:\...\StudentCode2.java
  Total Lines: 35
  Matched Lines: 23 (65.7% of file)

SIMILARITY METRICS:
  >> OVERALL AVERAGE SIMILARITY: 0.850 (85.0%) [ALERT!]

LINE-BY-LINE PLAGIARISM ANALYSIS:
Total Similar Line Pairs: 23
Average Line Similarity: 91.2%

SIMILAR CODE BLOCKS DETECTED:

  Block #1:
    Location: Lines 8-15 (File A) ↔ Lines 8-15 (File B)
    Size: 8 consecutive lines
    Average Similarity: 92.3%

ALL SIMILAR LINE PAIRS:
  [   8] ↔ [   8] |  95.2% | for (int i = 1; i <= 10; i++)
  [   9] ↔ [   9] |  98.1% | System.out.println(i + "! = ...
  [  10] ↔ [  10] |  100%  | }
  ...
```

## 🔍 How to Interpret Results

### Similarity Levels:
| Range | Status | Action |
|-------|--------|--------|
| **>75%** | 🔴 HIGH RISK | Investigate - Likely plagiarism |
| **50-75%** | 🟡 MODERATE | Review carefully |
| **<50%** | 🟢 LOW | Likely acceptable |

### Plagiarism Percentage:
| Range | Severity | Description |
|-------|----------|-------------|
| **>70%** | 🔴 Severe | Most of file is copied |
| **50-70%** | 🟡 Significant | Large portions copied |
| **30-50%** | 🟠 Notable | Some sections copied |
| **<30%** | 🟢 Minor | Common patterns only |

### Code Blocks:
- **>10 consecutive lines**: Very suspicious
- **5-10 lines**: Investigate context
- **2-4 lines**: May be common patterns

## 🧪 Sample Test Results

### Test 1: StudentCode1 vs StudentCode2 (Factorial)
```
Expected: HIGH SIMILARITY (~85%)
Reason: Nearly identical implementations, just renamed variables
Lines matched: ~23/35 (65.7%)
```

### Test 2: StudentCode4 vs StudentCode5 (Sorting)
```
Expected: HIGH SIMILARITY (~88%)
Reason: Same bubble sort algorithm, renamed variables
Lines matched: ~28/42 (66.7%)
```

### Test 3: StudentCode1 vs StudentCode3 (Different Factorials)
```
Expected: MODERATE SIMILARITY (~45%)
Reason: Same concept, different implementation approach
Lines matched: ~12/35 (34.3%)
```

### Test 4: StudentCode1 vs StudentCode6 (Different Topics)
```
Expected: LOW SIMILARITY (~10%)
Reason: Completely different functionality
Lines matched: ~2/35 (5.7%)
```

## 📁 New Files Created

### Test Samples:
- `StudentCode1.java` - Alice's factorial (35 lines)
- `StudentCode2.java` - Bob's factorial (35 lines) - **Similar to #1**
- `StudentCode3.java` - Charlie's factorial (42 lines) - **Different approach**
- `StudentCode4.java` - David's sorting (68 lines)
- `StudentCode5.java` - Eve's sorting (68 lines) - **Similar to #4**
- `StudentCode6.java` - Frank's string ops (48 lines)
- `StudentCode7.java` - Grace's Fibonacci (38 lines)
- `StudentCode8.java` - Henry's Fibonacci (38 lines) - **Similar to #7**

### New Components:
- `Project2_java/src/similarity/LineMatchDetail.java` - Line matching class
- `Project2_java/quick_test.bat` - Quick test script
- `SAMPLE_TEST_GUIDE.md` - Comprehensive guide
- `ENHANCEMENTS.md` - Technical documentation
- `WHATS_NEW.md` - This file

## 🚀 Getting Started (3 Steps)

### Step 1: Compile
```bash
cd Project2_java
javac -d bin src/**/*.java
```

### Step 2: Run
```bash
cd bin
java Project2_java.src.Main
```

### Step 3: Test
Select option 1, then enter:
```
..\StudentCode1.java
..\StudentCode2.java
done
```

## 💡 Tips

1. **Compare 3+ files** for comprehensive analysis
2. **Check the detailed report** for full evidence
3. **Focus on code blocks >5 lines** with >80% similarity
4. **Consider context** - some similarity is normal (imports, syntax)
5. **Use percentages** for clear reporting to students

## 📞 Need Help?

Check these files:
- `SAMPLE_TEST_GUIDE.md` - Usage guide
- `ENHANCEMENTS.md` - Technical details
- `QUICK_START.md` - Quick reference

## ✅ What You Get

✅ **Exact line numbers** of plagiarized code  
✅ **Plagiarism percentages** per file  
✅ **Similar code blocks** with locations  
✅ **Comprehensive reports** with evidence  
✅ **Multiple test samples** for validation  
✅ **Professional output** for academic use  

---

**Ready to detect plagiarism with precision! 🎯**

Start with: `cd Project2_java && quick_test.bat`
