package multipleImagen.paralela.porFilas;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImagenesParalelaPorFila {

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

            long inicioTotal = System.nanoTime();

            // ------------ variables para suma y conteo ----------
            long sumaTiempos = 0;
            int cantidadImagenes = 0;

            for (File archivo : archivos) {

                BufferedImage imagen = ImageIO.read(archivo);
                if (imagen == null) continue;

                int alto = imagen.getHeight();
                Thread[] hilos = new Thread[alto];

                long inicioImg = System.nanoTime();

                for (int fila = 0; fila < alto; fila++) {
                    hilos[fila] = new Thread(new FiltroGris(imagen, fila, fila + 1));
                    hilos[fila].start();
                }

                for (Thread h : hilos) h.join();

                long finImg = System.nanoTime();

                long tiempoMs = (finImg - inicioImg) / 1_000_000;
                System.out.println("Tiempo por imagen: " + tiempoMs + " ms");

                sumaTiempos += tiempoMs;
                cantidadImagenes++;

                String nombreSalida = archivo.getName().replace(".", "_gris.");
                ImageIO.write(imagen, "png", new File(carpetaSalida, nombreSalida));
            }

            long finTotal = System.nanoTime();
            System.out.println("Tiempo TOTAL: " + (finTotal - inicioTotal) / 1_000_000 + " ms");

            if (cantidadImagenes > 0) {
                double promedio = (double) sumaTiempos / cantidadImagenes;
                System.out.println("------------------------------------------");
                System.out.println("Suma de tiempos por imagen: " + sumaTiempos + " ms");
                System.out.println("Promedio por imagen: " + promedio + " ms");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}