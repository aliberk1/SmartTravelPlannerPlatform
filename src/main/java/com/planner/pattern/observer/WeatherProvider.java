package com.planner.pattern.observer;

import com.planner.model.City;
import com.planner.model.WeatherState;
import com.planner.pattern.singleton.CityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherProvider implements Runnable {
    private List<WeatherObserver> observers = new ArrayList<>();
    private boolean running = true;
    private Random random = new Random();

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.updateWeather();
        }
    }

    public void stopProvider() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(3000); // Wait 3 seconds
                
                // Update cities randomly
                List<City> cities = CityRepository.getInstance().getCities();
                WeatherState[] states = WeatherState.values();
                for (City city : cities) {
                    // Randomly fluctuate temperature between -5 and +5
                    double tempChange = -5 + (10 * random.nextDouble());
                    city.setCurrentTemperature(Math.round((city.getCurrentTemperature() + tempChange) * 10.0) / 10.0);
                    
                    // Randomly change weather state
                    city.setCurrentWeatherState(states[random.nextInt(states.length)]);
                }
                
                // Notify GUI
                notifyObservers();
            } catch (InterruptedException e) {
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }
}
