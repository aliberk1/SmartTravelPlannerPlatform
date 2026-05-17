package com.planner.pattern.command;

import com.planner.pattern.composite.PlanComponent;
import java.util.Collections;
import java.util.List;

/**
 * MoveComponentCommand Sınıfı (Command Tasarım Deseni)
 * 
 * Bu sınıf, plan ağacı içerisindeki bir aktivitenin veya alt planın sırasını yukarı/aşağı 
 * taşımak (yani listedeki yerini değiştirmek) için kullanılır.
 * 
 * Sunum Notu: Öğretmen "Yukarı/aşağı taşıma işlemini geri almak için ayrı bir kod mu yazdınız?"
 * derse; "Hayır, yukarı taşıma yönü -1, aşağı taşıma yönü +1 ise; undo() metodunda bu yönün 
 * tersini (-direction) parametre olarak vererek tek bir move() fonksiyonuyla hem ileri hem de 
 * geri alma işlemini çok temiz bir şekilde çözdük." diyebilirsiniz.
 */
public class MoveComponentCommand implements Command {
    // Bileşenin içinde bulunduğu ebeveyn plan düğümü
    private PlanComponent parent;
    // Sırası değiştirilecek olan alt bileşen
    private PlanComponent child;
    // Hareket yönü: Yukarı için -1, Aşağı için +1
    private int direction; 
    
    /**
     * Komut yapıcı metodu (Constructor)
     * 
     * @param parent Ebeveyn düğüm
     * @param child Taşınacak olan çocuk düğüm
     * @param direction Hareket yönü (-1: yukarı, 1: aşağı)
     */
    public MoveComponentCommand(PlanComponent parent, PlanComponent child, int direction) {
        this.parent = parent;
        this.child = child;
        this.direction = direction;
    }

    /**
     * Belirtilen yöne doğru taşıma işlemini gerçekleştirir.
     */
    @Override
    public void execute() {
        move(direction);
    }

    /**
     * Taşıma işlemini tam tersi yöne hareket ettirerek geri alır.
     */
    @Override
    public void undo() {
        move(-direction); // Hareket yönünü tersine çevirerek geri alma işlemini sağlar
    }
    
    /**
     * Liste elemanlarının yerini değiştiren yardımcı metot.
     * 
     * @param dir Hareket yönü (-1 veya +1)
     */
    private void move(int dir) {
        if (parent != null && parent.getChildren() != null) {
            List<PlanComponent> list = parent.getChildren();
            int index = list.indexOf(child); // Elemanın şu anki sırasını (indeksini) bul
            if (index >= 0) {
                int newIndex = index + dir; // Yeni indeksi hesapla
                // Yeni indeksin sınırlar içinde olup olmadığını kontrol et (taşma olmasın)
                if (newIndex >= 0 && newIndex < list.size()) {
                    // Java Collections sınıfının swap metodu ile iki elemanın yerini değiştir
                    Collections.swap(list, index, newIndex);
                }
            }
        }
    }
}
