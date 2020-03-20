package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import javafx.scene.control.Label;

/**
 * Game's logic and UI
 *
 */
public class BlackjackApp extends Application implements Runnable{

    private Deck deck = new Deck();
    private Hand dealer, player;
    private Text message = new Text();

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(20);
    private HBox playerCards = new HBox(20);

    private Parent createContent() {
        message.setFill(Color.WHITE);
        dealer = new Hand(dealerCards.getChildren());
        player = new Hand(playerCards.getChildren());

        Pane root = new Pane();
        root.setPrefSize(600, 800);

        Region background = new Region();
        background.setPrefSize(600, 800);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        VBox rootLayout = new VBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        Rectangle upBG = new Rectangle(550, 560);
        Image img = new Image("/images/table.png");
        upBG.setFill(new ImagePattern(img));
        Rectangle downBG = new Rectangle(560, 170);
        Image img2 = new Image("/images/menu.png");
        downBG.setFill(new ImagePattern(img2));

        // UP
        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text dealerScore = new Text("Dealer: " + dealer.getValue());
        dealerScore.setFill(Color.WHITE);
        Text playerScore = new Text("Player: " + player.getValue());
        playerScore.setFill(Color.WHITE);

        leftVBox.getChildren().addAll(dealerScore, dealerCards, message, playerCards, playerScore);

        // Down

        VBox rightVBox = new VBox(2);
        rightVBox.setAlignment(Pos.CENTER);

        TextField bet = new TextField("BET");
        bet.setDisable(true);
        bet.setMaxWidth(50);
        Text money = new Text("MONEY");



        Image play = new Image("/images/play.png");
        ImageView imagePlay = new ImageView(play);
        imagePlay.setFitHeight(50);
        imagePlay.setFitWidth(50);
        //Button btnPlay=new Button("",imagePlay);

        Image hit = new Image("/images/hit.png");
        ImageView imageHit = new ImageView(hit);
        imageHit.setFitHeight(50);
        imageHit.setFitWidth(50);
        //Button btnHit = new Button("",imageHit);

        Image stand = new Image("/images/stand.png");
        ImageView imageStand = new ImageView(stand);
        imageStand.setFitHeight(50);
        imageStand.setFitWidth(50);
        //Button btnStand = new Button("",imageStand);

        HBox buttonsHBox = new HBox(15, imageHit, imageStand);
        buttonsHBox.setAlignment(Pos.CENTER);

        rightVBox.getChildren().addAll(imagePlay, money, buttonsHBox);

        // ADD BOTH STACKS TO ROOT LAYOUT

        rootLayout.getChildren().addAll(new StackPane(upBG, leftVBox), new StackPane(downBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        // BIND PROPERTIES

        imagePlay.disableProperty().bind(playable);
        imageHit.disableProperty().bind(playable.not());
        imageStand.disableProperty().bind(playable.not());


        // INIT BUTTONS

        imagePlay.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
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
		//Continues till the dealer can no longer draw
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
    }


    public void Game(){
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setWidth(600);
        primaryStage.setHeight(800);
        //primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
}
