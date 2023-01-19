package edu.umg.helpers;

import java.util.function.Function;

public class RastriginFunction implements Function<Double[], Double> {
    @Override
    public Double apply(Double[] doubles) {
        double result = 10 * doubles.length;

        for (int i = 0; i < doubles.length; i++) {
            result += Math.pow(doubles[i], 2) - 10 * Math.cos(2 * Math.PI * doubles[i]);
        }

        return result;
    }
}
