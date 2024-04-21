package edu.umg.helpers.benchmark_functions.multidimensional;

import java.util.function.Function;

public class Rastrigin implements Function<Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        double result = 10 * args.length;

        for (Double arg : args) {
            result += Math.pow(arg, 2) - 10 * Math.cos(2 * Math.PI * arg);
        }

        return result;
    }
}
