package com.planner.pattern.decorator;

/**
 * ShoppingMallVisit Sınıfı (Decorator / Dekoratör Tasarım Deseni - Somut Dekoratör)
 * 
 * Bu sınıf, temel seyahat nesnesine "Alışveriş Merkezi Gezisi" aktivitesini giydirmek için kullanılır.
 * Alışveriş merkezi gezisinin maliyeti $25.0'dır ve plana 2.0 saat ekler.
 * 
 * Sunum Notu: Öğretmen "Bu nesne giydirmeleri arka arkaya nasıl yapılıyor?" diye sorursa;
 * "Örneğin, new ShoppingMallVisit(new MuseumVisit(new BaseCityVisit(city))) şeklinde yazıldığında;
 * önce temel ziyaret oluşturulur, müze ile giydirilir, sonra da alışveriş merkezi ile giydirilir.
 * getCost() çağrıldığında zincirleme olarak en içteki nesneye kadar gider ve maliyetleri toplar: 
 * 0.0 + 18.0 + 25.0 = 43.0 sonucunu üretir." diyebilirsiniz.
 */
public class ShoppingMallVisit extends ActivityDecorator {

    /**
     * Alışveriş merkezi ziyareti dekoratörü yapıcı metodu
     * 
     * @param decoratedVisit Giydirilecek olan seyahat nesnesi
     */
    public ShoppingMallVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    /**
     * Önceki seyahat açıklamasına ", Alışveriş Merkezi" ekler.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + ", Shopping Mall";
    }

    /**
     * Önceki seyahatin maliyetine alışveriş merkezi aktivite maliyeti olan 25.0 birim ekler.
     */
    @Override
    public double getCost() {
        return super.getCost() + 25.0;
    }

    /**
     * Önceki seyahatin süresine alışverişte geçirilecek süre olan 2.0 saat ekler.
     */
    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 2.0;
    }
}
