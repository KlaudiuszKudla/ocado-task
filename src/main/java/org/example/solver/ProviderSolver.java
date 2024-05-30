package org.example.solver;

import java.util.*;

public class ProviderSolver extends LinkedHashSet<Provider> {

    private Provider bestProvider;
    private final TreeSet<Provider> providers;
    private final List<String> products;

    public ProviderSolver(List<String> products, TreeSet<Provider> providers){
        this.products = products;
        bestProvider = new Provider();
        this.providers = providers;
    }

    public Provider getBestProvider() {
        return bestProvider;
    }

    public void findMostCoveringProvider() {
        LinkedHashSet<String> coveredElements = new LinkedHashSet<>();
        providers.stream()
                .map(provider -> new AbstractMap.SimpleEntry<>(provider, providersIntersection(products, provider.getProducts())))
                .filter(entry -> foundMoreCoveredElements(entry.getValue(), coveredElements))
                .forEach(entry -> {
                    bestProvider = entry.getKey();
                    coveredElements.clear();
                    coveredElements.addAll(entry.getValue());
                });
        bestProvider.setProducts(coveredElements);
    }

     private LinkedHashSet<String> providersIntersection(List<String> items, HashSet<String> products) {
        LinkedHashSet<String> setProducts = new LinkedHashSet<>(items);
        setProducts.retainAll(products);
        return setProducts;
    }

    private boolean foundMoreCoveredElements(Set<String> intersection, HashSet<String> coveredElements) {
        return intersection.size() > coveredElements.size();
    }

}
