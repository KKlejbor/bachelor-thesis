package edu.umg.algorithms.synchronous.objects;

import edu.umg.helpers.Miscellaneous;
import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import java.text.DecimalFormat;
import java.util.Arrays;

public record Firefly(
    Double[] location,
    BenchmarkFunction<Double[], Double> objectiveFunction
) {
    public Double getIntensity() {
        return getIntensity(false);
    }

    public Double getIntensity(boolean invert) {
        return invert
            ? objectiveFunction.apply(location) * -1D
            : objectiveFunction.apply(location);
    }

    public Firefly getCopy() {
        return new Firefly(
            Arrays.copyOf(location, location.length),
            objectiveFunction.getCopy()
        );
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("f(");

        for (int i = 0; i < location.length - 1; i++) {
            stringBuilder.append(
                String.format("%.16f, ", Miscellaneous.round(location[i], 16))
            );
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.################");

        return stringBuilder
            .append(
                String.format(
                    "%.16f",
                    Miscellaneous.round(location[location.length - 1], 16)
                )
            )
            .append(") = ")
            .append(decimalFormat.format(Miscellaneous.round(getIntensity(), 16)))
            .toString();
    }
}
