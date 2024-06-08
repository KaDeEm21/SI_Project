package com.example.si_project.algorithms;

import com.example.si_project.interfaces.IRegressive;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class RegressiveAlgorithm implements IRegressive {
    private Set<String> factSet = new HashSet<>(); //zbiór faktów
    Rules rules = new Rules();


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
            e.printStackTrace();
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
            factSet.addAll(Arrays.asList(line.split(",")));
            return true;
        } catch (IOException e) {
            // Obsługa błędu w przypadku problemów z plikiem
            showErrorAlert("Nie można wczytać pliku " + filename);
            e.printStackTrace();
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
    public boolean execute(String goal) {
        // Inicjalizacja zbioru s
        Set<String> s = rules.getRules().keySet().stream()
                .filter(strings -> strings.equals(goal))
                .collect(Collectors.toSet());

        // jeśli cel znajduje się już w zbiorze faktów zwracamy prawdę
        if (factSet.contains(goal)) return true;

        boolean premiseIsTrue = false;

        // główna pętla
        while (!premiseIsTrue && !s.isEmpty()) {
            // wybieramy regułę ze zbioru s
            String activeRule = s.iterator().next();
            if(rules.getRules().get(activeRule).isEmpty()) {
                premiseIsTrue = true;
                break;
            }
            // premises - przesłanki aktywnej reguły
            Set<String> premises = rules.getRules().get(activeRule);

            // w pętli for sprawdzamy każdą z przesłanek czy zawiera się w zbiorze faktów
            for (String w : premises) {
                premiseIsTrue = factSet.contains(w);
                if (!premiseIsTrue) {
                    premiseIsTrue = execute(w);
                    if (!premiseIsTrue) break;
                }
            }

            // Jeśli przesłanka nie jest prawdziwa, usuwamy aktywną regułę ze zbioru S
            if (!premiseIsTrue) {
                s.remove(activeRule);
            }
        }


        // Jeśli wszystkie przesłanki się zgadzają dodajemy cel do zbioru faktów
        if (premiseIsTrue) {
            factSet.add(goal);
        }

        return premiseIsTrue;
    }



    @Override
    public List<String> returnHeadsOfRulesSet() {

        return new ArrayList<>(rules.getRules().keySet());
    }
}
