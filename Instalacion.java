public class Instalacion extends OrdenServicio {
    private int puntos;

    public Instalacion(String codigo, String cliente, String direccion,
                       double horasEstimadas, String zona, int puntos) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (puntos <= 0) {
            throw new IllegalArgumentException("Los puntos deben ser un entero positivo.");
        }
        this.puntos = puntos;
    }

    public int getPuntos() {
        return puntos;
    }

    public int calcularHorasFacturables() {
        double horasPorPuntos = puntos * 0.75;
        return (int) Math.ceil(Math.max(getHorasEstimadas(), horasPorPuntos));
    }

    @Override
    public double calcularCosto() {
        double base = getZona().equals("METROPOLITANA") ? 100.0 : 175.0;
        return base + calcularHorasFacturables() * 90.0;
    }

    @Override
    public int tiempoEstimadoDias() {
        return getZona().equals("METROPOLITANA") ? 3 : 5;
    }

    @Override
    public int maxVisitas() {
        return 3;
    }
}
