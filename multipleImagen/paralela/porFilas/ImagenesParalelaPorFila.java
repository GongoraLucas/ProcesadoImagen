package multipleImagen.paralela.porFilas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

import filtros.gris.paralela.filas.FiltroGrisPorFila;

public class ImagenesParalelaPorFila {

    public static void ejecutar() {
        try {
            String rutaBase = System.getProperty("user.dir");

            File carpetaEntrada = new File(rutaBase + File.separator + "imagenes");
            File carpetaSalida = new File(rutaBase + File.separator + "imagenes_grises_concurrente");

            if (!carpetaSalida.exists()) {
                boolean ok = carpetaSalida.mkdirs();
                if (!ok) {
                    System.err.println("Advertencia: no se pudo crear la carpeta de salida: " + carpetaSalida.getAbsolutePath());
                }
            }

            File[] archivos = carpetaEntrada.listFiles((d, n) -> {
                String name = n.toLowerCase(Locale.ROOT);
                return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
            });

            if (archivos == null || archivos.length == 0) {
                System.out.println("No hay imágenes.");
                return;
            }

            long inicioTotal = System.nanoTime();

            for (File archivo : archivos) {
                BufferedImage imagen = ImageIO.read(archivo);
                if (imagen == null) continue;

                int alto = imagen.getHeight();
                int hilosDisponibles = Runtime.getRuntime().availableProcessors();

                Thread[] hilos = new Thread[hilosDisponibles];
                AtomicInteger filaActual = new AtomicInteger(0);

                long inicioImg = System.nanoTime();

                for (int i = 0; i < hilosDisponibles; i++) {
                    hilos[i] = new Thread(() -> {
                        int fila;
                        while ((fila = filaActual.getAndIncrement()) < alto) {
                            new FiltroGrisPorFila(imagen, fila, fila + 1).run();
                        }
                    });
                    hilos[i].start();
                }

                for (Thread h : hilos) h.join();

                long finImg = System.nanoTime();
                long tiempoMs = (finImg - inicioImg) / 1_000_000;
                System.out.println("Tiempo por imagen: " + tiempoMs + " ms");

                String nombreSalida = archivo.getName().replace(".", "_gris.");
                ImageIO.write(imagen, "png", new File(carpetaSalida, nombreSalida));
            }

            long finTotal = System.nanoTime();
            System.out.println("Tiempo TOTAL: " + (finTotal - inicioTotal) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
