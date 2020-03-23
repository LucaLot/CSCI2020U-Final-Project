/*
 * Name: Vithusan Jeevaratnam
 * Date: March 20th, 2020
 * FileName: Driver.java
 * Purpose: Used to begin the program all together
 */
package sample;


import javafx.application.Application;
import javafx.stage.Stage;

public class Driver extends Application {



    public static void main(String[] args) {
        System.out.println("System Succesfully started");

        // CREATES THE THREAD WHICH TRIGGERS THE WHOLE GAME ALL TOGETHER
        Runnable loading = new LoadingScreen();
        Thread load = new Thread(loading);
        load.run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
