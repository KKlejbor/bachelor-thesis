package edu.umg.helpers.benchmark_functions.multidimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import java.util.Arrays;

public class Griewank implements BenchmarkFunction<Double[], Double> {

    @Override
    public Double apply(Double[] args) {
        double term1 = Arrays.stream(args).reduce(
            0.0,
            (x, y) -> x + (Math.pow(y, 2) / 4000.0)
        );

        double[] modifiedArgs = new double[args.length];

        for (int i = 0; i < args.length; i++) {
            modifiedArgs[i] = args[i] / Math.sqrt(i + 1);
        }

        double term2 = Arrays.stream(modifiedArgs).reduce(1.0, (x, y) -> x * Math.cos(y));

        return term1 - term2 + 1;
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { 0D };
    }

    @Override
    public BenchmarkFunction<Double[], Double> getCopy() {
        return new Griewank();
    }
}
