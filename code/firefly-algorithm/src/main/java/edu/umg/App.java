package edu.umg;

import edu.umg.experiments.synchronous.Experiment;
import edu.umg.experiments.synchronous.ExperimentMultidimensional;
import edu.umg.experiments.synchronous.ExperimentTwoDimensional;
import edu.umg.helpers.Miscellaneous;
import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import edu.umg.helpers.benchmark_functions.multidimensional.*;
import edu.umg.helpers.benchmark_functions.two_dimensional.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

public class App {

    private static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(
            nThreads,
            nThreads,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );
    }

    private static void runSynchronousVersion(String[] functions) {
        double[] randomStepCoefficients = new double[] { 0.1, 0.2, 0.8 };
        double[] randomStepReductionCoefficients = new double[] { 0.98, 0.99, 0.998 };
        int[] populationSizes = new int[] { 100, 200, 500 };

        ArrayList<
            Quartet<BenchmarkFunction<Double[], Double>, Double, Double, Integer>
        > objectiveFunctionsND = new ArrayList<>();

        objectiveFunctionsND.add(new Quartet<>(new Ackley(), -32.768, 32.768, 6));
        objectiveFunctionsND.add(new Quartet<>(new Rastrigin(), -5.12, 5.12, 4));
        objectiveFunctionsND.add(new Quartet<>(new Schwefel(), -500D, 500D, 10));

        ArrayList<
            Triplet<BenchmarkFunction<Pair<Double, Double>, Double>, Double, Double>
        > objectiveFunctions2D = new ArrayList<>();

        objectiveFunctions2D.add(new Triplet<>(new DropWave(), -5.12, 5.12));
        objectiveFunctions2D.add(new Triplet<>(new Eggholder(), -512D, 512D));
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
                            if (
                                functions.length > 0 &&
                                Arrays.stream(functions).anyMatch(
                                    s ->
                                        s.equals(
                                            objectiveFunction
                                                .getValue0()
                                                .getClass()
                                                .getSimpleName()
                                        )
                                )
                            ) {
                                executorService.submit(experiment);
                            }
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
                            if (
                                functions.length > 0 &&
                                Arrays.stream(functions).anyMatch(
                                    s ->
                                        s.equals(
                                            objectiveFunction
                                                .getValue0()
                                                .getClass()
                                                .getSimpleName()
                                        )
                                )
                            ) {
                                executorService.submit(experiment);
                            }
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

    public static void main(String[] args) {
        Instant start = Instant.now();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(
            "========================================================================"
        );
        System.out.printf("Uruchomiono aplikację: %s \n", dtf.format(now));
        System.out.println(
            "========================================================================"
        );

        if (!Arrays.stream(args).anyMatch(s -> s.equals("skip-synch"))) {
            String[] tokens = Arrays.stream(args)
                .filter(s -> s.contains("only="))
                .findAny()
                .orElse("")
                .split("=");

            String[] functions = tokens.length > 0 ? tokens[1].split(",") : new String[0];

            runSynchronousVersion(functions);
        }

        now = LocalDateTime.now();
        Instant stop = Instant.now();
        System.out.println(
            "========================================================================"
        );
        System.out.printf("Aplikacja zakończyła działanie: %s \n", dtf.format(now));
        System.out.printf(
            "Czas działania: %s \n",
            Miscellaneous.getTime(Duration.between(start, stop).getSeconds())
        );
        System.out.println(
            "========================================================================"
        );
    }
}
