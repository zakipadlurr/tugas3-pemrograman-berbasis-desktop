import java.util.Scanner;

public class MainApp {
    private static Menu menuRestoran = new Menu();
    private static Pesanan keranjangPelanggan = new Pesanan(); 
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean berjalan = true;
        while (berjalan) {
            System.out.println("\n=== APLIKASI MANAJEMEN RESTORAN BANG ZACK ===");
            System.out.println("1. Tambah Item Baru ke Menu");
            System.out.println("2. Tampilkan Menu Restoran");
            System.out.println("3. Terima Pesanan Baru");
            System.out.println("4. Hitung Total Biaya & Tampilkan Struk");
            System.out.println("5. Simpan Daftar Menu");
            System.out.println("6. Keluar");
            System.out.print("Pilih opsi (1-6): ");

            try {
                int pilihan = Integer.parseInt(scanner.nextLine());
                switch (pilihan) {
                    case 1:
                        menuTambahItem();
                        break;
                    case 2:
                        menuRestoran.tampilkanSemuaMenu();
                        break;
                    case 3:
                        menuProsesPesanan();
                        break;
                    case 4:
                        if (keranjangPelanggan.isEmpty()) {
                            System.out.println("\n[Peringatan] Belum ada transaksi pesanan aktif yang tercatat!");
                        } else {
                            keranjangPelanggan.cetakDanSimpanStruk();
                            keranjangPelanggan.resetPesanan(); 
                        }
                        break;
                    case 5:
                        System.out.println("\n[Proses] Menghubungkan ke database file eksternal...");
                        menuRestoran.simpanMenuKeFile();
                        break;
                    case 6:
                        berjalan = false;
                        System.out.println("Terima kasih! Keluar dari program.");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid! Masukkan angka 1 sampai 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[Error] Input menu utama harus berupa angka!");
            }
        }
    }

    private static void menuTambahItem() {
        while (true) {
            System.out.println("\n--- TAMBAH ITEM MENU ---");
            System.out.println("1. Tambah Makanan");
            System.out.println("2. Tambah Minuman");
            System.out.println("3. Tambah Diskon");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih jenis (1-4): ");

            try {
                int pilihan = Integer.parseInt(scanner.nextLine());
                if (pilihan == 4) {
                    System.out.println("Kembali ke menu utama...");
                    break; 
                }

                if (pilihan < 1 || pilihan > 3) {
                    System.out.println("Pilihan tidak sesuai!");
                    continue;
                }

                System.out.print("Masukkan Nama Item: ");
                String nama = scanner.nextLine();

                if (pilihan == 3) {
                    System.out.print("Masukkan Persentase Diskon (0-100%): ");
                    double persen = Double.parseDouble(scanner.nextLine());
                    menuRestoran.tambahItem(new Diskon(nama, persen));
                } else {
                    System.out.print("Masukkan Harga: ");
                    double harga = Double.parseDouble(scanner.nextLine());

                    if (pilihan == 1) {
                        System.out.print("Masukkan Jenis Makanan (Berat/Camilan): ");
                        String jenis = scanner.nextLine();
                        menuRestoran.tambahItem(new Makanan(nama, harga, jenis));
                    } else if (pilihan == 2) {
                        System.out.print("Masukkan Jenis Minuman (Dingin/Panas): ");
                        String jenis = scanner.nextLine();
                        menuRestoran.tambahItem(new Minuman(nama, harga, jenis));
                    }
                }

                System.out.println("✓ Item '" + nama + "' berhasil dimasukkan ke daftar list sementara.");
                System.out.println("(Gunakan opsi nomor 5 pada menu utama jika ingin menyimpannya secara permanen)");
                break; 

            } catch (NumberFormatException e) {
                System.out.println("[Error] Input nominal/persentase harus berformat angka!");
            }
        }
    }

    // Diperbarui untuk meminta nama pelanggan di awal transaksi
    private static void menuProsesPesanan() {
        if (menuRestoran.getDaftarMenu().isEmpty()) {
            System.out.println("\n[Informasi] Menu restoran kosong! Tambahkan menu terlebih dahulu.");
            return;
        }

        // Jika ini adalah transaksi baru (keranjang masih kosong), minta input nama pelanggan
        if (keranjangPelanggan.isEmpty()) {
            System.out.print("\nMasukkan Nama Pelanggan: ");
            String nama = scanner.nextLine().trim();
            if (nama.isEmpty()) {
                nama = "Pelanggan Tanpa Nama";
            }
            keranjangPelanggan.setNamaPelanggan(nama);
        }

        while (true) {
            menuRestoran.tampilkanSemuaMenu();
            System.out.println("Pelanggan Aktif: " + keranjangPelanggan.getNamaPelanggan().toUpperCase());
            System.out.println("Ketik [Angka Menu] untuk mencatat item ke keranjang.");
            System.out.println("Ketik [Selesai] untuk mengakhiri pencatatan & kembali ke menu utama.");
            System.out.print("Pilihan Anda: ");

            String inputUser = scanner.nextLine().trim();

            if (inputUser.equalsIgnoreCase("selesai")) {
                System.out.println("Pencatatan pesanan selesai. Kembali ke menu utama.");
                break;
            }

            try {
                int kodePilihan = Integer.parseInt(inputUser);

                if (kodePilihan < 1 || kodePilihan > menuRestoran.getDaftarMenu().size()) {
                    throw new IndexOutOfBoundsException("Nomor menu tidak ditemukan di dalam daftar!");
                }

                MenuItem itemTerpilih = menuRestoran.getDaftarMenu().get(kodePilihan - 1);

                System.out.print("Masukkan Jumlah (Kuantitas) untuk " + itemTerpilih.getNama() + ": ");
                int kuantitas = Integer.parseInt(scanner.nextLine());

                if (kuantitas <= 0) {
                    System.out.println("[Peringatan] Jumlah pesanan minimal berangka 1!");
                    continue;
                }

                keranjangPelanggan.tambahPesanan(itemTerpilih, kuantitas);
                System.out.println("✓ Tercatat: " + kuantitas + "x " + itemTerpilih.getNama() + " dimasukkan ke keranjang.\n");

            } catch (NumberFormatException e) {
                System.out.println("[Error] Masukkan nomor menu yang valid atau ketik 'Selesai' untuk kembali!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("[Exception Error] " + e.getMessage());
            }
        }
    }
}