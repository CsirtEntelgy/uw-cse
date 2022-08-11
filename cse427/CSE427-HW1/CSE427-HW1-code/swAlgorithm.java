import java.util.HashMap;
import java.util.Random;

public class swAlgorithm {

    private HashMap<Character, HashMap<Character, Integer>> blosum62; //BLOSUM62 scoring matrix

    private int[][] scoreMatrix; //the scoring matrix

    private int[][] traceBackMatrix; //storing direction for traceback
    private static final int START = 0; //constant representing starting position of traceback
    private static final int UP = 1; //constant representing up direction of traceback
    private static final int LEFT = 2; //constant representing left direction of traceback
    private static final int DIAGONAL = 3; //constant representing diagonal direction of traceback

    private String amino1; //holds amino acid 1
    private String amino2; //holds amino acid 2
    private String amino1Id; //holds amino acid 1's id
    private String amino2Id; //holds amino acid 2's id

    /*
    Default constructor.
    Initializes the matrices by default.
    Populates the blosum62 table by default.
     */
    public swAlgorithm(){
        scoreMatrix = new int[0][0];
        traceBackMatrix = new int[0][0];
        populateBLOSUM62();
    }

    /*
    Evaluate given amino acids with the blosum62 table.
    Stores the score, optimal alignment, and empirical p-value as private variable.

    @param  amino1 : FASTA formatted string of amino acid 1
    @param  amino1Id : Identifier for amino1
    @param  amino2 : FASTA formatted string of amino acid 2
    @param  amino2ID : Identifier for amino1
    @param  perm : number of permutations to evaluate
    @param  isPerm : indicates if this run of method is a permutation (if so skip the printing process)
     */
    public int evaluate(String amino1, String amino1Id, String amino2, String amino2Id, int perm, Boolean isPerm){
        //some initialization
        this.amino1 = amino1;
        this.amino2 = amino2;
        this.amino1Id = amino1Id;
        this.amino2Id = amino2Id;
        scoreMatrix = new int[amino1.length() + 1][amino2.length() + 1];
        traceBackMatrix = new int[amino1.length() + 1][amino2.length() + 1];

        //filling first row & column
        scoreMatrix[0][0] = 0;
        traceBackMatrix[0][0] = 0;

        for(int i = 1; i < amino1.length() + 1; i++){
            scoreMatrix[i][0] = 0;
            traceBackMatrix[i][0] = 0;
        }

        for(int i = 1; i < amino2.length() + 1; i++){
            scoreMatrix[0][i] = 0;
            traceBackMatrix[0][i] = 0;
        }

        //populating rest of matrix
        for(int i = 1; i < amino1.length() + 1; i++){
            for(int j = 1; j < amino2.length() + 1; j++){
                //evaluate moves
                int upScore = scoreMatrix[i][j - 1] + score(0, j); //0 stands for gap ('-')
                int leftScore = scoreMatrix[i - 1][j] + score(i, 0); //0 stands for gap ('-')
                int diagScore = scoreMatrix[i - 1][j - 1] + score(i, j);

                //chose best move
                scoreMatrix[i][j] = Math.max(upScore, Math.max(leftScore, diagScore));

                //record chosen move
                if(scoreMatrix[i][j] == upScore)
                    traceBackMatrix[i][j] = 1;
                else if(scoreMatrix[i][j] == leftScore)
                    traceBackMatrix[i][j] = 2;
                else if(scoreMatrix[i][j] == diagScore)
                    traceBackMatrix[i][j] = 3;
            }
        }

        int maxScore = maxScore();

        if(isPerm)
            return maxScore; //if this is a permutation run, skip the printing process and return result

        //printing identifiers
        System.out.println("s1 = " + amino1Id + ", compared with, s2 = " + amino2Id);
        System.out.println();

        //printing score
        System.out.println("Score of this evaluation's optimal alignment is : ");
        System.out.println(maxScore);
        System.out.println();

        //printing optimal alignment
        System.out.println("Printing optimal alignment : ");
        for(int i = 0; i < amino1.length() + 1; i++) {
            for(int j = 0; j < amino2.length() + 1; j++) {
                if(scoreMatrix[i][j] == maxScore)
                    traceBack(i, j, "", "");
            }
        }
        System.out.println();

        //print score matrix if both strings are less than 15 characters
        if(amino1.length() < 15 && amino2.length() < 15) {
            System.out.println("Printing score matrix : ");
            for(int i = 0; i < amino1.length() + 1; i++){
                for(int j = 0; j < amino2.length() + 1; j++){
                    System.out.print(scoreMatrix[i][j]);
                    if(scoreMatrix[i][j] < 0)
                        System.out.print(" ");
                    else
                        System.out.print("  ");
                }
                System.out.print("\n");
            }
            System.out.println();
        }

        //print empirical p-value when N>0
        if(perm > 0){
            int counter = 0;
            for(int i = 0; i < perm; i++){
                int temp = evaluate(amino1, "", permutation(amino2), "", 0, true);
                if(temp > maxScore)
                    counter++;
            }
            System.out.println("Printing empirical p-value : ");
            double epVal = counter + 1 / perm + 1;
            System.out.println(epVal);
            System.out.println();
        }

        return maxScore;
    }

