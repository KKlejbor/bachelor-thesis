package edu.umg.experiments.synchronous;

import edu.umg.algorithms.synchronous.FireflyAlgorithm;
import edu.umg.algorithms.synchronous.objects.Firefly;
import edu.umg.helpers.Iteration;
import java.io.PrintWriter;
import java.util.function.Function;

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

    private final Function<Double[], Double> objectiveFunction;

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
        Function<Double[], Double> objectiveFunction
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
        Double[][][][] locations = new Double[numberOfRuns][][][];

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

            runs[i] = fireflyAlgorithm.run();
            locations[i] = fireflyAlgorithm.getLocations();

            Firefly finalSolution = new Firefly(
                fireflyAlgorithm.getFinalSolution(),
                objectiveFunction,
                false
            );

            System.out.printf("Najlepsze rozwiązanie: \n%s\n\n", finalSolution);
        }

        Iteration[] results = new Iteration[maximumNumberOfGenerations];

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
                    "results/%s_a_%s_d_%s_Pop_%d_Iter_%s.csv",
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
                    "%d,%1.6f,%1.6f\r\n",
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
                    "results/%s_a_%s_d_%s_Pop_%d_Iter_%s_locations.csv",
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
                            writer.printf("%.16f,\r\n", locations[0][i][j][k]);
                        }
                    }
                }
                writer.println();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
