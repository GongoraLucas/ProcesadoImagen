package unaImagen.secuencial;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImagenSecuencial {

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

            int ancho = imagen.getWidth();
            int alto  = imagen.getHeight();

            System.out.println("Procesando imagen de " + ancho + "x" + alto);

            long inicio = System.nanoTime();

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {

                    int pixel = imagen.getRGB(x, y);

                    int alpha = (pixel >> 24) & 0xff;
                    int red   = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8)  & 0xff;
                    int blue  =  pixel        & 0xff;

                    int gris = (red + green + blue) / 3;

                    int nuevoPixel =
                        (alpha << 24) | (gris << 16) | (gris << 8) | gris;

                    imagen.setRGB(x, y, nuevoPixel);
                }
            }

            long fin = System.nanoTime();

            File carpetaSalida = new File(rutaBase + File.separator + "imagenes_grises_secuencial");
            carpetaSalida.mkdirs();

            File archivoSalida = new File(carpetaSalida, "imagen_gris_secuencial.png");
            ImageIO.write(imagen, "png", archivoSalida);

            System.out.println("Tiempo total: " + (fin - inicio) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
