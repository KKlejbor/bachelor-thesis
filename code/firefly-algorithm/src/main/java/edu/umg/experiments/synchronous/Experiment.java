package edu.umg.experiments.synchronous;

public interface Experiment {
    void run();

    default String getFloatWithoutPeriod(String format, double value) {
        return String.format(format, value).replace(".", "");
    }
}
