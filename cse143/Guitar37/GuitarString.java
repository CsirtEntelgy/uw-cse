import java.util.*;

/**-------------------------------------------------------------------
* Class: GuitarString
* File: GuitarString.java
* Description: A class which stores the ring buffer. Also allows user
* to pluck, tic (apply Karplus-Strong update), and return the current
* sample.
* @see java.util.*
* @author: Young Bin Cho (Josh Cho)
* Environment: PC, Windows 8.1, jdk1.8_101, JGrasp
* Date: 10/11/2017
*-------------------------------------------------------------------*/

public class GuitarString {

   private Queue<Double> ringBuffer; //Queue that stores the ring buffer
   public double ENERGY_DECAY_FACTOR = 0.996;
   
   /**--------------------------------------------------------------
   * Method: GuitarString
   * Description: A constructer which initializes the ring buffer and
   * populates the ring buffer with appropriate number of zeros.
   * @param frequency : User input that is to be used as frequency
   * @throws IllegalArgumentException : If the user input is less than
   * or equal to zero. If the resulting size of ring buffer is less 
   * than 2
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public GuitarString(double frequency) {
      //Error handle: if the user input is invalid
      if(frequency <= 0) {
         throw new IllegalArgumentException("Frequency: " + frequency);
      }
      ringBuffer = new LinkedList<>(); //Initializing ringBuffer
      for(int i = 0; i < (int) Math.rint(StdAudio.SAMPLE_RATE/frequency); i++) {
         ringBuffer.add(0.0);
      }
      //Error handle: if the resulting size of ring buffer is less than 2
      if(ringBuffer.size() < 2) {
         throw new IllegalArgumentException("Size: " + ringBuffer.size());
      }
   }
   
   /**--------------------------------------------------------------
   * Method: GuitarString
   * Description: A constructer which initializes the ring buffer and
   * fills it with the user given list of numbers.
   * @param init : The array of numbers that is to populate the ring
   * buffer
   * @throws IllegalArgumentException : If the length of user input
   * is less than 2
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public GuitarString(double[] init) {
      //Error handle: if the length of user input is less than 2
      if(init.length < 2) {
         throw new IllegalArgumentException("Length: " + init.length);
      }
      ringBuffer = new LinkedList<>(); //Initializing ringBuffer
      for(int i = 0; i < init.length; i++) {
         ringBuffer.add(init[i]);
      }
   }
   
   /**--------------------------------------------------------------
   * Method: pluck
   * Description: Simulates pluck. Replaces all the elements in the
   * ring buffer with random number between -0.5 and 0.5.
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public void pluck() {
      Random rand = new Random();
      for(int i = 0; i < ringBuffer.size(); i++) {
         ringBuffer.add(rand.nextInt(2) - 0.5);
         ringBuffer.remove();
      }
   }
   
   /**--------------------------------------------------------------
   * Method: tic
   * Description: Takes first two sample in the ring buffer and
   * applys the Karplus-Strong update. Removes first sample.
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public void tic() {
      double first = ringBuffer.peek();
      ringBuffer.remove();
      double second = ringBuffer.peek();
      ringBuffer.add(ENERGY_DECAY_FACTOR * ((first + second)/2));
   }
   
   /**--------------------------------------------------------------
   * Method: sample
   * Description: Returns the current sample (first one of the ring
   * buffer que).
   * @return double : The current sample
   * @author: Young Bin Cho (Josh Cho)
   * Date: 10/11/2017
   *--------------------------------------------------------------*/
   public double sample() {
      return ringBuffer.peek();
   }
}