    /*
    Scoring method.
    Uses -4 for linear gap & uses BLOSUM62 matrix for scoring.

    @param  a1 : an alphabet from amino1
    @param  a2 : an alphabet from amino2

    @return int : evaluated score between the two alphabets
     */
    private int score(int a1, int a2){
        if(a1 == 0 || a2 == 0)
            return -4;
        return blosum62.get(Character.toUpperCase(amino1.charAt(a1 - 1))).get(Character.toUpperCase(amino2.charAt(a2 - 1)));
    }

    /*
    Returns the maximum score recorded in the score matrix.

    @return int : maximum score in score matrix
     */
    private int maxScore(){
        int max = 0;
        for(int i = 1; i < amino1.length() + 1; i++) {
            for (int j = 1; j < amino2.length() + 1; j++) {
                if(scoreMatrix[i][j] > max)
                    max = scoreMatrix[i][j];
            }
        }
        return max;
    }

    /*
    Traceback the score matrix and print the optimal alignment
     */
    private void traceBack(int x, int y, String trace1, String trace2){
        if(traceBackMatrix[x][y] == 0){ //terminal (reached start)
            wrapString(trace1, trace2, x, y);
            return;
        }

        if(traceBackMatrix[x][y] == 1) //up
            traceBack(x, y-1, "-" + trace1, Character.toUpperCase(amino2.charAt(y-1)) + trace2);
        else if(traceBackMatrix[x][y] == 2) //left
            traceBack(x - 1, y, Character.toUpperCase(amino1.charAt(x-1)) + trace1, "-" + trace2);
        else if(traceBackMatrix[x][y] == 3) //diagonal
            traceBack(x - 1, y - 1, Character.toUpperCase(amino1.charAt(x-1)) + trace1, Character.toUpperCase(amino2.charAt(y-1)) + trace2);
    }

    /*
    Returns a permutation of given string, swapping two chars.

    @return String : string after permutation of given string
     */
    private String permutation(String target){
        Random rand = new Random();
        int a = rand.nextInt(target.length());
        int b = rand.nextInt(target.length());

        char[] arr = target.toCharArray();
        char temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;

        return new String(arr);
    }

    /*
    Wraps the given string by 60 characters, and prints it.
    The formatting is following instructions.
     */
    private void wrapString(String s1, String s2, int x, int y){
        int wraps1 = (int)(s1.length() / 60) + 1;

        for(int i = 1; i < wraps1; i++){
            System.out.println(amino1Id + ": \t" + (x + (i - 1) * 60) + "\t" + s1.substring((i - 1) * 60, i * 60));
            System.out.println(amino2Id + ": \t" + (y + (i - 1) * 60) + "\t" + s2.substring((i - 1) * 60, i * 60));
        }

        System.out.println(amino1Id + ": \t" + (x + (wraps1 - 1) * 60) + "\t" + s1.substring((wraps1 - 1) * 60));
        System.out.println(amino2Id + ": \t" + (y + (wraps1 - 1) * 60) + "\t" + s2.substring((wraps1 - 1) * 60));
    }

