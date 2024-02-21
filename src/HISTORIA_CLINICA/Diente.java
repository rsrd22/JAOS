package HISTORIA_CLINICA;

import java.awt.Point;
import java.util.ArrayList;

public class Diente {

    public String id;
    public String titulo;
    public boolean presionadoCI;
    public boolean presionadoCD;
    public int secciones[];
    public String nombreXSeccion[];
    public Convencion[] convencionXSecciones;
    public int ancho;
    public int alto;
    public int x;
    public int y;
    public int ei;
    public int aac;
    public int parteSeleccionada, mp, mn;
    public ArrayList<Convencion> convenciones;
    public int tconv;
    public Point[] lineaV;
    public Point[] lineaH;
    public menuDeOpciones mopc;
    public boolean esPorSecciones;

    public Diente(String id, int x, int y, int ancho) {
        this.id = id;
        this.ancho = ancho;
        this.x = x;
        this.y = y;
        mopc = new menuDeOpciones();
        alto = ancho;
        aac = Math.round(ancho / 2);
        ei = Math.round((ancho - aac) / 2);
        parteSeleccionada = -1;//va del 0-4
        mp = 1;
        mn = -1;
        presionadoCI = false;
        presionadoCD = false;
        convenciones = new ArrayList<Convencion>();
        lineaV = new Point[2];
        lineaH = new Point[2];
        tconv = tiposDeConvencion.CV_DEFAULT;
        secciones = new int[]{0,0,0,0,0};//representa las cinco secciones del diente (arriba, izquierda, abajo, derecha, centro)
        convencionXSecciones = new Convencion[]{null,null,null,null,null};//representa los colores de las cinco secciones del diente
        nombreXSeccion = new String[]{null,null,null,null,null};
        esPorSecciones = false;
    }

    public int[] getPolXUP() {
        return new int[]{x, x + ancho, x + ei + aac, x + ei};
    }

    public int[] getPolYUP() {
        return new int[]{y, y, y + ei, y + ei};
    }

    public int[] getPolXLEFT() {
        return new int[]{x, x + ei, x + ei, x};
    }

    public int[] getPolYLEFT() {
        return new int[]{y, y + ei, y + ei + aac, y + alto};
    }

    public int[] getPolXDOWN() {
        return new int[]{x + ei, x + ei + aac, x + ancho, x};
    }

    public int[] getPolYDOWN() {
        return new int[]{y + ei + aac, y + ei + aac, y + alto, y + alto};
    }

    public int[] getPolXRIGHT() {
        return new int[]{x + ancho, x + ancho, x + ei + aac, x + ei + aac};
    }

    public int[] getPolYRIGHT() {
        return new int[]{y, y + alto, y + ei + aac, y + ei};
    }

    public int[] getPolXCENTER() {
        return new int[]{x + ei, x + ei + aac, x + ei + aac, x + ei};
    }

    public int[] getPolYCENTER() {
        return new int[]{y + ei, y + ei, y + ei + aac, y + ei + aac};
    }
}
