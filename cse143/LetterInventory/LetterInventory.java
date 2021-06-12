/**-------------------------------------------------------------------
* Class: LetterInventory
* File: LetterInventory.java
* Description: A class which stores the number of each alphabet in the
* given string ignoring all non-alphabet characters. Also keeps track
* of how many alphabets are there in total.
* @author: Young Bin Cho (Josh Cho)
* Environment: PC, Windows 8.1, jdk1.8_101, JGrasp
* Date: 10/05/2017
*-------------------------------------------------------------------*/

public class LetterInventory {
   
   private int[] alphabetData; //List that keeps track of the number of alphabets
   private int size; //Total number of alphabets stored
   
   /**--------------------------------------------------------------
   * Method: LetterInventory
   * Description: A constructor that initializes the private fields,
   * reads in the user input string and record how many of each 
   * alphabets are there (ignoring non-alphabet characters). Uses the
   * logic where the int value of lower case 'a' is 96. Recording is done
   * in an orderly fashion starting from there.
   * @param data : The string that is to be analyzed of how many of
   * each alphabet is present
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/      
   public LetterInventory(String data) {
      //Initializing private fields
      alphabetData = new int[27];
      size = 0;
      //Removing all non-alphabet characters and changing them to lower case
      data = data.replaceAll("[^a-zA-Z]", "").toLowerCase();
      //Recording the number of alphabets.
      for(int i = 0; i < data.length(); i++) {
         alphabetData[data.charAt(i) - 96] = alphabetData[data.charAt(i) - 96] + 1;
         size++;
      }
   }
   
   /**--------------------------------------------------------------
   * Method: get
   * Description: Returns the count of an alphabet user
   * wants.
   * @param letter : An alphabet that is to be searched of how many
   * was recorded
   * @return int : The count of the wanted alphabet
   * @throws IllegalArgumentException : If the given char is non-
   * alphabet, it throws an exception as it is unsearchable.
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/
   public int get(char letter) {
      if(!Character.isLetter(letter)) {
         throw new IllegalArgumentException("Letter: " + letter);
      }
      return alphabetData[Character.toLowerCase(letter) - 96];
   }
   
   /**--------------------------------------------------------------
   * Method: set
   * Description: Sets the count of an alphabet to user input. 
   * @param letter : The alphabet that the count is to be changed
   * @param value : The integer that is to be set as the count of the
   * given alphabet
   * @throws IllegalArgumentException : If the given char is non-
   * alphabet or the given integer is negative, it throws an exception 
   * as it is unsearchable and negative characters don't exist.
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/
   public void set(char letter, int value) {
      if(!Character.isLetter(letter) || value < 0) {
         throw new IllegalArgumentException("Letter: " + letter + "Value: " + value);
      }
      size += (value - alphabetData[Character.toLowerCase(letter) - 96]);
      alphabetData[Character.toLowerCase(letter) - 96] = value;
   }
   
   /**--------------------------------------------------------------
   * Method: size
   * Description: Returns the size field, which stores the total count
   * of how many alphabets are stored in total.
   * @return int : The count of how many of alphabets are stored in
   * total
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/
   public int size() {
      return size;
   }
   
   /**--------------------------------------------------------------
   * Method: isEmpty
   * Description: Checks if the LetterInventory is empty, meaning no
   * alphabets stored (passed). This is done through checking if size
   * field is zero.
   * @return boolean : True or false depending on the array being
   * empty or not
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/
   public boolean isEmpty() {
      return size == 0;
   }
   
   /**--------------------------------------------------------------
   * Method: toString
   * Description: Returns a string of alphabets in alphabetic order,
   * repeated as many times as they were originally stored.
   * @return String : The string that represents the whole data stored
   * in LetterInventory, surrounded by "[" and "]"
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/
   public String toString() {
      String augmentedString = "";
      for(int i = 0; i < alphabetData.length; i++) {
         for(int j = 0; j < alphabetData[i]; j++) {
            augmentedString += (char)(i + 96);
         }
      }
      return "[" + augmentedString + "]";
   }
   
   /**--------------------------------------------------------------
   * Method: add
   * Description: Adds the given LetterInventory to the current one.
   * Adds the counts for each alphabet and fixes the size to match the
   * new LetterInventory.
   * @param other : The LetterInventory that is to be added
   * @return LetterInventory : The LetterInventory that holds the new
   * array of integers representing the number of each alphabet as well
   * as the new size
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/
   public LetterInventory add (LetterInventory other) {
      //Creating temporary fields so the data in this LetterInventory is untouched
      LetterInventory tempInventory = new LetterInventory("");
      int[] tempArray = new int[27];
      int tempSize = size; 
      for(int i = 0; i < 26; i++){
         tempArray[i] = alphabetData[i] + other.alphabetData[i];
         tempSize += other.alphabetData[i];
      }
      tempInventory.alphabetData = tempArray;
      tempInventory.size = tempSize;
      return tempInventory;
   }
   
   /**--------------------------------------------------------------
   * Method: subtract
   * Description: Subtracts the given LetterInventory to the current one.
   * Subtracts the counts for each alphabet and fixes the size to match the
   * new LetterInventory. Returns null if any of the subtracted values are
   * negative.
   * @param other : The LetterInventory that is to be subtracted
   * @return LetterInventory : The LetterInventory that holds the new
   * array of integers representing the number of each alphabet as well
   * as the new size
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/05/2017
   *--------------------------------------------------------------*/
   public LetterInventory subtract (LetterInventory other) {
      //Creating temporary fields so the data in this LetterInventory is untouched
      LetterInventory tempInventory = new LetterInventory("");
      int[] tempArray = new int[27];
      int tempSize = size;
      for(int i = 0; i < 26; i++){
         if(alphabetData[i] - other.alphabetData[i] < 0) {
            return null;
         }
         tempArray[i] = alphabetData[i] - other.alphabetData[i];
         tempSize -= other.alphabetData[i];
      }
      tempInventory.alphabetData = tempArray;
      tempInventory.size = tempSize;
      return tempInventory;
   }
}