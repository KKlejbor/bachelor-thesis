package edu.umg.helpers.benchmark_functions.multidimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import java.util.Arrays;

public class Schwefel implements BenchmarkFunction<Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        double term1 = 418.9829 * args.length;
        double term2 = Arrays.stream(args).reduce(
            0.0,
            (x, y) -> x + (y * Math.sin(Math.sqrt(Math.abs(y))))
        );

        return term1 - term2;
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { 0D };
    }

    @Override
    public BenchmarkFunction<Double[], Double> getCopy() {
        return new Schwefel();
    }
}
