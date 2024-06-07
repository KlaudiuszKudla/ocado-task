package org.example.solver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Item {

    private final String name;
    private List<String> providers;


    public Item(String name, Set<String> providers) {
        this.name = name;
        this.providers = new ArrayList<>(providers);
    }

    public String getName() {
        return name;
    }

    public List<String> getProviders() {
        return providers;
    }
}
