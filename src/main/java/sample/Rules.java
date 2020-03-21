import javafx.scene.paint.ImagePattern;
import javafx.application.Application;
import java.io.FileNotFoundException;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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

public class Rules extends Application {
    GridPane root = new GridPane();
    HBox cards = new HBox();
    VBox Rules = new VBox();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // creates the background
        Image img = new Image(new FileInputStream("src/image/table.png"));
        Rectangle upBG = new Rectangle(520, 640);
        upBG.setFill(new ImagePattern(img));

        // displays to playing cards in the top-right corner
        ImageView imageJack1 = new ImageView(new Image(new FileInputStream("src/Cards/11.png")));
        ImageView imageJack2 = new ImageView(new Image(new FileInputStream("src/Cards/50.png")));
        imageJack1.setRotate(imageJack1.getRotate() - 10);
        imageJack2.setRotate(imageJack2.getRotate() + 10);
        cards.setAlignment(Pos.TOP_RIGHT);
        cards.getChildren().addAll(imageJack1, imageJack2);


        root.getChildren().addAll(upBG, getRules(), cards);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rules");
        primaryStage.show();
    }

    // Function: access a txt file and displays the text in a vBox
    private VBox getRules() {
        File file = new File("src/text/Rules.txt");
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
}