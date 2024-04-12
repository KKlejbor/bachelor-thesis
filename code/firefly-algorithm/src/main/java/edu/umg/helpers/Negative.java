package edu.umg.helpers;

import java.util.function.Function;

public class Negative implements Function<Double,Double> {

    @Override
    public Double apply(Double input) {
        return input * -1.0;
    }
}
