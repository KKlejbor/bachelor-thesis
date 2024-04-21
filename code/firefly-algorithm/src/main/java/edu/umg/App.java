package edu.umg;

import edu.umg.experiments.synchronous.Experiment;
import edu.umg.experiments.synchronous.ExperimentMultidimensional;
import edu.umg.helpers.benchmark_functions.multidimensional.Rastrigin;

public class App {

    public static void main(String[] args) {
        Experiment experiment = new ExperimentMultidimensional(
            1.0,
            0.1,
            0.8,
            0.98,
            90,
            2000,
            10,
            4,
            true,
            -5.12,
            5.12,
            new Rastrigin()
        );
        experiment.run();
    }
}
