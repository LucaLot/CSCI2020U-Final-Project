/*
* Date: March 20, 2020
* File Name: StartScreen.java
* Purpose: The screen is used to begin the blackjack game
 */

package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Screen;

import java.awt.*;
import java.io.*;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.application.Platform;
import javafx.scene.shape.Circle;



public class StartScreen extends Application implements Runnable {

    /*
    * Parameters: N/a
    * Return: N/a
    * Purpose: Used to store the actions that need to start when a thread for this specific class is created
     */
    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    startS();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    /*
    * Parameters:N/A
    * Return: N/A
    * Purpose: The necessary components created and used to create the intro screen
     */
    public void startS() throws FileNotFoundException {
        Stage primaryStage = new Stage();
        GridPane pane = new GridPane();
        // the start, instruction, high score and exit buttons on the front screen
        Button start = new Button("START");
        Button rules = new Button("INSTRUCTIONS");
        Button highScores = new Button("HIGH SCORES");
        Button exit = new Button("EXIT");

        start.setShape(new Circle(3));
        start.setMaxSize(130,50);
        start.setMinSize(130,50);
        start.setStyle("-fx-background-color: Black");
        start.setTextFill(Color.WHITE);
        start.setFont(new Font(14));

        rules.setShape(new Circle(1.5));
        rules.setMaxSize(130,50);
        rules.setMinSize(130,50);
        rules.setStyle("-fx-background-color: Black");
        rules.setTextFill(Color.WHITE);
        rules.setFont(new Font(14));

        highScores.setShape(new Circle(1.5));
        highScores.setMaxSize(130,50);
        highScores.setMinSize(130,50);
        highScores.setStyle("-fx-background-color: Black");
        highScores.setTextFill(Color.WHITE);
        highScores.setFont(new Font(14));

        exit.setShape(new Circle(1.5));
        exit.setMaxSize(130,50);
        exit.setMinSize(130,50);
        exit.setStyle("-fx-background-color: Black");
        exit.setTextFill(Color.WHITE);
        exit.setFont(new Font(14));

        // event handler for when the start button is selected triggering the beginning of the game itself
        start.setOnAction((event) -> {
            Runnable gameStart = new BlackjackApp();
            Thread game = new Thread(gameStart);
            primaryStage.close();
            game.run();
        });

        // event handler for when the instructions button is selected allowing the program to display the rules of the game
        rules.setOnAction((event) -> {
            Runnable ruleStart = new Rules();
            Thread rule = new Thread(ruleStart);
            primaryStage.close();
            rule.run();
        });
        // event handler for when the high score button is selected allowing the program to display user high scores
        highScores.setOnAction((event) -> {
          Score score = new Score();
          primaryStage.close();
          score.checkBal();
        });
            // event handler for when the instructions button is selected causing the program to exit
        exit.setOnAction((event) -> {
          System.exit(0);
        });

        // used to import and use an image for the background of the introduction screen
        Image image = new Image("/images/blackjack.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,new BackgroundSize(450,600,
                true,true,true,true));

        Background background = new Background(backgroundImage);
        pane.setBackground(background);


        pane.setPadding(new Insets(10,10,10,10));
        pane.setHgap(5);
        pane.setVgap(5);

        pane.setAlignment(Pos.CENTER_RIGHT);

        pane.add(start,1,0);
        pane.add(rules,1,3);
        pane.add(highScores,1,5);
        pane.add(exit,1,7);

        Scene scene = new Scene(pane);
        primaryStage.setMaxHeight(750);
        primaryStage.setMaxWidth(600);
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
