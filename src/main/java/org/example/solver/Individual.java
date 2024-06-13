package org.example.solver;

import java.util.*;

public class Individual implements Cloneable{

    private static List<Provider> providers;
    private static List<Item> items;
    private List<Integer> sequenceOfProviders;
    private int uniqueNumberCounter = 0;
    private int maxOccurences = 0;
    private float cost;
    private static int size;
    private Map<String, List<String>> basket;

    public Individual(List<Item> items, List<Provider> providers) {
        this.items = items;
        this.providers = providers;
        sequenceOfProviders = new ArrayList<>();
        this.size = items.size();
    }

    public Individual(Individual other) {
        this.providers = other.providers;
        this.items = other.items;
        this.sequenceOfProviders = other.sequenceOfProviders;
        this.uniqueNumberCounter = other.uniqueNumberCounter;
        this.maxOccurences = other.maxOccurences;
        this.cost = other.cost;
        this.basket = other.basket;
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
        this.maxOccurences = maxOccurrences;
        this.uniqueNumberCounter = uniqueNumbersCount;
        this.cost = ((float) 100 / maxOccurrences) * uniqueNumbersCount;
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
        repairSolutionV2();
        calculateCost();
    }

    public void repairSolution() {
        for (int i = 0; i < size; i++) {
            int providerNumber = sequenceOfProviders.get(i);
            Provider provider = providers.get(providerNumber);
            Item item = items.get(i);
            if (!isSolutionValid(item, provider)) {
                sequenceOfProviders.set(i, findRandomProviderNumber(item));
            }
        }
    }

    public void repairSolutionV2() {
        for (int i = 0; i < size; i++) {
            int providerNumber = sequenceOfProviders.get(i);
            Provider provider = providers.get(providerNumber);
            Item item = items.get(i);
            if (!isSolutionValid(item, provider)) {
                sequenceOfProviders.set(i, findMostCoveredProviderNumberV2(item));
            }
        }
    }

    public void generateGreedySequenceOfProviders() {
        for (int i = 0; i < size; i++) {
            Item item = items.get(i);
            sequenceOfProviders.add(findMostCoveredProviderNumberV2(item));
        }
        calculateCost();
    }

    public void createBasket(){
        basket = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int providerNumber = sequenceOfProviders.get(i);
            Provider provider = providers.get(providerNumber);
            Item item = items.get(i);
            List<String> itemList = basket.getOrDefault(provider.getName(), new ArrayList<>());
            itemList.add(item.getName());
            basket.put(provider.getName(), itemList);
        }
    }

    private int findMostCoveredProviderNumber(Item item) {
        int providerNumber = 0;
        Random random = new Random();
        for (int i = 0; i < providers.size(); i++) {
            if (providers.get(i).getProducts().contains(item.getName())) {
                if (providers.get(i+1).getProducts().contains(item.getName())) {
                    providerNumber = random.nextInt(i,i+2);
                }else{
                providerNumber = i;
                }
                break;
            }
        }
        return providerNumber;
    }

    private int findMostCoveredProviderNumberV2(Item item) {
        int providerNumber = 0;
        for (int i = 0; i < providers.size(); i++) {
            if (providers.get(i).getProducts().contains(item.getName())) {
                providerNumber = i;
                break;
            }
        }
        return providerNumber;
    }




    private int findRandomProviderNumber(Item item) {
        int providerNumber = 0;
        Random random = new Random();
        int itemProvidersSize =  item.getProviders().size();
        var rand = random.nextInt(itemProvidersSize);
        String providerName = item.getProviders().get(rand);
        for (int i = 0; i < providers.size(); i++) {
            if(providerName.equals(providers.get(i).getName())) {
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

    public Map<String, List<String>> getBasket() {
        return basket;
    }

    public int getUniqueNumberCounter() {
        return uniqueNumberCounter;
    }

    public int getMaxOccurences() {
        return maxOccurences;
    }

    @Override
    public Individual clone() {
        try {
            Individual clone = (Individual) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
