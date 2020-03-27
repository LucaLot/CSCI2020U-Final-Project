/*
* Date: March 11, 2020
* File Name: BlackjackApp.java
* Purpose: Main control for the Black Jack app, communicates with other portions
* and allows for betting and dealing
 */
package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.net.Socket;


public class BlackjackApp extends Application implements Runnable{

    private Deck deck = new Deck(); //Creates a deck to pull from
    private Score score = new Score(); //Creates a new instance to hold the final score, if the user wants to see
    private BetServer server = new BetServer(); //Allows the user to add bets
    private Socket connectToServer;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private Hand dealer, player;  //Creates cards that both the dealer and player use
    private Stage primaryStage = new Stage();
    private Text message = new Text();
    private Button exitButton = new Button("EXIT");

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox playerCards = new HBox(20);
    private TextField betMoney = new TextField();
    private Label moneyLabel = new Label("300");
    /**
     * Creates all required variables for the GUI to be displayed
     * @return A pane displaying the GUI information
     */
    private Parent createContent() {
        message.setFill(Color.WHITE);
        //Sets up dealer and player hands
        dealer = new Hand(dealerCards.getChildren());
        player = new Hand(playerCards.getChildren());

        // window for playing field
        Pane root = new Pane();
        root.setPrefSize(590, 800);

        Region background = new Region();
        background.setPrefSize(600, 800);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        VBox rootLayout = new VBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        // table where cards will be placed on
        Rectangle upBG = new Rectangle(590, 560);
        Image img = new Image("/images/table.png");
        upBG.setFill(new ImagePattern(img));
        // menu for the betting buttons and other asthetics
        Rectangle downBG = new Rectangle(560, 170);
        Image img2 = new Image("/images/menu.jpg");
        downBG.setFill(new ImagePattern(img2));

        //Places Board objects on the top of the screen
        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment(Pos.TOP_CENTER);
        leftVBox.setPadding(new Insets(5,10,5,5));

        Text dealerScore = new Text("Dealer: ");
        dealerScore.setFill(Color.WHITE);
        Text playerScore = new Text("Player: ");
        playerScore.setFill(Color.WHITE);

        leftVBox.getChildren().addAll(dealerScore, dealerCards, message, playerCards, playerScore);

        //Places menu objects on the bottom of the screen
        VBox rightVBox = new VBox(2);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setPadding(new Insets(5,5,0,40));

        //Sets up labels that relate to the user bet amount
        betMoney.setStyle("-fx-background-color: white");
        betMoney.setPadding(new Insets(0,0,5,5));
        betMoney.setMaxWidth(50);
        Label betLabel = new Label("BET");
        betLabel.setTextFill(Color.WHITE);
        betLabel.setPadding(new Insets(0,15,10,10));
        HBox userBets = new HBox();
        userBets.getChildren().addAll(betLabel, betMoney);
        userBets.setPadding(new Insets(5,0,5,10));

        //Displays the total amount of money that the user has
        Label totalMoney = new Label("MONEY: ");
        totalMoney.setTextFill(Color.WHITE);
        totalMoney.setPadding(new Insets(0,5,10,0));
        moneyLabel.setTextFill(Color.WHITE);
        HBox userMoney = new HBox();
        userMoney.getChildren().addAll(totalMoney,moneyLabel);
        userMoney.setPadding(new Insets(5,0,0,250));

        //Sets up the deal, hit and stand images
        Image play = new Image("/images/play.png");
        ImageView imagePlay = new ImageView(play);
        imagePlay.setFitHeight(50);
        imagePlay.setFitWidth(50);

        Image hit = new Image("/images/hit.png");
        ImageView imageHit = new ImageView(hit);
        imageHit.setFitHeight(50);
        imageHit.setFitWidth(50);

        Image stand = new Image("/images/stand.png");
        ImageView imageStand = new ImageView(stand);
        imageStand.setFitHeight(50);
        imageStand.setFitWidth(50);

        //Staggers the buttons, with the HBox only holding two buttons opposed to three
        HBox buttonsHBox = new HBox(15, imageHit, imageStand);
        buttonsHBox.setAlignment(Pos.CENTER);

        rightVBox.getChildren().addAll(imagePlay, buttonsHBox);

        exitButton.setStyle("-fx-background-color: black");
        exitButton.setTextFill(Color.WHITE);
        exitButton.setAlignment(Pos.TOP_RIGHT);
        HBox exitButtonPane = new HBox();
        exitButtonPane.getChildren().add(exitButton);
        exitButtonPane.setPadding(new Insets(0,0,0,530));
        //If user presses the exit button
        exitButton.setOnAction((event) -> {
            displayMessage();
        });

        //Adds all panes that were set up into the root
        rootLayout.getChildren().addAll(new StackPane(userBets, userMoney, exitButtonPane), new StackPane(upBG, leftVBox), new StackPane(downBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        //Binds the images to the booleans, controlling when they can actually
        //function. If the user presses them when they are not playable, the
        //effect will not be triggered
        imagePlay.disableProperty().bind(playable);
        imageHit.disableProperty().bind(playable.not());
        imageStand.disableProperty().bind(playable.not());

        //Sets up the images as buttons, if the mouse is clicked within the
        //vicinity of the image, the effect is triggered
        //Deal buttton setup
        imagePlay.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // the bet is set to the total amount of money in the bank
                exitButton.setDisable(true);
                if (betMoney.getText().equals("") == false) {
                  //Catches any non float values inputted
                  try{
                      //Sets the money to the max that the user has
                      if (Float.parseFloat(betMoney.getText()) > Float.parseFloat(moneyLabel.getText()))
                          betMoney.setText(moneyLabel.getText());
                        }
                        //If an improper number is inputted, changes the bet to a zero and proceeds
                        catch(NumberFormatException e){
                          betMoney.setText("0");
                        }
                      }
                        //Disables the use of adding new bets
                betMoney.setDisable(true);
                startNewGame();
                //Updates user and dealer scores
                playerScore.setText("Player: " + player.getValue());
                dealerScore.setText("Dealer: " + dealer.getValue());
              }
          });
        //Hit button setup
        imageHit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              //User takes a card. If user value is 21 or higher, ends the game
                player.takeCard(deck.drawCard());
                playerScore.setText("Player: " + player.getValue());
                if(player.getValue() >= 21){
                    endGame();
                }
            }
        });
        //Stand button
        imageStand.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
              //Pulls cards till Dealer goes over 16 (Can't draw anymore)
              //Reveals cards
                while (dealer.getValue() < 17) {
                    dealer.takeCard(deck.drawCard());
                    dealerScore.setText("Dealer: " + dealer.getValue());
                }

                endGame();
            }
        });

        return root;
    }
    /**
     * Sets up for a new game to be played
     */
    private void startNewGame() {
        playable.set(true);
        message.setText("");

        deck.refill();
        //Resets both user and dealer's hands
        dealer.reset();
        player.reset();
        //User and dealer each take two playerCards
        dealer.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        //If either user or dealer have 21, the game is ended, as there is
        //no point in proceeding
        if (player.getValue() == 21 || dealer.getValue() == 21)
            endGame();
    }
    /**
     * Sets up the connection to the betting server
     * @param w         String value used to communicate user with the server
     * @param blackjack Int value used to communicate with the server
     */
    public void connectServer(String w, int blackjack)
    {
        try {
          //Attempts to record data from the bet to the server
            float bet = Float.parseFloat(betMoney.getText());
            toServer.writeFloat(bet);
            toServer.writeUTF(w);
            toServer.writeInt(blackjack);

            toServer.flush();
            //Ajusts the moneylabel, and allows a new bet to be made
            float bank = fromServer.readFloat();
            moneyLabel.setText(Float.toString(bank));
            betMoney.setDisable(false);
        }
        catch(IOException e){
            System.err.print(e);
        }
    }
    /**
     * Process which deals with ending a game of blackjack
     */
    private void endGame() {
        playable.set(false); //As no bets can be made while the game is ending,
                            //user can not hit or stand

        int dealerValue = dealer.getValue();
        int playerValue = player.getValue();
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue;

        //Checks the winner. The dealer will win in an instance of a tie
        if (dealerValue == 21 || playerValue > 21 || dealerValue == playerValue
                || (dealerValue < 21 && dealerValue > playerValue)) {
            winner = "DEALER";
        }
        else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
            winner = "PLAYER";
        }
        message.setText(winner + " WON");

        // runs the server
        if (playerValue == 21 & betMoney.getText().isEmpty() == false)
            connectServer(winner,1);
        else if (playerValue != 21 & betMoney.getText().isEmpty() == false)
            connectServer(winner,0);

        betMoney.clear();

        // displays a message if the user bank is 0
        if (Float.parseFloat(moneyLabel.getText()) == 0.0f) {
            betMoney.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You've been kicked out of the game", ButtonType.OK);
            alert.showAndWait();
            //Removes user from game, as they can no longer bet
            if (alert.getResult() == ButtonType.OK)
                exit();
        }
        //Allows the user to bet money and exit the game
        else
            betMoney.setDisable(false);

        exitButton.setDisable(false);
    }
    /**
     * Displays message when the user wishes to exit the game
     */
    public void displayMessage()
    {
        // input for user details and put the amount of money in bets in a file
        Alert betsMessage = new Alert(Alert.AlertType.INFORMATION, "Do you want to save your bets?", ButtonType.OK, ButtonType.NO);
        betsMessage.showAndWait();
        if (betsMessage.getResult() == ButtonType.NO){
          exit(); //There is an exit function in both loops, as the code bugs out
                  //if thus doesn't occur
        }
        else
        {
            //If user wishes to save their game score, takes in name in order to save
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Enter Name");
            dialog.setHeaderText("Enter your name");
            dialog.setContentText("Name:");
            String name = dialog.showAndWait().toString();
            //As the dialog is stored in an optional string, optional[] needs to
            //be removed from the text
            name = name.substring(name.indexOf("[")+1,name.length()-1);
            //saves user data
            score.addAccount(name, moneyLabel.getText());
            exit();
        }
    }
    /**
     * Handles shutdown proccess of blackjack
     */
    public void exit()
    {
        //Attempts to close the server
        try {
            connectToServer.close();
            fromServer.close();
            toServer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //CLoses stage and runs the start screen
        server.closeServer();
        primaryStage.close();
        StartScreen startScreen = new StartScreen();
        startScreen.run();
    }
    /**
     * Sets up the background process of blackjack
     */
    public void Game(){
        try {
            server.start(primaryStage);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //Sets up the server
        try {
            connectToServer = new Socket("localhost", 8000);
            fromServer = new DataInputStream(connectToServer.getInputStream());
            toServer = new DataOutputStream(connectToServer.getOutputStream());
        }
        catch(IOException e){
            System.err.println(e);
        }
        //Creates the main stage
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setWidth(600);
        primaryStage.setHeight(800);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }

    @Override
    /**
     * Runs blackjack
     */
    public void run() {
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    Game();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    }
}
