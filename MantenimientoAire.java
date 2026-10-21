public class MantenimientoAire extends OrdenServicio {
    private int btu;

    public MantenimientoAire(String codigo, String cliente, String direccion,
                             double horasEstimadas, String zona, int btu) {
        super(codigo, cliente, direccion, horasEstimadas, zona);
        if (!getZona().equals("METROPOLITANA")) {
            throw new IllegalArgumentException("El mantenimiento de aire solo se permite en zona metropolitana.");
        }
        if (btu < 9000 || btu > 60000) {
            throw new IllegalArgumentException("Los BTU deben estar entre 9000 y 60000.");
        }
        this.btu = btu;
    }

    public int getBtu() {
        return btu;
    }

    @Override
    public double calcularCosto() {
        return 100.0 + getHorasEstimadas() * 110.0 + 80.0;
    }

    @Override
    public int tiempoEstimadoDias() {
        return 2;
    }

    @Override
    public int maxVisitas() {
        return 1;
    }

    @Override
    public boolean aceptaCuadrilla(Cuadrilla cuadrilla) {
        return cuadrilla.tieneCertificacionRefrigeracion();
    }
}
