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
public class Cita {
    public String id;
    public String nom_pac;
    public String id_cita;
    public String fecha;
    public String hora;
    public String estado;

    public Cita(String id, String id_cita, String fecha, String hora, String estado) {
        this.id = id;
        this.id_cita = id_cita;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }
    public Cita(String id, String nom_pac, String id_cita, String fecha, String hora, String estado) {
        this.id = id;
        this.nom_pac = nom_pac;
        this.id_cita = id_cita;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public String getNom_pac() {
        return nom_pac;
    }

    public void setNom_pac(String nom_pac) {
        this.nom_pac = nom_pac;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_cita() {
        return id_cita;
    }

    public void setId_cita(String id_cita) {
        this.id_cita = id_cita;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}

