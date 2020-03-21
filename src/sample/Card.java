package sample;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * Game card
 *
 */
public class Card extends Parent {
    private int value; //Holds the value of the called card
    public Card(int input) {
        ImageView card = new ImageView("/sample/resources/Cards/"+input+".png"); //Calls the card
        value = findValue(input); //Assigns card value
        getChildren().add(card); //Adds card to GUI
    }

    public int findValue(int input){
        value = input % 13;//Most card values can be assigned with this formula
        //If card is an ace
        if(value==1){
            value = 11;
        }
        //If card is a face value
        else if(value>10||value==0){
            value = 10;
        }
        return value;
    }
    public int getValue(){
        return value;
    }
}
