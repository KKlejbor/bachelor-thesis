package edu.umg.helpers.benchmark_functions.two_dimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import org.javatuples.Pair;

public class Bohachevsky implements BenchmarkFunction<Pair<Double, Double>, Double> {

    @Override
    public Double apply(Pair<Double, Double> args) {
        double term1 = Math.pow(args.getValue0(), 2);
        double term2 = 2 * Math.pow(args.getValue1(), 2);
        double subTerm1 = 3 * Math.PI * args.getValue0();
        double subTerm2 = 4 * Math.PI * args.getValue1();
        double term3 = 0.3 * Math.cos(subTerm1 + subTerm2);

        return term1 + term2 - term3 + 0.3;
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { 0D };
    }

    @Override
    public BenchmarkFunction<Pair<Double, Double>, Double> getCopy() {
        return new Bohachevsky();
    }
}
