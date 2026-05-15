package com.planner.pattern.iterator;

import com.planner.model.City;
import com.planner.model.WeatherState;
import java.util.List;

public class RainyIterator implements CityIterator {
    private List<City> cities;
    private int position;

    public RainyIterator(List<City> cities) {
        this.cities = cities;
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        while (position < cities.size()) {
            if (cities.get(position).getCurrentWeatherState() == WeatherState.RAINY) {
                return true;
            }
            position++;
        }
        return false;
    }

    @Override
    public City next() {
        if (this.hasNext()) {
            return cities.get(position++);
        }
        return null;
    }
}
