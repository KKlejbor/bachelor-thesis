package edu.umg.objects;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class Firefly  {
    private Double[] location;

    private final Function<Double[], Double> objectiveFunction;

    public Firefly(Function<Double[], Double> objectiveFunction, int numberOfDimensions, Double upperBound, Double lowerBound) {
        this.objectiveFunction = objectiveFunction;

        location = new Double[numberOfDimensions];

        for (int i = 0; i < location.length; i++) {
            location[i] = ThreadLocalRandom.current().nextDouble(lowerBound,upperBound);
        }
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = Arrays.copyOf(location, location.length);
    }

    public Double getIntensity() {
        return objectiveFunction.apply(location);
    }
}
