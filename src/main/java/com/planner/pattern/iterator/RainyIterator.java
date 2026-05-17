package com.planner.pattern.iterator;

import com.planner.model.City;
import com.planner.model.WeatherState;
import java.util.List;

/**
 * RainyIterator Sınıfı (Iterator / Yineleyici Tasarım Deseni - Somut Yineleyici)
 * 
 * Bu sınıf, şehir listesini dolaşırken SADECE hava durumu yağmurlu (RAINY) olan 
 * şehirleri filtreleyerek sırayla döndüren yineleyicidir.
 */
public class RainyIterator implements CityIterator {
    // Üzerinde gezineceğimiz şehir listesi
    private List<City> cities;
    // Listenin anlık hangi konumunda (indisinde) olduğumuzu tutan gösterge
    private int position;

    /**
     * RainyIterator yapıcı metodu (Constructor)
     * 
     * @param cities Üzerinde arama yapılacak şehirlerin tam listesi
     */
    public RainyIterator(List<City> cities) {
        this.cities = cities;
        this.position = 0; // Başlangıç indisi sıfırdır
    }

    /**
     * Listeyi sırayla dolaşır ve RAINY hava durumuna sahip sıradaki ilk şehri bulana kadar ilerler.
     * 
     * @return Yağmurlu bir sonraki şehir varsa true, listenin sonuna gelindiyse false döner.
     */
    @Override
    public boolean hasNext() {
        while (position < cities.size()) {
            // Eğer o anki şehrin hava durumu RAINY ise arama başarılıdır
            if (cities.get(position).getCurrentWeatherState() == WeatherState.RAINY) {
                return true;
            }
            position++; // RAINY değilse sıradaki indekse geç
        }
        return false; // Listede taranacak başka yağmurlu şehir kalmadıysa false
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
