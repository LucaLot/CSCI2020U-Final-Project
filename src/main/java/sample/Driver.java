/*
 * Date: March 20, 2020
 * File Name: Driver.java
 * Purpose: The class is used to trigger the whole beginning of the game
 */

package sample;


import javafx.application.Application;
import javafx.stage.Stage;

public class Driver extends Application {



    public static void main(String[] args) {
        System.out.println("System Succesfully started");

        Runnable loading = new LoadingScreen();
        Thread load = new Thread(loading);
        load.run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
