package com.planner.pattern.composite;

import java.util.List;

/**
 * ActivityLeaf Sınıfı (Composite / Bileşik Tasarım Deseni - Yaprak Düğüm)
 * 
 * Bu sınıf, plan ağacı içerisindeki en küçük, daha fazla alt elemana bölünemeyen 
 * tekil aktiviteleri (Örn: "Visit Museum", "Walk in the Park") temsil eder.
 * Composite desenindeki "Leaf" (Yaprak) rolünü üstlenir.
 * 
 * Sunum Notu: Öğretmen "Yaprak düğüme (Leaf) yeni bir eleman eklemeye çalışırsak ne olur?" 
 * veya "Neden add() ve remove() metodları hata fırlatıyor?" diye sorarsa;
 * "Yaprak düğümler ağacın son uçlarıdır ve alt elemanları olamaz. Bu nedenle, 
 * add() veya remove() işlemleri yapılmaya çalışıldığında 'UnsupportedOperationException' fırlatarak 
 * mimarinin yanlış kullanılmasını engelliyoruz." cevabını verebilirsiniz.
 */
public class ActivityLeaf implements PlanComponent {
    // Aktivitenin adı (Örn: "Walk in the Park")
    private String activityName;
    // Aktivitenin maliyeti (Dolar bazında)
    private double cost;
    // Aktivitenin süresi (Saat bazında)
    private double time;

    /**
     * Aktivite yapıcı metodu (Constructor)
     * 
     * @param activityName Aktivite ismi
     * @param cost Maliyet değeri
     * @param time Süre değeri
     */
    public ActivityLeaf(String activityName, double cost, double time) {
        this.activityName = activityName;
        this.cost = cost;
        this.time = time;
    }

    @Override
    public String getName() {
        return activityName;
    }

    /**
     * Tekil bir aktivite olduğu için doğrudan kendi maliyetini döndürür.
     */
    @Override
    public double getTotalCost() {
        return cost;
    }

    /**
     * Tekil bir aktivite olduğu için doğrudan kendi süresini döndürür.
     */
    @Override
    public double getTotalTime() {
        return time;
    }

    /**
     * Yaprak düğüme alt eleman eklenemez, bu yüzden hata fırlatır.
     */
    @Override
    public void add(PlanComponent component) {
        throw new UnsupportedOperationException("Yaprak düğüme alt eleman eklenemez (Cannot add to a leaf)");
    }

    /**
     * Yaprak düğüme belirli bir pozisyona da alt eleman eklenemez.
     */
    @Override
    public void add(int index, PlanComponent component) {
        throw new UnsupportedOperationException("Yaprak düğüme alt eleman eklenemez (Cannot add to a leaf)");
    }

    /**
     * Yaprak düğümden alt eleman çıkarılamaz, bu yüzden hata fırlatır.
     */
    @Override
    public void remove(PlanComponent component) {
        throw new UnsupportedOperationException("Yaprak düğümden alt eleman çıkarılamaz (Cannot remove from a leaf)");
    }

    /**
     * Alt elemanı olmadığı için null döndürür.
     */
    @Override
    public List<PlanComponent> getChildren() {
        return null;
    }

    /**
     * Konsol çıktısında girintili bir şekilde kendini yazdırır.
     */
    @Override
    public void print(String indent) {
        System.out.println(indent + "- " + getName() + " (Maliyet: " + getTotalCost() + ", Süre: " + getTotalTime() + "saat)");
    }
}
