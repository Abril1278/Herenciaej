import java.util.ArrayList;
import java.util.List;

public class Jornada {
    private String numero;
    private Cuadrilla cuadrilla;
    private List<OrdenServicio> ordenes;
    private String estado;
    private int resultadosPendientes;

    public Jornada(String numero, Cuadrilla cuadrilla) {
        if (numero == null || numero.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de jornada no puede estar vacío.");
        }
        this.numero = numero;
        this.cuadrilla = cuadrilla;
        this.ordenes = new ArrayList<>();
        this.estado = "EN_PREPARACION";
        this.resultadosPendientes = 0;
    }

    public void asignarOrden(OrdenServicio orden) {
        if (!estado.equals("EN_PREPARACION")) {
            throw new IllegalStateException("La jornada ya inició.");
        }
        if (!orden.aceptaCuadrilla(cuadrilla)) {
            throw new IllegalArgumentException("La cuadrilla no cumple los requisitos de esta orden.");
        }
        if (horasAsignadas() + orden.getHorasEstimadas() > cuadrilla.getCapacidadHoras()) {
            throw new IllegalArgumentException("La cuadrilla no tiene suficiente capacidad.");
        }
        orden.asignar();
        ordenes.add(orden);
    }

    public void retirarOrden(String codigo) {
        if (!estado.equals("EN_PREPARACION")) {
            throw new IllegalStateException("Solo se pueden retirar órdenes antes de iniciar.");
        }
        OrdenServicio orden = buscarOrden(codigo);
        if (orden == null) {
            throw new IllegalArgumentException("La orden no está en esta jornada.");
        }
        orden.retirar();
        ordenes.remove(orden);
    }

    public double iniciar() {
        if (!estado.equals("EN_PREPARACION") || ordenes.isEmpty()) {
            throw new IllegalStateException("La jornada debe estar en preparación y tener órdenes.");
        }
        double cobroVisitas = 0;
        for (OrdenServicio orden : ordenes) {
            cobroVisitas += orden.iniciarVisita();
        }
        estado = "EN_ATENCION";
        resultadosPendientes = ordenes.size();
        return cobroVisitas;
    }

    public void registrarResultado(String codigo, boolean trabajoRealizado) {
        if (!estado.equals("EN_ATENCION")) {
            throw new IllegalStateException("La jornada no está en atención.");
        }
        OrdenServicio orden = buscarOrden(codigo);
        if (orden == null || !orden.getEstado().equals("EN_ATENCION")) {
            throw new IllegalArgumentException("No hay una orden en atención con ese código en esta jornada.");
        }
        orden.registrarResultado(trabajoRealizado);
        resultadosPendientes--;
        if (resultadosPendientes == 0) {
            estado = "CERRADA";
        }
    }

    private OrdenServicio buscarOrden(String codigo) {
        for (OrdenServicio orden : ordenes) {
            if (orden.getCodigo().equalsIgnoreCase(codigo)) {
                return orden;
            }
        }
        return null;
    }

    public double horasAsignadas() {
        double total = 0;
        for (OrdenServicio orden : ordenes) {
            total += orden.getHorasEstimadas();
        }
        return total;
    }

    public double capacidadDisponible() {
        return cuadrilla.getCapacidadHoras() - horasAsignadas();
    }

    public String getNumero() { return numero; }
    public Cuadrilla getCuadrilla() { return cuadrilla; }
    public List<OrdenServicio> getOrdenes() { return new ArrayList<>(ordenes); }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return numero + " | cuadrilla: " + cuadrilla.getCodigo()
                + " | estado: " + estado + " | horas: " + horasAsignadas()
                + "/" + cuadrilla.getCapacidadHoras();
    }
}
