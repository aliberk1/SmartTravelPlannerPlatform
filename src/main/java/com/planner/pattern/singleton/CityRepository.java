package com.planner.pattern.singleton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planner.model.City;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CityRepository {
    private static CityRepository instance;
    private List<City> cities;

    private CityRepository() {
        cities = new ArrayList<>();
        loadCitiesFromJson();
    }

    public static synchronized CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository();
        }
        return instance;
    }

    private void loadCitiesFromJson() {
        try {
            Reader reader = new InputStreamReader(
                    getClass().getResourceAsStream("/data/cities.json"), "UTF-8");
            Gson gson = new Gson();
            Type cityListType = new TypeToken<ArrayList<City>>(){}.getType();
            cities = gson.fromJson(reader, cityListType);
            reader.close();
        } catch (Exception e) {
            System.err.println("Failed to load cities.json: " + e.getMessage());
            // Fallback list if json fails
            cities = new ArrayList<>();
        }
    }

    public List<City> getCities() {
        return cities;
    }
}
