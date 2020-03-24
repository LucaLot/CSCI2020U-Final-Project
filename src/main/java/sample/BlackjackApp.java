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
import java.net.Socket;

public class BlackjackApp extends Application implements Runnable{

    private Deck deck = new Deck();
    private BetServer server = new BetServer();
    private Socket connectToServer;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private Hand dealer, player;
    private Stage primaryStage = new Stage();
    private Text message = new Text();
    private Button exitButton = new Button("EXIT");

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox playerCards = new HBox(20);
    private TextField betMoney = new TextField();
    private Label moneyLabel = new Label("300");

    private Parent createContent() {
        message.setFill(Color.WHITE);
        dealer = new Hand(dealerCards.getChildren());
        player = new Hand(playerCards.getChildren());

        // window
        Pane root = new Pane();
        root.setPrefSize(590, 800);

        Region background = new Region();
        background.setPrefSize(600, 800);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        VBox rootLayout = new VBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        // table
        Rectangle upBG = new Rectangle(590, 560);
        Image img = new Image("/images/table.png");
        upBG.setFill(new ImagePattern(img));
        // menu
        Rectangle downBG = new Rectangle(560, 170);
        Image img2 = new Image("/images/menu.png");
        downBG.setFill(new ImagePattern(img2));

        // UP
        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment(Pos.TOP_CENTER);
        leftVBox.setPadding(new Insets(5,10,5,5));

        Text dealerScore = new Text("Dealer: ");
        dealerScore.setFill(Color.WHITE);
        Text playerScore = new Text("Player: ");
        playerScore.setFill(Color.WHITE);

        leftVBox.getChildren().addAll(dealerScore, dealerCards, message, playerCards, playerScore);

        // Down
        VBox rightVBox = new VBox(2);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.setPadding(new Insets(5,5,0,40));

        // user bets
        betMoney.setStyle("-fx-background-color: white");
        betMoney.setPadding(new Insets(0,0,5,5));
        betMoney.setMaxWidth(50);
        Label betLabel = new Label("BET");
        betLabel.setTextFill(Color.WHITE);
        betLabel.setPadding(new Insets(0,15,10,10));
        HBox userBets = new HBox();
        userBets.getChildren().addAll(betLabel, betMoney);
        userBets.setPadding(new Insets(5,0,5,10));

        // total money in the bank
        Label totalMoney = new Label("MONEY: ");
        totalMoney.setTextFill(Color.WHITE);
        totalMoney.setPadding(new Insets(0,5,10,0));
        moneyLabel.setTextFill(Color.WHITE);
        HBox userMoney = new HBox();
        userMoney.getChildren().addAll(totalMoney,moneyLabel);
        userMoney.setPadding(new Insets(5,0,0,250));


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

        HBox buttonsHBox = new HBox(15, imageHit, imageStand);
        buttonsHBox.setAlignment(Pos.CENTER);

        rightVBox.getChildren().addAll(imagePlay, buttonsHBox);

        exitButton.setStyle("-fx-background-color: black");
        exitButton.setTextFill(Color.WHITE);
        exitButton.setAlignment(Pos.TOP_RIGHT);
        HBox exitButtonPane = new HBox();
        exitButtonPane.getChildren().add(exitButton);
        exitButtonPane.setPadding(new Insets(0,0,0,530));

        exitButton.setOnAction((event) -> {
            displayMessage();
        });

        // ADD BOTH STACKS TO ROOT LAYOUT

        rootLayout.getChildren().addAll(new StackPane(userBets, userMoney, exitButtonPane), new StackPane(upBG, leftVBox), new StackPane(downBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        // BIND PROPERTIES

        imagePlay.disableProperty().bind(playable);
        imageHit.disableProperty().bind(playable.not());
        imageStand.disableProperty().bind(playable.not());
        // INIT BUTTONS

        imagePlay.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // the bet is set to the total amount of money in the bank
                if (betMoney.getText().equals("") == false) {
                    if (Float.parseFloat(betMoney.getText()) > Float.parseFloat(moneyLabel.getText()))
                        betMoney.setText(moneyLabel.getText());
                }

                betMoney.setDisable(true);
                startNewGame();
                //Updates user and dealer scores
                playerScore.setText("Player: " + player.getValue());
                dealerScore.setText("Dealer: " + dealer.getValue());
            }
        });

        imageHit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                player.takeCard(deck.drawCard());
                playerScore.setText("Player: " + player.getValue());
                if(player.getValue() >= 21){
                    endGame();
                }
            }
        });

        imageStand.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                while (dealer.getValue() < 17) {
                    dealer.takeCard(deck.drawCard());
                    dealerScore.setText("Dealer: " + dealer.getValue());
                }

                endGame();
            }
        });

        return root;
    }

    private void startNewGame() {
        playable.set(true);
        message.setText("");

        deck.refill();

        dealer.reset();
        player.reset();

        dealer.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
    }

    public void connectServer(String w, int blackjack)
    {
        try {
            float bet = Float.parseFloat(betMoney.getText());
            toServer.writeFloat(bet);
            toServer.writeUTF(w);
            toServer.writeInt(blackjack);

            toServer.flush();

            float bank = fromServer.readFloat();
            moneyLabel.setText(Float.toString(bank));
            betMoney.setDisable(false);
        }
        catch(IOException e){
            System.err.print(e);
        }
    }

    private void endGame() {
        playable.set(false);

        int dealerValue = dealer.getValue();
        int playerValue = player.getValue();
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue;

        // the order of checking is important
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

            if (alert.getResult() == ButtonType.OK)
                exit();
        }
        else
            betMoney.setDisable(false);
    }

    public void displayMessage()
    {
        // input for user details and put the amount of money in bets in a file
        Alert betsMessage = new Alert(Alert.AlertType.INFORMATION, "Do you want to save your bets?", ButtonType.OK, ButtonType.NO);
        betsMessage.showAndWait();
        if (betsMessage.getResult() == ButtonType.NO)
            Platform.exit();
        else
            primaryStage.close(); //TODO Implement code to save user scores

    }

    public void exit()
    {
        try {
            connectToServer.close();
            fromServer.close();
            toServer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        server.closeServer();
        primaryStage.close();
        StartScreen startScreen = new StartScreen();
        startScreen.run();
    }

    public void Game(){
        try {
            server.start(primaryStage);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            connectToServer = new Socket("localhost", 8000);
            fromServer = new DataInputStream(connectToServer.getInputStream());
            toServer = new DataOutputStream(connectToServer.getOutputStream());
        }
        catch(IOException e){
            System.err.println(e);
        }

        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setWidth(600);
        primaryStage.setHeight(800);
        //primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }

    @Override
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

    public static void main(String[] args) {
        launch(args);
    }

}
