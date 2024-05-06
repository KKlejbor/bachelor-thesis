package edu.umg.helpers.benchmark_functions.multidimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import java.util.Arrays;

public class AlpineN2 implements BenchmarkFunction<Double[], Double> {

    private final int numberOfDimensions;

    public AlpineN2(int numberOfDimensions) {
        this.numberOfDimensions = numberOfDimensions;
    }

    @Override
    public Double apply(Double[] args) {
        return Arrays.stream(args).reduce(
            1.0,
            (x, y) -> x * (Math.sqrt(y) * Math.sin(y))
        );
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { Math.pow(2.8081311800070053291, numberOfDimensions) };
    }

    @Override
    public BenchmarkFunction<Double[], Double> getCopy() {
        return new AlpineN2(numberOfDimensions);
    }
}
