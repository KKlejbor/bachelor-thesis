package edu.umg.algorithms.synchronous.objects;

import edu.umg.helpers.Negative;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.javatuples.Pair;

public class FireflyUsingPair {

    private final Function<Pair<Double, Double>, Double> objectiveFunction;
    private final Function<Double, Double> Negative = new Negative();
    private Pair<Double, Double> location;

    public FireflyUsingPair(
        Function<Pair<Double, Double>, Double> objectiveFunction,
        Double lowerBound,
        Double upperBound,
        boolean minimalize
    ) {
        this.objectiveFunction = minimalize
            ? objectiveFunction.andThen(Negative)
            : objectiveFunction;

        location = new Pair<>(
            ThreadLocalRandom.current().nextDouble(lowerBound, upperBound),
            ThreadLocalRandom.current().nextDouble(lowerBound, upperBound)
        );
    }

    public FireflyUsingPair(
        FireflyUsingPair firefly,
        Function<Pair<Double, Double>, Double> objectiveFunction,
        boolean minimalize
    ) {
        this.location = new Pair<>(
            firefly.location.getValue0(),
            firefly.location.getValue1()
        );
        this.objectiveFunction = minimalize
            ? objectiveFunction.andThen(Negative)
            : objectiveFunction;
    }

    public Double getLocationAt(int index) {
        return (Double) location.getValue(index);
    }

    public void setLocation(Pair<Double, Double> location) {
        this.location = location;
    }

    public Double getIntensity() {
        return objectiveFunction.apply(location);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("f(");

        for (int i = 0; i < location.getSize() - 1; i++) {
            stringBuilder.append(String.format("%.5f, ", (Double) location.getValue(i)));
        }

        return stringBuilder
            .append(
                String.format("%.5f", (Double) location.getValue(location.getSize() - 1))
            )
            .append(") = ")
            .append(String.format("%.5f", getIntensity()))
            .toString();
    }
}
