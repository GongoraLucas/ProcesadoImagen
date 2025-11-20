package multipleImagen.paralela.porImagenes;


import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.concurrent.atomic.AtomicInteger;

public class ImagenesParalelaPorImagen {

    public static void ejecutar() {
        try {
            File carpetaEntrada = new File("C:\\Users\\Lucas\\Desktop\\paralela\\java\\Imagenes\\PDI-concurrente\\imagenes");
            File carpetaSalida = new File("C:\\Users\\Lucas\\Desktop\\paralela\\java\\Imagenes\\PDI-concurrente\\imagenes_grises_concurrente");

            if (!carpetaSalida.exists()) carpetaSalida.mkdirs();

            File[] archivos = carpetaEntrada.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png") ||
                    name.toLowerCase().endsWith(".jpg") ||
                    name.toLowerCase().endsWith(".jpeg")
            );

            if (archivos == null || archivos.length == 0) {
                System.out.println("No hay imágenes para procesar.");
                return;
            }

            int numeroHilos = Runtime.getRuntime().availableProcessors();
            Thread[] hilos = new Thread[numeroHilos];

            AtomicInteger indiceGlobal = new AtomicInteger(0);

            long inicio = System.nanoTime();

            for (int i = 0; i < numeroHilos; i++) {
                hilos[i] = new Thread(() -> {
                    int index;
                    
                    while ((index = indiceGlobal.getAndIncrement()) < archivos.length) {
                        File archivoEntrada = archivos[index];
                        
                        try {
                            BufferedImage imagen = ImageIO.read(archivoEntrada);
                            
                            long inicioHilo = System.nanoTime();

                            new FiltroGrisCompleta(imagen).run();

                            String nombreSalida = archivoEntrada.getName().replace(".", "_gris.");
                            File salida = new File(carpetaSalida, nombreSalida);

                            ImageIO.write(imagen, "png", salida);

                            System.out.println("Procesada: " + archivoEntrada.getName());

                            long finHilo = System.nanoTime();
                            System.out.println("Tiempo por imagen: " + (finHilo - inicioHilo) / 1_000_000 + " ms\n");

                        } catch (Exception e) {
                            System.out.println("Error procesando " + archivoEntrada.getName());
                        }
                    }
                });

                hilos[i].start();
            }

            for (Thread hilo : hilos) {
                hilo.join();
            }

            long fin = System.nanoTime();

            System.out.println("\nProcesamiento terminado.");
            System.out.println("Tiempo total: " + (fin - inicio) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

