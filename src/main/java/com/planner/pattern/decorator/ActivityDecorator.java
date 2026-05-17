package com.planner.pattern.decorator;

/**
 * ActivityDecorator Soyut Sınıfı (Decorator / Dekoratör Tasarım Deseni)
 * 
 * Bu soyut sınıf, tüm aktivite dekoratörlerinin türeyeceği temel sınıftır.
 * CityVisit arayüzünü uygular ve kendi içinde sarıp sarmalayacağı (decorate edeceği) 
 * başka bir CityVisit nesnesini referans olarak tutar.
 * 
 * Sunum Notu: Öğretmen "Bu soyut sınıfın görevi nedir?" derse;
 * "Bu sınıf, sarılan CityVisit nesnesine ait tüm metot çağrılarını alt sınıflara aktarma görevini 
 * üstlenir. Böylece alt sınıflar (MuseumVisit vb.) sadece kendilerinin ekleyeceği yeni değerleri 
 * (ekstra maliyet, süre vb.) override edip, geri kalan işleri sarmalanmış nesneye (decoratedVisit) 
 * devredebilirler. Bu sayede inanılmaz esnek bir yapı kurmuş olduk." diyebilirsiniz.
 */
public abstract class ActivityDecorator implements CityVisit {
    // Dekore edilen (giydirilen) temel CityVisit nesnesi
    protected CityVisit decoratedVisit;

    /**
     * Dekoratör yapıcı metodu
     * 
     * @param decoratedVisit Giydirilecek olan alt CityVisit nesnesi
     */
    public ActivityDecorator(CityVisit decoratedVisit) {
        this.decoratedVisit = decoratedVisit;
    }

    /**
     * Giydirilen nesnenin açıklamasını aynen iletir (Alt sınıflar bunu genişletecektir).
     */
    @Override
    public String getDescription() {
        return decoratedVisit.getDescription();
    }

    /**
     * Giydirilen nesnenin maliyetini aynen iletir.
     */
    @Override
    public double getCost() {
        return decoratedVisit.getCost();
    }

    /**
     * Giydirilen nesnenin süresini aynen iletir.
     */
    @Override
    public double getTimeInHours() {
        return decoratedVisit.getTimeInHours();
    }
}
