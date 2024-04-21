package edu.umg.algorithms.synchronous;

import edu.umg.algorithms.synchronous.objects.Firefly;
import edu.umg.helpers.Iteration;
import edu.umg.helpers.Negative;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class FireflyAlgorithm {

    private final double maximalAttractiveness;
    private final double lightAbsorptionCoefficient;
    private final double randomStepReductionCoefficient;
    private final int populationSize;
    private final int maximumNumberOfGenerations;
    private final Iteration[] iterations;
    private final Double[][][] locations;
    private final Function<Double[], Double> objectiveFunction;
    private final int numberOfDimensions;
    private final boolean minimalize;
    private final double lowerBound;
    private final double upperBound;
    private double randomStepCoefficient;
    private Firefly[] population;
    private int locationOfTheBestSolution;
    private Firefly theBestSolution;
    private Firefly finalSolution;

    public FireflyAlgorithm(
        double maximalAttractiveness,
        double lightAbsorptionCoefficient,
        double randomStepCoefficient,
        double randomStepReductionCoefficient,
        int populationSize,
        int maximumNumberOfGenerations,
        Function<Double[], Double> objectiveFunction,
        int numberOfDimensions,
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
        this.numberOfDimensions = numberOfDimensions;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.minimalize = minimalize;
        this.iterations = new Iteration[maximumNumberOfGenerations];
        this.locations = new Double[maximumNumberOfGenerations +
        1][populationSize][numberOfDimensions];
    }

    public Iteration[] run() {
        initializePopulation();
        findTheBestSolution();
        addLocationAt(0);

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

                    if (population[i].getIntensity() > finalSolution.getIntensity()) {
                        finalSolution = new Firefly(
                            theBestSolution,
                            objectiveFunction,
                            minimalize
                        );
                    }
                }
            }

            theBestSolution.setLocation(computeNewLocation());
            reduceRandomStepCoefficient();
            addIteration(n);
            addLocationAt(n + 1);
        }

        return iterations;
    }

    private void initializePopulation() {
        population = new Firefly[populationSize];

        for (int i = 0; i < populationSize; i++) {
            population[i] = new Firefly(
                objectiveFunction,
                numberOfDimensions,
                lowerBound,
                upperBound,
                minimalize
            );
        }
    }

    private void findTheBestSolution() {
        locationOfTheBestSolution = 0;
        theBestSolution = population[0];

        for (int i = 1; i < populationSize; i++) {
            if (
                population[i].getIntensity() >
                population[locationOfTheBestSolution].getIntensity()
            ) {
                locationOfTheBestSolution = i;
                theBestSolution = population[locationOfTheBestSolution];
            }
        }

        finalSolution = new Firefly(theBestSolution, objectiveFunction, minimalize);
    }

    private double distanceBetweenFireflies(int index1, int index2) {
        double result = 0;

        for (int i = 0; i < numberOfDimensions; i++) {
            result +=
            Math.pow(
                population[index1].getLocationAt(i) - population[index2].getLocationAt(i),
                2
            );
        }

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

    private Double[] computeNewLocation(int index1, int index2) {
        double attractiveness = computeAttractiveness(index1, index2);
        Double[] newLocation = new Double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            newLocation[i] = population[index1].getLocationAt(i) +
            attractiveness *
                (population[index2].getLocationAt(i) -
                    population[index1].getLocationAt(i)) +
            computeRandomStep();

            if (newLocation[i] < lowerBound) {
                newLocation[i] = lowerBound;
            }

            if (newLocation[i] > upperBound) {
                newLocation[i] = upperBound;
            }
        }

        return newLocation;
    }

    private double computeRandomStep() {
        return randomStepCoefficient * (ThreadLocalRandom.current().nextDouble() - 0.5);
    }

    private Double[] computeNewLocation() {
        Double[] newLocation = new Double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            newLocation[i] = population[locationOfTheBestSolution].getLocationAt(i) +
            computeRandomStep();

            if (newLocation[i] < lowerBound) {
                newLocation[i] = lowerBound;
            }

            if (newLocation[i] > upperBound) {
                newLocation[i] = upperBound;
            }
        }

        return newLocation;
    }

    private void reduceRandomStepCoefficient() {
        randomStepCoefficient *= randomStepReductionCoefficient;
    }

    private void addIteration(int index) {
        double result = 0;

        Negative Negative = new Negative();

        for (Firefly firefly : population) {
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
            for (int j = 0; j < numberOfDimensions; j++) {
                locations[index][i][j] = population[i].getLocationAt(j);
            }
        }
    }

    public Firefly getFinalSolution() {
        return finalSolution;
    }

    public Double[][][] getLocations() {
        return locations;
    }
}
