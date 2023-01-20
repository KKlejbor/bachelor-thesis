package edu.umg.helpers;

import java.util.function.Function;

public class RastriginFunction implements Function<Double[], Double> {
    @Override
    public Double apply(Double[] doubles) {
        double result = 10 * doubles.length;

        for (int i = 0; i < doubles.length; i++) {
            result += Math.pow(doubles[i], 2) - 10 * Math.cos(2 * Math.PI * doubles[i]);
        }

        // FIXME: 20.01.2023 Add a flag to the Firefly class, which determines if the function should be maximized or minimized
        return -result;
    }
}
