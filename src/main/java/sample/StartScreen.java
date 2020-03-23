/*
* Name: Vithusan Jeevaratnam
* Date: March 20th, 2020
* FileName: StartScreen.java
* Purpose: Used in order to display the introduction screen of the game
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


import java.awt.*;
import java.io.*;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.application.Platform;
import javafx.scene.shape.Circle;


public class StartScreen extends Application implements Runnable {

    /*
    * Parameters: N/A
    * Return: N/A
    * Purpose: Is a runnable method which enables us to begin a thread when needed
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
    * Parameters: N/A
    * Return: N/A
    * Purpose: Creates the intro screen and displays on the user screen when called upon
     */
    public void startS() throws FileNotFoundException {

        // creates the intial components needed
        Stage primaryStage = new Stage();
        GridPane pane = new GridPane();
        Button start = new Button("START");
        Button rules = new Button("INSTRUCTIONS");

        // creates a button into a circle and modify its display
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


        // The action and event handler used for the start button which begins the thread for the actual game
        start.setOnAction((event) -> {
            Runnable gameStart = new BlackjackApp();
            Thread game = new Thread(gameStart);
            primaryStage.close();
            game.run();
        });

        // The action and event handler for the rules button which begins the thread for the instuction/rules screen
        rules.setOnAction((event) -> {
            Runnable ruleStart = new Rules();
            Thread rule = new Thread(ruleStart);
            primaryStage.close();
            rule.run();
        });


        // Used to import and display the background image for the game
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

        Scene scene = new Scene(pane);
        primaryStage.setMaxHeight(750);
        primaryStage.setMaxWidth(600);
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.show();




    }
}
