import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/*
Class that handles the HMM Viterbi iteration.
Takes the given string of data and iterates over it.
 */
public class HMM {
    // Map char to int (A = 0, C = 1, G = 2, T = 3)
    private static final HashMap<Character, Integer> INDEX = new HashMap<>(){{
        put('A', 0);
        put('C', 1);
        put('G', 2);
        put('T', 3);
    }};
    // Beginning transition values
    private static final double[] INIT_TRANS = new double[]{0.9999, 0.0001};
    private static final DecimalFormat DF = new DecimalFormat("#.######"); // decimal format

    private static String target; // Target string of data
    private double[][] HMMs1; // Iteration of state 1
    private double[][] HMMs2; // Iteration of state 2
    private double[][] maxVal; // Max value of HMMs1 & HMMs2
    private double[][] emissions; // emission rates
    private double[][] transitions; // transition rates

    /*
    Default constructor of this class.
    Initializes the fields and sets the target string to the given one.
     */
    public HMM(String target){
        this.target = target;

        this.HMMs1 = new double[2][target.length()]; // HMMs1[0] is State 1, [1] is State 2
        this.HMMs2 = new double[2][target.length()]; // HMMs2[0] is State 1, [1] is State 2
        this.maxVal = new double[2][target.length()]; // maxVal[0] is State 1, [1] is State 2

        // for the following 2 fields : [0] = State 1, [1] = State 2
        this.emissions = new double[][]{{0.25, 0.25, 0.25, 0.25}, {0.20, 0.30, 0.30, 0.20}}; // default values
        this.transitions = new double[][]{{0.9999, 0.0001}, {0.01, 0.99}}; // default values

        DF.setRoundingMode(RoundingMode.CEILING); // set decimal format for printing (rounding up to 6th decimal place)
    }

    /*
    Iterator class; iterates the HMM Viterbi given number of times.
    Also prints the findings according to the given guideline.

    @param  int : number of times to iterate
     */
    public void iterate(int n){
        for(int i = 1; i <= n; i++) {
            System.out.println("--------------- < Iteration " + i + " Start > ---------------");

            System.out.println("Emission Parameters Used : ");
            System.out.println("State 1 : (A = " + DF.format(emissions[0][0]) + "), (C = " + DF.format(emissions[0][1]) + "), " +
                    "(G = " + DF.format(emissions[0][2]) + "), (T = " + DF.format(emissions[0][3]) + ")");
            System.out.println("State 2 : (A = " + DF.format(emissions[1][0]) + "), (C = " + DF.format(emissions[1][1]) + "), " +
                    "(G = " + DF.format(emissions[1][2]) + "), (T = " + DF.format(emissions[1][3]) + ")\n");

            System.out.println("Transition Parameters Used : ");
            System.out.println("State 1 -> State 1 : " + DF.format(transitions[0][0]));
            System.out.println("State 1 -> State 2 : " + DF.format(transitions[0][1]));
            System.out.println("State 2 -> State 1 : " + DF.format(transitions[1][0]));
            System.out.println("State 2 -> State 2 : " + DF.format(transitions[1][1]) + "\n");

            train(); // train
            int[] traceback = traceback(); // traceback the training
            ArrayList<int[]> hits = findHits(traceback); // find hits

            System.out.println("Log probability of overall Viterbi path : "
                    + DF.format(maxVal[traceback[traceback.length - 1]][target.length() - 1]) + "\n");

            System.out.println("Total number of hits found : " + hits.size() + "\n");

            int count = 0;
            while((i == 10 && count < hits.size()) || (count < 5 && count < hits.size())){
                System.out.println("> Hit #" + (count+1) + " : ");
                System.out.println("Starting Position: " + hits.get(count)[0]
                        + " | Ending Position: " + hits.get(count)[1]
                        + " | Length: " + (hits.get(count)[1] - hits.get(count)[0] + 1));
                count++;
            }

            System.out.println("--------------- < Iteration " + i + " End > ---------------\n");
        }
    }

    /*
    Helper class for iterate().
    Train, depending on the given data.
    log(a*b) = log(a) + log(b), using this for more precision (but maybe should've used BigDecimal for all).

    @see    iterate()
     */
    private void train(){
        // accounting first char
        int index = INDEX.get(target.charAt(0));
        HMMs1[0][0] = Math.log(INIT_TRANS[0]) + Math.log(emissions[0][index]); // Math.log uses base e by default
        HMMs2[0][0] = Math.log(INIT_TRANS[1]) + Math.log(emissions[1][index]);
        maxVal[0][0] = HMMs1[0][0]; // storing max values
        maxVal[1][0] = HMMs2[0][0];

        // training for rest of the char in target string
        for(int i = 1; i < target.length(); i++){
            index = INDEX.get(target.charAt(i));
            HMMs1[0][i] = Math.log(transitions[0][0]) + Math.log(emissions[0][index]) + maxVal[0][i - 1]; // -> s1
            HMMs1[1][i] = Math.log(transitions[1][0]) + Math.log(emissions[0][index]) + maxVal[1][i - 1];

            HMMs2[0][i] = Math.log(transitions[0][1]) + Math.log(emissions[1][index]) + maxVal[0][i - 1]; // -> s2
            HMMs2[1][i] = Math.log(transitions[1][1]) + Math.log(emissions[1][index]) + maxVal[1][i - 1];

            maxVal[0][i] = Math.max(HMMs1[0][i], HMMs1[1][i]); // storing max values
            maxVal[1][i] = Math.max(HMMs2[0][i], HMMs2[1][i]);
        }
    }

