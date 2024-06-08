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
    private Set<Rule> rules = new HashSet<>();

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
            factSet.addAll(Arrays.asList(line.split(",")));
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
        rules.clear();
    }

    @Override
    public List<String> returnCurrentFactSet() {
        return new ArrayList<>(factSet);
    }

    @Override
    public boolean execute(String goal) {
        // Inicjalizacja zbioru s
        Set<Rule> s = rules
                .stream()
                .filter(rule -> rule.getHead().equals(goal))
                .collect(Collectors.toSet());

        // jeśli cel znajduje się już w zbiorze faktów zwracamy prawdę
        if (factSet.contains(goal)) return true;

        boolean premiseIsTrue = false;

        // główna pętla
        while (!premiseIsTrue && !s.isEmpty()) {
            // wybieramy regułę ze zbioru s
            Object activeRule = s.iterator().next();
            Set<String> tail = rules.stream().filter(rule -> rule.equals(activeRule)).findFirst().get().getTail();

            if (tail.isEmpty()) {
                premiseIsTrue = true;
                break;
            }

            // przesłanki - tail aktywnej reguły
            Set<String> premises = tail;

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
        return rules
                .stream()
                .map(x->String.valueOf(x.getHead()))
                .collect(Collectors.toList());
    }
}
