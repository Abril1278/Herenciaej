public class Cuadrilla {
    private String codigo;
    private double capacidadHoras;
    private boolean certificadaRefrigeracion;

    public Cuadrilla(String codigo, double capacidadHoras, boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de cuadrilla no puede estar vacío.");
        }
        if (capacidadHoras <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva.");
        }
        this.codigo = codigo;
        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
    }

    public String getCodigo() { return codigo; }
    public double getCapacidadHoras() { return capacidadHoras; }
    public boolean tieneCertificacionRefrigeracion() { return certificadaRefrigeracion; }

    public boolean tieneCapacidad(double horas) {
        return horas <= capacidadHoras;
    }

    @Override
    public String toString() {
        return codigo + " | capacidad: " + capacidadHoras + " h | certificación de refrigeración: "
                + (certificadaRefrigeracion ? "sí" : "no");
    }
}
