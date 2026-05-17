package com.planner.pattern.command;

import com.planner.pattern.composite.PlanComponent;

/**
 * AddComponentCommand Sınıfı (Command Tasarım Deseni)
 * 
 * Bu sınıf, Composite (Bileşik) yapıdaki bir plana (ActivityPlan) yeni bir alt bileşen 
 * (ActivityLeaf veya başka bir ActivityPlan) ekleme işlemini temsil eder.
 * 
 * Sunum Notu: Öğretmen "Bu komut ne işe yarıyor ve geri almayı nasıl yapıyor?" diye sorarsa;
 * "Bu sınıf bir aktiviteyi plana ekleme işlemini kapsüller. execute() çağrıldığında
 * hedef ebeveyn plana (parent) alt bileşeni (child) ekler. undo() çağrıldığında ise 
 * eklenen bu alt bileşeni ebeveyn plandan çıkararak işlemi tam olarak geri alır." diyebilirsiniz.
 */
public class AddComponentCommand implements Command {
    // Bileşenin ekleneceği üst plan (Composite nesnesi)
    private PlanComponent parent;
    // Eklenecek olan alt bileşen (Yaprak veya başka bir plan)
    private PlanComponent child;

    /**
     * Komut yapıcı metodu (Constructor)
     * 
     * @param parent Alt bileşenin ekleneceği ebeveyn düğüm
     * @param child Eklenecek olan alt bileşen
     */
    public AddComponentCommand(PlanComponent parent, PlanComponent child) {
        this.parent = parent;
        this.child = child;
    }

    /**
     * Komutu çalıştırır (Aktivite ekleme işlemini gerçekleştirir).
     */
    @Override
    public void execute() {
        if (parent != null) {
            parent.add(child); // Ebeveyn plana bileşeni ekler
        }
    }

    /**
     * Komutun etkilerini geri alır (Eklenen aktiviteyi silerek işlemi geri alır).
     */
    @Override
    public void undo() {
        if (parent != null) {
            parent.remove(child); // Eklenen bileşeni ebeveyn plandan siler
        }
    }
}
