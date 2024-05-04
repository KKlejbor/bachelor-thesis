package edu.umg.helpers.benchmark_functions.two_dimensional;


import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import org.javatuples.Pair;

public class Himmelblau implements BenchmarkFunction<Pair<Double, Double>, Double> {

    @Override
    public Double apply(Pair<Double, Double> args) {
        return (
            Math.pow(Math.pow(args.getValue0(), 2) + args.getValue1() - 11, 2) +
            Math.pow(args.getValue0() + Math.pow(args.getValue1(), 2) - 7, 2)
        );
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] {0D};
    }
}
