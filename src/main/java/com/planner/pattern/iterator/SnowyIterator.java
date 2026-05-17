package com.planner.pattern.iterator;

import com.planner.model.City;
import com.planner.model.WeatherState;
import java.util.List;

/**
 * SnowyIterator Sınıfı (Iterator / Yineleyici Tasarım Deseni - Somut Yineleyici)
 * 
 * Bu sınıf, şehir listesini dolaşırken SADECE hava durumu karlı (SNOWY) olan 
 * şehirleri filtreleyerek sırayla döndüren yineleyicidir.
 */
public class SnowyIterator implements CityIterator {
    // Üzerinde gezineceğimiz şehir listesi
    private List<City> cities;
    // Listenin anlık hangi konumunda (indisinde) olduğumuzu tutan gösterge
    private int position;

    /**
     * SnowyIterator yapıcı metodu (Constructor)
     * 
     * @param cities Üzerinde arama yapılacak şehirlerin tam listesi
     */
    public SnowyIterator(List<City> cities) {
        this.cities = cities;
        this.position = 0; // Başlangıç indisi sıfırdır
    }

    /**
     * Listeyi sırayla dolaşır ve SNOWY hava durumuna sahip sıradaki ilk şehri bulana kadar ilerler.
     * 
     * @return Karlı bir sonraki şehir varsa true, listenin sonuna gelindiyse false döner.
     */
    @Override
    public boolean hasNext() {
        while (position < cities.size()) {
            // Eğer o anki şehrin hava durumu SNOWY ise arama başarılıdır
            if (cities.get(position).getCurrentWeatherState() == WeatherState.SNOWY) {
                return true;
            }
            position++; // SNOWY değilse sıradaki indekse geç
        }
        return false; // Listede taranacak başka karlı şehir kalmadıysa false
    }

    /**
     * Kriteri sağlayan sıradaki şehri döndürür ve göstergeyi (position) bir arttırır.
     */
    @Override
    public City next() {
        if (this.hasNext()) {
            return cities.get(position++); // Şehri dön ve bir sonraki adıma geç
        }
        return null;
    }
}
