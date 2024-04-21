package edu.umg.helpers.benchmark_functions.multidimensional;

import java.util.Arrays;
import java.util.function.Function;

public class Sphere implements Function<Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        return Arrays.stream(args).reduce(0.0, (x, y) -> x + Math.pow(y, 2));
    }
}
