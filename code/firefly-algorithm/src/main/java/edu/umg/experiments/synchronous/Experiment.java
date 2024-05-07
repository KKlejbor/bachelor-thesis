package edu.umg.experiments.synchronous;

import edu.umg.helpers.Miscellaneous;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public interface Experiment extends Runnable {
    void run();

    default String getFloatWithoutPeriod(String format, double value) {
        return String.format(format, value).replace(".", "");
    }

    default double[] flatten(double[][] values) {
        return Miscellaneous.flatten(values);
    }

    default String getTime(double seconds) {
        return Miscellaneous.getTime(seconds);
    }
}
