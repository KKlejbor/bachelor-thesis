package edu.umg.algorithms.synchronous;

import edu.umg.algorithms.synchronous.objects.Firefly;
import edu.umg.algorithms.synchronous.objects.FireflyUsingPair;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import org.javatuples.Pair;

public class FireflyAlgorithmUsingPair {

    private final double maximalAttractiveness;

    private final double lightAbsorptionCoefficient;

    private double randomStepCoefficient;

    private final double randomStepReductionCoefficient;

    private final int populationSize;

    private final int maximumNumberOfGenerations;

    private FireflyUsingPair[] population;

    private int locationOfTheBestSolution;

    private FireflyUsingPair theBestSolution;

    private FireflyUsingPair finalSolution;

    private final Function<Pair<Double, Double>, Double> objectiveFunction;

    private final boolean minimalize;

    private final double lowerBound;

    private final double upperBound;

    public FireflyAlgorithmUsingPair(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double randomStepCoefficient,
        double randomStepReductionCoefficient,
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
        this.randomStepReductionCoefficient = randomStepReductionCoefficient;
        this.populationSize = populationSize;
        this.maximumNumberOfGenerations = maximumNumberOfGenerations;
        this.objectiveFunction = objectiveFunction;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.minimalize = minimalize;
    }

    public FireflyUsingPair run() {
        initializePopulation();
        findTheBestSolution();

        for (int n = 0; n < maximumNumberOfGenerations; n++) {
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < populationSize; j++) {
                    if (population[i].getIntensity() < population[j].getIntensity()) {
                        population[i].setLocation(computeNewLocation(i, j));
                    }
                }

                if (population[i].getIntensity() > theBestSolution.getIntensity()) {
                    theBestSolution = population[i];
                    locationOfTheBestSolution = i;

                    if (theBestSolution.getIntensity() > finalSolution.getIntensity()) {
                        finalSolution = new FireflyUsingPair(
                            theBestSolution,
                            objectiveFunction,
                            minimalize
                        );
                    }
                }
            }

            theBestSolution.setLocation(computeNewLocation());
            reduceRandomStepCoefficient();
        }

        return finalSolution;
    }

    private void initializePopulation() {
        population = new FireflyUsingPair[populationSize];

        for (int i = 0; i < populationSize; i++) {
            population[i] = new FireflyUsingPair(
                objectiveFunction,
                lowerBound,
                upperBound,
                minimalize
            );
        }
    }

    private void findTheBestSolution() {
        locationOfTheBestSolution = 0;
        theBestSolution = population[0];
        finalSolution = new FireflyUsingPair(
            theBestSolution,
            objectiveFunction,
            minimalize
        );

        for (int i = 1; i < populationSize; i++) {
            if (population[i].getIntensity() > theBestSolution.getIntensity()) {
                locationOfTheBestSolution = i;
                theBestSolution = population[locationOfTheBestSolution];
            }
        }
    }

    private double distanceBetweenFireflies(int index1, int index2) {
        double result = Math.pow(
            population[index1].getLocationAt(0) - population[index2].getLocationAt(0),
            2
        );
        result +=
        Math.pow(
            population[index1].getLocationAt(1) - population[index2].getLocationAt(1),
            2
        );

        return Math.sqrt(result);
    }

    private double computeAttractiveness(int index1, int index2) {
        return (
            maximalAttractiveness *
            Math.exp(
                -lightAbsorptionCoefficient *
                Math.pow(distanceBetweenFireflies(index1, index2), 2)
            )
        );
    }

    private Pair<Double, Double> computeNewLocation(int index1, int index2) {
        double attractiveness = computeAttractiveness(index1, index2);

        double first =
            population[index1].getLocationAt(0) +
            attractiveness *
                (population[index2].getLocationAt(0) -
                    population[index1].getLocationAt(0)) +
            computeRandomStep();
        double second =
            population[index1].getLocationAt(1) +
            attractiveness *
                (population[index2].getLocationAt(1) -
                    population[index1].getLocationAt(1)) +
            computeRandomStep();

        return createNewPair(first, second);
    }

    private double computeRandomStep() {
        return randomStepCoefficient * (ThreadLocalRandom.current().nextDouble() - 0.5);
    }

    private Pair<Double, Double> computeNewLocation() {
        double first =
            population[locationOfTheBestSolution].getLocationAt(0) + computeRandomStep();
        double second =
            population[locationOfTheBestSolution].getLocationAt(1) + computeRandomStep();

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
        randomStepCoefficient *= randomStepReductionCoefficient;
    }
}
