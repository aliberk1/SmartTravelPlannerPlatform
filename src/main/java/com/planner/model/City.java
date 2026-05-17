package com.planner.model;

/**
 * City: Uygulamadaki her şehri temsil eden model sınıfı.
 * Strategy deseninde sıralama kriterleri (nüfus, alan, isim) bu sınıfın
 * alanları üzerinden çalışır. Observer deseninde hava durumu güncellendiğinde
 * bu sınıfın currentTemperature ve currentWeatherState alanları değiştirilir.
 */
public class City {

    // Şehrin adı (örneğin "Istanbul", "Ankara")
    private String name;

    // Şehrin nüfusu; SortByPopulation stratejisinde sıralama kriteri olarak kullanılır
    private long population;

    // Şehrin yüzölçümü (km²); SortByArea stratejisinde sıralama kriteri olarak kullanılır
    private double area;

    // Şehrin anlık sıcaklığı (°C); WeatherProvider tarafından periyodik olarak güncellenir
    private double currentTemperature;

    // Şehrin anlık hava durumu (SUNNY, CLOUDY, RAINY, SNOWY);
    // Iterator filtreleme ve pasta grafik bu alana göre çalışır
    private WeatherState currentWeatherState;

    // Parametresiz yapıcı: JSON veya yansıma (reflection) tabanlı araçlar için gerekebilir
    public City() {
    }

    /**
     * Tüm alanları ayarlayan ana yapıcı metod.
     * CityRepository, şehirleri bu yapıcı ile oluşturur.
     */
    public City(String name, long population, double area, double currentTemperature, WeatherState currentWeatherState) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.currentTemperature = currentTemperature;
        this.currentWeatherState = currentWeatherState;
    }

    // --- Getter ve Setter metodları ---
    // Her alan için okuma (get) ve yazma (set) metodları tanımlanmıştır.
    // WeatherProvider, hava durumu güncellemelerinde set metodlarını kullanır.

    /** Şehrin adını döndürür. SortByName stratejisinde karşılaştırma için kullanılır. */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Şehrin nüfusunu döndürür. SortByPopulation stratejisinde kullanılır. */
    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    /** Şehrin alanını (km²) döndürür. SortByArea stratejisinde kullanılır. */
    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    /**
     * Anlık sıcaklığı döndürür.
     * Çubuk grafik ve önizleme başlığı bu değeri görüntüler.
     */
    public double getCurrentTemperature() {
        return currentTemperature;
    }

    /**
     * Sıcaklığı günceller.
     * WeatherProvider arka plan iş parçacığından bu metodu periyodik olarak çağırır.
     */
    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    /**
     * Anlık hava durumunu döndürür (SUNNY / CLOUDY / RAINY / SNOWY).
     * Iterator filtresi ve pasta grafik bu değere göre çalışır.
     */
    public WeatherState getCurrentWeatherState() {
        return currentWeatherState;
    }

    /**
     * Hava durumunu günceller.
     * WeatherProvider bu metodu çağırdıktan sonra Observer'lara bildirim gönderir.
     */
    public void setCurrentWeatherState(WeatherState currentWeatherState) {
        this.currentWeatherState = currentWeatherState;
    }

    /**
     * Şehri okunabilir metin olarak döndürür.
     * ListView hücrelerinde ve hata ayıklama çıktılarında kullanılır.
     */
    @Override
    public String toString() {
        return name + " (Pop: " + population + " | Area: " + area + " | Temp: " + currentTemperature + "°C | " + currentWeatherState + ")";
    }
}
