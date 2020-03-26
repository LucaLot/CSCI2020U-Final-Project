
/*
* Date: March 11, 2020
* File Name: Deck.java
* Purpose: Creates the deck for the player to draw cards from
 */
package sample;

public class Deck {

    private Card[] cards = new Card[52];//Holds all cards

    public Deck() {
        refill();
    }
    /**
     * Restocks the deck, used for setup after every game
     */
    public final void refill() {
        for (int i=1;i<=52;i++) {
          cards[i-1] = new Card(i);//Stores all cards inside an array
        }
    }
    /**
     * Draws cards from the pile, and removes any drawn card
     * @return Card selected
     */
    public Card drawCard() {
        Card card = null;
        while (card == null) {
            int index = (int)(Math.random()*cards.length);//Calls random card
            card = cards[index];
            cards[index] = null;//When a card is in play, it is removed from the draw pile, becoming null
        }
        return card;
    }
}
