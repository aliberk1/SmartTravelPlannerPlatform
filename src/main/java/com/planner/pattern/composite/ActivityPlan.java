package com.planner.pattern.composite;

import java.util.ArrayList;
import java.util.List;

public class ActivityPlan implements PlanComponent {
    private String planName;
    private List<PlanComponent> children;

    public ActivityPlan(String planName) {
        this.planName = planName;
        this.children = new ArrayList<>();
    }

    @Override
    public String getName() {
        return planName;
    }

    @Override
    public double getTotalCost() {
        double cost = 0;
        for (PlanComponent child : children) {
            cost += child.getTotalCost();
        }
        return cost;
    }

    @Override
    public double getTotalTime() {
        double time = 0;
        for (PlanComponent child : children) {
            time += child.getTotalTime();
        }
        return time;
    }

    @Override
    public void add(PlanComponent component) {
        children.add(component);
    }

    @Override
    public void remove(PlanComponent component) {
        children.remove(component);
    }

    @Override
    public List<PlanComponent> getChildren() {
        return children;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "+ " + getName() + " (Cost: " + getTotalCost() + ", Time: " + getTotalTime() + "h)");
        for (PlanComponent child : children) {
            child.print(indent + "  ");
        }
    }
}
