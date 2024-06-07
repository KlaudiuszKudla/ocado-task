package org.example.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Provider implements Comparable<Provider> {
    private final StringBuilder name = new StringBuilder();
    private List<String> products = new ArrayList<>();

    public Provider(String name, HashSet<String> products){
        setName(name);
        this.products.addAll(products);
    }

    public Provider(){}

    public void setName(String name) {
        this.name.replace(0, this.name.length(), name);
    }

    public String getName() {
        return name.toString();
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    @Override
    public int compareTo(Provider other) {
        int sizeComparison = Integer.compare(other.getProducts().size(), this.getProducts().size());

        if (sizeComparison != 0) {
            // Jeśli rozmiary są różne, zwróć wynik porównania rozmiarów
            return sizeComparison;
        } else {
            // W przypadku takiego samego rozmiaru zbiorów produktów, porównaj nazwy dostawców
            return other.getName().compareTo(this.getName());
        }
    }
}
