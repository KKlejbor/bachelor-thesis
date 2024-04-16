package edu.umg.helpers.benchmark_functions.two_dimensional;

import org.javatuples.Pair;

import java.util.function.Function;

public class Eggholder implements Function<Pair<Double, Double>, Double> {
    @Override
    public Double apply(Pair<Double, Double> args) {
        double term1 = -(args.getValue1() + 47);
        double term2 = Math.sin(Math.sqrt(Math.abs(args.getValue1() + (args.getValue0() / 2.0) + 47)));
        double term3 = -args.getValue0();
        double term4 = Math.sin(Math.sqrt(Math.abs(args.getValue0() - (args.getValue1() + 47))));
        return term1 * term2 + term3 * term4;
    }
}
