/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HISTORIA_CLINICA;

import java.awt.Color;

/**
 *
 * @author User
 */
class tiposDeImagenes {

    public int x;
    public int y;
    public int ancho;
    public int alto;
    public Color fondo;
    public Color colorsel;
    public String desc;
    public boolean sel;

    public tiposDeImagenes(int x, int y, int ancho, int alto, Color fondo, String desc) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.fondo = fondo;
        this.desc = desc;
        sel = false;
        colorsel = new Color(15,48,69);
    }
}
