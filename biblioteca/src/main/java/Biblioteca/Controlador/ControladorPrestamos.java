package Biblioteca.Controlador;

import Biblioteca.Modelo.Dao;
import Biblioteca.Modelo.Ejemplar;
import Biblioteca.Modelo.Prestamo;
import Biblioteca.Modelo.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorPrestamos {
    private Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);
    private Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
    private Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);

    public void registrarPrestamo(int usuarioId, int ejemplarId) {
        Usuario usuario = daoUsuario.findById(usuarioId);
        Ejemplar ejemplar = daoEjemplar.findById(ejemplarId);

        if (usuario == null || ejemplar == null) {
            throw new RuntimeException("Usuario o ejemplar no encontrado.");
        }

        // Verificar que el usuario no tenga más de 3 préstamos activos
        long prestamosActivos = daoPrestamo.findAllWhere("usuario.id", usuarioId)
                .stream()
                .filter(p -> p.getFechaDevolucion() == null)  // Filtramos los préstamos activos
                .count();
        if (prestamosActivos >= 3) {
            throw new RuntimeException("El usuario no puede tener más de 3 préstamos activos.");
        }

        // Verificar que el ejemplar esté disponible
        if (!"Disponible".equals(ejemplar.getEstado())) {
            throw new RuntimeException("El ejemplar no está disponible para préstamo.");
        }

        // Verificar que el usuario no tenga penalizaciones activas
        if (usuario.getPenalizacionHasta() != null && LocalDate.now().isBefore(usuario.getPenalizacionHasta())) {
            throw new RuntimeException("El usuario tiene una penalización activa.");
        }

        // Registrar préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaInicio(LocalDate.now());
        prestamo.setFechaDevolucion(null);  // Aún no devuelto
        daoPrestamo.insert(prestamo);

        // Actualizar estado del ejemplar
        ejemplar.setEstado("Prestado");
        daoEjemplar.update(ejemplar);
    }

    public void devolverEjemplar(int prestamoId) {
        Prestamo prestamo = daoPrestamo.findById(prestamoId);
        if (prestamo == null) throw new RuntimeException("Préstamo no encontrado.");

        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado("Disponible");
        LocalDate fechaDevolucion = LocalDate.now();
        prestamo.setFechaDevolucion(fechaDevolucion);

        Usuario usuario = prestamo.getUsuario();

        // Penalización si hay retraso
        if (fechaDevolucion.isAfter(prestamo.getFechaDevolucion())) {
            long diasRetraso = ChronoUnit.DAYS.between(prestamo.getFechaDevolucion(), fechaDevolucion);
            new ControladorUsuarios().penalizarUsuario(usuario.getId(), (int) diasRetraso);
            System.out.println("Devolución fuera de plazo. Penalización aplicada.");
        }

        daoUsuario.update(usuario);
        daoPrestamo.update(prestamo);
    }

    public List<Prestamo> verTodosLosPrestamos() {
        return daoPrestamo.findAll();
    }

    public void validarPrestamo(Usuario usuario, Ejemplar ejemplar) {
        // Verificar si el usuario tiene más de 3 préstamos activos
        if (usuario.getPrestamos().size() >= 3) {
            throw new RuntimeException("El usuario ya tiene 3 préstamos activos.");
        }

        // Verificar si el ejemplar está disponible
        if (!"Disponible".equals(ejemplar.getEstado())) {
            throw new RuntimeException("El ejemplar no está disponible.");
        }

        // Verificar si el usuario tiene alguna penalización activa
        if (usuario.getPenalizacionHasta() != null && usuario.getPenalizacionHasta().isAfter(LocalDate.now())) {
            throw new RuntimeException("El usuario tiene una penalización activa.");
        }
    }

    public static List<Prestamo> obtenerPrestamosPorUsuario(Usuario usuario) {
        Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);
        return daoPrestamo.findAllWhere("usuario.id", usuario.getId());
    }

}
