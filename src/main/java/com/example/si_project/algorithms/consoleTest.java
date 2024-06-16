package com.example.si_project.algorithms;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class consoleTest {
    public static void main(String[] args) {
//        ProgressiveAlgorithm pr = new ProgressiveAlgorithm();
//        pr.loadKnowledgeBaseSet("C:\\Users\\kadeem\\Desktop\\G\\rules.txt");
//        pr.loadFactSet("C:\\Users\\kadeem\\Desktop\\G\\facts.txt");
//        List<String> results = pr.execute();
//        results.forEach(System.out::println);

//        RegressiveAlgorithm re = new RegressiveAlgorithm();
//        re.loadKnowledgeBaseSet("C:\\Users\\kadeem\\Desktop\\G\\rules.txt");
//        re.loadFactSet("C:\\Users\\kadeem\\Desktop\\G\\facts.txt");
//        System.out.println("a: "+re.execute("x"));
//        System.out.println("b: "+re.execute("b"));
//        System.out.println("c: "+re.execute("c"));
//        System.out.println("d: "+re.execute("d"));
//        System.out.println("p: "+re.execute("p"));
//        System.out.println("q: "+re.execute("q"));
//        System.out.println("h: "+re.execute("h"));
//        System.out.println("x: "+re.execute("x"));
//        System.out.println("n: "+re.execute("n"));
//        System.out.println("f: "+re.execute("f"));

        loadFromCSV();

    }

    public static void loadFromCSV() {

        File file = new File("C:\\Users\\kadeem\\Desktop\\data_recipes.csv");

        if (file != null) {

            try (BufferedReader reader = new BufferedReader(new FileReader(file));
                 FileWriter rulesWriter = new FileWriter("rulesFromCsv.txt")) {

                List<String> linesCsv = reader.lines().skip(2).toList();

                //csv file structure
                //0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22
                //3-21 tail
                //1 - head

                linesCsv.stream().forEach(line -> {
                    String head = line.split(";", -1)[1];
                    Set<String> tail = new HashSet<>();
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

                System.out.println();


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}

