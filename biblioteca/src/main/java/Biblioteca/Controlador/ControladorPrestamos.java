/*package Biblioteca.Controlador;

import Biblioteca.Modelo.Dao;
import Biblioteca.Modelo.Ejemplar;
import Biblioteca.Modelo.Prestamo;
import Biblioteca.Modelo.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorPrestamos {
    private Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);

    public void registrarPrestamo(int usuarioId, int ejemplarId) {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);

        Usuario usuario = daoUsuario.findById(usuarioId);
        Ejemplar ejemplar = daoEjemplar.findById(ejemplarId);

        if (usuario.getPenalizacionHasta() != null && LocalDate.now().isBefore(usuario.getPenalizacionHasta())) {
            throw new RuntimeException("El usuario tiene una penalización activa.");
        }

        long prestamosActivos = usuario.getPrestamos().stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .count();
        if (prestamosActivos >= 3) {
            throw new RuntimeException("El usuario ya tiene 3 préstamos activos.");
        }

        if (!ejemplar.getEstado().equals("Disponible")) {
            throw new RuntimeException("El ejemplar no está disponible.");
        }

        ejemplar.setEstado("Prestado");
        daoEjemplar.update(ejemplar);

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaInicio(LocalDate.now());
        daoPrestamo.insert(prestamo);
    }

    public void registrarPrestamo(int usuarioId, int ejemplarId) {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);

        // Obtener el usuario y el ejemplar
        Usuario usuario = daoUsuario.findById(usuarioId);
        Ejemplar ejemplar = daoEjemplar.findById(ejemplarId);

        // Validar que el usuario y ejemplar existan
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        if (ejemplar == null || !ejemplar.getEstado().equals("Disponible")) {
            throw new RuntimeException("El ejemplar no está disponible.");
        }

        // Validar que el usuario no tenga más de 3 préstamos activos
        if (usuario.getPrestamos().size() >= 3) {
            throw new RuntimeException("El usuario no puede tener más de 3 préstamos activos.");
        }

        // Validar que el usuario no tenga penalización activa
        if (usuario.getPenalizacionHasta() > 0) {
            throw new RuntimeException("El usuario tiene una penalización activa y no puede realizar nuevos préstamos.");
        }

        // Registrar el préstamo
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = fechaInicio.plusDays(15); // Fecha límite es 15 días después de la fecha de inicio

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaLimite(fechaLimite);

        // Actualizar el estado del ejemplar a "Prestado"
        ejemplar.setEstado("Prestado");

        // Guardar el préstamo en la base de datos
        Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);
        daoPrestamo.create(prestamo);

        System.out.println("Préstamo registrado correctamente.");
    }

    public void devolverEjemplar(int prestamoId) {
        Prestamo prestamo = daoPrestamo.findById(prestamoId);
        if (prestamo == null) {
            throw new RuntimeException("Préstamo no encontrado.");
        }

        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado("Disponible");

        LocalDate fechaDevolucion = LocalDate.now();
        prestamo.setFechaDevolucion(fechaDevolucion);

        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Usuario usuario = prestamo.getUsuario();

        // Aquí es donde se calcula el retraso y se penaliza si es necesario
        if (fechaDevolucion.isAfter(prestamo.getFechaInicio().plusDays(15))) {
            long diasRetraso = fechaDevolucion.toEpochDay() - prestamo.getFechaInicio().plusDays(15).toEpochDay();

            // Aquí es donde implementamos la llamada al método penalizarUsuario
            ControladorUsuarios controladorUsuarios = new ControladorUsuarios();
            controladorUsuarios.penalizarUsuario(usuario.getId(), (int) diasRetraso);

            // También podemos actualizar el campo penalizacionHasta directamente si lo prefieres
            // usuario.setPenalizacionHasta(LocalDate.now().plusDays(diasRetraso * 15));
        }

        daoUsuario.update(usuario);
        daoPrestamo.update(prestamo);
    }


    public List<Prestamo> verPrestamosPorUsuario(int usuarioId) {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Usuario usuario = daoUsuario.findById(usuarioId);

        return usuario.getPrestamos().stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .toList();
    }

    public List<Prestamo> verTodosLosPrestamos() {
        return daoPrestamo.findAll();
    }

    public static List<Prestamo> obtenerPrestamosPorUsuario(Usuario usuario) {
        Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);

        // Filtramos los préstamos activos, es decir, aquellos cuyo campo fechaDevolucion es null
        return daoPrestamo.findAll()
                .stream()
                .filter(prestamo -> prestamo.getUsuario().equals(usuario) && prestamo.getFechaDevolucion() == null)
                .collect(Collectors.toList());
    }
}*/











