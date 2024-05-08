package edu.umg.algorithms.synchronous;

import edu.umg.algorithms.synchronous.objects.FireflyUsingPair;
import edu.umg.helpers.Iteration;
import edu.umg.helpers.benchmark_functions.BenchmarkFunction;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.javatuples.Pair;

public class FireflyAlgorithmUsingPair {

    private static final double THRESHOLD = 0.00001;
    private final double maximalAttractiveness;
    private final double lightAbsorptionCoefficient;
    private final double reductionCoefficient;
    private final int populationSize;
    private final int maximumNumberOfGenerations;
    private final Iteration[] iterations;
    private final Double[][][] locations;
    private final BenchmarkFunction<Pair<Double, Double>, Double> objectiveFunction;
    private final boolean minimalize;
    private final double lowerBound;
    private final double upperBound;
    private double randomStepCoefficient;
    private FireflyUsingPair[] population;
    private int locationOfTheBestSolution;
    private FireflyUsingPair theBestSolution;
    private FireflyUsingPair finalSolution;
    private int currentRun = 0;

    public FireflyAlgorithmUsingPair(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double randomStepCoefficient,
        double reductionCoefficient,
        int populationSize,
        int maximumNumberOfGenerations,
        BenchmarkFunction<Pair<Double, Double>, Double> objectiveFunction,
        double lowerBound,
        double upperBound,
        boolean minimalize
    ) {
        this.maximalAttractiveness = maximalAttractiveness;
        this.lightAbsorptionCoefficient = lightAbsorptionCoefficient;
        this.randomStepCoefficient = randomStepCoefficient;
        this.reductionCoefficient = reductionCoefficient;
        this.populationSize = populationSize;
        this.maximumNumberOfGenerations = maximumNumberOfGenerations;
        this.objectiveFunction = objectiveFunction;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.minimalize = minimalize;
        this.iterations = new Iteration[maximumNumberOfGenerations + 1];
        this.locations = new Double[maximumNumberOfGenerations + 1][populationSize][2];
    }

    public Iteration[] run() {
        initializePopulation();
        findTheBestSolution();
        addIteration(0);
        addLocationAt(0);
        int numberOfRunsWithoutImprovements = 5;

        while (currentRun < maximumNumberOfGenerations) {
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < populationSize; j++) {
                    if (
                        population[i].getIntensity(minimalize) <
                        population[j].getIntensity(minimalize)
                    ) {
                        population[i] = new FireflyUsingPair(
                            computeNewLocation(i, j),
                            objectiveFunction
                        );
                    }
                }

                if (
                    population[i].getIntensity(minimalize) >
                    theBestSolution.getIntensity(minimalize)
                ) {
                    theBestSolution = population[i];
                    locationOfTheBestSolution = i;

                    if (
                        theBestSolution.getIntensity(minimalize) >
                        finalSolution.getIntensity(minimalize)
                    ) {
                        finalSolution = theBestSolution.getCopy();
                        numberOfRunsWithoutImprovements = 5;
                    }

                    if (--numberOfRunsWithoutImprovements == 0) {
                        break;
                    }

                    if (hasReachedTheGoal()) {
                        break;
                    }
                }
            }

            theBestSolution = new FireflyUsingPair(
                computeNewLocation(),
                objectiveFunction
            );
            reduceRandomStepCoefficient();
            addIteration(currentRun + 1);
            addLocationAt(currentRun + 1);
            currentRun++;
        }

        return Arrays.copyOf(iterations, currentRun + 1);
    }

    private void initializePopulation() {
        population = new FireflyUsingPair[populationSize];

        for (int i = 0; i < populationSize; i++) {
            population[i] = new FireflyUsingPair(
                new Pair<>(
                    ThreadLocalRandom.current().nextDouble(lowerBound, upperBound),
                    ThreadLocalRandom.current().nextDouble(lowerBound, upperBound)
                ),
                objectiveFunction
            );
        }
    }

    private void findTheBestSolution() {
        locationOfTheBestSolution = 0;
        theBestSolution = population[0];

        for (int i = 1; i < populationSize; i++) {
            if (
                population[i].getIntensity(minimalize) >
                theBestSolution.getIntensity(minimalize)
            ) {
                locationOfTheBestSolution = i;
                theBestSolution = population[locationOfTheBestSolution];
            }
        }

        finalSolution = theBestSolution.getCopy();
    }

    private double distanceBetweenFireflies(int index1, int index2) {
        double result = Math.pow(
            population[index1].location().getValue0() -
            population[index2].location().getValue0(),
            2
        );
        result +=
        Math.pow(
            population[index1].location().getValue1() -
            population[index2].location().getValue1(),
            2
        );

        return result;
    }

    private double computeAttractiveness(int index1, int index2) {
        return (
            maximalAttractiveness *
            Math.exp(
                -lightAbsorptionCoefficient * distanceBetweenFireflies(index1, index2)
            )
        );
    }

    private Pair<Double, Double> computeNewLocation(int index1, int index2) {
        double attractiveness = computeAttractiveness(index1, index2);

        double first =
            population[index1].location().getValue0() +
            attractiveness *
                (population[index2].location().getValue0() -
                    population[index1].location().getValue0()) +
            computeRandomStep();
        double second =
            population[index1].location().getValue1() +
            attractiveness *
                (population[index2].location().getValue1() -
                    population[index1].location().getValue1()) +
            computeRandomStep();

        return createNewPair(first, second);
    }

    private double computeRandomStep() {
        return randomStepCoefficient * (ThreadLocalRandom.current().nextDouble() - 0.5);
    }

    private Pair<Double, Double> computeNewLocation() {
        double first =
            population[locationOfTheBestSolution].location().getValue0() +
            computeRandomStep();
        double second =
            population[locationOfTheBestSolution].location().getValue1() +
            computeRandomStep();

        return createNewPair(first, second);
    }

    private Pair<Double, Double> createNewPair(double first, double second) {
        if (first < lowerBound) {
            first = lowerBound;
        }

        if (first > upperBound) {
            first = upperBound;
        }

        if (second < lowerBound) {
            second = lowerBound;
        }

        if (second > upperBound) {
            second = upperBound;
        }

        return new Pair<>(first, second);
    }

    private void reduceRandomStepCoefficient() {
        randomStepCoefficient *= reductionCoefficient;
    }

    private void addIteration(int index) {
        double result = 0;

        for (FireflyUsingPair firefly : population) {
            result += firefly.getIntensity();
        }

        iterations[index] = new Iteration(result, finalSolution.getIntensity());
    }

    private void addLocationAt(int index) {
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < 2; j++) {
                locations[index][i][j] = (Double) population[i].location().getValue(j);
            }
        }
    }

    public FireflyUsingPair getFinalSolution() {
        return finalSolution.getCopy();
    }

    public Double[][][] getLocations() {
        return locations;
    }

    public double[] getIntensities() {
        double[] intensities = new double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            intensities[i] = population[i].getIntensity();
        }

        return intensities;
    }

    public boolean hasReachedTheGoal() {
        Double[] extremes = objectiveFunction.getExtremes();
        boolean result = false;

        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < extremes.length; j++) {
                result = areFloatsEqual(population[i].getIntensity(), extremes[j]);
                if (result) {
                    return result;
                }
            }
        }

        return result;
    }

    private boolean areFloatsEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < THRESHOLD;
    }

    public int getCurrentRun() {
        return currentRun;
    }
}
