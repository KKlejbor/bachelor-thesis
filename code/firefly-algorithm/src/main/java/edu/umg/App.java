package edu.umg;

import edu.umg.experiments.synchronous.Experiment;
import edu.umg.experiments.synchronous.ExperimentMultidimensional;
import edu.umg.experiments.synchronous.ExperimentTwoDimensional;
import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import edu.umg.helpers.benchmark_functions.multidimensional.*;
import edu.umg.helpers.benchmark_functions.two_dimensional.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

public class App {

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(
            nThreads,
            nThreads,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );
    }

    public static void main(String[] args) {
        double[] randomStepCoefficients = new double[] { 0.1, 0.2, 0.8 };
        double[] randomStepReductionCoefficients = new double[] { 0.98, 0.99, 0.998 };
        int[] populationSizes = new int[] { 100, 200, 500 };

        ArrayList<
            Quartet<BenchmarkFunction<Double[], Double>, Double, Double, Integer>
        > objectiveFunctionsND = new ArrayList<>();

        objectiveFunctionsND.add(new Quartet<>(new Ackley(), -32.768, 32.768, 6));
        //        objectiveFunctionsND.add(new Quartet<>(new AlpineN2(8), 0D, 10D, 8));
        //        objectiveFunctionsND.add(new Quartet<>(new Griewank(), -600D, 600D, 10));
        objectiveFunctionsND.add(new Quartet<>(new Rastrigin(), -5.12, 5.12, 4));
        objectiveFunctionsND.add(new Quartet<>(new Schwefel(), -500D, 500D, 10));
        //        objectiveFunctionsND.add(new Quartet<>(new Sphere(), -5.12, 5.12, 8));

        ArrayList<
            Triplet<BenchmarkFunction<Pair<Double, Double>, Double>, Double, Double>
        > objectiveFunctions2D = new ArrayList<>();

        //        objectiveFunctions2D.add(new Triplet<>(new Bohachevsky(), -100D, 100D));
        //        objectiveFunctions2D.add(new Triplet<>(new DropWave(), -5.12, 5.12));
        objectiveFunctions2D.add(new Triplet<>(new Easom(), -600D, 600D));
        objectiveFunctions2D.add(new Triplet<>(new Eggholder(), -5.12, 5.12));
        //        objectiveFunctions2D.add(new Triplet<>(new Himmelblau(), -500D, 500D));
        objectiveFunctions2D.add(new Triplet<>(new LevyN13(), -10D, 10D));

        try (
            ExecutorService executorService = newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() - 1
            )
        ) {
            for (Quartet<
                BenchmarkFunction<Double[], Double>,
                Double,
                Double,
                Integer
            > objectiveFunction : objectiveFunctionsND) {
                for (double randomStepCoefficient : randomStepCoefficients) {
                    for (double randomStepReductionCoefficient : randomStepReductionCoefficients) {
                        for (int populationSize : populationSizes) {
                            Experiment experiment = new ExperimentMultidimensional(
                                1.0,
                                0.1,
                                randomStepCoefficient,
                                randomStepReductionCoefficient,
                                populationSize,
                                2000,
                                10,
                                objectiveFunction.getValue3(),
                                true,
                                objectiveFunction.getValue1(),
                                objectiveFunction.getValue2(),
                                objectiveFunction.getValue0().getCopy()
                            );

                            executorService.submit(experiment);
                        }
                    }
                }
            }

            for (Triplet<
                BenchmarkFunction<Pair<Double, Double>, Double>,
                Double,
                Double
            > objectiveFunction : objectiveFunctions2D) {
                for (double randomStepCoefficient : randomStepCoefficients) {
                    for (double randomStepReductionCoefficient : randomStepReductionCoefficients) {
                        for (int populationSize : populationSizes) {
                            Experiment experiment = new ExperimentTwoDimensional(
                                1.0,
                                0.1,
                                randomStepCoefficient,
                                randomStepReductionCoefficient,
                                populationSize,
                                2000,
                                10,
                                true,
                                objectiveFunction.getValue1(),
                                objectiveFunction.getValue2(),
                                objectiveFunction.getValue0().getCopy()
                            );

                            executorService.submit(experiment);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.out.println("Completed");
        }
    }
}
