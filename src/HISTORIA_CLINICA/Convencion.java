package HISTORIA_CLINICA;

import java.awt.Color;
import java.awt.Point;

class Convencion {

    public int x, y, ANCHO, ALTO, id, tipoDeConvencion;
    public Color color;
    public Color fondo;
    public Color borde;
    public boolean press;
    public boolean over; 
    public String desc;
    public Point pMenEmer;

    public Convencion(int id, String descripcion, int tipoConv, int x, int y, int ANCHO, Color color) {
        this.x = x;
        this.y = y;
        this.ANCHO = ANCHO;
        ALTO = ANCHO;
        this.id = id;
        this.color = color;
        fondo = new Color(240, 240, 240);
        borde = new Color(200, 200, 200);
        tipoDeConvencion = tipoConv;
        press = false;
        over = false;
        desc = descripcion;
        pMenEmer = new Point(0, 0);
    }
    
    public Convencion(Convencion c) {
        x = c.x;
        y = c.y;
        ANCHO = c.ANCHO;
        ALTO = c.ANCHO;
        id = c.id;
        color = c.color;
        fondo = new Color(240, 240, 240);
        borde = new Color(200, 200, 200);
        tipoDeConvencion = c.tipoDeConvencion;
        press = false;
        over = false;
        desc = c.desc;
        pMenEmer = new Point(0, 0);
    }

    public Convencion() {
        x = 0;
        y = 0;
        ANCHO = 0;
        ALTO = 0;
        id = -1;
        color = null;
        fondo = new Color(240, 240, 240);
        borde = new Color(200, 200, 200);
        tipoDeConvencion = -1;
        press = false;
        over = false;
        desc = "";
        pMenEmer = new Point(0, 0);
    }
}
