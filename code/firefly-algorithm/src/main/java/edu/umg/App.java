package edu.umg;

import edu.umg.experiments.synchronous.Experiment;
import edu.umg.experiments.synchronous.ExperimentMultidimensional;
import edu.umg.experiments.synchronous.ExperimentTwoDimensional;
import edu.umg.helpers.benchmark_functions.multidimensional.*;
import edu.umg.helpers.benchmark_functions.two_dimensional.*;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.function.Function;

public class App {

    public static void main(String[] args) {
        double[] randomStepCoefficients = new double[] {0.1, 0.2, 0.8};
        double[] randomStepReductionCoefficients = new double[] {0.98, 0.99, 0.998};
        int[] populationSizes = new int[] {100, 250, 500};

        ArrayList<Quartet<Function<Double[],Double>, Double, Double, Integer>> objectiveFunctionsND = new ArrayList<>();

        objectiveFunctionsND.add(new Quartet<>(new Ackley(), -32.768, 32.768, 6));
        objectiveFunctionsND.add(new Quartet<>(new AlpineN2(), 0D, 10D, 8));
        objectiveFunctionsND.add(new Quartet<>(new Griewank(), -600D, 600D, 10));
        objectiveFunctionsND.add(new Quartet<>(new Rastrigin(), -5.12, 5.12, 4));
        objectiveFunctionsND.add(new Quartet<>(new Schwefel(), -500D, 500D, 10));
        objectiveFunctionsND.add(new Quartet<>(new Sphere(), -5.12, 5.12, 8));

        for (Quartet<Function<Double[],Double>, Double, Double, Integer> objectiveFunction : objectiveFunctionsND){
            for (double randomStepCoefficient : randomStepCoefficients){
                for (double randomStepReductionCoefficient: randomStepReductionCoefficients){
                    for(int populationSize : populationSizes){
                        Experiment experiment = new ExperimentMultidimensional(
                                1.0,
                                0.1,
                                randomStepCoefficient,
                                randomStepReductionCoefficient,
                                populationSize,
                                5000,
                                10,
                                objectiveFunction.getValue3(),
                                true,
                                objectiveFunction.getValue1(),
                                objectiveFunction.getValue2(),
                                objectiveFunction.getValue0()
                        );

                        experiment.run();
                    }
                }
            }
        }

        ArrayList<Triplet<Function<Pair<Double, Double>,Double>, Double, Double>> objectiveFunctions2D = new ArrayList<>();

        objectiveFunctions2D.add(new Triplet<>(new Bohachevsky(), -100D, 100D));
        objectiveFunctions2D.add(new Triplet<>(new DropWave(), -5.12, 5.12));
        objectiveFunctions2D.add(new Triplet<>(new Easom(), -600D, 600D));
        objectiveFunctions2D.add(new Triplet<>(new Eggholder(), -5.12, 5.12));
        objectiveFunctions2D.add(new Triplet<>(new Himmelblau(), -500D, 500D));
        objectiveFunctions2D.add(new Triplet<>(new LevyN13(), -5.12, 5.12));

        for (Triplet<Function<Pair<Double, Double>,Double>, Double, Double> objectiveFunction : objectiveFunctions2D){
            for (double randomStepCoefficient : randomStepCoefficients){
                for (double randomStepReductionCoefficient: randomStepReductionCoefficients){
                    for(int populationSize : populationSizes){
                        Experiment experiment = new ExperimentTwoDimensional(
                                1.0,
                                0.1,
                                randomStepCoefficient,
                                randomStepReductionCoefficient,
                                populationSize,
                                5000,
                                10,
                                true,
                                objectiveFunction.getValue1(),
                                objectiveFunction.getValue2(),
                                objectiveFunction.getValue0()
                        );

                        experiment.run();
                    }
                }
            }
        }
    }
}
