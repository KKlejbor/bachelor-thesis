package edu.umg.helpers.benchmark_functions.multidimensional;

import java.util.Arrays;
import java.util.function.Function;

public class Schwefel implements Function<Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        double term1 = 418.9829 * args.length;
        double term2 = Arrays.stream(args).reduce(
            0.0,
            (x, y) -> x + (y * Math.sin(Math.sqrt(Math.abs(y))))
        );

        return term1 - term2;
    }
}
