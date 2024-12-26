package Biblioteca.Vista;

public class Menu {
    public static void menuAdministrador() {
        System.out.println("\n=== Menú Administrador ===");
        System.out.println("1. Gestionar Usuarios");
        System.out.println("2. Gestionar Libros");
        System.out.println("3. Gestionar Ejemplares");
        System.out.println("4. Gestionar Préstamos");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static void menuUsuarioNormal() {
        System.out.println("\n=== Menú Usuario ===");
        System.out.println("1. Ver mis Préstamos");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción: ");
    }
}
