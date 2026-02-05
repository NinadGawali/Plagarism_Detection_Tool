package Project2_java.src;
import Project2_java.src.similarity.SimilarityFacade;
import Project2_java.src.similarity.SimilarityScores;
import Project2_java.src.similarity.LineMatchDetail;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main {
    private static final double SIMILARITY_THRESHOLD = 0.75; // 75% similarity threshold
    private static final double LINE_SIMILARITY_THRESHOLD = 0.70; // 70% for line-by-line
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws Exception {
        displayWelcome();
        
        while (true) {
            displayMenu();
            int choice = getMenuChoice();
            
            switch (choice) {
                case 1:
                    runPlagiarismCheck();
                    break;
                case 2:
                    viewPreviousReports();
                    break;
                case 3:
                    System.out.println("\n" + "=".repeat(60));
                    System.out.println("Thank you for using the Plagiarism Detection Tool!");
                    System.out.println("Goodbye!");
                    System.out.println("=".repeat(60));
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void displayWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("||" + " ".repeat(58) + "||");
        System.out.println("||" + centerText("PLAGIARISM DETECTION TOOL", 58) + "||");
        System.out.println("||" + centerText("Lab Exam Code Similarity Checker", 58) + "||");
        System.out.println("||" + " ".repeat(58) + "||");
        System.out.println("=".repeat(60));
        System.out.println("Welcome! This tool helps detect code similarity between");
        System.out.println("multiple student submissions using advanced algorithms.");
        System.out.println("=".repeat(60) + "\n");
    }
    
    private static void displayMenu() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("MAIN MENU:");
        System.out.println("-".repeat(60));
        System.out.println("1. Run Plagiarism Check");
        System.out.println("2. View Previous Reports");
        System.out.println("3. Exit");
        System.out.println("-".repeat(60));
        System.out.print("Enter your choice (1-3): ");
    }
    
    private static int getMenuChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void runPlagiarismCheck() throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PLAGIARISM CHECK");
        System.out.println("=".repeat(60));
        
        // Get list of files
        List<String> filePaths = getFilePaths();
        if (filePaths.size() < 2) {
            System.out.println("ERROR: At least 2 files are required for comparison.");
            return;
        }
        
        System.out.println("\nFiles to compare:");
        for (int i = 0; i < filePaths.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + filePaths.get(i));
        }
        
        System.out.print("\nProceed with comparison? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Comparison cancelled.");
            return;
        }
        
        // Perform comparisons
        System.out.println("\n" + "-".repeat(60));
        System.out.println("ANALYZING FILES...");
        System.out.println("-".repeat(60));
        
        List<ComparisonResult> allResults = new ArrayList<>();
        int totalComparisons = (filePaths.size() * (filePaths.size() - 1)) / 2;
        int currentComparison = 0;
        
        for (int i = 0; i < filePaths.size(); i++) {
            for (int j = i + 1; j < filePaths.size(); j++) {
                currentComparison++;
                System.out.printf("\rProgress: %d/%d comparisons", currentComparison, totalComparisons);
                
                String fileA = filePaths.get(i);
                String fileB = filePaths.get(j);
                
                SimilarityScores scores = SimilarityFacade.compareFiles(fileA, fileB, 5);
                double avgSimilarity = calculateAverageSimilarity(scores);
                
                ComparisonResult result = new ComparisonResult();
                result.fileA = fileA;
                result.fileB = fileB;
                result.scores = scores;
                result.averageSimilarity = avgSimilarity;
                result.suspicious = avgSimilarity >= SIMILARITY_THRESHOLD;
                
                // Perform detailed line-by-line analysis
                analyzeDetailedLineMatches(fileA, fileB, result);
                
                // Perform line-by-line analysis for suspicious pairs
                if (result.suspicious) {
                    result.similarLines = findSimilarLines(fileA, fileB);
                }
                
                allResults.add(result);
            }
        }
        
        System.out.println("\n\nAnalysis complete!\n");
        
        // Display results
        displayResults(allResults);
        
        // Save to log file
        saveToLog(allResults, filePaths);
    }
    
    private static List<String> getFilePaths() {
        List<String> paths = new ArrayList<>();
        
        System.out.println("\nEnter file paths (one per line).");
        System.out.println("Type 'done' when finished:");
        System.out.println("(You can use absolute paths or relative paths from current directory)");
        
        while (true) {
            System.out.print("\nFile path: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            
            if (input.isEmpty()) {
                continue;
            }
            
            // Check if file exists
            if (Files.exists(Paths.get(input))) {
                paths.add(input);
                System.out.println("  [OK] Added: " + input);
            } else {
                System.out.println("  [ERROR] File not found: " + input);
                System.out.print("  Try again? (y/n): ");
                String retry = scanner.nextLine().trim().toLowerCase();
                if (!retry.equals("y")) {
                    continue;
                }
            }
        }
        
        return paths;
    }
    
    private static double calculateAverageSimilarity(SimilarityScores scores) {
        return (scores.ngram + scores.kmp + scores.rabin + scores.suffix + scores.editNormalized) / 5.0;
    }
    
    private static void displayResults(List<ComparisonResult> results) {
        System.out.println("=".repeat(60));
        System.out.println("PLAGIARISM DETECTION RESULTS");
        System.out.println("=".repeat(60));
        
        // Sort by similarity (highest first)
        results.sort((a, b) -> Double.compare(b.averageSimilarity, a.averageSimilarity));
        
        int suspiciousCount = 0;
        for (ComparisonResult result : results) {
            if (result.suspicious) {
                suspiciousCount++;
            }
        }
        
        System.out.printf("\nTotal Comparisons: %d\n", results.size());
        System.out.printf("Suspicious Pairs: %d (Similarity >= %.0f%%)\n", 
                         suspiciousCount, SIMILARITY_THRESHOLD * 100);
        System.out.println("\n" + "-".repeat(60));
        
        for (int i = 0; i < results.size(); i++) {
            ComparisonResult result = results.get(i);
            
            System.out.printf("\n[Comparison %d] %s\n", i + 1, 
                             result.suspicious ? "[!] SUSPICIOUS - POSSIBLE PLAGIARISM" : "Normal");
            System.out.println("File A: " + getFileName(result.fileA));
            System.out.println("File B: " + getFileName(result.fileB));
            System.out.println();
            System.out.printf("  N-gram Jaccard       : %.3f (%.1f%%)\n", 
                             result.scores.ngram, result.scores.ngram * 100);
            System.out.printf("  KMP sample match     : %.3f\n", result.scores.kmp);
            System.out.printf("  Rabin-Karp sample    : %.3f\n", result.scores.rabin);
            System.out.printf("  Suffix-array sample  : %.3f\n", result.scores.suffix);
            System.out.printf("  Edit-distance (norm) : %.3f (%.1f%%)\n", 
                             result.scores.editNormalized, result.scores.editNormalized * 100);
            System.out.printf("  >> AVERAGE SIMILARITY : %.3f (%.1f%%)%s\n", 
                             result.averageSimilarity, result.averageSimilarity * 100,
                             result.suspicious ? " [!]" : "");
            
            // Show plagiarism percentages
            if (result.detailedMatches != null && !result.detailedMatches.isEmpty()) {
                double plagiarismPercentA = (double) result.matchedLinesA / result.totalLinesA * 100;
                double plagiarismPercentB = (double) result.matchedLinesB / result.totalLinesB * 100;
                
                System.out.println();
                System.out.println("  PLAGIARISM ANALYSIS:");
                System.out.printf("    File A: %d/%d lines matched (%.1f%% plagiarized)\n", 
                                 result.matchedLinesA, result.totalLinesA, plagiarismPercentA);
                System.out.printf("    File B: %d/%d lines matched (%.1f%% plagiarized)\n", 
                                 result.matchedLinesB, result.totalLinesB, plagiarismPercentB);
                System.out.printf("    Total similar line pairs found: %d\n", result.detailedMatches.size());
            }
            
            if (result.similarBlocks != null && !result.similarBlocks.isEmpty()) {
                System.out.println("\n  SIMILAR CODE BLOCKS:");
                for (SimilarBlock block : result.similarBlocks) {
                    if (block.getLineCount() >= 2) {
                        System.out.printf("    • Lines %d-%d (File A) ↔ Lines %d-%d (File B)\n", 
                                        block.startLineA, block.endLineA, block.startLineB, block.endLineB);
                        System.out.printf("      %d consecutive lines, avg %.1f%% similar\n", 
                                        block.getLineCount(), block.averageSimilarity * 100);
                    }
                }
            }
            
            System.out.println("-".repeat(60));
        }
    }
    
    private static List<String> findSimilarLines(String fileA, String fileB) throws IOException {
        List<String> result = new ArrayList<>();
        
        String contentA = Files.readString(Paths.get(fileA));
        String contentB = Files.readString(Paths.get(fileB));
        
        String[] linesA = contentA.split("\n");
        String[] linesB = contentB.split("\n");
        
        List<LineMatchDetail> allMatches = new ArrayList<>();
        
        // Find all similar line pairs
        for (int i = 0; i < linesA.length; i++) {
            String lineA = linesA[i].trim();
            if (lineA.isEmpty() || lineA.startsWith("//") || lineA.startsWith("/*")) {
                continue;
            }
            
            for (int j = 0; j < linesB.length; j++) {
                String lineB = linesB[j].trim();
                if (lineB.isEmpty() || lineB.startsWith("//") || lineB.startsWith("/*")) {
                    continue;
                }
                
                double similarity = calculateLineSimilarity(lineA, lineB);
                
                if (similarity >= LINE_SIMILARITY_THRESHOLD) {
                    allMatches.add(new LineMatchDetail(i + 1, j + 1, lineA, lineB, similarity));
                    break; // Found a match for this line in A, move to next line
                }
            }
        }
        
        // Group consecutive matches into blocks
        if (!allMatches.isEmpty()) {
            List<SimilarBlock> blocks = groupIntoBlocks(allMatches);
            for (SimilarBlock block : blocks) {
                if (block.getLineCount() >= 2) {
                    result.add(String.format("Lines %d-%d (File A) ↔ Lines %d-%d (File B): %d lines, avg %.1f%% similar",
                                            block.startLineA, block.endLineA, 
                                            block.startLineB, block.endLineB,
                                            block.getLineCount(), block.averageSimilarity * 100));
                }
            }
        }
        
        return result;
    }
    
    private static List<SimilarBlock> groupIntoBlocks(List<LineMatchDetail> matches) {
        List<SimilarBlock> blocks = new ArrayList<>();
        if (matches.isEmpty()) return blocks;
        
        SimilarBlock currentBlock = new SimilarBlock();
        currentBlock.startLineA = matches.get(0).lineNumberA;
        currentBlock.startLineB = matches.get(0).lineNumberB;
        currentBlock.matches.add(matches.get(0));
        
        for (int i = 1; i < matches.size(); i++) {
            LineMatchDetail current = matches.get(i);
            LineMatchDetail previous = matches.get(i - 1);
            
            // Check if lines are consecutive in both files
            if (current.lineNumberA == previous.lineNumberA + 1 && 
                current.lineNumberB == previous.lineNumberB + 1) {
                currentBlock.matches.add(current);
            } else {
                // End current block and start new one
                currentBlock.endLineA = previous.lineNumberA;
                currentBlock.endLineB = previous.lineNumberB;
                currentBlock.calculateAverage();
                blocks.add(currentBlock);
                
                currentBlock = new SimilarBlock();
                currentBlock.startLineA = current.lineNumberA;
                currentBlock.startLineB = current.lineNumberB;
                currentBlock.matches.add(current);
            }
        }
        
        // Add the last block
        LineMatchDetail last = matches.get(matches.size() - 1);
        currentBlock.endLineA = last.lineNumberA;
        currentBlock.endLineB = last.lineNumberB;
        currentBlock.calculateAverage();
        blocks.add(currentBlock);
        
        return blocks;
    }
    
    private static void analyzeDetailedLineMatches(String fileA, String fileB, ComparisonResult result) throws IOException {
        String contentA = Files.readString(Paths.get(fileA));
        String contentB = Files.readString(Paths.get(fileB));
        
        String[] linesA = contentA.split("\n");
        String[] linesB = contentB.split("\n");
        
        result.totalLinesA = linesA.length;
        result.totalLinesB = linesB.length;
        result.detailedMatches = new ArrayList<>();
        
        boolean[] matchedInA = new boolean[linesA.length];
        boolean[] matchedInB = new boolean[linesB.length];
        
        // Find all similar line pairs
        for (int i = 0; i < linesA.length; i++) {
            String lineA = linesA[i].trim();
            if (lineA.isEmpty() || lineA.startsWith("//") || lineA.startsWith("/*") || lineA.startsWith("*")) {
                continue;
            }
            
            for (int j = 0; j < linesB.length; j++) {
                String lineB = linesB[j].trim();
                if (lineB.isEmpty() || lineB.startsWith("//") || lineB.startsWith("/*") || lineB.startsWith("*")) {
                    continue;
                }
                
                double similarity = calculateLineSimilarity(lineA, lineB);
                
                if (similarity >= LINE_SIMILARITY_THRESHOLD) {
                    result.detailedMatches.add(new LineMatchDetail(i + 1, j + 1, lineA, lineB, similarity));
                    matchedInA[i] = true;
                    matchedInB[j] = true;
                    break;
                }
            }
        }
        
        // Count matched lines
        result.matchedLinesA = 0;
        result.matchedLinesB = 0;
        for (boolean matched : matchedInA) {
            if (matched) result.matchedLinesA++;
        }
        for (boolean matched : matchedInB) {
            if (matched) result.matchedLinesB++;
        }
        
        // Group into blocks
        result.similarBlocks = groupIntoBlocks(result.detailedMatches);
    }
    
    private static double calculateLineSimilarity(String line1, String line2) {
        // Normalize lines (remove extra whitespace, convert to lowercase)
        String normalized1 = line1.replaceAll("\\s+", " ").toLowerCase();
        String normalized2 = line2.replaceAll("\\s+", " ").toLowerCase();
        
        // Calculate similarity using edit distance
        int maxLen = Math.max(normalized1.length(), normalized2.length());
        if (maxLen == 0) return 1.0;
        
        int distance = levenshteinDistance(normalized1, normalized2);
        return 1.0 - ((double) distance / maxLen);
    }
    
    private static int levenshteinDistance(String a, String b) {
        int n = a.length(), m = b.length();
        if (n == 0) return m;
        if (m == 0) return n;
        
        int[] prev = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        
        for (int i = 1; i <= n; i++) {
            int[] curr = new int[m + 1];
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(prev[j - 1], Math.min(prev[j], curr[j - 1]));
                }
            }
            prev = curr;
        }
        return prev[m];
    }
    
    private static void saveToLog(List<ComparisonResult> results, List<String> filePaths) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String logFileName = "plagiarism_report_" + timestamp + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName))) {
            writer.println("=".repeat(80));
            writer.println("        COMPREHENSIVE PLAGIARISM DETECTION REPORT");
            writer.println("=".repeat(80));
            writer.println("Generated: " + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println();
            
            writer.println("FILES ANALYZED:");
            writer.println("-".repeat(80));
            for (int i = 0; i < filePaths.size(); i++) {
                writer.println((i + 1) + ". " + filePaths.get(i));
            }
            writer.println();
            
            writer.println("DETECTION PARAMETERS:");
            writer.println("-".repeat(80));
            writer.println("Overall Similarity Threshold: " + (SIMILARITY_THRESHOLD * 100) + "%");
            writer.println("Line-by-Line Similarity Threshold: " + (LINE_SIMILARITY_THRESHOLD * 100) + "%");
            writer.println("N-gram Size: 5");
            writer.println();
            
            // Sort by similarity
            results.sort((a, b) -> Double.compare(b.averageSimilarity, a.averageSimilarity));
            
            writer.println("=".repeat(80));
            writer.println("                    EXECUTIVE SUMMARY");
            writer.println("=".repeat(80));
            
            int suspiciousCount = 0;
            for (ComparisonResult result : results) {
                if (result.suspicious) suspiciousCount++;
            }
            
            writer.println("Total Comparisons: " + results.size());
            writer.println("Suspicious Pairs: " + suspiciousCount + " (" + 
                          String.format("%.1f", (double)suspiciousCount/results.size()*100) + "% of total)");
            writer.println("Clean Pairs: " + (results.size() - suspiciousCount));
            writer.println();
            
            if (suspiciousCount > 0) {
                writer.println("HIGH-RISK PAIRS (Sorted by Similarity):");
                writer.println("-".repeat(80));
                int rank = 1;
                for (ComparisonResult result : results) {
                    if (result.suspicious) {
                        writer.printf("%d. %s vs %s: %.1f%% similar\n", 
                                    rank++, getFileName(result.fileA), getFileName(result.fileB),
                                    result.averageSimilarity * 100);
                    }
                }
                writer.println();
            }
            
            writer.println("=".repeat(80));
            writer.println("                    DETAILED ANALYSIS");
            writer.println("=".repeat(80));
            
            for (int i = 0; i < results.size(); i++) {
                ComparisonResult result = results.get(i);
                
                writer.println();
                writer.println("#".repeat(80));
                writer.printf("COMPARISON #%d: %s vs %s\n", i + 1, 
                             getFileName(result.fileA), getFileName(result.fileB));
                if (result.suspicious) {
                    writer.println("STATUS: [!] SUSPICIOUS - POSSIBLE PLAGIARISM DETECTED [!]");
                } else {
                    writer.println("STATUS: Normal - No significant plagiarism detected");
                }
                writer.println("#".repeat(80));
                writer.println();
                
                writer.println("FILE INFORMATION:");
                writer.println("-".repeat(80));
                writer.println("File A: " + result.fileA);
                writer.println("  Total Lines: " + result.totalLinesA);
                if (result.detailedMatches != null) {
                    double percentA = (double) result.matchedLinesA / result.totalLinesA * 100;
                    writer.printf("  Matched Lines: %d (%.1f%% of file)\n", result.matchedLinesA, percentA);
                }
                writer.println();
                writer.println("File B: " + result.fileB);
                writer.println("  Total Lines: " + result.totalLinesB);
                if (result.detailedMatches != null) {
                    double percentB = (double) result.matchedLinesB / result.totalLinesB * 100;
                    writer.printf("  Matched Lines: %d (%.1f%% of file)\n", result.matchedLinesB, percentB);
                }
                writer.println();
                
                writer.println("SIMILARITY METRICS:");
                writer.println("-".repeat(80));
                writer.printf("  N-gram Jaccard Similarity : %.3f (%.1f%%)\n", 
                             result.scores.ngram, result.scores.ngram * 100);
                writer.printf("  KMP Pattern Match         : %.3f\n", result.scores.kmp);
                writer.printf("  Rabin-Karp Pattern Match  : %.3f\n", result.scores.rabin);
                writer.printf("  Suffix Array Match        : %.3f\n", result.scores.suffix);
                writer.printf("  Edit Distance (Normalized): %.3f (%.1f%%)\n", 
                             result.scores.editNormalized, result.scores.editNormalized * 100);
                writer.println();
                writer.printf("  >> OVERALL AVERAGE SIMILARITY: %.3f (%.1f%%)%s\n", 
                             result.averageSimilarity, result.averageSimilarity * 100,
                             result.suspicious ? " [ALERT!]" : "");
                writer.println();
                
                // Detailed line-by-line analysis
                if (result.detailedMatches != null && !result.detailedMatches.isEmpty()) {
                    writer.println("LINE-BY-LINE PLAGIARISM ANALYSIS:");
                    writer.println("-".repeat(80));
                    
                    double avgLineSimilarity = result.detailedMatches.stream()
                        .mapToDouble(m -> m.similarity)
                        .average()
                        .orElse(0.0);
                    
                    writer.printf("Total Similar Line Pairs: %d\n", result.detailedMatches.size());
                    writer.printf("Average Line Similarity: %.1f%%\n", avgLineSimilarity * 100);
                    writer.println();
                    
                    if (result.similarBlocks != null && !result.similarBlocks.isEmpty()) {
                        writer.println("SIMILAR CODE BLOCKS DETECTED:");
                        writer.println("-".repeat(80));
                        
                        for (int blockIdx = 0; blockIdx < result.similarBlocks.size(); blockIdx++) {
                            SimilarBlock block = result.similarBlocks.get(blockIdx);
                            if (block.getLineCount() >= 2) {
                                writer.printf("\n  Block #%d:\n", blockIdx + 1);
                                writer.printf("    Location: Lines %d-%d (File A) ↔ Lines %d-%d (File B)\n",
                                            block.startLineA, block.endLineA, 
                                            block.startLineB, block.endLineB);
                                writer.printf("    Size: %d consecutive lines\n", block.getLineCount());
                                writer.printf("    Average Similarity: %.1f%%\n", block.averageSimilarity * 100);
                                
                                if (block.getLineCount() <= 10) {
                                    writer.println("\n    Detailed Line Matches:");
                                    for (LineMatchDetail match : block.matches) {
                                        writer.printf("      Line %d ↔ Line %d (%.1f%% similar)\n",
                                                    match.lineNumberA, match.lineNumberB, 
                                                    match.similarity * 100);
                                        writer.println("        File A: " + truncate(match.contentA, 60));
                                        writer.println("        File B: " + truncate(match.contentB, 60));
                                    }
                                }
                            }
                        }
                        writer.println();
                    }
                    
                    // Show all individual matches if there aren't too many
                    if (result.detailedMatches.size() <= 50) {
                        writer.println("\nALL SIMILAR LINE PAIRS:");
                        writer.println("-".repeat(80));
                        writer.println("Format: [Line# in A] ↔ [Line# in B] | Similarity | Content Preview");
                        writer.println();
                        
                        for (LineMatchDetail match : result.detailedMatches) {
                            writer.printf("  [%4d] ↔ [%4d] | %5.1f%% | %s\n",
                                        match.lineNumberA, match.lineNumberB, 
                                        match.similarity * 100,
                                        truncate(match.contentA, 50));
                        }
                    } else {
                        writer.println("\n(Too many matches to list individually - see block summary above)");
                    }
                }
                
                writer.println();
                writer.println("=".repeat(80));
            }
            
            writer.println();
            writer.println("=".repeat(80));
            writer.println("                    END OF REPORT");
            writer.println("=".repeat(80));
        }
        
        System.out.println("\n[SUCCESS] Detailed report saved to: " + logFileName);
    }
    
    private static String truncate(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
    
    private static void viewPreviousReports() throws IOException {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PREVIOUS REPORTS");
        System.out.println("=".repeat(60));
        
        // Find all report files in current directory
        List<String> reports = new ArrayList<>();
        Files.list(Paths.get("."))
            .filter(path -> path.getFileName().toString().startsWith("plagiarism_report_"))
            .filter(path -> path.getFileName().toString().endsWith(".txt"))
            .forEach(path -> reports.add(path.toString()));
        
        if (reports.isEmpty()) {
            System.out.println("No previous reports found.");
            return;
        }
        
        reports.sort(Collections.reverseOrder()); // Most recent first
        
        System.out.println("\nAvailable reports:");
        for (int i = 0; i < reports.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + reports.get(i));
        }
        
        System.out.print("\nEnter report number to view (0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= reports.size()) {
                String content = Files.readString(Paths.get(reports.get(choice - 1)));
                System.out.println("\n" + content);
            }
        } catch (Exception e) {
            System.out.println("Invalid choice.");
        }
    }
    
    private static String getFileName(String path) {
        return Paths.get(path).getFileName().toString();
    }
    
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + 
               " ".repeat(Math.max(0, width - text.length() - padding));
    }
    
    // Inner class to hold comparison results
    static class ComparisonResult {
        String fileA;
        String fileB;
        SimilarityScores scores;
        double averageSimilarity;
        boolean suspicious;
        List<String> similarLines;
        List<LineMatchDetail> detailedMatches;
        List<SimilarBlock> similarBlocks;
        int totalLinesA;
        int totalLinesB;
        int matchedLinesA;
        int matchedLinesB;
    }
    
    // Inner class to represent a block of similar lines
    static class SimilarBlock {
        int startLineA;
        int endLineA;
        int startLineB;
        int endLineB;
        List<LineMatchDetail> matches;
        double averageSimilarity;
        
        public SimilarBlock() {
            this.matches = new ArrayList<>();
        }
        
        public void calculateAverage() {
            if (matches.isEmpty()) {
                averageSimilarity = 0.0;
                return;
            }
            double sum = 0.0;
            for (LineMatchDetail match : matches) {
                sum += match.similarity;
            }
            averageSimilarity = sum / matches.size();
        }
        
        public int getLineCount() {
            return matches.size();
        }
    }
}