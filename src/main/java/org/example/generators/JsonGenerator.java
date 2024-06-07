package org.example.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonGenerator {

    private static final List<String> DELIVERY_METHODS = Arrays.asList(
            "Pick-up point", "Parcel locker", "Courier", "Same day delivery",
            "Next day shipping", "Mailbox delivery", "In-store pick-up", "Express Collection",
            "Drone delivery", "Bike courier", "Standard post", "Weekend delivery",
            "International shipping", "Economy delivery", "Scheduled delivery",
            "Evening delivery", "Green delivery", "Contactless delivery",
            "Temperature-controlled delivery", "Heavy item delivery", "Pallet delivery",
            "White glove service", "Overnight shipping", "Regional shipping",
            "Bulk shipping"
    );

    public static Map<String, List<String>> generateProductsWithDelivery(int count) {
        Map<String, List<String>> products = new HashMap<>();
        Random random = new Random();

        for (int i = 1; i <= count; i++) {
            String productName = "Product " + i;
            List<String> deliveryMethods = getRandomDeliveryMethods(random);
            products.put(productName, deliveryMethods);
        }

        return products;
    }

    public static List<String> generateProducts(int count) {
        Random random = new Random();
        List<String> products = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String productName = "Product " + i;
            products.add(productName);
        }
        return products;
    }

    private static List<String> getRandomDeliveryMethods(Random random) {
//        int numberOfMethods = random.nextInt(DELIVERY_METHODS.size()) + 1; // At least 1 method
        int numberOfMethods = random.nextInt(8) + 1; // At least 1 method
        Set<String> selectedMethods = new HashSet<>();

        while (selectedMethods.size() < numberOfMethods) {
            String method = DELIVERY_METHODS.get(random.nextInt(DELIVERY_METHODS.size()));
            selectedMethods.add(method);
        }

        return new ArrayList<>(selectedMethods);
    }

    public static void generateProductsWithDelivertJson(Map<String, List<String>> products, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File(fileName), products);
            System.out.println("JSON generated successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateJsonWithProducts(List<String> products, String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File(fileName), products);
            System.out.println("JSON generated successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
