import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class fastaReader {

    private ArrayList<String> sequences;

    public fastaReader(){
        sequences = new ArrayList<>();
    }

    public void readFile(String filename) throws IOException {
        // locate target file (must be located within src/main/resources folder)
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + filename));

        // read first line
        String line = br.readLine();
        while(line.charAt(0) == '>'){ // ignore first n comment lines
            line = br.readLine();
        }

        // read file
        String temp = "";
        while(line != null){
            if(line.charAt(0) == '>'){
                sequences.add(stringProcessor(temp)); // treat all as uppercase (ACGT)
                temp = "";
            } else {
                temp += line;
            }
            line = br.readLine();
        }

        // adding last entry
        if(temp != "")
            sequences.add(stringProcessor(temp));
    }

    public ArrayList<String> getSequences(){ return this.sequences; }

    /*
    Processes the given string (read from file) to the required format.
    */
    private String stringProcessor(String s){
        s = s.replaceAll("[^acgtACGT ]", "T");
        s = s.toUpperCase();
        return s;
    }
}
