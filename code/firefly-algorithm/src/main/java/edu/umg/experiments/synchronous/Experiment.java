package edu.umg.experiments.synchronous;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public interface Experiment extends Runnable {
    void run();

    default String getFloatWithoutPeriod(String format, double value) {
        return String.format(format, value).replace(".", "");
    }

    default double[] flatten(double[][] values) {
        return Arrays.stream(values).flatMapToDouble(DoubleStream::of).toArray();
    }

    default String getTime(double seconds) {
        return (
            String.format("%02d:", (long) seconds / (3600)) +
            String.format("%02d:", (long) (seconds % 3600) / 60) +
            String.format("%02d", (long) seconds % 60)
        );
    }
}
