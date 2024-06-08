package com.example.si_project.algorithms;

import java.util.*;

public class Rules {
    private Set<Rule> rules;

    public Rules() {
        rules = new HashSet<>();
    }

    public void addRule(String head, Set<String> tail) {
        rules.add(new Rule(head, tail));

    }

    public void addRule(String rule) {

        if (rule.contains("<-")) {
            String[] split = rule.split("<-");
            String head = split[0];
            String[] tail = split[1].split(",");
            Set<String> tailSet = new HashSet<>();

            for (String condition : tail) {
                tailSet.add(condition.trim()); // Dodaj trim() aby usunąć nadmiarowe spacje
            }

            rules.add(new Rule(head, tailSet));
        } else {
            rules.add(new Rule(rule, new HashSet<>()));
        }
    }

//    public Set<String> getTail(String head) {
//        rules.
//        return rules.getOrDefault(head, new HashSet<>());
//    }

    // Metoda zwracająca reguły
    public Set<Rule> getRules() {
        return rules;
    }

    public String get(Rule rule) {
        return rule.getHead().toString();
    }

    public Set<String> getHeads() {
        Set<String> heads = new HashSet<String>();
        for (Rule rule : rules) {
            heads.add(rule.getHead());
        }
        return heads;
    }

    public void clearRules() {
        rules.clear();
    }
}
