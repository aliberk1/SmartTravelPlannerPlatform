package com.planner.pattern.strategy;

import com.planner.model.City;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SortByArea Sınıfı (Strategy / Strateji Tasarım Deseni - Somut Strateji)
 * 
 * Bu sınıf, şehirleri yüzölçümlerine (alanlarına) göre büyükten küçüğe sıralayan 
 * somut sıralama stratejisidir.
 */
public class SortByArea implements SortStrategy {
    /**
     * Şehir listesini yüzölçümlerine göre büyükten küçüğe (reversed) sıralar.
     */
    @Override
    public void sort(List<City> cities) {
        // Yüzölçümü değerlerine (double) göre karşılaştırma yapıp listeyi tersten (reversed) dizer
        Collections.sort(cities, Comparator.comparingDouble(City::getArea).reversed());
    }
}
