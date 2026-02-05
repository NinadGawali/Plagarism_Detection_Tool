# 🚀 Quick Reference Card

## One-Command Test
```bash
cd Project2_java && quick_test.bat
```

## Manual Testing (3 Steps)
```bash
# 1. Compile
cd Project2_java
javac -d bin src/**/*.java

# 2. Run  
cd bin
java Project2_java.src.Main

# 3. Enter when prompted:
..\StudentCode1.java
..\StudentCode2.java
done
```

## Test File Pairs

| Files | Similarity | What to Expect |
|-------|------------|----------------|
| **StudentCode1 + 2** | 🔴 **~85%** | High similarity - Factorial implementations |
| **StudentCode4 + 5** | 🔴 **~88%** | High similarity - Sorting algorithms |
| **StudentCode7 + 8** | 🔴 **~90%** | High similarity - Fibonacci sequences |
| **StudentCode1 + 3** | 🟡 **~45%** | Moderate - Different factorial approaches |
| **StudentCode1 + 6** | 🟢 **~10%** | Low - Different topics |

## Key Metrics

### Overall Similarity
- **>75%** 🔴 HIGH RISK - Likely plagiarism
- **50-75%** 🟡 MODERATE - Investigate
- **<50%** 🟢 LOW - Acceptable

### Plagiarism Percentage
- **>70%** 🔴 Severe - Most of file copied
- **50-70%** 🟡 Significant - Large portions copied  
- **30-50%** 🟠 Notable - Some sections copied
- **<30%** 🟢 Minor - Common patterns only

### Code Blocks
- **>10 lines** = Very suspicious
- **5-10 lines** = Investigate context
- **2-4 lines** = May be common patterns

## What You Get

### Console Shows:
✅ Overall similarity percentage  
✅ Plagiarism % per file  
✅ Matched lines count (X/Y)  
✅ Similar code blocks with locations  
✅ Block sizes and averages  

### Report File Contains:
✅ Executive summary with rankings  
✅ All algorithm scores  
✅ Line-by-line match details  
✅ Code block locations  
✅ Content previews  
✅ Individual similarity scores  

## Sample Output
```
[Comparison 1] [!] SUSPICIOUS - POSSIBLE PLAGIARISM

File A: StudentCode1.java
File B: StudentCode2.java

>> AVERAGE SIMILARITY : 0.850 (85.0%) [!]

PLAGIARISM ANALYSIS:
  File A: 23/35 lines matched (65.7% plagiarized)
  File B: 23/35 lines matched (65.7% plagiarized)

SIMILAR CODE BLOCKS:
  • Lines 8-15 (File A) ↔ Lines 8-15 (File B)
    8 consecutive lines, avg 92.3% similar
```

## Files Created

### Test Samples (8):
- StudentCode1-8.java

### Components:
- LineMatchDetail.java
- Enhanced Main.java

### Documentation (5):
- WHATS_NEW.md - Feature overview
- ENHANCEMENTS.md - Technical details
- SAMPLE_TEST_GUIDE.md - Full guide
- SUMMARY.md - Complete summary
- BEFORE_AFTER.md - Comparison
- QUICK_REFERENCE.md - This card

## Troubleshooting

**"Class not found"**
→ Recompile from Project2_java directory

**"File not found"**
→ Use `..\\` prefix for files outside bin directory

**No matches detected**
→ Files are genuinely different (expected for some pairs)

## Documentation

📖 **Start here:** WHATS_NEW.md  
📖 **Full guide:** SAMPLE_TEST_GUIDE.md  
📖 **Comparison:** BEFORE_AFTER.md  
📖 **Technical:** ENHANCEMENTS.md  
📖 **Summary:** SUMMARY.md  

---

**Your tool is ready! Start with quick_test.bat** 🎯
