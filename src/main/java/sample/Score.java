package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class Score extends Application {
    Stage primaryStage = new Stage();
    GridPane addPane = new GridPane();
    HashMap<String, String> accounts = new HashMap<>();

    public Score(){
      getCsv();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
    }

    private void getCsv(){
        String row = "";
        try {
            //FileReader file = new FileReader("src/data/moneyBank.csv");
            BufferedReader csvReader = new BufferedReader(new FileReader("src/main/java/sample/moneyBank.csv"));
            while ((row = csvReader.readLine())!=null){
                String[] temp = row.split(",");
                accounts.put(temp[0], temp[1]);
            }
            csvReader.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void updateCSV(){
        try {
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter("src/main/java/sample/moneyBank.csv"));

            for(Map.Entry<String, String> entry : accounts.entrySet()){
                csvWriter.write( entry.getKey() + "," + entry.getValue() );

                csvWriter.newLine();
            }
            csvWriter.close();
            //System.out.println(accounts.toString());

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // private void btnS(){
    //     Button a = new Button("Add account");
    //     Button b = new Button("Check Balance");
    //     addPane.add(a, 0, 1);
    //     a.setOnAction(new EventHandler<ActionEvent>() {
    //         @Override
    //         public void handle(ActionEvent event) {
    //             addAccount();
    //         }
    //     });
    //
    //     addPane.add(b, 0, 2);
    //     b.setOnAction(new EventHandler<ActionEvent>() {
    //         @Override
    //         public void handle(ActionEvent event) {
    //             checkBal();
    //         }
    //     });
    // }

    public void addAccount(String name, String value){
      accounts.put(name, value);
      updateCSV();
    }

    public void checkBal(){
      Stage primaryStage = new Stage();
        Label nameLbl = new Label("Name: ");
        TextField nameFld = new TextField();
        Label balanceLbl = new Label("Balance: ");
        TextField balanceFld = new TextField();
        balanceFld.setStyle("-fx-control-inner-background: lightgrey;");
        balanceFld.setFocusTraversable(false);
        balanceFld.setMouseTransparent(true);
        balanceFld.setEditable(false);
        Button addBtn = new Button("Check");

        addPane.add(nameLbl, 0, 0);
        addPane.add(nameFld, 1, 0);
        addPane.add(balanceLbl, 2, 0);
        addPane.add(balanceFld, 3, 0);
        addPane.add(addBtn, 1, 3);
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                balanceFld.clear();
                String Name = nameFld.getText();
                String value = accounts.get(Name);
                balanceFld.setText(value);
            }
        });
        Scene scene = new Scene(addPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
