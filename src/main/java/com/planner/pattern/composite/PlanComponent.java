package com.planner.pattern.composite;

import java.util.List;

/**
 * PlanComponent Arayüzü (Composite / Bileşik Tasarım Deseni)
 * 
 * Bu arayüz, Composite deseninin temelini oluşturur. Hem tekil aktiviteleri (ActivityLeaf - Yaprak) 
 * hem de bu aktiviteleri ve diğer planları içeren üst seyahat planı gruplarını (ActivityPlan - Bileşik) 
 * aynı arayüz üzerinden tek bir tip gibi yönetebilmemizi sağlar.
 * 
 * Sunum Notu: Öğretmen "Composite desenini neden kullandınız?" diye sorarsa;
 * "Kullanıcının seyahat planını oluştururken hem tekil aktiviteler ekleyebilmesini (müze ziyareti gibi), 
 * hem de bu aktiviteleri gruplayan alt planlar (Örn: '1. Gün Planı', 'Öğleden Sonra Planı') oluşturabilmesini 
 * istedik. Composite deseni sayesinde sistem, tek bir aktivite ile 10 aktiviteden oluşan bir alt plan grubuna 
 * tamamen aynı şekilde davranır. Toplam maliyet ve süreyi hesaplarken recursive (özyinelemeli) olarak tüm 
 * alt elemanları dolaşırız." diyebilirsiniz.
 */
public interface PlanComponent {
    /**
     * Bileşenin adını döndürür (Örn: "Müze Gezisi" veya "Hafta Sonu Rotası").
     */
    String getName();

    /**
     * Bileşenin toplam maliyetini döndürür. 
     * Yaprak düğümse kendi maliyetini, Composite düğümse tüm alt elemanlarının maliyetlerinin toplamını verir.
     */
    double getTotalCost();

    /**
     * Bileşenin toplam süresini (saat cinsinden) döndürür.
     * Yaprak düğümse kendi süresini, Composite düğümse tüm alt elemanlarının sürelerinin toplamını verir.
     */
    double getTotalTime();
    
    // --- Composite (Bileşik) İşlemleri ---
    // Bu metotlar, alt eleman ekleme, silme ve listeleme gibi ağaç yapısı işlemlerini tanımlar.

    /**
     * Bu plana yeni bir alt bileşen ekler (Sadece Composite düğümlerde çalışır).
     */
    void add(PlanComponent component);

    /**
     * Bu plana belirli bir pozisyona (index) yeni bir alt bileşen ekler.
     * RemoveComponentCommand.undo() işlemi sırasında silinen elemanı
     * orijinal konumuna geri yerleştirmek için kullanılır.
     *
     * @param index Eklenecek pozisyon (0-tabanlı)
     * @param component Eklenecek bileşen
     */
    void add(int index, PlanComponent component);

    /**
     * Bu plandan bir alt bileşeni çıkarır (Sadece Composite düğümlerde çalışır).
     */
    void remove(PlanComponent component);

    /**
     * Bu planın altındaki tüm elemanları liste olarak döndürür.
     */
    List<PlanComponent> getChildren();

    /**
     * Hata ayıklama ve konsol çıktısı için plan ağacını girintili bir şekilde yazdırır.
     */
    void print(String indent);
}
