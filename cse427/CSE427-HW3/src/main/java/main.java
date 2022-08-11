import java.io.IOException;

/*
Main class that runs the program.
Reads the given .fna file, parses it, and runs 10 iteration of HMM Viterbi on the read data.
 */
public class main {
    // runner class
    public static void main (String args[]) throws IOException {
        // target file MUST be located within src/main/resources/
        fastaReader fr = new fastaReader("GCF_000091665.1_ASM9166v1_genomic.fna");
        fr.readFile(); // load file with formatting

        HMM hmm = new HMM(fr.getContent()); // fill read contents into the HMM Viterbi algorithm

        final long start = System.currentTimeMillis();
        hmm.iterate(10); // iterate 10 times (10th time being a bit special)
        final long end = System.currentTimeMillis();

        System.out.println("Language Used : JAVA");
        System.out.println("Runtime of 10 iterations (in milliseconds) : " + (end - start));
    }
}
