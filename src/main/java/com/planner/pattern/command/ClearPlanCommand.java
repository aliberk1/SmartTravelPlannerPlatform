package com.planner.pattern.command;

import com.planner.pattern.composite.PlanComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * ClearPlanCommand Sınıfı (Command Tasarım Deseni)
 * 
 * Bu sınıf, aktif olan şehrin tüm seyahat planı ağacını (tüm aktiviteleri ve alt planları) 
 * tek bir seferde temizleme (silme) işlemini yönetir.
 * 
 * Sunum Notu: Öğretmen "Bu temizleme işlemini nasıl geri alıyorsunuz? Veriler kaybolmuyor mu?" 
 * diye sorarsa; "Komut oluşturulduğunda constructor içinde kök düğümün mevcut tüm alt elemanlarının 
 * (children) kopyasını 'previousChildren' adlı geçici bir listede yedekliyoruz. execute() çağrıldığında
 * ağacı temizliyoruz, ancak undo() çağrıldığında o yedeklediğimiz listeyi kök düğüme geri yükleyerek 
 * tüm ağacı eski haline getirebiliyoruz." cevabını verebilirsiniz.
 */
public class ClearPlanCommand implements Command {
    // Temizlenecek olan planın kök bileşeni (Root)
    private PlanComponent root;
    // Temizlemeden önceki alt elemanların yedeği (Geri alma işlemi için)
    private List<PlanComponent> previousChildren;

    /**
     * Komut yapıcı metodu (Constructor)
     * 
     * @param root Temizlenecek plan ağacının kök elemanı
     */
    public ClearPlanCommand(PlanComponent root) {
        this.root = root;
        // Geri alma işlemi için mevcut alt bileşenleri yedekle
        if (root.getChildren() != null) {
            this.previousChildren = new ArrayList<>(root.getChildren());
        }
    }

    /**
     * Plan ağacını tamamen temizler (Arayüzdeki "Clear Active City Tree" butonunun işlevidir).
     */
    @Override
    public void execute() {
        if (root != null && root.getChildren() != null) {
            root.getChildren().clear(); // Kök düğümün tüm alt elemanlarını siler
        }
    }

    /**
     * Temizlenen planı ve tüm alt bileşenlerini yedekten yükleyerek geri alır (Undo).
     */
    @Override
    public void undo() {
        if (root != null && previousChildren != null) {
            root.getChildren().clear();
            root.getChildren().addAll(previousChildren); // Yedeklenen elemanları geri ekler
        }
    }
}
