/**------------------------------------------------------------------
* Description: Takes a user given list of words, a string, and a int-
* eger. This program deconstructs the given string into 'n' words
* ('n' being the user given integer or less). The words used to 
* deconstruct the string will also be the words given by the user.
* Then the program will display the result, line by line, in the 
* format of "[words]".
* @author: Young Bin Cho (Josh Cho), CSE 143A Section AE
* Date: 11/16/2017
*------------------------------------------------------------------*/

import java.util.*;

public class AnagramSolver {
   private final List<String> dictionary; //Copy of the original dictionary
   private final Map<String, LetterInventory> inventories; //Word=LetterInventory map
   
   /**---------------------------------------------------------------
   * Description: Takes a user given list of words (dictionary) and
   * stores a copy of it. Creates a LetterInventory object for each
   * of the words and sort them into a map accordingly.
   * @param list : User given list of words
   *---------------------------------------------------------------*/
   public AnagramSolver(List<String> list) {
      //Initializing class variables
      dictionary = new ArrayList<String>(list);
      inventories = new HashMap<String, LetterInventory>();
      //Creating map of word=LetterInventory
      //This was done considering there are no duplicates
      for(String s : list)
         inventories.put(s, new LetterInventory(s));
   }
   
   /**---------------------------------------------------------------
   * Description: Takes a user given string and sorts the dictionary
   * to extract all the relevant words (being relevant meaning that
   * all characters of that word is included in the string). Passes
   * the shortened dictionary, the LetterInventory form of the
   * user given string, and the max number of words that can be used
   * to explore method. 
   * @param s : The user given string that is to be deconstructed
   * @param max : Max number of words that can be used to deconstruct
   * @throws IllegalArgumentException : Max is less than zero
   *---------------------------------------------------------------*/
   public void print(String s, int max) {
      //Error Handle: Max is less than zero
      if(max < 0)
         throw new IllegalArgumentException("Max is less than zero");
      //Initializing LetterInventory of the string and list of relevant words
      LetterInventory userInput = new LetterInventory(s);
      List<String> relevantWords = new ArrayList<String>();
      //Populating the list of relevant words
      for(String str : dictionary){
         if(userInput.subtract(inventories.get(str)) != null)
            relevantWords.add(str);
      }
      //Initializing empty list to pass into explore
      List<String> emptyList = new ArrayList<String>();
      //Print empty list if userInput is empty to begin with, else perform explore
      if(userInput.isEmpty())
         System.out.println(emptyList);
      else
         explore(relevantWords, max, emptyList, userInput);
   }
   
   /**---------------------------------------------------------------
   * Description: Searches through the list of relevant words and
   * if the user given string contains the word, the word is stored
   * and subtracted from the string. This process is continued until
   * the user given string is empty, which then checks if the list of
   * stored words has a valid number of words in it. If so, the list
   * is printed.
   * @param relevantWords : List of relevant words
   * @param max : Max number of words that can be used to deconstruct
   * userInput
   * @param selected : Words that have been subtracted from userInput
   * @param userInput : LetterInventory of letters left
   *---------------------------------------------------------------*/
   private void explore(List<String> relevantWords, int max, List<String> selected, 
                        LetterInventory userInput){
      if(userInput.isEmpty()){
         if(max == 0)
            System.out.println(selected);
         else if(selected.size() <= max)
            System.out.println(selected); 
      }
      else{
         for(String s : relevantWords){
            if(userInput.subtract(inventories.get(s)) != null){
               //Creating new temp so that it doesn't "over-store" words
               List<String> temp = new ArrayList<String>(selected);
               temp.add(s);
               explore(relevantWords, max, temp, 
                       userInput.subtract(inventories.get(s)));
            }
         }
      }
   }
}