package com.planner.pattern.decorator;

/**
 * MuseumVisit Sınıfı (Decorator / Dekoratör Tasarım Deseni - Somut Dekoratör)
 * 
 * Bu sınıf, temel seyahat nesnesine "Müze Gezisi" aktivitesini giydirmek için kullanılır.
 * Müze gezisinin maliyeti $18'dır ve plana 2.0 saat ekler.
 * 
 * Sunum Notu: Öğretmen "Aktivite fiyatlarını ve sürelerini nereden alıyor?" derse;
 * "Bu değerler somut dekoratör sınıflarında sabit olarak tanımlanmıştır. 
 * Örneğin MuseumVisit sınıfı maliyete $18.0, süreye ise 2.0 saat ekler." diyebilirsiniz.
 */
public class MuseumVisit extends ActivityDecorator {

    /**
     * Müze ziyareti dekoratörü yapıcı metodu
     * 
     * @param decoratedVisit Giydirilecek olan seyahat nesnesi
     */
    public MuseumVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    /**
     * Önceki seyahat açıklamasına ", Müze" ekler.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + ", Museum";
    }

    /**
     * Önceki seyahatin maliyetine müze giriş ücreti olan 18.0 birim ekler.
     */
    @Override
    public double getCost() {
        return super.getCost() + 18.0;
    }

    /**
     * Önceki seyahatin süresine müze gezme süresi olan 2.0 saat ekler.
     */
    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 2.0;
    }
}
