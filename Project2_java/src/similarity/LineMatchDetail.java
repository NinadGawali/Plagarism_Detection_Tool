package Project2_java.src.similarity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents detailed information about similar lines between two files
 */
public class LineMatchDetail {
    public int lineNumberA;
    public int lineNumberB;
    public String contentA;
    public String contentB;
    public double similarity;
    
    public LineMatchDetail(int lineA, int lineB, String contentA, String contentB, double similarity) {
        this.lineNumberA = lineA;
        this.lineNumberB = lineB;
        this.contentA = contentA;
        this.contentB = contentB;
        this.similarity = similarity;
    }
    
    @Override
    public String toString() {
        return String.format("Line %d ↔ Line %d (%.1f%% similar)", 
                           lineNumberA, lineNumberB, similarity * 100);
    }
}

/**
 * Represents a block of consecutive similar lines
 */
class SimilarBlock {
    public int startLineA;
    public int endLineA;
    public int startLineB;
    public int endLineB;
    public List<LineMatchDetail> matches;
    public double averageSimilarity;
    
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
    
    @Override
    public String toString() {
        return String.format("Lines %d-%d (File A) ↔ Lines %d-%d (File B): %d lines, avg %.1f%% similar",
                           startLineA, endLineA, startLineB, endLineB, 
                           getLineCount(), averageSimilarity * 100);
    }
}
