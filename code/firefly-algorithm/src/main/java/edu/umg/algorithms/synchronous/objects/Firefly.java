package edu.umg.algorithms.synchronous.objects;

import edu.umg.helpers.Negative;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.function.Function;

public record Firefly(Double[] location, Function<Double[], Double> objectiveFunction) {
    public Double getIntensity() {
        return objectiveFunction.apply(location);
    }

    public Firefly getCopy() {
        return getCopy(false);
    }

    public Firefly getCopy(boolean invert) {
        return new Firefly(
            Arrays.copyOf(location, location.length),
            invert
                ? this.objectiveFunction.andThen(new Negative())
                : this.objectiveFunction
        );
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("f(");

        for (int i = 0; i < location.length - 1; i++) {
            stringBuilder.append(String.format("%.16f, ", location[i]));
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.################");

        return stringBuilder
            .append(String.format("%.16f", location[location.length - 1]))
            .append(") = ")
            .append(decimalFormat.format(getIntensity()))
            .toString();
    }
}
