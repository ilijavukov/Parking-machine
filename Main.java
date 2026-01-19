package dom_1;

public class Main {
    
    private static final String ZONA = "Centar";
    private static final int BROJ_AUTOMATA = 3;
    private static final long SREDNJE_VREME_DOLASKA_MS = 500; // 500ms srednje vreme između dolazaka
    
    private static final int[] CENE_PO_ZONAMA = {100, 80, 50};
    
    private static final long VREME_PRVE_FAZE_MS = 5000;  // 5 sekundi
    private static final long VREME_DRUGE_FAZE_MS = 5000; // još 5 sekundi
    
    public static void main(String[] args) {
      
        try {
            Tarifnik tarifnik = new Tarifnik(CENE_PO_ZONAMA);
            System.out.println("Kreiran tarifnik: " + tarifnik);
            
            ParkingAutomat prototip = new ParkingAutomat();
            System.out.println("Kreiran prototip automat sa ID: " + prototip.getId());
            
            // Kreiranje aktivne parking zone
            AktivnaParkingZona zona = new AktivnaParkingZona(
                ZONA, 
                BROJ_AUTOMATA, 
                SREDNJE_VREME_DOLASKA_MS, 
                prototip
            );
            System.out.println("Kreirana zona: " + zona.getNaziv() + " sa " + BROJ_AUTOMATA + " automata");
            System.out.println();
            
            // Otvaranje zone
            System.out.println("Otvaranje zone...");
            zona.otvori(tarifnik);
            System.out.println("Zona je otvorena. Vozila počinju da pristižu...");
            System.out.println();
            
            // Čekanje prve faze
            System.out.println("Čekanje " + (VREME_PRVE_FAZE_MS / 1000) + " sekundi...");
            Thread.sleep(VREME_PRVE_FAZE_MS);
            
            // Ispis stanja posle prve faze
            System.out.println();
            System.out.println("STANJE POSLE PRVE FAZE: ");
            System.out.println(zona);
            System.out.println("Ukupno naplaćeno: " + zona.getUkupanNaplaceniIznos());
            System.out.println();
            
            // Čekanje druge faze
            System.out.println("Čekanje još " + (VREME_DRUGE_FAZE_MS / 1000) + " sekundi...");
            Thread.sleep(VREME_DRUGE_FAZE_MS);
            
            // Zatvaranje zone
            System.out.println();
            System.out.println("Zatvaranje zone...");
            zona.zatvori();
            System.out.println("Zona je zatvorena.");
            
            // Ispis konačnog stanja
            System.out.println();
            System.out.println("KONAČNO STANJE: ");
            System.out.println(zona);
            System.out.println("Ukupno naplaćeno: " + zona.getUkupanNaplaceniIznos());
            System.out.println();
            
            // Uništavanje zone
            System.out.println("Uništavanje zone...");
            zona.unisti();
            System.out.println("Zona je uništena.");
            System.out.println();
            
            System.out.println("SIMULACIJA ZAVRŠENA");
            
        } catch (ParkingException e) {
            System.err.println("Greška u parking sistemu: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Simulacija prekinuta: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
