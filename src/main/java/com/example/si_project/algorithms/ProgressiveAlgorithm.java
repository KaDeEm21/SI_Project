package com.example.si_project.algorithms;

import com.example.si_project.interfaces.IProgressive;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProgressiveAlgorithm implements IProgressive {

//    private List<String> factSet = new ArrayList<>();   //zbiór faktów
    private Set<String> factSet = new HashSet<>();   //zbiór faktów
    private Rules rules = new Rules();  //zbiór reguł


    @Override
    public boolean loadKnowledgeBaseSet(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                rules.addRule(line);
            }
            return true;
        } catch (IOException e) {
            // Obsługa błędu w przypadku problemów z plikiem
            showErrorAlert("Nie można wczytać pliku " + filename);
            return false;
        }
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd podczas wczytywania pliku");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public boolean loadFactSet(String filename) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            Collections.addAll(factSet, line.split(","));
            return true;
        } catch (IOException e) {
            // Obsługa błędu w przypadku problemów z plikiem
            showErrorAlert("Nie można wczytać pliku " + filename);
            return false;
        }
    }

    @Override
    public void eraseFactSet() {
        factSet.clear();
    }

    @Override
    public void eraseKnowledgeBase() {
        rules.getRules().clear();
    }

    @Override
    public List<String> returnCurrentFactSet() {
        return List.copyOf(factSet);
    }

    @Override
    public List<String> execute() {
        Map<String, Set<String>> s = new HashMap<>();
        Map<String, Set<String>> a = new HashMap<>();

        //inicjalizacja zbioru S
        rules.getRules().forEach((head, tail) -> {
            if (tail.stream().allMatch(x -> factSet.contains(x))) {
                s.put(head, tail);
            }
        });


        while (!s.isEmpty()) {
            //aktywowanie reguły
            Map.Entry<String, Set<String>> activeRule = s.entrySet().iterator().next();
            //usunięcie aktywnej reguły z zbioru S
            s.remove(activeRule.getKey());
            //dodanie konkluzji (key) do zbioru faktów
            factSet.add(activeRule.getKey());
            //dodanie aktywnej reguły do zbioru A (reguły aktywowane)
            a.put(activeRule.getKey(), activeRule.getValue());

            //aktualizacja zbioru S o nowe pasujące reguły
            rules.getRules().forEach((head, tail) -> {
                //sprawdzenie czy reguła pasuje do zbioru faktów
                if (tail.stream().allMatch(x -> factSet.contains(x)
                        //sprawdzenie czy reguła została już użyta (zawiera się w A)
                        && !a.containsKey(head))
                        && !factSet.contains(head)) {
                    s.put(head, tail);
                }


            });

        }

        return this.factSet.stream().toList();
    }
}
