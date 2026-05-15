package com.planner.pattern.composite;

import java.util.List;

public class ActivityLeaf implements PlanComponent {
    private String activityName;
    private double cost;
    private double time;

    public ActivityLeaf(String activityName, double cost, double time) {
        this.activityName = activityName;
        this.cost = cost;
        this.time = time;
    }

    @Override
    public String getName() {
        return activityName;
    }

    @Override
    public double getTotalCost() {
        return cost;
    }

    @Override
    public double getTotalTime() {
        return time;
    }

    @Override
    public void add(PlanComponent component) {
        throw new UnsupportedOperationException("Cannot add to a leaf");
    }

    @Override
    public void remove(PlanComponent component) {
        throw new UnsupportedOperationException("Cannot remove from a leaf");
    }

    @Override
    public List<PlanComponent> getChildren() {
        return null;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "- " + getName() + " (Cost: " + getTotalCost() + ", Time: " + getTotalTime() + "h)");
    }
}
