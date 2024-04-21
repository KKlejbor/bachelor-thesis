package edu.umg.algorithms.synchronous;

import edu.umg.helpers.Negative;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class FireflyAlgorithm {
    private final double maximalAttractiveness;

    private final double lightAbsorptionCoefficient;
    private final double reductionCoefficient;
    private final int populationSize;
    private final int maximumNumberOfGenerations;
    private final Function<Double[], Double> objectiveFunction;
    private final int numberOfDimensions;
    private final double lowerBound;
    private final double upperBound;
    private double randomStepCoefficient;
    private Double[][] population;
    private Double[] intensities;
    private int locationOfCurrentBestSolution;
    private Double[] currentBestSolution;
    private Double currentBestIntensity;
    private Double[] theBestSolution;
    private Double theBestIntensity;

    public FireflyAlgorithm(double maximalAttractiveness, double lightAbsorptionCoefficient, double randomStepCoefficient, double reductionCoefficient, int populationSize, int maximumNumberOfGenerations, Function<Double[], Double> objectiveFunction, int numberOfDimensions, double lowerBound, double upperBound, boolean minimalize) {
        this.maximalAttractiveness = maximalAttractiveness;
        this.lightAbsorptionCoefficient = lightAbsorptionCoefficient;
        this.randomStepCoefficient = randomStepCoefficient;
        this.reductionCoefficient = reductionCoefficient;
        this.populationSize = populationSize;
        this.maximumNumberOfGenerations = maximumNumberOfGenerations;
        this.objectiveFunction = minimalize ? objectiveFunction.andThen(new Negative()) : objectiveFunction;
        this.numberOfDimensions = numberOfDimensions;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Double[] run() {
        initializePopulation();
        findTheBestSolution();

        for (int n = 0; n < maximumNumberOfGenerations; n++) {
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < populationSize; j++) {
                    if (intensities[i] < intensities[j]) {
                        population[i] = computeNewLocation(i, j);
                    }
                }

                intensities[i] = objectiveFunction.apply(population[i]);

                if (intensities[i] > currentBestIntensity) {
                    System.arraycopy(population[i], 0, currentBestSolution, 0, numberOfDimensions);;
                    locationOfCurrentBestSolution = i;
                    currentBestIntensity = objectiveFunction.apply(currentBestSolution);

                    if (currentBestIntensity > theBestIntensity) {
                        System.arraycopy(currentBestSolution, 0, theBestSolution, 0, numberOfDimensions);
                        theBestIntensity = objectiveFunction.apply(theBestSolution);
                    }
                }
            }

            currentBestSolution = computeNewLocation();
            currentBestIntensity = objectiveFunction.apply(currentBestSolution);
            reduceRandomStepCoefficient();
        }

        return theBestSolution;
    }

    private void initializePopulation() {
        population = new Double[populationSize][];
        intensities = new Double[populationSize];

        for (int i = 0; i < populationSize; i++) {
            population[i] = new Double[numberOfDimensions];

            for (int j = 0; j < population[i].length; j++) {
                population[i][j] = ThreadLocalRandom.current().nextDouble(lowerBound, upperBound);
            }

            intensities[i] = objectiveFunction.apply(population[i]);
        }
    }

    private void findTheBestSolution() {
        locationOfCurrentBestSolution = 0;
        currentBestSolution = population[0];
        theBestSolution = new Double[numberOfDimensions];

        for (int i = 1; i < populationSize; i++) {
            if (intensities[i] > intensities[locationOfCurrentBestSolution]) {
                locationOfCurrentBestSolution = i;
                currentBestSolution = population[locationOfCurrentBestSolution];
            }
        }

        System.arraycopy(population[locationOfCurrentBestSolution], 0, currentBestSolution, 0, numberOfDimensions);
        System.arraycopy(population[locationOfCurrentBestSolution], 0, theBestSolution, 0, numberOfDimensions);
        currentBestIntensity = objectiveFunction.apply(currentBestSolution);
        theBestIntensity = objectiveFunction.apply(theBestSolution);
    }

    private double distanceBetweenFireflies(int index1, int index2) {
        double result = 0;

        for (int i = 0; i < numberOfDimensions; i++) {
            result += Math.pow(population[index1][i] - population[index2][i], 2);
        }

        return result;
    }

    private double computeAttractiveness(int index1, int index2) {
        return maximalAttractiveness * Math.exp(-lightAbsorptionCoefficient * distanceBetweenFireflies(index1, index2));
    }

    private Double[] computeNewLocation(int index1, int index2) {
        double attractiveness = computeAttractiveness(index1, index2);
        Double[] newLocation = new Double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            newLocation[i] = population[index1][i] + attractiveness * (population[index2][i] - population[index1][i]) + computeRandomStep();

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
            newLocation[i] = population[locationOfCurrentBestSolution][i] + computeRandomStep();

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
        randomStepCoefficient *= reductionCoefficient;
    }

}
