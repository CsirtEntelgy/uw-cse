import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class main {
    //Map int to char (reversed index) (A = 0, C = 1, G = 2, T = 3)
    private static final HashMap<Integer, Character> INDEX_REVERSE = new HashMap<>(){{
        put(0, 'A');
        put(1, 'C');
        put(2, 'G');
        put(3, 'T');
    }};

    private static final DecimalFormat DF = new DecimalFormat("#.####"); // decimal format

    public static void main (String args[]) throws IOException {
        DF.setRoundingMode(RoundingMode.CEILING); // set decimal format for printing (rounding up to 4th decimal place)

        orf_fileProcessor fr = new orf_fileProcessor("GCF_000091665.1_ASM9166v1_genomic.fna");
        fr.readFNAFile(); //reading .fna

        orf_handle h = new orf_handle(fr.getFNAContent()); //handler with read .fna content
        h.find(); //finding ORFs

        orf_markov m = new orf_markov();
        m.train(h.reportLongORF()); //training markov model with long ORFs

        //creating sample conditional probability table
        //rows and columns ordered A, C, G, T
        //results are arbitrary P(x) and Q(x) scores
        double[][] sampleP = new double[4][4];
        double[][] sampleQ = new double[4][4];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                sampleP[i][j] = m.arbiScore("AAG" + INDEX_REVERSE.get(i) + INDEX_REVERSE.get(j) + "T")[0];
                sampleQ[i][j] = m.arbiScore("AAG" + INDEX_REVERSE.get(i) + INDEX_REVERSE.get(j) + "T")[1];
            }
        }

        fr = new orf_fileProcessor("plusgenes-subset.gff");
        fr.readGFFFile(); //reading .gff
        ArrayList<Integer> endIndex = fr.getGffContent(); //getting ending indexes (accounted for 1-base and stop codon)

        ArrayList<double[]> fullReport = h.reportFull(endIndex, m); //table of full data

        //finding first 5 short and long ORF
        int foundSCount = 0;
        ArrayList<double[]> foundS = new ArrayList<>();
        int foundLCount = 0;
        ArrayList<double[]> foundL = new ArrayList<>();
        for(double[] d : fullReport){
            double length = d[2];
            if(foundSCount < 5 && length < 151){
                foundS.add(d);
                foundSCount++;
            } else if(foundLCount < 5 && length > 1400){
                foundL.add(d);
                foundLCount++;
            }
        }

        orf_graphics g = new orf_graphics(fullReport); //must have dependency, see pom.xml
        g.generateROC();

        System.out.println("------------------------------------ Begin Report ------------------------------------");
        System.out.println("Frames found in offset 0 : " + h.reportCount()[0]);
        System.out.println("Frames found in offset 1 : " + h.reportCount()[1]);
        System.out.println("Frames found in offset 2 : " + h.reportCount()[2] + "\n");

        System.out.println("Total number of SHORT ORFs : " + h.reportLength()[0]);
        System.out.println("Total number of LONG ORFs : " + h.reportLength()[1] + "\n");

        System.out.println("Total number of CDS found in GenBank data : " + fr.getGFFLength() + "\n");

        System.out.println("Sample conditional probability table for P(x) (rounded to 4 digits below zero) : ");
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(DF.format(sampleP[i][j]));
                if(j < 3)
                    System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Sample conditional probability table for Q(x) (rounded to 4 digits below zero) : ");
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(DF.format(sampleQ[i][j]));
                if(j < 3)
                    System.out.print(" | ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("Data summary of first 5 SHORT ORFs : ");
        for(double[] d : foundS){
            System.out.println("Start Pos: " + d[0] + " | End Pos: " + d[1] + " | Length: " + d[2]
                    + " | Markov Score: " + d[3] + " | In GenBank Database: " + d[4]);
        }
        System.out.println();

        System.out.println("Data summary of first 5 LONG ORFs : ");
        for(double[] d : foundL){
            System.out.println("Start Pos: " + d[0] + " | End Pos: " + d[1] + " | Length: " + d[2]
                    + " | Markov Score: " + d[3] + " | In GenBank Database: " + d[4]);
        }
        System.out.println();

        System.out.println("Maximum length threshold for 80%+ true positives : ");
        double totalL = 0; //total # of ORFs over threshold
        double totalTL = 0; //total # of ORFs over threshold and positive
        double positiveRateL = 0; //current positive rate
        int thresholdL = 0; //current threshold
        while(positiveRateL < 80){
            double temp1 = 0;
            double temp2 = 0;
            thresholdL++;
            for(double[] d: fullReport){
                if(d[2] > thresholdL) {
                    temp1++;
                    if(d[4] == 1)
                        temp2++;
                }
            }
            positiveRateL = temp2 / temp1 * 100;
            totalL = temp1;
            totalTL = temp2;
        }
        System.out.println(thresholdL + " | True Positives: " + totalTL + " | " +
                "False Positives: " + (totalL - totalTL)+ "\n");

        System.out.println("Maximum MM-score threshold for 80%+ true positives : ");
        double totalM = 0; //total # of ORFs over threshold
        double totalTM = 0; //total # of ORFs over threshold and positive
        double positiveRateM = 0; //current positive rate
        double thresholdM = 0.0; //current threshold
        while(positiveRateM < 80){
            double temp1 = 0;
            double temp2 = 0;
            thresholdM += 0.1;
            for(double[] d: fullReport){
                if(d[3] > thresholdM) {
                    temp1++;
                    if(d[4] == 1)
                        temp2++;
                }
            }
            positiveRateM = temp2 / temp1 * 100;
            totalM = temp1;
            totalTM = temp2;
        }
        System.out.println(thresholdM + " | True Positives: " + totalTM + " | " +
                "False Positives: " + (totalM - totalTM) + "\n");

        System.out.println("Short ORF average per-nucleotide contribution to MM-score : ");
        double lengthTotal = 0;
        double scoreTotal = 0;
        for(double[] d : fullReport){
            if(d[2] < 151){
                lengthTotal += d[2];
                scoreTotal += d[3];
            }
        }
        System.out.println(scoreTotal/lengthTotal + "\n");

        System.out.println("Determining predicted gene using distance from line : ");
        //using y = -0.05986x + 5 ... 0.05986x + y - 5 = 0
        double lineTrue = 0;
        double lineFalse = 0;
        for(double[] d : fullReport){
            double distance = (0.05986 * d[2] + d[3] - 5) / Math.sqrt(Math.pow(0.05986, 2) + 1);
            if(distance < 0){
                if(d[4] == 1)
                    lineTrue++;
                else
                    lineFalse++;
            }
        }
        System.out.println("True Positives: " + lineTrue + " | " + "False Positives: " + lineFalse
                + " | " + "True Positive Rate: " + lineTrue/(lineTrue+lineFalse)*100 + "%\n");

        System.out.println("Maximum distance from line threshold for 80%+ true positives : ");
        double lineThreshT = 0;
        double lineThreshF = 0;
        double lineCurrThresh = 100;
        double linePositiveRate = 0;
        while(linePositiveRate < 80){
            lineCurrThresh--;
            double temp1 = 0;
            double temp2 = 0;
            for (double[] d : fullReport) {
                double distance = (0.05986 * d[2] + d[3] - lineCurrThresh) / Math.sqrt(Math.pow(0.05986, 2) + 1);
                if (distance < 0) {
                    if (d[4] == 1)
                        temp1++;
                    else
                        temp2++;
                }
            }
            linePositiveRate = temp1/(temp1+temp2)*100;
            lineThreshT = temp1;
            lineThreshF = temp2;
        }
        System.out.println(lineCurrThresh + " | True Positives: " + lineThreshT + " | " +
                "False Positives: " + lineThreshF);
        System.out.println("------------------------------------ End Report ------------------------------------");
    }
}
