import java.util.ArrayList;
import java.util.Arrays;

public class functions {

    private double entHold = 0.0;

    public functions(){ }

    public double[][] makeCountMatrix(ArrayList<String> m, int length){
        // assume all sequences given are same length
        double[][] temp = new double[4][length]; // the rows are ordered A, C, G, T

        // building count matrix
        for(String s : m){
            for(int i = 0; i < length; i++){
                if(s.charAt(i) == 'A')
                    temp[0][i] = temp[0][i] + 1;
                else if(s.charAt(i) == 'C')
                    temp[1][i] = temp[1][i] + 1;
                else if(s.charAt(i) == 'G')
                    temp[2][i] = temp[2][i] + 1;
                else
                    temp[3][i] = temp[3][i] + 1;
            }
        }

        return temp;
    }

    public double[][] addPseudo(double[][] cm, double[] pseudo){
        // assume pseudo count vector is length 4
        double[][] temp = cm; // the rows are ordered A, C, G, T

        // adding pseudo vector
        for(int i = 0; i < temp[0].length; i++){
            temp[0][i] = temp[0][i] + pseudo[0];
            temp[1][i] = temp[1][i] + pseudo[1];
            temp[2][i] = temp[2][i] + pseudo[2];
            temp[3][i] = temp[3][i] + pseudo[3];
        }

        return temp;
    }

    public double[][] makeFrequencyMatrix(double[][] cm){
        double[][] temp = new double[4][cm[0].length]; // the rows are ordered A, C, G, T

        // building frequency matrix
        for(int i = 0; i < cm[0].length; i++){
            double sum = cm[0][i] + cm[1][i] + cm[2][i] + cm[3][i];
            temp[0][i] = cm[0][i] / sum;
            temp[1][i] = cm[1][i] / sum;
            temp[2][i] = cm[2][i] / sum;
            temp[3][i] = cm[3][i] / sum;
        }

        return temp;
    }

    public double entropy(double[][] fm, double[][] wm){
        double val = 0.0;
        for(int i = 0; i < fm[0].length; i++){
            val += ((fm[0][i] * wm[0][i]) + (fm[1][i] * wm[1][i]) + (fm[2][i] * wm[2][i]) + (fm[3][i] * wm[3][i]));
        }
        return val;
    }

    public double[][] makeWMM(double[][] fm){
        double[][] temp = new double[4][fm[0].length]; // the rows are ordered A, C, G, T

        // building weight matrix
        for(int i = 0; i < fm[0].length; i++){
            for(int j = 0; j < 4; j++){
                temp[j][i] = Math.log(fm[j][i]/0.25) / Math.log(2);
            }
        }

        return temp;
    }

    public double[] scanWMM(double[][] WMM, String sequences){
        double[] temp = new double[sequences.length() - 13 + 1];

        for(int i = 0; i < (sequences.length() - 13 + 1); i++){
            double score = 0.0;
            String target = sequences.substring(i, i + 13);
            for(int j = 0; j < 13; j++){
                if(target.charAt(j) == 'A')
                    score += WMM[0][j];
                else if(target.charAt(j) == 'C')
                    score += WMM[1][j];
                else if(target.charAt(j) == 'G')
                    score += WMM[2][j];
                else if(target.charAt(j) == 'T')
                    score += WMM[3][j];
            }
            temp[i] = score;
        }

        return temp;
    }

    public double[][] Estep(double[][] WMM, ArrayList<String> target){
        double[][] temp = new double[target.size()][target.get(0).length() - 13 + 1];

        int rowCount = 0;
        for(String s : target){
            double[] scan = scanWMM(WMM, s);
            double[] result = new double[scan.length];

            double sum = 0.0;
            for(double d : scan)
                sum += Math.pow(2, d);

            for(int i = 0; i < result.length; i++)
                result[i] = Math.pow(2, scan[i]) / sum;

            for(int j = 0; j < result.length; j++)
                temp[rowCount][j] = result[j];

            rowCount++;
        }

        return temp;
    }

    public double[][] Mstep(double[][] exp, ArrayList<String> m){
        double[][] temp = makeCountMatrix(m, m.get(0).length() - 13 + 1);
        for(int i = 0; i < temp.length; i++){
            for(int j = 0; j < temp[i].length; j++){
                temp[i][j] = temp[i][j] * exp[i][j];
            }
        }

        temp = addPseudo(temp, new double[]{0.25, 0.25, 0.25, 0.25});
        double[][] freq = makeFrequencyMatrix(temp);
        temp = makeWMM(freq);

        entHold = entropy(freq, temp);

        return temp;
    }

    public double[][] initEM(ArrayList<String> m){
        ArrayList<double[][]> counts = new ArrayList<>(); // count matrices (made by brute forcing)
        ArrayList<double[][]> freq = new ArrayList<>(); // frequency matrices (made from "counts")
        ArrayList<double[][]> WMMs = new ArrayList<>(); // WMMs (made from "freq")

        String target = m.get(0); // grabbing first input sequence
        ArrayList<String> motifs = new ArrayList<>(); // holding sampled motifs from first sequence

        // brute forcing motifs
        for(int i = 0; i < 101; i++){
            motifs.add(target.substring(i, i + 13));
        }

        // balancing so only 85% of original (+= 0.0625) ... added to counts
        for(String s : motifs)
            counts.add(addPseudo(makeCountMatrix(new ArrayList<>(Arrays.asList(s)), 13), new double[]{0.0625, 0.0625, 0.0625, 0.0625}));

        // making frequency matrices from counts ... added to freq
        for(double[][] fd : counts)
            freq.add(makeFrequencyMatrix(fd));

        // making WMM from freq ... added to WMMs
        for(double[][] wd : freq)
            WMMs.add(makeWMM(wd));

        // ---- Iteration ----
        double[][] WMMD = null;
        double[][] WMME = null;
        double highestEntropy = Double.MIN_VALUE;

        double[][] entropyList = new double[101][11];

        for(int i = 0; i < WMMs.size(); i++){
            entropyList[i][0] = entropy(freq.get(i), WMMs.get(i)); // storing beginning entropy
            double[][] next = WMMs.get(i);

            for(int j = 1; j < 11; j++){
                double[][] exp = Estep(next, m); // E-step
                double[][] mod = Mstep(exp, m); // M-step
                next = mod;

                // adding to entropy list & checking if entropy is higher
                entropyList[i][j] = entHold;
                if(highestEntropy < entHold) {
                    WMMD = next;
                    highestEntropy = entHold;
                }

                // save motif #51
                if(i == 50)
                    WMME = next;
            }
        }

        // printing entropy list
        System.out.println("Entropy List");
        for (int i = 0; i < entropyList.length; i++) {
            for (int j = 0; j < entropyList[i].length; j++) {
                System.out.print((double) Math.round(entropyList[i][j]) / 100 + " ");
            }
            System.out.println();
        }

        // printing WMMD
        System.out.println("WMMD");
        for (int i = 0; i < WMMD.length; i++) {
            for (int j = 0; j < WMMD[i].length; j++) {
                System.out.print((double) Math.round(WMMD[i][j]) / 100 + " ");
            }
            System.out.println();
        }

        // printing WMMD
        System.out.println("WMME");
        for (int i = 0; i < WMME.length; i++) {
            for (int j = 0; j < WMME[i].length; j++) {
                System.out.print((double) Math.round(WMME[i][j]) / 100 + " ");
            }
            System.out.println();
        }

        return WMMD;
    }

    public void evaluation(double[][] wmmd, ArrayList<String> target){
        // couldn't make this
    }
}
