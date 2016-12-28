/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.utils;

import java.util.Random;

/**
 *
 * @author casa
 */
public class UniformDistribution implements ProbDistribution {

    private double min;
    private double max;
    private Random random;

    public UniformDistribution(double min, double max) {
        this.min = min;
        this.max = max;
        random = new Random();
    }

    @Override
    public double getNext() {
        double n = min + (max - min) * random.nextDouble();
        System.out.println(n);
        return n;
    }

}
