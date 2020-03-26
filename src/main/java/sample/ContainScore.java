/**
 * Date: March 25, 2020
 * File Name: ContainScore.java
 * Purpose: The class holds user name and scores, in order to be displayed by
 * Score.java in a table column
 */
package sample;
import java.lang.Float;
//Class which contains the score of every user
public class ContainScore{
  private String name;
  private float score;
  //Sets up user names and scores
  public ContainScore(String name, String score){
    this.name = name;
    this.score = Float.parseFloat(score); //Converts to a float, for easier sorting
  }
  /**
   * Function which gets the user's name
   * @return User name
   */
  public String getName(){
    return name;
  }
  /**
   * Function which gets the user's score
   * @return User score
   */
  public float getScore(){
    return score;
  }
}
