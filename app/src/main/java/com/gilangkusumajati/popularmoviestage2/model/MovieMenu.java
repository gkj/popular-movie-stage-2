package com.gilangkusumajati.popularmoviestage2.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gilang Kusuma Jati on 7/29/17.
 */

public enum MovieMenu {
    POPULAR ("Most Popular"),
    TOP_RATED ("Highest Rated"),
    FAVORITE ("Favorite");

    private String name;

    MovieMenu(String s) {
        name = s;
    }

    public boolean equals(String otherName) {
        return name.toString().equals(otherName);
    }

    public boolean equals(MovieMenu other) {
        return name.toString().equals(other.toString());
    }

    public String toString() {
        return this.name;
    }

    public static Map<String, MovieMenu> typeMapping = new LinkedHashMap<>();
    static {
        typeMapping.put(POPULAR.toString(), POPULAR);
        typeMapping.put(TOP_RATED.toString(), TOP_RATED);
        typeMapping.put(FAVORITE.toString(), FAVORITE);
    }

    public static MovieMenu getType(String typeName) {
        if (typeMapping.get(typeName) == null) {
            throw new RuntimeException(String.format("There is no Type mapping with name (%s)"));
        }
        return typeMapping.get(typeName);
    }
}
