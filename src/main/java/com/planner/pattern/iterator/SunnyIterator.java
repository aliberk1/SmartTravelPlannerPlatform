package com.planner.pattern.iterator;

import com.planner.model.City;
import com.planner.model.WeatherState;
import java.util.List;

/**
 * SunnyIterator Sınıfı (Iterator / Yineleyici Tasarım Deseni - Somut Yineleyici)
 * 
 * Bu sınıf, şehir listesini dolaşırken SADECE hava durumu güneşli (SUNNY) olan 
 * şehirleri filtreleyerek sırayla döndüren yineleyicidir.
 * 
 * Sunum Notu: Gözlemci (Observer) ile gelen yeni hava durumu güncellemelerinden sonra listeyi 
 * filtrelerken bu Iterator'ı kullanırız. hasNext() metodu içinde bir while döngüsüyle listedeki 
 * elemanları inceleriz; hava durumu SUNNY olmayanları pas geçeriz. Bir tane SUNNY bulduğumuzda hasNext() 
 * true döner ve next() çağrısıyla o elemanı arayüze aktarırız. Böylece karmaşık filtreleme mantığını 
 * ListView içinden tamamen izole etmiş oluruz.
 */
public class SunnyIterator implements CityIterator {
    // Üzerinde gezineceğimiz şehir listesi
    private List<City> cities;
    // Listenin anlık hangi konumunda (indisinde) olduğumuzu tutan gösterge
    private int position;

    /**
     * SunnyIterator yapıcı metodu (Constructor)
     * 
     * @param cities Üzerinde arama yapılacak şehirlerin tam listesi
     */
    public SunnyIterator(List<City> cities) {
        this.cities = cities;
        this.position = 0; // Başlangıç indisi sıfırdır
    }

    /**
     * Listeyi sırayla dolaşır ve SUNNY hava durumuna sahip sıradaki ilk şehri bulana kadar ilerler.
     * 
     * @return Güneşli bir sonraki şehir varsa true, listenin sonuna gelindiyse false döner.
     */
    @Override
    public boolean hasNext() {
        while (position < cities.size()) {
            // Eğer o anki şehrin hava durumu SUNNY ise arama başarılıdır
            if (cities.get(position).getCurrentWeatherState() == WeatherState.SUNNY) {
                return true;
            }
            position++; // SUNNY değilse sıradaki indekse geç
        }
        return false; // Listede taranacak başka güneşli şehir kalmadıysa false
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
