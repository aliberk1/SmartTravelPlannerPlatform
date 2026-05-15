package com.planner.pattern.iterator;

import com.planner.model.City;

public interface CityIterator {
    boolean hasNext();
    City next();
}
