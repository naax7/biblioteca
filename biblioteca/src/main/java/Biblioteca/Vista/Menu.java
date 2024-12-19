package Biblioteca.Vista;

import Biblioteca.Modelo.Dao;
import Biblioteca.Modelo.Prestamo;
import Biblioteca.Modelo.Usuario;

import java.util.Scanner;

public class Menu {
    public static void inicioSesion() {
        System.out.println("Inicio de sesion" + "\n" +
                "Usuario: ");
    }

    public static void menuPrincipal() {
        Dao<Prestamo, Integer> daoPrestamo = new Dao<>(Prestamo.class, Integer.class);

        System.out.println("1. Ver préstamos\n" +
                "2. Salir");
        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        switch(opcion){
            case 1:
                System.out.println(daoPrestamo.findById());
                break;
            case 2:
                System.out.println(daoDepartamento.getAllDepartamentos());
                break;
    }
}
