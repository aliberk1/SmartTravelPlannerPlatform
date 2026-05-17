package com.planner.pattern.decorator;

import com.planner.model.City;

public class BaseCityVisit implements CityVisit {
    private City city;

    public BaseCityVisit(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String getDescription() {
        return "Visit to " + city.getName();
    }

    @Override
    public double getCost() {
        return 0.0; // Base visit cost
    }

    @Override
    public double getTimeInHours() {
        return 0.0; // Base visit should not add 24h default for tree views
    }
}
