package org.example;

import org.example.algorithms.GeneticAlgorithm;
import org.example.algorithms.GreedyAlgorithm;
import org.example.algorithms.RandomAlgorithm;
import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;
import org.example.generators.JsonGenerator;
import org.example.solver.ObjectFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Main {
    public static void main(String[] args) throws TooManyProvidersException, IOException, TooManyProductsException {
        RandomAlgorithm randomAlgorithm = new RandomAlgorithm();
        randomAlgorithm.randomAlgorithm();

        int numberOfProducts = 1000;
        Map<String, List<String>> products = JsonGenerator.generateProductsWithDelivery(numberOfProducts);
        Map<String, List<String>> products2 = new HashMap<>();
        products2.put("Product 1", Arrays.asList("Courier", "Parcel locker", "Pallet delivery", "Contactless delivery"));
        products2.put("Product 2", Arrays.asList("Courier", "Overnight shipping", "Regional shipping", "Green delivery"));
        products2.put("Product 3", Arrays.asList("Parcel locker", "In-store pick-up", "Temperature-controlled delivery","Standard post"));
        products2.put("Product 4", Arrays.asList("In-store pick-up", "White glove service", "Weekend delivery"));
        products2.put("Product 5", Arrays.asList("Courier", "Pick-up point", "Bulk shipping", "Standard post"));



//
//        "Pick-up point", "Parcel locker", "Courier", "Same day delivery",
//                "Next day shipping", "Mailbox delivery", "In-store pick-up", "Express Collection",
//                "Drone delivery", "Bike courier", "Standard post", "Weekend delivery",
//                "International shipping", "Economy delivery", "Scheduled delivery",
//                "Evening delivery", "Green delivery", "Contactless delivery",
//                "Temperature-controlled delivery", "Heavy item delivery", "Pallet delivery",
//                "White glove service", "Overnight shipping", "Regional shipping",
//                "Bulk shipping"

        JsonGenerator.generateProductsWithDelivertJson(products, "src/main/resources/products.json");

        List<String> basket = JsonGenerator.generateProducts(numberOfProducts);
        JsonGenerator.generateJsonWithProducts(basket, "src/main/resources/basket.json");
        ObjectFactory objectFactory = new ObjectFactory();
        System.out.println("Zachłanny: " + objectFactory.split(basket));

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        geneticAlgorithm.geneticAlgorithm(120, 1000, 80,10,8, "src/main/resources/genetic.csv");

        GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm();
        greedyAlgorithm.greedyAlgorithm("src/main/resources/greedy.csv");
    }
}