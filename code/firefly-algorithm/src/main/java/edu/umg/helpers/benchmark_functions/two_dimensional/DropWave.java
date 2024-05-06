package edu.umg.helpers.benchmark_functions.two_dimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import org.javatuples.Pair;

public class DropWave implements BenchmarkFunction<Pair<Double, Double>, Double> {

    @Override
    public Double apply(Pair<Double, Double> args) {
        double numerator =
            1 +
            Math.cos(
                12 *
                Math.sqrt(Math.pow(args.getValue0(), 2) + Math.pow(args.getValue1(), 2))
            );
        double denominator =
            0.5 * (Math.pow(args.getValue0(), 2) + Math.pow(args.getValue1(), 2)) + 2;
        return -(numerator / denominator);
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { -1D };
    }

    @Override
    public BenchmarkFunction<Pair<Double, Double>, Double> getCopy() {
        return new DropWave();
    }
}
