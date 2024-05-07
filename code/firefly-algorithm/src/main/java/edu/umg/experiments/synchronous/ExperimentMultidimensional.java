package edu.umg.experiments.synchronous;

import edu.umg.algorithms.synchronous.FireflyAlgorithm;
import edu.umg.helpers.Iteration;
import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import java.io.File;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class ExperimentMultidimensional implements Experiment {

    private final double maximalAttractiveness;
    private final double lightAbsorptionCoefficient;
    private final double randomStepCoefficient;
    private final double randomStepReductionCoefficient;
    private final int populationSize;
    private final int maximumNumberOfGenerations;
    private final int numberOfRuns;
    private final int numberOfDimensions;
    private final boolean minimalize;
    private final double lowerBound;
    private final double upperBound;

    private final BenchmarkFunction<Double[], Double> objectiveFunction;

    public ExperimentMultidimensional(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double randomStepCoefficient,
        double randomStepReductionCoefficient,
        int populationSize,
        int maximumNumberOfGenerations,
        int numberOfRuns,
        int numberOfDimensions,
        boolean minimalize,
        double lowerBound,
        double upperBound,
        BenchmarkFunction<Double[], Double> objectiveFunction
    ) {
        this.maximalAttractiveness = maximalAttractiveness;
        this.lightAbsorptionCoefficient = lightAbsorptionCoefficient;
        this.randomStepCoefficient = randomStepCoefficient;
        this.randomStepReductionCoefficient = randomStepReductionCoefficient;
        this.populationSize = populationSize;
        this.maximumNumberOfGenerations = maximumNumberOfGenerations;
        this.numberOfRuns = numberOfRuns;
        this.numberOfDimensions = numberOfDimensions;
        this.minimalize = minimalize;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.objectiveFunction = objectiveFunction;
    }

    @Override
    public void run() {
        Iteration[][] runs = new Iteration[numberOfRuns][];
        double[] bestValues = new double[numberOfRuns];
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
                "results/%s_a_%s_d_%s_Pop_%d_Iter_%s/",
                objectiveFunction.getClass().getSimpleName(),
                getFloatWithoutPeriod("%1.1f", randomStepCoefficient),
                getFloatWithoutPeriod("%1.3f", randomStepReductionCoefficient),
                populationSize,
                maximumNumberOfGenerations
            )
        );
        resultDir.mkdirs();

        for (int i = 0; i < numberOfRuns; i++) {
            FireflyAlgorithm fireflyAlgorithm = new FireflyAlgorithm(
                maximalAttractiveness,
                lightAbsorptionCoefficient,
                randomStepCoefficient,
                randomStepReductionCoefficient,
                populationSize,
                maximumNumberOfGenerations,
                objectiveFunction,
                numberOfDimensions,
                lowerBound,
                upperBound,
                minimalize
            );

            Instant start = Instant.now();
            runs[i] = fireflyAlgorithm.run();
            Instant stop = Instant.now();

            times[i] = Duration.between(start, stop).getSeconds();
            bestValues[i] = fireflyAlgorithm.getFinalSolution().getIntensity();

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

            int localEnd = fireflyAlgorithm.getCurrentRun();

            if (localEnd < end) {
                end = localEnd;
            }

            if (fireflyAlgorithm.hasReachedTheGoal()) {
                numberOfReaches++;
            }
        }

        Iteration[] results = new Iteration[end];

        for (int i = 0; i < end; i++) {
            results[i] = new Iteration(0.0, 0.0);
        }

        for (int i = 0; i < runs.length; i++) {
            for (int j = 0; j < end; j++) {
                if (results[j] == null) {
                    end = j;
                    break;
                }

                Iteration iteration = results[j];

                results[j] = new Iteration(
                    iteration.average() + runs[i][j].average(),
                    iteration.best() + runs[i][j].best()
                );
            }
        }

        for (int i = 0; i < end; i++) {
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
            for (int i = 0; i < end; i++) {
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
                "max;min;standard;avg;max_time;min_time;avg_time;reached_percent"
            );

            StandardDeviation sd = new StandardDeviation(false);
            writer.printf("%f1.6;", StatUtils.max(bestValues));
            writer.printf("%f1.6;", StatUtils.min(bestValues));
            writer.printf("%f1.6;", sd.evaluate(bestValues));
            writer.printf("%f1.6;", StatUtils.mean(bestValues));
            writer.print(getTime(StatUtils.max(times)) + ";");
            writer.print(getTime(StatUtils.min(times)) + ";");
            writer.print(getTime(StatUtils.mean(times)) + ";");
            writer.printf(
                "%1.2f\n",
                Math.round((numberOfReaches / (double) numberOfRuns) * 10000.0) / 100.0
            );
            writer.println();
            for (int i = 0; i < bestValues.length - 1; i++) {
                writer.printf("%f1.6;", bestValues[i]);
            }
            writer.printf("%f1.6;", bestValues[bestValues.length - 1]);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
