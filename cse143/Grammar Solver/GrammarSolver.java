/**-------------------------------------------------------------------
* Description: Takes in a list of rules. Seperates nonterminals and
* terminals from the given list. Sorts and records the given grammar
* rules. Returns user wanted nonterminal as words (not as in grammar
* notations).
* @author: Young Bin Cho (Josh Cho), CSE 143A Section AE
* Date: 11/01/2017
*-------------------------------------------------------------------*/
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GrammarSolver {
   
   private final SortedMap<String, List<String>> rules; //Stores grammar rules, nonterminals as key
   private String augmentedString; //String that temporarily stores the user requested sentence
   
   /**--------------------------------------------------------------
   * Description: Takes a list of grammar rules and seperates nonterminals
   * from terminals. Sorts and records all grammar rules accordingly paired.
   * @param Grammar : List of grammar rules
   * @throws IllegalArgumentException : User given list is empty
   * @throws IllegalArgumentException : Duplicate rules inputed
   *--------------------------------------------------------------*/
   public GrammarSolver(List<String> Grammar){
      //Exception handle: user given list is emptyBarbara Bush
      if(Grammar.isEmpty()) {
         throw new IllegalArgumentException("List is Empty");
      }
      //Initializing class variables
      rules = new TreeMap<String, List<String>>();
      augmentedString = "";
      //Sorting list into map of nonterminals and terminals
      for(String str : Grammar) {
         String[] s = str.trim().split("::=");
         //Exception handle: given nonterminal was already passed
         if(rules.containsKey(s[0]))
            throw new IllegalArgumentException("Duplicate rules found");
         else
            rules.put(s[0], new ArrayList<String>());
         String[] s2 = s[1].trim().split("[|]+");
         for(String str2 : s2) {
            rules.get(s[0]).add(str2.trim());
         }
      }
   }
   
   /**--------------------------------------------------------------
   * Description: Checks if a certain nonterminal is being considered
   * by the GrammarSolver.
   * @param symbol : Nonterminal that is to be checked
   * @return boolean : True if given nonterminal is being considered,
   * false otherwise
   *--------------------------------------------------------------*/
   public boolean grammarContains(String symbol){
      return rules.containsKey(symbol);
   }
   
   /**--------------------------------------------------------------
   * Description: Returns a string of nonterminals being considered
   * in alphabetic order.
   * @return String : Augmented string of all nonterminals being
   * considered by the GrammarSolver, surrounded by "[" and "]"
   *--------------------------------------------------------------*/
   public String getSymbols(){
      return rules.keySet().toString();
   }
   
   /**--------------------------------------------------------------
   * Description: Generates a user wanted nonterminal with words,
   * according to the grammar rules, user wanted number of times.
   * @param symbol : The nonterminal that is to be made into string
   * @param times : Number of string(nonterminal) to be created
   * @return String[] : Array of user wanted string(nonterminal), repeated
   * user wanted times
   * @throws IllegalArgumentException : There is no such nonterminal 
   * or invalid number of reproduction
   *--------------------------------------------------------------*/
   public String[] generate(String symbol, int times){
      //Exception handle: there is no such nonterminal or times is less than zero
      if(!rules.containsKey(symbol) || times < 0) {
         throw new IllegalArgumentException("Not in rule or times invalid");
      }
      String[] collection = new String[times];
      for(int i = 0; i < times; i++) {
         augmentedString = ""; //Reset the augmented string
         stringGenerator(symbol); //See stringGenerator
         collection[i] = augmentedString.trim();
      }
      return collection;
   }
   
   /**--------------------------------------------------------------
   * Description: Checks if the given string is a terminal, if not
   * it retrieves the according list of nonterminals from the map and
   * selects a random element from it. Sends it to generatorWorker.
   * @param str : String that is to be checked if terminal or not
   *--------------------------------------------------------------*/
   private void stringGenerator(String str){
      /*If the given string is not in the keyset(thus not a nonterminal) add it to 
      * augmentedString.But if the string is in the keyset(thus a nonterminal), 
      * get the associated ArrayList of strings and get a random string inside that
      * that ArrayList. Send that string to generatorWorker(See generatorWorker).
      */
      if(!rules.containsKey(str))
         augmentedString += (str + " ");
      else
         generatorWorker(rules.get(str).get(generateRandom(rules.get(str).size())));
   }
   
   /**--------------------------------------------------------------
   * Description: Splits the given string by whitespace, sends each
   * seperated string to stringGenerator.
   * @param str : String that is to be split by whitespace
   *--------------------------------------------------------------*/
   private void generatorWorker(String str){
      /*Splits the given string by white spaces. If the given string is a
      * sequence of nonterminals, it sends it to the stringGenerator
      * in an orderly fashion so that it produces words in the correct
      * order. (See stringGenerator.
      */
      for(String string : str.trim().split("\\s+"))
         stringGenerator(string);
   }
   
   /**--------------------------------------------------------------
   * Description: Generates a random integer between 0 and (given
   * maximum - 1).
   * @param max : Given maximum
   * @return int : A random integer in the boundary
   *--------------------------------------------------------------*/
   private int generateRandom(int max){
      return ThreadLocalRandom.current().nextInt(0, max);
      /*The max is not (max - 1) here because the size of
      * an ArrayList will be it's (maximum index + 1).
      * This is to generate a proper index that is in the bounds.
      */
   }
}