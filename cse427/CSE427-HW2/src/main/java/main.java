import java.io.IOException;
import java.util.ArrayList;

public class main {

    public static void main(String args[]) throws IOException {
        fastaReader fr = new fastaReader();
        functions f = new functions();

        fr.readFile("hw2-final-train.fasta"); // Select file (read documentation in fastaReader.java)
        ArrayList<String> parsed = fr.getSequences(); // Parse result from read
        fr.readFile("hw2-final-eval.fasta"); // Select file (read documentation in fastaReader.java)
        ArrayList<String> parsed2 = fr.getSequences(); // Parse result from read

        double[][] best = f.initEM(parsed); // initiate
        f.evaluation(best, parsed2);
    }
}
