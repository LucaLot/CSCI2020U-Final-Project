package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class Hand {

    private ObservableList<Node> cards;

    private int aces = 0;
    private int value = 0;

    public Hand(ObservableList<Node> cards) {
        this.cards = cards;
    }

    public void takeCard(Card card) {
        cards.add(card);
	//If the card value is an ace
        if (card.getValue() == 1 || card.getValue() == 14
            || card.getValue() == 27 || card.getValue() == 40) {
            aces++;
        }
        value += card.getValue();
        if (value > 21 && aces > 0) {
            value -= 10;    //count ace as a one instead of 11
            aces--;
        }
    }

    public void reset() {
        cards.clear();
        value = 0;
        aces = 0;
    }

    public int getValue() {
        return value;
    }
}
