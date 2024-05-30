package org.example;

import org.example.algorithms.GeneticAlgorithm;
import org.example.algorithms.GreedyAlgorithm;
import org.example.algorithms.RandomAlgorithm;
import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;
import org.example.generators.JsonGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;



public class Main {
    public static void main(String[] args) throws TooManyProvidersException, IOException, TooManyProductsException {
        RandomAlgorithm randomAlgorithm = new RandomAlgorithm();
        randomAlgorithm.randomAlgorithm();

        int numberOfProducts = 100;
        Map<String, List<String>> products = JsonGenerator.generateProductsWithDelivery(numberOfProducts);
        JsonGenerator.generateProductsWithDelivertJson(products, "src/main/resources/products.json");

        int numberOfProducts2 = 17;
        List<String> basket = JsonGenerator.generateProducts(numberOfProducts2);
        JsonGenerator.generateJsonWithProducts(basket, "src/main/resources/basket.json");



        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.geneticAlgorithm(120, 1000, 80,10,8, "src/main/resources/genetic");

        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm();
        greedyAlgorithm.greedyAlgorithm("src/main/resources/greedy");
    }
}