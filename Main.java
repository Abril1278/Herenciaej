public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        Vista vista = new Vista(controller);
        vista.mostrarMenu();
    }
}
