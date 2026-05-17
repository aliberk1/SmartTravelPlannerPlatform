package com.planner;

import com.planner.gui.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainApp Sınıfı (JavaFX Uygulama Sınıfı)
 * 
 * Bu sınıf, JavaFX uygulama döngüsünü başlatan, sahneyi (Scene) oluşturan ve 
 * birincil pencereyi (Stage) ekrana getiren ana sınıftır.
 * 
 * Sunum Notu: Öğretmen "Uygulama kapandığında arka plandaki hava durumu güncelleme thread'i 
 * nasıl sonlandırılıyor?" diye sorarsa; "primaryStage.setOnCloseRequest() içinde, 
 * MainController nesnemizin stopWeatherProvider() metodunu çağırarak arka planda çalışan 
 * WeatherProvider thread'inin running bayrağını false yapıyoruz ve System.exit(0) ile 
 * uygulamanın tüm thread'leriyle birlikte temiz bir şekilde sonlanmasını sağlıyoruz." 
 * diyebilirsiniz.
 */
public class MainApp extends Application {
    
    /**
     * JavaFX uygulamasının görsel olarak başlatıldığı yer (start metodu)
     */
    @Override
    public void start(Stage primaryStage) {
        // Kontrolcü (Controller) sınıfını başlatır (Arayüz ve iş mantığı kurulur)
        MainController controller = new MainController();
        
        // Pencerenin boyutunu 1200x800 piksel olarak ayarlayıp sahneyi oluşturur
        Scene scene = new Scene(controller.getRootView(), 1200, 800);
        
        // Pencere başlığını ayarlar
        primaryStage.setTitle("Smart Travel Planner Platform");
        
        // Sahneyi pencereye ekler ve pencereyi görünür kılar
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Uygulama sağ üst köşedeki çarpı butonundan kapatıldığında tetiklenir
        primaryStage.setOnCloseRequest(e -> {
            // Arka planda çalışan hava durumu thread'ini (WeatherProvider) durdurur
            controller.stopWeatherProvider();
            // Uygulamadan temizce çıkış yapar
            System.exit(0);
        });
    }

    /**
     * Main metodu: launch(args) çağrısı ile JavaFX start() yaşam döngüsünü başlatır.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
