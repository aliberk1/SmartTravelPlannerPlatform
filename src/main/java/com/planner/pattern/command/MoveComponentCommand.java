package com.planner.pattern.command;

import com.planner.pattern.composite.PlanComponent;
import java.util.Collections;
import java.util.List;

public class MoveComponentCommand implements Command {
    private PlanComponent parent;
    private PlanComponent child;
    private int direction; // -1 for up, 1 for down
    
    public MoveComponentCommand(PlanComponent parent, PlanComponent child, int direction) {
        this.parent = parent;
        this.child = child;
        this.direction = direction;
    }

    @Override
    public void execute() {
        move(direction);
    }

    @Override
    public void undo() {
        move(-direction); // Reverse the movement
    }
    
    private void move(int dir) {
        if (parent != null && parent.getChildren() != null) {
            List<PlanComponent> list = parent.getChildren();
            int index = list.indexOf(child);
            if (index >= 0) {
                int newIndex = index + dir;
                if (newIndex >= 0 && newIndex < list.size()) {
                    Collections.swap(list, index, newIndex);
                }
            }
        }
    }
}
