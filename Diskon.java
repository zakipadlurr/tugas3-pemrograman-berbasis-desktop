public class Diskon extends MenuItem {
    private double diskonPercent; 

    public Diskon(String nama, double diskonPercent) {
        super(nama, 0, "Diskon"); 
        this.diskonPercent = diskonPercent;
    }

    public double getDiskonPercent() { return diskonPercent; }

    @Override
    public void tampilMenu() {
        System.out.printf("[Diskon]  %-20s | Persentase Potongan: %.0f%%\n", 
                getNama(), diskonPercent);
    }
}