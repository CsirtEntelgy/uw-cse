/**------------------------------------------------------------------
* Description: A class which stores the frequency, int value of a 
* character, and the next nodes to follow. Is made so that it is
* comparable to other nodes of same type by comparing the frequency
* variable.
* @author: Young Bin Cho (Josh Cho), CSE 143A Section AE
* Date: 12/06/2017
*------------------------------------------------------------------*/
public class HuffmanNode implements Comparable<HuffmanNode>{
   
   //For frequency and character, -1 will be used as a null value for it
   public int frequency; //Number of occurance for this character
   public int character; //The int value of the character being considered
   public HuffmanNode left; //The node that corresponds to '0'
   public HuffmanNode right; //The node that corresponds to '1'
   
   /**---------------------------------------------------------------
   * Description: A constructor which takes in individual input for
   * all the class variables and sets them accordingly.
   * @param frequency : The value that is to be set as frequency
   * @param character : The value that is to be set as character
   * @param left : The value that is to be set as left
   * @param right : The value that is to be set as right
   *---------------------------------------------------------------*/
   public HuffmanNode(int frequency, int character, 
                        HuffmanNode left, HuffmanNode right){
      this.frequency = frequency;
      this.character = character;
      this.left = left;
      this.right = right;
   }
   
   /**---------------------------------------------------------------
   * Description: A constructor which takes in only the input for
   * frequency and character, sets the value accordingly, and sets
   * left and right as null.
   * @param frequency : The value that is to be set as frequency
   * @param character : The value that is to be set as character
   *---------------------------------------------------------------*/
   public HuffmanNode(int frequency, int character){
      this(frequency, character, null, null);
   }
   
   /**---------------------------------------------------------------
   * Description: Compares this class's frequency value with the
   * given class's frequency value. Returns a positive number if 
   * bigger, a zero if the same, and a negative number if smaller.
   * @param other : The node that is to be compared
   * @return int : Positive, negative, or zero; according to comparison
   *---------------------------------------------------------------*/
   public int compareTo(HuffmanNode other){
      return frequency - other.frequency;
   }
}