package edu.umg.helpers.benchmark_functions.two_dimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import org.javatuples.Pair;

public class LevyN13 implements BenchmarkFunction<Pair<Double, Double>, Double> {

    @Override
    public Double apply(Pair<Double, Double> args) {
        double term1 = Math.pow(Math.sin(3 * Math.PI * args.getValue0()), 2);
        double term2 = Math.pow(args.getValue0() - 1, 2);
        double term3 = 1 + Math.pow(Math.sin(3 * Math.PI * args.getValue1()), 2);
        double term4 = Math.pow(args.getValue1() - 1, 2);
        double term5 = 1 + Math.pow(Math.sin(2 * Math.PI * args.getValue1()), 2);
        return term1 + term2 * term3 + term4 * term5;
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { 0D };
    }

    @Override
    public BenchmarkFunction<Pair<Double, Double>, Double> getCopy() {
        return new LevyN13();
    }
}
