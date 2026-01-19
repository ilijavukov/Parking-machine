package dom_1;

import java.util.Arrays;

public class Tarifnik {
    
    private final int[] cenePoZonama;
    
    public Tarifnik(int[] cene) throws ParkingException {
        if (cene == null || cene.length == 0) {
            throw new ParkingException("Niz cena ne sme biti null ili prazan");
        }
        this.cenePoZonama = Arrays.copyOf(cene, cene.length);
    }
    
    public int getBrojZona() {
        return cenePoZonama.length;
    }
    
    public int getCena(int zona) throws ParkingException {
        if (zona < 0 || zona >= cenePoZonama.length) {
            throw new ParkingException(
                "Tarifna zona " + zona + " ne postoji. " +
                "Dozvoljene zone: 0 do " + (cenePoZonama.length - 1)
            );
        }
        return cenePoZonama[zona];
    }
    
    @Override
    public String toString() {
        return "Tarifnik" + Arrays.toString(cenePoZonama);
    }
}