    /*
    Helper class for iterate().
    Traceback the HMM matrix.
    Going in reverse order due to the nature of traceback.
    Also creates a new transition rate matrix from learning.

    @return int[] : Array representing path of traceback

    @see    iterate()
     */
    private int[] traceback(){
        int[] path = new int[target.length()]; // recording path
        double[][] count = new double[2][2]; // used to change(train) the current transition values

        // first case (going backwards)
        if(maxVal[0][path.length - 1] > maxVal[1][path.length - 1])
            path[path.length - 1] = 0;
        else
            path[path.length - 1] = 1;

        // traversing and determining paths (going backwards)
        for(int i = path.length - 2; i >= 0; i--){
            BigDecimal b1 = new BigDecimal(maxVal[0][i + 1]); // using BigDecimal for precision
            BigDecimal b2 = new BigDecimal(HMMs1[1][i + 1]);  // we are losing runtime efficiency, but precision
            BigDecimal b3 = new BigDecimal(maxVal[1][i + 1]); // seemed a bit more important in this case
            BigDecimal b4 = new BigDecimal(HMMs2[1][i + 1]);  // should've used BigDecimal all the way through...

            if( ((path[i + 1] == 0) && (b1.compareTo(b2) == 0))
                    || ((path[i + 1] == 1) && (b3.compareTo(b4) == 0)) )
                path[i] = 1;
            else
                path[i] = 0;

            // saving counts for constructing new transition matrix
            if(path[i + 1] == 0)
                count[path[i]][0] = count[path[i]][0] + 1;
            else
                count[path[i]][1] = count[path[i]][1] + 1;
        }

        // changing transition matrix
        double val00 = (count[0][0] / (count[0][0] + count[0][1]));
        double val11 = (count[1][1] / (count[1][0] + count[1][1]));
        this.transitions = new double[][]{
                {val00, (1 - val00)},
                {(1 - val11), val11}
        }; // new transition rates

        return path; // return traceback
    }

    /*
    Helper class for iterate().
    Finds the hits, given a traceback.
    Also creates a new emission rate matrix from learning.

    @param  int[] : Array representing path of traceback

    @return ArrayList<Int[]> : ArrayList of hits (int[] represents the beginning & ending index of the hit)

    @see    iterate()
     */
    private ArrayList<int[]> findHits(int[] path){
        ArrayList<int[]> hits = new ArrayList<>(); // list of hits (int[0] : begin index / int[1] : end index)
        double[][] newEmissions = new double[2][4]; // used to change(train) the current emission values

        int[] hit = null; // storing a hit (temp[0] = beginning index of hit, temp[1] = ending index of hit)

        // finding hits
        for(int i = 0; i < path.length; i++){
            if(path[i] == 1 && hit == null){ // we found a hit and is not counting over a hit (hit == null)
                hit = new int[2]; // begin index
                hit[0] = i + 1; // accounting for the 1-base of genomic positions
            } else if(path[i] == 0 && hit != null){ // we are counting through a hit (hit != null)
                hit[1] = i; // end index
                hits.add(hit); // a hit is determined, add to list
                hit = null; // reset hit
            }

            // saving counts for constructing new emission matrix
            newEmissions[path[i]][INDEX.get(target.charAt(i))] = newEmissions[path[i]][INDEX.get(target.charAt(i))] + 1;
        }

        // changing emission matrix
        double s1 = newEmissions[0][0] + newEmissions[0][1] + newEmissions[0][2] + newEmissions[0][3];
        double s2 = newEmissions[1][0] + newEmissions[1][1] + newEmissions[1][2] + newEmissions[1][3];
        this.emissions = new double[][]{
                {(newEmissions[0][0]/s1), (newEmissions[0][1]/s1), (newEmissions[0][2]/s1), (newEmissions[0][3]/s1)},
                {(newEmissions[1][0]/s2), (newEmissions[1][1]/s2), (newEmissions[1][2]/s2), (newEmissions[1][3]/s2)}
        }; // new emission rates

        return hits; // return list of hits
    }
}
