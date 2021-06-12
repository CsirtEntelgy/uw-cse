/**-------------------------------------------------------------------
* Description: Takes in a collection of words and filters words with 
* inappropriate length. Analyzes the pattern of a certain given character
* in a word and chooses the biggest set of words out of all the patterns.
* Also have methods for getting the number of guesses left, letters
* guessed, current set of words being considered, and the current
* pattern.
* @author: Young Bin Cho (Josh Cho), CSE 143A Section AE
* Date: 11/16/2017
*-------------------------------------------------------------------*/

import java.util.*;

public class HangmanManager {
   
   private Set<String> currentWords; //Current set of words being considered
   private SortedSet<Character> guessedLetters; //Sorted set of characters that have been guessed
   private String currentPattern; //Current pattern that is being considered
   private int numberOfGuesses; //Number of guesses left
   
   /**--------------------------------------------------------------
   * Description: A constructor which initializes the class variables,
   * takes words from the user given collection, and filters words
   * of inappropriate length and populates the set of words with the
   * rest.
   * @param dictionary : User given collection of words
   * @param length : The length of words that is to be used
   * @param max : Number of guesses to be given
   * @throws IllegalArgumentException : The length of word is less
   * than one or number of guesses is less than zero
   *--------------------------------------------------------------*/
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      //Exception handle: the length of word is less than one or number of guesses is less than zero
      if(length < 1 || max < 0)
         throw new IllegalArgumentException("Illegal length of word or number of guesses");
      //Initializing class variables
      currentWords = new TreeSet<String>();
      guessedLetters = new TreeSet<Character>();
      currentPattern = "";
      numberOfGuesses = max;
      currentKey = 0;
      LENGTH_OF_WORD = length;
      //Populating set with strings of appropriate length
      for(String s : dictionary) {
         if(s.length() == LENGTH_OF_WORD)
            currentWords.add(s);
      }
      //Creates an empty pattern of appropriate length
      for(int i = 0; i < LENGTH_OF_WORD; i++) {
         currentPattern += "-";
      }
   }
   
   /**--------------------------------------------------------------
   * Description: Returns the current set of words being considered
   * by the HangmanManager.
   * @return Set<String> : Current set of words being considered
   *--------------------------------------------------------------*/
   public Set<String> words() {
      return currentWords;
   }
   
   /**--------------------------------------------------------------
   * Description: Returns the number of guesses left.
   * @return int : Number of guesses left
   *--------------------------------------------------------------*/
   public int guessesLeft() {
      return numberOfGuesses;
   }
   
   /**--------------------------------------------------------------
   * Description: Returns a sorted set of characters that have been
   * guessed before.
   * @return SortedSet<Character> : Sorted set of characters that 
   * have been guessed before
   *--------------------------------------------------------------*/
   public SortedSet<Character> guesses() {
      return guessedLetters;
   }
   
   /**--------------------------------------------------------------
   * Description: Inserts a space between every character in pattern.
   * Returns the current pattern being considered.
   * @return String : Current pattern being considered
   * @throws IllegalStateException : The current set of words is
   * empty.
   *--------------------------------------------------------------*/
   public String pattern() {
      //Exception handle: the current set of words is empty.
      if(currentWords.isEmpty()) {
         throw new IllegalStateException("There are no more words left");
      }
      //Insert space between every character
      String temp = currentPattern.replace("", " ").trim();
      return temp;
   }
   
   /**--------------------------------------------------------------
   * Description: Sort the current set of words according to the user
   * given character. The biggest set of words containing the certain
   * pattern will be choosen as the current set of words being considered.
   * Creates a new pattern according to the changes and reduces the
   * number of guesses left when the user makes a wrong guess.
   * Returns how many times the user given character occured in the
   * new pattern that is to be used.
   * @return int : How many times the user given character occured 
   * in the new pattern that is to be used
   * @throws IllegalStateException : Number of guesses are all used 
   * or set of words is empty
   * @throws IllegalArgumentException : Character passed has already 
   * been guessed
   *--------------------------------------------------------------*/
   public int record(char guess) {
      //Exception handle: number of guesses are all used or set of words is empty
      if(numberOfGuesses < 1 || currentWords.isEmpty()) {
         throw new IllegalStateException("Number of guesses all used or there are no more words left");
      }
      //Exception handle: character passed has already been guessed
      if(!currentWords.isEmpty() && guessedLetters.contains(guess)) {
         throw new IllegalArgumentException("Character already guessed");
      }
      //See intoMap, getIndex, biggestSet, and createPattern
      biggestSet(intoMap(guess), guess);
      //Adding guessed letter to set
      guessedLetters.add(guess);
      //Decreasing number of guesses if the guess was wrong (see createPattern)
      if(currentKey == 0)
         numberOfGuesses--;
      return currentKey;
   }
   
   /**--------------------------------------------------------------
   * Description: Sorts the current set of words by where the user
   * given character appears in each word.
   * @return Map<String, Set<String>> : Organized map of the words
   *--------------------------------------------------------------*/
   private Map<String, Set<String>> intoMap(char ch) {
      Map<String, Set<String>> temp = new TreeMap<String, Set<String>>();
      for(String s : currentWords) {
         if(!temp.containsKey(getIndex(s,ch)))
            temp.put(getIndex(s, ch), new TreeSet<String>());
         temp.get(getIndex(s, ch)).add(s);
      }
      return temp;
   }
   
   /**--------------------------------------------------------------
   * Description: Returns a string of indexes. The given string is
   * analyzed and records the index of occurance of a character.
   * @return String : String of indexes
   *--------------------------------------------------------------*/
   private String getIndex(String str, char ch) {
      String temp = "";
      int index = str.indexOf(ch);
      while(index >= 0) {
         temp += index;
         index = str.indexOf(ch, index+1);
      }
      return temp;
   }
   
   /**--------------------------------------------------------------
   * Description: Finds the biggest set of words in a given map and
   * assign it at the current set of words. Create new pattern.
   * @return Set<String> : Biggest set of words in a given map
   *--------------------------------------------------------------*/
   private void biggestSet(Map<String, Set<String>> bucket, char ch) {
      Map.Entry<String, Set<String>> max = null;
      for(Map.Entry<String, Set<String>> entry : bucket.entrySet()) {
         if (max == null || entry.getValue().size() > max.getValue().size())
            max = entry;
      }
      createPattern(max.getKey(), ch); 
      currentWords = max.getValue();
   }
   
   /**--------------------------------------------------------------
   * Description: Creates new patter. Records the number of occurance
   * of the given character.
   *--------------------------------------------------------------*/
   private void createPattern(String str, char ch) {
      int x = 0;
      StringBuilder temp = new StringBuilder(currentPattern);
      for(int i = 0; i < str.length(); i++) {
         temp.setCharAt(str.charAt(i) - 48, ch);
         x++;
      }
      currentPattern = temp.toString();
      currentKey = x;
   }
}