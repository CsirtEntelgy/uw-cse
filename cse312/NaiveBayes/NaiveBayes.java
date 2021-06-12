// Do not submit with package statements if you are using eclipse.
// Only use what is provided in the standard libraries.

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.HashMap;

public class NaiveBayes {
    
    HashMap<String, Double> newHamMap;
    HashMap<String, Double> newSpamMap;
    double hamPossibility;
    double spamPossibility;
    
    /*
     * !! DO NOT CHANGE METHOD HEADER !!
     * If you change the method header here, our grading script won't
     * work and you will lose points!
     * 
     * Train your Naive Bayes Classifier based on the given training
     * ham and spam emails.
     *
     * Params:
     *      hams - email files labeled as 'ham'
     *      spams - email files labeled as 'spam'
     */
    public void train(File[] hams, File[] spams) throws IOException {
        HashSet<String> bigSet = new HashSet<>();
        HashMap<String, Integer> hamMap = new HashMap<>();
        newHamMap = new HashMap<>();
        HashMap<String, Integer> spamMap = new HashMap<>();
        newSpamMap = new HashMap<>();
        //initial sort
        for(File f : hams){
            for(String s : tokenSet(f)){
               bigSet.add(s);
               if(!hamMap.containsKey(s))
                  hamMap.put(s, 0);
               int temp = hamMap.get(s);
               hamMap.put(s, temp + 1);
            }
        }
        for(File f : spams){
            for(String s : tokenSet(f)){
               bigSet.add(s);
               if(!spamMap.containsKey(s))
                  spamMap.put(s, 0);
               int temp = spamMap.get(s);
               spamMap.put(s, temp + 1);
            }
        }
        //calculating possibility
        double hamSize = hams.length;
        double spamSize = spams.length;
        for(String s : bigSet){
            if(hamMap.containsKey(s))
               newHamMap.put(s, ((hamMap.get(s) + 1) / (hamSize + 2)));
            else
               newHamMap.put(s, (1.0 / (hamSize + 2)));
            if(spamMap.containsKey(s))
               newSpamMap.put(s, ((spamMap.get(s) + 1) / (spamSize + 2)));
            else
               newSpamMap.put(s, (1.0 / (spamSize + 2)));
        }
        hamPossibility = hamSize / (hamSize + spamSize);
        spamPossibility = spamSize / (hamSize + spamSize);
    }

    /*
     * !! DO NOT CHANGE METHOD HEADER !!
     * If you change the method header here, our grading script won't
     * work and you will lose points!
     *
     * Classify the given unlabeled set of emails. Follow the format in
     * example_output.txt and output your result to stdout. Note the order
     * of the emails in the output does NOT matter.
     * 
     * Do NOT directly process the file paths, to get the names of the
     * email files, check out File's getName() function.
     *
     * Params:
     *      emails - unlabeled email files to be classified
     */
    public void classify(File[] emails) throws IOException {
        for(File f : emails){
            double bigHam = 0.0;
            double bigSpam = 0.0;
            for(String s : tokenSet(f)){
               if(newHamMap.containsKey(s))
                  bigHam += Math.log(newHamMap.get(s));
               if(newSpamMap.containsKey(s))
                  bigSpam += Math.log(newSpamMap.get(s));
            }
            if((Math.log(spamPossibility) + bigSpam) > (Math.log(hamPossibility) + bigHam))
               System.out.println(f.getName() + " spam");
            else
               System.out.println(f.getName() + " ham");
        }
    }


    /*
     *  Helper Function:
     *  This function reads in a file and returns a set of all the tokens. 
     *  It ignores "Subject:" in the subject line.
     *  
     *  If the email had the following content:
     *  
     *  Subject: Get rid of your student loans
     *  Hi there ,
     *  If you work for us , we will give you money
     *  to repay your student loans . You will be 
     *  debt free !
     *  FakePerson_22393
     *  
     *  This function would return to you
     *  ['be', 'student', 'for', 'your', 'rid', 'we', 'of', 'free', 'you', 
     *   'us', 'Hi', 'give', '!', 'repay', 'will', 'loans', 'work', 
     *   'FakePerson_22393', ',', '.', 'money', 'Get', 'there', 'to', 'If', 
     *   'debt', 'You']
     */
    public static HashSet<String> tokenSet(File filename) throws IOException {
        HashSet<String> tokens = new HashSet<String>();
        Scanner filescan = new Scanner(filename);
        filescan.next(); // Ignoring "Subject"
        while(filescan.hasNextLine() && filescan.hasNext()) {
            tokens.add(filescan.next());
        }
        filescan.close();
        return tokens;
    }
}
