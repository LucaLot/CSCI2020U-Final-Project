/*
* Date: March 18, 2020
* File Name: Rules.java
* Purpose: Allows the display of rules to the user
 */
package sample;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.paint.ImagePattern;
import javafx.application.Application;
import java.io.FileNotFoundException;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Scanner;
import java.io.File;
import javafx.geometry.Insets;

public class Rules extends Application implements Runnable{
    GridPane root = new GridPane();
    HBox cards = new HBox();
    VBox Rules = new VBox();
    HBox backButt = new HBox();

    /**
     * Starts the code by setting everything required
     */
    public void StartUp() throws FileNotFoundException {
        Stage primaryStage  = new Stage();
        //Return button
        Button back = new Button("BACK");
        back.setShape(new Circle(3));
        back.setMaxSize(130,40);
        back.setMinSize(130,40);
        back.setStyle("-fx-background-color: Black");
        back.setTextFill(Color.WHITE);
        back.setFont(new Font(14));
        backButt.getChildren().addAll(back);
        //Sets up to allow the user to return to the start screen
        back.setOnAction((event) -> {
            Runnable screenStart = new StartScreen();
            Thread screen = new Thread(screenStart);
            primaryStage.close();
            screen.run();
        });

        // creates the background
        Image img = new Image("/images/table.png");
        Rectangle upBG = new Rectangle(600, 640);
        upBG.setFill(new ImagePattern(img));

        // displays to playing cards in the top-right corner
        ImageView imageJack1 = new ImageView("/Cards/11.png");
        ImageView imageJack2 = new ImageView("Cards/50.png");
        imageJack1.setRotate(imageJack1.getRotate() - 10);
        imageJack2.setRotate(imageJack2.getRotate() + 10);
        cards.setAlignment(Pos.TOP_RIGHT);
        cards.getChildren().addAll(imageJack1, imageJack2);

        root.getChildren().addAll(upBG, getRules(), cards,backButt);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rules");
        primaryStage.setMaxHeight(640);
        primaryStage.setMaxWidth(600);
        primaryStage.setMinHeight(640);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }
    /**
     * Accesses a text file and displays the information pulled in a vBox
     * @return A VBox containing the text information
     */
    private VBox getRules() {
        //Pulls information from file
        File file = new File("src/main/java/sample/Rules.txt");
        Scanner scanFile = null;
        try {
            scanFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // searches through every word
        while (scanFile.hasNext()) {
            //adds each line found into the vBox
            Text rule = new Text(scanFile.nextLine());
            rule.setFill(Color.WHITE);
            rule.setFont(Font.font(15));
            Rules.getChildren().add(rule);
        }
        scanFile.close();

        return Rules;
    }

    @Override
    /**
     * Initial run, allows for the portion of code to run
     */
    public void run() {
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    StartUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
