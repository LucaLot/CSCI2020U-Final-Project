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

    public void startS() throws FileNotFoundException {
        Stage primaryStage = new Stage();
        GridPane pane = new GridPane();
        Button start = new Button("START");
        Button rules = new Button("INSTRUCTIONS");

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

        start.setOnAction((event) -> {
            Runnable gameStart = new BlackjackApp();
            Thread game = new Thread(gameStart);
            game.run();
        });





        FileInputStream input = new FileInputStream("src/main/java/sample/blackjack.jpg");
        Image image = new Image(input);
        BackgroundImage backgroundImage = new BackgroundImage(image,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,new BackgroundSize(450,600,
                true,true,true,true));

        Background background = new Background(backgroundImage);
        pane.setBackground(background);


        //pane.setMaxSize(450,600);
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

