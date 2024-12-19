package Biblioteca.Vista;

import Biblioteca.Controlador.ControladorUsuarios;
import Biblioteca.Modelo.Usuario;

import java.util.Scanner;

public class Consola {
    private static Usuario usuario;

    public static Usuario menuInicioSesion(){
        Usuario usuario = null;
        Scanner sc = new Scanner(System.in);
        Menu.inicioSesion();
        String email = sc.nextLine();
        System.out.println("Contraseña: ");
        String pass = sc.nextLine();
        try{
            usuario = ControladorUsuarios.validaUsuario(email, pass);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return menuInicioSesion();
        }
        return usuario;
    }

    public static void menuPrincipal(){

    }

    public static void main(String[] args) {
        menuInicioSesion();
    }
}
