package com.planner.pattern.decorator;

public class ShoppingMallVisit extends ActivityDecorator {
    public ShoppingMallVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Shopping Mall";
    }

    @Override
    public double getCost() {
        return super.getCost() + 100.0;
    }

    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 2.5;
    }
}
