# Code Plagiarism Detection using String Algorithms (Java)

## 📌 Project Overview

This project implements a **code plagiarism detection system** using classic **string matching and similarity algorithms** in Java.  
It compares two source code files and computes similarity scores using multiple techniques to identify copied or highly similar code.

The system demonstrates how different algorithms behave under:
- Exact copying
- Minor edits
- Variable renaming
- Formatting changes

This project is suitable for:
- Academic plagiarism detection demos
- Data Structures & Algorithms projects
- Software Engineering / COA / Final-year projects

---

## 🧠 Algorithms Used

The project uses the following algorithms:

| Algorithm | Purpose |
|---------|--------|
| **N-gram Jaccard Similarity** | Token-level similarity |
| **Edit Distance (Normalized)** | Measures minimal edits required |
| **KMP (Knuth–Morris–Pratt)** | Exact substring matching |
| **Rabin–Karp** | Hash-based substring matching |
| **Suffix Array** | Longest common substring detection |
| **Trie (support structure)** | Efficient prefix storage |

📌 Note:
- KMP, Rabin–Karp, and Suffix Array detect **exact copied blocks**
- Edit Distance and N-grams detect **obfuscated plagiarism**

---

## 📂 Directory Structure

```
Code-Plagiarism-Detection-using-String-Algorithms-JAVA/
│
├── Project2_java/
│   └── src/
│       ├── Main.java
│       └── similarity/
│           ├── SimilarityFacade.java
│           ├── SimilarityScores.java
│           ├── EditDistance.java
│           ├── KMP.java
│           ├── NGram.java
│           ├── RabinKarp.java
│           ├── SuffixArray.java
│           └── Trie.java
│
├── SampleA.java
├── SampleB.java
└── README.md
```

---

## ⚙️ Requirements

- Java JDK **8 or above**
- Command line / Terminal (Git Bash, CMD, Linux shell)

Check Java version:
```bash
java -version
```

---

## ▶️ How to Compile the Project

From the project root directory, run:

```bash
javac -d . Project2_java/src/Main.java Project2_java/src/similarity/*.java
```

📌 `-d .` ensures `.class` files are placed according to package structure.

---

## ▶️ How to Run the Project

Provide two Java source files as input:

```bash
java Project2_java.src.Main SampleA.java SampleB.java
```

---

## 📊 Sample Output

```yaml
N-gram Jaccard       : 0.586
KMP sample match     : 0.0
Rabin-Karp sample    : 0.0
Suffix-array sample  : 0.0
Edit-distance (norm) : 0.933
```

**Interpretation:**
- High Edit Distance / N-gram → plagiarism with obfuscation
- High KMP / Rabin-Karp / Suffix → exact copy
- Low exact-match but high edit similarity → renamed variables / formatting changes