package unaImagen.paralela;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.concurrent.atomic.AtomicInteger;

import filtros.gris.paralela.filas.FiltroGrisPorFila;

public class ImagenParalela {

    public static void ejecutar() {
        try {
            String rutaBase = System.getProperty("user.dir");

            File archivoEntrada = new File(
                rutaBase + File.separator + "imagenes" + File.separator + "aki-hayakawa-de-chainsaw-man-13585.jpg"
            );

            BufferedImage imagen = ImageIO.read(archivoEntrada);

            if (imagen == null) {
                System.out.println("No se pudo cargar la imagen.");
                return;
            }

            int alto = imagen.getHeight();

    
            int numeroHilos = Runtime.getRuntime().availableProcessors();
            Thread[] hilos = new Thread[numeroHilos];

            AtomicInteger filaActual = new AtomicInteger(0);


            long inicio = System.nanoTime();

            for (int i = 0; i < numeroHilos; i++) {
                hilos[i] = new Thread(() -> {

                    int fila;

                    while ((fila = filaActual.getAndIncrement()) < alto) {
                    
                        new FiltroGrisPorFila(imagen, fila, fila + 1).run();
                    }

                });

                hilos[i].start();
            }

            for (Thread h : hilos) h.join();

            long fin = System.nanoTime();

            File carpetaSalida = new File(rutaBase + File.separator + "imagenes_grises_concurrente");
            carpetaSalida.mkdirs();

            File archivoSalida = new File(carpetaSalida, "imagen_gris_paralela.png");
            ImageIO.write(imagen, "png", archivoSalida);

            System.out.println("Tiempo total: " + (fin - inicio) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
