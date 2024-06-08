package com.example.si_project.algorithms;

import java.util.List;

public class consoleTest {
    public static void main(String[] args) {
//        ProgressiveAlgorithm pr = new ProgressiveAlgorithm();
//        pr.loadKnowledgeBaseSet("C:\\Users\\kadeem\\Desktop\\G\\rules.txt");
//        pr.loadFactSet("C:\\Users\\kadeem\\Desktop\\G\\facts.txt");
//        List<String> results = pr.execute();
//        results.forEach(System.out::println);

        RegressiveAlgorithm re = new RegressiveAlgorithm();
        re.loadKnowledgeBaseSet("C:\\Users\\kadeem\\Desktop\\G\\rules.txt");
        re.loadFactSet("C:\\Users\\kadeem\\Desktop\\G\\facts.txt");
        System.out.println("a: "+re.execute("x"));
        System.out.println("b: "+re.execute("b"));
        System.out.println("c: "+re.execute("c"));
        System.out.println("d: "+re.execute("d"));
        System.out.println("p: "+re.execute("p"));
        System.out.println("q: "+re.execute("q"));
        System.out.println("h: "+re.execute("h"));
        System.out.println("x: "+re.execute("x"));
        System.out.println("n: "+re.execute("n"));
        System.out.println("f: "+re.execute("f"));

    }
}
