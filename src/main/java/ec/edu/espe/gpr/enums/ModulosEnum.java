package ec.edu.espe.gpr.enums;

public enum ModulosEnum {
    INVESTIGACION("Investigacion", "investigacion"),
    VINCULACION("Vinculacion", "vinculacion"),
    DOCENCIA("Docencia", "docencia");

    private final String value;
    private final String text;

    private ModulosEnum(String value, String text) {
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
