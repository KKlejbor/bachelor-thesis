package edu.umg.helpers.benchmark_functions.multidimensional;

import java.util.Arrays;
import java.util.function.Function;

public class Ackley implements Function<Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        double subTerm1 = Arrays.stream(args).reduce(0.0, (x, y) -> x + Math.pow(y, 2));
        double term1 = Math.exp(-0.2 * Math.sqrt(subTerm1 / (double) args.length));

        double subTerm2 = Arrays.stream(args).reduce(
            0.0,
            (x, y) -> x + Math.cos(2 * Math.PI * y)
        );
        double term2 = Math.exp(subTerm2 / (double) args.length);

        return -20 * term1 - term2 + 20 + Math.E;
    }
}
