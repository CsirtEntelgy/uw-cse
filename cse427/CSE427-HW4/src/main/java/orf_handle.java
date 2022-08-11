import java.util.ArrayList;

/*
 Class for finding ORFs inside given sequence.
 Found ORFs are non-nested, following the given definition.
 */
public class orf_handle {

    private static String seq; //given data sequence
    private ArrayList<int[]> ORF; //storing found non-nested ORF

    private int pointer = 1; //current pointer (3-based)
    private int last1 = 0; //starting pos of last reading of frame1-offset0
    private int last2 = 1; //starting pos of last reading of frame2-offset1
    private int last3 = 2; //starting pos of last reading of frame3-offset2

    private int numF1 = 0; //number of ORF found in frame1
    private int numF2 = 0; //number of ORF found in frame2
    private int numF3 = 0; //number of ORF found in frame3

    /*
     Default constructor. Set values accordingly.

     @param  String : Content of target sequence
     */
    public orf_handle(String seq){
        this.seq = seq;
        this.ORF = new ArrayList<>();
    }

    /*
     Returns number of ORFs found in each frame

     @param  int[] : int[0] = number of ORF found in frame1, int[1] = number of ORF found in frame2, ...
     */
    public int[] reportCount(){
        int[] count = new int[3];
        count[0] = numF1;
        count[1] = numF2;
        count[2] = numF3;

        return count;
    }

    /*
     Returns the number of short (<151) and long (>1400) ORFs

     @return int[] : int[0] = number of short ORFs found, int[1] = number of long ORFs found
     */
    public int[] reportLength(){
        int[] count = new int[2];
        int s = 0; int l = 0;
        for(int[] i : ORF){
            if(i[1] - i[0] + 1 > 1400) //long orf
                l++;
            else if(i[1] - i[0] + 1 < 151) //short orf
                s++;
        }
        count[0] = s; count[1] = l;
        return count;
    }

    /*
     Returns the actual string of found long ORFs.
     (the actual string, not coordinates)

     @return ArrayList<String> : all found long ORFs
     */
    public ArrayList<String> reportLongORF(){
        ArrayList<String> temp = new ArrayList<>();
        for(int[] i : ORF){
            if(i[1] - i[0] + 1 > 1400)
                temp.add(seq.substring(i[0], i[1] + 1));
        }
        return temp;
    }

    /*
     Returns the full report of all found ORFs.
     The report format is following the instruction in the assignment.
     Report contains: start pos, end pos, length, markov score, and 0 for non-in-database and 1 for vice versa.

     @param endIndex : all "ending index" found in the genebank database (indicator for a correct hit)
     @param m : trained markov model, used for markov scoring

     @return ArrayList<double[]> : full report for all ORFs
     */
    public ArrayList<double[]> reportFull(ArrayList<Integer> endIndex, orf_markov m){
        ArrayList<double[]> result = new ArrayList<>();
        for(int[] i : ORF){
            double[] temp = new double[5];
            temp[0] = i[0] + 1; //start pos, accounting for 1-base
            temp[1] = i[1] + 1 + 3; //end pos, accounting for 1-base and "including stop codon"
            temp[2] = i[1] - i[0] + 1; //length
            temp[3] = m.score(seq.substring(i[0], i[1] + 1)); //markov score
            if(endIndex.contains(i[1]))
                temp[4] = 1;

            result.add(temp);
        }
        return result;
    }

    /*
     Finds all non-nested ORFs in the target sequence.
     */
    public void find(){
        while((pointer+1) * 3 <= seq.length()){
            //frame1 find, if nested then discard
            if(seq.substring(pointer * 3, (pointer+1) * 3).equals("TAA")
                    || seq.substring(pointer * 3, (pointer+1) * 3).equals("TAG")
                    || seq.substring(pointer * 3, (pointer+1) * 3).equals("TGA")){
                if(last2 != 1 && last3 != 2 && last2 > last1 && last3 > last1) {
                    ORF.add(new int[]{last1, pointer * 3 - 1}); //storing real pos (string wise)
                    numF1++;
                }
                last1 = pointer * 3 + 3; //accounting for 3-base codons and offset
            }
            //frame2 find, if nested then discard
            if((pointer+1) * 3 + 1 <= seq.length() && (seq.substring(pointer * 3 + 1, (pointer+1) * 3 + 1).equals("TAA")
                    || seq.substring(pointer * 3 + 1, (pointer+1) * 3 + 1).equals("TAG")
                    || seq.substring(pointer * 3 + 1, (pointer+1) * 3 + 1).equals("TGA"))){
                if(last1 != 0 && last3 != 2 && last1 > last2 && last3 > last2) {
                    ORF.add(new int[]{last2, pointer * 3}); //storing real pos (string wise)
                    numF2++;
                }
                last2 = pointer * 3 + 1 + 3; //accounting for 3-base codons and offset
            }
            //frame3 find, if nested then discard
            if((pointer+1) * 3 + 2 <= seq.length() && (seq.substring(pointer * 3 + 2, (pointer+1) * 3 + 2).equals("TAA")
                    || seq.substring(pointer * 3 + 2, (pointer+1) * 3 + 2).equals("TAG")
                    || seq.substring(pointer * 3 + 2, (pointer+1) * 3 + 2).equals("TGA"))){
                if(last1 != 0 && last2 != 1 && last1 > last3 && last2 > last3) {
                    ORF.add(new int[]{last3, pointer * 3 + 1}); //storing real pos (string wise)
                    numF3++;
                }
                last3 = pointer * 3 + 2 + 3; //accounting for 3-base codons and offset
            }
            //increment pointer to next
            pointer++;
        }
    }
}
