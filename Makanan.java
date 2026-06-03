public class Makanan extends MenuItem {
    private String jenisMakanan; 

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        this.jenisMakanan = jenisMakanan;
    }

    public String getJenisMakanan() { return jenisMakanan; }

    @Override
    public void tampilMenu() {
        System.out.printf("[Makanan] %-20s | Harga: Rp%-10.2f | Jenis: %s\n", 
                getNama(), getHarga(), jenisMakanan);
    }
}