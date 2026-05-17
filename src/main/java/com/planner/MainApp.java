package com.planner;

import com.planner.gui.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController();
        
        Scene scene = new Scene(controller.getRootView(), 1200, 800);
        
        primaryStage.setTitle("Smart Travel Planner Platform");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Stop background thread when app closes
        primaryStage.setOnCloseRequest(e -> {
            controller.stopWeatherProvider();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
