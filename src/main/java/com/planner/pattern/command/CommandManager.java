package com.planner.pattern.command;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Once a new command is executed, redo stack is cleared
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    public String getUndoDescription() {
        if (undoStack.isEmpty()) return "Nothing";
        return formatClassName(undoStack.peek().getClass().getSimpleName());
    }

    public String getRedoDescription() {
        if (redoStack.isEmpty()) return "Nothing";
        return formatClassName(redoStack.peek().getClass().getSimpleName());
    }

    private String formatClassName(String name) {
        String base = name.replace("Command", "");
        if (base.equals("AddComponent")) return "Add Plan Component";
        if (base.equals("RemoveComponent")) return "Remove Plan Component";
        if (base.equals("MoveComponent")) return "Move Plan Component";
        if (base.equals("ClearPlan")) return "Clear Active City Tree";
        return base;
    }
}
