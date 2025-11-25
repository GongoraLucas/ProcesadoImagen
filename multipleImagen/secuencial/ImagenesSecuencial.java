package multipleImagen.secuencial;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImagenesSecuencial {

    public static void ejecutar() {
        try {

            String rutaBase = System.getProperty("user.dir");

            File carpetaEntrada = new File(rutaBase + File.separator + "imagenes");
            File carpetaSalida = new File(rutaBase + File.separator + "imagenes_grises_secuencial");
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

            for (File archivo : archivos) {

                BufferedImage imagen = ImageIO.read(archivo);
                if (imagen == null) continue;

                long inicio = System.nanoTime();

                int ancho = imagen.getWidth();
                int alto  = imagen.getHeight();

                for (int y = 0; y < alto; y++) {
                    for (int x = 0; x < ancho; x++) {

                        int pixel = imagen.getRGB(x, y);

                        int a = (pixel >> 24) & 0xff;
                        int r = (pixel >> 16) & 0xff;
                        int g = (pixel >> 8)  & 0xff;
                        int b =  pixel        & 0xff;

                        int gris = (r + g + b) / 3;
                        int nuevo = (a << 24) | (gris << 16) | (gris << 8) | gris;

                        imagen.setRGB(x, y, nuevo);
                    }
                }

                long fin = System.nanoTime();
                long tiempoMs = (fin - inicio) / 1_000_000;

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
