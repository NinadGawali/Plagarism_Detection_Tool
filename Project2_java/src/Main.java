package Project2_java.src;
import Project2_java.src.similarity.SimilarityFacade;
import Project2_java.src.similarity.SimilarityScores;               
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths; 
import java.util.*;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java plagiarismdetection.Main fileA fileB");
            return;
        }
        SimilarityScores scores = SimilarityFacade.compareFiles(args[0], args[1], 5);
        System.out.printf("N-gram Jaccard       : %.3f%n", scores.ngram);
        System.out.printf("KMP sample match     : %.1f%n", scores.kmp);
        System.out.printf("Rabin-Karp sample    : %.1f%n", scores.rabin);
        System.out.printf("Suffix-array sample  : %.1f%n", scores.suffix);
        System.out.printf("Edit-distance (norm) : %.3f%n", scores.editNormalized);
    }
}