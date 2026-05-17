package com.planner.pattern.strategy;

import com.planner.model.City;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SortByPopulation Sınıfı (Strategy / Strateji Tasarım Deseni - Somut Strateji)
 * 
 * Bu sınıf, şehirleri nüfuslarına göre büyükten küçüğe sıralayan 
 * somut sıralama stratejisidir.
 */
public class SortByPopulation implements SortStrategy {
    /**
     * Şehir listesini nüfusa göre büyükten küçüğe (reversed) sıralar.
     */
    @Override
    public void sort(List<City> cities) {
        // Nüfus değerlerine (long) göre karşılaştırma yapıp listeyi tersten (reversed) dizer
        Collections.sort(cities, Comparator.comparingLong(City::getPopulation).reversed());
    }
}
