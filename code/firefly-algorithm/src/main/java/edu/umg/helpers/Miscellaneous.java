package edu.umg.helpers;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class Miscellaneous {
    public static double[] flatten(double[][] values) {
        return Arrays.stream(values).flatMapToDouble(DoubleStream::of).toArray();
    }

    public static String getTime(double seconds) {
        return (
                String.format("%02d:", (long) seconds / (3600)) +
                        String.format("%02d:", (long) (seconds % 3600) / 60) +
                        String.format("%02d", (long) seconds % 60)
        );
    }
}
