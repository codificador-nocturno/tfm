/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.markov;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import tfm.model.nrt.Operations;
import tfm.model.pcst.PCSet;

/**
 *
 * @author william
 */
public class Matrix implements Serializable {

    private Double[][] probMatrix;
    private String[] from;
    private String[] to;
    private int size;
    private String name;

    private DecimalFormat df;

    private static final long serialVersionUID = 1;

    public Matrix(String name) {
        df = new DecimalFormat("#.##");
        this.name = name;
    }

    public void loadClasses(List<String> operations) {
        size = 12;
        from = new String[size];
        to = new String[size];

        for (int i = 0; i < size; i++) {
            from[i] = "[" + i + "]";
            to[i] = "[" + i + "]";
        }

        probMatrix = init(0.0, size);
        countTransitions(operations);
        calcProbability();
    }

    public void loadSingleOp(List<String> operations) {
        size = 4;
        from = new String[size];
        to = new String[size];

        Character[] ops = Operations.opArray;
        int i = 0;
        for (Character op : ops) {
            from[i] = op.toString();
            to[i] = op.toString();
            i++;
        }

        probMatrix = init(0.0, size);
        countTransitions(operations);
        calcProbability();
    }

    public void loadMultipleOp(List<String> operations) {
        countOperations(operations);

        countTransitions(operations);

        calcProbability();
    }

    private void countOperations(List<String> ops) {
        //map for operations recount
        Map<String, Integer> opsMap = new HashMap<>();

        for (String op : ops) {
            if (opsMap.get(op) == null) {
                opsMap.put(op, 1);
            } else {
                opsMap.put(op, opsMap.get(op) + 1);
            }
        }

        size = opsMap.keySet().size();

        from = new String[size];
        to = new String[size];
        //copy to arrays
        int i = 0;
        for (String s : opsMap.keySet()) {
            from[i] = s;
            to[i] = s;
            i++;
        }

        probMatrix = init(0.0, size);

        Collections.sort(Arrays.asList(from));
        Collections.sort(Arrays.asList(to));
    }

    private void countTransitions(List<String> ops) {
        //count transitions
        for (int j = 0; j < ops.size() - 1; j++) {
            probMatrix[index(ops.get(j))][index(ops.get(j + 1))]++;
        }
    }

    private Double[][] init(double value, int size) {
        Double[][] m = new Double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                m[i][j] = value;
            }
        }

        return m;
    }

    private int index(String op) {
        for (int i = 0; i < from.length; i++) {
            if (from[i].equals(op)) {
                return i;
            }
        }

        return -1;
    }

    public void print() {
        System.out.println(name);
        System.out.print("* ");
        for (String s : to) {
            System.out.print(s + " ");
        }

        System.out.println("");

        for (int i = 0; i < from.length; i++) {
            System.out.print(from[i] + " ");
            for (int j = 0; j < from.length; j++) {
                System.out.print(df.format(probMatrix[i][j]) + " ");
            }
            System.out.println("");
        }
    }

    private void calcProbability() {
        for (int i = 0; i < size; i++) {
            double rowTotal = rowTotal(i);

            for (int j = 0; j < size; j++) {
                probMatrix[i][j] = ((double) probMatrix[i][j] / (double) rowTotal);
            }
        }
    }

    private double rowTotal(int row) {
        double sum = 0;

        for (int j = 0; j < size; j++) {
            sum += probMatrix[row][j];
        }

        if (sum == 0) {
            sum = 1;
        }

        return sum;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public List<String> generateSteps(int length) {
        List<String> ops = new ArrayList<>();

        Random r = new Random();
        //find random start point
        int i = r.nextInt(from.length);

        for (int l = 0; l < length; l++) {
            //generate probability
            double prob = r.nextDouble();
            double sum = 0;

            for (int j = 0; j < size; j++) {
                sum += probMatrix[i][j];
                if (sum >= prob) {
                    ops.add(to[j]);
                    i = j;
                    break;
                }
            }
        }

        return ops;
    }

    public List<PCSet> generateSets(int length) {
        List<PCSet> sets = new ArrayList<>();

        List<String> strings = generateSteps(length);

        for (String s : strings) {
            sets.add(new PCSet(Integer.parseInt(s.replaceAll("\\[", "").replaceAll("\\]", ""))));
        }

        //aca voy
        return sets;
    }

    public boolean check() {
        //find loops
        for (int i = 0; i < size; i++) {
            if (probMatrix[i][i] == 1.0) {
                //System.out.println("Warning loop detected!");
                return false;
            }
        }

        //find deadends
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += probMatrix[i][j];
            }
            if (sum == 0) {
                //System.out.println("Warning sum cero in row!");
                return false;
            }
        }

        return true;
    }
}
