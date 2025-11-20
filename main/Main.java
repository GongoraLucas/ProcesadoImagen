package main;

import java.util.Scanner;

import unaImagen.secuencial.ImagenSecuencial;
import unaImagen.paralela.ImagenParalela;
import multipleImagen.secuencial.ImagenesSecuencial;
import multipleImagen.paralela.porFilas.ImagenesParalelaPorFila;
import multipleImagen.paralela.porImagenes.ImagenesParalelaPorImagen;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Secuencial 1 imagen");
            System.out.println("2. Secuencial varias imagenes");
            System.out.println("3. Paralelo 1 imagen");
            System.out.println("4. Paralelo varias imagenes por fila");
            System.out.println("5. Paralelo varias imagenes por imagen");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    ImagenSecuencial.ejecutar();
                    break;

                case 2:
                    ImagenesSecuencial.ejecutar();
                    break;
                    
                    case 3:
                    ImagenParalela.ejecutar();
                    break;

                case 4:
                    ImagenesParalelaPorFila.ejecutar();
                    break;

                case 5:
                    ImagenesParalelaPorImagen.ejecutar();
                    break;

                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida, intenta otra vez.");
            }

            System.out.println(); // línea en blanco

        } while (opcion != 0);

        sc.close();
    }
}
