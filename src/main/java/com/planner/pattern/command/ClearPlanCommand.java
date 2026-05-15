package com.planner.pattern.command;

import com.planner.pattern.composite.PlanComponent;
import java.util.ArrayList;
import java.util.List;

public class ClearPlanCommand implements Command {
    private PlanComponent root;
    private List<PlanComponent> previousChildren;

    public ClearPlanCommand(PlanComponent root) {
        this.root = root;
        if (root.getChildren() != null) {
            this.previousChildren = new ArrayList<>(root.getChildren());
        }
    }

    @Override
    public void execute() {
        if (root != null && root.getChildren() != null) {
            root.getChildren().clear();
        }
    }

    @Override
    public void undo() {
        if (root != null && previousChildren != null) {
            root.getChildren().clear();
            root.getChildren().addAll(previousChildren);
        }
    }
}
