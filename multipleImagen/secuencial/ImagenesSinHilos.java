import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImagenesSinHilos {

    public static void main(String[] args) {
        try {
            // Carpetas
            File carpetaEntrada = new File("../../imagenes");
            File carpetaSalida = new File("../../imagenes_grises_secuencial");

            // Crear carpeta de salida si no existe
            if (!carpetaSalida.exists()) {
                carpetaSalida.mkdirs();
            }

            // Listar archivos de imagen
            File[] archivos = carpetaEntrada.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png") ||
                    name.toLowerCase().endsWith(".jpg") ||
                    name.toLowerCase().endsWith(".jpeg")
            );

            if (archivos == null || archivos.length == 0) {
                System.out.println("No se encontraron imágenes en la carpeta 'imagenes'.");
                return;
            }

            System.out.println("Total de imágenes encontradas: " + archivos.length);

            long inicioTotal = System.nanoTime();

            for (File archivoEntrada : archivos) {

                System.out.println("Procesando: " + archivoEntrada.getName());

                BufferedImage imagen = ImageIO.read(archivoEntrada);

                if (imagen == null) {
                    System.out.println("No se pudo cargar la imagen " + archivoEntrada.getName());
                    continue;
                }

                int ancho = imagen.getWidth();
                int alto = imagen.getHeight();

                long inicio = System.nanoTime();

                // Procesamiento en escala de grises
                for (int y = 0; y < alto; y++) {
                    for (int x = 0; x < ancho; x++) {
                        int pixel = imagen.getRGB(x, y);

                        int alpha = (pixel >> 24) & 0xff;
                        int red = (pixel >> 16) & 0xff;
                        int green = (pixel >> 8) & 0xff;
                        int blue = pixel & 0xff;

                        int gris = (red + green + blue) / 3;

                        int nuevoPixel = (alpha << 24) | (gris << 16) | (gris << 8) | gris;
                        imagen.setRGB(x, y, nuevoPixel);
                    }
                }

                long fin = System.nanoTime();

                // Guardar con mismo nombre + "_gris"
                String nombreSalida = archivoEntrada.getName().replace(".", "_gris.");
                File archivoSalida = new File(carpetaSalida, nombreSalida);

                ImageIO.write(imagen, "png", archivoSalida);

                System.out.println("Guardado en: " + archivoSalida.getPath());
                System.out.println("Tiempo por imagen: " + (fin - inicio) / 1_000_000 + " ms\n");
            }

            long finTotal = System.nanoTime();
            System.out.println("Tiempo total del procesamiento: " + (finTotal - inicioTotal) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
