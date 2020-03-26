/*
* Date: March 11, 2020
* File Name: Card.java
* Purpose: Allows the entry of cards into the system, allowing the user to draw
* and handle cards
 */
package sample;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class Card extends Parent {
    private int value; //Holds the value of the called card (1-11)
    public Card(int input) {
        ImageView card = new ImageView("/Cards/"+input+".png"); //Calls the card
        value = findValue(input); //Assigns card value
        getChildren().add(card); //Adds card to GUI
    }
    /**
     * Depending on the card played, calculates the value of the card
     * @param  input Numerical value of the card inputted into the system
     * @return       Returns the played value of the card
     */
    public int findValue(int input){
      value = input % 13;//Most card values can be assigned with this formula
      //If card is an ace, set to 11
      if(value==1){
        value = 11;
      }
      //If card has a face value, set to 10
      else if(value>10||value==0){
        value = 10;
      }
      return value;
    }
    /**
     * Gets the total value of the card without needing to calculate a value
     * @return Card value
     */
    public int getValue(){
      return value;
    }
}
