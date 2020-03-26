/*
* Date: March 11, 2020
* File Name: Hand.java
* Purpose: Allows Cards to be put into the user's "hand", while calculating
* the value
 */
package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class Hand {

    private ObservableList<Node> cards;//Holds an array of cards in hand

    private int aces = 0; //How many aces the user has
    private int value = 0;//Total value of user's hand

    public Hand(ObservableList<Node> cards) {
        this.cards = cards;
    }

    /**
     * Takes a card, putting it into the user's hand and calculates the value
     * @param card Card value drawn from avalible deck
     */
    public void takeCard(Card card) {
        cards.add(card);
	       //If the card value is an ace
        if (card.getValue() == 1 || card.getValue() == 14
            || card.getValue() == 27 || card.getValue() == 40) {
            aces++;
        }
        value += card.getValue();
        //If value exceeds 21, but user has an ace, couts the ace as a one instead of
        //11, as the user can still play
        if (value > 21 && aces > 0) {
            value -= 10;    //count ace as a one instead of 11
            aces--;         //As the ace is counted, remove it from being counted
        }
    }
    /**
     * Resets the values, for starting a new game
     */
    public void reset() {
        cards.clear();
        value = 0;
        aces = 0;
    }
    /**
     * Gets the value of the hand of the user
     * @return Total hand value
     */
    public int getValue() {
        return value;
    }
}
