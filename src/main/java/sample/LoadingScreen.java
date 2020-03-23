/*
 * Name: Vithusan Jeevaratnam
 * Date: March 20th, 2020
 * FileName: LoadingScreen.java
 * Purpose: Used in order to create the loading effect to the game through a progress bar
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
    Timer timer = new Timer();

    @Override
    public void start(Stage stage) throws Exception {
    }

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
                    startLoad();

                    // timer used to trigger the start of the game after 5 seconds of loading
                    timer.schedule(new TimerTask(){
                        @Override
                        public void run() {
                            Runnable starting = new StartScreen();
                            Thread start = new Thread(starting);
                            start.run();
                        }
                    },5000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }


    /*
    * Parameters: N/A
    * Return: N/A
    * Purpose: Used to display the progress bar to create the effect of loading up
     */
    public void startLoad(){
        Stage primaryStage = new Stage();
        TilePane r = new TilePane();
        ProgressBar pb = new ProgressBar(); // progress bar

        pb.setMinHeight(50);
        pb.setMinWidth(250);

        r.getChildren().add(pb);

        // event handler used to increase and decrease the loading effect on the progress bar itself
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
