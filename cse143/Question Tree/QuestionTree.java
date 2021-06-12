/**------------------------------------------------------------------
* Description: Reads a user given file or constructs from scratch, 
* a tree of question and answers. Also has the ability to write the
* current tree to a file. This class is the 'manager' for the
* questions game; doing jobs such as asking the questions, taking
* user feedback, and modifying the question tree if needed.
* @author: Young Bin Cho (Josh Cho), CSE 143A Section AE
* Date: 11/28/2017
*------------------------------------------------------------------*/

import java.io.*;
import java.util.*;

public class QuestionTree{

   QuestionNode overallRoot; //Root where nodes will stack onto
   Scanner console; //Declared scanner, used throughout the code
   
   /**---------------------------------------------------------------
   * Description: A constructor that initializes the scanner and
   * sets the overallRoot to a single node with data "computer".
   *---------------------------------------------------------------*/
   public QuestionTree(){
      //Initializing class variables
      overallRoot = new QuestionNode("computer");
      console = new Scanner(System.in);
   }
   
   /**---------------------------------------------------------------
   * Description: Calls the readHelper method, replacing the current
   * overallRoot with a user given one. (@see readHelper method)
   * @param input : The scanner that holds the file information line
   * by line
   *---------------------------------------------------------------*/
   public void read(Scanner input){
      overallRoot = readHelper(input);
   }
   
   /**---------------------------------------------------------------
   * Description: Reads the user given file, determines whether its 
   * a question or an answer, sorts them into the overallRoot 
   * accordingly (left to right), constructing a tree.
   * @param input : The scanner that holds the file information line
   * by line
   * @return QuestionNode : The concatenated tree after execution
   *---------------------------------------------------------------*/
   private QuestionNode readHelper(Scanner input){
      String type = input.nextLine(); //'Q' or 'A'
      //The QuestionNode form of the user given line
      QuestionNode root = new QuestionNode(input.nextLine());
      if(type.equals("Q:")){
         root.yes = readHelper(input);
         root.no = readHelper(input);
      }
      return root;
   }
   
   /**---------------------------------------------------------------
   * Description: Calls the writeHelper method, writing the current
   * overallRoot to a user designated file. (@see writeHelper method)
   * @param output : The stream writer that writes to the designated
   * file.
   *---------------------------------------------------------------*/
   public void write(PrintStream output){
      writeHelper(overallRoot, output);
   }
   
   /**---------------------------------------------------------------
   * Description: Writes the current overallRoot tree into a file,
   * from left to right, with according headers 'Q' or 'A', and line
   * by line.
   * @param root : The current QuestionNode that is being considered
   * @param output : The stream writer that writes to the designated
   * file.
   *---------------------------------------------------------------*/
   private void writeHelper(QuestionNode root, PrintStream output){
      if(root != null){
         if(root.yes == null && root.no == null)
            output.println("A:");
         else
            output.println("Q:");
         output.println(root.data);
         writeHelper(root.yes, output);
         writeHelper(root.no, output);
      }
   }
   
   /**---------------------------------------------------------------
   * Description: Calls the questionsHelper method, replacing the 
   * current overallRoot with a new one (may not change if the
   * program guessed the right answer). (@see questionsHelper method)
   *---------------------------------------------------------------*/
   public void askQuestions(){
      overallRoot = questionsHelper(overallRoot);
   }
   
   /**---------------------------------------------------------------
   * Description: Interacts with the user and goes through the
   * overallRoot with series of yes or no questions. When an answer
   * is reached, the user may say its the right answer, displaying 
   * the according dialog, or could say its not the right answer, 
   * expanding the tree with user given question and answer.
   * @param root : The current QuestionNode being considered
   * @return QuestionNode : The concatenated tree after execution
   *---------------------------------------------------------------*/
   private QuestionNode questionsHelper(QuestionNode root){
      //When the recursion reaches an answer
      if(root.yes == null && root.no == null){
         //Guessed Correctly
         if(yesTo("Would your object happen to be " + root.data + "?"))
            System.out.println("Great, I got it right!");
         //Wrong guess : expanding the tree
         else{
            System.out.print("What is the name of your object? ");
            String name = console.nextLine();
            System.out.print("Please give me a yes/no question that\n" +
                               "distinguishes between your object\n" +
                               "and mine--> ");
            String question = console.nextLine();
            //Creating necessary nodes and rearranging
            if(yesTo("And what is the answer for your object?"))
               root = new QuestionNode(question, new QuestionNode(name), root);
            else
               root = new QuestionNode(question, root, new QuestionNode(name));
         }
      }
      //Calls the method as root.yes or no so the tree stays concatenated
      else if(yesTo(root.data))
         root.yes = questionsHelper(root.yes);
      else
         root.no =  questionsHelper(root.no);
      return root;
   }
   
   /**---------------------------------------------------------------
   * Description: This method asks the given question until the user 
   * types ¡°y¡± or ¡°n.¡±  It returns true if ¡°y¡±, false if ¡°n¡±.
   * @param prompt : The dialog that is to be displayed to user
   * @return boolean : True or false according to user given answer
   * 'y' or 'n'
   *---------------------------------------------------------------*/
   public boolean yesTo(String prompt){
      System.out.print(prompt + " (y/n)? ");
      String response = console.nextLine().trim().toLowerCase();
      while (!response.equals("y") && !response.equals("n")){
         System.out.println("Please answer y or n.");
         System.out.print(prompt + " (y/n)? ");
         response = console.nextLine().trim().toLowerCase();
      }
      return response.equals("y");
   }
}