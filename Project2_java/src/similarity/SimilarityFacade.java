package Project2_java.src.similarity;
import Project2_java.src.algo.NGram;
import Project2_java.src.algo.KMP;
import Project2_java.src.algo.RabinKarp;
import Project2_java.src.algo.SuffixArray;
import Project2_java.src.algo.EditDistance;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths; 
import java.util.List;  

/**
 * SimilarityFacade provides a simple interface to compare two files using various string similarity algorithms.
 */
// This class encapsulates the logic for comparing files using N-Gram, KMP, Rab in-Karp, Suffix Array, and Edit Distance algorithms.
// It reads the contents of the files, computes similarity scores, and returns them in a SimilarityScores object.
// The methods are static, allowing easy access without needing to instantiate the class.
// The compareFiles method takes two file paths and an N-Gram size, reads the files, and computes the similarity scores using different algorithms.
// It returns a SimilarityScores object containing the results      
// The N-Gram size is used to build the N-Gram fingerprints for Jaccard similarity.
// The KMP, Rabin-Karp, and Suffix Array algorithms are used to find occurrences of a representative sample from the second file in the first file.
// The Edit Distance algorithm computes the normalized edit distance between the two files.
// The results are stored in a SimilarityScores object, which           
// contains fields for each algorithm's score, allowing easy access to the results.
// This facade simplifies the process of comparing files by providing a single method to handle all the complexity
// of reading files, computing fingerprints, and calculating similarity scores.         


public class SimilarityFacade {
    public static SimilarityScores compareFiles(String fileA, String fileB, int ngramN) throws IOException {
        String A = Files.readString(Paths.get(fileA));
        String B = Files.readString(Paths.get(fileB));
        SimilarityScores scores = new SimilarityScores();

        // N-Gram Jaccard Similarity
        var fpA = NGram.build(A, ngramN);
        var fpB = NGram.build(B, ngramN);
        scores.ngram = NGram.jaccard(fpA, fpB);

        // Representative sample match
        String probe = B.substring(0, Math.min(80, B.length()));

        // KMP
        List<Integer> kmp = KMP.search(probe, A);
        scores.kmp = kmp.isEmpty() ? 0.0 : 1.0;

        // Rabin-Karp
        List<Integer> rabin = RabinKarp.search(probe, A);
        scores.rabin = rabin.isEmpty() ? 0.0 : 1.0;

        // Suffix Array sample search
        SuffixArray sa = new SuffixArray(A);
        List<Integer> saHits = sa.substringSearch(probe);
        scores.suffix = saHits.isEmpty() ? 0.0 : 1.0;

        // Edit Distance
        int dist = EditDistance.levenshtein(A, B);
        scores.editNormalized = 1.0 - ((double) dist / Math.max(A.length(), B.length()));

        return scores;
    }
}