package dom_1;

import java.util.concurrent.atomic.AtomicInteger;

public class ParkingAutomat {
    
    private static final AtomicInteger GENERATOR_ID = new AtomicInteger(0);
    
    private final int id;
    private Tarifnik tarifnik;
    private int naplaceniIznos;
    
    public ParkingAutomat() {
        this.id = GENERATOR_ID.incrementAndGet();
        this.tarifnik = null;
        this.naplaceniIznos = 0;
    }
    
    private ParkingAutomat(int id) {
        this.id = id;
        this.tarifnik = null;
        this.naplaceniIznos = 0;
    }
    
    
    public int getId() {
        return id;
    }
    
    public synchronized void setTarifnik(Tarifnik tarifnik) throws ParkingException {
        if (tarifnik == null) {
            throw new ParkingException("Tarifnik ne može biti null");
        }
        this.tarifnik = tarifnik;
        this.naplaceniIznos = 0;
    }
    
    public synchronized Tarifnik getTarifnik() {
        return tarifnik;
    }
    
    public ParkingAutomat napraviKopiju() {
        return new ParkingAutomat(GENERATOR_ID.incrementAndGet());
    }
    
    public synchronized int naplatiParkiranje(Tarifirano vozilo, int brojSati) throws ParkingException {
        if (tarifnik == null) {
            throw new ParkingException(
                "Automat " + id + ": Tarifnik nije definisan - nije moguće naplatiti parkiranje"
            );
        }
        
        if (brojSati <= 0) {
            throw new ParkingException(
                "Automat " + id + ": Broj sati mora biti pozitivan, dobijeno: " + brojSati
            );
        }
        
        int zona = vozilo.getTarifnaZona();
        
        int cenaPoSatu;
        try {
            cenaPoSatu = tarifnik.getCena(zona);
        } catch (ParkingException e) {
            throw new ParkingException(
                "Automat " + id + ": Tarifna zona " + zona + " je van dozvoljenog opsega (0-" + 
                (tarifnik.getBrojZona() - 1) + ")"
            );
        }
        
        int iznos = cenaPoSatu * brojSati;
        naplaceniIznos += iznos;
        
        return iznos;
    }
    
    public synchronized int getNaplaceniIznos() {
        return naplaceniIznos;
    }
    
    @Override
    public synchronized String toString() {
        return id + "(" + naplaceniIznos + ")";
    }
}
