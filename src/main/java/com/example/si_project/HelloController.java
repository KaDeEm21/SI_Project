package com.example.si_project;

import com.example.si_project.algorithms.ProgressiveAlgorithm;
import com.example.si_project.algorithms.RegressiveAlgorithm;
import com.example.si_project.algorithms.Rule;
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
import java.util.*;
import java.util.stream.Collectors;

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
        if (result) {
            resultsTextArea.appendText("\"" + choiceBox.getValue().toString() + "\" jest spełnione :D");
        } else {
            resultsTextArea.appendText("\"" + choiceBox.getValue().toString() + "\" nie jest spełnione :(");
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

            Set<Rule> r = new HashSet<>();
            try {
                Scanner scanner = new Scanner(selectedFile);
                while (scanner.hasNext()) {
                    r.add(new Rule(scanner.nextLine()));
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            ObservableList<String> items = FXCollections.observableArrayList(r.stream().map(Rule::getHead).collect(Collectors.toSet()));
            choiceBox.setItems(items);

        } else {
            System.out.println("Nie wybrano pliku.");
        }
    }

    @FXML
    private void handleLoadCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj plik CSV");


        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki CSV", "*.csv"),
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*")
        );


        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {

            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                 FileWriter rulesWriter = new FileWriter("rulesFromCsv.txt")) {

                List<String> linesCsv = reader.lines().skip(2).toList();

                //csv file structure
                //0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22
                //3-21 tail
                //1 - head

                linesCsv.stream().forEach(line -> {
                    String head = line.split(";", -1)[1];
                    List<String> tail = new ArrayList<>();
                    for (int i = 3; i < 22; i++) {
                        if (!line.split(";", -1)[i].isEmpty()) {
                            tail.add(line.split(";", -1)[i]);
                        }
                    }
                    try {
                        if (head != null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(head);
                            sb.append("<-");
                            for (String condition : tail) {
                                if(condition.contains(",")){
                                    condition.replace(",","");
                                }
                                sb.append(condition);
                                sb.append(",");
                            }
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append("\n");
                            rulesWriter.write(sb.toString());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });


                rulesWriter.close();

                // Ustawienie ścieżki do pliku w polu tekstowym
                String rulesFilePath = new File("rulesFromCsv.txt").getAbsolutePath();
                rulesPathTextField.setText(rulesFilePath);
                System.out.println("Wczytano plik: " + selectedFile.getAbsolutePath());
                System.out.println("Ścieżka do zapisanego pliku: " + rulesFilePath);


                try (BufferedReader fileReader = new BufferedReader(new FileReader("rulesFromCsv.txt"))) {
                    Set<Rule> r = fileReader.lines()
                            .map(Rule::new) // Zakładając, że konstruktor Rule przyjmuje String jako parametr
                            .collect(Collectors.toSet());

                    ObservableList<String> items = FXCollections.observableArrayList(r.stream().map(Rule::getHead).collect(Collectors.toSet()));
                    choiceBox.setItems(items);

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
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