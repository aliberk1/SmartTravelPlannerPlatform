package com.planner.pattern.decorator;

/**
 * CityCenterVisit Sınıfı (Decorator / Dekoratör Tasarım Deseni - Somut Dekoratör)
 * 
 * Bu sınıf, temel seyahat nesnesine "Şehir Merkezi Gezisi" aktivitesini 
 * giydirmek için kullanılır. Şehir merkezi gezisi ücretsizdir ($0) ancak plana 1.5 saat ekler.
 * 
 * Sunum Notu: Öğretmen "Dekoratördeki super.getCost() veya super.getTimeInHours() çağrıları 
 * ne yapıyor?" derse; "Bu metotlar, sarmalanmış alt nesnenin maliyet ve süresini getirir. 
 * Biz de bu temel değerlerin üzerine kendi aktivitemizin ek maliyetini (+0.0) ve süresini (+1.5) 
 * ekleyerek yeni toplam değeri hesaplarız. Bu sayede zincirleme toplama işlemi yapılmış olur." 
 * cevabını verebilirsiniz.
 */
public class CityCenterVisit extends ActivityDecorator {

    /**
     * Şehir merkezi dekoratörü yapıcı metodu
     * 
     * @param decoratedVisit Giydirilecek olan seyahat nesnesi
     */
    public CityCenterVisit(CityVisit decoratedVisit) {
        super(decoratedVisit);
    }

    /**
     * Önceki açıklamanın sonuna ", Şehir Merkezi" ifadesini ekler.
     */
    @Override
    public String getDescription() {
        return super.getDescription() + ", City Center";
    }

    /**
     * Önceki seyahatin maliyetine 0.0 TL/Dolar ekler (Şehir merkezi gezisi ücretsizdir).
     */
    @Override
    public double getCost() {
        return super.getCost() + 0.0; // Şehir merkezi gezisi ücretsizdir
    }

    /**
     * Önceki seyahatin süresine 1.5 saat ekler.
     */
    @Override
    public double getTimeInHours() {
        return super.getTimeInHours() + 1.5;
    }
}
