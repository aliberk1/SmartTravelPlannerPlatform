package com.planner.pattern.composite;

import java.util.List;

public interface PlanComponent {
    String getName();
    double getTotalCost();
    double getTotalTime();
    
    // Composite operations
    void add(PlanComponent component);
    void remove(PlanComponent component);
    List<PlanComponent> getChildren();
    void print(String indent);
}
