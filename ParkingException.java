package dom_1;

public class ParkingException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public ParkingException(String poruka) {
        super(poruka);
    }
    
    public ParkingException(String poruka, Throwable uzrok) {
        super(poruka, uzrok);
    }
}
