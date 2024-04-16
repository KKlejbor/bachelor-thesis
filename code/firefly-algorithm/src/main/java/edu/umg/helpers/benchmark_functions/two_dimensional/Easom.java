package edu.umg.helpers.benchmark_functions.two_dimensional;

import org.javatuples.Pair;

import java.util.function.Function;

public class Easom implements Function<Pair<Double, Double>, Double> {
    @Override
    public Double apply(Pair<Double, Double> args) {
        double term1 = -Math.cos(args.getValue0());
        double term2 = Math.cos(args.getValue1());

        double subTerm1 = -Math.pow(args.getValue0() - Math.PI, 2);
        double subTerm2 = Math.pow(args.getValue1() - Math.PI, 2);
        double term3 = Math.exp(subTerm1 - subTerm2);

        return term1 * term2 * term3;
    }
}
