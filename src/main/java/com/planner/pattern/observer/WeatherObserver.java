package com.planner.pattern.observer;

/**
 * WeatherObserver Arayüzü (Observer / Gözlemci Tasarım Deseni)
 * 
 * Bu arayüz, hava durumundaki anlık değişiklikleri dinlemek (gözlemlemek) isteyen 
 * sınıfların (Gözlemci - Observer) uygulaması gereken 'updateWeather' metodunu tanımlar.
 * 
 * Sunum Notu: Öğretmen "Observer desenini neden kullandınız?" diye sorarsa;
 * "Hava durumu verileri arka planda (ayrı bir thread olan WeatherProvider üzerinde) sürekli 
 * olarak değişmektedir. Arayüzün (MainController) bu değişimi anında algılayıp grafikleri ve listeleri 
 * güncellemesi gerekiyordu. Arayüzü sürekli sorgu yapmak (polling) zorunda bırakmak yerine, 
 * Observer deseni kullanarak WeatherProvider'ın durum değiştiğinde bizi otomatik olarak uyarmasını (push) 
 * sağladık. Böylece çok daha verimli ve gerçek zamanlı bir sistem kurmuş olduk." diyebilirsiniz.
 */
public interface WeatherObserver {
    /**
     * Hava durumu güncellendiğinde tetiklenen metot. 
     * Gözlemci sınıf bu metot içerisinde gerekli güncelleme işlemlerini yapar.
     */
    void updateWeather();
}
