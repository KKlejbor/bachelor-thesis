package edu.umg.experiments.synchronous;

public interface Experiment {
    void run();

    default String getFloatWithoutPeriod(String format, double value) {
        return String.format(format, value).replace(".", "");
    }

    default double[] flatten(double[][] values) {
        double[] all = new double[values.length * values[0].length];

        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                all[i * j + i] = values[i][j];
            }
        }

        return all;
    }

    default String getTime(double seconds) {
        return (
                String.format("%02d:", (long) seconds / (216000)) +
                        String.format("%02d:", (long) seconds / (3600)) +
                        String.format("%02d", (long) seconds / (60))
        );
    }
}
