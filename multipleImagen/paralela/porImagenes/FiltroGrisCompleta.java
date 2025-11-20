import java.awt.image.BufferedImage;

public class FiltroGrisCompleta implements Runnable {

    private final BufferedImage imagen;

    public FiltroGrisCompleta(BufferedImage imagen) {
        this.imagen = imagen;
    }

    @Override
    public void run() {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {

                int pixel = imagen.getRGB(x, y);

                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                int gris = (r + g + b) / 3;

                int nuevo = (gris << 16) | (gris << 8) | gris;

                imagen.setRGB(x, y, nuevo);
            }
        }
    }
}
