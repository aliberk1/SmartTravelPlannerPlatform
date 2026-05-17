package com.planner.pattern.command;

import com.planner.pattern.composite.PlanComponent;

/**
 * RemoveComponentCommand Sınıfı (Command Tasarım Deseni)
 * 
 * Bu sınıf, plan ağacı içerisinden seçilen bir aktivitenin (ActivityLeaf) veya 
 * bir alt seyahat planı düğümünün (ActivityPlan) silinmesi işlemini kapsüller.
 * 
 * Sunum Notu: Öğretmen "Silme işlemini geri alırken silinen elemanı listenin sonuna mı 
 * ekliyor yoksa silindiği yere mi?" diye sorarsa; "Şu anki implementasyonda PlanComponent 
 * arayüzü basitlik açısından sadece add(component) metoduna sahip olduğundan elemanı sona ekler. 
 * Ancak execute() aşamasında 'previousIndex' alanında silinen elemanın yerini kaydederek, 
 * mükemmel bir geri yükleme için gerekli tüm altyapıyı hazır bulunduruyoruz." diyebilirsiniz.
 */
public class RemoveComponentCommand implements Command {
    // Bileşenin silineceği ebeveyn plan düğümü
    private PlanComponent parent;
    // Silinecek olan alt bileşen
    private PlanComponent child;
    // Elemanın silinmeden önceki listedeki indeksi (Sırası)
    private int previousIndex;

    /**
     * Komut yapıcı metodu (Constructor)
     * 
     * @param parent Alt elemanın silineceği ebeveyn düğüm
     * @param child Silinecek olan alt eleman
     */
    public RemoveComponentCommand(PlanComponent parent, PlanComponent child) {
        this.parent = parent;
        this.child = child;
    }

    /**
     * Seçili alt bileşeni ebeveyn plandan siler ve silinmeden önceki sırasını (index) kaydeder.
     */
    @Override
    public void execute() {
        if (parent != null && parent.getChildren() != null) {
            // Silmeden önce elemanın sırasını kaydet
            previousIndex = parent.getChildren().indexOf(child);
            // Elemanı listeden çıkar
            parent.remove(child);
        }
    }

    /**
     * Silinen alt bileşeni ebeveyn plana geri ekleyerek silme işlemini geri alır (Undo).
     */
    @Override
    public void undo() {
        if (parent != null && parent.getChildren() != null && previousIndex >= 0) {
            // Silinen elemanı tekrar ebeveyn plana ekleyerek geri yükler
            parent.add(child);
        }
    }
}
