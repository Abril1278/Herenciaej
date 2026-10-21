import java.util.Map;
import java.util.Scanner;

public class Vista {
    private Scanner scanner;
    private Controller controller;

    public Vista(Controller controller) {
        this.controller = controller;
        scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== TECNIHOGAR ===");
            System.out.println("1. Registrar orden");
            System.out.println("2. Registrar cuadrilla");
            System.out.println("3. Crear jornada");
            System.out.println("4. Asignar orden a jornada");
            System.out.println("5. Retirar orden de jornada");
            System.out.println("6. Iniciar jornada");
            System.out.println("7. Registrar resultado de orden");
            System.out.println("8. Ver órdenes, cuadrillas y jornadas");
            System.out.println("9. Ver ingresos");
            System.out.println("0. Salir");
            int opcion = leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1: registrarOrden(); break;
                    case 2: registrarCuadrilla(); break;
                    case 3: crearJornada(); break;
                    case 4: asignarOrden(); break;
                    case 5: retirarOrden(); break;
                    case 6: iniciarJornada(); break;
                    case 7: registrarResultado(); break;
                    case 8: mostrarListas(); break;
                    case 9: mostrarIngresos(); break;
                    case 0: salir = true; break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("No se pudo realizar la operación: " + e.getMessage());
            }
        }
        System.out.println("Programa finalizado.");
        scanner.close();
    }

    private void registrarOrden() {
        System.out.println("Tipo: 1 Diagnóstico, 2 Instalación, 3 Instalación eléctrica certificada, 4 Mantenimiento de aire");
        int tipo = leerEntero("Elige el tipo: ");
        String codigo = leerTexto("Código: ");
        String cliente = leerTexto("Nombre del cliente: ");
        String direccion = leerTexto("Dirección: ");
        double horas = leerDouble("Horas estimadas: ");
        String zona = leerTexto("Zona (METROPOLITANA/PERIFERICA): ").toUpperCase();

        OrdenServicio orden;
        switch (tipo) {
            case 1:
                orden = new Diagnostico(codigo, cliente, direccion, horas, zona);
                break;
            case 2:
                int puntos = leerEntero("Cantidad de puntos: ");
                orden = new Instalacion(codigo, cliente, direccion, horas, zona, puntos);
                break;
            case 3:
                int puntosCertificados = leerEntero("Cantidad de puntos: ");
                double materiales = leerDouble("Valor de los materiales: ");
                orden = new InstalacionElectricaCertificada(codigo, cliente, direccion,
                        horas, zona, puntosCertificados, materiales);
                break;
            case 4:
                int btu = leerEntero("BTU del equipo: ");
                orden = new MantenimientoAire(codigo, cliente, direccion, horas, zona, btu);
                break;
            default:
                throw new IllegalArgumentException("Tipo de servicio no válido.");
        }

        System.out.println("Costo: Q" + String.format("%.2f", controller.cotizar(orden)));
        System.out.println("Tiempo estimado: " + orden.tiempoEstimadoDias() + " días hábiles.");
        if (leerTexto("¿Deseas registrar la orden? (S/N): ").equalsIgnoreCase("S")) {
            controller.registrarOrden(orden);
            System.out.println("Orden registrada.");
        } else {
            System.out.println("La orden solo se cotizó; no se registró.");
        }
    }

    private void registrarCuadrilla() {
        String codigo = leerTexto("Código de cuadrilla: ");
        double capacidad = leerDouble("Capacidad de horas por jornada: ");
        boolean certificada = leerTexto("¿Tiene certificación en refrigeración? (S/N): ")
                .equalsIgnoreCase("S");
        controller.registrarCuadrilla(new Cuadrilla(codigo, capacidad, certificada));
        System.out.println("Cuadrilla registrada.");
    }

    private void crearJornada() {
        String numero = leerTexto("Número de jornada: ");
        String codigoCuadrilla = leerTexto("Código de cuadrilla: ");
        controller.crearJornada(numero, codigoCuadrilla);
        System.out.println("Jornada creada en preparación.");
    }

    private void asignarOrden() {
        String numero = leerTexto("Número de jornada: ");
        String codigo = leerTexto("Código de orden: ");
        controller.asignarOrden(numero, codigo);
        System.out.println("Orden asignada.");
    }

    private void retirarOrden() {
        String numero = leerTexto("Número de jornada: ");
        String codigo = leerTexto("Código de orden: ");
        controller.retirarOrden(numero, codigo);
        System.out.println("Orden retirada.");
    }

    private void iniciarJornada() {
        String numero = leerTexto("Número de jornada: ");
        controller.iniciarJornada(numero);
        System.out.println("Jornada iniciada. Las visitas adicionales se cobraron si correspondía.");
    }

    private void registrarResultado() {
        String numero = leerTexto("Número de jornada: ");
        String codigo = leerTexto("Código de orden: ");
        boolean realizado = leerTexto("¿Se realizó el trabajo? (S/N): ").equalsIgnoreCase("S");
        controller.registrarResultado(numero, codigo, realizado);
        System.out.println("Resultado registrado.");
    }

    private void mostrarListas() {
        System.out.println("\n--- ÓRDENES ---");
        for (OrdenServicio orden : controller.consultarOrdenes()) System.out.println(orden);
        System.out.println("\n--- CUADRILLAS ---");
        for (Cuadrilla cuadrilla : controller.consultarCuadrillas()) System.out.println(cuadrilla);
        System.out.println("\n--- JORNADAS ---");
        for (Jornada jornada : controller.consultarJornadas()) System.out.println(jornada);
    }

    private void mostrarIngresos() {
        System.out.println("Ingresos por registro: Q" + String.format("%.2f", controller.getIngresosRegistro()));
        System.out.println("Ingresos por visitas adicionales: Q" + String.format("%.2f", controller.getIngresosVisitas()));
        System.out.println("Ingresos totales: Q" + String.format("%.2f", controller.getIngresosTotales()));
        System.out.println("Ingresos por categoría:");
        for (Map.Entry<String, Double> dato : controller.getIngresosCategoria().entrySet()) {
            System.out.println(dato.getKey() + ": Q" + String.format("%.2f", dato.getValue()));
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero válido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número válido.");
            }
        }
    }
}
