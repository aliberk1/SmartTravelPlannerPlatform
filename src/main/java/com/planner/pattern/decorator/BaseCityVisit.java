package com.planner.pattern.decorator;

import com.planner.model.City;

/**
 * BaseCityVisit Sınıfı (Decorator / Dekoratör Tasarım Deseni - Somut Bileşen)
 * 
 * Bu sınıf, üzerine diğer aktivitelerin (dekoratörlerin) giydirileceği EN TEMEL 
 * şehir ziyaretini temsil eder. Herhangi bir ekstra aktivite içermeyen yalın bir ziyarettir.
 * 
 * Sunum Notu: Öğretmen "Bu sınıf ne işe yarıyor? Neden maliyet ve süresi 0?" diye sorarsa;
 * "Bu sınıf giydireceğimiz tüm dekoratörlerin temelidir. Bir şehre yapılacak ham seyahati ifade eder. 
 * Başlangıçta ekstra bir aktivite eklenmediği için maliyeti ve ek süresi 0'dır. Üzerine müze gezisi, 
 * park yürüyüşü gibi aktiviteler eklendikçe (dekore edildikçe) bu değerler dinamik olarak artacaktır." 
 * diyebilirsiniz.
 */
public class BaseCityVisit implements CityVisit {
    // Ziyaret edilecek olan şehir bilgisi
    private City city;

    /**
     * Temel şehir ziyareti yapıcı metodu
     * 
     * @param city Ziyaret edilecek şehir nesnesi
     */
    public BaseCityVisit(City city) {
        this.city = city;
    }

    /**
     * İlgili şehir nesnesini döndürür.
     */
    public City getCity() {
        return city;
    }

    /**
     * Temel seyahat açıklamasını oluşturur.
     */
    @Override
    public String getDescription() {
        return "Visit to " + city.getName();
    }

    /**
     * Temel seyahat maliyeti başlangıçta sıfırdır.
     */
    @Override
    public double getCost() {
        return 0.0;
    }

    /**
     * Temel seyahat süresi başlangıçta sıfırdır.
     */
    @Override
    public double getTimeInHours() {
        return 0.0;
    }
}
