// Author: Malaviya Nerumalan
// Date: March 20, 2020
// Purpose: To determine the bank amount of the user and sends the server the current bets
//******************************************************************************************************

package sample;

//******************************************************************************************************
// class Bet
// Fields:
//      currentBet - the current bet placed by the user
//      bank - the user's bank
// Methods:
//      constructor
//      addBet - places the bet
//      dealerWin - determines the total amount of money in the bank if the dealer wins
//      playerWin - determines the total amount of money in the bank if the player wins
//      blackjack - determines the total amount of money in the bank if the player has blackjack
//      getTotal - returns the total amount of money in the bank
//******************************************************************************************************
public class Bet
{
    protected float currentBet;
    protected float bank;

    // constructor
    public Bet()
    {
        this.currentBet = 0.0f;
        this.bank = 300.0f;
    }

    //******************************************************************************************************
    // Author: Malaviya Nerumalan
    // Date: March 20, 2020
    // Purpose: To place the current bet made by the user
    // Parameters: money that is bet by the user
    // Return: none
    //*******************************************************************************************************
    public void addBet(float betMoney)
    {
        this.currentBet = betMoney;
    }
    //******************************************************************************************************
    // Author: Malaviya Nerumalan
    // Date: March 20, 2020
    // Purpose: To determine the total amount of money in the bank if the dealer wins
    // Parameters: none
    // Return: none
    //*******************************************************************************************************
    public void dealerWin()
    {
        bank = bank - currentBet;
        if (bank < 0)
            bank = 0;
    }
    //******************************************************************************************************
    // Author: Malaviya Nerumalan
    // Date: March 20, 2020
    // Purpose: To determine the total amount of money in the bank if the player wins
    // Parameters: none
    // Return: none
    //*******************************************************************************************************
    public void playerWin()
    {
        bank = bank + currentBet;
    }
    //******************************************************************************************************
    // Author: Malaviya Nerumalan
    // Date: March 20, 2020
    // Purpose: To determine the total amount of money in the bank if the player has blackjack
    // Parameters: none
    // Return: none
    //*******************************************************************************************************
    public void blackjack()
    {
        bank = bank + (currentBet)*1.5f;
    }
    //******************************************************************************************************
    // Author: Malaviya Nerumalan
    // Date: March 20, 2020
    // Purpose: To return the total amount of money in the bank
    // Parameters: none
    // Return: none
    //*******************************************************************************************************
    public float getTotal()
    {
        return bank;
    }
}
