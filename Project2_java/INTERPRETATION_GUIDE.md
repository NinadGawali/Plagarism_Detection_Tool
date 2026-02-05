# Understanding Plagiarism Detection Results

This guide helps you interpret the results from the Plagiarism Detection Tool.

## The Five Algorithms Explained

### 1. N-gram Jaccard Similarity (0.0 - 1.0)

**What it measures:** Structural similarity based on code tokens

**How it works:**
- Breaks code into overlapping sequences (n-grams)
- Compares the sets of sequences between files
- Calculates Jaccard similarity (intersection / union)

**Example:**
```java
// File A: "int x = 5;"
// File B: "int y = 5;"
// Similar structure, different variable name
// N-gram score: ~0.85 (high)
```

**Interpretation:**
- **0.0 - 0.3:** Very different code structure
- **0.3 - 0.6:** Some structural similarity
- **0.6 - 0.8:** High structural similarity
- **0.8 - 1.0:** Nearly identical structure

**Best for:** Detecting copied code with variable/function renaming

---

### 2. Edit Distance (Normalized) (0.0 - 1.0)

**What it measures:** Character-level similarity

**How it works:**
- Counts minimum edits (insert, delete, replace) to transform one string to another
- Normalized by file length

**Example:**
```java
// File A: "count = count + 1;"
// File B: "counter = counter + 1;"
// Only 2 character insertions ('e', 'r')
// Edit distance score: ~0.92 (very high)
```

**Interpretation:**
- **0.0 - 0.4:** Completely different files
- **0.4 - 0.7:** Some similarity
- **0.7 - 0.9:** High similarity with minor changes
- **0.9 - 1.0:** Nearly identical

**Best for:** Detecting minor edits, formatting changes, or comment additions

---

### 3. KMP (Knuth-Morris-Pratt) (0.0 or 1.0)

**What it measures:** Exact substring presence

**How it works:**
- Takes first 80 characters from File B
- Searches for exact match in File A
- Binary result: found (1.0) or not found (0.0)

**Example:**
```java
// If the opening of File B appears anywhere in File A:
// KMP = 1.0 (exact match found)
// Otherwise:
// KMP = 0.0 (no match)
```

**Interpretation:**
- **0.0:** No exact 80-character block found
- **1.0:** Exact block found → strong evidence of copying

**Best for:** Detecting exact copy-paste plagiarism

---

### 4. Rabin-Karp (0.0 or 1.0)

**What it measures:** Hash-based pattern matching

**How it works:**
- Similar to KMP but uses rolling hash
- Searches for the same 80-character sample
- Binary result

**Example:**
```java
// Same as KMP, but faster for multiple patterns
// Result: 0.0 or 1.0
```

**Interpretation:**
- **0.0:** No exact block found
- **1.0:** Exact block found

**Best for:** Quick detection of exact matches

---

### 5. Suffix Array (0.0 or 1.0)

**What it measures:** Longest common substring

**How it works:**
- Builds suffix array of File A
- Searches for 80-character sample from File B
- Binary result

**Example:**
```java
// Efficient for finding longest shared code segments
// Result: 0.0 or 1.0
```

**Interpretation:**
- **0.0:** No significant common substring
- **1.0:** Large common substring found

**Best for:** Finding longest copied sections

---

## Combined Analysis

### Pattern 1: Exact Copy
```
N-gram Jaccard       : 0.995 (99.5%)
KMP sample match     : 1.000
Rabin-Karp sample    : 1.000
Suffix-array sample  : 1.000
Edit-distance (norm) : 0.998 (99.8%)
>> AVERAGE: 0.998 (99.8%) [!] SUSPICIOUS
```

**Verdict:** Blatant plagiarism - nearly identical files
**Action:** Immediate investigation required

---

### Pattern 2: Copy with Minor Changes
```
N-gram Jaccard       : 0.856 (85.6%)
KMP sample match     : 1.000
Rabin-Karp sample    : 1.000
Suffix-array sample  : 1.000
Edit-distance (norm) : 0.823 (82.3%)
>> AVERAGE: 0.936 (93.6%) [!] SUSPICIOUS
```

**Verdict:** Plagiarism with minor modifications (comments, spacing)
**Action:** Review line-by-line details

---

### Pattern 3: Heavy Variable Renaming
```
N-gram Jaccard       : 0.892 (89.2%)
KMP sample match     : 0.000
Rabin-Karp sample    : 0.000
Suffix-array sample  : 0.000
Edit-distance (norm) : 0.756 (75.6%)
>> AVERAGE: 0.330 (33.0%)
```

**Verdict:** Similar structure but different implementation
**Action:** May be acceptable if logic differs

---

### Pattern 4: Different but Similar Logic
```
N-gram Jaccard       : 0.634 (63.4%)
KMP sample match     : 0.000
Rabin-Karp sample    : 0.000
Suffix-array sample  : 0.000
Edit-distance (norm) : 0.589 (58.9%)
>> AVERAGE: 0.245 (24.5%)
```

**Verdict:** Similar approach but independent work
**Action:** Normal variation, no concern

