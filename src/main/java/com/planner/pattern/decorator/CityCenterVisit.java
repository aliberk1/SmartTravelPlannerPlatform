package com.planner.pattern.decorator;

public class CityCenterVisit extends ActivityDecorator {
    public CityCenterVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Historic City Center";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.0;
    }

    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 1.5;
    }
}
