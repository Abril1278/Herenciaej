public class Diagnostico extends OrdenServicio {
    public Diagnostico(String codigo, String cliente, String direccion,
                       double horasEstimadas, String zona) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (horasEstimadas > 1.5) {
            throw new IllegalArgumentException("Un diagnóstico no puede superar 1.5 horas.");
        }
    }

    @Override
    public double calcularCosto() {
        return getZona().equals("METROPOLITANA") ? 150.0 : 225.0;
    }

    @Override
    public int tiempoEstimadoDias() {
        return getZona().equals("METROPOLITANA") ? 1 : 2;
    }

    @Override
    public int maxVisitas() {
        return 3;
    }
}
