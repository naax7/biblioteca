package Biblioteca.Controlador;

import Biblioteca.Modelo.Usuario;
import Biblioteca.Modelo.Dao;
import Biblioteca.Vista.Consola;
import Biblioteca.Vista.Menu;

import java.time.LocalDate;

public class ControladorUsuarios {
    public static Usuario validaUsuario(String email, String password){
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Usuario usuario = null;
        try{
            usuario = daoUsuario.findUnique("email", email);
        } catch (jakarta.persistence.NoResultException e){
            throw new RuntimeException("Usuario no encontrado");
        }
        if(usuario != null){
            if (usuario.getPassword().equals(password)){
                return usuario;
            }
            throw new RuntimeException("La contraseña es incorrecta");
        }
        return usuario;
    }

    public static void registrarUsuario(String dni, String nombre, String email, String password, String tipo) {
        // Aquí va la lógica para registrar un usuario.
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setDni(dni);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setTipo(tipo);

        // Aquí se debe guardar el usuario, por ejemplo, en la base de datos
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        daoUsuario.insert(nuevoUsuario);
    }

    public static void modificarUsuario(String dni, String nombre, String email, String password, String tipo) {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        // Buscar al usuario por su DNI
        Usuario usuarioExistente = daoUsuario.findUnique("dni", dni);

        // Si el usuario no existe, lanzar una excepción
        if (usuarioExistente == null) {
            throw new RuntimeException("El usuario con DNI " + dni + " no existe.");
        }

        // Actualizamos los datos del usuario
        usuarioExistente.setNombre(nombre);
        usuarioExistente.setEmail(email);
        usuarioExistente.setPassword(password);
        usuarioExistente.setTipo(tipo);

        // Realizamos el update en la base de datos
        daoUsuario.update(usuarioExistente);
    }

    public void penalizarUsuario(int usuarioId, int dias) {
        Dao<Usuario, Integer> daoUsuario = new Dao<>(Usuario.class, Integer.class);
        Usuario usuario = daoUsuario.findById(usuarioId);
        if (usuario != null) {
            usuario.setPenalizacionHasta(LocalDate.now().plusDays(dias));
            daoUsuario.update(usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado.");
        }
    }

}