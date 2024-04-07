package org.example.solver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exceptions.TooManyProductsException;
import org.example.exceptions.TooManyProvidersException;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasketSplitter {

    private static final int MAX_PRODUCTS_IN_CONFIG_FILE = 1000;
    private static final int MAX_PROVIDERS_IN_CONFIG_FILE = 10;
    private static final int MAX_PRODUCTS_IN_BASKET = 100;
    private final String absolutePathToConfigFile;
    private final Map<String, List<String>> basket;

    public BasketSplitter(String absolutePathToConfigFile) {
        this.absolutePathToConfigFile = absolutePathToConfigFile;
        this.basket = new HashMap<>();
    }

    public Map<String, List<String>> split(List<String> items) throws IOException, TooManyProvidersException, TooManyProductsException {
        if (items.size() > MAX_PRODUCTS_IN_BASKET){
            throw new TooManyProductsException("Przekroczono limi przedmiotów w koszyku (" + MAX_PRODUCTS_IN_BASKET + ")");
        }
        var itemProviders = fetchDataFromFile(this.absolutePathToConfigFile);
        List<Provider> providersItem = findMatchingProviders(items, itemProviders);
        HashSet<String> products = new HashSet<>(items);
        findSubsets(products, providersItem);
        return basket;
    }

    public List<Provider> findMatchingProviders(List<String> items, Map<String, List<String>> itemProviders){
        Map<String, HashSet<String>> providersItem = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : itemProviders.entrySet()) {
            String mapItem = entry.getKey();
            List<String> availableProviders = entry.getValue();
            for (String item : items) {
                if (item.contains(mapItem)) {
                    for (String provider: availableProviders){
                        if (!providersItem.containsKey(provider)) {
                            providersItem.put(provider, new HashSet<>());
                        }
                        providersItem.get(provider).add(item);
                    }
                }
            }
        }
        return convertMapToProvider(providersItem);
    }

    private List<Provider> convertMapToProvider(Map<String, HashSet<String>> providerItems) {
        return providerItems.entrySet().stream()
                .map(entry -> new Provider(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
    private void findSubsets(HashSet<String> products, List<Provider> providers) {
        while(!products.isEmpty()){
            findBestProvider(products, providers);
        }
    }

    public void findBestProvider(HashSet<String> products, List<Provider> providersList){
        ProviderSolver providerSolver = new ProviderSolver(products, providersList);
        providerSolver.findMostCoveringProvider();
        String bestProviderName = providerSolver.getBestProvider().getName();
        List<String> coveredProducts = providerSolver.getBestProvider().getProducts().stream().toList();
        coveredProducts.forEach(products::remove);
        basket.put(bestProviderName, coveredProducts);
    }

    protected Map<String, List<String>> fetchDataFromFile(String absolutePathToConfigFile) throws IOException, TooManyProvidersException, TooManyProductsException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, List<String>> itemsToProvidersMap = objectMapper.readValue(new File(absolutePathToConfigFile), Map.class);
        checkSizesOfConfigFile(itemsToProvidersMap);
        return itemsToProvidersMap;
    }

     protected void checkSizesOfConfigFile(Map<String, List<String>> itemsToProvidersMap) throws TooManyProductsException, TooManyProvidersException {
        if (itemsToProvidersMap.size() > MAX_PRODUCTS_IN_CONFIG_FILE) {
            throw new TooManyProductsException("Przekroczono limit przedmiotów w pliku konfiguracyjnym (" + MAX_PRODUCTS_IN_CONFIG_FILE + ")");
        }
        for (Map.Entry<String, List<String>> entry : itemsToProvidersMap.entrySet()) {
            if (entry.getValue().size() > MAX_PROVIDERS_IN_CONFIG_FILE) {
                throw new TooManyProvidersException("Przekroczono limit dostawców (" + MAX_PROVIDERS_IN_CONFIG_FILE + ") dla przedmiotu: " + entry.getKey());
            }
        }
    }


}

