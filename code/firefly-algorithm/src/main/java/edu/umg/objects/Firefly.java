package edu.umg.objects;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Firefly  {
    private Double[] location;

    private final Function<Double[], Double> objectiveFunction;

    public Firefly(Function<Double[], Double> objectiveFunction, int numberOfDimensions, Double lowerBound, Double upperBound) {
        this.objectiveFunction = objectiveFunction;

        location = new Double[numberOfDimensions];

        for (int i = 0; i < location.length; i++) {
            location[i] = ThreadLocalRandom.current().nextDouble(lowerBound,upperBound);
        }
    }

    public Double getLocationAt(int index) {
        return location[index];
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    public Double getIntensity() {
        return objectiveFunction.apply(location);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("f(");

        for (int i = 0; i < location.length - 1; i++) {
            stringBuilder.append(String.format("%.5f, ",location[i]));
        }

        return stringBuilder.append(String.format("%.5f",location[location.length - 1])).append(") = ").append(String.format("%.5f",getIntensity())).toString();
    }
}
