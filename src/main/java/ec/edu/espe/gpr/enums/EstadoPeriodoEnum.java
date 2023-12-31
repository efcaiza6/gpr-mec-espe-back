package ec.edu.espe.gpr.enums;

public enum EstadoPeriodoEnum {
    ACTIVE("ACTIVO", "Activo"),
    INACTIVE("INACTIVO", "Inactivo");

    private final String value;
    private final String text;

    private EstadoPeriodoEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public String getValue() {
        return this.value;
    }
}