    /*
    Hard-code the BLOSUM62 scoring matrix into blosum62 variable.
    Hopefully there isn't a human-error in here.
     */
    private void populateBLOSUM62(){
        blosum62 = new HashMap<>();

        blosum62.put('A', new HashMap<>());
        blosum62.get('A').put('A', 4);
        blosum62.get('A').put('R', -1);
        blosum62.get('A').put('N', -2);
        blosum62.get('A').put('D', -2);
        blosum62.get('A').put('C', 0);
        blosum62.get('A').put('Q', -1);
        blosum62.get('A').put('E', -1);
        blosum62.get('A').put('G', 0);
        blosum62.get('A').put('H', -2);
        blosum62.get('A').put('I', -1);
        blosum62.get('A').put('L', -1);
        blosum62.get('A').put('K', -1);
        blosum62.get('A').put('M', -1);
        blosum62.get('A').put('F', -2);
        blosum62.get('A').put('P', -1);
        blosum62.get('A').put('S', 1);
        blosum62.get('A').put('T', 0);
        blosum62.get('A').put('W', -3);
        blosum62.get('A').put('Y', -2);
        blosum62.get('A').put('V', 0);

        blosum62.put('R', new HashMap<>());
        blosum62.get('R').put('A', -1);
        blosum62.get('R').put('R', 5);
        blosum62.get('R').put('N', 0);
        blosum62.get('R').put('D', -2);
        blosum62.get('R').put('C', -3);
        blosum62.get('R').put('Q', 1);
        blosum62.get('R').put('E', 0);
        blosum62.get('R').put('G', -2);
        blosum62.get('R').put('H', 0);
        blosum62.get('R').put('I', -3);
        blosum62.get('R').put('L', -2);
        blosum62.get('R').put('K', 2);
        blosum62.get('R').put('M', -1);
        blosum62.get('R').put('F', -3);
        blosum62.get('R').put('P', -2);
        blosum62.get('R').put('S', -1);
        blosum62.get('R').put('T', -1);
        blosum62.get('R').put('W', -3);
        blosum62.get('R').put('Y', -2);
        blosum62.get('R').put('V', -3);

        blosum62.put('N', new HashMap<>());
        blosum62.get('N').put('A', -2);
        blosum62.get('N').put('R', 0);
        blosum62.get('N').put('N', 6);
        blosum62.get('N').put('D', 1);
        blosum62.get('N').put('C', -3);
        blosum62.get('N').put('Q', 0);
        blosum62.get('N').put('E', 0);
        blosum62.get('N').put('G', 0);
        blosum62.get('N').put('H', 1);
        blosum62.get('N').put('I', -3);
        blosum62.get('N').put('L', -3);
        blosum62.get('N').put('K', 0);
        blosum62.get('N').put('M', -2);
        blosum62.get('N').put('F', -3);
        blosum62.get('N').put('P', -2);
        blosum62.get('N').put('S', 1);
        blosum62.get('N').put('T', 0);
        blosum62.get('N').put('W', 4);
        blosum62.get('N').put('Y', -2);
        blosum62.get('N').put('V', -3);

        blosum62.put('D', new HashMap<>());
        blosum62.get('D').put('A', -2);
        blosum62.get('D').put('R', -2);
        blosum62.get('D').put('N', 1);
        blosum62.get('D').put('D', 6);
        blosum62.get('D').put('C', -3);
        blosum62.get('D').put('Q', 0);
        blosum62.get('D').put('E', 2);
        blosum62.get('D').put('G', -1);
        blosum62.get('D').put('H', -1);
        blosum62.get('D').put('I', -3);
        blosum62.get('D').put('L', -4);
        blosum62.get('D').put('K', -1);
        blosum62.get('D').put('M', -3);
        blosum62.get('D').put('F', -3);
        blosum62.get('D').put('P', -1);
        blosum62.get('D').put('S', 0);
        blosum62.get('D').put('T', -1);
        blosum62.get('D').put('W', -4);
        blosum62.get('D').put('Y', -3);
        blosum62.get('D').put('V', -3);

        blosum62.put('C', new HashMap<>());
        blosum62.get('C').put('A', 0);
        blosum62.get('C').put('R', -3);
        blosum62.get('C').put('N', -3);
        blosum62.get('C').put('D', -3);
        blosum62.get('C').put('C', 9);
        blosum62.get('C').put('Q', -3);
        blosum62.get('C').put('E', -4);
        blosum62.get('C').put('G', -3);
        blosum62.get('C').put('H', -3);
        blosum62.get('C').put('I', -1);
        blosum62.get('C').put('L', -1);
        blosum62.get('C').put('K', -3);
        blosum62.get('C').put('M', -1);
        blosum62.get('C').put('F', -2);
        blosum62.get('C').put('P', -3);
        blosum62.get('C').put('S', -1);
        blosum62.get('C').put('T', -1);
        blosum62.get('C').put('W', -2);
        blosum62.get('C').put('Y', -2);
        blosum62.get('C').put('V', -1);

        blosum62.put('Q', new HashMap<>());
        blosum62.get('Q').put('A', -1);
        blosum62.get('Q').put('R', 1);
        blosum62.get('Q').put('N', 0);
        blosum62.get('Q').put('D', 0);
        blosum62.get('Q').put('C', -3);
        blosum62.get('Q').put('Q', 5);
        blosum62.get('Q').put('E', 2);
        blosum62.get('Q').put('G', -2);
        blosum62.get('Q').put('H', 0);
        blosum62.get('Q').put('I', -3);
        blosum62.get('Q').put('L', -2);
        blosum62.get('Q').put('K', 1);
        blosum62.get('Q').put('M', 0);
        blosum62.get('Q').put('F', -3);
        blosum62.get('Q').put('P', -1);
        blosum62.get('Q').put('S', 0);
        blosum62.get('Q').put('T', -1);
        blosum62.get('Q').put('W', -2);
        blosum62.get('Q').put('Y', -1);
        blosum62.get('Q').put('V', -2);

        blosum62.put('E', new HashMap<>());
        blosum62.get('E').put('A', -1);
        blosum62.get('E').put('R', 0);
        blosum62.get('E').put('N', 0);
        blosum62.get('E').put('D', 2);
        blosum62.get('E').put('C', -4);
        blosum62.get('E').put('Q', 2);
        blosum62.get('E').put('E', 5);
        blosum62.get('E').put('G', -2);
        blosum62.get('E').put('H', 0);
        blosum62.get('E').put('I', -3);
        blosum62.get('E').put('L', -3);
        blosum62.get('E').put('K', 1);
        blosum62.get('E').put('M', -2);
        blosum62.get('E').put('F', -3);
        blosum62.get('E').put('P', -1);
        blosum62.get('E').put('S', 0);
        blosum62.get('E').put('T', -1);
        blosum62.get('E').put('W', -3);
        blosum62.get('E').put('Y', -2);
        blosum62.get('E').put('V', -2);

        blosum62.put('G', new HashMap<>());
        blosum62.get('G').put('A', 0);
        blosum62.get('G').put('R', -2);
        blosum62.get('G').put('N', 0);
        blosum62.get('G').put('D', -1);
        blosum62.get('G').put('C', -3);
        blosum62.get('G').put('Q', -2);
        blosum62.get('G').put('E', -2);
        blosum62.get('G').put('G', 6);
        blosum62.get('G').put('H', -2);
        blosum62.get('G').put('I', -4);
        blosum62.get('G').put('L', -4);
        blosum62.get('G').put('K', -2);
        blosum62.get('G').put('M', -3);
        blosum62.get('G').put('F', -3);
        blosum62.get('G').put('P', -2);
        blosum62.get('G').put('S', 0);
        blosum62.get('G').put('T', -2);
        blosum62.get('G').put('W', -2);
        blosum62.get('G').put('Y', -3);
        blosum62.get('G').put('V', -3);

        blosum62.put('H', new HashMap<>());
        blosum62.get('H').put('A', -2);
        blosum62.get('H').put('R', 0);
        blosum62.get('H').put('N', 1);
        blosum62.get('H').put('D', -1);
        blosum62.get('H').put('C', -3);
        blosum62.get('H').put('Q', 0);
        blosum62.get('H').put('E', 0);
        blosum62.get('H').put('G', -2);
        blosum62.get('H').put('H', 8);
        blosum62.get('H').put('I', -3);
        blosum62.get('H').put('L', -3);
        blosum62.get('H').put('K', -1);
        blosum62.get('H').put('M', -2);
        blosum62.get('H').put('F', -1);
        blosum62.get('H').put('P', -2);
        blosum62.get('H').put('S', -1);
        blosum62.get('H').put('T', -2);
        blosum62.get('H').put('W', -2);
        blosum62.get('H').put('Y', 2);
        blosum62.get('H').put('V', -3);

        blosum62.put('I', new HashMap<>());
        blosum62.get('I').put('A', -1);
        blosum62.get('I').put('R', -3);
        blosum62.get('I').put('N', -3);
        blosum62.get('I').put('D', -3);
        blosum62.get('I').put('C', -1);
        blosum62.get('I').put('Q', -3);
        blosum62.get('I').put('E', -3);
        blosum62.get('I').put('G', -4);
        blosum62.get('I').put('H', -3);
        blosum62.get('I').put('I', 4);
        blosum62.get('I').put('L', 2);
        blosum62.get('I').put('K', -3);
        blosum62.get('I').put('M', 1);
        blosum62.get('I').put('F', 0);
        blosum62.get('I').put('P', -3);
        blosum62.get('I').put('S', -2);
        blosum62.get('I').put('T', -1);
        blosum62.get('I').put('W', -3);
        blosum62.get('I').put('Y', -1);
        blosum62.get('I').put('V', 3);

        blosum62.put('L', new HashMap<>());
        blosum62.get('L').put('A', -1);
        blosum62.get('L').put('R', -2);
        blosum62.get('L').put('N', -3);
        blosum62.get('L').put('D', -4);
        blosum62.get('L').put('C', -1);
        blosum62.get('L').put('Q', -2);
        blosum62.get('L').put('E', -3);
        blosum62.get('L').put('G', -4);
        blosum62.get('L').put('H', -3);
        blosum62.get('L').put('I', 2);
        blosum62.get('L').put('L', 4);
        blosum62.get('L').put('K', -2);
        blosum62.get('L').put('M', 2);
        blosum62.get('L').put('F', 0);
        blosum62.get('L').put('P', -3);
        blosum62.get('L').put('S', -2);
        blosum62.get('L').put('T', -1);
        blosum62.get('L').put('W', -2);
        blosum62.get('L').put('Y', -1);
        blosum62.get('L').put('V', 1);

        blosum62.put('K', new HashMap<>());
        blosum62.get('K').put('A', -1);
        blosum62.get('K').put('R', 2);
        blosum62.get('K').put('N', 0);
        blosum62.get('K').put('D', -1);
        blosum62.get('K').put('C', -3);
        blosum62.get('K').put('Q', 1);
        blosum62.get('K').put('E', 1);
        blosum62.get('K').put('G', -2);
        blosum62.get('K').put('H', -1);
        blosum62.get('K').put('I', -3);
        blosum62.get('K').put('L', -2);
        blosum62.get('K').put('K', 5);
        blosum62.get('K').put('M', -1);
        blosum62.get('K').put('F', -3);
        blosum62.get('K').put('P', -1);
        blosum62.get('K').put('S', 0);
        blosum62.get('K').put('T', -1);
        blosum62.get('K').put('W', -3);
        blosum62.get('K').put('Y', -2);
        blosum62.get('K').put('V', -2);

        blosum62.put('M', new HashMap<>());
        blosum62.get('M').put('A', -1);
        blosum62.get('M').put('R', -1);
        blosum62.get('M').put('N', -2);
        blosum62.get('M').put('D', -3);
        blosum62.get('M').put('C', -1);
        blosum62.get('M').put('Q', 0);
        blosum62.get('M').put('E', -2);
        blosum62.get('M').put('G', -3);
        blosum62.get('M').put('H', -2);
        blosum62.get('M').put('I', 1);
        blosum62.get('M').put('L', 2);
        blosum62.get('M').put('K', -1);
        blosum62.get('M').put('M', 5);
        blosum62.get('M').put('F', 0);
        blosum62.get('M').put('P', -2);
        blosum62.get('M').put('S', -1);
        blosum62.get('M').put('T', -1);
        blosum62.get('M').put('W', -1);
        blosum62.get('M').put('Y', -1);
        blosum62.get('M').put('V', 1);

        blosum62.put('F', new HashMap<>());
        blosum62.get('F').put('A', -2);
        blosum62.get('F').put('R', -3);
        blosum62.get('F').put('N', -3);
        blosum62.get('F').put('D', -3);
        blosum62.get('F').put('C', -2);
        blosum62.get('F').put('Q', -3);
        blosum62.get('F').put('E', -3);
        blosum62.get('F').put('G', -3);
        blosum62.get('F').put('H', -1);
        blosum62.get('F').put('I', 0);
        blosum62.get('F').put('L', 0);
        blosum62.get('F').put('K', -3);
        blosum62.get('F').put('M', 0);
        blosum62.get('F').put('F', 6);
        blosum62.get('F').put('P', -4);
        blosum62.get('F').put('S', -2);
        blosum62.get('F').put('T', -2);
        blosum62.get('F').put('W', 1);
        blosum62.get('F').put('Y', 3);
        blosum62.get('F').put('V', -1);

        blosum62.put('P', new HashMap<>());
        blosum62.get('P').put('A', -1);
        blosum62.get('P').put('R', -2);
        blosum62.get('P').put('N', -2);
        blosum62.get('P').put('D', -1);
        blosum62.get('P').put('C', -3);
        blosum62.get('P').put('Q', -1);
        blosum62.get('P').put('E', -1);
        blosum62.get('P').put('G', -2);
        blosum62.get('P').put('H', -2);
        blosum62.get('P').put('I', -3);
        blosum62.get('P').put('L', -3);
        blosum62.get('P').put('K', -1);
        blosum62.get('P').put('M', -2);
        blosum62.get('P').put('F', -4);
        blosum62.get('P').put('P', 7);
        blosum62.get('P').put('S', -1);
        blosum62.get('P').put('T', -1);
        blosum62.get('P').put('W', -4);
        blosum62.get('P').put('Y', -3);
        blosum62.get('P').put('V', -2);

        blosum62.put('S', new HashMap<>());
        blosum62.get('S').put('A', 1);
        blosum62.get('S').put('R', -1);
        blosum62.get('S').put('N', 1);
        blosum62.get('S').put('D', 0);
        blosum62.get('S').put('C', -1);
        blosum62.get('S').put('Q', 0);
        blosum62.get('S').put('E', 0);
        blosum62.get('S').put('G', 0);
        blosum62.get('S').put('H', -1);
        blosum62.get('S').put('I', -2);
        blosum62.get('S').put('L', -2);
        blosum62.get('S').put('K', 0);
        blosum62.get('S').put('M', -1);
        blosum62.get('S').put('F', -2);
        blosum62.get('S').put('P', -1);
        blosum62.get('S').put('S', 4);
        blosum62.get('S').put('T', 1);
        blosum62.get('S').put('W', -3);
        blosum62.get('S').put('Y', -2);
        blosum62.get('S').put('V', -2);

        blosum62.put('T', new HashMap<>());
        blosum62.get('T').put('A', 0);
        blosum62.get('T').put('R', -1);
        blosum62.get('T').put('N', 0);
        blosum62.get('T').put('D', -1);
        blosum62.get('T').put('C', -1);
        blosum62.get('T').put('Q', -1);
        blosum62.get('T').put('E', -1);
        blosum62.get('T').put('G', -2);
        blosum62.get('T').put('H', -2);
        blosum62.get('T').put('I', -1);
        blosum62.get('T').put('L', -1);
        blosum62.get('T').put('K', -1);
        blosum62.get('T').put('M', -1);
        blosum62.get('T').put('F', -2);
        blosum62.get('T').put('P', -1);
        blosum62.get('T').put('S', 1);
        blosum62.get('T').put('T', 5);
        blosum62.get('T').put('W', -2);
        blosum62.get('T').put('Y', -2);
        blosum62.get('T').put('V', 0);

        blosum62.put('W', new HashMap<>());
        blosum62.get('W').put('A', -3);
        blosum62.get('W').put('R', -3);
        blosum62.get('W').put('N', -4);
        blosum62.get('W').put('D', -4);
        blosum62.get('W').put('C', -2);
        blosum62.get('W').put('Q', -2);
        blosum62.get('W').put('E', -3);
        blosum62.get('W').put('G', -2);
        blosum62.get('W').put('H', -2);
        blosum62.get('W').put('I', -3);
        blosum62.get('W').put('L', -2);
        blosum62.get('W').put('K', -3);
        blosum62.get('W').put('M', -1);
        blosum62.get('W').put('F', 1);
        blosum62.get('W').put('P', -4);
        blosum62.get('W').put('S', -3);
        blosum62.get('W').put('T', -2);
        blosum62.get('W').put('W', 11);
        blosum62.get('W').put('Y', 2);
        blosum62.get('W').put('V', -3);

        blosum62.put('Y', new HashMap<>());
        blosum62.get('Y').put('A', -2);
        blosum62.get('Y').put('R', -2);
        blosum62.get('Y').put('N', -2);
        blosum62.get('Y').put('D', -3);
        blosum62.get('Y').put('C', -2);
        blosum62.get('Y').put('Q', -1);
        blosum62.get('Y').put('E', -2);
        blosum62.get('Y').put('G', -3);
        blosum62.get('Y').put('H', 2);
        blosum62.get('Y').put('I', -1);
        blosum62.get('Y').put('L', -1);
        blosum62.get('Y').put('K', -2);
        blosum62.get('Y').put('M', -1);
        blosum62.get('Y').put('F', 3);
        blosum62.get('Y').put('P', -3);
        blosum62.get('Y').put('S', -2);
        blosum62.get('Y').put('T', -2);
        blosum62.get('Y').put('W', 2);
        blosum62.get('Y').put('Y', 7);
        blosum62.get('Y').put('V', -1);

        blosum62.put('V', new HashMap<>());
        blosum62.get('V').put('A', 0);
        blosum62.get('V').put('R', -3);
        blosum62.get('V').put('N', -3);
        blosum62.get('V').put('D', -3);
        blosum62.get('V').put('C', -1);
        blosum62.get('V').put('Q', -2);
        blosum62.get('V').put('E', -2);
        blosum62.get('V').put('G', -3);
        blosum62.get('V').put('H', -3);
        blosum62.get('V').put('I', 3);
        blosum62.get('V').put('L', 1);
        blosum62.get('V').put('K', -2);
        blosum62.get('V').put('M', 1);
        blosum62.get('V').put('F', -1);
        blosum62.get('V').put('P', -2);
        blosum62.get('V').put('S', -2);
        blosum62.get('V').put('T', 0);
        blosum62.get('V').put('W', -3);
        blosum62.get('V').put('Y', -1);
        blosum62.get('V').put('V', 4);
    }
}
