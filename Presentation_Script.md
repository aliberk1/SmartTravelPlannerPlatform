# 🎓 SENG 324 - Smart Travel Planner Sunum Senaryosu

Bu doküman, projeyi hocaya veya jüriye sunarken hangi adımları izleyeceğinizi ve hangi cümleyi kurarken nereye tıklamanız gerektiğini adım adım anlatır. 

Eğer 3 kişilik bir gruptaysanız, sunumu 3 bölüme ayırabilirsiniz.

---

## 🎤 BÖLÜM 1: Giriş, Veri ve Sıralama (1. Öğrenci)

**Öğrenci:** "Hocam merhabalar, SENG 324 dersi Smart Travel Planner projemizi sunacağız. Projemizi JavaFX kullanarak modern bir masaüstü uygulaması olarak geliştirdik ve sizden istenen 7 tasarım deseninin tamamını uyguladık."

**(Fare ile uygulamanın sol üstüne gelin)**
**Öğrenci:** "Öncelikle **Singleton** desenimizden bahsedeyim. Sistem ayağa kalktığında `CityRepository` sınıfımız `cities.json` dosyasını sadece 1 kez okuyor. Tüm uygulama veriyi bu tek noktadan alıyor. Burada sol üstte tüm şehirlerimizi görüyoruz."

**(Fare ile 'Sort by Name' yazan ComboBox'a tıklayın ve 'Sort by Population' seçin. Listenin değiştiğini gösterin)**
**Öğrenci:** "İkinci olarak **Strategy** desenimizi kullandık. Şehirleri isme, nüfusa veya yüzölçümüne göre sıralamak için farklı strateji algoritmaları yazdık. Gördüğünüz gibi combobox'tan seçimi değiştirdiğimizde çalışma anında (runtime) sıralama algoritması değişiyor."

**(Fare ile hemen altındaki Hava Durumu (All, SUNNY, vb.) ComboBox'ına tıklayın ve 'RAINY' seçin)**
**Öğrenci:** "Üçüncü desenimiz **Iterator** deseni. Şehir listesi üzerinde gezinirken spesifik hava durumlarına göre (örneğin sadece yağmurlu şehirleri) filtreleme yapmamızı sağlıyor. Listemiz sadece Iterator'ın izin verdiği kayıtları ekrana basıyor."

---

## 🎤 BÖLÜM 2: Canlı Simülasyon ve Grafikler (2. Öğrenci)

**(Fareyi ortadaki Bar ve Pasta Grafiklerine getirin. Ekranda grafiklerin 3 saniyede bir oynamasını izletin)**

**Öğrenci:** "Şimdi uygulamanın ortasındaki dinamik grafik paneline gelelim. Burada dördüncü desenimiz olan **Observer** desenini kullandık. Sistemimizde ayrı bir Thread (iş parçacığı) olarak çalışan bir `WeatherProvider` (Hava Durumu Sağlayıcı) simülatörümüz var. Bu simülatör her 3 saniyede bir şehirlerin sıcaklıklarını ve hava durumlarını rastgele değiştiriyor."

**Öğrenci:** "Burada hem Bar grafiğimiz hem de Pasta grafiğimiz bu simülatöre `Observer` (Abone) olarak bağlandı. Biz grafikleri manuel güncellemeksizin, hava durumu datası her değiştiğinde grafikler Subscriber oldukları için *push* mantığıyla anında kendi kendilerini güncelliyorlar."

---

## 🎤 BÖLÜM 3: Gezi Planlayıcısı, İç İçe Yapı ve Undo/Redo (3. Öğrenci)

**(Sol taraftaki All Cities listesinden 'Istanbul'a tıklayın. Sağ alttaki 'Add Selected City as Plan Node' butonuna basın. Sağdaki ağaçta Istanbul belirecek)**

**Öğrenci:** "Gelelim Seyahat Planlama (Travel Planner) kısmına. Projemizin sağ tarafında beşinci ve altıncı desenlerimiz olan **Composite** ve **Decorator** desenlerini birlikte kullandık. Öncelikle Composite yapısıyla Ağaç (Tree) mimarisi kurduk. Ana planın altına şehirleri düğüm (node) olarak ekliyoruz."

**(Sağ alttaki 'Add Museum' ve 'Add Mall' butonlarına basın. Ağaçta Istanbul'un altına eklendiklerini ve üstteki Total Cost/Time yazısının arttığını gösterin)**

**Öğrenci:** "Seçtiğimiz bir şehrin altına Aktiviteler eklediğimizde, ağaç hiyerarşisi oluşuyor ve *Toplam Maliyet* ile *Toplam Süre* Composite deseni sayesinde özyinelemeli (recursive) olarak tüm ağaçta toplanarak hesaplanıyor."

**Öğrenci:** "Aktivitelerin (Müze, Park vb.) maliyet ve sürelerini ise temel bir şehir ziyaretinin üzerine sarmalayarak, yani **Decorator** deseni kullanarak hesapladık. Böylece mevcut City nesnemizi hiç bozmadan ona yeni özellikler giydirmiş olduk."

**(Sağdaki 'Undo' butonuna tıklayın. Son eklenen Mall silinsin. Sonra 'Redo' deyin geri gelsin. Sonra 'Clear Plan' diyerek hepsini temizleyin)**

**Öğrenci:** "Son ve yedinci desenimiz ise **Command** deseni. Tüm bu ağaca ekleme, silme ve temizleme işlemlerini birer Command objesi haline getirdik ve bir Stack mekanizmasıyla `CommandManager` içinde tuttuk. Bu sayede gördüğünüz gibi Undo (Geri Al) ve Redo (Yinele) işlemlerini kusursuz bir şekilde yönetebiliyoruz."

**Öğrenci:** "Bizi dinlediğiniz için teşekkür ederiz. Projemizle ilgili sormak istediğiniz sorular varsa yanıtlayabiliriz."

---

## 💡 Ekstra İpuçları (Sunum Sırasında Hoca Sorarsa)
- **Hoca:** "Neden JavaFX kullandınız?" 
  - **Cevap:** "Swing eski ve Observer deseninde dinamik grafik animasyonlarını (özellikle chartların anlık zıplamasını) yansıtmakta zayıf olduğu için modern ve Thread-safe bir araç olan JavaFX tercih ettik."
- **Hoca:** "Command pattern'de Undo yaparken veriyi nasıl siliyorsunuz?" 
  - **Cevap:** "Her komut kendi içinde `execute()` (çalıştır) ve `undo()` (tersine çevir) metotlarını barındırıyor. Undo tetiklendiğinde `Remove` metodunu çağırıp o objeyi Ağaçtan çıkartıyoruz."
- **Hoca:** "Composite'in neresi Node neresi Leaf?" 
  - **Cevap:** "`ActivityPlan` node'dur yani içine başka şeyler alabilir. `ActivityLeaf` ise yapraktır, yani son noktadır aktivitenin ta kendisidir içine obje almaz."