/*package Biblioteca.Controlador;

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

    public void registrarPrestamo(int usuarioId, int ejemplarId) {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);

        // Obtener el usuario y el ejemplar
        Usuario usuario = daoUsuario.findById(usuarioId);
        Ejemplar ejemplar = daoEjemplar.findById(ejemplarId);

        // Validar que el usuario y ejemplar existan
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        if (ejemplar == null || !ejemplar.getEstado().equals("Disponible")) {
            throw new RuntimeException("El ejemplar no está disponible.");
        }

        // Validar que el usuario no tenga más de 3 préstamos activos
        long prestamosActivos = usuario.getPrestamos().stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .count();
        if (prestamosActivos >= 3) {
            throw new RuntimeException("El usuario no puede tener más de 3 préstamos activos.");
        }

        // Validar que el usuario no tenga penalización activa
        if (usuario.getPenalizacionHasta() != null && LocalDate.now().isBefore(usuario.getPenalizacionHasta())) {
            throw new RuntimeException("El usuario tiene una penalización activa y no puede realizar nuevos préstamos.");
        }

        // Registrar el préstamo
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = fechaInicio.plusDays(15); // Fecha límite es 15 días después de la fecha de inicio

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaDevolucion(fechaLimite);

        // Actualizar el estado del ejemplar a "Prestado"
        ejemplar.setEstado("Prestado");

        // Guardar el préstamo en la base de datos
        daoPrestamo.insert(prestamo);

        System.out.println("Préstamo registrado correctamente.");
    }

    public void devolverEjemplar(int prestamoId) {
        Prestamo prestamo = daoPrestamo.findById(prestamoId);
        if (prestamo == null) {
            throw new RuntimeException("Préstamo no encontrado.");
        }

        Ejemplar ejemplar = prestamo.getEjemplar();
        ejemplar.setEstado("Disponible");

        LocalDate fechaDevolucion = LocalDate.now();
        prestamo.setFechaDevolucion(fechaDevolucion);

        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Usuario usuario = prestamo.getUsuario();

        // Aquí es donde se calcula el retraso y se penaliza si es necesario
        if (fechaDevolucion.isAfter(prestamo.getFechaDevolucion())) {
            long diasRetraso = ChronoUnit.DAYS.between(prestamo.getFechaDevolucion(), fechaDevolucion);

            // Penalizar al usuario por el retraso
            ControladorUsuarios controladorUsuarios = new ControladorUsuarios();
            controladorUsuarios.penalizarUsuario(usuario.getId(), (int) diasRetraso);

            System.out.println("Devolución fuera de plazo. Penalización aplicada.");
        }

        daoUsuario.update(usuario);
        daoPrestamo.update(prestamo);

        System.out.println("Préstamo devuelto correctamente.");
    }

    public List<Prestamo> verPrestamosPorUsuario(int usuarioId) {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Usuario usuario = daoUsuario.findById(usuarioId);

        return usuario.getPrestamos().stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .collect(Collectors.toList());
    }

    public List<Prestamo> verTodosLosPrestamos() {
        return daoPrestamo.findAll();
    }

    public static List<Prestamo> obtenerPrestamosPorUsuario(Usuario usuario) {
        Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);

        // Filtramos los préstamos activos, es decir, aquellos cuyo campo fechaDevolucion es null
        return daoPrestamo.findAll()
                .stream()
                .filter(prestamo -> prestamo.getUsuario().equals(usuario) && prestamo.getFechaDevolucion() == null)
                .collect(Collectors.toList());
    }
}*/




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

    public List<Prestamo> verPrestamosPorUsuario(int usuarioId) {
        Usuario usuario = daoUsuario.findById(usuarioId);
        return usuario.getPrestamos().stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .collect(Collectors.toList());
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
