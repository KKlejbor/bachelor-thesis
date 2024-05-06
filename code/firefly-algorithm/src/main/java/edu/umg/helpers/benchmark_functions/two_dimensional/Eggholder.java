package edu.umg.helpers.benchmark_functions.two_dimensional;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import org.javatuples.Pair;

public class Eggholder implements BenchmarkFunction<Pair<Double, Double>, Double> {

    @Override
    public Double apply(Pair<Double, Double> args) {
        double term1 = -(args.getValue1() + 47);
        double term2 = Math.sin(
            Math.sqrt(Math.abs(args.getValue1() + (args.getValue0() / 2.0) + 47))
        );
        double term3 = -args.getValue0();
        double term4 = Math.sin(
            Math.sqrt(Math.abs(args.getValue0() - (args.getValue1() + 47)))
        );
        return term1 * term2 + term3 * term4;
    }

    @Override
    public Double[] getExtremes() {
        return new Double[] { -959.6406627106155 };
    }

    @Override
    public BenchmarkFunction<Pair<Double, Double>, Double> getCopy() {
        return new Eggholder();
    }
}
