package com.planner.pattern.decorator;

/**
 * CityVisit Arayüzü (Decorator / Dekoratör Tasarım Deseni)
 * 
 * Bu arayüz, Decorator tasarım deseninin temel bileşen tipidir. Hem dekore edilecek 
 * temel sınıf (BaseCityVisit) hem de dekoratör sınıfları (ActivityDecorator ve alt sınıfları) 
 * bu arayüzü uygular. Böylece nesneleri dinamik olarak üst üste giydirebiliriz.
 * 
 * Sunum Notu: Öğretmen "Decorator desenini neden ve nerede kullandınız?" diye sorarsa;
 * "Seyahat planı hazırlarken bir şehre yapılacak temel ziyarete (BaseCityVisit) sonradan dinamik olarak 
 * yeni aktiviteler (Müze gezisi, alışveriş merkezi gezisi vb.) ekleyerek seyahatin tanımını, 
 * maliyetini ve süresini değiştirmek istedik. Bu deseni kullanarak nesneleri çalışma zamanında (runtime) 
 * dinamik olarak genişletebiliyoruz. Kalıtım yerine kompozisyonu tercih ederek kod tekrarını önledik." 
 * diyebilirsiniz.
 */
public interface CityVisit {
    /**
     * Ziyaretin açıklamasını döndürür (Örn: "Istanbul Ziyareti, Müze, Alışveriş").
     */
    String getDescription();

    /**
     * Ziyaretin toplam maliyetini döndürür.
     */
    double getCost();

    /**
     * Ziyaretin toplam süresini (saat) döndürür.
     */
    double getTimeInHours();
}
