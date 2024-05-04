package edu.umg.algorithms.synchronous.objects;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;

import java.text.DecimalFormat;
import java.util.Arrays;

public record Firefly(Double[] location, BenchmarkFunction<Double[], Double> objectiveFunction) {
    public Double getIntensity(){
        return getIntensity(false);
    }

    public Double getIntensity(boolean invert){
        return invert ? objectiveFunction.apply(location) * -1D : objectiveFunction.apply(location);
    }

    public Firefly getCopy() {
        return new Firefly(Arrays.copyOf(location, location.length), objectiveFunction);
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
