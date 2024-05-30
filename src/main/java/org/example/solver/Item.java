package org.example.solver;

import java.util.LinkedHashSet;
import java.util.Set;

public class Item {

    private final String name;
    private LinkedHashSet<String> providers;


    public Item(String name, Set<String> providers) {
        this.name = name;
        this.providers = new LinkedHashSet<>(providers);
    }

    public String getName() {
        return name;
    }

    public LinkedHashSet<String> getProviders() {
        return providers;
    }
}
