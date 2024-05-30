package org.example.solver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;

import java.io.File;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectFactory {

    private static final int MAX_PRODUCTS_IN_CONFIG_FILE = 100000;
    private static final int MAX_PROVIDERS_IN_CONFIG_FILE = 100000;
    private static final int MAX_PRODUCTS_IN_BASKET = 100000;
    private static final String ABSOLUTE_PATH_TO_CONFIG_FILE = "src/main/resources/products.json";
    private static final String ABSOLUTE_PATH_TO_BASKET_FILE = "src/main/resources/basket.json";
    private final Map<String, List<String>> basket;

    public ObjectFactory() {
        this.basket = new HashMap<>();
    }

    public Map<String, List<String>> split(List<String> products) throws IOException, TooManyProvidersException, TooManyProductsException {
        if (products.size() > MAX_PRODUCTS_IN_BASKET) {
            throw new TooManyProductsException("Przekroczono limi przedmiotów w koszyku (" + MAX_PRODUCTS_IN_BASKET + ")");
        }
        var itemToProvidersMap = fetchProvidersFromFile(products);
        List<Item> items = convertItemToProviderToItem(itemToProvidersMap);
        TreeSet<Provider> providers = findMatchingProviders(products, itemToProvidersMap);

        findSubsets(products, providers);
        return basket;
    }

    public Individual createIndividual() throws IOException, TooManyProvidersException, TooManyProductsException {
        List<String> products = fetchProductsFromFile();
        if (products.size() > MAX_PRODUCTS_IN_BASKET) {
            throw new TooManyProductsException("Przekroczono limi przedmiotów w koszyku (" + MAX_PRODUCTS_IN_BASKET + ")");
        }
        var itemToProvidersMap = fetchProvidersFromFile(products);
        List<Item> items = convertItemToProviderToItem(itemToProvidersMap);
        TreeSet<Provider> providers = findMatchingProviders(products, itemToProvidersMap);
        ArrayList<Provider> providerList = new ArrayList<>(providers);
        Individual individual = new Individual(items, providerList);
        return individual;
    }



    public TreeSet<Provider> findMatchingProviders(List<String> items, Map<String, Set<String>> itemToProvidersMap) {
        Map<String, HashSet<String>> providersToItemMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : itemToProvidersMap.entrySet()) {
            String mapItem = entry.getKey();
            Set<String> availableProviders = entry.getValue();
            for (String item : items) {
                if (item.contains(mapItem)) {
                    for (String provider : availableProviders) {
                        if (!providersToItemMap.containsKey(provider)) {
                            providersToItemMap.put(provider, new HashSet<>());
                        }
                        providersToItemMap.get(provider).add(item);
                    }
                }
            }
        }
        TreeSet<Provider> providers =  convertMapToProvider(providersToItemMap);
        return providers;
    }

    private TreeSet<Provider> convertMapToProvider(Map<String, HashSet<String>> providerToItemsMap) {
        HashSet<Provider> answer =  providerToItemsMap.entrySet().stream()
                .map(entry -> new Provider(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(HashSet::new));
        TreeSet<Provider> providers = new TreeSet<>(answer);
        return providers;
    }

    private List<Item> convertItemToProviderToItem(Map<String, Set<String>> itemToProvidersMap) {
        ArrayList<Item> items =  itemToProvidersMap.entrySet().stream()
                .map(entry -> new Item(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
        return items;
    }

    private void findSubsets(List<String> products, TreeSet<Provider> providers) {
        while (!products.isEmpty()) {
            findBestProvider(products, providers);
        }
    }

    public void findBestProvider(List<String> products, TreeSet<Provider> providersList) {
        ProviderSolver providerSolver = new ProviderSolver(products, providersList);
        providerSolver.findMostCoveringProvider();
        String bestProviderName = providerSolver.getBestProvider().getName();
        List<String> coveredProducts = providerSolver.getBestProvider().getProducts().stream().toList();
        coveredProducts.forEach(products::remove);
        basket.put(bestProviderName, coveredProducts);
    }

    protected Map<String, Set<String>> fetchProvidersFromFile(List<String> items) throws IOException, TooManyProvidersException, TooManyProductsException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Set<String>> itemToProvidersMap = objectMapper.readValue(
                new File(ABSOLUTE_PATH_TO_CONFIG_FILE),
                new TypeReference<Map<String, Set<String>>>() {}
        );
        Map<String, Set<String>> filteredMap = itemToProvidersMap.entrySet().stream()
                .filter(entry -> items.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        checkSizesOfConfigFile(filteredMap);
        return filteredMap;
    }

    protected List<String> fetchProductsFromFile() throws IOException, TooManyProductsException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> products = objectMapper.readValue(new File(ABSOLUTE_PATH_TO_BASKET_FILE), List.class);
        return products;
    }

    protected void checkSizesOfConfigFile(Map<String, Set<String>> itemsToProvidersMap) throws TooManyProductsException, TooManyProvidersException {
        if (itemsToProvidersMap.size() > MAX_PRODUCTS_IN_CONFIG_FILE) {
            throw new TooManyProductsException("Przekroczono limit przedmiotów w pliku konfiguracyjnym (" + MAX_PRODUCTS_IN_CONFIG_FILE + ")");
        }
        for (Map.Entry<String, Set<String>> entry : itemsToProvidersMap.entrySet()) {
            if (entry.getValue().size() > MAX_PROVIDERS_IN_CONFIG_FILE) {
                throw new TooManyProvidersException("Przekroczono limit dostawców (" + MAX_PROVIDERS_IN_CONFIG_FILE + ") dla przedmiotu: " + entry.getKey());
            }
        }
    }


}

