
package filtros.arcoiris;

import java.awt.image.BufferedImage;

/**
 *
 * @author Lucas Gongora
 * @author Klever Jami
 * @author Juan Medina
 * @author Evelyn Villareal
 */

public class FiltroArcoirisPorFila implements Runnable {
    private final BufferedImage imagen;
    private final int inicioFila;
    private final int finFila;

    public FiltroArcoirisPorFila(BufferedImage imagen, int inicioFila, int finFila) {
        this.imagen = imagen;
        this.inicioFila = inicioFila;
        this.finFila = finFila;
    }

    @Override
    public void run() {
        for (int y = inicioFila; y < finFila; y++) {
            for (int x = 0; x < imagen.getWidth(); x++) {
                int pixel = imagen.getRGB(x, y);

                int rojo = (pixel >> 16) & 0xFF;
                int verde = (pixel >> 8) & 0xFF;
                int azul = pixel & 0xFF;

                float brillo = (rojo + verde + azul) / (3.0f * 255.0f);

                // POSICIÓN DIAGONAL
                float posicion = ((float) x / imagen.getWidth() +
                        (float) y / imagen.getHeight()) / 2.0f;

                // Repetir el arcoíris si hace falta
                posicion = posicion % 1.0f;

                int[] rgb = hsvToRgb(posicion, 1.0f, brillo);

                int nuevoPixel = (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
                imagen.setRGB(x, y, nuevoPixel);
            }
        }
    }

    private int[] hsvToRgb(float h, float s, float v) {
        int[] rgb = new int[3];

        float c = v * s; // Chroma: cuánta ‘coloridad’ tiene el color
        float x = c * (1 - Math.abs((h * 6) % 2 - 1));
        float m = v - c; // ‘m’ ajusta el brillo (Value)

        float r, g, b;

        if (h < 1.0f / 6.0f) {
            r = c;
            g = x;
            b = 0;
        } else if (h < 2.0f / 6.0f) {
            r = x;
            g = c;
            b = 0;
        } else if (h < 3.0f / 6.0f) {
            r = 0;
            g = c;
            b = x;
        } else if (h < 4.0f / 6.0f) {
            r = 0;
            g = x;
            b = c;
        } else if (h < 5.0f / 6.0f) {
            r = x;
            g = 0;
            b = c;
        } else {
            r = c;
            g = 0;
            b = x;
        }

        // Ajustar con ‘m’ para incluir el brillo y convertir a rango [0-255]
        rgb[0] = Math.max(0, Math.min(255, (int) ((r + m) * 255)));
        rgb[1] = Math.max(0, Math.min(255, (int) ((g + m) * 255)));
        rgb[2] = Math.max(0, Math.min(255, (int) ((b + m) * 255)));

        return rgb;
    }

}
