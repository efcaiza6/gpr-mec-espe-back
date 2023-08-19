package ec.edu.espe.gpr.exceptions;

public class DocenteNotFoundException extends RuntimeException{
    public DocenteNotFoundException(String mensaje) {
        super(mensaje);
    }
}
