package Biblioteca.Controlador;

import Biblioteca.Modelo.Dao;
import Biblioteca.Modelo.Ejemplar;
import Biblioteca.Modelo.Libro;

import java.util.List;

public class ControladorEjemplares {
    private Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);

    public void registrarEjemplar(String isbn, String estado) {
        // Verifica si el libro existe.
        Dao<Libro, String> daoLibro = new Dao<>(Libro.class, String.class);
        Libro libro = daoLibro.findById(isbn);
        if (libro == null) {
            throw new RuntimeException("El libro con ISBN " + isbn + " no existe.");
        }

        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setIsbn(libro);
        ejemplar.setEstado(estado);
        daoEjemplar.insert(ejemplar);
    }

    public void modificarEjemplar(String isbn, int idEjemplar, String estado) {
        // Buscar el libro por ISBN
        Dao<Libro, String> daoLibro = new Dao<>(Libro.class, String.class);
        Libro libro = daoLibro.findById(isbn);

        if (libro == null) {
            throw new RuntimeException("El libro con ISBN " + isbn + " no existe.");
        }

        // Buscar el ejemplar específico por su ID
        Dao<Ejemplar, Integer> daoEjemplar = new Dao<>(Ejemplar.class, Integer.class);
        Ejemplar ejemplar = daoEjemplar.findById(idEjemplar);

        if (ejemplar == null) {
            throw new RuntimeException("El ejemplar con ID " + idEjemplar + " no existe.");
        }

        // Verificar si el ejemplar corresponde al libro correcto
        if (!ejemplar.getIsbn().getIsbn().equals(isbn)) {
            throw new RuntimeException("El ejemplar no corresponde al libro con ISBN " + isbn);
        }

        // Modificar el estado del ejemplar
        ejemplar.setEstado(estado);

        // Actualizar el ejemplar en la base de datos
        daoEjemplar.update(ejemplar);
    }


    public int contarDisponibles(String isbn) {
        List<Ejemplar> ejemplares = daoEjemplar.findAll();
        return (int) ejemplares.stream()
                .filter(e -> e.getIsbn().getIsbn().equals(isbn) && e.getEstado().equals("Disponible"))
                .count();
    }
}
