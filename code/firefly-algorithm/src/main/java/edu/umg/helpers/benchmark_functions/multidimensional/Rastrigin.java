package edu.umg.helpers.benchmark_functions.multidimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;

public class Rastrigin implements BenchmarkFunction<Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        double result = 10 * args.length;

        for (Double arg : args) {
            result += Math.pow(arg, 2) - 10 * Math.cos(2 * Math.PI * arg);
        }

        return result;
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { 0D };
    }

    @Override
    public BenchmarkFunction<Double[], Double> getCopy() {
        return new Rastrigin();
    }
}
