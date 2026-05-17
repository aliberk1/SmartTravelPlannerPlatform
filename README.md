# Smart Travel Planner

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue.svg)
![Maven](https://img.shields.io/badge/Maven-Build-success.svg)
![Design Patterns](https://img.shields.io/badge/Design_Patterns-7_Implemented-brightgreen.svg)

Smart Travel Planner is a modern desktop application built with JavaFX that helps users plan their trips and activities. It was developed primarily to demonstrate the practical application of 7 core **Software Design Patterns** in a real-world scenario.

The application features real-time simulated weather data, dynamic filtering, sorting, and a robust tree-based trip planner complete with cost/time calculation and undo/redo capabilities.

## Features

- **City Data Management:** View a comprehensive list of cities loaded from a local JSON source.
- **Dynamic Sorting & Filtering:** Sort cities by Name, Population, or Area. Filter cities based on their current simulated weather (Sunny, Rainy, etc.).
- **Real-Time Data Simulation:** Includes a live weather simulator that randomly updates weather conditions and temperatures every 3 seconds, automatically reflecting changes on live charts.
- **Trip Planning:** Add cities to your travel plan and nest various activities (Museums, Malls, Parks) under them to build a detailed itinerary.
- **Cost & Time Calculation:** Automatically calculates the total cost and duration of your planned trip based on selected activities.
- **Undo/Redo System:** Made a mistake? Seamlessly undo or redo your actions in the trip planner.

## Implemented Design Patterns

This project successfully implements the following **7 Design Patterns**:

1.  **Singleton:** `CityRepository` ensures the initial data is read only once, acting as the single source of truth for all application data.
2.  **Strategy:** Different algorithms are used to sort the city list (by Name, Population, Area), which can be switched dynamically at runtime via the UI.
3.  **Iterator:** A custom iterator traverses the city list, allowing the UI to filter and display cities based on specific weather conditions.
4.  **Observer:** A background `WeatherProvider` thread pushes live weather updates every 3 seconds to subscribed Bar and Pie charts, updating the UI without manual intervention.
5.  **Composite:** Used to build the Tree hierarchy for the trip planner. `ActivityPlan` nodes can contain cities and other activities, allowing recursive calculation of total cost and time.
6.  **Decorator:** Wraps activity properties (like a Museum or Mall visit) around a base city visit, dynamically calculating cost and time without modifying the core objects.
7.  **Command:** All planner modifications (Add, Delete, Clear) are encapsulated as Command objects and managed by a `CommandManager` using a Stack mechanism, enabling perfect Undo and Redo operations.

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 17** or higher
- **Maven** 3.8+

### Installation & Execution

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Hamit3306/Smart-Travel-Planner.git
   cd Smart-Travel-Planner
   ```

2. **Build the project using Maven**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   You can run the application directly via the JavaFX Maven Plugin:
   ```bash
   mvn javafx:run
   ```
   *Alternatively, you can run the generated Fat JAR located in the `target/` directory.*

## Project Structure

- `src/main/java/com/planner`: Contains the core Java source code, UI controllers, and design pattern implementations.
- `pom.xml`: Maven configuration file managing dependencies (JavaFX, Gson) and build plugins.

## Technologies Used

- **Language:** Java 17
- **GUI Framework:** JavaFX 17
- **Build Tool:** Apache Maven
- **JSON Parsing:** Google Gson

## Author

- **Hamit3306**
