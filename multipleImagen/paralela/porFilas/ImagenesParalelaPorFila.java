package multipleImagen.paralela.porFilas;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImagenesParalelaPorFila {

    public static void ejecutar() {
        try {
            long inicioTotal = System.nanoTime();   

            File carpetaEntrada = new File("C:\\Users\\Lucas\\Desktop\\paralela\\java\\Imagenes\\PDI-concurrente\\imagenes");
            File carpetaSalida = new File("C:\\Users\\Lucas\\Desktop\\paralela\\java\\Imagenes\\PDI-concurrente\\imagenes_grises_concurrente");

            if (!carpetaSalida.exists())
                carpetaSalida.mkdirs();

            File[] archivos = carpetaEntrada.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png") ||
                name.toLowerCase().endsWith(".jpg") ||
                name.toLowerCase().endsWith(".jpeg")
            );

            if (archivos == null || archivos.length == 0) {
                System.out.println("No hay imágenes para procesar.");
                return;
            }

            for (File archivoEntrada : archivos) {

                System.out.println("Procesando: " + archivoEntrada.getName());

                BufferedImage imagen = ImageIO.read(archivoEntrada);

                if (imagen == null) {
                    System.out.println("No se pudo cargar la imagen " + archivoEntrada.getName());
                    continue;
                }

                int altura = imagen.getHeight();   // 🔥 un hilo por fila
                Thread[] hilos = new Thread[altura];

                long inicioImagen = System.nanoTime();

                // Crear un hilo para cada fila
                for (int fila = 0; fila < altura; fila++) {
                    hilos[fila] = new Thread(new FiltroGris(imagen, fila, fila + 1));
                    hilos[fila].start();
                }

                // Esperar a que todos terminen
                for (Thread t : hilos) {
                    t.join();
                }

                long finImagen = System.nanoTime();

                // Guardar imagen procesada
                String nombreSalida = archivoEntrada.getName().replace(".", "_gris.");
                File archivoSalida = new File(carpetaSalida, nombreSalida);

                ImageIO.write(imagen, "png", archivoSalida);

                System.out.println("Guardado: " + archivoSalida.getPath());
                System.out.println("Tiempo por imagen: " + (finImagen - inicioImagen) / 1_000_000 + " ms\n");
            }

            long finTotal = System.nanoTime(); 

             System.out.println("Tiempo total del procesamiento: " + (finTotal - inicioTotal) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
