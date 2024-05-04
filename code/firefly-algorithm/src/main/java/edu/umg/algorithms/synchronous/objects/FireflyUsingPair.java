package edu.umg.algorithms.synchronous.objects;

import edu.umg.helpers.Negative;
import java.util.function.Function;
import org.javatuples.Pair;

public record FireflyUsingPair(
    Pair<Double, Double> location,
    Function<Pair<Double, Double>, Double> objectiveFunction
) {
    public Double getIntensity() {
        return objectiveFunction.apply(location);
    }

    public FireflyUsingPair getCopy() {
        return getCopy(false);
    }

    public FireflyUsingPair getCopy(boolean invert) {
        return new FireflyUsingPair(
            new Pair<>(location.getValue0(), location.getValue1()),
            invert
                ? this.objectiveFunction.andThen(new Negative())
                : this.objectiveFunction
        );
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
