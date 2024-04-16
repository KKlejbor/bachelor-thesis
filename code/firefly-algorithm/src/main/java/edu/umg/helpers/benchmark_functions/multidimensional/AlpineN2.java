package edu.umg.helpers.benchmark_functions.multidimensional;

import java.util.Arrays;
import java.util.function.Function;

public class AlpineN2 implements Function<Double[], Double> {
    @Override
    public Double apply(Double[] args) {
        return Arrays.stream(args).reduce(1.0, (x, y) -> x * (Math.sqrt(y) * Math.sin(y)));
    }
}
