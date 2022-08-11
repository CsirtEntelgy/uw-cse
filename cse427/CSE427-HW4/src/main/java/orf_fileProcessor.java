import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 Class for reading and processing the given file.
 Can handle .fna and .gff files, in correct format.
 Given files must be formatted to assignment guideline.
 */
public class orf_fileProcessor {

    private String filename; //name of file
    private String fnaContent; //storing the content of .fna file
    private ArrayList<Integer> gffContent; //storing the content of .gff file

    private int gffLength;

    /*
     Default constructor. Set values accordingly.

     @param  String : name of file to read (must be located in src/main/resources/)
     */
    public orf_fileProcessor(String filename){
        this.filename = filename;
        this.fnaContent = "";
        this.gffContent = new ArrayList<>();

        this.gffLength = 0;
    }

    /*
     Returns the contents of read .fna file.

     @return String : Formatted string of content
     */
    public String getFNAContent(){
        return this.fnaContent;
    }

    /*
     Returns the contents of read .gff file.

     @return ArrayList<Integer> : Formatted content (Integer = end index)
     */
    public ArrayList<Integer> getGffContent(){ return this.gffContent; }

    /*
     Returns the length of read gff file.

     @return int : Length of gff file
     */
    public int getGFFLength() { return this.gffLength; }

    /*
     Read .gff file and process it to meet the format requirement.
     */
    public void readGFFFile() throws IOException {
        //locate target file (must be located within src/main/resources folder)
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + filename));

        int temp = 0; //counter for length of file
        String line = br.readLine(); //first line

        while(line != null){ //while we have lines
            String[] delim = line.split("\\t");
            gffContent.add(Integer.parseInt(delim[4]) - 1 - 3); //accounting for 1-base and "included stop codon"
            line = br.readLine();
            temp++; //line count +1
        }

        this.gffLength = temp; //set global length to read length
    }

    /*
     Read .fna file and process it to meet the format requirement.

     @see    processFNAContent()
     */
    public void readFNAFile() throws IOException {
        //locate target file (must be located within src/main/resources folder)
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + filename));

        //read first line
        String line = br.readLine();
        while(line.charAt(0) == '>') //ignore first n comment lines
            line = br.readLine();

        //read lines and concat until we hit the next '>'
        while(line != null && line.charAt(0) != '>') {
            fnaContent += line.replaceAll("\\s+", "");
            line = br.readLine();
        }

        //process read lines from .fna file
        processFNAContent();
    }

    /*
     Helper class for readFile(), replaces all invalid characters.
     Valid for processing .fna files.

     @see    readFNAFile()
     */
    private void processFNAContent(){
        String temp = fnaContent;
        temp = temp.replaceAll("[^acgACG]", "T");
        temp = temp.toUpperCase();
        this.fnaContent = temp;
    }
}