---

### Pattern 5: Completely Different
```
N-gram Jaccard       : 0.123 (12.3%)
KMP sample match     : 0.000
Rabin-Karp sample    : 0.000
Suffix-array sample  : 0.000
Edit-distance (norm) : 0.234 (23.4%)
>> AVERAGE: 0.071 (7.1%)
```

**Verdict:** Independent work
**Action:** No action needed

---

## Line-by-Line Analysis

When pairs are flagged as suspicious (>= 75%), the tool shows:

```
Similar Line Ranges:
  * Lines 5-23 (File A) <-> Lines 5-23 (File B) - 98.5% similar
  * Lines 30-47 (File A) <-> Lines 30-47 (File B) - 95.2% similar
```

**How to use this:**
1. Open both files side-by-side
2. Navigate to the specified lines
3. Compare the actual code
4. Determine if similarity is acceptable (e.g., template code) or plagiarism

**Example:**

**File A, Lines 5-23:**
```java
public class BubbleSort {
    public static void sort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (arr[j] > arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }
}
```

**File B, Lines 5-23:**
```java
public class BubbleSort {
    public static void sort(int[] array) {  // Changed: arr -> array
        int size = array.length;             // Changed: n -> size
        for (int i = 0; i < size-1; i++) {
            for (int j = 0; j < size-i-1; j++) {
                if (array[j] > array[j+1]) {
                    int tmp = array[j];       // Changed: temp -> tmp
                    array[j] = array[j+1];
                    array[j+1] = tmp;
                }
            }
        }
    }
}
```

**Analysis:**
- Nearly identical logic
- Only variable names changed
- 98.5% similarity is accurate
- **Verdict:** Clear plagiarism

---

## Decision Flow Chart

```
Is Average Similarity >= 75%?
│
├─ NO → Independent work likely
│       Action: No further review needed
│
└─ YES → Suspicious pair
         │
         Check line-by-line details:
         │
         ├─ Template/boilerplate code?
         │  └─ YES → May be acceptable
         │     └─ NO → Continue investigation
         │
         Check pattern matching (KMP, Rabin-Karp, Suffix):
         │
         ├─ All 1.0? → Exact copy
         │  └─ Action: Strong evidence of plagiarism
         │
         ├─ All 0.0 but high N-gram/Edit?
         │  └─ Variable renaming, still plagiarism
         │
         └─ Manual verification required
            └─ Interview students
            └─ Check timestamps
            └─ Review process
```

---

## Common False Positives

### 1. Template Code
**Situation:** All students use provided starter code
**Example:** Package declarations, imports, basic structure
**Solution:** Adjust threshold or manually verify unique portions

### 2. Simple Assignments
**Situation:** Very basic problems with limited solutions
**Example:** "Write a function to add two numbers"
**Solution:** Focus on complex assignments

### 3. Legitimate Collaboration
**Situation:** Group projects or pair programming
**Example:** Approved collaboration per assignment rules
**Solution:** Document approved groups

---

## Best Practices

### Before Running Analysis

1. **Collect all files** in organized structure
2. **Document** which submissions are group work
3. **Note** any provided template code
4. **Set appropriate thresholds** for assignment complexity

### During Analysis

1. **Review all suspicious pairs** (>= 75%)
2. **Check line-by-line details** for each suspicious pair
3. **Document findings** using generated reports
4. **Cross-reference** with submission timestamps

### After Analysis

1. **Interview students** with suspicious submissions
2. **Request explanations** for similar code
3. **Compare** with previous assignments (pattern of behavior)
4. **Make fair decisions** based on all evidence

---

## Real-World Examples

### Example 1: Caught Red-Handed
```
Student A submitted: 2024-10-15 14:23
Student B submitted: 2024-10-15 14:25
Similarity: 99.8%
Line ranges: 100% match across entire file
```
**Conclusion:** Clear plagiarism, likely screen-shared or emailed

### Example 2: Suspicious but Explainable
```
Student C submitted: 2024-10-15 13:45
Student D submitted: 2024-10-15 16:30
Similarity: 82.3%
Line ranges: Only algorithm implementation similar
```
**Investigation:** Both referenced same online tutorial
**Conclusion:** Educational integrity violation (uncited source)

### Example 3: False Alarm
```
Student E submitted: 2024-10-15 14:00
Student F submitted: 2024-10-15 14:15
Similarity: 76.5%
Line ranges: Mostly import statements and setup code
```
**Investigation:** Both used course template
**Conclusion:** Acceptable, unique logic portions differ

---

## Summary

| Metric | What It Catches | Red Flag Value |
|--------|----------------|----------------|
| N-gram Jaccard | Structure copying | > 0.80 |
| Edit Distance | Minor changes | > 0.85 |
| KMP/Rabin/Suffix | Exact blocks | = 1.0 |
| Average | Overall | >= 0.75 |

**Remember:**
- No single metric is definitive
- Context matters (assignment complexity, provided code)
- Line-by-line details are crucial
- Always verify suspicious cases manually

---

**Use this guide to make informed, fair decisions about academic integrity! ⚖️**
