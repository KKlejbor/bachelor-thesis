package edu.umg.helpers.benchmark_functions.multidimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;

import java.util.Arrays;

public class Sphere implements BenchmarkFunction <Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        return Arrays.stream(args).reduce(0.0, (x, y) -> x + Math.pow(y, 2));
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] {0D};
    }
}
