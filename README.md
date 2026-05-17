# Smart Travel Planner Platform

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue.svg)
![Maven](https://img.shields.io/badge/Maven-Build-success.svg)
![Design Patterns](https://img.shields.io/badge/Design_Patterns-7_Implemented-brightgreen.svg)

## Project Overview

Smart Travel Planner Platform is a desktop application built with **JavaFX** for the **SENG 324 - Software Design Patterns** term project. It enables users to explore, monitor, and plan trips across different cities while demonstrating the practical usage of **7 core Software Design Patterns**.

The application features real-time simulated weather data, dynamic filtering, sorting, a robust tree-based trip planner with recursive cost/time calculation, and full undo/redo capabilities.

## Features

- **City Data Management:** View a list of cities loaded from a JSON data source via a Singleton repository.
- **Dynamic Sorting:** Sort cities by Name, Population, or Area using the Strategy Design Pattern.
- **Weather Filtering:** Filter cities by current weather condition (Sunny, Cloudy, Rainy, Snowy) using the Iterator Design Pattern.
- **Real-Time Weather Simulation:** A background thread (Observer pattern) randomly updates weather conditions and temperatures every 3 seconds, dynamically reflecting changes on live Bar and Pie charts.
- **Trip Planning with Decorator:** Add decorated activities (Museum, Shopping Mall, Park, City Center) to cities. Each decorator dynamically wraps cost and time information.
- **Hierarchical Plan Builder (Composite):** Build full travel plans as a tree structure containing multiple Activity Plans and Activity Leaves with recursive total cost and time calculation.
- **Custom Activities:** Add user-defined activities (e.g., Cinema, Dinner) with custom cost and duration via the GUI.
- **Undo/Redo System (Command):** All plan modifications are encapsulated as Command objects, enabling perfect Undo and Redo via a dual-stack mechanism.
- **No Duplicate Activities:** The system prevents adding duplicate activities under the same composite node.

## Implemented Design Patterns

| # | Pattern | Classes | Purpose |
|---|---------|---------|---------|
| 1 | **Singleton** | `CityRepository` | Reads JSON once, provides shared city list from a single point |
| 2 | **Strategy** | `SortStrategy`, `SortByName`, `SortByPopulation`, `SortByArea` | Extensible sorting algorithms switchable at runtime |
| 3 | **Iterator** | `CityIterator`, `SunnyIterator`, `CloudyIterator`, `RainyIterator`, `SnowyIterator` | Custom weather-filtered iterators over city collections |
| 4 | **Observer** | `WeatherObserver`, `WeatherProvider`, `MainController` | Push-based weather updates from background thread to UI |
| 5 | **Decorator** | `CityVisit`, `BaseCityVisit`, `ActivityDecorator`, `MuseumVisit`, `CityCenterVisit`, `ShoppingMallVisit`, `ParkVisit` | Dynamically wrap city visits with activity features |
| 6 | **Composite** | `PlanComponent`, `ActivityPlan`, `ActivityLeaf` | Recursive tree structure for trip planning with total cost/time |
| 7 | **Command** | `Command`, `CommandManager`, `AddComponentCommand`, `RemoveComponentCommand`, `MoveComponentCommand`, `ClearPlanCommand` | Encapsulated GUI actions with undo/redo support |

## GUI Layout

The GUI includes the following panels:

1. **Control Area** — Sorting (ComboBox) and weather filtering (ComboBox)
2. **All Cities List** — Sorted list of all cities with population, area, temperature, weather
3. **Filtered Cities List** — Weather-filtered city list using Iterator pattern
4. **Planner Panel** — Checkbox-based activity selection, custom activity input, Decorator preview
5. **Travel Plan Panel** — Hierarchical TreeView showing Composite plan structure
6. **Bar Chart** — Real-time city temperature visualization
7. **Pie Chart** — Weather distribution with custom legend
8. **Status Bar** — Current tree total (cost/time) and Undo/Redo descriptions
9. **Undo/Redo Buttons** — Full command history navigation

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Apache Maven** 3.8+

### Installation & Execution

1. **Clone the repository**:
   ```bash
   git clone https://github.com/aliberk1/SmartTravelPlannerPlatform.git
   cd SmartTravelPlannerPlatform
   ```

2. **Build the project using Maven**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   ```bash
   mvn javafx:run
   ```
   *Alternatively, run the Fat JAR (Windows only):*
   ```bash
   java -jar YourPackagedApp.jar
   ```
   > **Note:** The fat JAR includes Windows-specific JavaFX native libraries. On Linux/Mac, use `mvn javafx:run` instead.

## Project Structure

```
src/main/java/com/planner/
├── AppLauncher.java              # Entry point (fat JAR launcher)
├── MainApp.java                  # JavaFX Application class
├── model/
│   ├── City.java                 # City data model
│   └── WeatherState.java         # Weather enum (SUNNY, CLOUDY, RAINY, SNOWY)
├── gui/
│   └── MainController.java       # Main UI controller (Observer implementation)
└── pattern/
    ├── singleton/
    │   └── CityRepository.java   # Singleton city data repository
    ├── strategy/
    │   ├── SortStrategy.java     # Strategy interface
    │   ├── SortByName.java       # Sort by city name (A-Z)
    │   ├── SortByPopulation.java # Sort by population (desc)
    │   └── SortByArea.java       # Sort by area (desc)
    ├── iterator/
    │   ├── CityIterator.java     # Iterator interface
    │   ├── SunnyIterator.java    # Filter: SUNNY cities
    │   ├── CloudyIterator.java   # Filter: CLOUDY cities
    │   ├── RainyIterator.java    # Filter: RAINY cities
    │   └── SnowyIterator.java    # Filter: SNOWY cities
    ├── observer/
    │   ├── WeatherObserver.java  # Observer interface
    │   └── WeatherProvider.java  # Subject: background weather updater
    ├── decorator/
    │   ├── CityVisit.java        # Component interface
    │   ├── BaseCityVisit.java    # Concrete component
    │   ├── ActivityDecorator.java# Abstract decorator
    │   ├── MuseumVisit.java      # Concrete decorator ($18.0, 2.0h)
    │   ├── CityCenterVisit.java  # Concrete decorator ($0.0, 1.5h)
    │   ├── ShoppingMallVisit.java# Concrete decorator ($25.0, 2.0h)
    │   └── ParkVisit.java        # Concrete decorator ($7.0, 1.0h)
    ├── composite/
    │   ├── PlanComponent.java    # Component interface
    │   ├── ActivityPlan.java     # Composite node
    │   └── ActivityLeaf.java     # Leaf node
    └── command/
        ├── Command.java          # Command interface
        ├── CommandManager.java   # Invoker with undo/redo stacks
        ├── AddComponentCommand.java
        ├── RemoveComponentCommand.java
        ├── MoveComponentCommand.java
        └── ClearPlanCommand.java
```

## Technologies Used

- **Language:** Java 17
- **GUI Framework:** JavaFX 17
- **Build Tool:** Apache Maven
- **JSON Parsing:** Google Gson

## Team

- **Team 1** — SENG 324 Term Project
  - Student A — Singleton + Strategy + Iterator
  - Student B — Observer + Decorator
  - Student C — Composite + Command + GUI

> See [CONTRIBUTIONS.md](CONTRIBUTIONS.md) for detailed contribution report.
