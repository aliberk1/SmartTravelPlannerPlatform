package com.planner.pattern.strategy;

import com.planner.model.City;
import java.util.List;

public interface SortStrategy {
    void sort(List<City> cities);
}
