/**------------------------------------------------------------------
* Description: A node class for the 20 Questions program. Stores the
* string that is either a question or an answer. Also directs at next
* question or answer depending on user choice of 'yes' or 'no'.
* @author: Young Bin Cho (Josh Cho), CSE 143A Section AE
* Date: 11/28/2017
*------------------------------------------------------------------*/

public class QuestionNode{

   public String data; //String that holds the user given data
   public QuestionNode yes; //Next node that corresponds to the answer 'yes'
   public QuestionNode no; //Next node that corresponds to the answer 'no'
   
   /**---------------------------------------------------------------
   * Description: A constructor that takes in a string, QuestionNode,
   * and another QuestionNode; assigns them to the according data
   * field of this class.
   * @param data : The user given string
   * @param yes : The node that will correspond to the 'yes' answer
   * @param no : The node that will correspond to the 'no' answer
   *---------------------------------------------------------------*/
   public QuestionNode(String data, QuestionNode yes, QuestionNode no){
      this.data = data;
      this.yes = yes;
      this.no = no;
   }
   
   /**---------------------------------------------------------------
   * Description: A constructor that takes in only a string and
   * constructs the class, 'yes' and 'no' nodes set to null.
   * @param data : The user given string
   *---------------------------------------------------------------*/
   public QuestionNode(String data){
      this(data, null, null);
   }
}