package com.planner.pattern.iterator;

import com.planner.model.City;

/**
 * CityIterator Arayüzü (Iterator / Yineleyici Tasarım Deseni)
 * 
 * Bu arayüz, şehir listesi koleksiyonunu hava durumuna göre filtreleyerek dolaşmak (iterate etmek) 
 * için gerekli olan standart yineleyici metotlarını tanımlar.
 * 
 * Sunum Notu: Öğretmen "Neden standart bir Java Iterator yerine kendi Iterator arayüzünüzü yazdınız?" 
 * diye sorursa; "Uygulamamızda seyahat planlaması yaparken şehirleri anlık hava durumuna göre filtreleyerek 
 * gezmemiz gerekiyor. Kendi yazdığımız filtreli yineleyiciler (SunnyIterator, CloudyIterator vb.) 
 * sayesinde listenin iç yapısını (nasıl saklandığını, indis durumlarını) dışarıya sızdırmadan sadece 
 * hasNext() ve next() çağrılarıyla filtrelenmiş şehirleri kolayca ve güvenle dönebiliyoruz." diyebilirsiniz.
 */
public interface CityIterator {
    /**
     * Filtre kriterine uyan bir sonraki şehir elemanının olup olmadığını kontrol eder.
     * 
     * @return Uyan bir şehir varsa true, yoksa false döner.
     */
    boolean hasNext();

    /**
     * Filtre kriterine uyan bir sonraki şehir nesnesini döndürür.
     * 
     * @return Kriteri sağlayan sıradaki City nesnesi
     */
    City next();
}
