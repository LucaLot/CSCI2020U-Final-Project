/**
 * Date: March 24, 2020
 * File Name: Score.java
 * Purpose: The class stores user scores in a CSV file, and allows for the
 * retrival of such files in a sorted table
 */
package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.*;

public class Score{
  //Creates Hasmap for holding variables from text document
    HashMap<String, String> accounts = new HashMap<>();
    public Score(){
      getCsv(); //Sets up hashmap
    }
    /**
     * Function which sets up the hasmap for use of other portions of the class
     */
    private void getCsv(){
        String row = "";
        //Calls the buffered reader, attempts to read file
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("src/main/java/sample/moneyBank.csv"));
            while ((row = csvReader.readLine())!=null){
                String[] temp = row.split(",");
                accounts.put(temp[0], temp[1]); //Places name and score into hashmap
            }
            csvReader.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Updates the CSV with any new changes, such as adding a new score
     */
    private void updateCSV(){
        try {
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter("src/main/java/sample/moneyBank.csv"));
            //Splits into proper CSV format with a comma
            for(Map.Entry<String, String> entry : accounts.entrySet()){
                csvWriter.write( entry.getKey() + "," + entry.getValue() );

                csvWriter.newLine();
            }
            csvWriter.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    /**
     * Allows a new user name to be entered, sets up for it to be added to txt file
     * @param name  Player name
     * @param value Player Score
     */
    public void addAccount(String name, String value){
      accounts.put(name, value); //Updates hashmap
      updateCSV();
    }
    /**
     * Checks the balance of all entered accounts, and outputs the name and
     * score of all, highest to lowest
     */
    public void checkBal(){
      Stage primaryStage = new Stage();
      Scene scene = new Scene(new VBox(),400,250);
      //Creates a table column to hold values
      TableView table = new TableView<>();
      //Observable list helps set up the table
      ObservableList<ContainScore> high = FXCollections.observableArrayList();
      //Array list contains the values held by the map, so they can be inserted
      //into the Observable List
      ArrayList<String> nameList = new ArrayList<String>(accounts.keySet());
      ArrayList<String> scoreList = new ArrayList<String>(accounts.values());
        for (int i=0;i<accounts.size();i++){
          high.add(new ContainScore(nameList.get(i),scoreList.get(i)));
        }
        //Creates new table columns
        TableColumn<ContainScore,String> name = new TableColumn("Name");
        TableColumn<ContainScore,Float> score = new TableColumn("Score");
        //Creates setCellValueFactories to allow the table columns to be updated
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setMinWidth(200);
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        score.setMinWidth(200);

        table.setItems(high);
        table.getColumns().addAll(name,score);
        //Sorts the score column from order of highest to lowest
        score.sortTypeProperty().set(javafx.scene.control.TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(score);

        Button addBtn = new Button("Return");
        GridPane buttonPane = new GridPane();
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.add(addBtn, 1,5);
        //Adds button action, which returns to the start menu
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              primaryStage.close();
              StartScreen startScreen = new StartScreen();
              startScreen.run();
            }
        });
        ((VBox) scene.getRoot()).getChildren().addAll(table,buttonPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
