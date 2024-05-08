package edu.umg.experiments.synchronous;

import edu.umg.algorithms.synchronous.FireflyAlgorithmUsingPair;
import edu.umg.helpers.Iteration;
import edu.umg.helpers.Miscellaneous;
import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import java.io.File;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
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
        double[] bestValues = new double[numberOfRuns];
        double[][] values = new double[numberOfRuns][];
        double[] times = new double[numberOfRuns];
        int numberOfReaches = 0;
        int end = maximumNumberOfGenerations + 1;

        System.out.printf(
            "Wystartował eksperyment -- Funkcja %s, alpha = %1.3f, delta = %1.3f, gamma = %1.3f, pop = %d\n",
            objectiveFunction.getClass().getSimpleName(),
            randomStepCoefficient,
            randomStepReductionCoefficient,
            lightAbsorptionCoefficient,
            populationSize
        );

        File resultDir = new File(
            String.format(
                "results/%s_a_%s_d_%s_Pop_%d_Iter_%d/",
                objectiveFunction.getClass().getSimpleName(),
                getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                populationSize,
                maximumNumberOfGenerations
            )
        );
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

            Instant start = Instant.now();
            runs[i] = fireflyAlgorithm.run();
            Instant stop = Instant.now();

            times[i] = Duration.between(start, stop).getSeconds();

            System.out.printf(
                "\nFunkcja %s, alpha = %1.3f, delta = %1.3f, gamma = %1.3f, pop = %d, podejście %d.: %s\nNajlepsze rozwiązanie: \n%s\n\n",
                objectiveFunction.getClass().getSimpleName(),
                randomStepCoefficient,
                randomStepReductionCoefficient,
                lightAbsorptionCoefficient,
                populationSize,
                i + 1,
                getTime(times[i]),
                fireflyAlgorithm.getFinalSolution()
            );

            locations[i] = fireflyAlgorithm.getLocations();

            values[i] = fireflyAlgorithm.getIntensities();
            bestValues[i] = fireflyAlgorithm.getFinalSolution().getIntensity();

            if (fireflyAlgorithm.hasReachedTheGoal()) {
                numberOfReaches++;
            }

            int localEnd = fireflyAlgorithm.getCurrentRun();

            if (localEnd < end) {
                end = localEnd;
            }

            try (
                PrintWriter writer = new PrintWriter(
                    String.format(
                        "results/%s_a_%s_d_%s_Pop_%d_Iter_%d/%s_a_%s_d_%s_Pop_%d_Iter_%d_run_%d_values.csv",
                        objectiveFunction.getClass().getSimpleName(),
                        getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                        getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                        populationSize,
                        maximumNumberOfGenerations,
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
                    writer.printf("%1.5f\n", Miscellaneous.round(values[i][j], 5));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
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
                    "results/%s_a_%s_d_%s_Pop_%d_Iter_%d/%s_a_%s_d_%s_Pop_%d_Iter_%d.csv",
                    objectiveFunction.getClass().getSimpleName(),
                    getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                    getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                    populationSize,
                    maximumNumberOfGenerations,
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
                    "%d,%1.5f,%1.5f\n",
                    i + 1,
                    Miscellaneous.round(results[i].average(), 5),
                    Miscellaneous.round(results[i].best(), 5)
                );
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try (
            PrintWriter writer = new PrintWriter(
                String.format(
                    "results/%s_a_%s_d_%s_Pop_%d_Iter_%d/%s_a_%s_d_%s_Pop_%d_Iter_%d_locations.csv",
                    objectiveFunction.getClass().getSimpleName(),
                    getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                    getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                    populationSize,
                    maximumNumberOfGenerations,
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
                            writer.printf("%.16f,", Miscellaneous.round(locations[0][i][j][k], 16));
                        } else {
                            writer.printf("%.16f\n", Miscellaneous.round(locations[0][i][j][k], 16));
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
                    "results/%s_a_%s_d_%s_Pop_%d_Iter_%d/%s_a_%s_d_%s_Pop_%d_Iter_%d_stats.csv",
                    objectiveFunction.getClass().getSimpleName(),
                    getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                    getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                    populationSize,
                    maximumNumberOfGenerations,
                    objectiveFunction.getClass().getSimpleName(),
                    getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                    getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                    populationSize,
                    maximumNumberOfGenerations
                )
            )
        ) {
            writer.println(
                "max,min,standard,avg,max_time,min_time,avg_time,reached_percent"
            );
            StandardDeviation sd = new StandardDeviation(false);
            writer.printf("%1.5f,", Miscellaneous.round(StatUtils.max(bestValues), 5));
            writer.printf("%1.5f,", Miscellaneous.round(StatUtils.min(bestValues), 5));
            writer.printf("%1.5f,", Miscellaneous.round(sd.evaluate(bestValues), 5));
            writer.printf("%1.5f,", Miscellaneous.round(StatUtils.mean(bestValues), 5));
            writer.print(getTime(StatUtils.max(times)) + ",");
            writer.print(getTime(StatUtils.min(times)) + ",");
            writer.print(getTime(StatUtils.mean(times)) + ",");
            writer.printf(
                "%1.2f\n",
                Miscellaneous.round((numberOfReaches / (double) numberOfRuns) * 100D, 2)
            );
            writer.println();
            for (int i = 0; i < bestValues.length - 1; i++) {
                writer.printf("%1.5f,", Miscellaneous.round(bestValues[i], 5));
            }
            writer.printf("%1.5f\n", Miscellaneous.round(bestValues[bestValues.length - 1], 5));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
