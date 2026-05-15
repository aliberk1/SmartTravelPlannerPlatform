package com.planner.pattern.strategy;

import com.planner.model.City;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByPopulation implements SortStrategy {
    @Override
    public void sort(List<City> cities) {
        Collections.sort(cities, Comparator.comparingLong(City::getPopulation).reversed());
    }
}
