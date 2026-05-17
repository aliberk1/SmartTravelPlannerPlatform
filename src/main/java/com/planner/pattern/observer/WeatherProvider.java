package com.planner.pattern.observer;

import com.planner.model.City;
import com.planner.model.WeatherState;
import com.planner.pattern.singleton.CityRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WeatherProvider Sınıfı (Observer / Gözlemci Tasarım Deseni - Somut Konu / Subject)
 * 
 * Bu sınıf, arka planda ayrı bir thread (iş parçacığı) olarak çalışan ve her 3 saniyede bir 
 * şehirlerin hava durumu ile sıcaklıklarını rastgele değiştiren sınıftır. Değişim gerçekleştiğinde 
 * kayıtlı olan tüm WeatherObserver (Gözlemci) nesnelerine (örneğin MainController) haber verir.
 * 
 * Sunum Notu: Öğretmen "Bu provider verileri nasıl güncelliyor ve haber veriyor?" diye sorarsa;
 * "WeatherProvider sınıfı Java'nın 'Runnable' arayüzünü uygular. run() metodunda sonsuz bir döngü 
 * içinde 3 saniye uyur (Thread.sleep). Uykudan sonra, Singleton olan CityRepository üzerinden tüm 
 * şehirleri çeker. Şehirlerin sıcaklıklarını ve hava durumlarını rastgele günceller. Hemen ardından 
 * notifyObservers() metodunu çağırarak tüm kayıtlı gözlemcileri uyarır. Gözlemciler de kendi içlerindeki 
 * updateWeather() metodunu çalıştırarak ekranı günceller." diyebilirsiniz.
 */
public class WeatherProvider implements Runnable {
    // Kayıtlı gözlemcilerin (Observers) tutulduğu thread-safe liste
    // CopyOnWriteArrayList kullanarak farklı thread'lerden güvenli iterasyon sağlanır
    private List<WeatherObserver> observers = new CopyOnWriteArrayList<>();
    // Thread'in çalışmaya devam etmesini sağlayan kontrol bayrağı
    private boolean running = true;
    // Sıcaklık ve hava durumu değişimleri için rastgele sayı üreteci
    private Random random = new Random();

    /**
     * Yeni bir gözlemciyi (Örn: GUI denetleyicisi) listeye kaydeder.
     * 
     * @param observer Kaydedilecek gözlemci nesnesi
     */
    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    /**
     * Bir gözlemciyi kayıt listesinden çıkarır.
     * 
     * @param observer Çıkarılacak gözlemci nesnesi
     */
    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    /**
     * Listedeki tüm gözlemcileri tek tek dolaşarak güncellemeyi haber verir.
     */
    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.updateWeather(); // Gözlemcinin güncelleme metodunu tetikler
        }
    }

    /**
     * Arka plan thread çalışmasını güvenli bir şekilde sonlandırmak için kullanılır.
     */
    public void stopProvider() {
        running = false;
    }

    /**
     * Arka plan thread'inin ana gövdesidir.
     */
    @Override
    public void run() {
        while (running) {
            try {
                // Her 3 saniyede bir güncelleme yapmak için bekle
                Thread.sleep(3000); 
                
                // Singleton reposundan tüm şehir listesini al
                List<City> cities = CityRepository.getInstance().getCities();
                WeatherState[] states = WeatherState.values();
                
                // Şehirlerin sıcaklıklarını ve hava durumlarını rastgele güncelle
                for (City city : cities) {
                    // Sıcaklığı -5 ile +5 derece arasında dalgalandır
                    double tempChange = -5 + (10 * random.nextDouble());
                    city.setCurrentTemperature(Math.round((city.getCurrentTemperature() + tempChange) * 10.0) / 10.0);
                    
                    // Hava durumunu (SUNNY, CLOUDY, RAINY, SNOWY) rastgele bir duruma geçir
                    city.setCurrentWeatherState(states[random.nextInt(states.length)]);
                }
                
                // Güncelleme yapıldığını tüm kayıtlı gözlemcilere (Örn: Arayüze) bildir
                notifyObservers();
            } catch (InterruptedException e) {
                running = false; // Hata durumunda döngüden çık
                Thread.currentThread().interrupt(); // Thread kesintisini bildir
            }
        }
    }
}
