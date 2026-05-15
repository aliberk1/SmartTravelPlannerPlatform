package com.planner.gui;

import com.planner.model.City;
import com.planner.model.WeatherState;
import com.planner.pattern.command.*;
import com.planner.pattern.composite.ActivityLeaf;
import com.planner.pattern.composite.ActivityPlan;
import com.planner.pattern.composite.PlanComponent;
import com.planner.pattern.decorator.*;
import com.planner.pattern.iterator.*;
import com.planner.pattern.observer.WeatherObserver;
import com.planner.pattern.observer.WeatherProvider;
import com.planner.pattern.singleton.CityRepository;
import com.planner.pattern.strategy.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class MainController implements WeatherObserver {

    private BorderPane root;
    private List<City> allCities;

    // UI Elements
    private ListView<City> allCitiesListView;
    private ListView<City> filteredCitiesListView;
    private ComboBox<String> sortComboBox;
    private ComboBox<String> filterComboBox;
    
    private BarChart<String, Number> tempChart;
    private PieChart weatherPieChart;

    private TreeView<String> planTreeView;
    private TreeItem<String> rootTreeItem;
    
    private Label totalCostTimeLabel;

    // Logic Elements
    private CommandManager commandManager;
    private WeatherProvider weatherProvider;
    private ActivityPlan rootPlan;

    public MainController() {
        allCities = new ArrayList<>(CityRepository.getInstance().getCities());
        commandManager = new CommandManager();
        rootPlan = new ActivityPlan("My Travel Plan");
        
        initUI();
        initLogic();
        refreshAllCitiesList();
        updateCharts();
        refreshPlanView();
    }

    public BorderPane getRootView() {
        return root;
    }

    private void initUI() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // LEFT: Cities & Filters
        VBox leftBox = new VBox(10);
        leftBox.setPrefWidth(300);
        
        sortComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "Sort by Name", "Sort by Population", "Sort by Area"));
        sortComboBox.getSelectionModel().selectFirst();
        sortComboBox.setOnAction(e -> applySortStrategy());

        allCitiesListView = new ListView<>();
        allCitiesListView.setPrefHeight(200);

        filterComboBox = new ComboBox<>(FXCollections.observableArrayList(
                "All", "SUNNY", "CLOUDY", "RAINY", "SNOWY"));
        filterComboBox.getSelectionModel().selectFirst();
        filterComboBox.setOnAction(e -> applyIteratorFilter());

        filteredCitiesListView = new ListView<>();
        filteredCitiesListView.setPrefHeight(200);

        leftBox.getChildren().addAll(
                new Label("All Cities (Strategy Pattern)"), sortComboBox, allCitiesListView,
                new Label("Filtered Cities (Iterator Pattern)"), filterComboBox, filteredCitiesListView
        );

        // CENTER: Charts (Observer Pattern)
        VBox centerBox = new VBox(10);
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        tempChart = new BarChart<>(xAxis, yAxis);
        tempChart.setTitle("City Temperatures");
        
        weatherPieChart = new PieChart();
        weatherPieChart.setTitle("Weather Distribution");

        centerBox.getChildren().addAll(tempChart, weatherPieChart);

        // RIGHT: Planner & Actions (Command, Composite, Decorator)
        VBox rightBox = new VBox(10);
        rightBox.setPrefWidth(350);

        rootTreeItem = new TreeItem<>("Root Plan");
        planTreeView = new TreeView<>(rootTreeItem);
        planTreeView.setPrefHeight(300);

        totalCostTimeLabel = new Label("Total Cost: 0.0 | Total Time: 0.0h");
        totalCostTimeLabel.setStyle("-fx-font-weight: bold;");

        // Action Buttons
        HBox historyBox = new HBox(5);
        Button undoBtn = new Button("Undo");
        Button redoBtn = new Button("Redo");
        Button clearBtn = new Button("Clear Plan");
        undoBtn.setOnAction(e -> { commandManager.undo(); refreshPlanView(); });
        redoBtn.setOnAction(e -> { commandManager.redo(); refreshPlanView(); });
        clearBtn.setOnAction(e -> {
            commandManager.executeCommand(new ClearPlanCommand(rootPlan));
            refreshPlanView();
        });
        historyBox.getChildren().addAll(undoBtn, redoBtn, clearBtn);

        // Activity Planner
        VBox activityBox = new VBox(5);
        activityBox.setStyle("-fx-border-color: gray; -fx-padding: 5;");
        Button addCityBtn = new Button("Add Selected City as Plan Node");
        addCityBtn.setOnAction(e -> addCityToPlan());

        Button addMuseumBtn = new Button("Add Museum (+50$, 3h)");
        Button addMallBtn = new Button("Add Mall (+100$, 2.5h)");
        Button addParkBtn = new Button("Add Park (+15$, 1.5h)");
        Button addCenterBtn = new Button("Add City Center (+30$, 4h)");

        addMuseumBtn.setOnAction(e -> addActivityToSelectedCity("Museum"));
        addMallBtn.setOnAction(e -> addActivityToSelectedCity("Mall"));
        addParkBtn.setOnAction(e -> addActivityToSelectedCity("Park"));
        addCenterBtn.setOnAction(e -> addActivityToSelectedCity("Center"));
        
        Button removeBtn = new Button("Remove Selected Item");
        removeBtn.setOnAction(e -> removeSelectedItem());

        activityBox.getChildren().addAll(
                new Label("Planner Actions"),
                addCityBtn, addMuseumBtn, addMallBtn, addParkBtn, addCenterBtn, removeBtn
        );

        rightBox.getChildren().addAll(
                new Label("Travel Plan (Composite Pattern)"),
                planTreeView, totalCostTimeLabel, historyBox, activityBox
        );

        root.setLeft(leftBox);
        root.setCenter(centerBox);
        root.setRight(rightBox);
    }

    private void initLogic() {
        weatherProvider = new WeatherProvider();
        weatherProvider.addObserver(this);
        Thread thread = new Thread(weatherProvider);
        thread.setDaemon(true);
        thread.start();
    }

    public void stopWeatherProvider() {
        if (weatherProvider != null) {
            weatherProvider.stopProvider();
        }
    }

    private void applySortStrategy() {
        String selection = sortComboBox.getValue();
        SortStrategy strategy;
        if ("Sort by Population".equals(selection)) {
            strategy = new SortByPopulation();
        } else if ("Sort by Area".equals(selection)) {
            strategy = new SortByArea();
        } else {
            strategy = new SortByName();
        }
        
        strategy.sort(allCities);
        refreshAllCitiesList();
    }

    private void applyIteratorFilter() {
        String selection = filterComboBox.getValue();
        List<City> filtered = new ArrayList<>();
        CityIterator iterator = null;

        if ("SUNNY".equals(selection)) {
            iterator = new SunnyIterator(allCities);
        } else if ("CLOUDY".equals(selection)) {
            iterator = new CloudyIterator(allCities);
        } else if ("RAINY".equals(selection)) {
            iterator = new RainyIterator(allCities);
        } else if ("SNOWY".equals(selection)) {
            iterator = new SnowyIterator(allCities);
        }

        if (iterator != null) {
            while (iterator.hasNext()) {
                filtered.add(iterator.next());
            }
        } else {
            filtered.addAll(allCities);
        }

        filteredCitiesListView.setItems(FXCollections.observableArrayList(filtered));
    }

    private void refreshAllCitiesList() {
        allCitiesListView.setItems(FXCollections.observableArrayList(allCities));
    }

    @Override
    public void updateWeather() {
        Platform.runLater(() -> {
            refreshAllCitiesList();
            applyIteratorFilter();
            updateCharts();
        });
    }

    private void updateCharts() {
        // Update Bar Chart
        tempChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Temperature (°C)");
        int sunnyCount = 0, cloudyCount = 0, rainyCount = 0, snowyCount = 0;

        for (City city : CityRepository.getInstance().getCities()) {
            series.getData().add(new XYChart.Data<>(city.getName(), city.getCurrentTemperature()));
            
            switch (city.getCurrentWeatherState()) {
                case SUNNY: sunnyCount++; break;
                case CLOUDY: cloudyCount++; break;
                case RAINY: rainyCount++; break;
                case SNOWY: snowyCount++; break;
            }
        }
        tempChart.getData().add(series);

        // Update Pie Chart
        weatherPieChart.getData().clear();
        if (sunnyCount > 0) weatherPieChart.getData().add(new PieChart.Data("Sunny", sunnyCount));
        if (cloudyCount > 0) weatherPieChart.getData().add(new PieChart.Data("Cloudy", cloudyCount));
        if (rainyCount > 0) weatherPieChart.getData().add(new PieChart.Data("Rainy", rainyCount));
        if (snowyCount > 0) weatherPieChart.getData().add(new PieChart.Data("Snowy", snowyCount));
    }

    // --- COMPOSITE & COMMAND LOGIC ---

    private void addCityToPlan() {
        City selected = allCitiesListView.getSelectionModel().getSelectedItem();
        if (selected == null) selected = filteredCitiesListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        ActivityPlan cityPlan = new ActivityPlan("City Visit: " + selected.getName());
        commandManager.executeCommand(new AddComponentCommand(rootPlan, cityPlan));
        refreshPlanView();
    }

    private void addActivityToSelectedCity(String activityType) {
        // Find the selected CityPlan in the composite tree
        TreeItem<String> selectedItem = planTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem == rootTreeItem) {
            System.out.println("Please select a city plan to add an activity.");
            return;
        }

        // We map TreeItem names back to objects. A robust app uses custom TreeCells.
        // For simplicity, we search by name in the children of rootPlan.
        String targetName = selectedItem.getValue();
        PlanComponent targetPlan = findComponentByName(rootPlan, targetName);
        
        if (targetPlan instanceof ActivityPlan) {
            CityVisit base = new BaseCityVisit(new City("Temp", 0, 0, 0, WeatherState.SUNNY));
            CityVisit decorated = null;
            
            switch (activityType) {
                case "Museum": decorated = new MuseumVisit(base); break;
                case "Mall": decorated = new ShoppingMallVisit(base); break;
                case "Park": decorated = new ParkVisit(base); break;
                case "Center": decorated = new CityCenterVisit(base); break;
            }
            
            if (decorated != null) {
                // Using Decorator to get cost and time
                ActivityLeaf leaf = new ActivityLeaf(decorated.getDescription(), decorated.getCost(), decorated.getTimeInHours());
                commandManager.executeCommand(new AddComponentCommand(targetPlan, leaf));
                refreshPlanView();
            }
        }
    }

    private void removeSelectedItem() {
        TreeItem<String> selectedItem = planTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem == rootTreeItem) return;

        String targetName = selectedItem.getValue();
        TreeItem<String> parentItem = selectedItem.getParent();
        
        if (parentItem != null) {
            PlanComponent parentPlan = (parentItem == rootTreeItem) ? rootPlan : findComponentByName(rootPlan, parentItem.getValue());
            PlanComponent childPlan = findComponentByName(parentPlan, targetName);
            
            if (parentPlan != null && childPlan != null) {
                commandManager.executeCommand(new RemoveComponentCommand(parentPlan, childPlan));
                refreshPlanView();
            }
        }
    }

    private PlanComponent findComponentByName(PlanComponent parent, String name) {
        if (parent.getName().equals(name)) return parent;
        if (parent.getChildren() != null) {
            for (PlanComponent child : parent.getChildren()) {
                if (child.getName().equals(name)) return child;
                if (child.getChildren() != null) {
                    PlanComponent found = findComponentByName(child, name);
                    if (found != null) return found;
                }
            }
        }
        return null;
    }

    private void refreshPlanView() {
        rootTreeItem.getChildren().clear();
        buildTreeView(rootTreeItem, rootPlan);
        rootTreeItem.setExpanded(true);
        totalCostTimeLabel.setText("Total Cost: $" + rootPlan.getTotalCost() + " | Total Time: " + rootPlan.getTotalTime() + "h");
    }

    private void buildTreeView(TreeItem<String> treeNode, PlanComponent component) {
        if (component.getChildren() != null) {
            for (PlanComponent child : component.getChildren()) {
                TreeItem<String> item = new TreeItem<>(child.getName());
                treeNode.getChildren().add(item);
                item.setExpanded(true);
                buildTreeView(item, child);
            }
        }
    }
}
