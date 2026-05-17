package com.planner.pattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * ActivityPlan Sınıfı (Composite / Bileşik Tasarım Deseni - Bileşik Düğüm)
 * 
 * Bu sınıf, içerisinde birden fazla aktiviteyi (ActivityLeaf) veya diğer alt planları 
 * (ActivityPlan) barındırabilen seyahat planı gruplarını temsil eder.
 * Composite desenindeki "Composite" (Bileşik) rolünü üstlenir.
 * 
 * Sunum Notu: Öğretmen "Bir plan grubunun toplam maliyetini ve süresini nasıl hesaplıyorsunuz?" 
 * diye sorarsa; "Her ActivityPlan kendi içinde bir eleman listesi (children) tutar. 
 * getTotalCost() ve getTotalTime() metotları çağrıldığında, liste içerisindeki tüm alt elemanların 
 * ilgili metotlarını özyinelemeli (recursive) olarak çağırıp toplarız. Böylece ağacın derinliği ne 
 * kadar olursa olsun, en tepedeki kök düğüm tüm ağacın toplam değerini otomatik olarak hesaplar." 
 * cevabını verebilirsiniz.
 */
public class ActivityPlan implements PlanComponent {
    // Plan grubunun adı (Örn: "1. Gün Planı", "Kültür Turu")
    private String planName;
    // Bu planın altındaki tüm bileşenlerin listesi (Yapraklar veya diğer alt planlar)
    private List<PlanComponent> children;

    /**
     * Plan grubu yapıcı metodu (Constructor)
     * 
     * @param planName Plan ismi
     */
    public ActivityPlan(String planName) {
        this.planName = planName;
        this.children = new ArrayList<>();
    }

    @Override
    public String getName() {
        return planName;
    }

    /**
     * Bu planın altındaki tüm elemanların maliyetlerini recursive olarak toplar.
     */
    @Override
    public double getTotalCost() {
        double cost = 0;
        for (PlanComponent child : children) {
            cost += child.getTotalCost(); // Her bir alt elemanın kendi toplam maliyetini alır
        }
        return cost;
    }

    /**
     * Bu planın altındaki tüm elemanların sürelerini recursive olarak toplar.
     */
    @Override
    public double getTotalTime() {
        double time = 0;
        for (PlanComponent child : children) {
            time += child.getTotalTime(); // Her bir alt elemanın kendi toplam süresini alır
        }
        return time;
    }

    /**
     * Plan grubuna yeni bir alt seyahat elemanı (Yaprak veya başka bir Plan) ekler.
     */
    @Override
    public void add(PlanComponent component) {
        children.add(component);
    }

    /**
     * Plan grubundan bir alt seyahat elemanını çıkarır.
     */
    @Override
    public void remove(PlanComponent component) {
        children.remove(component);
    }

    /**
     * Bu planın altındaki eleman listesini döndürür (TreeView doldurulurken kullanılır).
     */
    @Override
    public List<PlanComponent> getChildren() {
        return children;
    }

    /**
     * Hata ayıklama çıktısı için planı girintili yazar ve tüm alt elemanlarını da yazdırır.
     */
    @Override
    public void print(String indent) {
        System.out.println(indent + "+ " + getName() + " (Maliyet: " + getTotalCost() + ", Süre: " + getTotalTime() + "saat)");
        for (PlanComponent child : children) {
            child.print(indent + "  "); // Alt elemanların girintisini arttırarak recursive olarak yazdırır
        }
    }
}
