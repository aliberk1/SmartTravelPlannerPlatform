package com.planner.pattern.command;

import java.util.Stack;

/**
 * CommandManager Sınıfı (Command Tasarım Deseni - İstemci / Invoker)
 * 
 * Bu sınıf, uygulamadaki tüm komutların (ekleme, silme, taşıma, temizleme) 
 * yönetiminden ve Undo (Geri Al) / Redo (Yeniden Yap) geçmişinin tutulmasından sorumludur.
 * Geçmiş yönetimi için iki adet LIFO (Son Giren İlk Çıkar) yığını (Stack) kullanır.
 * 
 * Sunum Notu: Öğretmen "Undo ve Redo mekanizmasını arka planda nasıl kodladınız?" diye sorarsa;
 * "İki adet Stack (Yığın) yapısı tanımladık: 'undoStack' ve 'redoStack'. 
 *  1. executeCommand(): Yeni bir komut çalıştırıldığında, execute() metodunu çağırır, komutu 
 *  'undoStack'e koyar ve 'redoStack'i temizler (çünkü yeni bir işlem yapıldığında ileri alma geçmişi sıfırlanır).
 *  2. undo(): 'undoStack'in en üstündeki komutu çeker (pop), komutun undo() metodunu çalıştırarak 
 *  işlemi geri alır ve bu komutu ileri alınabilmesi için 'redoStack'e push'lar.
 *  3. redo(): 'redoStack'in en üstündeki komutu çeker, tekrar execute() çağırır ve 'undoStack'e geri atar." 
 * diyebilirsiniz.
 */
public class CommandManager {
    // Geri alınabilecek komutların tutulduğu yığın (Undo geçmişi)
    private Stack<Command> undoStack = new Stack<>();
    // Yeniden yapılabilecek (ileri alınacak) komutların tutulduğu yığın (Redo geçmişi)
    private Stack<Command> redoStack = new Stack<>();

    /**
     * Yeni bir komutu çalıştırır ve geri alma geçmişine (undoStack) kaydeder.
     * 
     * @param command Çalıştırılacak komut nesnesi
     */
    public void executeCommand(Command command) {
        command.execute(); // Komutu fiilen çalıştır
        undoStack.push(command); // Geri alma geçmişine ekle
        redoStack.clear(); // Yeni bir işlem yapıldığı için ileri alma geçmişini sıfırla
    }

    /**
     * Son yapılan işlemi geri alır (Undo).
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop(); // Son komutu yığından çıkar
            command.undo(); // Komutun kendi içindeki geri alma mantığını çalıştır
            redoStack.push(command); // Bu komutu ileri alınabilmesi için redo yığınına ekle
        }
    }

    /**
     * Geri alınan son işlemi tekrar uygular (Redo).
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop(); // Redo yığınının en üstündeki komutu çıkar
            command.execute(); // Komutu tekrar çalıştır
            undoStack.push(command); // Komutu tekrar geri alınabilmesi için undo yığınına ekle
        }
    }

    /**
     * Durum çubuğunda gösterilmek üzere, geri alınacak son işlemin açıklamasını döndürür.
     */
    public String getUndoDescription() {
        if (undoStack.isEmpty()) return "Nothing"; // Geri alınacak işlem yoksa
        return formatClassName(undoStack.peek().getClass().getSimpleName()); // En üstteki komutun adını biçimlendirip dön
    }

    /**
     * Durum çubuğunda gösterilmek üzere, yeniden yapılacak işlemin açıklamasını döndürür.
     */
    public String getRedoDescription() {
        if (redoStack.isEmpty()) return "Nothing"; // Yeniden yapılacak işlem yoksa
        return formatClassName(redoStack.peek().getClass().getSimpleName());
    }

    /**
     * Komut sınıf isimlerini durum çubuğunda daha şık ve okunabilir 
     * metinlere dönüştüren yardımcı metot.
     */
    private String formatClassName(String name) {
        String base = name.replace("Command", "");
        if (base.equals("AddComponent")) return "Add Plan Component";
        if (base.equals("RemoveComponent")) return "Remove Plan Component";
        if (base.equals("MoveComponent")) return "Move Plan Component";
        if (base.equals("ClearPlan")) return "Clear Active City Tree";
        if (base.equals("AddCityToTrip")) return "Add City to Trip";
        if (base.equals("RemoveCityFromTrip")) return "Remove City from Trip";
        return base;
    }
}
