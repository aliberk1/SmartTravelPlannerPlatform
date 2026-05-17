package com.planner.pattern.command;

import com.planner.pattern.composite.ActivityPlan;
import java.util.Map;

/**
 * RemoveCityFromTripCommand Sınıfı (Command Tasarım Deseni)
 * 
 * PDF Gereksinimi: "remove city from trip" işlemi undoable bir Command olmalıdır.
 * Bu sınıf, bir şehri seyahat planından çıkarma işlemini kapsüller.
 * Undo ile şehir ve tüm aktivite ağacı geri yüklenir.
 * 
 * Sunum Notu: Öğretmen "Şehri trip'ten çıkardığınızda planlar da mı siliniyor?"
 * diye sorarsa; "Evet, cityPlans Map'inden şehir kaldırılır ama undo() sırasında
 * yedeklenen ActivityPlan referansı (previousPlan) tekrar Map'e yerleştirilerek
 * tüm ağaç yapısı eksiksiz geri yüklenir." diyebilirsiniz.
 */
public class RemoveCityFromTripCommand implements Command {
    // Tüm şehir planlarının tutulduğu Map referansı
    private Map<String, ActivityPlan> cityPlans;
    // Trip'ten çıkarılacak şehrin adı
    private String cityName;
    // Silinen planın yedeği (undo için)
    private ActivityPlan previousPlan;

    /**
     * Komut yapıcı metodu
     * 
     * @param cityPlans Tüm şehir planlarının saklandığı Map
     * @param cityName Çıkarılacak şehrin adı
     */
    public RemoveCityFromTripCommand(Map<String, ActivityPlan> cityPlans, String cityName) {
        this.cityPlans = cityPlans;
        this.cityName = cityName;
        // Silmeden önce planı yedekle
        this.previousPlan = cityPlans.get(cityName);
    }

    /**
     * Şehri trip'ten çıkarır: cityPlans Map'inden kaldırır.
     */
    @Override
    public void execute() {
        cityPlans.remove(cityName);
    }

    /**
     * Çıkarılan şehri ve tüm aktivite ağacını geri yükler.
     */
    @Override
    public void undo() {
        if (previousPlan != null) {
            cityPlans.put(cityName, previousPlan);
        }
    }
}
