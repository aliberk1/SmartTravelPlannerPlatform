package com.planner.pattern.strategy;

import com.planner.model.City;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * SortByName Sınıfı (Strategy / Strateji Tasarım Deseni - Somut Strateji)
 * 
 * Bu sınıf, şehirleri alfabetik olarak isimlerine göre (A-Z) sıralayan 
 * somut sıralama stratejisidir.
 */
public class SortByName implements SortStrategy {
    /**
     * Şehir listesini isimlerine göre alfabetik olarak sıralar.
     */
    @Override
    public void sort(List<City> cities) {
        // Java'nın Collections.sort ve Comparator API'si kullanılarak isim karşılaştırması yapılır
        Collections.sort(cities, Comparator.comparing(City::getName));
    }
}
