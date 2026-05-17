package com.planner.pattern.singleton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planner.model.City;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * CityRepository Sınıfı (Singleton / Tekil Tasarım Deseni)
 * 
 * Bu sınıf, uygulamadaki tüm şehir verilerini "cities.json" dosyasından okuyup bellek üzerinde 
 * saklayan tek merkezli bir veri deposudur. Uygulama genelinde sadece tek bir veri deposu 
 * nesnesi olmasını garanti altına almak için Singleton deseni kullanılmıştır.
 * 
 * Sunum Notu: Öğretmen "Neden Singleton kullandınız ve nasıl uyguladınız?" diye sorarsa;
 * "Şehir verilerinin her bileşende ayrı ayrı diskten okunması çok maliyetli olurdu ve veriler 
 * senkronize kalamazdı (bir yerdeki hava durumu değişimi diğerine yansımazdı). Bu yüzden Singleton 
 * kullandık. Uygulaması için:
 *  1. Constructor'ı (CityRepository()) 'private' yaparak dışarıdan new ile nesne üretimini engelledik.
 *  2. Kendi türünde static bir 'instance' alanı tanımladık.
 *  3. Dış dünyaya kontrollü erişim sağlayan static 'getInstance()' metodunu yazdık. Çoklu thread 
 *  ortamında (güvenlik için) bu metodu 'synchronized' yaptık." diyebilirsiniz.
 */
public class CityRepository {
    // Sınıfın tekil örneğini (instance) static olarak tutuyoruz
    private static CityRepository instance;
    // Bellekte saklanan tüm şehirlerin listesi
    private List<City> cities;

    /**
     * Private Yapıcı Metod (Constructor)
     * Dışarıdan "new CityRepository()" çağrılarını engeller. 
     * Yalnızca getInstance() metodu içinden bir kez çağrılır.
     */
    private CityRepository() {
        cities = new ArrayList<>();
        loadCitiesFromJson(); // Şehirleri json dosyasından yükle
    }

    /**
     * Dışarıdan tekil nesneye ulaşmak için kullanılan küresel erişim noktası.
     * synchronized anahtar kelimesi ile thread-safe (iş parçacığı açısından güvenli) hale getirilmiştir.
     * 
     * @return CityRepository sınıfının tekil örneği
     */
    public static synchronized CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository(); // İlk çağrıda nesne oluşturulur (Lazy Initialization)
        }
        return instance; // Sonraki çağrılarda hep aynı nesne döndürülür
    }

    /**
     * cities.json dosyasından şehir verilerini okuyan ve Gson kütüphanesi 
     * ile City nesne listesine dönüştüren özel metot.
     */
    private void loadCitiesFromJson() {
        try {
            // cities.json dosyasını utf-8 formatında oku
            Reader reader = new InputStreamReader(
                    getClass().getResourceAsStream("/data/cities.json"), "UTF-8");
            Gson gson = new Gson();
            // Java'daki generic tipleri Gson'a tanıtmak için TypeToken kullanıyoruz
            Type cityListType = new TypeToken<ArrayList<City>>(){}.getType();
            cities = gson.fromJson(reader, cityListType);
            reader.close();
        } catch (Exception e) {
            System.err.println("Failed to load cities.json: " + e.getMessage());
            // JSON yükleme başarısız olursa uygulama çökmesin diye boş bir liste oluştur
            cities = new ArrayList<>();
        }
    }

    /**
     * Bellekteki şehir listesini döndürür.
     */
    public List<City> getCities() {
        return cities;
    }
}
