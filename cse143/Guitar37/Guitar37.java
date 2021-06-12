/**-------------------------------------------------------------------
* Class: Guitar37
* File: Guitar37.java
* Description: A class which stores 37 strings. Plays notes, plucks, and
* tics all of those strings. The strings are assinged corresponding
* keys. Implements Guitar.
* @author: Young Bin Cho (Josh Cho)
* Environment: PC, Windows 8.1, jdk1.8_101, JGrasp
* Date: 10/11/2017
*-------------------------------------------------------------------*/

public class Guitar37 implements Guitar {
   
   private GuitarString[] guitarStrings; //Array of guitar strings
   private int time; //Number of tics
   public static final String KEYBOARD =
        "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";  //Keyboard layout
      
   /**--------------------------------------------------------------
   * Method: Guitar37
   * Description: A constructer which initializes the array of
   * guitar strings and time variable (number of tics). Assign appropriate
   * frequency for each string.
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public Guitar37() {
      guitarStrings = new GuitarString[37];
      time = 0;
      for(int i = 0; i < 37; i++) {
         guitarStrings[i] = new GuitarString(440 * Math.pow(2, (i - 24)/12.0));
      }
   }
   
   /**--------------------------------------------------------------
   * Method: playNote
   * Description: Takes the given pitch and plucks the appropriate
   * string for the pitch.
   * @param pitch : The user given pitch
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public void playNote(int pitch) {
      if(-24 <= pitch && pitch <= 12)
         guitarStrings[pitch + 24].pluck();
   }
   
   /**--------------------------------------------------------------
   * Method: hasString
   * Description: Checks if the user input char (keyboard) is playable.
   * @param string : The character that is to be examined
   * @return boolean : True or false depending on whether the given
   * char is playable
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public boolean hasString(char string) {
      if(KEYBOARD.indexOf(string) >= 0)
         return true;
      return false;
   }
   
   /**--------------------------------------------------------------
   * Method: pluck
   * Description: Plucks the corresponding string to user input.
   * @param string : The string that is to be plucked
   * @throws IllegalArgumentException : If the user input is not a
   * valid key (not playable)
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public void pluck(char string) {
      if(KEYBOARD.indexOf(string) < 0) {
         throw new IllegalArgumentException("Char: " + string);
      }
      guitarStrings[KEYBOARD.indexOf(string)].pluck();
   }
   
   /**--------------------------------------------------------------
   * Method: sample
   * Description: Adds the sample value of all the strings.
   * @return double : The added value of sample of all the strings
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public double sample() {
      double sample = 0;
      for(int i = 0; i < guitarStrings.length; i++) {
         sample += guitarStrings[i].sample();
      }
      return sample;
   }
   
   /**--------------------------------------------------------------
   * Method: tic
   * Description: Tics all the strings. Increases the time variable
   * everytime tic is perfomed.
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public void tic() {
      for(int i = 0; i < guitarStrings.length; i++) {
         guitarStrings[i].tic();
      }
      time++;
   }
   
   /**--------------------------------------------------------------
   * Method: time
   * Description: Returns the number of tic performed.
   * @return int : Number of tic performed
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public int time() {
      return time;
   }
}