package com.planner.pattern.decorator;

public class MuseumVisit extends ActivityDecorator {
    public MuseumVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Museum";
    }

    @Override
    public double getCost() {
        return super.getCost() + 50.0;
    }

    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 3.0;
    }
}
