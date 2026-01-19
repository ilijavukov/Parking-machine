package dom_1;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class AktivnaParkingZona {
    
    private enum StanjeZone {
        KREIRANA, OTVORENA, ZATVORENA, UNISTENA
    }
    
    private final String naziv;
    private final ParkingAutomat[] automati;
    private final long srednjeVremeDolaska;
    private final Random random;
    
    private StanjeZone stanje;
    private Thread nitSimulacije;
    private final AtomicBoolean aktivnaSimulacija;
    
    private final Object zakljucaj = new Object();
    
    public AktivnaParkingZona(String naziv, int brojAutomata, long srednjeVremeDolaska, 
                              ParkingAutomat prototipAutomat) throws ParkingException {
        if (naziv == null || naziv.trim().isEmpty()) {
            throw new ParkingException("Naziv zone ne može biti null ili prazan");
        }
        if (brojAutomata <= 0) {
            throw new ParkingException("Broj automata mora biti pozitivan, dobijeno: " + brojAutomata);
        }
        if (srednjeVremeDolaska <= 0) {
            throw new ParkingException("Srednje vreme dolaska mora biti pozitivno, dobijeno: " + srednjeVremeDolaska);
        }
        if (prototipAutomat == null) {
            throw new ParkingException("Prototip automat ne može biti null");
        }
        
        this.naziv = naziv;
        this.srednjeVremeDolaska = srednjeVremeDolaska;
        this.random = new Random();
        this.stanje = StanjeZone.KREIRANA;
        this.aktivnaSimulacija = new AtomicBoolean(false);
        
        this.automati = new ParkingAutomat[brojAutomata];

        this.automati[0] = prototipAutomat;
        for (int i = 1; i < brojAutomata; i++) {
        	this.automati[i] = prototipAutomat.napraviKopiju();
        }
    }
    
    public void otvori(Tarifnik noviTarifnik) throws ParkingException {
        synchronized (zakljucaj) {
            if (stanje == StanjeZone.UNISTENA) {
                throw new ParkingException("Zona '" + naziv + "' je uništena i ne može se ponovo otvoriti");
            }
            if (stanje == StanjeZone.OTVORENA) {
                throw new ParkingException("Zona '" + naziv + "' je već otvorena");
            }
            if (noviTarifnik == null) {
                throw new ParkingException("Tarifnik ne može biti null pri otvaranju zone");
            }
            
            for (ParkingAutomat automat : automati) {
                automat.setTarifnik(noviTarifnik);
            }
            
            stanje = StanjeZone.OTVORENA;
            aktivnaSimulacija.set(true);
            
            nitSimulacije = new Thread(this::simulirajDolaskeVozila, "Simulacija-" + naziv);
            nitSimulacije.start();
        }
    }
    
    public void zatvori() throws ParkingException {
        synchronized (zakljucaj) {
            if (stanje == StanjeZone.UNISTENA) {
                throw new ParkingException("Zona '" + naziv + "' je uništena");
            }
            if (stanje != StanjeZone.OTVORENA) {
                throw new ParkingException("Zona '" + naziv + "' nije otvorena");
            }
            
            aktivnaSimulacija.set(false);
        }
        
        if (nitSimulacije != null) {
            try {
                nitSimulacije.interrupt();
                nitSimulacije.join(5000); // Čekaj max 5 sekundi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        synchronized (zakljucaj) {
            stanje = StanjeZone.ZATVORENA;
        }
    }
    
    public void unisti() throws ParkingException {
        synchronized (zakljucaj) {
            if (stanje == StanjeZone.UNISTENA) {
                throw new ParkingException("Zona '" + naziv + "' je već uništena");
            }
                  
            if (stanje == StanjeZone.OTVORENA) {
                aktivnaSimulacija.set(false);
            }
        }
        
        if (nitSimulacije != null && nitSimulacije.isAlive()) {
            try {
                nitSimulacije.interrupt();
                nitSimulacije.join(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        synchronized (zakljucaj) {
            stanje = StanjeZone.UNISTENA;
        }
    }
    
   
    public int getUkupanNaplaceniIznos() {
        synchronized (zakljucaj) {
            int ukupno = 0;
            for (ParkingAutomat automat : automati) {
                ukupno += automat.getNaplaceniIznos();
            }
            return ukupno;
        }
    }
    
   
    public String getNaziv() {
        return naziv;
    }
    
    private void simulirajDolaskeVozila() {
        while (aktivnaSimulacija.get() && !Thread.currentThread().isInterrupted()) {
            try {              
                double faktor = 0.7 + random.nextDouble() * 0.6; // od 0.7 do 1.3
                long interval = (long) (srednjeVremeDolaska * faktor);
                
                Thread.sleep(interval);
                
                if (!aktivnaSimulacija.get()) {
                    break;
                }
                
                Tarifnik trenutniTarifnik;
                synchronized (zakljucaj) {
                    if (automati.length == 0 || automati[0].getTarifnik() == null) {
                        continue;
                    }
                    trenutniTarifnik = automati[0].getTarifnik();
                }
                
                int brojZona = trenutniTarifnik.getBrojZona();
                
                int zona = random.nextInt(brojZona);
                
                int indeksAutomata = random.nextInt(automati.length);
                
                int brojSati = 1 + random.nextInt(4);
                
                TarifiranoVozilo vozilo = new TarifiranoVozilo(zona);
                
                synchronized (zakljucaj) {
                    if (aktivnaSimulacija.get()) {
                        try {
                            automati[indeksAutomata].naplatiParkiranje(vozilo, brojSati);
                        } catch (ParkingException e) {
                         
                        }
                    }
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    @Override
    public String toString() {
        synchronized (zakljucaj) {
            StringBuilder sb = new StringBuilder();
            sb.append(naziv).append("(").append(getUkupanNaplaceniIznos()).append("): ");
            
            for (int i = 0; i < automati.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(automati[i].toString());
            }
            
            return sb.toString();
        }
    }
}
