import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private List<OrdenServicio> ordenes;
    private List<Cuadrilla> cuadrillas;
    private List<Jornada> jornadas;
    private double ingresosRegistro;
    private double ingresosVisitas;
    private Map<String, Double> ingresosCategoria;

    public Controller() {
        ordenes = new ArrayList<>();
        cuadrillas = new ArrayList<>();
        jornadas = new ArrayList<>();
        ingresosRegistro = 0;
        ingresosVisitas = 0;
        ingresosCategoria = new HashMap<>();
    }

    public double cotizar(OrdenServicio orden) {
        return orden.calcularCosto();
    }

    public void registrarOrden(OrdenServicio orden) {
        if (buscarOrden(orden.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe una orden con ese código.");
        }
        ordenes.add(orden);
        ingresosRegistro += orden.calcularCosto();
        String categoria = orden.getCategoria();
        ingresosCategoria.put(categoria,
                ingresosCategoria.getOrDefault(categoria, 0.0) + orden.calcularCosto());
    }

    public void registrarCuadrilla(Cuadrilla cuadrilla) {
        if (buscarCuadrilla(cuadrilla.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe una cuadrilla con ese código.");
        }
        cuadrillas.add(cuadrilla);
    }

    public Jornada crearJornada(String numero, String codigoCuadrilla) {
        if (buscarJornada(numero) != null) {
            throw new IllegalArgumentException("Ya existe una jornada con ese número.");
        }
        Cuadrilla cuadrilla = buscarCuadrilla(codigoCuadrilla);
        if (cuadrilla == null) {
            throw new IllegalArgumentException("No se encontró esa cuadrilla.");
        }
        for (Jornada jornada : jornadas) {
            if (jornada.getCuadrilla().getCodigo().equalsIgnoreCase(codigoCuadrilla)
                    && !jornada.getEstado().equals("CERRADA")) {
                throw new IllegalStateException("La cuadrilla ya tiene una jornada sin cerrar.");
            }
        }
        Jornada jornada = new Jornada(numero, cuadrilla);
        jornadas.add(jornada);
        return jornada;
    }

    public void asignarOrden(String numeroJornada, String codigoOrden) {
        Jornada jornada = buscarJornada(numeroJornada);
        OrdenServicio orden = buscarOrden(codigoOrden);
        if (jornada == null || orden == null) {
            throw new IllegalArgumentException("No se encontró la jornada o la orden.");
        }
        jornada.asignarOrden(orden);
    }

    public void retirarOrden(String numeroJornada, String codigoOrden) {
        Jornada jornada = buscarJornada(numeroJornada);
        if (jornada == null) {
            throw new IllegalArgumentException("No se encontró la jornada.");
        }
        jornada.retirarOrden(codigoOrden);
    }

    public void iniciarJornada(String numeroJornada) {
        Jornada jornada = buscarJornada(numeroJornada);
        if (jornada == null) {
            throw new IllegalArgumentException("No se encontró la jornada.");
        }
        double cobro = jornada.iniciar();
        ingresosVisitas += cobro;
    }

    public void registrarResultado(String numeroJornada, String codigoOrden, boolean realizado) {
        Jornada jornada = buscarJornada(numeroJornada);
        if (jornada == null) {
            throw new IllegalArgumentException("No se encontró la jornada.");
        }
        jornada.registrarResultado(codigoOrden, realizado);
    }

    public OrdenServicio buscarOrden(String codigo) {
        for (OrdenServicio orden : ordenes) {
            if (orden.getCodigo().equalsIgnoreCase(codigo)) return orden;
        }
        return null;
    }

    public Cuadrilla buscarCuadrilla(String codigo) {
        for (Cuadrilla cuadrilla : cuadrillas) {
            if (cuadrilla.getCodigo().equalsIgnoreCase(codigo)) return cuadrilla;
        }
        return null;
    }

    public Jornada buscarJornada(String numero) {
        for (Jornada jornada : jornadas) {
            if (jornada.getNumero().equalsIgnoreCase(numero)) return jornada;
        }
        return null;
    }

    public List<OrdenServicio> consultarOrdenes() { return new ArrayList<>(ordenes); }
    public List<Cuadrilla> consultarCuadrillas() { return new ArrayList<>(cuadrillas); }
    public List<Jornada> consultarJornadas() { return new ArrayList<>(jornadas); }
    public double getIngresosRegistro() { return ingresosRegistro; }
    public double getIngresosVisitas() { return ingresosVisitas; }

    public Map<String, Double> getIngresosCategoria() {
        return new HashMap<>(ingresosCategoria);
    }

    public double getIngresosTotales() {
        return ingresosRegistro + ingresosVisitas;
    }
}
