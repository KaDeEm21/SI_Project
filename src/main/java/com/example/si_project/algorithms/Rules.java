package com.example.si_project.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Rules {
    private Map<String, Set<String>> rules;

    public Rules() {
        rules = new HashMap<>();
    }

    public void addRule(String head, Set<String> tail) {
        rules.put(head, tail);
    }

    public void addRule(String rule) {
        if (rule.contains("<-")) {
            String[] split = rule.split("<-");
            String head = split[0];
            String[] tail = split[1].split(",");
            Set<String> tailSet = new HashSet<>();

            for (String condition : tail) {
                tailSet.add(condition);
            }

            rules.put(head, tailSet);
        } else {
            rules.put(rule, new HashSet<>());
        }
    }

    public Set<String> gettail(String head) {
        return rules.getOrDefault(head, new HashSet<>());
    }


    // Metoda zwracająca reguły
    public Map<String, Set<String>> getRules() {
        return rules;
    }
}
