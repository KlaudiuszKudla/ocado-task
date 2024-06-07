package org.example.algorithms;

import com.opencsv.CSVWriter;
import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;
import org.example.solver.Individual;
import org.example.solver.ObjectFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GreedyAlgorithm {

    public void greedyAlgorithm(String fileToSave){
        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(fileToSave))){
            String[] headers = {"Iteracja", "Current", "Best", "Worst", "Average"};
            csvWriter.writeNext(headers);
            var bestResult = Float.MIN_VALUE;
            var worstResult = Float.MAX_VALUE;
            long average = 0;
            Individual individual = generateIndividual();
                var currentCost = individual.getCost();
                if (currentCost < bestResult) bestResult = currentCost;
                if (currentCost > worstResult) worstResult = currentCost;
                average+= currentCost;
                String[] row = {String.valueOf(1), String.valueOf(currentCost), String.valueOf(bestResult), String.valueOf(worstResult), String.valueOf(average/(1))};
                csvWriter.writeNext(row);

        } catch (IOException | TooManyProvidersException | TooManyProductsException e) {
            throw new RuntimeException(e);
        }
    }

    protected Individual generateIndividual() throws TooManyProvidersException, IOException, TooManyProductsException {
        ObjectFactory objectFactory = new ObjectFactory();
        Individual individual = objectFactory.createIndividual();
        individual.generateGreedySequenceOfProviders();
        individual.createBasket();
        System.out.println("Greedy provider with maxOccurences" + individual.getMaxOccurences());
        System.out.println("Greedy size of providers " + individual.getUniqueNumberCounter());
        System.out.println(individual.getBasket());
        return individual;
    }
}
