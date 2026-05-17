package com.planner.pattern.command;

/**
 * Command Arayüzü (Command Tasarım Deseni)
 * 
 * Bu arayüz, tüm komut sınıflarının uygulaması gereken temel metotları tanımlar.
 * Command deseni, bir isteği nesne haline getirerek parametreleştirmemizi, 
 * kuyruğa almamızı, kaydetmemizi ve en önemlisi Undo (Geri Al) / Redo (Yeniden Yap) 
 * işlemlerini gerçekleştirmemizi sağlar.
 */
public interface Command {
    /**
     * Komutu çalıştırır. İlgili eylemi (ekleme, silme, taşıma vb.) gerçekleştirir.
     */
    void execute();

    /**
     * Komutun etkilerini geri alır (Undo). 
     * Sistem durumunu komut çalıştırılmadan önceki haline döndürür.
     */
    void undo();
}
