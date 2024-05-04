package edu.umg.algorithms.synchronous.objects;


import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import org.javatuples.Pair;

public record FireflyUsingPair(
    Pair<Double, Double> location,
    BenchmarkFunction<Pair<Double, Double>, Double> objectiveFunction
) {
    public Double getIntensity(){
        return getIntensity(false);
    }
    public Double getIntensity(boolean invert) {
        return invert ? objectiveFunction.apply(location) * -1D : objectiveFunction.apply(location);
    }

    public FireflyUsingPair getCopy() {
        return new FireflyUsingPair(new Pair<>(location.getValue0(), location.getValue1()), this.objectiveFunction);
    }

    @Override
    public String toString() {
        return (
            "f(" +
            String.format("%.16f, ", location.getValue0()) +
            String.format("%.16f", location.getValue1()) +
            ") = " +
            String.format("%.16f", getIntensity())
        );
    }
}
