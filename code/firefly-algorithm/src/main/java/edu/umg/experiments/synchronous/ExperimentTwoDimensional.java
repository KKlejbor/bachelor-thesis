package edu.umg.experiments.synchronous;

import edu.umg.algorithms.synchronous.FireflyAlgorithmUsingPair;
import edu.umg.algorithms.synchronous.objects.FireflyUsingPair;
import edu.umg.helpers.Iteration;

import java.io.File;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;

import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import org.apache.commons.math3.stat.StatUtils;
import org.javatuples.Pair;

public class ExperimentTwoDimensional implements Experiment {

    private final double maximalAttractiveness;
    private final double lightAbsorptionCoefficient;
    private final double randomStepCoefficient;
    private final double randomStepReductionCoefficient;
    private final int populationSize;
    private final int maximumNumberOfGenerations;
    private final int numberOfRuns;
    private final boolean minimalize;
    private final double lowerBound;
    private final double upperBound;

    private final BenchmarkFunction<Pair<Double, Double>, Double> objectiveFunction;

    public ExperimentTwoDimensional(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double randomStepCoefficient,
        double randomStepReductionCoefficient,
        int populationSize,
        int maximumNumberOfGenerations,
        int numberOfRuns,
        boolean minimalize,
        double lowerBound,
        double upperBound,
        BenchmarkFunction<Pair<Double, Double>, Double> objectiveFunction
    ) {
        this.maximalAttractiveness = maximalAttractiveness;
        this.lightAbsorptionCoefficient = lightAbsorptionCoefficient;
        this.randomStepCoefficient = randomStepCoefficient;
        this.randomStepReductionCoefficient = randomStepReductionCoefficient;
        this.populationSize = populationSize;
        this.maximumNumberOfGenerations = maximumNumberOfGenerations;
        this.numberOfRuns = numberOfRuns;
        this.minimalize = minimalize;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.objectiveFunction = objectiveFunction;
    }

    @Override
    public void run() {
        Iteration[][] runs = new Iteration[numberOfRuns][];
        Double[][][][] locations = new Double[numberOfRuns][][][];
        double[][] values = new double[numberOfRuns][];
        double[] times = new double[numberOfRuns];
        int numberOfReaches = 0;

        File resultDir = new File(String.format("results/%s/", objectiveFunction.getClass().getSimpleName()));
        resultDir.mkdirs();

        for (int i = 0; i < numberOfRuns; i++) {
            FireflyAlgorithmUsingPair fireflyAlgorithm = new FireflyAlgorithmUsingPair(
                maximalAttractiveness,
                lightAbsorptionCoefficient,
                randomStepCoefficient,
                randomStepReductionCoefficient,
                populationSize,
                maximumNumberOfGenerations,
                objectiveFunction,
                lowerBound,
                upperBound,
                minimalize
            );

            System.out.printf(
                    "Funkcja %s, podejście %d.: ",
                    objectiveFunction.getClass().getSimpleName(),
                    i + 1
            );

            Instant start = Instant.now();
            runs[i] = fireflyAlgorithm.run();
            Instant stop = Instant.now();

            times[i] = Duration.between(start, stop).getSeconds();

            System.out.println("ukończone " + getTime(times[i]));
            locations[i] = fireflyAlgorithm.getLocations();

            values[i] = fireflyAlgorithm.getIntensities();

            if(fireflyAlgorithm.hasReachedTheGoal()){
                numberOfReaches++;
            }

            try (
                    PrintWriter writer = new PrintWriter(
                            String.format(
                                    "results/%s/%s_a_%s_d_%s_Pop_%d_Iter_%s_run_%d_values.csv",
                                    objectiveFunction.getClass().getSimpleName(),
                                    objectiveFunction.getClass().getSimpleName(),
                                    getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                                    getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                                    populationSize,
                                    maximumNumberOfGenerations,
                                    i
                            )
                    )
            ) {
                for (int j = 0; j < values.length; j++) {
                    writer.printf("%f1.6\n", values[i][j]);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            FireflyUsingPair finalSolution = fireflyAlgorithm.getFinalSolution();


            System.out.printf("Najlepsze rozwiązanie: \n%s\n\n", finalSolution);
        }

        Iteration[] results = new Iteration[maximumNumberOfGenerations + 1];

        for (int i = 0; i < results.length; i++) {
            results[i] = new Iteration(0.0, 0.0);
        }

        for (int i = 0; i < runs.length; i++) {
            for (int j = 0; j < runs[i].length; j++) {
                Iteration iteration = results[j];
                results[j] = new Iteration(
                    iteration.average() + runs[i][j].average(),
                    iteration.best() + runs[i][j].best()
                );
            }
        }

        for (int i = 0; i < results.length; i++) {
            results[i] = new Iteration(
                results[i].average() / (double) (numberOfRuns * populationSize),
                results[i].best() / (double) (numberOfRuns)
            );
        }

        try (
            PrintWriter writer = new PrintWriter(
                String.format(
                    "results/%s/%s_a_%s_d_%s_Pop_%d_Iter_%s.csv",
                    objectiveFunction.getClass().getSimpleName(),
                        objectiveFunction.getClass().getSimpleName(),
                    getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                    getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                    populationSize,
                    maximumNumberOfGenerations
                )
            )
        ) {
            for (int i = 0; i < results.length; i++) {
                writer.printf(
                    "%d,%1.6f,%1.6f\n",
                    i + 1,
                    results[i].average(),
                    results[i].best()
                );
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try (
            PrintWriter writer = new PrintWriter(
                String.format(
                    "results/%s/%s_a_%s_d_%s_Pop_%d_Iter_%s_locations.csv",
                    objectiveFunction.getClass().getSimpleName(),
                    objectiveFunction.getClass().getSimpleName(),
                    getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                    getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                    populationSize,
                    maximumNumberOfGenerations
                )
            )
        ) {
            for (int i = 0; i < locations[0].length; i++) {
                for (int k = 0; k < 2; k++) {
                    for (int j = 0; j < locations[0][i].length; j++) {
                        if (j < locations[0][i].length - 1) {
                            writer.printf("%.16f,", locations[0][i][j][k]);
                        } else {
                            writer.printf("%.16f,\n", locations[0][i][j][k]);
                        }
                    }
                }
                writer.println();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try (
                PrintWriter writer = new PrintWriter(
                        String.format(
                                "results/%s/%s_a_%s_d_%s_Pop_%d_Iter_%s_stats.csv",
                                objectiveFunction.getClass().getSimpleName(),
                                objectiveFunction.getClass().getSimpleName(),
                                getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                                getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                                populationSize,
                                maximumNumberOfGenerations
                        )
                )
        ) {
            writer.println("max;min;standard;avg;max_time;min_time;avg_time;reached_percent");
            double[] allValues = flatten(values);
            writer.printf("%f1.6;", StatUtils.max(allValues));
            writer.printf("%f1.6;", StatUtils.min(allValues));
            writer.printf("%f1.6;", StatUtils.mean(allValues));
            writer.print(getTime(StatUtils.max(times)));
            writer.print(getTime(StatUtils.min(times)));
            writer.print(getTime(StatUtils.mean(times)));
            writer.printf("%f1.2\n", Math.round((numberOfReaches / (double) numberOfRuns) * 10000.0) / 100.0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
