package Biblioteca.Vista;

import Biblioteca.Controlador.ControladorEjemplares;
import Biblioteca.Controlador.ControladorLibros;
import Biblioteca.Controlador.ControladorPrestamos;
import Biblioteca.Controlador.ControladorUsuarios;
import Biblioteca.Modelo.*;

import java.util.List;
import java.util.Scanner;

public class Consola {
    private Usuario usuarioActual;
    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        usuarioActual = iniciarSesion(); // Realizar el inicio de sesión
        if (usuarioActual != null) {
            if (usuarioActual.getTipo().equalsIgnoreCase("administrador")) {
                mostrarMenuAdministrador();
            } else {
                mostrarMenuUsuarioNormal();
            }
        }
    }

    private Usuario iniciarSesion() {
        System.out.println("=== Inicio de Sesión ===");
        while (true) {
            System.out.print("Ingrese su email: ");
            String email = sc.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String password = sc.nextLine();

            try {
                return ControladorUsuarios.validaUsuario(email, password);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("¿Desea intentarlo nuevamente? (s/n)");
                if (!sc.nextLine().equalsIgnoreCase("s")) {
                    System.out.println("Saliendo...");
                    return null;
                }
            }
        }
    }

    private void mostrarMenuAdministrador() {
        int opcion;
        do {
            Menu.menuAdministrador();
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer
            switch (opcion) {
                case 1 -> gestionarUsuarios();
                case 2 -> gestionarLibros();
                case 3 -> gestionarEjemplares();
                case 4 -> gestionarPrestamos();
                case 5 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    private void mostrarMenuUsuarioNormal() {
        int opcion;
        do {
            Menu.menuUsuarioNormal();
            opcion = sc.nextInt();
            sc.nextLine(); // Limpiar buffer
            switch (opcion) {
                case 1: // Ver tus Préstamos
                    List<Prestamo> prestamosUsuario = ControladorPrestamos.obtenerPrestamosPorUsuario(usuarioActual);
                    if (prestamosUsuario.isEmpty()) {
                        System.out.println("No tienes préstamos activos.");
                    } else {
                        System.out.println("\n=== Tus Préstamos ===");
                        for (Prestamo prestamo : prestamosUsuario) {
                            System.out.println("- Ejemplar ID: " + prestamo.getEjemplar().getId() +
                                    " | Libro: " + prestamo.getEjemplar().getIsbn().getTitulo() +
                                    " | Fecha de inicio: " + prestamo.getFechaInicio() +
                                    " | Fecha de devolución: " +
                                    (prestamo.getFechaDevolucion() != null ? prestamo.getFechaDevolucion() : "Pendiente"));
                        }
                    }
                    break;
                case 2:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 2);
    }




    private void gestionarUsuarios() {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);

        System.out.println("=== Gestión de Usuarios ===");
        List<Usuario> usuarios = daoUsuario.findAll();
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }

        System.out.println("Opciones disponibles:");
        System.out.println("1. Agregar usuario");
        System.out.println("2. Modificar usuario");
        System.out.println("3. Eliminar usuario");
        System.out.println("4. Volver");
        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (opcion) {
            case 1 -> {
                System.out.println("Ingrese DNI: ");
                String dni = sc.nextLine();
                System.out.println("Ingrese nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Ingrese email: ");
                String email = sc.nextLine();
                System.out.println("Ingrese contraseña: ");
                String password = sc.nextLine();
                System.out.println("Ingrese tipo (normal/administrador): ");
                String tipo = sc.nextLine();

                ControladorUsuarios.registrarUsuario(dni, nombre, email, password, tipo);
                System.out.println("Usuario agregado correctamente.");
            }
            case 2 -> {
                System.out.println("Ingrese DNI: ");
                String dni = sc.nextLine();
                System.out.println("Ingrese nombre: ");
                String nombre = sc.nextLine();
                System.out.println("Ingrese email: ");
                String email = sc.nextLine();
                System.out.println("Ingrese contraseña: ");
                String password = sc.nextLine();
                System.out.println("Ingrese tipo (normal/administrador): ");
                String tipo = sc.nextLine();

                ControladorUsuarios.modificarUsuario(dni, nombre, email, password, tipo);
                System.out.println("Usuario modificado correctamente.");
            }
            case 3 -> {
                System.out.println("Ingrese ID del usuario a eliminar: ");
                int id = sc.nextInt();
                Usuario usuario = daoUsuario.findById(id);
                if (usuario != null) {
                    daoUsuario.delete(usuario);
                    System.out.println("Usuario eliminado correctamente.");
                } else {
                    System.out.println("Usuario no encontrado.");
                }
            }
            case 4 -> System.out.println("Volviendo...");
            default -> System.out.println("Opción no válida.");
        }
    }

    private void gestionarLibros() {
        Dao<Libro, String> daoLibro = new Dao<>(Libro.class, String.class);

        System.out.println("=== Gestión de Libros ===");
        List<Libro> libros = daoLibro.findAll();
        for (Libro libro : libros) {
            System.out.println(libro);
        }

        System.out.println("Opciones disponibles:");
        System.out.println("1. Agregar libro");
        System.out.println("2. Modificar libro");
        System.out.println("3. Eliminar libro");
        System.out.println("4. Volver");
        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (opcion) {
            case 1 -> {
                System.out.println("Ingrese ISBN: ");
                String isbn = sc.nextLine();
                System.out.println("Ingrese título: ");
                String titulo = sc.nextLine();
                System.out.println("Ingrese autor: ");
                String autor = sc.nextLine();

                ControladorLibros.registrarLibro(isbn, titulo, autor);
                System.out.println("Libro agregado correctamente.");
            }
            case 2 -> {
                System.out.println("Ingrese ISBN: ");
                String isbn = sc.nextLine();
                System.out.println("Ingrese titulo: ");
                String titulo = sc.nextLine();
                System.out.println("Ingrese autor: ");
                String autor = sc.nextLine();

                ControladorLibros.modificarLibro(isbn, titulo, autor);
                System.out.println("Libro modificado correctamente.");
            }
            case 3 -> {
                System.out.println("Ingrese ISBN del libro a eliminar: ");
                String isbn = sc.nextLine();
                Libro libro = daoLibro.findById(isbn);
                if (libro != null && libro.getEjemplars().isEmpty()) {
                    daoLibro.delete(libro);
                    System.out.println("Libro eliminado correctamente.");
                } else {
                    System.out.println("No se puede eliminar el libro porque tiene ejemplares registrados.");
                }
            }

            case 4 -> System.out.println("Volviendo...");
            default -> System.out.println("Opción no válida.");
        }
    }

    private void gestionarEjemplares() {
        Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);
        Dao<Libro, String> daoLibro = new Dao<>(Libro.class, String.class);

        System.out.println("=== Gestión de Ejemplares ===");
        System.out.println("Opciones disponibles:");
        System.out.println("1. Registrar ejemplar");
        System.out.println("2. Modificar ejemplares");
        System.out.println("3. Ver ejemplares por ISBN");
        System.out.println("4. Eliminar ejemplar");
        System.out.println("5. Volver");
        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (opcion) {
            case 1 -> {
                System.out.println("Ingrese ISBN del libro: ");
                String isbn = sc.nextLine();
                Libro libro = daoLibro.findById(isbn);
                if (libro != null) {
                    System.out.println("Ingrese el estado del ejemplar (Disponible/Prestado/Dañado): ");
                    String estado = sc.nextLine();
                    ControladorEjemplares controladorEjemplares = new ControladorEjemplares();
                    controladorEjemplares.registrarEjemplar(isbn, estado);
                    System.out.println("Ejemplar registrado correctamente.");
                } else {
                    System.out.println("Libro no encontrado.");
                }
            }
            case 2 -> {
                System.out.println("Ingrese ISBN: ");
                String isbn = sc.nextLine();
                System.out.println("Ingrese ID del ejemplar que desea modificar: ");
                int idEjemplar = sc.nextInt(); // ID del ejemplar
                sc.nextLine(); // Limpiar el buffer
                Dao<Libro, String> daoLibro2 = new Dao<>(Libro.class, String.class);
                Libro libro = daoLibro2.findById(isbn);

                if (libro != null) {
                    System.out.println("Ingrese el estado del ejemplar: ");
                    String estado = sc.nextLine();
                    ControladorEjemplares controladorEjemplares = new ControladorEjemplares();
                    controladorEjemplares.modificarEjemplar(isbn, idEjemplar, estado);  // Modificar pasando el ID del ejemplar
                    System.out.println("Ejemplar modificado correctamente.");
                } else {
                    System.out.println("Libro no encontrado.");
                }
            }

            case 3 -> {
                System.out.println("Ingrese ISBN del libro para ver ejemplares: ");
                String isbn = sc.nextLine();
                int disponibles = new ControladorEjemplares().contarDisponibles(isbn);
                System.out.println("Ejemplares disponibles: " + disponibles);
            }
            case 4 -> {
                System.out.println("Ingrese ID del ejemplar a eliminar: ");
                int id = sc.nextInt();
                Ejemplar ejemplar = daoEjemplar.findById(id);
                if (ejemplar != null) {
                    daoEjemplar.delete(ejemplar);
                    System.out.println("Ejemplar eliminado correctamente.");
                } else {
                    System.out.println("Ejemplar no encontrado.");
                }
            }
            case 5 -> System.out.println("Volviendo...");
            default -> System.out.println("Opción no válida.");
        }
    }


    private void gestionarPrestamos() {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);
        Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);

        System.out.println("=== Gestión de Préstamos ===");
        System.out.println("Opciones disponibles:");
        System.out.println("1. Registrar préstamo");
        System.out.println("2. Devolver préstamo");
        System.out.println("3. Ver préstamos");
        System.out.println("4. Volver");
        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        switch (opcion) {
            case 1 -> {
                // Registrar préstamo
                System.out.println("Ingrese ID del usuario: ");
                int usuarioId = sc.nextInt();
                System.out.println("Ingrese ID del ejemplar: ");
                int ejemplarId = sc.nextInt();
                sc.nextLine(); // Limpiar buffer

                ControladorPrestamos controladorPrestamos = new ControladorPrestamos();
                try {
                    controladorPrestamos.registrarPrestamo(usuarioId, ejemplarId);
                    System.out.println("Préstamo registrado correctamente.");
                } catch (RuntimeException e) {
                    System.out.println("Error al registrar el préstamo: " + e.getMessage());
                }
            }
            case 2 -> {
                // Devolver préstamo
                System.out.println("Ingrese ID del préstamo a devolver: ");
                int prestamoId = sc.nextInt();
                sc.nextLine(); // Limpiar buffer

                ControladorPrestamos controladorPrestamos = new ControladorPrestamos();
                try {
                    controladorPrestamos.devolverEjemplar(prestamoId);
                    System.out.println("Préstamo devuelto correctamente.");
                } catch (RuntimeException e) {
                    System.out.println("Error al devolver el préstamo: " + e.getMessage());
                }
            }
            case 3 -> {
                ControladorPrestamos controladorPrestamos = new ControladorPrestamos();
                controladorPrestamos.verTodosLosPrestamos().forEach(prestamo -> System.out.println(prestamo));
            }
            case 4 -> System.out.println("Volviendo...");
            default -> System.out.println("Opción no válida.");
        }
    }

    public static void main(String[] args) {
        Consola consola = new Consola();
        consola.iniciar();
    }
}
