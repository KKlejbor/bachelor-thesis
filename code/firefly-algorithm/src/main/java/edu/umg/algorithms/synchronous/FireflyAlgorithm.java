package edu.umg.algorithms.synchronous;

import edu.umg.objects.Firefly;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class FireflyAlgorithm {
    private final double maximalAttractiveness;

    private final double lightAbsorptionCoefficient;
    private double randomStepCoefficient;

    private final double randomStepReductionCoefficient;

    private final int populationSize;

    private final int maximumNumberOfGenerations;

    private Firefly[] population;

    private int locationOfTheBestSolution;

    private Firefly theBestSolution;

    private final Function<Double[], Double> objectiveFunction;

    private final int numberOfDimensions;

    private final double lowerBound;
    private final double upperBound;

    public FireflyAlgorithm(double maximalAttractiveness, double lightAbsorptionCoefficient, double randomStepCoefficient, double randomStepReductionCoefficient, int populationSize, int maximumNumberOfGenerations , Function<Double[], Double> objectiveFunction, int numberOfDimensions, double lowerBound, double upperBound) {
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
    }

    public Firefly run(){
        initializePopulation();
        findTheBestSolution();

        for (int n = 0; n < maximumNumberOfGenerations; n++) {
            for (int i = 0; i < populationSize; i++) {
                for (int j = 0; j < populationSize; j++) {
                    if(population[i].getIntensity() < population[j].getIntensity()){
                        population[i].setLocation(computeNewLocation(i, j));
                    }
                }

                if(population[i].getIntensity() > theBestSolution.getIntensity()){
                    theBestSolution = population[i];
                    locationOfTheBestSolution = i;
                }
            }

            theBestSolution.setLocation(computeNewLocation());
            reduceRandomStepCoefficient();
        }

        return population[locationOfTheBestSolution];
    }

    private void initializePopulation(){
        population = new Firefly[populationSize];

        for (int i = 0; i < populationSize; i++) {
            population[i] = new Firefly(objectiveFunction, numberOfDimensions, lowerBound, upperBound);
        }
    }

    private void findTheBestSolution(){
        locationOfTheBestSolution = 0;
        theBestSolution = population[0];

        for (int i = 1; i < populationSize; i++) {
            if(population[i].getIntensity() > theBestSolution.getIntensity()){
                locationOfTheBestSolution = i;
                theBestSolution = population[locationOfTheBestSolution];
            }
        }
    }

    private double distanceBetweenFireflies(int index1, int index2){
        double result = 0;

        for (int i = 0; i < numberOfDimensions; i++) {
            result += Math.pow(population[index1].getLocationAt(i) - population[index2].getLocationAt(i), 2);
        }

        return Math.sqrt(result);
    }

    private double computeAttractiveness(int index1, int index2){
        return maximalAttractiveness * Math.exp(-lightAbsorptionCoefficient * Math.pow(distanceBetweenFireflies(index1, index2), 2));
    }

    private Double[] computeNewLocation(int index1, int index2){
        double attractiveness = computeAttractiveness(index1, index2);
        Double[] newLocation = new Double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            newLocation[i] = population[index1].getLocationAt(i) + attractiveness * (population[index2].getLocationAt(i) - population[index1].getLocationAt(i)) + computeRandomStep();
        }

        return newLocation;
    }

    private double computeRandomStep(){
        return randomStepCoefficient * (ThreadLocalRandom.current().nextDouble() - 0.5);
    }

    private Double[] computeNewLocation(){
        Double[] newLocation = new Double[numberOfDimensions];

        for (int i = 0; i < numberOfDimensions; i++) {
            newLocation[i] = population[locationOfTheBestSolution].getLocationAt(i) + computeRandomStep();
        }

        return newLocation;
    }

    private void reduceRandomStepCoefficient(){
        randomStepCoefficient *= randomStepReductionCoefficient;
    }

}
