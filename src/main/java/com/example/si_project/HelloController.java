package com.example.si_project;

import com.example.si_project.algorithms.ProgressiveAlgorithm;
import com.example.si_project.algorithms.RegressiveAlgorithm;
import com.example.si_project.algorithms.Rules;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class HelloController {
    @FXML
    public TextArea resultsTextArea = new TextArea();

    @FXML
    private TextField factsPathTextField = new TextField();

    @FXML
    private TextField rulesPathTextField = new TextField();

    @FXML
    public ChoiceBox choiceBox = new ChoiceBox();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    private void handleOnGenerateRulesClick(ActionEvent event) {
        resultsTextArea.clear();
        ProgressiveAlgorithm pr = new ProgressiveAlgorithm();
        pr.loadKnowledgeBaseSet(rulesPathTextField.getText());
        pr.loadFactSet(factsPathTextField.getText());
        List<String> results = pr.execute();
        resultsTextArea.appendText("Zaktualizowana lista faktów:\n");
        results.forEach(x -> resultsTextArea.appendText(x + "\n"));
    }

    @FXML
    private void handleOnCheckGoal(ActionEvent event) {

        RegressiveAlgorithm re = new RegressiveAlgorithm();
        re.loadKnowledgeBaseSet(rulesPathTextField.getText());
        re.loadFactSet(factsPathTextField.getText());
        resultsTextArea.clear();
        boolean result = re.execute(choiceBox.getValue().toString());
        if(result) {
            resultsTextArea.appendText("\""+choiceBox.getValue().toString() + "\" jest spełnione :D");
        }else{
            resultsTextArea.appendText("\""+choiceBox.getValue().toString() + "\" nie jest spełnione :(");
        }

    }

    @FXML
    private void handleLoadFacts(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj fakty");

        // Ustawienie filtrów plików
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"),
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")
        );

        // Wyświetlenie okna dialogowego wyboru pliku
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            factsPathTextField.setText(selectedFile.getAbsolutePath());
            System.out.println("Wybrano plik: " + selectedFile.getAbsolutePath());
        } else {
            System.out.println("Nie wybrano pliku.");
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleLoadRules(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj reguły");

        // Ustawienie filtrów plików
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"),
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")
        );

        // Wyświetlenie okna dialogowego wyboru pliku
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            rulesPathTextField.setText(selectedFile.getAbsolutePath());
            System.out.println("Wybrano plik: " + selectedFile.getAbsolutePath());
            System.out.println(rulesPathTextField.getText());

            Rules r = new Rules();
            try {
                Scanner scanner = new Scanner(selectedFile);
                while (scanner.hasNext()){
                    r.addRule(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

//TODO: check
//            ObservableList<String> items = FXCollections.observableArrayList(r.getRules().);
//            choiceBox.setItems(items);

        } else {
            System.out.println("Nie wybrano pliku.");
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleAboutKarol(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacje o autorze");
        alert.setHeaderText(null);
        alert.setContentText("Karol Michalak\nInformatyka I - 2 rok\nEmail: kadeem393@gmail.com\nNr indeksu: 160095");
        alert.showAndWait();
    }

    @FXML
    private void handleAboutPrzemek(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacje o autorze");
        alert.setHeaderText(null);
        alert.setContentText("Przemek Naja");
        alert.showAndWait();
    }
}