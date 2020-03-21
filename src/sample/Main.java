package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("System Succesfully started");

        Runnable loading = new LoadingScreen();
        Thread load = new Thread(loading);
        load.run();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
