package sample;

/**
 * Standard 52 card deck
 *
 *
 */
public class Deck {

    private Card[] cards = new Card[52];//Holds all cards

    public Deck() {
        refill();
    }

    public final void refill() {
        for (int i=1;i<=52;i++) {
            cards[i-1] = new Card(i);//Stores all cards inside an array
        }
    }

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