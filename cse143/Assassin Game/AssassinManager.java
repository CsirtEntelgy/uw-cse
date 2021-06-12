import java.util.*;
/**-------------------------------------------------------------------
* Class: AssassinManager
* File: AssassinManager.java
* Description: A class which stores the kill ring and graveyard.
* Perfoms the "kill" and rearranges the kill ring and graveyard accordingly. 
* Has various methods such as checking if the given input is in the kill ring or
* the graveyard, seeing if the game is over and displaying the winner,
* and printing the current kill ring and graveyard.
* @see java.util.*
* @author: Young Bin Cho (Josh Cho)
* Environment: PC, Windows 8.1, jdk1.8_101, JGrasp
* Date: 10/19/2017
*-------------------------------------------------------------------*/
public class AssassinManager {

   private AssassinNode frontRing; //Front of the kill ring
   private AssassinNode frontGraveyard; //Frong of the graveyard
   
   /**--------------------------------------------------------------
   * Description: A constructor which initializes the class variables,
   * takes names from the user given list, and creates a list of nodes
   * containing the names, according to order.
   * @param names : User given list of names
   * @throws IllegalArgumentException : If list is empty
   *--------------------------------------------------------------*/
   public AssassinManager(List<String> names) {
      //Exception handle: list is empty
      if(names.size() == 0) {
         throw new IllegalArgumentException("Size: " + names.size());
      }
      //Initializing class variables
      frontRing = new AssassinNode(names.get(0));
      frontGraveyard = new AssassinNode(null);
      //Creating a list of nodes from given list of names
      AssassinNode current = frontRing; //Temporary AssassinNode to keep track of last node (same for all the following "current")
      for(int i = 1; i < names.size(); i++) {
         AssassinNode add = new AssassinNode(names.get(i));
         current.next = add;
         current = current.next;
      }
   }
   
   /**--------------------------------------------------------------
   * Description: Prints the current kill ring.
   *--------------------------------------------------------------*/
   public void printKillRing() {
      AssassinNode current = frontRing;
      //General case
      while(current.next != null) {
         System.out.println("    " + current.name + " is stalking " + current.next.name);
         current = current.next;
      }
      //Last person stalking the first (Also case of one name)
      System.out.println("    " + current.name + " is stalking " + frontRing.name);
   }
   
   /**--------------------------------------------------------------
   * Description: Prints the current graveyard. Nothing happens if
   * there are no one in the graveyard.
   *--------------------------------------------------------------*/
   public void printGraveyard() {
      AssassinNode current = frontGraveyard;
      //Case of graveyard empty
      if(frontGraveyard.killer == null) {
         return;
      }
      //General case
      while(current != null) {
         System.out.println("    " + current.name + " was killed by " + current.killer);
         current = current.next;
      }
   }
   
   /**--------------------------------------------------------------
   * Description: Checks if the user given name is currently in the
   * kill ring or not.
   * @param name : Name that is to be checked
   * @return boolean : True if name is in kill ring, false otherwise
   *--------------------------------------------------------------*/
   public boolean killRingContains(String name) {
      AssassinNode current = frontRing;
      while(current!= null) {
         if(current.name.toLowerCase().equals(name.toLowerCase()))
            return true;
         current = current.next;
      }
      return false;
   }
   
   /**--------------------------------------------------------------
   * Description: Checks if the user given name is currently in the
   * graveyard or not, returns false if graveyard is empty.
   * @param name : Name that is to be checked
   * @return boolean : True if name is in graveyard, false otherwise
   *--------------------------------------------------------------*/
   public boolean graveyardContains(String name) {
      AssassinNode current = frontGraveyard;
      //Case of graveyard empty
      if(frontGraveyard.killer == null)
         return false;
      //General case
      while(current != null) {
         if(current.name.toLowerCase().equals(name.toLowerCase()))
            return true;
         current = current.next;
      }
      return false;
   }
   
   /**--------------------------------------------------------------
   * Description: Checks if the game is over (only one left in kill ring).
   * @return boolean : True if game is over, false otherwise
   *--------------------------------------------------------------*/
   public boolean gameOver() {
      if(frontRing.next == null)
         return true;
      return false;
   }
   
   /**--------------------------------------------------------------
   * Description: Returns the name of the winner. Returns null if
   * game is not yet over.
   * @return String : Name of winner
   *--------------------------------------------------------------*/
   public String winner() {
      if(gameOver())
         return frontRing.name;
      return null;
   }
   
   /**--------------------------------------------------------------
   * Description: Private method which returns the last node of the
   * link the user given node is in.
   * @param frontOf : The user given node
   * @return AssassinNode : The last node of the link
   *--------------------------------------------------------------*/
   private AssassinNode lastNode(AssassinNode frontOf) {
      AssassinNode current = frontOf;
      while(current.next != null) {
         current = current.next;
      }
      return current;
   }
   
   /**--------------------------------------------------------------
   * Description: Removes the person of the user given name from the
   * kill ring and moves him/her to the graveyard.
   * @param name : The person to be killed (removed)
   * @throws IllegalStateException : If game is over
   * @throws IllegalArgumentException : If the user given name is
   * not in the kill ring
   *--------------------------------------------------------------*/
   public void kill(String name) {
      //Exception handle: game is over
      if(gameOver()) {
         throw new IllegalStateException("Game is over");
      }
      //Exception handle: if the name was not found in ring
      if(!killRingContains(name)) {
         throw new IllegalArgumentException("Name not in kill ring");
      }
      //Case of first node equals to name
      if(frontRing.name.toLowerCase().equals(name.toLowerCase())) {
         frontRing.killer = lastNode(frontRing).name; //Assigning killer
         AssassinNode cutAt = frontRing; //Locating cut point
         frontRing = frontRing.next; //Skip the node to be removed
         cutAt.next = null; //Cuts the node from original
         //Check if the graveyard is empty or not and perform accordingly
         if(frontGraveyard.killer == null) {
            frontGraveyard = cutAt;
         }
         else {
            cutAt.next = frontGraveyard;
            frontGraveyard = cutAt;
         }
      }
      //General case
      AssassinNode current = frontRing;
      while(current != null && current.next != null) {
         if(current.next.name.toLowerCase().equals(name.toLowerCase())) {
            current.next.killer = current.name; //Assigning killer
            AssassinNode cutAt = current.next; //Locating cut point
            current.next = cutAt.next; //Skip the node to be removed
            cutAt.next = null; //Cuts the node from original
            //Check if the graveyard is empty or not and perform accordingly
            if(frontGraveyard.killer == null) {
               frontGraveyard = cutAt;
            }
            else {
               cutAt.next = frontGraveyard;
               frontGraveyard = cutAt;
            }
         }
         current = current.next;
      }
   }
}