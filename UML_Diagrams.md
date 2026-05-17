# Smart Travel Planner - UML Sınıf Diyagramı (Class Diagram)

Aşağıdaki UML diyagramı, projede kullanılan **7 Tasarım Desenini (Design Pattern)** ve sınıflar arasındaki ilişkileri göstermektedir. Bu diyagramı proje raporuna veya sunumuna ekleyebilirsin.

*(Markdown destekleyen bir düzenleyicide (örneğin GitHub, VSCode, Obsidian) bu dosya görsel bir diyagram olarak görünecektir.)*

```mermaid
classDiagram
    direction TB

    %% 1. SINGLETON PATTERN
    namespace Singleton {
        class CityRepository {
            - static CityRepository instance
            - List~City~ cities
            - CityRepository()
            + static CityRepository getInstance()
            + List~City~ getCities()
        }
    }

    %% 2. STRATEGY PATTERN
    namespace Strategy {
        class SortStrategy {
            <<interface>>
            + sort(List~City~ cities)
        }
        class SortByName
        class SortByPopulation
        class SortByArea
        
        SortStrategy <|.. SortByName
        SortStrategy <|.. SortByPopulation
        SortStrategy <|.. SortByArea
    }

    %% 3. ITERATOR PATTERN
    namespace Iterator {
        class CityIterator {
            <<interface>>
            + hasNext() boolean
            + next() City
        }
        class SunnyIterator
        class CloudyIterator
        class RainyIterator
        class SnowyIterator
        
        CityIterator <|.. SunnyIterator
        CityIterator <|.. CloudyIterator
        CityIterator <|.. RainyIterator
        CityIterator <|.. SnowyIterator
    }

    %% 4. OBSERVER PATTERN
    namespace Observer {
        class WeatherObserver {
            <<interface>>
            + updateWeather()
        }
        class WeatherProvider {
            - List~WeatherObserver~ observers
            + attach(WeatherObserver observer)
            + detach(WeatherObserver observer)
            + notifyObservers()
            + run()
        }
        WeatherProvider o-- WeatherObserver : notifies
    }

    %% 5. DECORATOR PATTERN
    namespace Decorator {
        class CityVisit {
            <<interface>>
            + getDescription() String
            + getCost() double
            + getTimeInHours() double
        }
        class BaseCityVisit {
            - City city
        }
        class ActivityDecorator {
            <<abstract>>
            # CityVisit decoratedVisit
        }
        class MuseumVisit
        class ParkVisit
        class ShoppingMallVisit
        class CityCenterVisit
        
        CityVisit <|.. BaseCityVisit
        CityVisit <|.. ActivityDecorator
        ActivityDecorator o-- CityVisit : wraps
        ActivityDecorator <|-- MuseumVisit
        ActivityDecorator <|-- ParkVisit
        ActivityDecorator <|-- ShoppingMallVisit
        ActivityDecorator <|-- CityCenterVisit
    }

    %% 6. COMPOSITE PATTERN
    namespace Composite {
        class PlanComponent {
            <<interface>>
            + getName() String
            + getTotalCost() double
            + getTotalTime() double
            + add(PlanComponent component)
            + add(int index, PlanComponent component)
            + remove(PlanComponent component)
            + getChildren() List~PlanComponent~
        }
        class ActivityPlan {
            - String planName
            - List~PlanComponent~ children
        }
        class ActivityLeaf {
            - String activityName
            - double cost
            - double time
        }
        
        PlanComponent <|.. ActivityPlan
        PlanComponent <|.. ActivityLeaf
        ActivityPlan o-- PlanComponent : contains
    }

    %% 7. COMMAND PATTERN
    namespace Command {
        class Command {
            <<interface>>
            + execute()
            + undo()
        }
        class CommandManager {
            - Stack~Command~ undoStack
            - Stack~Command~ redoStack
            + executeCommand(Command command)
            + undo()
            + redo()
        }
        class AddComponentCommand
        class RemoveComponentCommand
        class MoveComponentCommand
        class ClearPlanCommand
        class AddCityToTripCommand
        class RemoveCityFromTripCommand
        
        Command <|.. AddComponentCommand
        Command <|.. RemoveComponentCommand
        Command <|.. MoveComponentCommand
        Command <|.. ClearPlanCommand
        Command <|.. AddCityToTripCommand
        Command <|.. RemoveCityFromTripCommand
        CommandManager o-- Command : manages
    }

    %% CORE MODELS
    class City {
        - String name
        - long population
        - double area
        - double currentTemperature
        - WeatherState currentWeatherState
    }
    
    class WeatherState {
        <<enumeration>>
        SUNNY
        CLOUDY
        RAINY
        SNOWY
    }
```
