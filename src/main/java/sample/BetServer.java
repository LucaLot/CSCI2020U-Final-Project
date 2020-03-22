// Author: Malaviya Nerumalan
// Date: March 20, 2020
// Purpose: To create a bet server that handles all bets made by the user and updates the money in their bank
//**************************************************************************************************************************

package sample;

import javafx.application.Platform;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BetServer extends Application
{
  public Bet bets = new Bet();
  private TextArea output = new TextArea();

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    new Thread(()-> {
      try {
        // creates the server
        ServerSocket server = new ServerSocket(8000);
        Platform.runLater(() -> output.appendText("Server started"));
        // connects to the client
        Socket socket = server.accept();
        Platform.runLater(() -> output.appendText("Connected to client"));

        DataInputStream dataInput = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());

        while (true)
        {
          // reads the bet made by the user
          float currentBet = dataInput.readFloat();
          String winner = dataInput.readUTF();
          int blackjack = dataInput.readInt();
          bets.addBet(currentBet);

          if (blackjack == 1)
          bets.blackjack();
          else if (winner.equals("PLAYER"))
            bets.playerWin();
          else
            bets.dealerWin();

          // sends the total money in the bank to the user
          dataOutput.writeFloat(bets.getTotal());
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }).start();

    Scene scene = new Scene(new ScrollPane(output), 200, 200);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
