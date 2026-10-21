public class InstalacionElectricaCertificada extends Instalacion {
    private double valorMateriales;

    public InstalacionElectricaCertificada(String codigo, String cliente, String direccion,
                                           double horasEstimadas, String zona, int puntos,
                                           double valorMateriales) {
        super(codigo, cliente, direccion, horasEstimadas, zona, puntos);
        if (valorMateriales < 0) {
            throw new IllegalArgumentException("El valor de los materiales no puede ser negativo.");
        }
        this.valorMateriales = valorMateriales;
    }

    public double getValorMateriales() {
        return valorMateriales;
    }

    @Override
    public double calcularCosto() {
        return super.calcularCosto() + 120.0 + valorMateriales * 0.05;
    }

    @Override
    public int tiempoEstimadoDias() {
        return super.tiempoEstimadoDias() + 2;
    }

    @Override
    public int maxVisitas() {
        return 2;
    }
}
