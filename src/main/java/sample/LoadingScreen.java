/*
 * Date: March 20, 2020
 * File Name: LoadingScreen.java
 * Purpose: The screen is used to display a progress bar stimulating the effect of the game loading
 */


package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import java.awt.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;






public class LoadingScreen extends Application implements Runnable {


    static double ii = 0;
    // timer variable is used to delay the beginning of the game
    Timer timer = new Timer();

    @Override
    public void start(Stage stage) throws Exception {
    }

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
                    startLoad();
                    // once timer is over, the game will start up
                    timer.schedule(new TimerTask(){
                        @Override
                        public void run() {
                            Runnable starting = new StartScreen();
                            Thread start = new Thread(starting);
                            start.run();
                        }
                    },2000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /*
    * Parameters: N/a
    * Return: N/a
    * Purpose: stores the loading process with a progress bar
     */
    public void startLoad(){
        Stage primaryStage = new Stage();
        TilePane r = new TilePane();

        // progress bar
        ProgressBar pb = new ProgressBar();

        pb.setMinHeight(50);
        pb.setMinWidth(250);

        r.getChildren().add(pb);

        // event handler to manipulate the movement on the progress bar
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // set progress to different level of progressbar
                ii += 0.1;
                pb.setProgress(ii);
            }

        };
        Scene loading = new Scene(r,250,50);
        primaryStage.setScene(loading);
        primaryStage.show();

    }
}
