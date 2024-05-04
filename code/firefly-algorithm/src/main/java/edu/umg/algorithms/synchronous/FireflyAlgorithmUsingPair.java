package edu.umg.algorithms.synchronous;

import edu.umg.algorithms.synchronous.objects.FireflyUsingPair;
import edu.umg.helpers.Iteration;
import edu.umg.helpers.Negative;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.javatuples.Pair;

public class FireflyAlgorithmUsingPair {

    private final double maximalAttractiveness;
    private final double lightAbsorptionCoefficient;
    private final double reductionCoefficient;
    private final int populationSize;
    private final int maximumNumberOfGenerations;
    private final Iteration[] iterations;
    private final Double[][][] locations;
    private final Function<Pair<Double, Double>, Double> objectiveFunction;
    private final boolean minimalize;
    private final double lowerBound;
    private final double upperBound;
    private double randomStepCoefficient;
    private FireflyUsingPair[] population;
    private int locationOfTheBestSolution;
    private FireflyUsingPair theBestSolution;
    private FireflyUsingPair finalSolution;

    public FireflyAlgorithmUsingPair(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double randomStepCoefficient,
        double reductionCoefficient,
        int populationSize,
        int maximumNumberOfGenerations,
        Function<Pair<Double, Double>, Double> objectiveFunction,
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
        this.objectiveFunction = minimalize
            ? objectiveFunction.andThen(new Negative())
            : objectiveFunction;
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

        for (int n = 0; n < maximumNumberOfGenerations; n++) {
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < populationSize; j++) {
                    if (population[i].getIntensity() < population[j].getIntensity()) {
                        population[i] = new FireflyUsingPair(
                            computeNewLocation(i, j),
                            objectiveFunction
                        );
                    }
                }

                if (population[i].getIntensity() > theBestSolution.getIntensity()) {
                    theBestSolution = population[i];
                    locationOfTheBestSolution = i;

                    if (theBestSolution.getIntensity() > finalSolution.getIntensity()) {
                        finalSolution = theBestSolution.getCopy();
                    }
                }
            }

            theBestSolution = new FireflyUsingPair(
                computeNewLocation(),
                objectiveFunction
            );
            reduceRandomStepCoefficient();
            addIteration(n + 1);
            addLocationAt(n + 1);
        }

        return iterations;
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
            if (population[i].getIntensity() > theBestSolution.getIntensity()) {
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

        Negative Negative = new Negative();

        for (FireflyUsingPair firefly : population) {
            result += firefly.getIntensity();
        }

        iterations[index] = new Iteration(
            minimalize ? Negative.apply(result) : result,
            minimalize
                ? Negative.apply(finalSolution.getIntensity())
                : finalSolution.getIntensity()
        );
    }

    private void addLocationAt(int index) {
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < 2; j++) {
                locations[index][i][j] = (Double) population[i].location().getValue(j);
            }
        }
    }

    public FireflyUsingPair getFinalSolution() {
        return finalSolution.getCopy(minimalize);
    }

    public Double[][][] getLocations() {
        return locations;
    }
}
