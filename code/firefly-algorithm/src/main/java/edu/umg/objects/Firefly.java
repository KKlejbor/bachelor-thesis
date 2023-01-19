package edu.umg.objects;

import java.util.Arrays;
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

    public Firefly(Firefly firefly){
        this.objectiveFunction = firefly.objectiveFunction;

        this.location = Arrays.copyOf(firefly.location, firefly.location.length);
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
}
