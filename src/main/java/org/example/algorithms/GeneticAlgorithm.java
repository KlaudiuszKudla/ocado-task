package org.example.algorithms;

import com.opencsv.CSVWriter;
import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;
import org.example.solver.Individual;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GeneticAlgorithm extends Algorithm{

    public void geneticAlgorithm(int popSize, int generations, int crossProbability, int mutationProbability, int tourSize, String fileToSave) throws TooManyProvidersException, TooManyProductsException {

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave))) {
            String[] headers = {"Iteracja", "Best Result", "Worst Result", "Average Cost"};
            writer.writeNext(headers);
            generateRandomIndividuals(popSize);
            Individual currentBestIndividual = null;
            for (int i = 0; i < generations; i++) {
                List<Individual> tournamentWinners = findTournamentWinners(population, 14, tourSize);
                crossTournamentWinners(tournamentWinners, crossProbability, population, mutationProbability);
                mutatePopulation(population, mutationProbability);
                currentBestIndividual = findBestIndividual(population);
                var worstResult = findWorstIndividual(population).getCost();
                var averageCost = getAverageCost(population);
                String[] row = {String.valueOf(i + 1), String.valueOf(currentBestIndividual.getCost()), String.valueOf(worstResult), String.valueOf(averageCost)};
                writer.writeNext(row);
                currentBestIndividual.createBasket();
                System.out.println("Genetic provider with maxOccurences" + currentBestIndividual.getMaxOccurences());
                System.out.println("Genetic size of Providers" + currentBestIndividual.getUniqueNumberCounter());
                System.out.println(currentBestIndividual.getBasket());
            }
            currentBestIndividual.createBasket();
            System.out.println("Genetic provider with maxOccurences" + currentBestIndividual.getMaxOccurences());
            System.out.println("Genetic size of Providers" + currentBestIndividual.getUniqueNumberCounter());
            System.out.println(currentBestIndividual.getBasket());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private List<Individual> findTournamentWinners(List<Individual> population, int times, int tourSize) {
        Set<Individual> bestResults = new HashSet<>();
        while(bestResults.size() < times) {
            List<Individual> tournamentParticipants = shuffleIndividuals(population, tourSize);
            var bestResult = findBestIndividual(tournamentParticipants);
            bestResults.add(bestResult);
        }
        return new ArrayList<>(bestResults);
    }

    private void crossTournamentWinners(List<Individual> tournamentWinners, int crossProbability, List<Individual> population, int mutationProbability){
        var sizeOfTournamentWinners = tournamentWinners.size();
        Random random = new Random();
        List<Individual> newPopulation = new ArrayList<>();
        while (newPopulation.size() != population.size()) {
            var randomIndex = random.nextInt(sizeOfTournamentWinners);
            var secondRandomIndex = random.nextInt(sizeOfTournamentWinners);
            Individual parent1 = tournamentWinners.get(randomIndex);
            while(randomIndex == secondRandomIndex){
                secondRandomIndex = random.nextInt(sizeOfTournamentWinners);
            }
            Individual parent2 = tournamentWinners.get(secondRandomIndex);
            if (shouldCross(crossProbability)) {
                Individual child1 = orderedCrossover(parent1, parent2);
                Individual child2 = orderedCrossover(parent2,parent1);
                newPopulation.add(child1);
                newPopulation.add(child2);
            } else {
                newPopulation.add(parent1);
                newPopulation.add(parent2);
            }
        }
        population.clear();
        population.addAll(newPopulation);
    }

    public Individual orderedCrossover(Individual unit1, Individual unit2){
        java.util.Random rand = new Random();
        var sequenceOfProviders = unit1.getSequenceOfProviders();
        var sequenceOfProviders1 = unit2.getSequenceOfProviders();

        var newSequenceOfProviders = new ArrayList<Integer>(sequenceOfProviders);
        var size = sequenceOfProviders.size();
        var firstcut = rand.nextInt(size);
        var secondCut = rand.nextInt(firstcut, size);
        if (secondCut<size-1){
            for (int i = secondCut+1; i <size ; i++) {
                var provider = sequenceOfProviders1.get(i);
                newSequenceOfProviders.set(i, provider);
            }
        }
        if (firstcut>0){
            for (int i = 0; i <firstcut ; i++) {
                var provider = sequenceOfProviders1.get(i);
                newSequenceOfProviders.set(i, provider);
            }
        }
        Individual newIdividual = new Individual(newSequenceOfProviders);
        newIdividual.repairSolution();
        newIdividual.calculateCost();
        return newIdividual;
    }

    public void mutatePopulation(List<Individual> population, int mutationProbability) {
        population.stream()
                .filter(individual -> shouldMutate(mutationProbability))
                .forEach(individual -> individual.swapMutationV2());
    }

    private boolean shouldCross(int crossProbability){
        java.util.Random rand = new java.util.Random();
        int randomValue = rand.nextInt(100);
        return randomValue < crossProbability;
    }

    private boolean shouldMutate(int mutationProbability) {
        java.util.Random rand = new Random();
        int randomValue = rand.nextInt(100);
        return randomValue < mutationProbability;
    }




}
