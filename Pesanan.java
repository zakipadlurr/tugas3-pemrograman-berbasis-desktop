import java.io.*;
import java.util.ArrayList;

public class Pesanan {
    private static class ItemDipesan {
        MenuItem item;
        int jumlah;

        ItemDipesan(MenuItem item, int jumlah) {
            this.item = item;
            this.jumlah = jumlah;
        }
    }

    private ArrayList<ItemDipesan> listPesanan;
    private String namaPelanggan; // untuk nama pelanggan

    public Pesanan() {
        this.listPesanan = new ArrayList<>();
        this.namaPelanggan = "-"; // Default jika tidak diisi
    }

    // Encapsulation: Getter dan Setter untuk Nama Pelanggan
    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public void tambahPesanan(MenuItem item, int jumlah) {
        for (ItemDipesan ip : listPesanan) {
            if (ip.item.getNama().equalsIgnoreCase(item.getNama())) {
                ip.jumlah += jumlah;
                return;
            }
        }
        listPesanan.add(new ItemDipesan(item, jumlah));
    }

    public boolean isEmpty() {
        return listPesanan.isEmpty();
    }

    public void resetPesanan() {
        listPesanan.clear();
        this.namaPelanggan = "-"; // Reset nama setelah transaksi selesai
    }

    public void cetakDanSimpanStruk() {
        StringBuilder struk = new StringBuilder();
        struk.append("\n============================================\n");
        struk.append("               STRUK PESANAN                \n");
        struk.append("============================================\n");
        // Menampilkan nama pelanggan di struk
        struk.append(String.format(" Pelanggan: %-31s\n", namaPelanggan.toUpperCase()));
        struk.append("--------------------------------------------\n");

        double subtotal = 0;
        double diskonPersen = 0;
        double diskonLangsung = 0;

        for (ItemDipesan ip : listPesanan) {
            if (ip.item instanceof Diskon) {
                Diskon d = (Diskon) ip.item;
                diskonPersen += d.getDiskonPercent();
                diskonLangsung += (d.getHarga() * ip.jumlah);
                struk.append(String.format("Diskon: %-20s x%d  -Rp%.2f\n", d.getNama(), ip.jumlah, (d.getHarga() * ip.jumlah)));
            } else {
                double totalItem = ip.item.getHarga() * ip.jumlah;
                subtotal += totalItem;
                struk.append(String.format("%-25s x%d  Rp%.2f\n", ip.item.getNama(), ip.jumlah, totalItem));
            }
        }

        double setelahPotongan = subtotal - diskonLangsung;
        double nilaiDiskonPersen = setelahPotongan * (diskonPersen / 100);
        double totalAkhir = setelahPotongan - nilaiDiskonPersen;

        struk.append("--------------------------------------------\n");
        struk.append(String.format("Subtotal Makanan/Minuman:        Rp%.2f\n", subtotal));
        if (diskonLangsung > 0)     struk.append(String.format("Potongan Diskon Langsung:       -Rp%.2f\n", diskonLangsung));
        if (diskonPersen > 0)       struk.append(String.format("Potongan Persentase (%.0f%%):     -Rp%.2f\n", diskonPersen, nilaiDiskonPersen));
        struk.append("--------------------------------------------\n");
        struk.append(String.format("TOTAL BAYAR:                     Rp%.2f\n", Math.max(totalAkhir, 0)));
        struk.append("============================================\n");

        System.out.print(struk.toString());

        try (PrintWriter out = new PrintWriter(new FileWriter("struk_pesanan.txt"))) {
            out.print(struk.toString());
            System.out.println("[Sistem] Struk belanja berhasil dicetak ke 'struk_pesanan.txt'");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan struk ke file: " + e.getMessage());
        }
    }
}