package Project2_java.src;

import Project2_java.src.similarity.SimilarityFacade;
import Project2_java.src.similarity.SimilarityScores;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WebComparisonRunner {
    private static final double SIMILARITY_THRESHOLD = 0.82;
    private static final double LINE_SIMILARITY_THRESHOLD = 0.78;
    private static final int MAX_LINE_PAIRS_IN_RESPONSE = 35;
    private static final int MAX_BLOCK_MATCH_LINES = 10;

    private static class Result {
        String fileA;
        String fileB;
        SimilarityScores scores;
        double average;
        double strict;
        double averageLineSimilarity;
        boolean suspicious;
        int totalLinesA;
        int totalLinesB;
        int matchedLinesA;
        int matchedLinesB;
        List<LineMatch> lineMatches = new ArrayList<>();
        List<MatchBlock> similarBlocks = new ArrayList<>();
    }

    private static class LineMatch {
        int lineNumberA;
        int lineNumberB;
        String contentA;
        String contentB;
        double similarity;
    }

    private static class MatchBlock {
        int startLineA;
        int endLineA;
        int startLineB;
        int endLineB;
        double averageSimilarity;
        List<LineMatch> matches = new ArrayList<>();
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("At least 2 file paths are required.");
            System.exit(1);
        }

        try {
            List<Result> results = new ArrayList<>();

            for (int i = 0; i < args.length; i++) {
                for (int j = i + 1; j < args.length; j++) {
                    SimilarityScores scores = SimilarityFacade.compareFiles(args[i], args[j], 5);

                    Result result = new Result();
                    result.fileA = args[i];
                    result.fileB = args[j];
                    result.scores = scores;
                    result.average = average(scores);
                    analyzeDetailedLineMatches(result);
                    result.strict = strictScore(result);
                    result.suspicious = result.strict >= SIMILARITY_THRESHOLD;
                    results.add(result);
                }
            }

            results.sort(Comparator.comparingDouble((Result r) -> r.strict).reversed());

            int suspiciousCount = 0;
            for (Result r : results) {
                if (r.suspicious) {
                    suspiciousCount++;
                }
            }

            StringBuilder out = new StringBuilder();
            out.append("{");
            out.append("\"threshold\":").append(SIMILARITY_THRESHOLD).append(",");
            out.append("\"lineSimilarityThreshold\":").append(LINE_SIMILARITY_THRESHOLD).append(",");
            out.append("\"totalFiles\":").append(args.length).append(",");
            out.append("\"totalComparisons\":").append(results.size()).append(",");
            out.append("\"suspiciousCount\":").append(suspiciousCount).append(",");
            out.append("\"scoreExplanations\":{");
            out.append("\"strict\":\"Weighted strict score using N-gram, edit similarity, line coverage, and probe signals. Used for flagging.\",");
            out.append("\"ngram\":\"Token fingerprint overlap between files. Higher value means larger shared code structure.\",");
            out.append("\"editNormalized\":\"Character-level similarity after edits. Higher value means fewer changes needed to transform one file to another.\",");
            out.append("\"kmp\":\"Binary probe search signal (0 or 1) using KMP. 1 means probe text from file B appears in file A.\",");
            out.append("\"rabin\":\"Binary probe search signal (0 or 1) using Rabin-Karp matching.\",");
            out.append("\"suffix\":\"Binary probe search signal (0 or 1) using suffix-array lookup.\",");
            out.append("\"lineCoverage\":\"Share of significant lines that found strong matches across files.\"");
            out.append("},");
            out.append("\"results\":[");

            for (int i = 0; i < results.size(); i++) {
                Result r = results.get(i);
                if (i > 0) {
                    out.append(",");
                }

                out.append("{");
                out.append("\"fileA\":\"").append(escapeJson(r.fileA)).append("\",");
                out.append("\"fileB\":\"").append(escapeJson(r.fileB)).append("\",");
                out.append("\"scores\":{");
                out.append("\"ngram\":").append(r.scores.ngram).append(",");
                out.append("\"kmp\":").append(r.scores.kmp).append(",");
                out.append("\"rabin\":").append(r.scores.rabin).append(",");
                out.append("\"suffix\":").append(r.scores.suffix).append(",");
                out.append("\"editNormalized\":").append(r.scores.editNormalized);
                out.append("},");
                out.append("\"averageSimilarity\":").append(r.average).append(",");
                out.append("\"strictSimilarity\":").append(r.strict).append(",");
                out.append("\"averageLineSimilarity\":").append(r.averageLineSimilarity).append(",");
                out.append("\"totalLinesA\":").append(r.totalLinesA).append(",");
                out.append("\"totalLinesB\":").append(r.totalLinesB).append(",");
                out.append("\"matchedLinesA\":").append(r.matchedLinesA).append(",");
                out.append("\"matchedLinesB\":").append(r.matchedLinesB).append(",");
                out.append("\"lineCoverageA\":").append(safeDivide(r.matchedLinesA, r.totalLinesA)).append(",");
                out.append("\"lineCoverageB\":").append(safeDivide(r.matchedLinesB, r.totalLinesB)).append(",");
                out.append("\"similarBlocks\":[");
                for (int blockIndex = 0; blockIndex < r.similarBlocks.size(); blockIndex++) {
                    MatchBlock block = r.similarBlocks.get(blockIndex);
                    if (blockIndex > 0) {
                        out.append(",");
                    }
                    out.append("{");
                    out.append("\"startLineA\":").append(block.startLineA).append(",");
                    out.append("\"endLineA\":").append(block.endLineA).append(",");
                    out.append("\"startLineB\":").append(block.startLineB).append(",");
                    out.append("\"endLineB\":").append(block.endLineB).append(",");
                    out.append("\"lineCount\":").append(block.matches.size()).append(",");
                    out.append("\"averageSimilarity\":").append(block.averageSimilarity).append(",");
                    out.append("\"matches\":[");
                    for (int matchIndex = 0; matchIndex < block.matches.size() && matchIndex < MAX_BLOCK_MATCH_LINES; matchIndex++) {
                        LineMatch match = block.matches.get(matchIndex);
                        if (matchIndex > 0) {
                            out.append(",");
                        }
                        appendLineMatchJson(out, match);
                    }
                    out.append("]}");
                }
                out.append("],");
                out.append("\"lineMatches\":[");
                for (int matchIndex = 0; matchIndex < r.lineMatches.size() && matchIndex < MAX_LINE_PAIRS_IN_RESPONSE; matchIndex++) {
                    if (matchIndex > 0) {
                        out.append(",");
                    }
                    appendLineMatchJson(out, r.lineMatches.get(matchIndex));
                }
                out.append("],");
                out.append("\"suspicious\":").append(r.suspicious);
                out.append("}");
            }

            out.append("]}");
            System.out.println(out);
        } catch (IOException | RuntimeException e) {
            System.err.println("Comparison failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private static double average(SimilarityScores scores) {
        return (scores.ngram + scores.kmp + scores.rabin + scores.suffix + scores.editNormalized) / 5.0;
    }

    private static void analyzeDetailedLineMatches(Result result) throws IOException {
        String[] linesA = Files.readString(Paths.get(result.fileA)).split("\\n");
        String[] linesB = Files.readString(Paths.get(result.fileB)).split("\\n");

        result.totalLinesA = linesA.length;
        result.totalLinesB = linesB.length;

        boolean[] matchedInA = new boolean[linesA.length];
        boolean[] matchedInB = new boolean[linesB.length];

        for (int i = 0; i < linesA.length; i++) {
            String lineA = sanitizeLine(linesA[i]);
            if (!isMeaningfulLine(lineA)) {
                continue;
            }

            int bestJ = -1;
            double bestSimilarity = 0.0;

            for (int j = 0; j < linesB.length; j++) {
                if (matchedInB[j]) {
                    continue;
                }
                String lineB = sanitizeLine(linesB[j]);
                if (!isMeaningfulLine(lineB)) {
                    continue;
                }

                double similarity = calculateLineSimilarity(lineA, lineB);
                if (similarity > bestSimilarity) {
                    bestSimilarity = similarity;
                    bestJ = j;
                }
            }

            if (bestJ >= 0 && bestSimilarity >= LINE_SIMILARITY_THRESHOLD) {
                LineMatch match = new LineMatch();
                match.lineNumberA = i + 1;
                match.lineNumberB = bestJ + 1;
                match.contentA = linesA[i].trim();
                match.contentB = linesB[bestJ].trim();
                match.similarity = bestSimilarity;
                result.lineMatches.add(match);
                matchedInA[i] = true;
                matchedInB[bestJ] = true;
            }
        }

        result.matchedLinesA = countMatches(matchedInA);
        result.matchedLinesB = countMatches(matchedInB);
        result.averageLineSimilarity = result.lineMatches.stream()
            .mapToDouble(m -> m.similarity)
            .average()
            .orElse(0.0);
        result.similarBlocks = groupIntoBlocks(result.lineMatches);
    }

    private static List<MatchBlock> groupIntoBlocks(List<LineMatch> matches) {
        List<MatchBlock> blocks = new ArrayList<>();
        if (matches.isEmpty()) {
            return blocks;
        }

        matches.sort(Comparator.comparingInt((LineMatch m) -> m.lineNumberA)
            .thenComparingInt(m -> m.lineNumberB));

        MatchBlock current = new MatchBlock();
        current.startLineA = matches.get(0).lineNumberA;
        current.startLineB = matches.get(0).lineNumberB;
        current.matches.add(matches.get(0));

        for (int i = 1; i < matches.size(); i++) {
            LineMatch previous = matches.get(i - 1);
            LineMatch line = matches.get(i);

            if (line.lineNumberA == previous.lineNumberA + 1 && line.lineNumberB == previous.lineNumberB + 1) {
                current.matches.add(line);
            } else {
                finalizeAndAddBlock(blocks, current);
                current = new MatchBlock();
                current.startLineA = line.lineNumberA;
                current.startLineB = line.lineNumberB;
                current.matches.add(line);
            }
        }

        finalizeAndAddBlock(blocks, current);
        return blocks;
    }

    private static void finalizeAndAddBlock(List<MatchBlock> blocks, MatchBlock block) {
        if (block.matches.size() < 2) {
            return;
        }
        LineMatch last = block.matches.get(block.matches.size() - 1);
        block.endLineA = last.lineNumberA;
        block.endLineB = last.lineNumberB;
        block.averageSimilarity = block.matches.stream().mapToDouble(m -> m.similarity).average().orElse(0.0);
        blocks.add(block);
    }

    private static double strictScore(Result result) {
        double probeSignal = (result.scores.kmp + result.scores.rabin + result.scores.suffix) / 3.0;
        double lineCoverage = (safeDivide(result.matchedLinesA, result.totalLinesA)
            + safeDivide(result.matchedLinesB, result.totalLinesB)) / 2.0;

        return (0.45 * result.scores.ngram)
            + (0.35 * result.scores.editNormalized)
            + (0.15 * lineCoverage)
            + (0.05 * probeSignal);
    }

    private static void appendLineMatchJson(StringBuilder out, LineMatch match) {
        out.append("{");
        out.append("\"lineNumberA\":").append(match.lineNumberA).append(",");
        out.append("\"lineNumberB\":").append(match.lineNumberB).append(",");
        out.append("\"similarity\":").append(match.similarity).append(",");
        out.append("\"contentA\":\"").append(escapeJson(truncate(match.contentA, 140))).append("\",");
        out.append("\"contentB\":\"").append(escapeJson(truncate(match.contentB, 140))).append("\"");
        out.append("}");
    }

    private static int countMatches(boolean[] marks) {
        int total = 0;
        for (boolean mark : marks) {
            if (mark) {
                total++;
            }
        }
        return total;
    }

    private static double safeDivide(int value, int total) {
        if (total <= 0) {
            return 0.0;
        }
        return (double) value / total;
    }

    private static boolean isMeaningfulLine(String line) {
        if (line.isEmpty() || line.length() < 3) {
            return false;
        }
        return !line.equals("{") && !line.equals("}") && !line.equals(";");
    }

    private static String sanitizeLine(String line) {
        String noInlineComment = line.replaceAll("//.*$", "");
        return noInlineComment.replaceAll("\\s+", " ").trim().toLowerCase();
    }

    private static double calculateLineSimilarity(String lineA, String lineB) {
        int maxLength = Math.max(lineA.length(), lineB.length());
        if (maxLength == 0) {
            return 1.0;
        }
        int distance = levenshteinDistance(lineA, lineB);
        return 1.0 - ((double) distance / maxLength);
    }

    private static int levenshteinDistance(String a, String b) {
        int n = a.length();
        int m = b.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }

        int[] previous = new int[m + 1];
        for (int j = 0; j <= m; j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            int[] current = new int[m + 1];
            current[0] = i;
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    current[j] = previous[j - 1];
                } else {
                    current[j] = 1 + Math.min(previous[j - 1], Math.min(previous[j], current[j - 1]));
                }
            }
            previous = current;
        }

        return previous[m];
    }

    private static String truncate(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength - 3) + "...";
    }

    private static String escapeJson(String text) {
        StringBuilder escaped = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (c < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
                }
            }
        }
        return escaped.toString();
    }
}
