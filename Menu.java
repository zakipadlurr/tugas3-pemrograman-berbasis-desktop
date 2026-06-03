import java.io.*;
import java.util.ArrayList;

public class Menu {
    private ArrayList<MenuItem> daftarMenu;
    private final String FILE_MENU = "daftar_menu.txt";

    public Menu() {
        daftarMenu = new ArrayList<>();
        muatMenuDariFile(); 
    }

    public void tambahItem(MenuItem item) {
        daftarMenu.add(item);
    }

    public ArrayList<MenuItem> getDaftarMenu() {
        return daftarMenu;
    }

    public void tampilkanSemuaMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("\n[Informasi] Belum ada menu yang tersedia. Silakan tambah menu baru terlebih dahulu!");
            return;
        }
        System.out.println("\n==================== DAFTAR MENU RESTORAN BANG ZACK ====================");
        for (int i = 0; i < daftarMenu.size(); i++) {
            System.out.print((i + 1) + ". ");
            daftarMenu.get(i).tampilMenu();
        }
        System.out.println("==============================================================");
    }

    public void simpanMenuKeFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_MENU))) {
            int jumlahTersimpan = 0;
            for (MenuItem item : daftarMenu) {
                if (item instanceof Makanan) {
                    Makanan m = (Makanan) item;
                    writer.println("MAKANAN;" + m.getNama() + ";" + m.getHarga() + ";" + m.getJenisMakanan());
                } else if (item instanceof Minuman) {
                    Minuman mn = (Minuman) item;
                    writer.println("MINUMAN;" + mn.getNama() + ";" + mn.getHarga() + ";" + mn.getJenisMinuman());
                } else if (item instanceof Diskon) {
                    Diskon d = (Diskon) item;
                    // Hanya menyimpan nama dan persentase diskon
                    writer.println("DISKON;" + d.getNama() + ";" + d.getDiskonPercent());
                }
                jumlahTersimpan++;
            }
            System.out.println("[Sukses] Berhasil mengekspor " + jumlahTersimpan + " item menu ke dalam file '" + FILE_MENU + "'.");
            
        } catch (IOException e) {
            System.out.println("[Error] Gagal menyimpan menu ke file teks: " + e.getMessage());
        }
    }

    public void muatMenuDariFile() {
        File file = new File(FILE_MENU);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 3) continue; 

                String tipe = tokens[0];
                String nama = tokens[1];

                if (tipe.equals("MAKANAN") && tokens.length == 4) {
                    double harga = Double.parseDouble(tokens[2]);
                    String jenis = tokens[3];
                    daftarMenu.add(new Makanan(nama, harga, jenis));
                } else if (tipe.equals("MINUMAN") && tokens.length == 4) {
                    double harga = Double.parseDouble(tokens[2]);
                    String jenis = tokens[3];
                    daftarMenu.add(new Minuman(nama, harga, jenis));
                } else if (tipe.equals("DISKON")) {
                    // Memuat data diskon berdasarkan persentase langsung
                    double percent = Double.parseDouble(tokens[2]);
                    daftarMenu.add(new Diskon(nama, percent));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Gagal memuat menu dari file: " + e.getMessage());
        }
    }
}