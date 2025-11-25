package main;

import java.util.Scanner;
import java.io.File;

import unaImagen.secuencial.ImagenSecuencial;
import unaImagen.paralela.ImagenParalela;
import unaImagen.paralela.ImagenParalelaArcoiris;
import multipleImagen.secuencial.ImagenesSecuencial;
import multipleImagen.paralela.porFilas.ImagenesParalelaPorFila;
import multipleImagen.paralela.porImagenes.ImagenesParalelaPorImagen;

public class Main {

    public static void eliminarArchivos(File carpeta) {
        if (carpeta.exists() && carpeta.isDirectory()) {

            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File f : archivos) {
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        eliminarArchivos(f);
                        f.delete();
                    }
                }
            }

            System.out.println("Archivos eliminados en: " + carpeta.getPath());
        } else {
            System.out.println("Carpeta no encontrada: " + carpeta.getPath());
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcion;

        String rutaBase = System.getProperty("user.dir");
        File carpetaConcurrente = new File(rutaBase + File.separator + "imagenes_grises_concurrente");
        File carpetaSecuencial = new File(rutaBase + File.separator + "imagenes_grises_secuencial");

        do {
            System.out.println("\n\t================== Notificaciones ==============\n\t");
            eliminarArchivos(carpetaSecuencial);
            eliminarArchivos(carpetaConcurrente);
            System.out.println("\n\t=================================================\n\t");

            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Secuencial 1 imagen");
            System.out.println("2. Paralelo 1 imagen");
            System.out.println("3. Paralelo 1 imagen (Arcoiris)");
            System.out.println("4. Secuencial varias imágenes");
            System.out.println("5. Paralelo varias imágenes por fila");
            System.out.println("6. Paralelo varias imágenes por imagen");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    ImagenSecuencial.ejecutar();
                    break;

                case 2:
                    ImagenParalela.ejecutar();
                    break;
                case 3:
                    ImagenParalelaArcoiris.ejecutar();
                    break;

                case 4:
                    ImagenesSecuencial.ejecutar();
                    break;

                case 5:
                    ImagenesParalelaPorFila.ejecutar();
                    break;

                case 6:
                    ImagenesParalelaPorImagen.ejecutar();
                    break;

                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida, intenta otra vez.");
            }

            System.out.println();

        } while (opcion != 0);

        sc.close();
    }
}
