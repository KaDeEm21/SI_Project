package com.example.si_project.algorithms;

import com.example.si_project.interfaces.IProgressive;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProgressiveAlgorithm implements IProgressive {

    private Set<String> factSet = new HashSet<>();   //zbiór faktów

    private Set<Rule> rules = new HashSet<>();  //zbiór reguł


    @Override
    public boolean loadKnowledgeBaseSet(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                rules.add(new Rule(line));
            }
            return true;
        } catch (IOException e) {
            // Obsługa błędu w przypadku problemów z plikiem
            e.printStackTrace();
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
            e.printStackTrace();
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
        rules.clear();
    }

    @Override
    public List<String> returnCurrentFactSet() {
        return List.copyOf(factSet);
    }

    @Override
    public List<String> execute() {
        Set<Rule> s = new HashSet<>();
        Set<Rule> a = new HashSet<>();

        //inicjalizacja zbioru S
        rules.forEach(rule->{
            if(rule.getTail().stream().allMatch(x->factSet.contains(x))){
                s.add(rule);
            }
        });


        while (!s.isEmpty()) {
            //aktywowanie reguły
            Rule activeRule = s.stream().iterator().next();
            //usunięcie aktywnej reguły z zbioru S
            s.remove(activeRule);
            //dodanie konkluzji (key) do zbioru faktów
            factSet.add(activeRule.getHead());
            //dodanie aktywnej reguły do zbioru A (reguły aktywowane)
            a.add(activeRule);

            //aktualizacja zbioru S o nowe pasujące reguły
            rules.forEach(rule ->{
                //sprawdzenie czy reguła pasuje do zbioru faktów
                if (rule.getTail().stream().allMatch(x -> factSet.contains(x))
                        //sprawdzenie czy reguła została już użyta (zawiera się w A)
                        &&!a.contains(rule)
                        &&!factSet.contains(rule.getHead())) {
                    s.add(rule);
                }
            });


        }

        return this.factSet.stream().toList();
    }
}
