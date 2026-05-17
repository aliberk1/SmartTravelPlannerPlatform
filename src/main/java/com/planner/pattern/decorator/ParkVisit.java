package com.planner.pattern.decorator;

public class ParkVisit extends ActivityDecorator {
    public ParkVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Park";
    }

    @Override
    public double getCost() {
        return super.getCost() + 7.0;
    }

    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 1.0;
    }
}
