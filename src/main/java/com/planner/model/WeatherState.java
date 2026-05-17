package com.planner.model;

/**
 * WeatherState Numaralandırması (Enum)
 * 
 * Uygulamadaki şehirlerin sahip olabileceği olası hava durumu hallerini temsil eder.
 * 
 * SUNNY  -> Güneşli
 * CLOUDY -> Bulutlu
 * RAINY  -> Yağmurlu
 * SNOWY  -> Karlı
 * 
 * Sunum Notu: Bu enum, Iterator tasarım deseninde şehirleri filtrelerken (SunnyIterator vb.) 
 * ve pasta grafikte dağılım hesaplaması yaparken anahtar kelime olarak kullanılır.
 */
public enum WeatherState {
    SUNNY,
    CLOUDY,
    RAINY,
    SNOWY
}
