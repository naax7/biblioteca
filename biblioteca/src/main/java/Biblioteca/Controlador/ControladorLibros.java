package Biblioteca.Controlador;

import Biblioteca.Modelo.Dao;
import Biblioteca.Modelo.Libro;

public class ControladorLibros {
    private static Dao<Libro, String> daoLibro = new Dao<>(Libro.class, String.class);

    public static void registrarLibro(String isbn, String titulo, String autor) {
        Libro libro = new Libro(isbn, autor, titulo);
        daoLibro.insert(libro);
    }

    public static void modificarLibro(String isbn, String titulo, String autor) {
        Libro libro = new Libro(isbn, autor, titulo);
        daoLibro.update(libro);
    }

    public static Libro buscarLibro(String isbn) {
        return daoLibro.findById(isbn);
    }
}
