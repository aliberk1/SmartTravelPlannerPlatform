package com.planner.pattern.decorator;

/**
 * ParkVisit Sınıfı (Decorator / Dekoratör Tasarım Deseni - Somut Dekoratör)
 * 
 * Bu sınıf, temel seyahat nesnesine "Park Yürüyüşü" aktivitesini giydirmek için kullanılır.
 * Park yürüyüşünün maliyeti $7.0'dır ve plana 1.0 saat ekler.
 * 
 * Sunum Notu: Öğretmen "Neden her aktivite için yeni bir sınıf oluşturdunuz?" diye sorarsa;
 * "Bu, Single Responsibility (Tek Sorumluluk) prensibine uygundur. Her bir aktivitenin kendine 
 * özgü maliyet, süre ve açıklama ekleme kuralı vardır. Bu sayede sisteme yeni bir aktivite eklemek 
 * istediğimizde mevcut sınıflara dokunmadan (Open/Closed Prensibi) yeni bir dekoratör sınıfı yazmamız 
 * yeterli olmaktadır." diyebilirsiniz.
 */
public class ParkVisit extends ActivityDecorator {

    /**
     * Park ziyareti dekoratörü yapıcı metodu
     * 
     * @param decoratedVisit Giydirilecek olan seyahat nesnesi
     */
    public ParkVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    /**
     * Önceki seyahat açıklamasına ", Park" ekler.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + ", Park";
    }

    /**
     * Önceki seyahatin maliyetine park giriş/aktivite ücreti olan 7.0 birim ekler.
     */
    @Override
    public double getCost() {
        return super.getCost() + 7.0;
    }

    /**
     * Önceki seyahatin süresine parkta geçirilecek süre olan 1.0 saat ekler.
     */
    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 1.0;
    }
}
