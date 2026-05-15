package com.planner.pattern.command;

import com.planner.pattern.composite.PlanComponent;

public class AddComponentCommand implements Command {
    private PlanComponent parent;
    private PlanComponent child;

    public AddComponentCommand(PlanComponent parent, PlanComponent child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public void execute() {
        if (parent != null) {
            parent.add(child);
        }
    }

    @Override
    public void undo() {
        if (parent != null) {
            parent.remove(child);
        }
    }
}
