package multipleImagen.paralela.porImagenes;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.concurrent.atomic.AtomicInteger;

public class ImagenesParalelaPorImagen {

    public static void ejecutar() {
        try {

            String rutaBase = System.getProperty("user.dir");

            File carpetaEntrada = new File(rutaBase + File.separator + "imagenes");
            File carpetaSalida  = new File(rutaBase + File.separator + "imagenes_grises_concurrente");
            carpetaSalida.mkdirs();

            File[] archivos = carpetaEntrada.listFiles((d, n) ->
                n.toLowerCase().endsWith(".png") ||
                n.toLowerCase().endsWith(".jpg") ||
                n.toLowerCase().endsWith(".jpeg")
            );

            if (archivos == null || archivos.length == 0) {
                System.out.println("No hay imágenes.");
                return;
            }

            int numeroHilos = Runtime.getRuntime().availableProcessors();
            Thread[] hilos = new Thread[numeroHilos];

            AtomicInteger indice = new AtomicInteger(0);

            long inicioTotal = System.nanoTime();

            for (int i = 0; i < numeroHilos; i++) {
                hilos[i] = new Thread(() -> {

                    int idx;

                    while ((idx = indice.getAndIncrement()) < archivos.length) {

                        File archivo = archivos[idx];

                        try {
                            BufferedImage img = ImageIO.read(archivo);

                            long ti = System.nanoTime();

                            new FiltroGrisCompleta(img).run();

                            long tf = System.nanoTime();

                            String salidaNombre = archivo.getName().replace(".", "_gris.");
                            ImageIO.write(img, "png", new File(carpetaSalida, salidaNombre));

                            System.out.println("Tiempo por imagen: " + (tf - ti) / 1_000_000 + " ms");

                        } catch (Exception e) {
                            System.out.println("Error procesando " + archivo.getName());
                        }
                    }

                });

                hilos[i].start();
            }

            for (Thread h : hilos) h.join();

            long finTotal = System.nanoTime();
            System.out.println("Tiempo TOTAL: " + (finTotal - inicioTotal) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
