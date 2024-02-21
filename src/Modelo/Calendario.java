/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author richard
 */
public class Calendario {
    public String anio;
    public String mes;
    public String dia;
    public int ancho;
    public int alto;
    public int x;
    public int y;
    public boolean press;

    public Calendario(String anio, String mes, String dia, int ancho, int alto, int x, int y) {
        this.anio = anio;
        this.mes = mes;
        this.dia = dia;
        this.ancho = ancho;
        this.alto = alto;
        this.x = x;
        this.y = y;
        this.press = false;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean getPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
    }
    
    
    
    
    
}
