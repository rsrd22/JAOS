/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Modelo;

/**
 *
 * @author AT-DESARROLLO
 */
public class Paciente {
    public String pk_paciente;
    public String costo;
    public String tipo;

    public Paciente(String pk_paciente, String costo, String tipo) {
        this.pk_paciente = pk_paciente;
        this.costo = costo;
        this.tipo = tipo;
    }

    public String getPk_paciente() {
        return pk_paciente;
    }

    public void setPk_paciente(String pk_paciente) {
        this.pk_paciente = pk_paciente;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
