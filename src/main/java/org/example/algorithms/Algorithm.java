package org.example.algorithms;

import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;
import org.example.solver.Individual;
import org.example.solver.ObjectFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Algorithm {

    protected List<Individual> population;
    protected int size;


    protected void generateRandomIndividuals(int popSize) throws TooManyProvidersException, IOException, TooManyProductsException {
        ObjectFactory objectFactory = new ObjectFactory();
        Individual cachedIndividual = objectFactory.createIndividual();
        if (population == null){
            population = new ArrayList<>();
        }

        for (int i = 0; i < popSize; i++) {
            Individual individual = new Individual(cachedIndividual.getItems(), cachedIndividual.getProviders());
            individual.generateRandomSolution();
            population.add(individual);
        }
    }

    protected List<Individual> shuffleIndividuals(List<Individual> individuals, int sizeOfResults){
        Collections.shuffle(individuals);
        List<Individual> shuffledIndividuals = new ArrayList<>(sizeOfResults);
        for (int i = 0; i < sizeOfResults; i++) {
            shuffledIndividuals.add(individuals.get(i));
        }
        return shuffledIndividuals;
    }

    protected Individual findBestIndividual(List<Individual> individuals){
        Individual bestResult = null;
        var bestScore = Float.MAX_VALUE;
        for (Individual individual: individuals){
            var score = individual.getCost();
            if (score < bestScore){
                bestScore = score;
                bestResult = individual;
            }
        }
        return bestResult;
    }
    protected Individual findWorstIndividual(List<Individual> individuals){
        Individual worstResult = null;
        var worstScore = 0.0;
        for (Individual individual: individuals){
            var currentCost = individual.getCost();
            if (currentCost > worstScore){
                worstScore = currentCost;
                worstResult = individual;
            }
        }
        return worstResult;
    }

    protected double getAverageCost(List<Individual> individuals){
        var averageCost = 0.0;
        for (Individual individual: individuals) averageCost += individual.getCost();
        return averageCost/individuals.size();
    }
}
