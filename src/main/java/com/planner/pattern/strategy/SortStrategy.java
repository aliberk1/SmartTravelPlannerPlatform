package com.planner.pattern.strategy;

import com.planner.model.City;
import java.util.List;

/**
 * SortStrategy Arayüzü (Strategy / Strateji Tasarım Deseni)
 * 
 * Bu arayüz, şehirlerin farklı kriterlere (İsim, Nüfus, Alan) göre sıralanabilmesini 
 * sağlayan algoritma ailesinin ortak arayüzüdür.
 * 
 * Sunum Notu: Öğretmen "Strategy desenini neden kullandınız?" diye sorursa;
 * "Kullanıcının şehirleri isim, nüfus veya yüzölçümüne göre sıralayabilmesini istedik. 
 * Sıralama algoritmalarını MainController sınıfı içerisine if-else bloklarıyla yazmak yerine, 
 * her sıralama kriterini kendi sınıfında bağımsız bir strateji olarak modelledik. Bu sayede 
 * gelecekte yeni bir sıralama kriteri (Örn: Sıcaklığa göre) eklemek istediğimizde, mevcut koda 
 * dokunmadan sadece yeni bir strateji sınıfı eklememiz yeterli olmaktadır (Open/Closed Prensibi)." 
 * diyebilirsiniz.
 */
public interface SortStrategy {
    /**
     * Kendisine parametre olarak verilen şehir listesini, uyguladığı stratejiye 
     * uygun olarak sıralar (örneğin isme, nüfusa veya alana göre).
     * 
     * @param cities Sıralanacak şehir listesi
     */
    void sort(List<City> cities);
}
