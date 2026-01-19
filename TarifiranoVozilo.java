package dom_1;

public class TarifiranoVozilo implements Tarifirano {
    
    private final int tarifnaZona;
    
    public TarifiranoVozilo(int tarifnaZona) {
        this.tarifnaZona = tarifnaZona;
    }
    
    @Override
    public int getTarifnaZona() {
        return tarifnaZona;
    }
    
    @Override
    public String toString() {
        return "Vozilo[zona=" + tarifnaZona + "]";
    }
}
