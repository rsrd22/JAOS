package HISTORIA_CLINICA;

import java.awt.Color;

class Opcion {

    public String descripcion;
    public int id;
    public int x;
    public int y;
    public int ancho;
    public int alto;
    public Color fondo;

    public Opcion(String descripcion, int id, int x, int y, int ancho, int alto) {
        this.descripcion = descripcion;
        this.id = id;
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        fondo = new Color(255,255,255);
    }

    public Opcion() {
        descripcion = "";
        id = -1;
        x = -1;
        y = -1;
        ancho = 0;
        alto = 0;
        fondo = new Color(255,255,255);
    }
}
