/**------------------------------------------------------------------
* Description: This class takes in an array or a file and stores it. 
* Has the ability to write the stored data out in the standard format 
* (line of character value followed by its location. 0 meaning left 
* and 1 meaning right). Has the ability to decode the encoded version 
* of a file to plain text.
* @author: Young Bin Cho (Josh Cho), CSE 143A Section AE
* Date: 12/06/2017
*------------------------------------------------------------------*/
import java.util.*;
import java.io.*;

public class HuffmanTree{

   private HuffmanNode overallRoot; //Base for the binary tree
   
   /**---------------------------------------------------------------
   * Description: Takes in an array of int and creates HuffmanNodes
   * according to each. From the first element to last, the n-th
   * element is how many n-th ASCII characters are there. Ignoring
   * characters with 0 occurances, populates the priority queue with
   * the created HuffmanNodes. Adds one end-of-file HuffmanNode, which
   * is the (n+1)-th element, to indicate the end of file. Overwrites
   * overallRoot with a call to the method 'constructor'.
   * @param count : The array of integers that represents the ASCII
   *---------------------------------------------------------------*/
   public HuffmanTree(int[] count){
      //Sorting list of ints into priorityQueue of HuffmanNodes
      Queue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
      for(int i = 0; i < count.length; i++){
         if(count[i] > 0)
            q.add(new HuffmanNode(count[i], i));
      }
      //Adding the eof node
      q.add(new HuffmanNode(1, count.length));
      //Constructing tree out of the list
      overallRoot = constructor(q);
   }
   
   /**---------------------------------------------------------------
   * Description: Takes in a user given file as a scanner. Constructs
   * the binary tree by calling on the method 'reconstructor'.
   * @param input : A scanner which holds the user given file
   *---------------------------------------------------------------*/
   public HuffmanTree(Scanner input){
      //Initializing overallRoot to pass it into reconstructor
      overallRoot = new HuffmanNode(-1, -1);
      //Constructing tree out of the file
      while(input.hasNextLine())
         reconstructor(overallRoot, Integer.parseInt(input.nextLine()), 
                        input.nextLine());
   }
   
   /**---------------------------------------------------------------
   * Description: Removes the first two elements of the queue and 
   * adds a HuffmanNode with a frequency which is their frequency 
   * added together and their left and right being the two removed
   * HuffmanNodes until there is only one element left in the queue,
   * then returns the element.
   * @param q : The queue that is to be sorted
   * @return HuffmanNode : The complete binary tree made from the
   * elements of the q
   *---------------------------------------------------------------*/
   private HuffmanNode constructor(Queue<HuffmanNode> q){
      if(q.size() == 1)
         return q.remove();
      else{
         HuffmanNode first = q.remove();
         HuffmanNode second = q.remove();
         q.add(new HuffmanNode(first.frequency + second.frequency, -1, 
                                 first, second));
         return constructor(q);
      }
   }
   
   /**---------------------------------------------------------------
   * Description: Loops through the binary tree according to the
   * user given string. If there are no nodes in the direction to be
   * headed, creates a node. If the navigation through the string
   * is over, the resulting node is specified with a certain character
   * value.
   * @param root : The current HuffmanNode being examined
   * @param leaf : The specified int value that is to be set as the
   * character value for a leaf node
   * @param str : The direction to follow to the leaf node
   *---------------------------------------------------------------*/
   private void reconstructor(HuffmanNode root, int leaf, String str){
      if(str.isEmpty())
         root.character = leaf;
      else{
         if(str.charAt(0) == '0'){
            if(root.left == null)
               root.left = new HuffmanNode(-1, -1);
            reconstructor(root.left, leaf, str.substring(1));
         }
         else{
            if(root.right == null)
               root.right = new HuffmanNode(-1, -1);
            reconstructor(root.right, leaf, str.substring(1));
         }
      }
   }
   
   /**---------------------------------------------------------------
   * Description: Calls on the method 'writeHelper'.
   * @param output : The streamwriter used to print the tree
   *---------------------------------------------------------------*/
   public void write(PrintStream output){
      writeHelper(overallRoot, "", output);
   }
   
   /**---------------------------------------------------------------
   * Description: For every leaf node, prints the character 
   * information and the route taken to get to that node.
   * @param root : The current node being examined
   * @param str : Recording of the route taken
   * @param output : The streamwriter used to print the tree
   *---------------------------------------------------------------*/
   private void writeHelper(HuffmanNode root, String str, 
                              PrintStream output){
      if(root != null){
         if(root.left == null && root.right == null){
            output.println(root.character);
            output.println(str);
         }
         writeHelper(root.left, str + "0", output);
         writeHelper(root.right, str+ "1", output);
      }
   }
   
   /**---------------------------------------------------------------
   * Description: Calls on the method 'decodHelper' with overallRoot
   * until it reaches the end of file.
   * @param input : Stream of 0 and 1, used as directions in naviga-
   * ting the overallRoot
   * @param output : The streamwriter used to print the text
   * @param eof : Indicator that the reading should be over
   *---------------------------------------------------------------*/
   public void decode(BitInputStream input, PrintStream output, 
                        int eof){
      //Below process is to avoid stackoverflow
      int bit = decodeHelper(overallRoot, input, eof);
      while(bit != -1){
         output.write(bit);
         bit = decodeHelper(overallRoot, input, eof);
      }
   }
   
   /**---------------------------------------------------------------
   * Description: Returns -1 when reached the end of file. Else follows
   * the directions given through the inputstream and returns the int
   * value of the resulting node.
   * @param root : The current node being considered
   * @param input : Stream of 0 and 1, used as directions in naviga-
   * ting the overallRoot
   * @param eof : Indicator for end of file
   * @return int : The character value of the resulting node
   *---------------------------------------------------------------*/
   private int decodeHelper(HuffmanNode root, BitInputStream input, 
                              int eof){
      if(root.left == null && root.right == null){
         if(root.character == eof)
            return -1; //reached the eof
         else
            return root.character;
      }
      else{
         if(input.readBit() == 0)
            return decodeHelper(root.left, input, eof);
         else
            return decodeHelper(root.right, input, eof);
      }
   }
}