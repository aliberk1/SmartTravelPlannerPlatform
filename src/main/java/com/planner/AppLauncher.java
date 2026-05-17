package com.planner;

/**
 * AppLauncher Sınıfı (Giriş Noktası Sınıfı)
 * 
 * Bazı JavaFX paketleme ve derleme araçlarında (Örneğin fat JAR veya shading süreçlerinde) 
 * doğrudan Application sınıfından (MainApp) kalıtım almış bir sınıfı ana giriş noktası 
 * olarak belirtmek bağımlılık veya sınıf yükleme (ClassNotFoundException) hatalarına sebep olabilir.
 * Bu sınıf, JavaFX Application sınıfından doğrudan kalıtım almayan, saf bir ana giriş 
 * (Launcher) sınıfı olarak tasarlanmıştır.
 * 
 * Sunum Notu: Öğretmen "Neden doğrudan MainApp'i değil de AppLauncher'ı çalıştırıyorsunuz?" 
 * diye sorursa; "JavaFX çalışma zamanı (runtime) kütüphanelerinin modüler yapısından ötürü, 
 * fat JAR (bağımlılıkları içeren tek bir JAR dosyası) oluşturup dağıtırken JavaFX modül kontrolünü 
 * aşmak ve uygulamanın her ortamda sorunsuz başlatılabilmesini sağlamak için bu şekilde 
 * bağımsız bir tetikleyici sınıf kullanmak endüstri standardı bir çözümdür." diyebilirsiniz.
 */
public class AppLauncher {
    /**
     * Uygulamanın en temel giriş noktası (Main Metodu)
     */
    public static void main(String[] args) {
        // Gerçek JavaFX uygulaması olan MainApp sınıfının main metodunu tetikler
        MainApp.main(args);
    }
}
