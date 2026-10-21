public abstract class OrdenServicio {
    private String codigo;
    private String cliente;
    private String direccion;
    private double horasEstimadas;
    private String zona;
    private String estado;
    private int visitas;

    public OrdenServicio(String codigo, String cliente, String direccion,
                         double horasEstimadas, String zona) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El cliente no puede estar vacío.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía.");
        }
        if (horasEstimadas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser positivas.");
        }
        if (!zona.equalsIgnoreCase("METROPOLITANA")
                && !zona.equalsIgnoreCase("PERIFERICA")) {
            throw new IllegalArgumentException("La zona debe ser METROPOLITANA o PERIFERICA.");
        }

        this.codigo = codigo;
        this.cliente = cliente;
        this.direccion = direccion;
        this.horasEstimadas = horasEstimadas;
        this.zona = zona.toUpperCase();
        this.estado = "PENDIENTE";
        this.visitas = 0;
    }

    public abstract double calcularCosto();
    public abstract int tiempoEstimadoDias();
    public abstract int maxVisitas();

    public boolean aceptaCuadrilla(Cuadrilla cuadrilla) {
        return true;
    }

    public void asignar() {
        if (!estado.equals("PENDIENTE")) {
            throw new IllegalStateException("La orden debe estar pendiente.");
        }
        estado = "ASIGNADA";
    }

    public void retirar() {
        if (!estado.equals("ASIGNADA")) {
            throw new IllegalStateException("Solo se puede retirar una orden asignada.");
        }
        estado = "PENDIENTE";
    }

    public double iniciarVisita() {
        if (!estado.equals("ASIGNADA")) {
            throw new IllegalStateException("La orden debe estar asignada.");
        }
        if (visitas >= maxVisitas()) {
            estado = "CANCELADA";
            throw new IllegalStateException("La orden ya alcanzó el máximo de visitas.");
        }
        double cargo = visitas >= 1 ? 40.0 : 0.0;
        visitas++;
        estado = "EN_ATENCION";
        return cargo;
    }

    public void registrarResultado(boolean trabajoRealizado) {
        if (!estado.equals("EN_ATENCION")) {
            throw new IllegalStateException("La orden no está en atención.");
        }
        if (trabajoRealizado) {
            estado = "COMPLETADA";
        } else if (visitas >= maxVisitas()) {
            estado = "CANCELADA";
        } else {
            estado = "PENDIENTE";
        }
    }

    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public String getDireccion() { return direccion; }
    public double getHorasEstimadas() { return horasEstimadas; }
    public String getZona() { return zona; }
    public String getEstado() { return estado; }
    public int getVisitas() { return visitas; }

    public String getCategoria() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return codigo + " | " + getCategoria() + " | " + cliente
                + " | " + zona + " | Q" + String.format("%.2f", calcularCosto())
                + " | " + estado + " | visitas: " + visitas;
    }
}
