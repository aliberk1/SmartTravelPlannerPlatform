package com.planner.pattern.command;

import com.planner.pattern.composite.ActivityPlan;
import java.util.Map;

/**
 * AddCityToTripCommand Sınıfı (Command Tasarım Deseni)
 * 
 * PDF Gereksinimi: "add city to trip" işlemi undoable bir Command olmalıdır.
 * Bu sınıf, bir şehri seyahat planına ekleme işlemini kapsüller.
 * Şehir, cityPlans Map'ine eklenir ve undo ile geri alınabilir.
 * 
 * Sunum Notu: Öğretmen "Şehri trip'e ekleme işlemi neden Command olarak sarmalandı?"
 * diye sorarsa; "PDF şartnamesinde 'add city to trip' undoable eylemler listesinde yer alıyor.
 * Bu sayede kullanıcı yanlışlıkla bir şehri eklerse Undo butonuyla geri alabilir." diyebilirsiniz.
 */
public class AddCityToTripCommand implements Command {
    // Tüm şehir planlarının tutulduğu Map referansı
    private Map<String, ActivityPlan> cityPlans;
    // Trip'e eklenecek şehrin adı
    private String cityName;
    // Oluşturulan veya mevcut plan referansı
    private ActivityPlan plan;
    // Şehir daha önce Map'te var mıydı? (undo için kontrol)
    private boolean alreadyExisted;

    /**
     * Komut yapıcı metodu
     * 
     * @param cityPlans Tüm şehir planlarının saklandığı Map
     * @param cityName Eklenecek şehrin adı
     */
    public AddCityToTripCommand(Map<String, ActivityPlan> cityPlans, String cityName) {
        this.cityPlans = cityPlans;
        this.cityName = cityName;
        this.alreadyExisted = cityPlans.containsKey(cityName);
    }

    /**
     * Şehri trip'e ekler: cityPlans Map'ine yeni bir ActivityPlan koyar.
     * Şehir zaten mevcutsa tekrar ekleme yapmaz.
     */
    @Override
    public void execute() {
        if (!cityPlans.containsKey(cityName)) {
            plan = new ActivityPlan(cityName + " Root Plan");
            cityPlans.put(cityName, plan);
        }
    }

    /**
     * Eklenen şehri trip'ten çıkararak işlemi geri alır.
     * Şehir daha önce mevcutsa (alreadyExisted), undo sırasında silmez.
     */
    @Override
    public void undo() {
        if (!alreadyExisted) {
            cityPlans.remove(cityName);
        }
    }
}
