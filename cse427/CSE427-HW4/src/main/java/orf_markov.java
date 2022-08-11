import java.util.ArrayList;
import java.util.HashMap;

/*
 Class for training the markov model and
 scoring a given sequence based on the training results.
 */
public class orf_markov {
    //Map char to int (A = 0, C = 1, G = 2, T = 3)
    private static final HashMap<Character, Integer> INDEX = new HashMap<>(){{
        put('A', 0);
        put('C', 1);
        put('G', 2);
        put('T', 3);
    }};

    //Map int to char (reversed index) (A = 0, C = 1, G = 2, T = 3)
    private static final HashMap<Integer, Character> INDEX_REVERSE = new HashMap<>(){{
        put(0, 'A');
        put(1, 'C');
        put(2, 'G');
        put(3, 'T');
    }};

    private HashMap<String, double[]> freq; //holding frequencies, double[] = double[4] (0=A, 1=C, 2=G, 3=T)
    private HashMap<String, double[]> freq_reverse; //holding frequencies, double[] = double[4] (0=A, 1=C, 2=G, 3=T)

    /*
     Default constructor. Initiates global variables.
     */
    public orf_markov(){
        freq = new HashMap<>();
        freq_reverse = new HashMap<>();
    }

    /*
     Scores the given string based on training.
     Result is log(P(x)/Q(x)).

     @param  s : sequence to score

     @return double : markov model score of given string
     */
    public double score(String s){
        if(s.length() < 5)
            return 0.0;

        double pVal = 0; //log P(x)
        double qVal = 0; //log Q(x)

        int i = 0;
        while(i + 5 < s.length()){
            double tempP = freq.get(s.substring(i, i+5))[INDEX.get(s.charAt(i+5))];
            double tempQ = freq_reverse.get(s.substring(i, i+5))[INDEX.get(s.charAt(i+5))];
            pVal += Math.log(tempP);
            qVal += Math.log(tempQ);
            i++;
        }

        //log(x/y) = log(x) - log(y)
        return (pVal - qVal);
    }

    /*
     Returns the arbitrary P(x) and Q(x) score for given string.

     @param  s : sequence to score

     @return double[] : double[0] = P(x), double[1] = Q(x)
     */
    public double[] arbiScore(String s){
        double pVal = 1;
        double qVal = 1;
        int i = 0;
        while(i + 5 < s.length()){
            double tempP = freq.get(s.substring(i, i+5))[INDEX.get(s.charAt(i+5))];
            double tempQ = freq_reverse.get(s.substring(i, i+5))[INDEX.get(s.charAt(i+5))];
            pVal = pVal * tempP;
            qVal = qVal * tempQ;
            i++;
        }
        return new double[]{pVal, qVal};
    }

    /*
     Trains the markov model.
     Training parameters are outlined in the assignment.
     Automatically calls on the reverseTrain() to create Q(x) (background).

     @param  data : List of strings to be used in training (long ORFs)

     @see reverseTrain()
     */
    public void train(ArrayList<String> data){
        HashMap<String, int[]> count = shipDefaultMap(); //holding counts, int[] = int[4] (0=A, 1=C, 2=G, 3=T)

        // counting (for k=5)
        for(String s : data){
            int i = 0;
            while(i + 5 < s.length()){
                int curr = count.get(s.substring(i, i+5))[INDEX.get(s.charAt(i+5))];
                count.get(s.substring(i, i+5))[INDEX.get(s.charAt(i+5))] = curr + 1; //increment by 1 (counted)
                i++;
            }
        }

        //creating frequency matrix
        for(String s : count.keySet()){
            //total count per sample (adding 4 for pseudo count)
            double total = count.get(s)[0] + count.get(s)[1] + count.get(s)[2] + count.get(s)[3] + 4;
            double[] temp = new double[4];
            temp[0] = (count.get(s)[0] + 1) / total; //adding 1 for pseudo count
            temp[1] = (count.get(s)[1] + 1) / total;
            temp[2] = (count.get(s)[2] + 1) / total;
            temp[3] = (count.get(s)[3] + 1) / total;

            freq.put(s, temp);
        }

        reverseTrain(data);
    }

    /*
     Helper method for train().
     Creates a reverse complement of training set and trains off it.

     @param  data : List of strings to be used in training (long ORFs)

     @see train()
     */
    private void reverseTrain(ArrayList<String> data){
        HashMap<String, int[]> count = shipDefaultMap(); //holding counts, int[] = int[4] (0=A, 1=C, 2=G, 3=T)

        //counting (for k=5)
        for(String s : data){
            String rev = reverse(s); //reverse complement
            int i = 0;
            while(i + 5 < rev.length()){
                int curr = count.get(rev.substring(i, i+5))[INDEX.get(rev.charAt(i+5))];
                count.get(rev.substring(i, i+5))[INDEX.get(rev.charAt(i+5))] = curr + 1; //increment by 1 (counted)
                i++;
            }
        }

        //creating frequency matrix
        for(String s : count.keySet()){
            //total count per sample (adding 4 for pseudo count)
            double total = count.get(s)[0] + count.get(s)[1] + count.get(s)[2] + count.get(s)[3] + 4;
            double[] temp = new double[4];
            temp[0] = (count.get(s)[0] + 1) / total; //adding 1 for pseudo count
            temp[1] = (count.get(s)[1] + 1) / total;
            temp[2] = (count.get(s)[2] + 1) / total;
            temp[3] = (count.get(s)[3] + 1) / total;

            freq_reverse.put(s, temp);
        }
    }

    /*
     Creates the default map.
     Since we are using k=5, the keys for the map are all possible combination of A,C,G,T in 5 digits.

     @return HashMap<String, int[]> : default map
     */
    private HashMap<String, int[]> shipDefaultMap(){
        HashMap<String, int[]> temp = new HashMap<>();

        //creating all possible 5-letter combination between A,C,G,T (k=5)
        for(int a = 0; a < 4; a++){
            for(int b = 0; b < 4; b++){
                for(int c = 0; c < 4; c++){
                    for(int d = 0; d < 4; d++){
                        for(int e = 0; e < 4; e++){
                            String s = "" + INDEX_REVERSE.get(a) + INDEX_REVERSE.get(b) + INDEX_REVERSE.get(c)
                                    + INDEX_REVERSE.get(d) + INDEX_REVERSE.get(e);
                            temp.put(s, new int[4]);
                        }
                    }
                }
            }
        }

        return temp;
    }

    /*
     Finds and returns the reverse complement of given sequence.
     Reverse complement = A <-> T and C <-> G.

     @param  s : String to find the reverse complement of

     @return String : Reverse complemented string
     */
    private String reverse(String s){
        String result = "";
        String temp = new StringBuilder(s).reverse().toString(); //first reverse the string
        //swapping out the characters (A<->T, C<->G)
        for(char c : temp.toCharArray()){
            if(c == 'A')
                result += 'T';
            if(c == 'T')
                result += 'A';
            if(c == 'C')
                result += 'G';
            if(c == 'G')
                result += 'C';
        }
        return result;
    }
}
