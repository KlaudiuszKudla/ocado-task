package org.example.solver;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class Provider {
    private final StringBuilder name = new StringBuilder();
    private LinkedHashSet<String> products = new LinkedHashSet<>();

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

    public LinkedHashSet<String> getProducts() {
        return products;
    }

    public void setProducts(LinkedHashSet<String> products) {
        this.products = products;
    }
}
