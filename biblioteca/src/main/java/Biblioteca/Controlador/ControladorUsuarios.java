package Biblioteca.Controlador;

import Biblioteca.Modelo.Usuario;
import Biblioteca.Modelo.Dao;
import Biblioteca.Vista.Consola;
import Biblioteca.Vista.Menu;

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
}