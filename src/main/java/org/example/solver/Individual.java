package org.example.solver;

import java.util.*;

public class Individual {

    private static List<Provider> providers;
    private static List<Item> items;
    private List<Integer> sequenceOfProviders;
    private float cost;
    private static int size;

    public Individual(List<Item> items, List<Provider> providers) {
        this.items = items;
        this.providers = providers;
        sequenceOfProviders = new ArrayList<>();
        this.size = items.size();
    }

    public Individual(List<Integer> sequenceOfProviders) {
        this.sequenceOfProviders = sequenceOfProviders;
    }

    public void generateRandomSolution() {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            int value = rand.nextInt(providers.size());
            sequenceOfProviders.add(value);
        }
        repairSolution();
        calculateCost();
    }

    public void calculateCost() {
        Map<Integer, Integer> distinctValues = new HashMap<>();
        int uniqueNumbersCount = 0;
        int maxOccurrences = 0;
        for (int i = 0; i < size; i++) {
            int number = sequenceOfProviders.get(i);
            distinctValues.put(number, distinctValues.getOrDefault(number, 0) + 1);
        }
        uniqueNumbersCount = distinctValues.size();
        for (Map.Entry<Integer, Integer> entry : distinctValues.entrySet()) {
            if (entry.getValue() > maxOccurrences) {
                maxOccurrences = entry.getValue();
            }
        }
        this.cost = ((float) 1 / uniqueNumbersCount) * maxOccurrences;
    }

    public void swapMutationV2() {
        var random = new Random();
        var firstIndex = random.nextInt(size);
        var secondIndex = random.nextInt(size);
        while (firstIndex == secondIndex) {
            secondIndex = random.nextInt(size);
        }
        var firstCity = this.sequenceOfProviders.get(firstIndex);
        sequenceOfProviders.set(firstIndex, sequenceOfProviders.get(secondIndex));
        sequenceOfProviders.set(secondIndex, firstCity);
        repairSolution();
        calculateCost();
    }

    public void repairSolution() {
        for (int i = 0; i < size; i++) {
            int providerNumber = sequenceOfProviders.get(i);
            Provider provider = providers.get(providerNumber);
            Item item = items.get(i);
            if (!isSolutionValid(item, provider)) {
                sequenceOfProviders.set(i, findMostCoveredProviderNumber(item));
            }
        }
    }

    public void generateGreedySequenceOfProviders() {
        for (int i = 0; i < size; i++) {
            Item item = items.get(i);
            sequenceOfProviders.add(findMostCoveredProviderNumber(item));
        }
        calculateCost();
    }

    private int findMostCoveredProviderNumber(Item item) {
        int providerNumber = 0;
        for (int i = 0; i < providers.size(); i++) {
            if (item.getProviders().contains(providers.get(i).getName())) {
                providerNumber = i;
                break;
            }
        }
        return providerNumber;
    }

    private boolean isSolutionValid(Item item, Provider provider) {
        return item.getProviders().contains(provider.getName());
    }

    public float getCost() {
        return cost;
    }

    public List<Integer> getSequenceOfProviders() {
        return sequenceOfProviders;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public List<Item> getItems() {
        return items;
    }

}
