package com.example.si_project.algorithms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule {
    private String head;
    private Set<String> tail = new HashSet<>();

    public Rule(String head, Set<String> tail) {
        this.head = head;
        this.tail = tail;
    }

    public Rule(String l){
        String line = l.toLowerCase();
        if (line.contains("<-")){
        String[] parts = line.split("<-");
        this.head = parts[0].trim();

            this.tail.addAll(Arrays.asList(parts[1].trim().split(",")));
        }else {
            this.head = line;
            this.tail = new HashSet<>();
        }
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Set<String> getTail() {
        return tail;
    }

    public void setTail(Set<String> tail) {
        this.tail = tail;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(head).append("<-");
        for (String element : tail) {
            stringBuilder.append(element).append(",");
        }
        // Usuń ostatni przecinek, jeśli istnieje
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;
        return this.getHead().equals(rule.getHead()) && this.getTail().equals(rule.getTail());
    }
}
