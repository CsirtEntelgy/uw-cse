import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
Class for reading and processing the given file (FASTA format).

 */
public class fastaReader {
    private String filename; // name of file
    private String content; // storing the content of file

    /*
    Default constructor.
    Set values accordingly.

    @param  String : name of file to read (must be located in src/main/resources/)
     */
    public fastaReader(String filename){
        this.filename = filename;
        this.content = "";
    }

    /*
    Returns the contents

    @return String : Formatted string of content
     */
    public String getContent(){
        return this.content;
    }

    /*
    Read the file and process it to meet the format requirement.
     */
    public void readFile() throws IOException {
        // locate target file (must be located within src/main/resources folder)
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + filename));

        // read first line
        String line = br.readLine();
        while(line.charAt(0) == '>') // ignore first n comment lines
            line = br.readLine();

        // read lines and concat until we hit the next '>'
        while(line != null && line.charAt(0) != '>') {
            content += line.replaceAll("\\s+", "");
            line = br.readLine();
        }

        // process read lines
        processContent();
    }

    /*
    Helper class for readFile(), replaces all invalid characters.

    @see    readFile()
     */
    private void processContent(){
        String temp = content;
        temp = temp.replaceAll("[^acgACG]", "T");
        temp = temp.toUpperCase();
        this.content = temp;
    }
}
