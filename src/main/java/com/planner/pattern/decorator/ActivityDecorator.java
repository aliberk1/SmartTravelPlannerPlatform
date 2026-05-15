package com.planner.pattern.decorator;

public abstract class ActivityDecorator implements CityVisit {
    protected CityVisit decoratedVisit;

    public ActivityDecorator(CityVisit decoratedVisit) {
        this.decoratedVisit = decoratedVisit;
    }

    @Override
    public String getDescription() {
        return decoratedVisit.getDescription();
    }

    @Override
    public double getCost() {
        return decoratedVisit.getCost();
    }

    @Override
    public double getTimeInHours() {
        return decoratedVisit.getTimeInHours();
    }
}
